package edneyosf.edconv.features.settings

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
import edneyosf.edconv.core.utils.FileUtils
import edneyosf.edconv.features.settings.events.SettingsEvent
import edneyosf.edconv.features.settings.managers.SettingsManager
import edneyosf.edconv.features.settings.states.SettingsState
import edneyosf.edconv.features.settings.states.SettingsStatus
import edneyosf.edconv.features.settings.strings.settingsDialogStrings
import edneyosf.edconv.features.settings.strings.SettingsDialogStrings.Key.TITLE
import edneyosf.edconv.features.settings.strings.SettingsDialogStrings.Key.DESCRIPTION
import edneyosf.edconv.features.settings.strings.SettingsDialogStrings.Key.SELECT_FFMPEG
import edneyosf.edconv.features.settings.strings.SettingsDialogStrings.Key.SELECT_FFPROBE
import edneyosf.edconv.features.settings.strings.SettingsDialogStrings.Key.CANCEL_BUTTON
import edneyosf.edconv.features.settings.strings.SettingsDialogStrings.Key.CONFIRMATION_BUTTON
import edneyosf.edconv.features.settings.strings.SettingsDialogStrings.Key.PICK_FFMPEG_TITLE
import edneyosf.edconv.features.settings.strings.SettingsDialogStrings.Key.PICK_FFPROBE_TITLE
import edneyosf.edconv.ui.components.buttons.PrimaryButton
import edneyosf.edconv.ui.components.cards.WarningAlertCard
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

    LaunchedEffect(status) {
        if(status is SettingsStatus.Complete) onComplete()
    }

    CompositionLocalProvider(stringsComp provides settingsDialogStrings) {
        state.Content(onEvent = manager::onEvent)
    }
}

@Composable
private fun SettingsState.Content(onEvent: (SettingsEvent) -> Unit) {
    val defined = !(ffmpegPath.isBlank() || ffprobePath.isBlank())
    val isLoading = status is SettingsStatus.Loading
    val pickFFmpegTitle = strings[PICK_FFMPEG_TITLE]
    val pickFFprobeTitle = strings[PICK_FFPROBE_TITLE]

    SimpleDialog(
        icon = Icons.Rounded.Settings,
        title = strings[TITLE],
        content = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(strings[DESCRIPTION], textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(dimens.xl))
                Row {
                    PrimaryButton(
                        icon = if(ffmpegPath.isNotBlank()) Icons.Rounded.CheckCircle else null,
                        text = strings[SELECT_FFMPEG],
                        loading = isLoading,
                        onClick = {
                            FileUtils.pickFile(title = pickFFmpegTitle)?.let {
                                onEvent(SettingsEvent.SetFFmpegPath(it))
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(dimens.md))
                    PrimaryButton(
                        icon = if(ffprobePath.isNotBlank()) Icons.Rounded.CheckCircle else null,
                        text = strings[SELECT_FFPROBE],
                        loading = isLoading,
                        onClick = {
                            FileUtils.pickFile(title = pickFFprobeTitle)?.let {
                                onEvent(SettingsEvent.SetFFprobePath(it))
                            }
                        }
                    )
                }
                if(status is SettingsStatus.Error) {
                    Spacer(modifier = Modifier.height(dimens.md))
                    WarningAlertCard("Falha ao jogar lol!")
                }
            }
        },
        confirmationButtonEnabled = defined && !isLoading,
        confirmationText = settingsDialogStrings[CONFIRMATION_BUTTON],
        cancelText = settingsDialogStrings[CANCEL_BUTTON],
        onConfirmation = { onEvent(SettingsEvent.OnSave) },
        onDismissRequest = { }
    )
}

@Composable
private fun DefaultPreview() {
    CompositionLocalProvider(stringsComp provides settingsDialogStrings) {
        SettingsState.default().copy(ffmpegPath = "ffmpeg")
            .Content(onEvent = {})
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