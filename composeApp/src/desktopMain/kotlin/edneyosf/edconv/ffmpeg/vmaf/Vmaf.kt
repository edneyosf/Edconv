package edneyosf.edconv.ffmpeg.vmaf

import edneyosf.edconv.core.common.Error
import edneyosf.edconv.core.utils.DateTimeUtils
import edneyosf.edconv.ffmpeg.data.ProgressData
import edneyosf.edconv.ffmpeg.ffmpeg.VmafFFmpeg
import kotlinx.coroutines.*
import kotlinx.coroutines.swing.Swing
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class Vmaf(
    private val scope: CoroutineScope, private val onStart: () -> Unit, private val onStdout: (String) -> Unit,
    private val onError: (Error) -> Unit, private val onProgress: (ProgressData?) -> Unit,
    private val onStop: () -> Unit
) {
    private var process: Process? = null

    fun run(ffmpeg: String, data: VmafFFmpeg) = scope.launch(context = Dispatchers.IO) {
        val cmd = data.command()

        notify { onStart() }
        onStdout("Command = { ${cmd.joinToString(separator = " ")} }")

        try {
            val referenceFile = File(data.reference)
            val distortedFile = File(data.distorted)
            val modelFile = File(data.model)

            if(!referenceFile.exists() || !distortedFile.exists() || !modelFile.exists()) {
                notify { onError(Error.INPUT_FILE_NOT_EXIST) }
                return@launch
            }
            else if(!referenceFile.isFile || !distortedFile.isFile || !modelFile.isFile) {
                notify { onError(Error.INPUT_NOT_FILE) }
                return@launch
            }

            process = ProcessBuilder(
                ffmpeg,
                *cmd
            ).start()

            process?.let {
                notify { onProgress(null) }

                BufferedReader(InputStreamReader(it.errorStream)).useLines { lines ->
                    lines.forEach { line ->
                        val progress = line.getProgressData()

                        if (progress != null) notify { onProgress(progress) }
                        else onStdout(line)
                    }
                }

                val exitCode = it.waitFor()
                if (exitCode != 0) notify { onError(Error.CONVERSION_PROCESS_COMPLETED) }

            } ?: run {
                notify { onError(Error.PROCESS_NULL) }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            destroyProcess()
            notify { onError(Error.CONVERSION_PROCESS) }
        }
        finally {
            process = null
            notify { onStop() }
        }

        return@launch
    }

    private fun String.getProgressData(): ProgressData? {
        var progress: ProgressData? = null

        try {
            val regex = Regex(pattern = VmafPattern.PROGRESS)
            val match = regex.find(input = this)

            if(match != null) {
                val (_, rawTime, _, speed) = match.destructured
                val time = DateTimeUtils.timeToLong(time = rawTime, pattern = VmafPattern.TIME)

                progress = ProgressData(
                    time = time,
                    speed = speed
                )
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            onStdout("Error = { " + e.message + " }")
        }

        return progress
    }

    fun destroyProcess() {
        process?.destroyForcibly()
        process = null
    }

    private suspend inline fun <T> notify(crossinline block: () -> T): Unit =
        withContext(context = Dispatchers.Swing) { block() }
}