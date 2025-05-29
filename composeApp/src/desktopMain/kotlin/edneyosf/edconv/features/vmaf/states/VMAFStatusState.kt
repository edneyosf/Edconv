package edneyosf.edconv.features.vmaf.states

import edneyosf.edconv.core.common.Error

sealed interface VMAFStatusState {
    data object Initial: VMAFStatusState
    data object Loading: VMAFStatusState
    data object Progress: VMAFStatusState
    data object Complete: VMAFStatusState
    data class Failure(val error: Error): VMAFStatusState
}