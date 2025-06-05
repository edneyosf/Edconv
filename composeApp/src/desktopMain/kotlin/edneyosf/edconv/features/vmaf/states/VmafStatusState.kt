package edneyosf.edconv.features.vmaf.states

import edneyosf.edconv.core.common.Error

sealed interface VmafStatusState {
    data object Initial: VmafStatusState
    data object Loading: VmafStatusState
    data class Progress(val percentage: Float, val speed: String): VmafStatusState
    data object Complete: VmafStatusState
    data class Failure(val error: Error): VmafStatusState
}