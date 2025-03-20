package ui.compositions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import app.AppConfigs

inline val languages: String
    @Composable
    @ReadOnlyComposable
    get() = languagesComp.current

val languagesComp = compositionLocalOf { AppConfigs.LANGUAGE_DEFAULT }