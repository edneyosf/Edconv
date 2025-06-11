package edneyosf.edconv.ffmpeg.converter

import edneyosf.edconv.core.common.Error
import edneyosf.edconv.core.extensions.notifyMain
import edneyosf.edconv.ffmpeg.data.ProgressData
import edneyosf.edconv.ffmpeg.extensions.getProgressData
import edneyosf.edconv.ffmpeg.ffmpeg.FFmpegArgs
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class Converter(
    private val scope: CoroutineScope, private val onStart: () -> Unit, private val onStdout: (String) -> Unit,
    private val onError: (Error) -> Unit, private val onProgress: (ProgressData?) -> Unit,
    private val onStop: () -> Unit
) {
    private var process: Process? = null

    fun run(ffmpeg: String, inputFile: File, cmd: String, outputFile: File) = scope.launch(context = Dispatchers.IO) {
        notifyMain { onStart() }
        onStdout("Command = { $cmd }")

        try {
            if (outputFile.exists() && outputFile.isFile) outputFile.delete()
            outputFile.parentFile?.mkdirs()

            if(!inputFile.exists()) {
                notifyMain { onError(Error.INPUT_FILE_NOT_EXIST) }
                return@launch
            }
            else if(!inputFile.isFile) {
                notifyMain { onError(Error.INPUT_NOT_FILE) }
                return@launch
            }

            process = ProcessBuilder(
                ffmpeg,
                FFmpegArgs.INPUT, inputFile.absolutePath,
                *cmd.normalize(),
                outputFile.absolutePath
            ).start()

            process?.let {
                notifyMain { onProgress(null) }

                BufferedReader(InputStreamReader(it.errorStream)).useLines { lines ->
                    lines.forEach { line ->
                        val progress = line.getProgressData(
                            pattern = ConverterPattern.PROGRESS,
                            onException = { e -> onStdout("Error = { " + e.message + " }") }
                        )

                        if (progress != null) notifyMain { onProgress(progress) }
                        else onStdout(line)
                    }
                }

                val exitCode = it.waitFor()
                if (exitCode != 0) notifyMain { onError(Error.CONVERSION_PROCESS_COMPLETED) }

            } ?: run {
                notifyMain { onError(Error.PROCESS_NULL) }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            destroyProcess()
            notifyMain { onError(Error.CONVERSION_PROCESS) }
        }
        finally {
            process = null
            notifyMain { onStop() }
        }

        return@launch
    }

    fun destroyProcess() {
        process?.destroyForcibly()
        process = null
    }

    private fun String.normalize(): Array<String> {
        val regex = Regex(pattern = ConverterPattern.COMMAND)
        val data = this.split(regex)
        val filtered = data.filter { it.isNotBlank() }

        return filtered.toTypedArray()
    }
}