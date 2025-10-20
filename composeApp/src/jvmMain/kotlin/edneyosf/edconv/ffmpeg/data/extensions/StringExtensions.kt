package edneyosf.edconv.ffmpeg.data.extensions

fun String.toFrameRate() = split('/')
    .takeIf { it.size == 2 && it[1] != "0" }
    ?.let { it[0].toFloat() / it[1].toFloat() }
    ?: 0.0f