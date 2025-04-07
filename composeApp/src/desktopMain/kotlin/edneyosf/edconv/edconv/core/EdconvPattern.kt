package edneyosf.edconv.edconv.core

object EdconvPattern {
    const val TIME = "HH:mm:ss.SS"
    const val PROGRESS = "size=\\s*(\\d+KiB)\\s+time=(\\d+:\\d+:\\d+\\.\\d+)\\s+bitrate=\\s*([\\d.]+kbits/s)\\s+speed=\\s*([\\d.]+x)"
    const val COMMAND = "\\s+"
}