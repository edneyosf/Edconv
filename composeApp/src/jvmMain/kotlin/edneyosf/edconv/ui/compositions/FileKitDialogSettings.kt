package edneyosf.edconv.ui.compositions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import io.github.vinceglb.filekit.dialogs.FileKitDialogSettings

inline val fileKitDialogSettings: FileKitDialogSettings
    @Composable
    @ReadOnlyComposable
    get() = fileKitDialogSettingsComp.current

val fileKitDialogSettingsComp = compositionLocalOf { FileKitDialogSettings() }