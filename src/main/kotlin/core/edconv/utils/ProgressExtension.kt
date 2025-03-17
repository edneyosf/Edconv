package core.edconv.utils

import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private const val PROGRESS = "progress="

fun String.isProgress() = this.contains(PROGRESS)

fun String.getProgressJson() = this.substringAfter(PROGRESS)

infix fun String.calculateProgress(duration: String): Float {
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SS")
    val totalTime = LocalTime.parse(this, formatter)
    val currentTime = LocalTime.parse(duration, formatter)
    val totalSeconds = Duration.between(LocalTime.MIN, totalTime).seconds.toFloat()
    val currentSeconds = Duration.between(LocalTime.MIN, currentTime).seconds.toFloat()

    return if (totalSeconds > 0) (currentSeconds / totalSeconds) * 100 else 0.0f
}
