package edneyosf.edconv.ui.components.extensions

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Suppress("UnusedReceiverParameter")
@Composable
fun DividerDefaults.customColor(darkTheme: Boolean = isSystemInDarkTheme()): Color {
    return if(darkTheme) MaterialTheme.colorScheme.surfaceContainerHighest
    else MaterialTheme.colorScheme.surfaceContainerHighest
}