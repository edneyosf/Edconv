package edconv.core

import edconv.core.EdconvConfigs.PROGRESS_PATTERN
import edconv.core.EdconvConfigs.TIME_PATTERN
import edconv.core.data.ProgressData
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
                    val progress = line.getProgressData()

                    if (progress != null)
                        notify { onProgress(progress) }
                    else
                        notify { onStdout(line) }
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

    private fun String.getProgressData(): ProgressData? {
        var progress: ProgressData? = null

        try {
            val regex = Regex(pattern = PROGRESS_PATTERN)
            val match = regex.find(input = this)

            if(match != null) {
                val (size, rawTime, bitrate, speed) = match.destructured
                val time = rawTime.toLongTime()

                progress = ProgressData(
                    size = size,
                    time = time,
                    bitrate = bitrate,
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

    private fun String.toLongTime(): Long {
        val formatter = DateTimeFormatter.ofPattern(TIME_PATTERN)
        val timeDraft = LocalTime.parse(this, formatter)
        val duration = Duration.between(LocalTime.MIDNIGHT, timeDraft)

        return duration.toMillis()
    }

    private suspend inline fun <T> notify(crossinline block: () -> T): Unit =
        withContext(context = Dispatchers.Main) { block() }
}