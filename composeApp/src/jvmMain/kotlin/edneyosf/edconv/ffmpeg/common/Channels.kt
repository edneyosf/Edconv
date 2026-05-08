package edneyosf.edconv.ffmpeg.common

import edneyosf.edconv.ffmpeg.ffmpeg.FFmpegArgs

enum class Channels(val value: String, val text: String) {
    MONO(value = "1", text = "Mono"),
    STEREO(value = "2", text = "Stereo"),
    SURROUND_51(value = "6", text = "Surround (5.1)"),
    SURROUND_71(value = "8", text = "Surround (7.1)");

    companion object {
        fun getFromEncoder(encoder: Encoder) = when (encoder) {
            Encoder.OPUS, Encoder.AAC, Encoder.AAC_FDK, Encoder.EAC3, Encoder.FLAC -> entries.toList()
            Encoder.AC3 -> entries.filter { it != SURROUND_71 }
            Encoder.MP3 -> entries.filter { it != SURROUND_51 && it != SURROUND_71 }
            else -> emptyList()
        }

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