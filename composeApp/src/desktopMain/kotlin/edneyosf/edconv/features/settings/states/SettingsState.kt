package edneyosf.edconv.features.settings.states

data class SettingsState(
    val status: SettingsStatus,
    val ffmpegPath: String,
    val ffprobePath: String,
) {
    companion object {
        fun default() = SettingsState(
            status = SettingsStatus.Initial,
            ffmpegPath = "",
            ffprobePath = ""
        )
    }
}

sealed interface SettingsStatus {
    data object Initial: SettingsStatus
    data object Loading: SettingsStatus
    data object Complete: SettingsStatus
    data class Error(val message: String? = null): SettingsStatus
}