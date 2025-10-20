package edneyosf.edconv.features.metrics.ui

import androidx.compose.runtime.Composable
import edneyosf.edconv.features.metrics.MetricsEvent
import edneyosf.edconv.features.metrics.states.MetricsState
import edneyosf.edconv.features.metrics.states.MetricsStatusState.*

@Composable
fun MetricsState.Dialogs(event: MetricsEvent) {
    status.run {
        when (this) {
            is Failure -> {
                MetricsErrorDialog(
                    error = error,
                    onFinish = { event.setStatus(Initial) }
                )
            }
            is Complete -> {
                MetricsCompleteDialog(
                    vmafScore = vmafScore,
                    psnrScore = psnrScore,
                    ssimScore = ssimScore,
                    startTime = startTime,
                    finishTime = finishTime,
                    duration = duration,
                    onFinish = { event.setStatus(Initial) }
                )
            }
            else -> Unit
        }
    }
}