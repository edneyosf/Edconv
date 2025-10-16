package edneyosf.edconv.ffmpeg.common

import edneyosf.edconv.ffmpeg.ffmpeg.FFmpegArgs

enum class Channels(val value: String, val text: String) {
    MONO(value = "1", text = "Mono"),
    STEREO(value = "2", text = "Stereo"),
    SURROUND_51(value = "6", text = "Surround (5.1)"),
    SURROUND_71(value = "8", text = "Surround (7.1)");

    companion object {
        fun getAll() = entries.toList()

        fun customArguments(channels: Int?, inputChannels: Int?, encoder: Encoder): List<String>? {
            val value = channels ?: inputChannels

            return when (encoder) {
                Encoder.OPUS -> {
                    if (value == SURROUND_51.value.toInt() || value == SURROUND_71.value.toInt()) {
                        listOf(FFmpegArgs.MAPPING_FAMILY, "1")
                    }
                    else null
                }
                else -> null
            }
        }

        fun fromValue(value: Int?) = when(value) {
            1 -> MONO
            2 -> STEREO
            6 -> SURROUND_51
            8 -> SURROUND_71
            else -> null
        }
    }
}