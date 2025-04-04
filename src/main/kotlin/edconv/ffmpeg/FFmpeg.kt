package edconv.ffmpeg

import edconv.common.MediaType

class FFmpeg private constructor(
    val logLevel: String,
    val mediaType: MediaType,
    val codec: String,
    var bitRate: String? = null,
    var vbr: String? = null,
    val sampleRate: String? = null,
    val channels: String? = null,
    val preset: String? = null,
    val crf: Int? = null,
    var profile: String? = null,
    val pixelFormat: String? = null,
    val filter: String? = null,
    val noAudio: Boolean = false,
    val noVideo: Boolean = false
) {

    companion object {
        fun createAudio(
            logLevel: String, codec: String, sampleRate: String? = null, channels: String? = null,
            filter: String? = null, noVideo: Boolean = false): FFmpeg {

            return FFmpeg(
                logLevel = logLevel,
                mediaType = MediaType.AUDIO,
                codec = codec,
                sampleRate = sampleRate,
                channels = channels,
                filter = filter,
                noVideo = noVideo,
            )
        }

        fun createVideo(
            logLevel: String, codec: String, preset: String, crf: Int, profile: String? = null,
            pixelFormat: String? = null, filter: String? = null, noAudio: Boolean = false): FFmpeg {

            return FFmpeg(
                logLevel = logLevel,
                mediaType = MediaType.VIDEO,
                codec = codec,
                preset = preset,
                crf = crf,
                pixelFormat = pixelFormat,
                profile = profile,
                filter = filter,
                noAudio = noAudio,
            )
        }
    }

    fun build(): String {
        val data = mutableListOf(
            FFmpegArgs.LOG_LEVEL, logLevel,
            codecArg(), codec
        )

        data.addCmd(bitRateArg(), bitRate)
        data.addCmd(FFmpegArgs.VBR, vbr)
        data.addCmd(FFmpegArgs.SAMPLE_RATE, sampleRate)
        data.addCmd(FFmpegArgs.CHANNELS, channels)
        data.addCmd(FFmpegArgs.PRESET, preset)
        data.addCmd(FFmpegArgs.CRF, crf?.toString())
        data.addCmd(profileArg(), profile)
        data.addCmd(FFmpegArgs.PIXEL_FORMAT, pixelFormat)
        data.addCmd(filterArg(), filter)

        if(noAudio) data.add(FFmpegArgs.NO_AUDIO)
        if(noVideo) data.add(FFmpegArgs.NO_VIDEO)

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
}