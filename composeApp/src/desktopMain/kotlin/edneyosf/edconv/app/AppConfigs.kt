package edneyosf.edconv.app

import edneyosf.edconv.core.Languages
import edneyosf.edconv.edconv.common.FFLogLevel

object AppConfigs {
    var outputDefault = ""
    const val NAME = "Edconv"
    const val MIN_WINDOW_WIDTH = 1100
    const val MIN_WINDOW_HEIGHT = 730
    const val LANGUAGE = Languages.EN
    const val FFMPEG_LOG_LEVEL = FFLogLevel.VERBOSE
}