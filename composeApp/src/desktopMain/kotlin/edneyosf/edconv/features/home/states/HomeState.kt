package edneyosf.edconv.features.home.states

import edneyosf.edconv.edconv.common.*
import edneyosf.edconv.edconv.core.data.MediaData

data class HomeState(
    val status: HomeStatus,
    val dialog: HomeDialog,
    val logs: String,
    val cmd: String,
    val input: MediaData?,
    val output: String?,
    val codec: Codec?,
    val compression: CompressionType?,
    val channels: Channels?,
    val vbr: Int?,
    val bitrate: Bitrate?,
    val sampleRate: SampleRate?,
    val preset: String?,
    val crf: Int?,
    val resolution: Resolution?,
    val pixelFormat: PixelFormat?,
    val noAudio: Boolean,
    val noSubtitle: Boolean,
    val noMetadata: Boolean
) {
    companion object {
        fun default() = HomeState(
            status = HomeStatus.Initial,
            dialog = HomeDialog.None,
            logs = "",
            cmd = "",
            input = null,
            output = null,
            codec = null,
            compression = null,
            channels = null,
            vbr = null,
            bitrate = null,
            sampleRate = null,
            preset = null,
            crf = null,
            resolution = null,
            pixelFormat = null,
            noAudio = false,
            noSubtitle = false,
            noMetadata = false
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

sealed interface HomeDialog {
    data object None: HomeDialog
    data object Settings: HomeDialog
    data class MediaInfo(val mediaData: MediaData): HomeDialog
}