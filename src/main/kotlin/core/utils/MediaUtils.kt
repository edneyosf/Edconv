package core.utils

import core.Configs
import edconv.common.MediaType
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object MediaUtils {
    fun getVideoResolution(file: File): Pair<Int, Int> {
        val command = arrayOf(
            Configs.ffprobePath,
            "-v", "error",
            "-select_streams", "v:0",
            "-show_entries", "stream=width,height",
            "-of", "csv=s=x:p=0",
            file.absolutePath
        )
        val process = ProcessBuilder(*command)
            .redirectErrorStream(true)
            .start()

        val inputStream = InputStreamReader(process.inputStream)
        val reader = BufferedReader(inputStream)
        val data = reader.readLine()?.split("x")
        val resolution = data?.map { it.toInt() } ?: listOf(0, 0)
        val width = resolution.getOrElse(index = 0, defaultValue = { 0 })
        val height = resolution.getOrElse(index = 1, defaultValue = { 0 })

        return Pair(width, height)
    }

    fun getDuration(file: File): Long {
        val command = arrayOf(
            Configs.ffprobePath,
            "-v", "error",
            "-show_entries", "format=duration",
            "-of", "default=noprint_wrappers=1:nokey=1",
            file.absolutePath
        )
        val process = ProcessBuilder(*command)
            .redirectErrorStream(true)
            .start()

        val inputStream = InputStreamReader(process.inputStream)
        val reader = BufferedReader(inputStream)
        val durationInSeconds = reader.readLine()?.toDoubleOrNull() ?: 0.0

        return (durationInSeconds * 1000).toLong()
    }

    fun getType(file: File): MediaType {
        val command = listOf(
            Configs.ffprobePath,
            "-v", "error",
            "-show_entries", "stream=codec_type",
            "-of", "csv=p=0",
            file.absolutePath
        )
        val process = ProcessBuilder(command)
            .redirectErrorStream(true)
            .start()

        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val output =  reader.readLines().map { it.trim() }

        return when {
            "video" in output -> MediaType.VIDEO
            "audio" in output -> MediaType.AUDIO
            else -> MediaType.UNKNOWN
        }
    }

    fun getSize(file: File) = file.length()
}