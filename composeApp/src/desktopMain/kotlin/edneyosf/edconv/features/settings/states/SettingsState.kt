package edneyosf.edconv.features.settings.states

data class SettingsState(
    val status: SettingsStatusState = SettingsStatusState.Initial,
    val ffmpegPath: String = "",
    val ffprobePath: String = "",
)