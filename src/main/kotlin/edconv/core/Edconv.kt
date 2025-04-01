package edconv.core

import edconv.core.EdconvConfigs.FFMPEG
import edconv.core.EdconvConfigs.FFPROBE
import edconv.core.EdconvConfigs.TIME_PATTERN
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
    private val scope: CoroutineScope, private val onStart: () -> Unit, private val onStdout: (String) -> Unit,
    private val onError: (Throwable) -> Unit, private val onProgress: (ProgressData) -> Unit,
    private val onStop: () -> Unit
) {
    private var process: Process? = null

    fun run(ffmpeg: FFmpeg) = scope.launch(context = Dispatchers.IO) {
        notify { onStart() }
        val cmd = ffmpeg.build()

        notify { onStdout("Command = { " + cmd.joinToString(" ") + " }\n") }

        try {
            process = ProcessBuilder(cmd)
                .start()

            process?.let {
                val reader = BufferedReader(InputStreamReader(it.errorStream))
                var line: String?

                while (true) {
                    line = reader.readLine() ?: break
                    val regex = Regex("size=\\s*(\\d+KiB)\\s+time=(\\d+:\\d+:\\d+\\.\\d+)\\s+bitrate=\\s*([\\d.]+kbits/s)\\s+speed=\\s*([\\d.]+x)")
                    val match = regex.find(line)
                    if (match != null) {
                        val (size, timeRaw, bitrate, speed) = match.destructured

                        val formatter = DateTimeFormatter.ofPattern(TIME_PATTERN)
                        val timeDraft = LocalTime.parse(timeRaw, formatter)
                        val time = Duration.between(LocalTime.MIDNIGHT, timeDraft).toMillis()
                        val lastProgress = ProgressData(size, time, bitrate, speed)

                        notify { onProgress(lastProgress) }
                    }
                    else {
                        notify { onStdout(line) }
                    }
                }

            } ?: run { notify { onError(Throwable("Process is null")) } }
        }
        catch (e: Exception) {
            destroyProcess()
            notify { onError(e) }
        }
        finally {
            process = null
            notify { onStop() }
        }

        return@launch
    }

    fun destroyProcess() {
        CmdUtils.killProcess(FFPROBE)
        CmdUtils.killProcess(FFMPEG)
        process?.destroyForcibly()
        process = null
    }

    private suspend inline fun <T> notify(crossinline block: () -> T): Unit =
        withContext(context = Dispatchers.Main) { block() }
}