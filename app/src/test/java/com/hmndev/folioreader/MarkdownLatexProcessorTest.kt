package com.hmndev.folioreader

import com.hmndev.folioreader.core.MarkdownLatexProcessor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MarkdownLatexProcessorTest {

    @Test
    fun testDocumentAnalysisAndFormulaExtraction() {
        val markdown = """
            # Quantum Mechanics Basics
            
            Here is a famous equation:
            $$
            i\hbar \frac{\partial}{\partial t} \Psi = \hat{H}\Psi
            $$
            
            And an inline formula ${'$'}E = mc^2${'$'} inside text.
            
            ## Second Section
            Some additional paragraph text with another formula ${'$'}\vec{F} = m\vec{a}${'$'}.
        """.trimIndent()

        val analysis = MarkdownLatexProcessor.analyzeDocument(markdown)

        assertEquals("Quantum Mechanics Basics", analysis.title)
        assertEquals(2, analysis.headings.size)
        assertEquals("Quantum Mechanics Basics", analysis.headings[0].title)
        assertEquals(1, analysis.headings[0].level)
        assertEquals("Second Section", analysis.headings[1].title)
        assertEquals(2, analysis.headings[1].level)

        // Verify formulas
        assertEquals(3, analysis.formulas.size)
        assertEquals(1, analysis.stats.displayFormulaCount)
        assertEquals(2, analysis.stats.inlineFormulaCount)
        assertTrue(analysis.stats.wordCount > 10)
        assertTrue(analysis.stats.readingTimeMinutes >= 1)
    }

    @Test
    fun testPreprocessingLatexBlocks() {
        val markdown = """
            Text before
            $$
            \int_0^1 x^2 dx = \frac{1}{3}
            $$
            Text after
        """.trimIndent()

        val preprocessed = MarkdownLatexProcessor.preprocessMarkdown(markdown)
        assertTrue(preprocessed.contains("katex-display-wrapper"))
    }
}
