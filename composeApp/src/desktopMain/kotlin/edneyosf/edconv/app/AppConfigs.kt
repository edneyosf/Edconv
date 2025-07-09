package edneyosf.edconv.app

import edneyosf.edconv.core.common.Languages
import edneyosf.edconv.ffmpeg.common.FFLogLevel

object AppConfigs {
    const val NAME = "Edconv"
    const val MIN_WINDOW_WIDTH = 1100
    const val MIN_WINDOW_HEIGHT = 730
    const val MIN_SUB_WINDOW_WIDTH = 800
    const val MIN_SUB_WINDOW_HEIGHT = 600
    const val LANGUAGE = Languages.EN
    const val FFMPEG_LOG_LEVEL = FFLogLevel.VERBOSE
    const val LOG_MONITOR_DELAY = 2000L
}