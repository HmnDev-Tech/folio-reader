package com.hmndev.folioreader.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderTopBar(
    title: String,
    visible: Boolean,
    onBackClick: () -> Unit,
    onOutlineClick: () -> Unit,
    onFormulasClick: () -> Unit,
    onTypographyClick: () -> Unit,
    onThemeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
        modifier = modifier
    ) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Shelf"
                    )
                }
            },
            actions = {
                IconButton(onClick = onOutlineClick) {
                    Icon(
                        imageVector = Icons.Default.FormatListBulleted,
                        contentDescription = "Table of Contents"
                    )
                }
                IconButton(onClick = onFormulasClick) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = "Formulas Inspector"
                    )
                }
                IconButton(onClick = onTypographyClick) {
                    Icon(
                        imageVector = Icons.Default.TextFields,
                        contentDescription = "Typography"
                    )
                }
                IconButton(onClick = onThemeClick) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = "Reading Theme"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
                titleContentColor = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}
