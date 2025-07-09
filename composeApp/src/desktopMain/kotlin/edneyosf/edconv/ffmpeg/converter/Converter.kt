package edneyosf.edconv.ffmpeg.converter

import edneyosf.edconv.core.common.Error
import edneyosf.edconv.core.extensions.notifyMain
import edneyosf.edconv.core.process.EdProcess
import edneyosf.edconv.ffmpeg.data.ProgressData
import edneyosf.edconv.ffmpeg.extensions.getProgressData
import edneyosf.edconv.ffmpeg.ffmpeg.FFmpegArgs
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class Converter(
    private val process: EdProcess,
    private val onStdout: (String) -> Unit,
    private val onProgress: (ProgressData?) -> Unit
) {

    suspend fun run(ffmpeg: String, inputFile: File, cmd: String, outputFile: File): Error? {
        var error: Error? = null

        onStdout("Command = { $cmd }")

        try {
            if (outputFile.exists() && outputFile.isFile) outputFile.delete()
            outputFile.parentFile?.mkdirs()

            if(!inputFile.exists()) return Error.INPUT_FILE_NOT_EXIST
            else if(!inputFile.isFile) return Error.INPUT_NOT_FILE

            process.conversion = ProcessBuilder(
                ffmpeg,
                FFmpegArgs.INPUT, inputFile.absolutePath,
                *cmd.normalize(),
                outputFile.absolutePath
            ).start()

            process.conversion?.let {
                notifyMain { onProgress(null) }

                BufferedReader(InputStreamReader(it.errorStream)).useLines { lines ->
                    lines.forEach { line ->
                        val progress = line.getProgressData { e -> onStdout("Error = { " + e.message + " }") }

                        if (progress != null) notifyMain { onProgress(progress) }
                        else onStdout(line)
                    }
                }

                val exitCode = it.waitFor()
                if (exitCode != 0) error = Error.CONVERSION_PROCESS_COMPLETED

            } ?: run {
                error = Error.PROCESS_NULL
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            destroyProcess()
            error = Error.CONVERSION_PROCESS
        }
        finally {
            process.conversion = null
        }

        return error
    }

    fun destroyProcess() {
        process.conversion?.destroyForcibly()
        process.conversion = null
    }

    private fun String.normalize(): Array<String> {
        val regex = Regex(pattern = "\\s+")
        val data = this.split(regex)
        val filtered = data.filter { it.isNotBlank() }

        return filtered.toTypedArray()
    }
}