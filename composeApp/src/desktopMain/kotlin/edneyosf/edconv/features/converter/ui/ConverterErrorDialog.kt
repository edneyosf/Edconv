package edneyosf.edconv.features.converter.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import edneyosf.edconv.core.common.Error
import edneyosf.edconv.features.common.CommonStrings.Keys.ERROR_TITLE
import edneyosf.edconv.features.common.CommonStrings.Keys.CONFIRMATION_BUTTON
import edneyosf.edconv.features.common.CommonStrings.Keys.ERROR_DEFAULT
import edneyosf.edconv.features.common.commonStrings
import edneyosf.edconv.features.converter.strings.ConverterErrorDialogStrings.Keys.*
import edneyosf.edconv.features.converter.strings.converterErrorDialogStrings
import edneyosf.edconv.ui.components.dialogs.SimpleDialog
import edneyosf.edconv.ui.compositions.strings
import edneyosf.edconv.ui.compositions.stringsComp
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview

@Composable
fun ConverterErrorDialog(error: Error, onFinish: () -> Unit) {
    CompositionLocalProvider(value = stringsComp provides converterErrorDialogStrings) {
        val description = when(error) {
            Error.ON_STARTING_CONVERSION -> strings[ON_STARTING_CONVERSION]
            Error.ON_STARTING_CONVERSION_REQUIREMENTS -> strings[ON_STARTING_CONVERSION_REQUIREMENTS]
            Error.ON_STOPPING_CONVERSION -> strings[ON_STOPPING_CONVERSION]
            Error.START_TIME_NULL -> strings[START_TIME_NULL]
            Error.INPUT_FILE_NOT_EXIST -> strings[INPUT_FILE_NOT_EXIST]
            Error.INPUT_NOT_FILE -> strings[INPUT_NOT_FILE]
            Error.CONVERSION_PROCESS_COMPLETED -> strings[CONVERSION_PROCESS_COMPLETED]
            Error.PROCESS_NULL -> strings[PROCESS_NULL]
            Error.CONVERSION_PROCESS -> strings[CONVERSION_PROCESS]
            else -> commonStrings[ERROR_DEFAULT]
        }

        SimpleDialog(
            icon = Icons.Rounded.Error,
            iconDescription = error.code,
            title = commonStrings[ERROR_TITLE],
            content = { Text(text = description) },
            onConfirmation = onFinish,
            confirmationText = commonStrings[CONFIRMATION_BUTTON]
        )
    }
}

@Composable
private fun DefaultPreview() = ConverterErrorDialog(Error.CONVERSION_PROCESS, onFinish = {})

@Preview
@Composable
private fun EnglishLight() = EnglishLightPreview { DefaultPreview() }

@Preview
@Composable
private fun EnglishDark() = EnglishDarkPreview { DefaultPreview() }

@Preview
@Composable
private fun PortugueseLight() = PortugueseLightPreview { DefaultPreview() }

@Preview
@Composable
private fun PortugueseDark() = PortugueseDarkPreview { DefaultPreview() }