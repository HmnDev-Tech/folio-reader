package com.hmndev.folioreader.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.hmndev.folioreader.core.ReadingThemeMode

private val ParchmentColorScheme = lightColorScheme(
    primary = ParchmentAccent,
    background = ParchmentBg,
    surface = ParchmentSurface,
    onPrimary = ParchmentBg,
    onBackground = ParchmentText,
    onSurface = ParchmentText,
    outline = ParchmentBorder
)

private val ObsidianColorScheme = darkColorScheme(
    primary = ObsidianAccent,
    background = ObsidianBg,
    surface = ObsidianSurface,
    onPrimary = ObsidianBg,
    onBackground = ObsidianText,
    onSurface = ObsidianText,
    outline = ObsidianBorder
)

private val SepiaColorScheme = lightColorScheme(
    primary = SepiaAccent,
    background = SepiaBg,
    surface = SepiaSurface,
    onPrimary = SepiaBg,
    onBackground = SepiaText,
    onSurface = SepiaText,
    outline = SepiaBorder
)

private val AmoledColorScheme = darkColorScheme(
    primary = AmoledAccent,
    background = AmoledBg,
    surface = AmoledSurface,
    onPrimary = AmoledBg,
    onBackground = AmoledText,
    onSurface = AmoledText,
    outline = AmoledBorder
)

@Composable
fun FolioReaderTheme(
    themeMode: ReadingThemeMode = ReadingThemeMode.PARCHMENT,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme: ColorScheme = when (themeMode) {
        ReadingThemeMode.PARCHMENT -> ParchmentColorScheme
        ReadingThemeMode.OBSIDIAN -> ObsidianColorScheme
        ReadingThemeMode.SEPIA -> SepiaColorScheme
        ReadingThemeMode.AMOLED -> AmoledColorScheme
        ReadingThemeMode.DYNAMIC_MONET -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            } else {
                if (darkTheme) ObsidianColorScheme else ParchmentColorScheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
