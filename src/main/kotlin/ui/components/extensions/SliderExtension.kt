package ui.components.extensions

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.SliderColors
import androidx.compose.material.SliderDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun SliderDefaults.custom(darkTheme: Boolean = isSystemInDarkTheme()): SliderColors {
    return if(darkTheme) {
        colors(
            activeTrackColor = MaterialTheme.colorScheme.primary,
            thumbColor = MaterialTheme.colorScheme.primary,
            inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer
        )
    }
    else {
        colors(
            activeTrackColor = MaterialTheme.colorScheme.primary,
            thumbColor = MaterialTheme.colorScheme.primary,
            inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer
        )
    }
}