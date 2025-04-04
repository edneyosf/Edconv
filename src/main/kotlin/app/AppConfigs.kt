package app

import core.Languages
import edconv.ffmpeg.FFmpegLogLevel

object AppConfigs {
    var title = ""
    var outputDefault = ""
    const val NAME = "Edconv"
    const val MIN_WINDOW_WIDTH = 1250
    const val MIN_WINDOW_HEIGHT = 730
    const val LANGUAGE = Languages.EN
    const val FFMPEG_LOG_LEVEL = FFmpegLogLevel.VERBOSE
}