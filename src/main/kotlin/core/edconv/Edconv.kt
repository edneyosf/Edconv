package core.edconv

import core.aac.AACBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class Edconv(private val scope: CoroutineScope) {

    private val core: String
    private val ffmpeg: String
    private val ffprobe: String

    init {
        val root = File(System.getProperty("compose.application.resources.dir")).parentFile.parentFile.parentFile
        val binDir = root.absolutePath + "/bin/"

        core = binDir + "edconv"
        ffmpeg = binDir + "ffmpeg"
        ffprobe = binDir + "ffprobe"
    }

    fun toAV1(onStdout: (String) -> Unit, onStderr: (String) -> Unit) {
        //TODO
    }

    fun toH265(onStdout: (String) -> Unit, onStderr: (String) -> Unit) {
        //TODO
    }

    fun toEAC3(onStdout: (String) -> Unit, onStderr: (String) -> Unit) {
        //TODO
    }

    fun toAAC(
        inputFile: String, outputFile: String, channels: String, kbps: String? = null, vbr: String? = null,
        sampleRate: String? = null, onStdout: (String) -> Unit, onStderr: (String) -> Unit) {

        val cmd = AACBuilder(
            inputFile = inputFile,
            outputFile = outputFile,
            channels = channels,
            kbps = kbps,
            vbr = vbr,
            sampleRate = sampleRate
        ).build()

        run(command = cmd, onStdout = onStdout, onStderr = onStderr)
    }

    private fun run(command: List<String>, onStdout: (String) -> Unit, onStderr: (String) -> Unit) {
        val cmd = mutableListOf(core, EdconvArgs.FFMPEG, ffmpeg, EdconvArgs.FFPROBE, ffprobe)

        cmd.addAll(command)

        println(cmd)

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

    companion object {
        const val STATUS_COMPLETE = "status=Complete"
        const val STATUS_ERROR = "status=Error"
    }
}