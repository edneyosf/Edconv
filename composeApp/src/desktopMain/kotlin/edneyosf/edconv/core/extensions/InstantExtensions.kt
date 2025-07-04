package edneyosf.edconv.core.extensions

import edneyosf.edconv.core.common.DateTimePattern
import edneyosf.edconv.core.utils.DateTimeUtils
import java.time.Instant

fun Instant.formatTime() = DateTimeUtils.instantToText(instant = this, pattern = DateTimePattern.TIME_HMS)

fun Instant.durationUntil(end: Instant) = DateTimeUtils.durationText(this, end)