package features.home.states

import edconv.common.*

data class HomeState(
    val status: HomeStatus,
    val logs: String,
    val inputFile: String?,
    val outputFile: String?,
    val format: MediaFormat?,
    val channels: Channels?,
    val vbr: Int?,
    val kbps: Kbps?,
    val sampleRate: SampleRate?,
    val preset: String?,
    val crf: Int,
    val resolution: Resolution?,
    val pixelFormat: PixelFormat?,
    val noAudio: Boolean
)

sealed interface HomeStatus {
    data object Initial: HomeStatus
    data object Loading: HomeStatus
    data class Progress(val percentage: Float): HomeStatus
    data object Complete: HomeStatus
    data class Error(val message: String? = null): HomeStatus
}