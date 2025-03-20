package features.home.states

import core.edconv.common.Resolutions

data class HomeState(
    val status: HomeStatus,
    val inputFile: String?,
    val outputFile: String,
    val format: String,
    val channels: String,
    val vbr: String,
    val kbps: String,
    val sampleRate: String?,
    val preset: String?,
    val crf: Int,
    val resolution: Resolutions,
    val bit: String,
    val noAudio: Boolean
)

sealed interface HomeStatus {
    data object Initial: HomeStatus
    data object Loading: HomeStatus
    data class Progress(val percentage: Float): HomeStatus
    data object Complete: HomeStatus
    data class Error(val message: String? = null): HomeStatus
}