package ui.compositions

import androidx.compose.runtime.compositionLocalOf
import core.common.Texts

val texts = compositionLocalOf<Texts> { error("No value provided for texts") }