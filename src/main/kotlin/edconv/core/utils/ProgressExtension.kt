package edconv.core.utils

import edconv.core.EdconvConfigs.PROGRESS

fun String.isProgress() = this.contains(PROGRESS)

fun String.retrieveProgressJson() = this.substringAfter(PROGRESS)

infix fun Long.calculateProgress(duration: Long): Float {
    return if (this > 0) (duration.toFloat() / this.toFloat()) * 100 else 0.0f
}