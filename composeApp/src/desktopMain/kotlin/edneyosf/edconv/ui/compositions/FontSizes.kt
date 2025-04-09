package edneyosf.edconv.ui.compositions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

inline val fontSizes: FontSizes
    @Composable
    @ReadOnlyComposable
    get() = fontSizesComp.current

val fontSizesComp = compositionLocalOf { FontSizes() }

data class FontSizes(
    @Deprecated("") val a: TextUnit = 12.sp,
    @Deprecated("") val b: TextUnit = 14.sp,
    @Deprecated("") val c: TextUnit = 16.sp,
    @Deprecated("") val d: TextUnit = 18.sp,
    @Deprecated("") val e: TextUnit = 20.sp,
    @Deprecated("") val f: TextUnit = 22.sp,
    @Deprecated("") val g: TextUnit = 24.sp,
    @Deprecated("") val h: TextUnit = 26.sp,
    @Deprecated("") val i: TextUnit = 28.sp,
    @Deprecated("") val j: TextUnit = 30.sp,
    @Deprecated("") val k: TextUnit = 32.sp,
    @Deprecated("") val l: TextUnit = 34.sp,
    @Deprecated("") val m: TextUnit = 36.sp,
    @Deprecated("") val n: TextUnit = 38.sp,
    @Deprecated("") val o: TextUnit = 40.sp,
    @Deprecated("") val p: TextUnit = 42.sp
)