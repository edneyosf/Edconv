package edconv.ffmpeg

import edconv.common.MediaType

class FFmpeg private constructor(
    val source: String,
    val logLevel: String,
    val mediaType: MediaType,
    val input: String,
    val output: String,
    val codec: String,
    var bitRate: String? = null,
    var vbr: String? = null,
    val sampleRate: String? = null,
    val channels: String? = null,
    val preset: String? = null,
    val crf: String? = null,
    var profile: String? = null,
    val pixelFormat: String? = null,
    val filter: String? = null,
    val noAudio: Boolean = false
) {

    companion object {
        fun createAudio(
            source: String, logLevel: String, input: String, output: String, codec: String,
            sampleRate: String? = null, channels: String? = null, filter: String? = null): FFmpeg {

            return FFmpeg(
                source = source,
                logLevel = logLevel,
                mediaType = MediaType.AUDIO,
                input = input,
                output = output,
                codec = codec,
                sampleRate = sampleRate,
                channels = channels,
                filter = filter,
            )
        }

        fun createVideo(
            source: String, logLevel: String, input: String, output: String, codec: String, preset: String,
            crf: String, pixelFormat: String? = null, filter: String? = null,
            noAudio: Boolean = false): FFmpeg {

            return FFmpeg(
                source = source,
                logLevel = logLevel,
                mediaType = MediaType.VIDEO,
                input = input,
                output = output,
                codec = codec,
                preset = preset,
                crf = crf,
                pixelFormat = pixelFormat,
                filter = filter,
                noAudio = noAudio,
            )
        }
    }

    fun build(): List<String> {
        val data = mutableListOf(
            source,
            FFmpegArgs.LOG_LEVEL, logLevel,
            FFmpegArgs.INPUT, input,
            codecArg(), codec
        )

        data.addCmd(bitRateArg(), bitRate)
        data.addCmd(FFmpegArgs.VBR, vbr)
        data.addCmd(FFmpegArgs.SAMPLE_RATE, sampleRate)
        data.addCmd(FFmpegArgs.CHANNELS, channels)
        data.addCmd(FFmpegArgs.PRESET, preset)
        data.addCmd(FFmpegArgs.CRF, crf)
        data.addCmd(profileArg(), profile)
        data.addCmd(FFmpegArgs.PIXEL_FORMAT, pixelFormat)
        data.addCmd(filterArg(), filter)

        if(noAudio) data.add(FFmpegArgs.NO_AUDIO_VIDEO)

        data.add(output)

        return data
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