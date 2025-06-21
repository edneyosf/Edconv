package edneyosf.edconv.features.converter.states

import edneyosf.edconv.core.common.Error

sealed interface ConverterStatusState {
    data object Initial: ConverterStatusState
    data object Loading: ConverterStatusState
    data class Progress(val percentage: Float, val speed: String): ConverterStatusState
    data class Complete(val startTime: String, val finishTime: String, val duration: String): ConverterStatusState
    data class Failure(val error: Error): ConverterStatusState
}