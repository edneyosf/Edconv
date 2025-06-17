package edneyosf.edconv.features.converter.states

import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.ffmpeg.common.*

data class ConverterState(
    val status: ConverterStatusState = ConverterStatusState.Initial,
    val dialog: ConverterDialogState = ConverterDialogState.None,
    val command: String = "",
    val input: InputMedia,
    val type: MediaType,
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
    val noMetadata: Boolean = false,
    val pendingQueueSize: Int = 0
)