package edneyosf.edconv.ffmpeg.ffmpeg

import edneyosf.edconv.ffmpeg.common.CompressionType
import edneyosf.edconv.ffmpeg.common.MediaType
import edneyosf.edconv.ffmpeg.ffmpeg.extensions.addCmd

class FFmpeg private constructor(
    val logLevel: String,
    val mediaType: MediaType,
    val indexVideo: Int? = null,
    val indexAudio: Int? = null,
    val encoderAudio: String? = null,
    val encoderVideo: String? = null,
    var bitrateAudio: String? = null,
    var bitrateVideo: String? = null,
    var bitrateControlAudio: Int? = null,
    var bitrateControlVideo: Int? = null,
    val sampleRate: String? = null,
    val channels: String? = null,
    val presetVideo: String? = null,
    var profileVideo: String? = null,
    val pixelFormat: String? = null,
    val filterAudio: String? = null,
    val filterVideo: String? = null,
    val noSubtitle: Boolean = false,
    val noMetadata: Boolean = false,
    val noChapters: Boolean = false,
    val custom: List<String>? = null,
    val uncompressedVideo: Boolean = false,
    val uncompressedAudio: Boolean = false
) {

    companion object {
        fun createAudio(
            logLevel: String, indexAudio: Int?, encoder: String, compressionType: CompressionType?,
            sampleRate: String? = null, bitrateControl: Int?, bitrate: String?, channels: String? = null,
            filter: String? = null, noMetadata: Boolean = false, custom: List<String>? = null,
            noChapters: Boolean = false
        ): FFmpeg {

            return FFmpeg(
                logLevel = logLevel,
                mediaType = MediaType.AUDIO,
                indexAudio = indexAudio,
                encoderAudio = encoder,
                bitrateControlAudio = if(compressionType == CompressionType.VBR) bitrateControl else null,
                bitrateAudio = if(compressionType == CompressionType.CBR) bitrate else null,
                uncompressedAudio = compressionType == CompressionType.COPY,
                sampleRate = sampleRate,
                channels = channels,
                filterAudio = filter,
                noMetadata = noMetadata,
                noChapters = noChapters,
                custom = custom
            )
        }

        fun createVideo(
            logLevel: String, indexVideo: Int?, indexAudio: Int?, encoderVideo: String, encoderAudio: String?,
            compressionTypeVideo: CompressionType?, compressionTypeAudio: CompressionType?,presetVideo: String,
            bitrateControlVideo: Int?, bitrateControlAudio: Int?, bitrateVideo: String?, bitrateAudio: String?,
            profileVideo: String? = null, pixelFormat: String? = null, filterVideo: String? = null,
            filterAudio: String? = null, sampleRate: String? = null, channels: String? = null,
            noSubtitle: Boolean = false, noMetadata: Boolean = false, custom: List<String>? = null,
            noChapters: Boolean = false
        ): FFmpeg {

            return FFmpeg(
                logLevel = logLevel,
                mediaType = MediaType.VIDEO,
                indexVideo = indexVideo,
                indexAudio = indexAudio,
                encoderVideo = encoderVideo,
                encoderAudio = encoderAudio,
                presetVideo = presetVideo,
                bitrateControlVideo = if(compressionTypeVideo == CompressionType.CRF) bitrateControlVideo else null,
                bitrateVideo = if(compressionTypeVideo == CompressionType.CBR) bitrateVideo else null,
                bitrateControlAudio = if(compressionTypeAudio == CompressionType.VBR) bitrateControlAudio else null,
                bitrateAudio = if(compressionTypeAudio == CompressionType.CBR) bitrateAudio else null,
                uncompressedAudio = compressionTypeAudio == CompressionType.COPY,
                uncompressedVideo = compressionTypeVideo == CompressionType.COPY,
                pixelFormat = pixelFormat,
                profileVideo = profileVideo,
                filterVideo = filterVideo,
                filterAudio = filterAudio,
                sampleRate = sampleRate,
                channels = channels,
                noSubtitle = noSubtitle,
                noMetadata = noMetadata,
                noChapters = noChapters,
                custom = custom
            )
        }
    }

    fun build(): String {
        val data = mutableListOf<String>()

        data.addCmd(param = FFmpegArgs.LOG_LEVEL, value = logLevel)
        data.add(FFmpegArgs.STATS)
        if(isVideo() && !noSubtitle) data.addCmd(param = FFmpegArgs.MAP, value = "0:s?")

        if(indexVideo != -1) {
            data.addCmd(param = FFmpegArgs.MAP, value = if(indexVideo == null) "0:v" else "0:v:$indexVideo")

            if(!uncompressedVideo) {
                data.addCmd(param = FFmpegArgs.ENCODER_VIDEO, value = encoderVideo)
                data.addCmd(param = FFmpegArgs.BITRATE_VIDEO, value = bitrateVideo)
                data.addCmd(param = FFmpegArgs.PRESET, value = presetVideo)
                data.addCmd(param = FFmpegArgs.CRF, value = bitrateControlVideo?.toString())
                data.addCmd(param = FFmpegArgs.PROFILE_VIDEO, value = profileVideo)
                data.addCmd(param = FFmpegArgs.PIXEL_FORMAT, value = pixelFormat)
                data.addCmd(param = FFmpegArgs.FILTER_VIDEO, value = filterVideo)
            }
            else {
                data.addCmd(param = FFmpegArgs.ENCODER_VIDEO, value = FFmpegArgs.COPY)
            }
        }
        else {
            data.addCmd(param = FFmpegArgs.MAP, value = "-0:v")
        }

        if(indexAudio != -1) {
            data.addCmd(param = FFmpegArgs.MAP, value = if(indexAudio == null) "0:a" else "0:a:$indexAudio")

            if(!uncompressedAudio) {
                data.addCmd(param = FFmpegArgs.ENCODER_AUDIO, value = encoderAudio)
                data.addCmd(param = FFmpegArgs.BITRATE_AUDIO, value = bitrateAudio)
                data.addCmd(param = FFmpegArgs.VBR, value = bitrateControlAudio?.toString())
                data.addCmd(param = FFmpegArgs.SAMPLE_RATE, value = sampleRate)
                data.addCmd(param = FFmpegArgs.CHANNELS, value = channels)
                data.addCmd(param = FFmpegArgs.FILTER_AUDIO, value = filterAudio)
            }
            else {
                data.addCmd(param = FFmpegArgs.ENCODER_AUDIO, value = FFmpegArgs.COPY)
            }
        }
        else {
            data.addCmd(param = FFmpegArgs.MAP, value = "-0:a")
        }

        if(noSubtitle) data.add(FFmpegArgs.NO_SUBTITLE)
        if(noMetadata) data.addCmd(param = FFmpegArgs.MAP_METADATA, value = "-1")
        if(noChapters) data.addCmd(param = FFmpegArgs.MAP_CHAPTERS, value = "-1")

        custom?.let { data.addAll(custom) }

        if(isVideo() && !noSubtitle) data.addCmd(param = FFmpegArgs.ENCODER_SUBTITLES, value = FFmpegArgs.COPY)

        return data.joinToString(separator = " ")
    }

    private fun isVideo() = mediaType == MediaType.VIDEO
}