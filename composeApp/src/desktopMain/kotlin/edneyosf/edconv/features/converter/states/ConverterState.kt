package edneyosf.edconv.features.converter.states

import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.ffmpeg.common.*

data class ConverterState(
    val status: ConverterStatusState = ConverterStatusState.Initial,
    val dialog: ConverterDialogState = ConverterDialogState.None,
    val input: InputMedia? = null,
    val type: MediaType? = null,
    val output: Pair<String, String>? = null,
    val queueSize: Int = 0,
    val indexAudio: Int? = 0,
    val indexVideo: Int? = 0,
    val encoderAudio: Encoder? = null,
    val encoderVideo: Encoder? = null,
    val compressionTypeAudio: CompressionType? = null,
    val compressionTypeVideo: CompressionType? = null,
    val bitrateAudio: Bitrate? = null,
    val bitrateVideo: Bitrate? = null,
    val bitrateControlAudio: Int? = null,
    val bitrateControlVideo: Int? = null,
    val presetVideo: String? = null,
    val channels: Channels? = null,
    val resolution: Resolution? = null,
    val pixelFormat: PixelFormat? = null,
    val sampleRate: SampleRate? = null,
    val noSubtitle: Boolean = false,
    val noMetadata: Boolean = false,
    val hdr10ToSdr: Boolean = false,
    val noChapters: Boolean = false
)