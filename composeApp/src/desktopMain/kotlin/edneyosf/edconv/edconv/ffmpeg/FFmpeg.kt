package edneyosf.edconv.edconv.ffmpeg

import edneyosf.edconv.edconv.common.CompressionType
import edneyosf.edconv.edconv.common.MediaType

class FFmpeg private constructor(
    val logLevel: String,
    val mediaType: MediaType,
    val codec: String,
    var bitrate: String? = null,
    var vbr: String? = null,
    val sampleRate: String? = null,
    val channels: String? = null,
    val preset: String? = null,
    val crf: Int? = null,
    var profile: String? = null,
    val pixelFormat: String? = null,
    val filter: String? = null,
    val noAudio: Boolean = false,
    val noSubtitle: Boolean = false,
    val noMetadata: Boolean = false,
    val custom: List<String>? = null
) {

    companion object {
        fun createAudio(
            logLevel: String, codec: String, compressionType: CompressionType?, sampleRate: String? = null,
            vbr: String?, bitrate: String?, channels: String? = null, filter: String? = null,
            noMetadata: Boolean = false, custom: List<String>? = null): FFmpeg {

            return FFmpeg(
                logLevel = logLevel,
                mediaType = MediaType.AUDIO,
                codec = codec,
                vbr = if(compressionType == CompressionType.VBR) vbr else null,
                bitrate = if(compressionType == CompressionType.CBR) bitrate else null,
                sampleRate = sampleRate,
                channels = channels,
                filter = filter,
                noMetadata = noMetadata,
                custom = custom
            )
        }

        fun createVideo(
            logLevel: String, codec: String, compressionType: CompressionType?, preset: String, crf: Int?,
            bitrate: String?, profile: String? = null, pixelFormat: String? = null, filter: String? = null,
            noAudio: Boolean = false, noSubtitle: Boolean = false, noMetadata: Boolean = false): FFmpeg {

            return FFmpeg(
                logLevel = logLevel,
                mediaType = MediaType.VIDEO,
                codec = codec,
                preset = preset,
                crf = if(compressionType == CompressionType.CRF) crf else null,
                bitrate = if(compressionType == CompressionType.CBR) bitrate else null,
                pixelFormat = pixelFormat,
                profile = profile,
                filter = filter,
                noAudio = noAudio,
                noSubtitle = noSubtitle,
                noMetadata = noMetadata
            )
        }
    }

    fun build(): String {
        val data = mutableListOf<String>()

        data.addCmd(FFmpegArgs.LOG_LEVEL, logLevel)
        if(isVideo()) data.addCmd(FFmpegArgs.MAP, "0:v:0")
        if(!noAudio) data.addCmd(FFmpegArgs.MAP, if(isVideo()) "0:a" else "0:a:0")
        if(isVideo() && !noSubtitle) data.addCmd(FFmpegArgs.MAP, "0:s?")
        data.addCmd(codecArg(), codec)
        data.addCmd(bitRateArg(), bitrate)
        data.addCmd(FFmpegArgs.VBR, vbr)
        data.addCmd(FFmpegArgs.SAMPLE_RATE, sampleRate)
        data.addCmd(FFmpegArgs.CHANNELS, channels)
        data.addCmd(FFmpegArgs.PRESET, preset)
        data.addCmd(FFmpegArgs.CRF, crf?.toString())
        data.addCmd(profileArg(), profile)
        data.addCmd(FFmpegArgs.PIXEL_FORMAT, pixelFormat)
        data.addCmd(filterArg(), filter)

        if(noAudio) data.add(FFmpegArgs.NO_AUDIO)
        if(noSubtitle) data.add(FFmpegArgs.NO_SUBTITLE)
        if(isVideo() && noMetadata) data.addCmd(FFmpegArgs.MAP_METADATA, "-1")

        custom?.let { data.addAll(custom) }

        if(isVideo() && !noAudio) data.addCmd(FFmpegArgs.CODEC_AUDIO, FFmpegArgs.COPY)
        if(isVideo() && !noSubtitle) data.addCmd(FFmpegArgs.CODEC_SUBTITLES, FFmpegArgs.COPY)

        return data.joinToString(separator = " ")
    }

    private fun codecArg() = if(isAudio()) FFmpegArgs.CODEC_AUDIO else FFmpegArgs.CODEC_VIDEO
    private fun bitRateArg() = if(isAudio()) FFmpegArgs.BITRATE_AUDIO else FFmpegArgs.BITRATE_VIDEO
    private fun profileArg() = if(isAudio()) FFmpegArgs.PROFILE_AUDIO else FFmpegArgs.PROFILE_VIDEO
    private fun filterArg() = if(isAudio()) FFmpegArgs.FILTER_AUDIO else FFmpegArgs.FILTER_VIDEO

    private fun MutableList<String>.addCmd(param: String, value: String?) = value?.let {
        add(param)
        add(it)
    }

    private fun isAudio() = mediaType == MediaType.AUDIO
    private fun isVideo() = mediaType == MediaType.VIDEO
}