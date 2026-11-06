package com.hmndev.folioreader.ui.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.hmndev.folioreader.core.DocumentAnalysis
import com.hmndev.folioreader.core.FolioDocument
import com.hmndev.folioreader.core.MarkdownLatexProcessor
import com.hmndev.folioreader.core.ReaderTypographyConfig
import com.hmndev.folioreader.core.ReadingLayoutMode
import com.hmndev.folioreader.core.ReadingThemeMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.UUID

data class ReaderUiState(
    val shelf: List<FolioDocument> = emptyList(),
    val currentDocument: FolioDocument? = null,
    val currentTheme: ReadingThemeMode = ReadingThemeMode.PARCHMENT,
    val currentLayout: ReadingLayoutMode = ReadingLayoutMode.CONTINUOUS_SCROLL,
    val typographyConfig: ReaderTypographyConfig = ReaderTypographyConfig(),
    val isControlsVisible: Boolean = true,
    val readingProgress: Float = 0f,
    val pagerPages: List<String> = emptyList(),
    val currentPagerIndex: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ReaderViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(ReaderUiState())
    val uiState: StateFlow<ReaderUiState> = _uiState.asStateFlow()

    init {
        loadPrepackagedSampleDocuments()
    }

    private fun loadPrepackagedSampleDocuments() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val sampleDocs = mutableListOf<FolioDocument>()
            val assetNames = listOf(
                "quantum_computing.md",
                "riemann_hypothesis.md",
                "material_design_guide.md"
            )

            withContext(Dispatchers.IO) {
                val assetManager = getApplication<Application>().assets
                for (name in assetNames) {
                    try {
                        val inputStream = assetManager.open("sample_docs/$name")
                        val reader = BufferedReader(InputStreamReader(inputStream))
                        val content = reader.readText()
                        reader.close()

                        val analysis = MarkdownLatexProcessor.analyzeDocument(content)
                        sampleDocs.add(
                            FolioDocument(
                                id = UUID.randomUUID().toString(),
                                title = analysis.title,
                                content = content,
                                sourcePath = "assets://sample_docs/$name",
                                analysis = analysis
                            )
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            _uiState.update { state ->
                if (state.shelf.isEmpty()) {
                    state.copy(shelf = sampleDocs, isLoading = false)
                } else {
                    state.copy(isLoading = false)
                }
            }
        }
    }

    fun openDocument(document: FolioDocument) {
        val pages = splitIntoPages(document.content)
        _uiState.value = _uiState.value.copy(
            currentDocument = document,
            pagerPages = pages,
            currentPagerIndex = 0,
            readingProgress = document.lastReadPosition,
            isControlsVisible = true
        )
    }

    fun closeDocument() {
        _uiState.value = _uiState.value.copy(
            currentDocument = null,
            pagerPages = emptyList()
        )
    }

    fun importDocumentFromUri(uri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val content = withContext(Dispatchers.IO) {
                    val cr = getApplication<Application>().contentResolver
                    cr.openInputStream(uri)?.use { stream ->
                        BufferedReader(InputStreamReader(stream)).readText()
                    } ?: ""
                }

                if (content.isNotBlank()) {
                    val analysis = withContext(Dispatchers.IO) {
                        MarkdownLatexProcessor.analyzeDocument(content)
                    }
                    val newDoc = FolioDocument(
                        id = UUID.randomUUID().toString(),
                        title = analysis.title,
                        content = content,
                        sourcePath = uri.toString(),
                        analysis = analysis
                    )

                    val updatedShelf = listOf(newDoc) + _uiState.value.shelf
                    _uiState.value = _uiState.value.copy(
                        shelf = updatedShelf,
                        isLoading = false
                    )
                    openDocument(newDoc)
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Selected file is empty."
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to import file: ${e.localizedMessage}"
                )
            }
        }
    }

    fun toggleControlsVisibility() {
        _uiState.value = _uiState.value.copy(
            isControlsVisible = !_uiState.value.isControlsVisible
        )
    }

    fun updateReadingProgress(progress: Float) {
        _uiState.value = _uiState.value.copy(readingProgress = progress)
    }

    fun setPagerIndex(index: Int) {
        val total = _uiState.value.pagerPages.size
        val progress = if (total > 1) index.toFloat() / (total - 1) else 1f
        _uiState.value = _uiState.value.copy(
            currentPagerIndex = index,
            readingProgress = progress
        )
    }

    fun setTheme(theme: ReadingThemeMode) {
        _uiState.value = _uiState.value.copy(currentTheme = theme)
    }

    fun toggleLayoutMode() {
        val next = if (_uiState.value.currentLayout == ReadingLayoutMode.CONTINUOUS_SCROLL) {
            ReadingLayoutMode.TACTILE_PAGER
        } else {
            ReadingLayoutMode.CONTINUOUS_SCROLL
        }
        _uiState.value = _uiState.value.copy(currentLayout = next)
    }

    fun updateTypography(config: ReaderTypographyConfig) {
        _uiState.value = _uiState.value.copy(typographyConfig = config)
    }

    private fun splitIntoPages(content: String): List<String> {
        // Break into pages on horizontal rule or section headings
        val sections = content.split(Regex("(?m)^(?=#{1,2}\\s|---)"))
            .filter { it.trim().isNotBlank() }
        return if (sections.size > 1) {
            sections
        } else {
            // Split by paragraph clusters
            content.split("\n\n\n").filter { it.isNotBlank() }.ifEmpty { listOf(content) }
        }
    }
}
