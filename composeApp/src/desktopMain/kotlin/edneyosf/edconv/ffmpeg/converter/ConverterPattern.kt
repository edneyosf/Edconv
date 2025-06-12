package edneyosf.edconv.ffmpeg.converter

object ConverterPattern {
    const val TIME = "HH:mm:ss.SS"
    const val PROGRESS = "size=\\s*(\\S+)\\s+time=(\\d+:\\d+:\\d+\\.\\d+)\\s+bitrate=\\s*(\\S+)\\s+speed=\\s*([\\d.]+x)"
    const val COMMAND = "\\s+"
}