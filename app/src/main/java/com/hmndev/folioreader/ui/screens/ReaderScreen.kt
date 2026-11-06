package com.hmndev.folioreader.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.hmndev.folioreader.core.FolioDocument
import com.hmndev.folioreader.core.ReaderTypographyConfig
import com.hmndev.folioreader.core.ReadingLayoutMode
import com.hmndev.folioreader.core.ReadingThemeMode
import com.hmndev.folioreader.ui.components.FloatingReaderPill
import com.hmndev.folioreader.ui.components.FormulaInspectorSheet
import com.hmndev.folioreader.ui.components.LatexMarkdownViewer
import com.hmndev.folioreader.ui.components.OutlineBottomSheet
import com.hmndev.folioreader.ui.components.ReaderTopBar
import com.hmndev.folioreader.ui.components.ReadingProgressIndicator
import com.hmndev.folioreader.ui.components.ReadingProgressPill
import com.hmndev.folioreader.ui.components.ThemeSelectorSheet
import com.hmndev.folioreader.ui.components.TypographySettingsSheet
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    document: FolioDocument,
    isControlsVisible: Boolean,
    readingProgress: Float,
    currentTheme: ReadingThemeMode,
    currentLayout: ReadingLayoutMode,
    typographyConfig: ReaderTypographyConfig,
    pagerPages: List<String>,
    currentPagerIndex: Int,
    onBackClick: () -> Unit,
    onToggleControls: () -> Unit,
    onProgressChange: (Float) -> Unit,
    onPagerIndexChange: (Int) -> Unit,
    onThemeSelect: (ReadingThemeMode) -> Unit,
    onToggleLayout: () -> Unit,
    onTypographyChange: (ReaderTypographyConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    var showOutlineSheet by remember { mutableStateOf(false) }
    var showFormulasSheet by remember { mutableStateOf(false) }
    var showTypographySheet by remember { mutableStateOf(false) }
    var showThemeSheet by remember { mutableStateOf(false) }

    val outlineSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val formulasSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val typographySheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val themeSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val pagerState = rememberPagerState(
        initialPage = currentPagerIndex,
        pageCount = { pagerPages.size.coerceAtLeast(1) }
    )

    LaunchedEffect(pagerState.currentPage) {
        onPagerIndexChange(pagerState.currentPage)
    }

    Scaffold(
        topBar = {
            ReaderTopBar(
                title = document.title,
                visible = isControlsVisible,
                onBackClick = onBackClick,
                onOutlineClick = { showOutlineSheet = true },
                onFormulasClick = { showFormulasSheet = true },
                onTypographyClick = { showTypographySheet = true },
                onThemeClick = { showThemeSheet = true }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Reader Viewport
            if (currentLayout == ReadingLayoutMode.CONTINUOUS_SCROLL) {
                // Continuous Flow Mode
                LatexMarkdownViewer(
                    markdownContent = document.content,
                    typographyConfig = typographyConfig,
                    onDocumentTapped = onToggleControls,
                    onScrollProgress = onProgressChange,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Tactile Pager Mode with 3D paper curl / scale animation
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { pageIndex ->
                    val pageContent = pagerPages.getOrElse(pageIndex) { document.content }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                val pageOffset = (
                                    (pagerState.currentPage - pageIndex) + pagerState.currentPageOffsetFraction
                                ).absoluteValue

                                // Material 3 spring-like tactile scaling
                                val scale = lerp(
                                    start = 0.92f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                )
                                scaleX = scale
                                scaleY = scale
                                alpha = lerp(
                                    start = 0.5f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                )
                            }
                    ) {
                        LatexMarkdownViewer(
                            markdownContent = pageContent,
                            typographyConfig = typographyConfig,
                            onDocumentTapped = onToggleControls,
                            onScrollProgress = { /* pager handles progress */ },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // Top Reading Progress Bar (when controls visible)
            ReadingProgressIndicator(
                progress = readingProgress,
                visible = isControlsVisible,
                modifier = Modifier.align(Alignment.TopCenter)
            )

            // Discrete floating progress pill (when controls hidden)
            AnimatedVisibility(
                visible = !isControlsVisible,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                ReadingProgressPill(
                    progress = readingProgress,
                    readTimeMinutes = document.analysis.stats.readingTimeMinutes
                )
            }

            // Bottom Pager Controls (if Pager mode)
            if (currentLayout == ReadingLayoutMode.TACTILE_PAGER && isControlsVisible && pagerPages.size > 1) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 90.dp, start = 32.dp, end = 32.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.92f))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Page ${pagerState.currentPage + 1} of ${pagerPages.size}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Slider(
                            value = pagerState.currentPage.toFloat(),
                            onValueChange = { targetPage ->
                                coroutineScope.launch {
                                    pagerState.scrollToPage(targetPage.toInt())
                                }
                            },
                            valueRange = 0f..(pagerPages.size - 1).toFloat(),
                            steps = (pagerPages.size - 2).coerceAtLeast(0),
                            modifier = Modifier.fillMaxWidth(0.6f)
                        )
                    }
                }
            }

            // Floating Bottom Pill Dock
            FloatingReaderPill(
                visible = isControlsVisible,
                onOutlineClick = { showOutlineSheet = true },
                onFormulasClick = { showFormulasSheet = true },
                onTypographyClick = { showTypographySheet = true },
                onThemeClick = { showThemeSheet = true },
                onLayoutToggle = onToggleLayout,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }

    // Modal Bottom Sheets
    if (showOutlineSheet) {
        OutlineBottomSheet(
            sheetState = outlineSheetState,
            headings = document.analysis.headings,
            onHeadingClick = { heading ->
                // Jump to heading in pager if relevant
                val targetIndex = (heading.lineNumber / 40).coerceIn(0, (pagerPages.size - 1).coerceAtLeast(0))
                coroutineScope.launch {
                    pagerState.animateScrollToPage(targetIndex)
                }
            },
            onDismissRequest = { showOutlineSheet = false }
        )
    }

    if (showFormulasSheet) {
        FormulaInspectorSheet(
            sheetState = formulasSheetState,
            formulas = document.analysis.formulas,
            onDismissRequest = { showFormulasSheet = false }
        )
    }

    if (showTypographySheet) {
        TypographySettingsSheet(
            sheetState = typographySheetState,
            config = typographyConfig,
            onConfigChange = onTypographyChange,
            onDismissRequest = { showTypographySheet = false }
        )
    }

    if (showThemeSheet) {
        ThemeSelectorSheet(
            sheetState = themeSheetState,
            currentTheme = currentTheme,
            onThemeSelect = onThemeSelect,
            onDismissRequest = { showThemeSheet = false }
        )
    }
}
