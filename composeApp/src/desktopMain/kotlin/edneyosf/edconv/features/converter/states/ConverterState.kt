package edneyosf.edconv.features.converter.states

import edneyosf.edconv.ffmpeg.common.*
import edneyosf.edconv.ffmpeg.data.InputMedia

data class ConverterState(
    val input: InputMedia,
    val mediaType: MediaType,
    val status: ConverterStatus = ConverterStatus.Initial,
    val dialog: ConverterDialog = ConverterDialog.None,
    val logs: String = "",
    val cmd: String = "",
    val output: String? = null,
    val codec: Codec? = null,
    val compression: CompressionType? = null,
    val channels: Channels? = null,
    val vbr: Int? = null,
    val bitrate: Bitrate? = null,
    val sampleRate: SampleRate? = null,
    val preset: String? = null,
    val crf: Int? = null,
    val resolution: Resolution? = null,
    val pixelFormat: PixelFormat? = null,
    val noAudio: Boolean = false,
    val noSubtitle: Boolean = false,
    val noMetadata: Boolean = false
)

sealed interface ConverterStatus {
    data object Initial: ConverterStatus
    data object Loading: ConverterStatus
    data object FileExists: ConverterStatus
    data class Progress(val percentage: Float, val speed: String): ConverterStatus
    data class Complete(val startTime: String, val finishTime: String, val duration: String): ConverterStatus
    data class Error(val message: String? = null): ConverterStatus
}

sealed interface ConverterDialog {
    data object None: ConverterDialog
    data object Settings: ConverterDialog
    data class MediaInfo(val inputMedia: InputMedia): ConverterDialog
}