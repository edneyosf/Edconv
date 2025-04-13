package edneyosf.edconv.edconv.utils

import edneyosf.edconv.core.ConfigManager
import edneyosf.edconv.edconv.core.data.ContentTypeData
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object MediaUtils {

    private const val AUDIO_TYPE = "audio"
    private const val VIDEO_TYPE = "video"
    private const val SUBTITLE_TYPE = "subtitle"

    /**
     * Command: ffprobe -v error -select_streams v:0 -show_entries stream=width,height -of csv=p=0 <file_path>
     * Result model: "1920,1080"
     */
    fun getVideoResolution(file: File): Pair<Int, Int>? {
        var resolution: Pair<Int, Int>? = null
        val command = arrayOf(
            ConfigManager.getFFprobePath(),
            "-v", "error",
            "-select_streams", "v:0",
            "-show_entries", "stream=width,height",
            "-of", "csv=p=0",
            file.absolutePath
        )

        try {
            val reader = getReader(command)
            val line = reader.readLine()
            val data = line.trim().split(",")
            val width = data.first().toInt()
            val height = data.last().toInt()

            resolution = Pair(width, height)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        return resolution
    }

    /**
     * Command: ffprobe -v error -show_entries format=duration -of csv=p=0 <file_path>
     * Result model: "1382.255000"
     */
    fun getDuration(file: File): Long? {
        var duration: Long? = null
        val command = arrayOf(
            ConfigManager.getFFprobePath(),
            "-v", "error",
            "-show_entries", "format=duration",
            "-of", "csv=p=0",
            file.absolutePath
        )

        try {
            val reader = getReader(command)
            val line = reader.readLine()
            val durationInSeconds = line.trim().toDouble()

            duration = (durationInSeconds * 1000).toLong()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        return duration
    }

    /**
     * Command: ffprobe -v error -show_entries stream=codec_type -of csv=p=0 <file_path>
     * Result model:
     * "video
     *  audio
     *  subtitle"
     */
    fun getContentType(file: File): ContentTypeData {
        var contentType = ContentTypeData()
        val command = arrayOf(
            ConfigManager.getFFprobePath(),
            "-v", "error",
            "-show_entries", "stream=codec_type",
            "-of", "csv=p=0",
            file.absolutePath
        )

        try {
            val reader = getReader(command)
            val lines = reader.readLines()
            val output =  lines.map { it.trim() }

            contentType = contentType.copy(
                audio = AUDIO_TYPE in output,
                video = VIDEO_TYPE in output,
                subtitle = SUBTITLE_TYPE in output
            )
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        return contentType
    }

    /**
     * Command: ffprobe -v error -select_streams a:0 -show_entries stream=channels -of csv=p=0 <file_path>
     * Result model: "6"
     */
    fun getAudioChannels(file: File): Int? {
        var channels: Int? = null
        val command = arrayOf(
            ConfigManager.getFFprobePath(),
            "-v", "error",
            "-select_streams", "a:0",
            "-show_entries", "stream=channels",
            "-of", "csv=p=0",
            file.absolutePath
        )

        try {
            val reader = getReader(command)
            val line = reader.readLine()
            channels = line.trim().toInt()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        return channels
    }

    fun getSize(file: File) = file.length()

    private fun getReader(command: Array<String>): BufferedReader {
        val process = ProcessBuilder(*command)
            .redirectErrorStream(true)
            .start()

        return BufferedReader(InputStreamReader(process.inputStream))
    }
}