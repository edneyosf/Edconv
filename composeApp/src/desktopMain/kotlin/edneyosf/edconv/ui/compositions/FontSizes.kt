package edneyosf.edconv.ui.compositions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

//@Deprecated("Pare de usar", level = DeprecationLevel.ERROR, replaceWith = ReplaceWith(""))
inline val fontSizes: FontSizes
    @Composable
    @ReadOnlyComposable
    get() = fontSizesComp.current

val fontSizesComp = compositionLocalOf { FontSizes() }

data class FontSizes(
    val a: TextUnit = 12.sp,
    val b: TextUnit = 14.sp,
    val c: TextUnit = 16.sp,
    val d: TextUnit = 18.sp,
    val e: TextUnit = 20.sp,
    val f: TextUnit = 22.sp,
    val g: TextUnit = 24.sp,
    val h: TextUnit = 26.sp,
    val i: TextUnit = 28.sp,
    val j: TextUnit = 30.sp,
    val k: TextUnit = 32.sp,
    val l: TextUnit = 34.sp,
    val m: TextUnit = 36.sp,
    val n: TextUnit = 38.sp,
    val o: TextUnit = 40.sp,
    val p: TextUnit = 42.sp
)