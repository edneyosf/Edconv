package edneyosf.edconv.ffmpeg.data.extensions

fun Float.toDuration() = (this * 1000).toLong()