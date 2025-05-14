package edneyosf.edconv.features.converter.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import edneyosf.edconv.features.converter.strings.ConverterDialogStrings.Keys.CANCEL_BUTTON
import edneyosf.edconv.features.converter.strings.ConverterDialogStrings.Keys.CONFIRMATION_BUTTON
import edneyosf.edconv.features.converter.strings.ConverterDialogStrings.Keys.OVERWRITE_FILE
import edneyosf.edconv.features.converter.strings.ConverterDialogStrings.Keys.WARNING_TITLE
import edneyosf.edconv.features.converter.strings.converterDialogStrings
import edneyosf.edconv.ui.components.dialogs.SimpleDialog
import edneyosf.edconv.ui.compositions.strings
import edneyosf.edconv.ui.compositions.stringsComp
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview

@Composable
fun OverwriteFileDialog(onCancel: () -> Unit, onConfirmation: () -> Unit) {
    CompositionLocalProvider(stringsComp provides converterDialogStrings) {
        SimpleDialog(
            icon = Icons.Rounded.Warning,
            title = strings[WARNING_TITLE],
            content = { Text(strings[OVERWRITE_FILE]) },
            confirmationText = strings[CONFIRMATION_BUTTON],
            onConfirmation = onConfirmation,
            cancelText = strings[CANCEL_BUTTON],
            onCancel = onCancel
        )
    }
}

@Composable
private fun OverwriteFilePreview() = OverwriteFileDialog(onConfirmation = {}, onCancel = {})

@Preview
@Composable
private fun OverwriteFileEnglishLight() = EnglishLightPreview { OverwriteFilePreview() }

@Preview
@Composable
private fun OverwriteFileEnglishDark() = EnglishDarkPreview { OverwriteFilePreview() }

@Preview
@Composable
private fun OverwriteFilePortugueseLight() = PortugueseLightPreview { OverwriteFilePreview() }

@Preview
@Composable
private fun OverwriteFilePortugueseDark() = PortugueseDarkPreview { OverwriteFilePreview() }