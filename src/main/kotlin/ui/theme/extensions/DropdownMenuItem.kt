package ui.theme.extensions

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.runtime.Composable

@Composable
fun MenuItemColors.custom(darkTheme: Boolean = isSystemInDarkTheme()): MenuItemColors {
    return if(darkTheme) {
        MenuDefaults.itemColors().copy(
            textColor = MaterialTheme.colorScheme.inverseSurface
        )
    }
    else {
        MenuDefaults.itemColors().copy(
            textColor = MaterialTheme.colorScheme.inverseSurface,
        )
    }
}