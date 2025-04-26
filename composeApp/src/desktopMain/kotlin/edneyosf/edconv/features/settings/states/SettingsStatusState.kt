package edneyosf.edconv.features.settings.states

sealed interface SettingsStatus {
    data object Initial: SettingsStatus
    data object Loading: SettingsStatus
    data object Complete: SettingsStatus
    data class Error(val id: String): SettingsStatus
}