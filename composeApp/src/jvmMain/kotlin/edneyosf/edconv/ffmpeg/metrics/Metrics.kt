package edneyosf.edconv.ffmpeg.metrics

import edneyosf.edconv.core.common.Error
import edneyosf.edconv.core.extensions.notifyMain
import edneyosf.edconv.core.process.EdProcess
import edneyosf.edconv.ffmpeg.data.ProgressData
import edneyosf.edconv.ffmpeg.extensions.getProgressData
import edneyosf.edconv.ffmpeg.ffmpeg.MetricsFFmpeg
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class Metrics(
    private val process: EdProcess, private val scope: CoroutineScope,
    private val onStart: () -> Unit, private val onStdout: (String) -> Unit,
    private val onError: (Error) -> Unit, private val onProgress: (ProgressData?) -> Unit,
    private val onStop: (MetricsScore) -> Unit
) {
    private lateinit var score: MetricsScore

    fun run(ffmpeg: String, data: MetricsFFmpeg) = scope.launch(context = Dispatchers.IO) {
        val cmd = data.command()

        notifyMain { onStart() }
        onStdout("Command = { ${cmd.joinToString(separator = " ")} }")
        score = MetricsScore()

        try {
            val referenceFile = File(data.reference)
            val distortedFile = File(data.distorted)

            if(!referenceFile.exists() || !distortedFile.exists()) {
                notifyMain { onError(Error.INPUT_FILE_NOT_EXIST) }
                return@launch
            }
            else if(!referenceFile.isFile || !distortedFile.isFile) {
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
                if (exitCode != 0) notifyMain { onError(Error.METRICS_PROCESS_COMPLETED) }

            } ?: run {
                notifyMain { onError(Error.PROCESS_NULL) }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            destroyProcess()
            notifyMain { onError(Error.METRICS_PROCESS) }
        }
        finally {
            process.analysis = null
            notifyMain { onStop(score) }
        }

        return@launch
    }

    private fun String.getScore() {
        try {
            val vmafScore = findScore(pattern = "VMAF score:\\s*(\\d+(?:\\.\\d+)?)")
            val psnrScore = findScore(pattern = "average:(\\d+(?:\\.\\d+)?)")
            val ssimScore = findScore(pattern = "All:\\s*(\\d+(?:\\.\\d+)?)")

            vmafScore?.let { score = score.copy(vmaf = it) }
            psnrScore?.let { score = score.copy(psnr = it) }
            ssimScore?.let { score = score.copy(ssim = it) }
        }
        catch (e: Exception) {
            e.printStackTrace()
            onStdout("Error = { " + e.message + " }")
        }
    }

    private fun String.findScore(pattern: String): Double? {
        val regex = Regex(pattern = pattern)
        val match = regex.find(input = this)
        var score: Double? = null

        if(match != null) {
            val (value) = match.destructured
            score = value.toDouble()
        }

        return score
    }

    fun destroyProcess() {
        process.analysis?.destroyForcibly()
        process.analysis = null
    }
}