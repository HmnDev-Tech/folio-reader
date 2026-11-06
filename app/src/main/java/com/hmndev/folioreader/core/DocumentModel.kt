package com.hmndev.folioreader.core

import com.google.gson.annotations.SerializedName

data class HeadingItem(
    @SerializedName("level") val level: Int,
    @SerializedName("title") val title: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("line_number") val lineNumber: Int
)

data class LatexFormula(
    @SerializedName("raw") val raw: String,
    @SerializedName("is_display") val isDisplay: Boolean,
    @SerializedName("line_number") val lineNumber: Int,
    @SerializedName("preview") val preview: String
)

data class DocumentStats(
    @SerializedName("word_count") val wordCount: Int,
    @SerializedName("character_count") val characterCount: Int,
    @SerializedName("reading_time_minutes") val readingTimeMinutes: Int,
    @SerializedName("heading_count") val headingCount: Int,
    @SerializedName("formula_count") val formulaCount: Int,
    @SerializedName("display_formula_count") val displayFormulaCount: Int,
    @SerializedName("inline_formula_count") val inlineFormulaCount: Int,
    @SerializedName("code_block_count") val codeBlockCount: Int
)

data class DocumentAnalysis(
    @SerializedName("title") val title: String,
    @SerializedName("stats") val stats: DocumentStats,
    @SerializedName("headings") val headings: List<HeadingItem>,
    @SerializedName("formulas") val formulas: List<LatexFormula>
)

data class FolioDocument(
    val id: String,
    val title: String,
    val content: String,
    val sourcePath: String? = null,
    val analysis: DocumentAnalysis,
    val lastReadPosition: Float = 0f,
    val lastReadTimestamp: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false
)

enum class ReadingThemeMode {
    PARCHMENT,
    OBSIDIAN,
    SEPIA,
    AMOLED,
    DYNAMIC_MONET
}

enum class ReadingLayoutMode {
    CONTINUOUS_SCROLL,
    TACTILE_PAGER
}

data class ReaderTypographyConfig(
    val fontSizeSp: Int = 18,
    val lineHeightRatio: Float = 1.68f,
    val fontFamily: String = "Georgia, serif"
)
