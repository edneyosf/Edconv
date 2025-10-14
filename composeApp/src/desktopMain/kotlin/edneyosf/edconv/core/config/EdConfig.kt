package edneyosf.edconv.core.config

private const val FILE_NAME = "config.json"

class EdConfig : ConfigManager(fileName = FILE_NAME) {

    var ffmpegPath
        get() = config.ffmpegPath
        set(value) = save { ffmpegPath = value }

    var ffprobePath
        get() = config.ffprobePath
        set(value) = save { ffprobePath = value }
}