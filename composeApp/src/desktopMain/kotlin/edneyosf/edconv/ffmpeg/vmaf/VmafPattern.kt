package edneyosf.edconv.ffmpeg.vmaf

object VmafPattern {
    const val TIME = "HH:mm:ss.SS"
    const val PROGRESS = "size=\\s*(\\S+)\\s+time=(\\d+:\\d+:\\d+\\.\\d+)\\s+bitrate=\\s*(\\S+)\\s+speed=\\s*([\\d.]+x)"
    const val SCORE = "VMAF score:\\s*(\\d+(?:\\.\\d+)?)"
}