package edneyosf.edconv.features.home.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import edneyosf.edconv.core.common.Errors
import edneyosf.edconv.features.common.CommonStrings.Keys.ERROR_TITLE
import edneyosf.edconv.features.common.CommonStrings.Keys.CONFIRMATION_BUTTON
import edneyosf.edconv.features.common.CommonStrings.Keys.ERROR_DEFAULT
import edneyosf.edconv.features.common.commonStrings
import edneyosf.edconv.features.home.strings.homeErrorDialogStrings
import edneyosf.edconv.features.home.strings.HomeErrorDialogStrings.Keys.*
import edneyosf.edconv.ui.components.dialogs.SimpleDialog
import edneyosf.edconv.ui.compositions.strings
import edneyosf.edconv.ui.compositions.stringsComp
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview

@Composable
fun HomeErrorDialog(error: String, onFinish: () -> Unit) {
    CompositionLocalProvider(value = stringsComp provides homeErrorDialogStrings) {
        val description = when(error) {
            Errors.LOAD_CONFIGS -> strings[LOAD_CONFIGS]
            Errors.UNKNOWN_INPUT_MEDIA -> strings[UNKNOWN_INPUT_MEDIA]
            Errors.NO_DURATION_INPUT_MEDIA -> strings[NO_DURATION_INPUT_MEDIA]
            Errors.NO_STREAM_FOUND_INPUT_MEDIA -> strings[NO_STREAM_FOUND_INPUT_MEDIA]
            Errors.NO_CHANNELS_INPUT_MEDIA -> strings[NO_CHANNELS_INPUT_MEDIA]
            Errors.NO_RESOLUTION_INPUT_MEDIA -> strings[NO_RESOLUTION_INPUT_MEDIA]
            else -> commonStrings[ERROR_DEFAULT]
        }

        SimpleDialog(
            icon = Icons.Rounded.Error,
            title = commonStrings[ERROR_TITLE],
            content = {
                Text(
                    text = "$description\n($error)",
                    style = TextStyle(textAlign = TextAlign.Center)
                )
            },
            onConfirmation = onFinish,
            confirmationText = commonStrings[CONFIRMATION_BUTTON]
        )
    }
}

@Composable
private fun DefaultPreview() = HomeErrorDialog(error = "Sample", onFinish = {})

@Preview
@Composable
private fun ErrorEnglishLight() = EnglishLightPreview { DefaultPreview() }

@Preview
@Composable
private fun ErrorEnglishDark() = EnglishDarkPreview { DefaultPreview() }

@Preview
@Composable
private fun ErrorPortugueseLight() = PortugueseLightPreview { DefaultPreview() }

@Preview
@Composable
private fun ErrorPortugueseDark() = PortugueseDarkPreview { DefaultPreview() }