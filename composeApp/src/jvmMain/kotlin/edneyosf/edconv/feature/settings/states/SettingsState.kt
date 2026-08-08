package edneyosf.edconv.feature.settings.states

data class SettingsState(
    val status: SettingsStatusState = SettingsStatusState.Initial,
    val ffmpegPath: String = "",
    val ffprobePath: String = "",
)