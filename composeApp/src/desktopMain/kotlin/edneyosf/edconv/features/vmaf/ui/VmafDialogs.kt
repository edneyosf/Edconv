package edneyosf.edconv.features.vmaf.ui

import androidx.compose.runtime.Composable
import edneyosf.edconv.features.mediainfo.MediaInfoDialog
import edneyosf.edconv.features.vmaf.VmafEvent
import edneyosf.edconv.features.vmaf.states.VmafDialogState.*
import edneyosf.edconv.features.vmaf.states.VmafState
import edneyosf.edconv.features.vmaf.states.VmafStatusState.*

@Composable
fun VmafState.Dialogs(event: VmafEvent) {
    status.run {
        when (this) {
            is Failure -> {
                VmafErrorDialog(
                    error = error,
                    onFinish = { event.setStatus(Initial) }
                )
            }
            is Complete -> {
                VmafCompleteDialog(
                    score = score,
                    startTime = startTime,
                    finishTime = finishTime,
                    duration = duration,
                    onFinish = { event.setStatus(Initial) }
                )
            }
            else -> Unit
        }
    }

    dialog.run {
        when (this) {
            is MediaInfo -> {
                inputMedia.MediaInfoDialog(
                    onFinish = { event.setDialog(dialog = None) }
                )
            }
            else -> Unit
        }
    }
}