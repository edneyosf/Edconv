package edneyosf.edconv.ffmpeg.extensions

import edneyosf.edconv.core.utils.DateTimeUtils
import edneyosf.edconv.ffmpeg.converter.ConverterPattern
import edneyosf.edconv.ffmpeg.data.ProgressData

fun String.getProgressData(pattern: String, onException: (e: Exception) -> Unit): ProgressData? {
    var progress: ProgressData? = null

    try {
        val regex = Regex(pattern = pattern)
        val match = regex.find(input = this)

        if(match != null) {
            val (_, rawTime, _, speed) = match.destructured
            val time = DateTimeUtils.timeToLong(time = rawTime, pattern = ConverterPattern.TIME)

            progress = ProgressData(
                time = time,
                speed = speed
            )
        }
    }
    catch (e: Exception) {
        e.printStackTrace()
        onException(e)
    }

    return progress
}