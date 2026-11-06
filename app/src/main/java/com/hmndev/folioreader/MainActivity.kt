package com.hmndev.folioreader

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.hmndev.folioreader.ui.screens.ReaderScreen
import com.hmndev.folioreader.ui.screens.ShelfScreen
import com.hmndev.folioreader.ui.theme.FolioReaderTheme
import com.hmndev.folioreader.ui.viewmodel.ReaderViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: ReaderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        handleIntent(intent)

        setContent {
            val uiState by viewModel.uiState.collectAsState()

            FolioReaderTheme(themeMode = uiState.currentTheme) {
                FolioAppContent(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_VIEW) {
            val uri = intent.data
            if (uri != null) {
                viewModel.importDocumentFromUri(uri)
            }
        }
    }
}

@Composable
fun FolioAppContent(
    viewModel: ReaderViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    BackHandler(enabled = uiState.currentDocument != null) {
        viewModel.closeDocument()
    }

    Crossfade(
        targetState = uiState.currentDocument,
        label = "ScreenTransition",
        modifier = modifier
    ) { activeDoc ->
        if (activeDoc == null) {
            ShelfScreen(
                uiState = uiState,
                onDocumentClick = { doc -> viewModel.openDocument(doc) },
                onImportUri = { uri -> viewModel.importDocumentFromUri(uri) }
            )
        } else {
            ReaderScreen(
                document = activeDoc,
                isControlsVisible = uiState.isControlsVisible,
                readingProgress = uiState.readingProgress,
                currentTheme = uiState.currentTheme,
                currentLayout = uiState.currentLayout,
                typographyConfig = uiState.typographyConfig,
                pagerPages = uiState.pagerPages,
                currentPagerIndex = uiState.currentPagerIndex,
                onBackClick = { viewModel.closeDocument() },
                onToggleControls = { viewModel.toggleControlsVisibility() },
                onProgressChange = { p -> viewModel.updateReadingProgress(p) },
                onPagerIndexChange = { i -> viewModel.setPagerIndex(i) },
                onThemeSelect = { t -> viewModel.setTheme(t) },
                onToggleLayout = { viewModel.toggleLayoutMode() },
                onTypographyChange = { cfg -> viewModel.updateTypography(cfg) }
            )
        }
    }
}
