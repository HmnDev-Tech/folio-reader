package com.hmndev.folioreader.core

import com.google.gson.Gson
import java.util.regex.Pattern

object MarkdownLatexProcessor {
    private val gson = Gson()

    fun analyzeDocument(markdown: String): DocumentAnalysis {
        if (RustReaderCore.isAvailable()) {
            try {
                val json = RustReaderCore.nativeAnalyzeDocument(markdown)
                val analysis = gson.fromJson(json, DocumentAnalysis::class.java)
                if (analysis != null) {
                    return analysis
                }
            } catch (t: Throwable) {
                // Fallback to Kotlin engine below
            }
        }
        return analyzeDocumentKotlinFallback(markdown)
    }

    fun preprocessMarkdown(markdown: String): String {
        if (RustReaderCore.isAvailable()) {
            try {
                return RustReaderCore.nativePreprocessMarkdown(markdown)
            } catch (t: Throwable) {
                // Fallback to Kotlin engine
            }
        }
        return preprocessMarkdownKotlinFallback(markdown)
    }

    private fun analyzeDocumentKotlinFallback(markdown: String): DocumentAnalysis {
        val lines = markdown.lines()
        val headings = mutableListOf<HeadingItem>()
        val formulas = mutableListOf<LatexFormula>()
        var wordCount = 0
        var codeBlockCount = 0
        var inCodeBlock = false

        val headingPattern = Pattern.compile("^(#{1,6})\\s+(.+)$")
        val displayMathPattern = Pattern.compile("(?s)\\$\\$(.+?)\\$\\$")
        val inlineMathPattern = Pattern.compile("\\$([^\\$\\n]+?)\\$")

        for ((idx, line) in lines.withIndex()) {
            val lineNum = idx + 1
            val trimmed = line.trim()

            if (trimmed.startsWith("```")) {
                inCodeBlock = !inCodeBlock
                if (inCodeBlock) codeBlockCount++
                continue
            }

            if (inCodeBlock) continue

            val headingMatcher = headingPattern.matcher(trimmed)
            if (headingMatcher.matches()) {
                val hashes = headingMatcher.group(1) ?: ""
                val text = headingMatcher.group(2)?.trim() ?: ""
                val slug = text.lowercase().replace(Regex("[^a-z0-9 ]"), "").replace(' ', '-')
                headings.add(
                    HeadingItem(
                        level = hashes.length,
                        title = text,
                        slug = slug,
                        lineNumber = lineNum
                    )
                )
            }

            wordCount += trimmed.split(Regex("\\s+")).filter { it.isNotBlank() }.size
        }

        // Display math extraction
        val displayMatcher = displayMathPattern.matcher(markdown)
        while (displayMatcher.find()) {
            val raw = displayMatcher.group(1)?.trim() ?: ""
            val preview = if (raw.length > 60) raw.take(60) + "..." else raw
            formulas.add(
                LatexFormula(
                    raw = raw,
                    isDisplay = true,
                    lineNumber = 1,
                    preview = preview
                )
            )
        }

        // Inline math extraction
        val cleaned = displayMathPattern.matcher(markdown).replaceAll("")
        val inlineMatcher = inlineMathPattern.matcher(cleaned)
        while (inlineMatcher.find()) {
            val raw = inlineMatcher.group(1)?.trim() ?: ""
            if (raw.isNotBlank()) {
                val preview = if (raw.length > 50) raw.take(50) + "..." else raw
                formulas.add(
                    LatexFormula(
                        raw = raw,
                        isDisplay = false,
                        lineNumber = 1,
                        preview = preview
                    )
                )
            }
        }

        val title = headings.firstOrNull { it.level == 1 }?.title
            ?: lines.firstOrNull { it.isNotBlank() }?.trim()
            ?: "Untitled Document"

        val displayCount = formulas.count { it.isDisplay }
        val inlineCount = formulas.count { !it.isDisplay }
        val totalFormulas = formulas.size

        val rawSeconds = (wordCount / 200.0 * 60.0) + (totalFormulas * 15.0)
        val readTimeMinutes = kotlin.math.max(1, kotlin.math.ceil(rawSeconds / 60.0).toInt())

        val stats = DocumentStats(
            wordCount = wordCount,
            characterCount = markdown.length,
            readingTimeMinutes = readTimeMinutes,
            headingCount = headings.size,
            formulaCount = totalFormulas,
            displayFormulaCount = displayCount,
            inlineFormulaCount = inlineCount,
            codeBlockCount = codeBlockCount
        )

        return DocumentAnalysis(
            title = title,
            stats = stats,
            headings = headings,
            formulas = formulas
        )
    }

    private fun preprocessMarkdownKotlinFallback(markdown: String): String {
        val displayPattern = Pattern.compile("(?s)\\$\\$\\n*(.+?)\\n*\\$\\$")
        val matcher = displayPattern.matcher(markdown)
        val sb = StringBuffer()
        while (matcher.find()) {
            val eq = matcher.group(1) ?: ""
            matcher.appendReplacement(sb, "\n<div class=\"katex-display-wrapper\">\\$\\$$eq\\$\\$</div>\n")
        }
        matcher.appendTail(sb)
        return sb.toString()
    }
}
