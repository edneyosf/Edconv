package features.home.states

import edconv.common.Channels
import edconv.common.MediaFormat
import edconv.common.Resolutions

data class HomeState(
    val status: HomeStatus,
    val logs: String,
    val inputFile: String?,
    val outputFile: String?,
    val format: MediaFormat?,
    val channels: Channels?,
    val vbr: String,
    val kbps: String?,
    val sampleRate: String?,
    val preset: String?,
    val crf: Int,
    val resolution: Resolutions?,
    val bit: String?,
    val noAudio: Boolean
)

sealed interface HomeStatus {
    data object Initial: HomeStatus
    data object Loading: HomeStatus
    data class Progress(val percentage: Float): HomeStatus
    data object Complete: HomeStatus
    data class Error(val message: String? = null): HomeStatus
}