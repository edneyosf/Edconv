package edneyosf.edconv.ffmpeg.common

import edneyosf.edconv.ffmpeg.ffmpeg.FFmpegArgs

enum class Channels(val value: String, val text: String) {
    MONO(value = "1", text = "Mono"),
    STEREO(value = "2", text = "Stereo"),
    SURROUND_51(value = "6", text = "Surround (5.1)"),
    SURROUND_71(value = "8", text = "Surround (7.1)");

    fun downmixingFilter(sourceChannels: Int): String? {
        var filter: String? = null

        if(sourceChannels == SURROUND_51.value.toInt() && this == STEREO) {
            filter = DOWNMIXING_51_20_FILTER
        }

        return filter
    }

    companion object {
        fun getAll() = entries.toList()

        fun customArguments(channels: Int?, inputChannels: Int?, codec: Codec): List<String>? {
            val value = channels ?: inputChannels

            return when (codec) {
                Codec.OPUS -> {
                    if (value == SURROUND_51.value.toInt() || value == SURROUND_71.value.toInt()) {
                        listOf(FFmpegArgs.MAPPING_FAMILY, "1")
                    }
                    else null
                }
                else -> null
            }
        }
    }
}

private const val DOWNMIXING_51_20_FILTER = "lowpass=c=LFE:f=120,volume=1.6,pan=stereo|FL=0.8*FL+0.5*FC+0.6*BL+0.4*LFE|FR=0.8*FR+0.5*FC+0.6*BR+0.4*LFE"