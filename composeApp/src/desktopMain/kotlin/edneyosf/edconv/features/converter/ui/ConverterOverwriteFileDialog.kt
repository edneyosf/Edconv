package edneyosf.edconv.features.converter.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import edneyosf.edconv.features.common.commonStrings
import edneyosf.edconv.features.common.CommonStrings.Keys.WARNING_TITLE
import edneyosf.edconv.features.common.CommonStrings.Keys.CANCEL_BUTTON
import edneyosf.edconv.features.common.CommonStrings.Keys.CONFIRMATION_BUTTON
import edneyosf.edconv.features.converter.strings.ConverterOverwriteFileDialogStrings.Keys.DESCRIPTION
import edneyosf.edconv.features.converter.strings.converterOverwriteFileDialogStrings
import edneyosf.edconv.ui.components.dialogs.SimpleDialog
import edneyosf.edconv.ui.compositions.strings
import edneyosf.edconv.ui.compositions.stringsComp
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview

@Composable
fun OverwriteFileDialog(onCancel: () -> Unit, onConfirmation: () -> Unit) {
    CompositionLocalProvider(value = stringsComp provides converterOverwriteFileDialogStrings) {
        SimpleDialog(
            icon = Icons.Rounded.Warning,
            title = commonStrings[WARNING_TITLE],
            content = { Text(text = strings[DESCRIPTION]) },
            confirmationText = commonStrings[CONFIRMATION_BUTTON],
            onConfirmation = onConfirmation,
            cancelText = commonStrings[CANCEL_BUTTON],
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