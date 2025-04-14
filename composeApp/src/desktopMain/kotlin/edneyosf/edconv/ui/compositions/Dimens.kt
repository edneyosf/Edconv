package edneyosf.edconv.ui.compositions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

inline val dimens: Dimens
    @Composable
    @ReadOnlyComposable
    get() = dimensComp.current

val dimensComp = compositionLocalOf { Dimens() }

data class Dimens(
    val none: Dp = 0.dp,    // No spacing
    val xxs: Dp = 4.dp,     // Extra, extra, small - tiny spacing
    val xs: Dp = 8.dp,      // Extra, small - minimum margin
    val sm: Dp = 12.dp,     // Small - light padding
    val md: Dp = 16.dp,     // Medium - default padding/margin
    val ml: Dp = 18.dp,     // Medium-large - small icons
    val lg: Dp = 20.dp,     // Large - regular icons
    val xl: Dp = 24.dp,     // Extra large - section separation
    val xxl: Dp = 32.dp,    // Extra, extra large - blocks/cards
    val xxxl: Dp = 40.dp,   // Huge
    val jumbo: Dp = 48.dp,  // Massive
    val ultra: Dp = 56.dp,  // Ultra
    val giga: Dp = 64.dp,   // Gigantic
)