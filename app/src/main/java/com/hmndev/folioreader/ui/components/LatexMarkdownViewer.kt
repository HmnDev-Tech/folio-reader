package com.hmndev.folioreader.ui.components

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.hmndev.folioreader.core.ReaderTypographyConfig
import org.json.JSONObject

class FolioJsBridge(
    private val onDocumentTapped: () -> Unit,
    private val onScrollProgress: (Float) -> Unit
) {
    @JavascriptInterface
    fun onDocumentTapped() {
        onDocumentTapped.invoke()
    }

    @JavascriptInterface
    fun onScrollProgress(progress: Float) {
        onScrollProgress.invoke(progress)
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LatexMarkdownViewer(
    markdownContent: String,
    typographyConfig: ReaderTypographyConfig,
    onDocumentTapped: () -> Unit,
    onScrollProgress: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme

    val bgHex = String.format("#%06X", 0xFFFFFF and colorScheme.background.toArgb())
    val textHex = String.format("#%06X", 0xFFFFFF and colorScheme.onBackground.toArgb())
    val primaryHex = String.format("#%06X", 0xFFFFFF and colorScheme.primary.toArgb())
    val surfaceHex = String.format("#%06X", 0xFFFFFF and colorScheme.surface.toArgb())
    val borderHex = String.format("#%06X", 0xFFFFFF and colorScheme.outline.toArgb())

    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.allowFileAccess = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            setBackgroundColor(0) // Transparent to show native surface
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    updateContentAndTheme(
                        view,
                        markdownContent,
                        bgHex,
                        textHex,
                        primaryHex,
                        surfaceHex,
                        borderHex,
                        typographyConfig
                    )
                }
            }
            addJavascriptInterface(FolioJsBridge(onDocumentTapped, onScrollProgress), "FolioBridge")
            loadUrl("file:///android_asset/viewer_template.html")
        }
    }

    // Reactively update theme and typography when user changes settings
    LaunchedEffect(colorScheme, typographyConfig, markdownContent) {
        updateContentAndTheme(
            webView,
            markdownContent,
            bgHex,
            textHex,
            primaryHex,
            surfaceHex,
            borderHex,
            typographyConfig
        )
    }

    AndroidView(
        factory = { webView },
        modifier = modifier.fillMaxSize()
    )
}

private fun updateContentAndTheme(
    webView: WebView?,
    markdown: String,
    bgHex: String,
    textHex: String,
    primaryHex: String,
    surfaceHex: String,
    borderHex: String,
    typography: ReaderTypographyConfig
) {
    if (webView == null) return

    val jsonEscaped = JSONObject.quote(markdown)
    val script = """
        (function() {
            if (typeof updateTheme === 'function') {
                updateTheme(
                    '$bgHex',
                    '$textHex',
                    '$primaryHex',
                    '$surfaceHex',
                    '$surfaceHex',
                    '$borderHex',
                    '${typography.fontFamily}',
                    ${typography.fontSizeSp},
                    ${typography.lineHeightRatio}
                );
            }
            if (typeof setDocument === 'function') {
                setDocument($jsonEscaped);
            }
        })();
    """.trimIndent()
    webView.evaluateJavascript(script, null)
}
