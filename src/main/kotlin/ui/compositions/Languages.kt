package ui.compositions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf

inline val languages: String
    @Composable
    @ReadOnlyComposable
    get() = languagesComp.current

val languagesComp = compositionLocalOf<String> { error("No value provided for languagesComp") }