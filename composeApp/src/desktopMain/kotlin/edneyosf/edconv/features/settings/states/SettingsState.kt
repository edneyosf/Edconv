package edneyosf.edconv.features.settings.states

data class SettingsState(
    val status: SettingsStatus = SettingsStatus.Initial,
    val ffmpegPath: String = "",
    val ffprobePath: String = "",
)