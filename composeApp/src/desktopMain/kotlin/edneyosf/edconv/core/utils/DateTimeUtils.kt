package edneyosf.edconv.core.utils

import java.time.Duration
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.Temporal

object DateTimeUtils {
    fun timeToLong(time: String, pattern: String): Long {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val timeDraft = LocalTime.parse(time, formatter)
        val duration = Duration.between(LocalTime.MIDNIGHT, timeDraft)

        return duration.toMillis()
    }

    fun instantToText(instant: Instant, pattern: String): String {
        val systemZone = ZoneId.systemDefault()
        val start = instant.atZone(systemZone).toLocalTime()
        val formatter = DateTimeFormatter.ofPattern(pattern)

        return start.format(formatter)
    }

    fun durationText(start: Temporal, end: Temporal): String {
        val duration = Duration.between(start, end)
        val hours = (duration.toHours() % 24).toString().padStart(2, '0')
        val minutes = (duration.toMinutes() % 60).toString().padStart(2, '0')
        val seconds = (duration.seconds % 60).toString().padStart(2, '0')

        return "$hours:$minutes:$seconds"
    }
}