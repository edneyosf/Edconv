package core.edconv.utils

import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private const val PROGRESS = "progress="

fun String.isProgress() = this.contains(PROGRESS)

fun String.retrieveProgressJson() = this.substringAfter(PROGRESS)

infix fun String.calculateProgress(duration: String): Float {
    val pattern = "HH:mm:ss.SS"
    val formatter = DateTimeFormatter.ofPattern(pattern)
    val totalTime = LocalTime.parse(this, formatter)
    val currentTime = LocalTime.parse(duration, formatter)
    val totalDuration = Duration.between(LocalTime.MIN, totalTime)
    val currentDuration = Duration.between(LocalTime.MIN, currentTime)
    val totalSeconds = totalDuration.seconds.toFloat()
    val currentSeconds = currentDuration.seconds.toFloat()

    return if (totalSeconds > 0) (currentSeconds / totalSeconds) * 100 else 0.0f
}