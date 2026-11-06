package com.hmndev.folioreader.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hmndev.folioreader.core.ReaderTypographyConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypographySettingsSheet(
    sheetState: SheetState,
    config: ReaderTypographyConfig,
    onConfigChange: (ReaderTypographyConfig) -> Unit,
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
                text = "Typography & Spacing",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Font Size
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Font Size",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${config.fontSizeSp} sp",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Slider(
                value = config.fontSizeSp.toFloat(),
                onValueChange = { onConfigChange(config.copy(fontSizeSp = it.toInt())) },
                valueRange = 14f..28f,
                steps = 13,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Line Height Ratio
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Line Height",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = String.format("%.2fx", config.lineHeightRatio),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Slider(
                value = config.lineHeightRatio,
                onValueChange = { onConfigChange(config.copy(lineHeightRatio = it)) },
                valueRange = 1.3f..2.2f,
                steps = 9,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Font Family
            Text(
                text = "Typeface",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilterChip(
                    selected = config.fontFamily.contains("serif"),
                    onClick = { onConfigChange(config.copy(fontFamily = "'Georgia', serif")) },
                    label = { Text("Serif") }
                )
                FilterChip(
                    selected = config.fontFamily.contains("sans-serif"),
                    onClick = { onConfigChange(config.copy(fontFamily = "system-ui, sans-serif")) },
                    label = { Text("Sans") }
                )
                FilterChip(
                    selected = config.fontFamily.contains("monospace"),
                    onClick = { onConfigChange(config.copy(fontFamily = "'Roboto Mono', monospace")) },
                    label = { Text("Mono") }
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
