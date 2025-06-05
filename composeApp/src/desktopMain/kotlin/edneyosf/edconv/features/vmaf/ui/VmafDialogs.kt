package edneyosf.edconv.features.vmaf.ui

import androidx.compose.runtime.Composable
import edneyosf.edconv.features.mediainfo.MediaInfoDialog
import edneyosf.edconv.features.vmaf.events.VmafEvent
import edneyosf.edconv.features.vmaf.states.VmafDialogState
import edneyosf.edconv.features.vmaf.states.VmafState

@Composable
fun VmafState.Dialogs(event: VmafEvent) {
    /*status.run {
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

            else -> Unit
        }
    }*/

    dialog.run {
        when (this) {

            is VmafDialogState.MediaInfo -> {
                inputMedia.MediaInfoDialog(
                    onFinish = { event.setDialog(dialog = VmafDialogState.None) }
                )
            }

            else -> Unit
        }
    }
}