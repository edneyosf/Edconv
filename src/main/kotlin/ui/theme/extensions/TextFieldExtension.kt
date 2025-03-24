package ui.theme.extensions

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable

@Composable
fun TextFieldColors.custom(darkTheme: Boolean = isSystemInDarkTheme()): TextFieldColors {
    return if(darkTheme) {
        TextFieldDefaults.colors().copy(
            focusedTextColor = MaterialTheme.colorScheme.inverseSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.inverseSurface,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceContainer
        )
    }
    else {
        TextFieldDefaults.colors().copy(
            focusedTextColor = MaterialTheme.colorScheme.inverseSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.inverseSurface,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.surfaceDim
        )
    }
}
