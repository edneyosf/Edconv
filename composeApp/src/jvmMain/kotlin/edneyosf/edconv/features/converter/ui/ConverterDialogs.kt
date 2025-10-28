package edneyosf.edconv.features.converter.ui

import androidx.compose.runtime.Composable
import edneyosf.edconv.core.config.RemoteConfig
import edneyosf.edconv.features.converter.ConverterEvent
import edneyosf.edconv.features.converter.states.ConverterDialogState
import edneyosf.edconv.features.converter.states.ConverterState
import edneyosf.edconv.features.converter.states.ConverterStatusState
import edneyosf.edconv.features.converter.enums.ConverterFileExistsAction as FileExistsAction
import edneyosf.edconv.features.settings.ui.SettingsDialog
import org.koin.compose.koinInject

@Composable
fun ConverterState.Dialogs(event: ConverterEvent) {
    val remoteConfig = koinInject<RemoteConfig>()
    val donationUrl = remoteConfig.donationUrl

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
                    donationUrl = donationUrl,
                    onFinish = { event.setStatus(ConverterStatusState.Initial) }
                )
            }

            else -> Unit
        }
    }

    dialog.run {
        when (this) {
            is ConverterDialogState.Settings -> SettingsDialog { event.setDialog(ConverterDialogState.None) }

            is ConverterDialogState.FileExists -> {
                ConverterOverwriteFileDialog(
                    onCancel = { event.setDialog(ConverterDialogState.None) },
                    onConfirmation = {
                        when(action) {
                            FileExistsAction.START -> event.start(overwrite = true)
                            FileExistsAction.ADD_TO_QUEUE -> event.addToQueue(overwrite = true)
                            FileExistsAction.ON_CONVERSION -> { /* TODO */ }
                        }
                    }
                )
            }

            else -> Unit
        }
    }
}