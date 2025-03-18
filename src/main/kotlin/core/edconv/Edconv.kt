package core.edconv

import core.aac.AACBuilder
import core.av1.AV1Builder
import core.eac3.EAC3Builder
import core.edconv.EdconvArgs.FFMPEG_PATH
import core.edconv.EdconvArgs.FFPROBE_PATH
import core.edconv.EdconvConfigs.CORE
import core.edconv.EdconvConfigs.FFMPEG
import core.edconv.EdconvConfigs.FFPROBE
import core.edconv.EdconvConfigs.STATUS_COMPLETE
import core.edconv.EdconvConfigs.STATUS_ERROR
import core.edconv.common.Resolutions
import core.h265.H265Builder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class Edconv(
    private val scope: CoroutineScope, private val onStdout: (String) -> Unit, private val onStderr: (String) -> Unit) {

    private val core: String
    private val ffmpeg: String
    private val ffprobe: String

    init {
        val root = File(System.getProperty("compose.application.resources.dir")).parentFile.parentFile.parentFile
        val binDir = root.absolutePath + "/bin/"

        core = binDir + CORE
        ffmpeg = binDir + FFMPEG
        ffprobe = binDir + FFPROBE
    }

    fun toAAC(
        inputFile: String, outputFile: String, channels: String, kbps: String? = null, vbr: String? = null,
        sampleRate: String? = null) {

        val cmd = AACBuilder(
            inputFile = inputFile,
            outputFile = outputFile,
            channels = channels,
            kbps = kbps,
            vbr = vbr,
            sampleRate = sampleRate
        )

        run(command = cmd.build())
    }

    fun toEAC3(inputFile: String, outputFile: String, channels: String, kbps: String,
               sampleRate: String? = null) {

        val cmd = EAC3Builder(
            inputFile = inputFile,
            outputFile = outputFile,
            channels = channels,
            kbps = kbps,
            sampleRate = sampleRate
        )

        run(command = cmd.build())
    }

    fun toH265(
        inputFile: String, outputFile: String, preset: String, crf: Int, resolution: Resolutions,
        noAudio: Boolean = false, bit: String? = null) {

        val cmd = H265Builder(
            inputFile = inputFile,
            outputFile = outputFile,
            preset = preset,
            crf = crf,
            resolution = resolution,
            bit = bit,
            noAudio = noAudio
        )

        run(command = cmd.build())
    }

    fun toAV1(
        inputFile: String, outputFile: String, preset: String, crf: Int, resolution: Resolutions,
        noAudio: Boolean = false, bit: String? = null) {

        val cmd = AV1Builder(
            inputFile = inputFile,
            outputFile = outputFile,
            preset = preset,
            crf = crf,
            resolution = resolution,
            bit = bit,
            noAudio = noAudio
        )

        run(command = cmd.build())
    }

    private fun run(command: List<String>) {
        val cmd = mutableListOf(core, FFMPEG_PATH, ffmpeg, FFPROBE_PATH, ffprobe)
            .apply { addAll(command) }

        scope.launch(context = Dispatchers.IO) {
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