package edneyosf.edconv.features.metrics.states

import edneyosf.edconv.core.common.Error

sealed interface MetricsStatusState {
    data object Initial: MetricsStatusState
    data object Loading: MetricsStatusState
    data class Progress(val percentage: Float, val speed: String): MetricsStatusState
    data class Failure(val error: Error): MetricsStatusState

    data class Complete(
        val startTime: String,
        val finishTime: String,
        val duration: String,
        val vmafScore: String? = null,
        val psnrScore: String? = null,
        val ssimScore: String? = null
    ): MetricsStatusState
}