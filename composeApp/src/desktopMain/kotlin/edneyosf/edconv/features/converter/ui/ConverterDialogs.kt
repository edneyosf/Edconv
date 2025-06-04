package edneyosf.edconv.features.converter.ui

import androidx.compose.runtime.Composable
import edneyosf.edconv.features.converter.events.ConverterEvent
import edneyosf.edconv.features.converter.states.ConverterDialogState
import edneyosf.edconv.features.converter.states.ConverterState
import edneyosf.edconv.features.converter.states.ConverterStatusState
import edneyosf.edconv.features.mediainfo.MediaInfoDialog
import edneyosf.edconv.features.settings.ui.SettingsDialog

@Composable
fun ConverterState.Dialogs(event: ConverterEvent) {
    status.run {
        when (this) {
            is ConverterStatusState.Failure -> {
                ConverterErrorDialog(
                    error = error,
                    onFinish = { event.setStatus(ConverterStatusState.Initial) }
                )
            }

            is ConverterStatusState.Complete -> {
                ConverterCompleteDialog(
                    startTime = startTime,
                    finishTime = finishTime,
                    duration = duration,
                    onFinish = { event.setStatus(ConverterStatusState.Initial) }
                )
            }

            is ConverterStatusState.FileExists -> {
                ConverterOverwriteFileDialog(
                    onCancel = { event.setStatus(ConverterStatusState.Initial) },
                    onConfirmation = { event.start(overwrite = true) }
                )
            }

            else -> Unit
        }
    }

    dialog.run {
        when (this) {
            is ConverterDialogState.Settings -> SettingsDialog { event.setDialog(ConverterDialogState.None) }

            is ConverterDialogState.MediaInfo -> {
                inputMedia.MediaInfoDialog(
                    onFinish = { event.setDialog(ConverterDialogState.None) }
                )
            }

            else -> Unit
        }
    }
}