package edneyosf.edconv.features.vmaf.states

import edneyosf.edconv.core.common.Error

sealed interface VmafStatusState {
    data object Initial: VmafStatusState
    data object Loading: VmafStatusState
    data class Progress(val percentage: Float, val speed: String): VmafStatusState
    data class Failure(val error: Error): VmafStatusState

    data class Complete(
        val startTime: String,
        val finishTime: String,
        val duration: String,
        val score: String
    ): VmafStatusState
}