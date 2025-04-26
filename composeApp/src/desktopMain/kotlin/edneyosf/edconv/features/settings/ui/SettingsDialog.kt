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
import edneyosf.edconv.core.common.Errors
import edneyosf.edconv.core.extensions.LaunchedEffected
import edneyosf.edconv.core.utils.FileUtils
import edneyosf.edconv.features.common.commonStrings
import edneyosf.edconv.features.settings.events.SettingsEvent
import edneyosf.edconv.features.settings.managers.SettingsManager
import edneyosf.edconv.features.settings.states.SettingsState
import edneyosf.edconv.features.settings.states.SettingsStatus
import edneyosf.edconv.features.settings.strings.settingsDialogStrings
import edneyosf.edconv.features.settings.strings.SettingsDialogStrings.Keys.*
import edneyosf.edconv.features.common.CommonStrings.Keys.ERROR_DEFAULT
import edneyosf.edconv.ui.components.alerts.ErrorAlertText
import edneyosf.edconv.ui.components.buttons.PrimaryButton
import edneyosf.edconv.ui.components.dialogs.SimpleDialog
import edneyosf.edconv.ui.compositions.*
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview

@Composable
fun SettingsDialog(onComplete: () -> Unit) {
    val scope = rememberCoroutineScope()
    val manager = remember { SettingsManager(scope) }
    val state by manager.state
    val status = state.status

    LaunchedEffected(key = status) {
        if(it is SettingsStatus.Complete) onComplete()
    }

    CompositionLocalProvider(value = stringsComp provides settingsDialogStrings) {
        state.Content(event = manager)
    }
}

@Composable
private fun SettingsState.Content(event: SettingsEvent) {
    val defined = !(ffmpegPath.isBlank() || ffprobePath.isBlank())
    val isLoading = status is SettingsStatus.Loading
    val pickFFmpegTitle = strings[PICK_FFMPEG_TITLE]
    val pickFFprobeTitle = strings[PICK_FFPROBE_TITLE]

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
                        onClick = {
                            FileUtils.pickFile(title = pickFFmpegTitle)
                                ?.let { event.setFFmpegPath(it) }
                        }
                    )
                    Spacer(modifier = Modifier.width(width = dimens.md))
                    PrimaryButton(
                        icon = Icons.Rounded.CheckCircle.takeIf { ffprobePath.isNotBlank() },
                        text = strings[SELECT_FFPROBE],
                        loading = isLoading,
                        onClick = {
                            FileUtils.pickFile(title = pickFFprobeTitle)
                                ?.let { event.setFFprobePath(it) }
                        }
                    )
                }
                if(status is SettingsStatus.Error) {
                    val error = status.id
                    val message = when(error) {
                        Errors.FFMPEG_OR_FFPROBE_VERIFICATION -> strings[FFMPEG_OR_FFPROBE_VERIFICATION]
                        Errors.CONFIGURATION_SAVE -> strings[CONFIGURATION_SAVE]
                        else -> commonStrings[ERROR_DEFAULT]
                    }

                    Spacer(modifier = Modifier.height(height = dimens.md))
                    ErrorAlertText(text = "$error: $message")
                }
            }
        },
        confirmationEnabled = defined && !isLoading,
        confirmationText = settingsDialogStrings[CONFIRMATION_BUTTON],
        onConfirmation = { event.onSave() },
        onDismissRequest = { }
    )
}

@Composable
private fun DefaultPreview() {
    CompositionLocalProvider(value = stringsComp provides settingsDialogStrings) {
        SettingsState(ffmpegPath = "ffmpeg", status = SettingsStatus.Error(id = "0001"))
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