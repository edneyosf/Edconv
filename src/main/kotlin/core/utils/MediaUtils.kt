package core.utils

import core.Configs
import edconv.common.MediaType
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object MediaUtils {

    /**
     * Command: ffprobe -v error -select_streams v:0 -show_entries stream=width,height -of csv=p=0 <file_path>
     * Result model: "1920,1080"
     */
    fun getVideoResolution(file: File): Pair<Int, Int>? {
        var resolution: Pair<Int, Int>? = null
        val command = arrayOf(
            Configs.ffprobePath,
            "-v", "error",
            "-select_streams", "v:0",
            "-show_entries", "stream=width,height",
            "-of", "csv=p=0",
            file.absolutePath
        )

        try {
            val reader = getReader(command)
            val line = reader.readLine()!!
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
            Configs.ffprobePath,
            "-v", "error",
            "-show_entries", "format=duration",
            "-of", "csv=p=0",
            file.absolutePath
        )

        try {
            val reader = getReader(command)
            val line = reader.readLine()!! // 1382.255000
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
    fun getType(file: File): MediaType? {
        var type: MediaType? = null
        val command = arrayOf(
            Configs.ffprobePath,
            "-v", "error",
            "-show_entries", "stream=codec_type",
            "-of", "csv=p=0",
            file.absolutePath
        )

        try {
            val reader = getReader(command)
            val lines = reader.readLines()
            val output =  lines.map { it.trim() }

            type = when {
                "video" in output -> MediaType.VIDEO
                "audio" in output -> MediaType.AUDIO
                else -> null
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
        }

        return type
    }

    fun getSize(file: File) = file.length()

    private fun getReader(command: Array<String>): BufferedReader {
        val process = ProcessBuilder(*command)
            .redirectErrorStream(true)
            .start()

        return BufferedReader(InputStreamReader(process.inputStream))
    }
}