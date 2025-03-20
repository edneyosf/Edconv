package ui.compositions

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val dimens = compositionLocalOf { Dimens() }

data class Dimens(
    val droplet: Dp = 0.dp,
    val mist: Dp = 2.dp,
    val drizzle: Dp = 4.dp,
    val ripple: Dp = 6.dp,
    val puddle: Dp = 8.dp,
    val splash: Dp = 10.dp,
    val stream: Dp = 12.dp,
    val brook: Dp = 14.dp,
    val wavelet: Dp = 16.dp,
    val current: Dp = 18.dp,

    val tide: Dp = 20.dp,
    val surge: Dp = 22.dp,
    val swell: Dp = 24.dp,
    val breaker: Dp = 26.dp,
    val foam: Dp = 28.dp,
    val billow: Dp = 30.dp,
    val wake: Dp = 32.dp,

    val bay: Dp = 34.dp,
    val gulf: Dp = 36.dp,
    val abyss: Dp = 38.dp,
    val deep: Dp = 40.dp,
    val expanse: Dp = 42.dp,
    val horizon: Dp = 44.dp,
    val fathom: Dp = 46.dp,

    val trench: Dp = 48.dp,
    val chasm: Dp = 50.dp,
    val maelstrom: Dp = 52.dp,
    val vortex: Dp = 54.dp,
    val tempest: Dp = 56.dp,
    val tsunami: Dp = 58.dp,
    val oceanic: Dp = 60.dp,

    val leviathan: Dp = 62.dp,
    val infinite: Dp = 64.dp,
    val nautical: Dp = 66.dp,
    val celestial: Dp = 68.dp,
    val abyssal: Dp = 70.dp,
    val cosmic: Dp = 72.dp
)