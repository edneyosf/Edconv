package edneyosf.edconv.features.settings.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import edneyosf.edconv.core.common.Error
import edneyosf.edconv.core.extensions.LaunchedEffected
import edneyosf.edconv.features.common.commonStrings
import edneyosf.edconv.features.settings.SettingsEvent
import edneyosf.edconv.features.settings.SettingsViewModel
import edneyosf.edconv.features.settings.states.SettingsState
import edneyosf.edconv.features.settings.states.SettingsStatusState
import edneyosf.edconv.features.settings.strings.settingsDialogStrings
import edneyosf.edconv.features.settings.strings.SettingsDialogStrings.Keys.*
import edneyosf.edconv.features.common.CommonStrings.Keys.ERROR_DEFAULT
import edneyosf.edconv.features.common.CommonStrings.Keys.CONFIRMATION_BUTTON
import edneyosf.edconv.ui.components.alerts.ErrorAlertText
import edneyosf.edconv.ui.components.buttons.PrimaryButton
import edneyosf.edconv.ui.components.dialogs.SimpleDialog
import edneyosf.edconv.ui.compositions.*
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsDialog(onComplete: () -> Unit) {
    val viewModel = koinViewModel<SettingsViewModel>()
    val state by viewModel.state

    LaunchedEffected(key = state.status) {
        if(it is SettingsStatusState.Complete) {
            onComplete()
            viewModel.setStatus(status = SettingsStatusState.Initial)
        }
    }

    CompositionLocalProvider(value = stringsComp provides settingsDialogStrings) {
        state.Content(event = viewModel)
    }
}

@Composable
private fun SettingsState.Content(event: SettingsEvent) {
    val defined = !(ffmpegPath.isBlank() || ffprobePath.isBlank())
    val isLoading = status is SettingsStatusState.Loading
    val pickFFmpeg = rememberFilePickerLauncher(
        title = strings[PICK_FFMPEG_TITLE],
        onResult = { it?.file?.let { file -> event.setFFmpegPath(file.absolutePath) } },
        dialogSettings = fileKitDialogSettings
    )
    val pickFFprobe = rememberFilePickerLauncher(
        title = strings[PICK_FFPROBE_TITLE],
        onResult = { it?.file?.let { file -> event.setFFprobePath(file.absolutePath) } },
        dialogSettings = fileKitDialogSettings
    )


    SimpleDialog(
        icon = Icons.Rounded.Settings,
        title = strings[TITLE],
        content = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if(defined) strings[DEFINED] else strings[NO_DEFINED],
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(height = dimens.xl))
                Row {
                    PrimaryButton(
                        icon = Icons.Rounded.CheckCircle.takeIf { ffmpegPath.isNotBlank() },
                        text = strings[SELECT_FFMPEG],
                        loading = isLoading,
                        onClick = { pickFFmpeg.launch() }
                    )
                    Spacer(modifier = Modifier.width(width = dimens.md))
                    PrimaryButton(
                        icon = Icons.Rounded.CheckCircle.takeIf { ffprobePath.isNotBlank() },
                        text = strings[SELECT_FFPROBE],
                        loading = isLoading,
                        onClick = { pickFFprobe.launch() }
                    )
                }
                if(status is SettingsStatusState.Failure) {
                    val error = status.error
                    val message = when(error) {
                        Error.FFMPEG_OR_FFPROBE_VERIFICATION -> strings[FFMPEG_OR_FFPROBE_VERIFICATION]
                        Error.CONFIGURATION_SAVE -> strings[CONFIGURATION_SAVE]
                        else -> commonStrings[ERROR_DEFAULT]
                    }

                    Spacer(modifier = Modifier.height(height = dimens.md))
                    ErrorAlertText(text = "$error: $message")
                }
            }
        },
        confirmationEnabled = defined && !isLoading,
        confirmationText = commonStrings[CONFIRMATION_BUTTON],
        onConfirmation = { event.onSave() },
        onDismissRequest = { }
    )
}

@Composable
private fun DefaultPreview() {
    CompositionLocalProvider(value = stringsComp provides settingsDialogStrings) {
        SettingsState(ffmpegPath = "ffmpeg", status = SettingsStatusState.Failure(Error.DEFAULT))
            .Content(event = object : SettingsEvent {})
    }
}

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