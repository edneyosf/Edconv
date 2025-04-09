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
    val none: Dp = 0.dp,                   // No spacing
    val xxs: Dp = 4.dp,                    // Extra, extra, small - tiny spacing
    val xs: Dp = 8.dp,                     // Extra, small - minimum margin
    val sm: Dp = 12.dp,                    // Small - light padding
    val md: Dp = 16.dp,                    // Medium - default padding/margin
    val ml: Dp = 18.dp,                    // Medium-large - small icons
    val lg: Dp = 20.dp,                    // Large - regular icons
    val xl: Dp = 24.dp,                    // Extra large - section separation
    val xxl: Dp = 32.dp,                   // Extra, extra large - blocks/cards
    val xxxl: Dp = 40.dp,                  // Huge
    val jumbo: Dp = 48.dp,                 // Massive
    val ultra: Dp = 56.dp,                 // Ultra
    val giga: Dp = 64.dp,                  // Gigantic

    @Deprecated("") val a: Dp = 0.dp,
    @Deprecated("") val b: Dp = 2.dp,
    @Deprecated("") val d: Dp = 6.dp,
    @Deprecated("") val f: Dp = 10.dp,
    @Deprecated("") val i: Dp = 16.dp,
    @Deprecated("") val m: Dp = 24.dp,
    @Deprecated("") val n: Dp = 26.dp,
    @Deprecated("") val q: Dp = 32.dp,
    @Deprecated("") val r: Dp = 34.dp,
    @Deprecated("") val s: Dp = 36.dp,
    @Deprecated("") val t: Dp = 38.dp,
    @Deprecated("") val u: Dp = 40.dp,
    @Deprecated("") val v: Dp = 42.dp,
    @Deprecated("") val w: Dp = 44.dp,
    @Deprecated("") val x: Dp = 46.dp,
    @Deprecated("") val y: Dp = 48.dp,
    @Deprecated("") val z: Dp = 50.dp,
    @Deprecated("") val aa: Dp = 52.dp,
    @Deprecated("") val ab: Dp = 54.dp,
    @Deprecated("") val ac: Dp = 56.dp,
    @Deprecated("") val ad: Dp = 58.dp,
    @Deprecated("") val ae: Dp = 60.dp,
    @Deprecated("") val af: Dp = 62.dp,
    @Deprecated("") val ag: Dp = 64.dp,
    @Deprecated("") val ah: Dp = 66.dp,
    @Deprecated("") val ai: Dp = 68.dp,
    @Deprecated("") val aj: Dp = 70.dp,
    @Deprecated("") val ak: Dp = 72.dp
)