package edneyosf.edconv.ui.compositions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import edneyosf.edconv.core.common.Languages

inline val language: String
    @Composable
    @ReadOnlyComposable
    get() = languageComp.current

val languageComp = compositionLocalOf { Languages.EN }