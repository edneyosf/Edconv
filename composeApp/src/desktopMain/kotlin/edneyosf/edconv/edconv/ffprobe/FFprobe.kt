package edneyosf.edconv.edconv.ffprobe

import edneyosf.edconv.core.ConfigManager
import edneyosf.edconv.edconv.common.FFLogLevel
import edneyosf.edconv.edconv.common.MediaType
import edneyosf.edconv.edconv.core.data.AudioData
import edneyosf.edconv.edconv.core.data.ContentTypeData
import edneyosf.edconv.edconv.core.data.InputMedia
import edneyosf.edconv.edconv.core.data.SubtitleData
import edneyosf.edconv.edconv.core.data.VideoData
import kotlinx.serialization.json.Json
import edneyosf.edconv.edconv.ffprobe.FFprobeArgs.SHOW_ENTRIES
import edneyosf.edconv.edconv.ffprobe.FFprobeArgs.STREAM_TAGS
import edneyosf.edconv.edconv.ffprobe.FFprobeArgs.FORMAT
import edneyosf.edconv.edconv.ffprobe.FFprobeArgs.STREAM
import edneyosf.edconv.edconv.ffprobe.FFprobeArgs.LOG_LEVEL
import edneyosf.edconv.edconv.ffprobe.FFprobeArgs.OF
import edneyosf.edconv.edconv.ffprobe.FFprobeArgs.JSON
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object FFprobe {

    private const val AUDIO_TYPE = "audio"
    private const val VIDEO_TYPE = "video"
    private const val SUBTITLE_TYPE = "subtitle"

    fun analyze(file: File): InputMedia? {
        var inputMedia: InputMedia? = null

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
            val output = json.decodeFromString<OutputFFprobe>(jsonText)
            val subtitleStreams = mutableListOf<SubtitleData>()
            val videoStreams = mutableListOf<VideoData>()
            val audioStreams = mutableListOf<AudioData>()
            var hasSubtitle = false
            var hasVideo = false
            var hasAudio = false

            output.streams.forEach { stream ->
                when (stream.codecType) {
                    VIDEO_TYPE -> {
                        val frameRate = stream.frameRate
                            ?.split('/')
                            ?.takeIf { it.size == 2 && it[1] != "0" }
                            ?.let { it[0].toFloat() / it[1].toFloat() }
                            ?: 0.0f

                        hasVideo = true
                        videoStreams.add(
                            VideoData(
                                codec = stream.codecLongName,
                                title = stream.tags?.title,
                                language = stream.tags?.language,
                                profile = stream.profile,
                                width = stream.width,
                                height = stream.height,
                                bitDepth = stream.bitDepth,
                                pixFmt = stream.pixFmt,
                                fps = frameRate,
                                level = stream.level,
                                filmGrain = stream.filmGrain == 1,
                                displayAspectRatio = stream.displayAspectRatio,
                                fieldOrder = stream.fieldOrder
                            )
                        )
                    }

                    AUDIO_TYPE -> {
                        hasAudio = true
                        audioStreams.add(
                            AudioData(
                                codec = stream.codecLongName,
                                title = stream.tags?.title,
                                language = stream.tags?.language,
                                profile = stream.profile,
                                channels = stream.channels,
                                sampleRate = stream.sampleRate,
                                bitDepth = stream.bitDepth
                            )
                        )
                    }

                    SUBTITLE_TYPE -> {
                        hasSubtitle = true
                        subtitleStreams.add(
                            SubtitleData(
                                codec = stream.codecLongName,
                                title = stream.tags?.title,
                                language = stream.tags?.language
                            )
                        )
                    }
                }
            }

            val type = when {
                hasAudio && !hasVideo -> MediaType.AUDIO
                hasVideo -> MediaType.VIDEO
                else -> return null
            }

            val format = output.format

            inputMedia = InputMedia(
                path = file.path,
                type = type,
                formatName = format.formatLongName,
                contentType = ContentTypeData(audio = hasAudio, video = hasVideo, subtitle = hasSubtitle),
                duration = format.duration?.let { (it * 1000).toLong() },
                bitRate = format.bitRate,
                size = file.length(),
                videoStreams = videoStreams,
                audioStreams = audioStreams,
                subtitleStreams = subtitleStreams
            )
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        return inputMedia
    }

    private fun formatEntries(): String {
        val entries = mutableListOf("duration", "format_long_name", "bit_rate")

        return "$FORMAT=${entries.joinToString(",")}"
    }

    private fun streamEntries(): String {
        val entries = mutableListOf(
            "codec_type", "codec_name", "codec_long_name", "width", "height", "level", "film_grain",
            "bits_per_raw_sample", "sample_rate", "channels", "r_frame_rate", "pix_fmt", "profile", "field_order",
            "display_aspect_ratio"
        )

        return "$STREAM=${entries.joinToString(",")}"
    }

    private fun streamTagsEntries(): String {
        val entries = mutableListOf("title", "language")

        return "$STREAM_TAGS=${entries.joinToString(",")}"
    }
}