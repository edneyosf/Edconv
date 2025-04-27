package edneyosf.edconv.features.converter.states

sealed interface ConverterStatusState {
    data object Initial: ConverterStatusState
    data object Loading: ConverterStatusState
    data object FileExists: ConverterStatusState
    data class Progress(val percentage: Float, val speed: String): ConverterStatusState
    data class Complete(val startTime: String, val finishTime: String, val duration: String): ConverterStatusState
    data class Error(val message: String? = null): ConverterStatusState
}