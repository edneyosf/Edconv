package ui.compositions

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
    val a: Dp = 0.dp,
    val b: Dp = 2.dp,
    val c: Dp = 4.dp,
    val d: Dp = 6.dp,
    val e: Dp = 8.dp,
    val f: Dp = 10.dp,
    val g: Dp = 12.dp,
    val h: Dp = 14.dp,
    val i: Dp = 16.dp,
    val j: Dp = 18.dp,

    val k: Dp = 20.dp,
    val l: Dp = 22.dp,
    val m: Dp = 24.dp,
    val n: Dp = 26.dp,
    val o: Dp = 28.dp,
    val p: Dp = 30.dp,
    val q: Dp = 32.dp,

    val r: Dp = 34.dp,
    val s: Dp = 36.dp,
    val t: Dp = 38.dp,
    val u: Dp = 40.dp,
    val v: Dp = 42.dp,
    val w: Dp = 44.dp,
    val x: Dp = 46.dp,

    val y: Dp = 48.dp,
    val z: Dp = 50.dp,
    val aa: Dp = 52.dp,
    val ab: Dp = 54.dp,
    val ac: Dp = 56.dp,
    val ad: Dp = 58.dp,
    val ae: Dp = 60.dp,

    val af: Dp = 62.dp,
    val ag: Dp = 64.dp,
    val ah: Dp = 66.dp,
    val ai: Dp = 68.dp,
    val aj: Dp = 70.dp,
    val ak: Dp = 72.dp
)