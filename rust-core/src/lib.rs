use jni::objects::{JClass, JString};
use jni::sys::jstring;
use jni::JNIEnv;
use regex::Regex;
use serde::{Deserialize, Serialize};

#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct HeadingItem {
    pub level: u32,
    pub title: String,
    pub slug: String,
    pub line_number: usize,
}

#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct LatexFormula {
    pub raw: String,
    pub is_display: bool,
    pub line_number: usize,
    pub preview: String,
}

#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct DocumentStats {
    pub word_count: usize,
    pub character_count: usize,
    pub reading_time_minutes: u32,
    pub heading_count: usize,
    pub formula_count: usize,
    pub display_formula_count: usize,
    pub inline_formula_count: usize,
    pub code_block_count: usize,
}

#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct DocumentAnalysis {
    pub title: String,
    pub stats: DocumentStats,
    pub headings: Vec<HeadingItem>,
    pub formulas: Vec<LatexFormula>,
}

pub struct MarkdownLatexEngine;

impl MarkdownLatexEngine {
    pub fn analyze(content: &str) -> DocumentAnalysis {
        let lines: Vec<&str> = content.lines().collect();
        let mut headings = Vec::new();
        let mut formulas = Vec::new();
        let mut word_count = 0;
        let mut code_block_count = 0;
        let mut in_code_block = false;

        let display_math_re = Regex::new(r"(?s)\$\$(.+?)\$\$").unwrap();
        let inline_math_re = Regex::new(r"\$([^\$\n]+?)\$").unwrap();
        let heading_re = Regex::new(r"^(#{1,6})\s+(.+)$").unwrap();

        // 1. Process line by line for structure
        for (idx, line) in lines.iter().enumerate() {
            let line_num = idx + 1;
            let trimmed = line.trim();

            if trimmed.starts_with("```") {
                in_code_block = !in_code_block;
                if in_code_block {
                    code_block_count += 1;
                }
                continue;
            }

            if in_code_block {
                continue;
            }

            // Headings
            if let Some(caps) = heading_re.captures(trimmed) {
                let hashes = caps.get(1).map_or("", |m| m.as_str());
                let text = caps.get(2).map_or("", |m| m.as_str()).trim();
                let level = hashes.len() as u32;
                let slug = text
                    .to_lowercase()
                    .replace(|c: char| !c.is_alphanumeric() && c != ' ', "")
                    .replace(' ', "-");

                headings.push(HeadingItem {
                    level,
                    title: text.to_string(),
                    slug,
                    line_number: line_num,
                });
            }

            // Simple word count
            word_count += trimmed.split_whitespace().count();
        }

        // 2. Extract Display Math Formulas $$ ... $$
        for (idx, line) in lines.iter().enumerate() {
            let line_num = idx + 1;
            for cap in display_math_re.captures_iter(line) {
                if let Some(m) = cap.get(1) {
                    let formula_text = m.as_str().trim().to_string();
                    let preview = if formula_text.len() > 60 {
                        format!("{}...", &formula_text[..60])
                    } else {
                        formula_text.clone()
                    };
                    formulas.push(LatexFormula {
                        raw: formula_text,
                        is_display: true,
                        line_number: line_num,
                        preview,
                    });
                }
            }
        }

        // Multi-line display math
        for cap in display_math_re.captures_iter(content) {
            if let Some(m) = cap.get(1) {
                let formula_text = m.as_str().trim().to_string();
                if !formulas.iter().any(|f| f.raw == formula_text && f.is_display) {
                    let preview = if formula_text.len() > 60 {
                        format!("{}...", &formula_text[..60])
                    } else {
                        formula_text.clone()
                    };
                    formulas.push(LatexFormula {
                        raw: formula_text,
                        is_display: true,
                        line_number: 1,
                        preview,
                    });
                }
            }
        }

        // 3. Extract Inline Math Formulas $ ... $ (excluding display math)
        let cleaned_without_display = display_math_re.replace_all(content, "");
        for (idx, line) in cleaned_without_display.lines().enumerate() {
            let line_num = idx + 1;
            for cap in inline_math_re.captures_iter(line) {
                if let Some(m) = cap.get(1) {
                    let formula_text = m.as_str().trim().to_string();
                    if !formula_text.is_empty() {
                        let preview = if formula_text.len() > 50 {
                            format!("{}...", &formula_text[..50])
                        } else {
                            formula_text.clone()
                        };
                        formulas.push(LatexFormula {
                            raw: formula_text,
                            is_display: false,
                            line_number: line_num,
                            preview,
                        });
                    }
                }
            }
        }

        // Document Title
        let title = headings
            .iter()
            .find(|h| h.level == 1)
            .map(|h| h.title.clone())
            .unwrap_or_else(|| {
                lines
                    .iter()
                    .find(|l| !l.trim().is_empty())
                    .map(|l| l.trim().to_string())
                    .unwrap_or_else(|| "Untitled Document".to_string())
            });

        let display_count = formulas.iter().filter(|f| f.is_display).count();
        let inline_count = formulas.iter().filter(|f| !f.is_display).count();
        let total_formula_count = formulas.len();

        // Reading time: 200 words per minute + 15 seconds per formula
        let raw_seconds = (word_count as f64 / 200.0 * 60.0) + (total_formula_count as f64 * 15.0);
        let reading_time_minutes = (raw_seconds / 60.0).ceil().max(1.0) as u32;

        let stats = DocumentStats {
            word_count,
            character_count: content.chars().count(),
            reading_time_minutes,
            heading_count: headings.len(),
            formula_count: total_formula_count,
            display_formula_count: display_count,
            inline_formula_count: inline_count,
            code_block_count,
        };

        DocumentAnalysis {
            title,
            stats,
            headings,
            formulas,
        }
    }

    /// Preprocesses markdown to protect LaTeX math blocks from markdown syntax collision
    pub fn preprocess_latex_markdown(content: &str) -> String {
        // Protect display equations by wrapping them in isolated div containers
        let display_re = Regex::new(r"(?s)\$\$\n*(.+?)\n*\$\$").unwrap();
        let safe_display = display_re.replace_all(content, |caps: &regex::Captures| {
            format!("\n<div class=\"katex-display-wrapper\">$${}$$</div>\n", &caps[1])
        });

        safe_display.to_string()
    }
}

// ================= JNI Bindings =================

#[no_mangle]
pub extern "system" fn Java_com_hmndev_folioreader_core_RustReaderCore_nativeAnalyzeDocument(
    mut env: JNIEnv,
    _class: JClass,
    input: JString,
) -> jstring {
    let input_str: String = match env.get_string(&input) {
        Ok(s) => s.into(),
        Err(_) => String::new(),
    };

    let analysis = MarkdownLatexEngine::analyze(&input_str);
    let json = serde_json::to_string(&analysis).unwrap_or_else(|_| "{}".to_string());

    let output = env.new_string(json).expect("Couldn't create java string");
    output.into_raw()
}

#[no_mangle]
pub extern "system" fn Java_com_hmndev_folioreader_core_RustReaderCore_nativePreprocessMarkdown(
    mut env: JNIEnv,
    _class: JClass,
    input: JString,
) -> jstring {
    let input_str: String = match env.get_string(&input) {
        Ok(s) => s.into(),
        Err(_) => String::new(),
    };

    let processed = MarkdownLatexEngine::preprocess_latex_markdown(&input_str);
    let output = env.new_string(processed).expect("Couldn't create java string");
    output.into_raw()
}

#[no_mangle]
pub extern "system" fn Java_com_hmndev_folioreader_core_RustReaderCore_nativeExtractFormulas(
    mut env: JNIEnv,
    _class: JClass,
    input: JString,
) -> jstring {
    let input_str: String = match env.get_string(&input) {
        Ok(s) => s.into(),
        Err(_) => String::new(),
    };

    let analysis = MarkdownLatexEngine::analyze(&input_str);
    let json = serde_json::to_string(&analysis.formulas).unwrap_or_else(|_| "[]".to_string());

    let output = env.new_string(json).expect("Couldn't create java string");
    output.into_raw()
}

#[no_mangle]
pub extern "system" fn Java_com_hmndev_folioreader_core_RustReaderCore_nativeVersion(
    env: JNIEnv,
    _class: JClass,
) -> jstring {
    let ver = "RustReaderCore 1.0.0 (Serde + Regex AST Engine)";
    let output = env.new_string(ver).expect("Couldn't create java string");
    output.into_raw()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_markdown_and_latex_analysis() {
        let md = r#"# Quantum Computing

The state vector is given by:
$$
|\psi\rangle = \alpha |0\rangle + \beta |1\rangle
$$
Where $|\alpha|^2 + |\beta|^2 = 1$.

## Bloch Sphere
A qubit on the sphere has coordinates:
$$\mathbf{r} = (\sin\theta\cos\phi, \sin\theta\sin\phi, \cos\theta)$$
"#;
        let analysis = MarkdownLatexEngine::analyze(md);
        assert_eq!(analysis.title, "Quantum Computing");
        assert_eq!(analysis.headings.len(), 2);
        assert_eq!(analysis.formulas.len(), 3);
        assert_eq!(analysis.stats.display_formula_count, 2);
        assert_eq!(analysis.stats.inline_formula_count, 1);
    }
}
