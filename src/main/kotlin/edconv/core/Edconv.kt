package edconv.core

import edconv.core.EdconvConfigs.FFMPEG
import edconv.core.EdconvConfigs.FFPROBE
import edconv.core.EdconvConfigs.STATUS_COMPLETE
import edconv.core.EdconvConfigs.STATUS_ERROR
import edconv.core.data.ProgressData
import edconv.core.utils.CmdUtils
import edconv.ffmpeg.FFmpeg
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Edconv(
    private val scope: CoroutineScope, private val onStdout: (String) -> Unit, private val onStderr: (String) -> Unit,
    private val onStart: () -> Unit, private val onStop: () -> Unit, private val onProgress: (ProgressData) -> Unit
) {
    private var process: Process? = null

    fun run(ffmpeg: FFmpeg) = scope.launch(context = Dispatchers.IO) {
        val cmd = ffmpeg.build()

        notify(cmd.joinToString(" "), onStdout)

        try {
            process = ProcessBuilder(cmd)
                .redirectErrorStream(true)
                .start()

            withContext(context = Dispatchers.Main){ onStart() }

            process?.let {
                val reader = BufferedReader(InputStreamReader(it.inputStream))
                var line: String?

                while (true) {
                    line = reader.readLine() ?: break
                    val regex = Regex("size=\\s*(\\d+KiB)\\s+time=(\\d+:\\d+:\\d+\\.\\d+)\\s+bitrate=\\s*([\\d.]+kbits/s)\\s+speed=\\s*([\\d.]+x)")
                    val match = regex.find(line)
                    if (match != null) {
                        val (size, timeRaw, bitrate, speed) = match.destructured

                        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SS")
                        val timeDraft = LocalTime.parse(timeRaw, formatter)
                        val time = Duration.between(LocalTime.MIDNIGHT, timeDraft).toMillis()
                        println("sieze: "+size)
                        println("timeraw: "+timeRaw)
                        println("bitrate: "+bitrate)
                        println("speed: "+speed)
                        val lastProgress = ProgressData(size, time, bitrate, speed)
                        withContext(context = Dispatchers.Main) {
                            onProgress(lastProgress)
                        }
                    }

                    notify("\r$line", onStdout)
                }

            } ?: run {
                notify("Process is null", onStderr)
                notify(STATUS_ERROR, onStdout)
            }

            process = null
            withContext(context = Dispatchers.Main){ onStop() }
        }
        catch (e: Exception) {
            destroyProcess()
            notify(e.message, onStderr)
            withContext(context = Dispatchers.Main){ onStop() }
            notify(STATUS_ERROR, onStdout)
        }
    }

    fun destroyProcess() {
        CmdUtils.killProcess(FFPROBE)
        CmdUtils.killProcess(FFMPEG)
        process?.destroyForcibly()
        process = null
    }

    private suspend fun notify(content: String?, onStd: (String) -> Unit) = content?.let {
        withContext(context = Dispatchers.Main) { onStd(it) }
    }
}