package edconv.core

import edconv.aac.AACBuilder
import edconv.av1.AV1Builder
import edconv.eac3.EAC3Builder
import edconv.core.EdconvArgs.FFMPEG_PATH
import edconv.core.EdconvArgs.FFPROBE_PATH
import edconv.core.EdconvConfigs.CORE
import edconv.core.EdconvConfigs.FFMPEG
import edconv.core.EdconvConfigs.FFPROBE
import edconv.core.EdconvConfigs.STATUS_COMPLETE
import edconv.core.EdconvConfigs.STATUS_ERROR
import edconv.common.Resolutions
import edconv.h265.H265Builder
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader

class Edconv(
    private val binDir: String, private val scope: CoroutineScope, private val onStdout: (String) -> Unit,
    private val onStderr: (String) -> Unit) {

    private val core: String
    private val ffmpeg: String
    private val ffprobe: String

    init {
        binDir.let {
            core = it + CORE
            ffmpeg = it + FFMPEG
            ffprobe = it + FFPROBE
        }
    }

    fun toAAC(
        inputFile: String, outputFile: String, channels: String, kbps: String? = null, vbr: String? = null,
        sampleRate: String? = null): Job {

        val cmd = AACBuilder(
            inputFile = inputFile,
            outputFile = outputFile,
            channels = channels,
            kbps = kbps,
            vbr = vbr,
            sampleRate = sampleRate
        )

        return run(command = cmd.build())
    }

    fun toEAC3(inputFile: String, outputFile: String, channels: String, kbps: String,
               sampleRate: String? = null): Job {

        val cmd = EAC3Builder(
            inputFile = inputFile,
            outputFile = outputFile,
            channels = channels,
            kbps = kbps,
            sampleRate = sampleRate
        )

        return run(command = cmd.build())
    }

    fun toH265(
        inputFile: String, outputFile: String, preset: String, crf: Int, resolution: Resolutions?,
        noAudio: Boolean = false, bit: String? = null): Job {

        val cmd = H265Builder(
            inputFile = inputFile,
            outputFile = outputFile,
            preset = preset,
            crf = crf,
            resolution = resolution,
            bit = bit,
            noAudio = noAudio
        )

        return run(command = cmd.build())
    }

    fun toAV1(
        inputFile: String, outputFile: String, preset: String, crf: Int, resolution: Resolutions?,
        noAudio: Boolean = false, bit: String? = null): Job {

        val cmd = AV1Builder(
            inputFile = inputFile,
            outputFile = outputFile,
            preset = preset,
            crf = crf,
            resolution = resolution,
            bit = bit,
            noAudio = noAudio
        )

        return run(command = cmd.build())
    }

    private fun run(command: List<String>): Job {
        val cmd = mutableListOf(core, FFMPEG_PATH, ffmpeg, FFPROBE_PATH, ffprobe)
            .apply { addAll(command) }

        return scope.launch(context = Dispatchers.IO) {
            try {
                val process = ProcessBuilder(cmd).start()
                val outReader = BufferedReader(InputStreamReader(process.inputStream))
                val errReader = BufferedReader(InputStreamReader(process.errorStream))
                var line: String?

                while (true) {
                    line = outReader.readLine() ?: break
                    notify(line, onStdout)
                }

                while (true) {
                    line = errReader.readLine() ?: break
                    notify(line, onStderr)
                }

                notify(STATUS_COMPLETE, onStdout)
            }
            catch (e: Exception) {
                notify(e.message, onStderr)
                notify(STATUS_ERROR, onStdout)
            }
        }
    }

    private suspend fun notify(content: String?, onStd: (String) -> Unit) = content?.let {
        withContext(context = Dispatchers.Main) { onStd(it) }
    }
}