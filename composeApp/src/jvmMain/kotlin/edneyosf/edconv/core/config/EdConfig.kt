package edneyosf.edconv.core.config

import androidx.compose.runtime.mutableStateOf

private const val FILE_NAME = "config.json"

class EdConfig : ConfigManager(fileName = FILE_NAME) {

    var ffmpegPath
        get() = config.ffmpegPath
        set(value) = save { ffmpegPath = value }

    var ffprobePath
        get() = config.ffprobePath
        set(value) = save { ffprobePath = value }

    private var darkThemeState = mutableStateOf(value = true)
    var darkTheme: Boolean
        get() {
            darkThemeState.value = config.darkTheme
            return darkThemeState.value
        }
        set(value) {
            darkThemeState.value = value
            save { config.darkTheme = value }
        }
}