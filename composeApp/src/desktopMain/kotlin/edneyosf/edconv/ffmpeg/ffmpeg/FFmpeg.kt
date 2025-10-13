package edneyosf.edconv.ffmpeg.ffmpeg

import edneyosf.edconv.ffmpeg.common.CompressionType
import edneyosf.edconv.ffmpeg.common.MediaType
import edneyosf.edconv.ffmpeg.ffmpeg.extensions.addCmd

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
    val custom: List<String>? = null,
    val uncompressedVideo: Boolean = false,
    val uncompressedAudio: Boolean = false
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
                uncompressedAudio = compressionType == CompressionType.COPY,
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
                uncompressedVideo = compressionType == CompressionType.COPY,
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

        data.addCmd(param = FFmpegArgs.LOG_LEVEL, value = logLevel)
        data.add(FFmpegArgs.STATS)
        if(isVideo()) data.addCmd(param = FFmpegArgs.MAP, value = "0:v:0")
        if(!noAudio) data.addCmd(param = FFmpegArgs.MAP, value = if(isVideo()) "0:a?" else "0:a:0")
        if(isVideo() && !noSubtitle) data.addCmd(param = FFmpegArgs.MAP, value = "0:s?")

        if(!uncompressedVideo && !uncompressedAudio) {
            data.addCmd(param = codecArg(), value = codec)
            data.addCmd(param = bitRateArg(), value = bitrate)
            data.addCmd(param = FFmpegArgs.VBR, value = vbr)
            data.addCmd(param = FFmpegArgs.SAMPLE_RATE, value = sampleRate)
            data.addCmd(param = FFmpegArgs.CHANNELS, value = channels)
            data.addCmd(param = FFmpegArgs.PRESET, value = preset)
            data.addCmd(param = FFmpegArgs.CRF, value = crf?.toString())
            data.addCmd(param = profileArg(), value = profile)
            data.addCmd(param = FFmpegArgs.PIXEL_FORMAT, value = pixelFormat)
            data.addCmd(param = filterArg(), value = filter)
        } else {
            data.addCmd(param = codecArg(), value = FFmpegArgs.COPY)
        }

        if(noAudio) data.add(FFmpegArgs.NO_AUDIO)
        if(noSubtitle) data.add(FFmpegArgs.NO_SUBTITLE)
        if(noMetadata) data.addCmd(param = FFmpegArgs.MAP_METADATA, value = "-1")

        custom?.let { data.addAll(custom) }

        if(isVideo() && !noAudio) data.addCmd(param = FFmpegArgs.CODEC_AUDIO, value = FFmpegArgs.COPY)
        if(isVideo() && !noSubtitle) data.addCmd(param = FFmpegArgs.CODEC_SUBTITLES, value = FFmpegArgs.COPY)

        return data.joinToString(separator = " ")
    }

    private fun codecArg() = if(isAudio()) FFmpegArgs.CODEC_AUDIO else FFmpegArgs.CODEC_VIDEO
    private fun bitRateArg() = if(isAudio()) FFmpegArgs.BITRATE_AUDIO else FFmpegArgs.BITRATE_VIDEO
    private fun profileArg() = if(isAudio()) FFmpegArgs.PROFILE_AUDIO else FFmpegArgs.PROFILE_VIDEO
    private fun filterArg() = if(isAudio()) FFmpegArgs.FILTER_AUDIO else FFmpegArgs.FILTER_VIDEO

    private fun isAudio() = mediaType == MediaType.AUDIO
    private fun isVideo() = mediaType == MediaType.VIDEO
}