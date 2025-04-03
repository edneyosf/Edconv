package features.home.states

import edconv.common.*
import edconv.core.data.MediaData
import java.time.Instant

data class HomeState(
    val status: HomeStatus,
    val logs: String,
    val inputFile: MediaData?,
    val outputFile: String?,
    val codec: Codec?,
    val channels: Channels?,
    val vbr: Int?,
    val bitrate: Bitrate?,
    val sampleRate: SampleRate?,
    val preset: String?,
    val crf: Int?,
    val resolution: Resolution?,
    val pixelFormat: PixelFormat?,
    val noAudio: Boolean
) {
    companion object {
        fun default() = HomeState(
            status = HomeStatus.Initial,
            logs = "",
            inputFile = null,
            outputFile = null,
            codec = null,
            channels = null,
            vbr = null,
            bitrate = null,
            sampleRate = null,
            preset = null,
            crf = null,
            resolution = null,
            pixelFormat = null,
            noAudio = false
        )
    }
}

sealed interface HomeStatus {
    data object Initial: HomeStatus
    data object Loading: HomeStatus
    data object FileExists: HomeStatus
    data class Progress(val percentage: Float, val speed: String): HomeStatus
    data class Complete(val startTime: String, val finishTime: String, val duration: String): HomeStatus
    data class Error(val message: String? = null): HomeStatus
}