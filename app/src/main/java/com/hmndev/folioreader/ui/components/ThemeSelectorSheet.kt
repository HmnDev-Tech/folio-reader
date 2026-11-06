package com.hmndev.folioreader.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hmndev.folioreader.core.ReadingThemeMode
import com.hmndev.folioreader.ui.theme.AmoledBg
import com.hmndev.folioreader.ui.theme.ObsidianBg
import com.hmndev.folioreader.ui.theme.ParchmentBg
import com.hmndev.folioreader.ui.theme.SepiaBg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSelectorSheet(
    sheetState: SheetState,
    currentTheme: ReadingThemeMode,
    onThemeSelect: (ReadingThemeMode) -> Unit,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Atmosphere & Themes",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(20.dp))

            ThemeItemRow(
                title = "Velum Parchment",
                subtitle = "Warm natural paper for daylight reading",
                previewBg = ParchmentBg,
                previewBorder = Color(0x33000000),
                isSelected = currentTheme == ReadingThemeMode.PARCHMENT,
                onClick = { onThemeSelect(ReadingThemeMode.PARCHMENT) }
            )

            ThemeItemRow(
                title = "Obsidian Manuscript",
                subtitle = "Deep slate contrast with electric accents",
                previewBg = ObsidianBg,
                previewBorder = Color(0x33FFFFFF),
                isSelected = currentTheme == ReadingThemeMode.OBSIDIAN,
                onClick = { onThemeSelect(ReadingThemeMode.OBSIDIAN) }
            )

            ThemeItemRow(
                title = "Sepia Solace",
                subtitle = "Golden hour warm tint for restful focus",
                previewBg = SepiaBg,
                previewBorder = Color(0x33000000),
                isSelected = currentTheme == ReadingThemeMode.SEPIA,
                onClick = { onThemeSelect(ReadingThemeMode.SEPIA) }
            )

            ThemeItemRow(
                title = "Pure AMOLED",
                subtitle = "True pitch black for maximum OLED battery savings",
                previewBg = AmoledBg,
                previewBorder = Color(0x33FFFFFF),
                isSelected = currentTheme == ReadingThemeMode.AMOLED,
                onClick = { onThemeSelect(ReadingThemeMode.AMOLED) }
            )

            ThemeItemRow(
                title = "Material 3 You Dynamic",
                subtitle = "Fluid palette adapted from device wallpaper",
                previewBg = MaterialTheme.colorScheme.primaryContainer,
                previewBorder = MaterialTheme.colorScheme.primary,
                isSelected = currentTheme == ReadingThemeMode.DYNAMIC_MONET,
                onClick = { onThemeSelect(ReadingThemeMode.DYNAMIC_MONET) }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ThemeItemRow(
    title: String,
    subtitle: String,
    previewBg: Color,
    previewBorder: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(previewBg)
                    .border(1.5.dp, previewBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = if (previewBg == AmoledBg || previewBg == ObsidianBg) Color.White else Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                )
            }
        }
    }
}
