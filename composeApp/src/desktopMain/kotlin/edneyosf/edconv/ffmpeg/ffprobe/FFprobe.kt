package edneyosf.edconv.ffmpeg.ffprobe

import edneyosf.edconv.core.ConfigManager
import edneyosf.edconv.ffmpeg.common.FFLogLevel
import edneyosf.edconv.ffmpeg.data.FFprobeData
import edneyosf.edconv.ffmpeg.data.InputMediaData
import edneyosf.edconv.ffmpeg.data.mappers.toInputMediaData
import kotlinx.serialization.json.Json
import edneyosf.edconv.ffmpeg.ffprobe.FFprobeArgs.SHOW_ENTRIES
import edneyosf.edconv.ffmpeg.ffprobe.FFprobeArgs.STREAM_TAGS
import edneyosf.edconv.ffmpeg.ffprobe.FFprobeArgs.FORMAT
import edneyosf.edconv.ffmpeg.ffprobe.FFprobeArgs.STREAM
import edneyosf.edconv.ffmpeg.ffprobe.FFprobeArgs.LOG_LEVEL
import edneyosf.edconv.ffmpeg.ffprobe.FFprobeArgs.OF
import edneyosf.edconv.ffmpeg.ffprobe.FFprobeArgs.JSON
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class FFprobe(private val file: File) {

    fun analyze(): InputMediaData? {
        var inputMedia: InputMediaData? = null

        try {
            val command = arrayOf(
                ConfigManager.getFFprobePath(),
                LOG_LEVEL, FFLogLevel.ERROR,
                SHOW_ENTRIES, formatEntries(),
                SHOW_ENTRIES, streamEntries(),
                SHOW_ENTRIES, streamTagsEntries(),
                OF, JSON,
                file.absolutePath
            )
            val process = ProcessBuilder(*command)
                .redirectErrorStream(true)
                .start()

            val input = InputStreamReader(process.inputStream)
            val reader = BufferedReader(input)
            val jsonText = reader.readText()
            val json = Json { ignoreUnknownKeys = true }
            val output = json.decodeFromString<FFprobeData>(string = jsonText)

            inputMedia = output.toInputMediaData(inputFile = file)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        return inputMedia
    }

    private fun formatEntries(): String {
        val entries = mutableListOf("duration", "format_long_name", "bit_rate")

        return "$FORMAT=${entries.joinToString(separator = ",")}"
    }

    private fun streamEntries(): String {
        val entries = mutableListOf(
            "codec_type", "codec_name", "codec_long_name", "width", "height", "level", "film_grain",
            "bits_per_raw_sample", "sample_rate", "channels", "r_frame_rate", "pix_fmt", "profile", "field_order",
            "display_aspect_ratio"
        )

        return "$STREAM=${entries.joinToString(separator = ",")}"
    }

    private fun streamTagsEntries(): String {
        val entries = mutableListOf("title", "language")

        return "$STREAM_TAGS=${entries.joinToString(separator = ",")}"
    }
}