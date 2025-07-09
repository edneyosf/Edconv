package edneyosf.edconv.ffmpeg.vmaf

import edneyosf.edconv.core.common.Error
import edneyosf.edconv.core.extensions.notifyMain
import edneyosf.edconv.core.process.EdProcess
import edneyosf.edconv.ffmpeg.data.ProgressData
import edneyosf.edconv.ffmpeg.extensions.getProgressData
import edneyosf.edconv.ffmpeg.ffmpeg.VmafFFmpeg
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class Vmaf(
    private val process: EdProcess, private val scope: CoroutineScope,
    private val onStart: () -> Unit, private val onStdout: (String) -> Unit,
    private val onError: (Error) -> Unit, private val onProgress: (ProgressData?) -> Unit,
    private val onStop: (Double?) -> Unit
) {
    private var score: Double? = null

    fun run(ffmpeg: String, data: VmafFFmpeg) = scope.launch(context = Dispatchers.IO) {
        val cmd = data.command()

        notifyMain { onStart() }
        onStdout("Command = { ${cmd.joinToString(separator = " ")} }")
        score = null

        try {
            val referenceFile = File(data.reference)
            val distortedFile = File(data.distorted)
            val modelFile = File(data.model)

            if(!referenceFile.exists() || !distortedFile.exists() || !modelFile.exists()) {
                notifyMain { onError(Error.INPUT_FILE_NOT_EXIST) }
                return@launch
            }
            else if(!referenceFile.isFile || !distortedFile.isFile || !modelFile.isFile) {
                notifyMain { onError(Error.INPUT_NOT_FILE) }
                return@launch
            }

            process.analysis = ProcessBuilder(
                ffmpeg,
                *cmd
            ).start()

            process.analysis?.let {
                notifyMain { onProgress(null) }

                BufferedReader(InputStreamReader(it.errorStream)).useLines { lines ->
                    lines.forEach { line ->
                        val progress = line.getProgressData{ e -> onStdout("Error = { " + e.message + " }") }

                        line.getScore()

                        if (progress != null) notifyMain { onProgress(progress) }
                        else onStdout(line)
                    }
                }

                val exitCode = it.waitFor()
                if (exitCode != 0) notifyMain { onError(Error.VMAF_PROCESS_COMPLETED) }

            } ?: run {
                notifyMain { onError(Error.PROCESS_NULL) }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            destroyProcess()
            notifyMain { onError(Error.VMAF_PROCESS) }
        }
        finally {
            process.analysis = null
            notifyMain { onStop(score) }
        }

        return@launch
    }

    private fun String.getScore() {
        try {
            val regex = Regex(pattern = "VMAF score:\\s*(\\d+(?:\\.\\d+)?)")
            val match = regex.find(input = this)

            if(match != null) {
                val (value) = match.destructured

                score = value.toDouble()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            onStdout("Error = { " + e.message + " }")
        }
    }

    fun destroyProcess() {
        process.analysis?.destroyForcibly()
        process.analysis = null
    }
}