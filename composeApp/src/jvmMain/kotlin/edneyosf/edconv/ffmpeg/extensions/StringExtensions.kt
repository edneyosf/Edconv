package edneyosf.edconv.ffmpeg.extensions

import edneyosf.edconv.core.utils.DateTimeUtils
import edneyosf.edconv.ffmpeg.data.ProgressData

fun String.getProgressData(onException: (e: Exception) -> Unit): ProgressData? {
    var progress: ProgressData? = null

    try {
        val timeRegex = Regex(pattern = "time=(\\d+:\\d+:\\d+\\.\\d+)")
        val speedRegex = Regex(pattern = "speed=\\s*([\\d.]+x)")
        val timeMatch = timeRegex.find(input = this)
        val speedMatch = speedRegex.find(input = this)

        if(timeMatch != null && speedMatch != null) {
            val (rawTime) = timeMatch.destructured
            val (rawSpeed) = speedMatch.destructured
            val time = DateTimeUtils.timeToLong(time = rawTime, pattern = "HH:mm:ss.SS")

            progress = ProgressData(
                time = time,
                speed = rawSpeed
            )
        }
    }
    catch (e: Exception) {
        e.printStackTrace()
        onException(e)
    }

    return progress
}