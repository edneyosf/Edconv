package edneyosf.edconv.ui.compositions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import edneyosf.edconv.core.common.Texts

val texts: Texts
    @Composable
    @ReadOnlyComposable
    get() = textsComp.current

val textsComp = compositionLocalOf<Texts> { error("No value provided for texts") }