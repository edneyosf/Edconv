package ui.components.extensions

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable

@Composable
fun TextFieldColors.custom(darkTheme: Boolean = isSystemInDarkTheme()): TextFieldColors {
    return if(darkTheme) {
        this.copy(
            unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceContainer,
            disabledIndicatorColor = MaterialTheme.colorScheme.surfaceContainer
        )
    }
    else {
        this.copy(
            unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceDim,
            disabledIndicatorColor = MaterialTheme.colorScheme.surfaceDim
        )
    }
}