package edneyosf.edconv.features.settings.states

import edneyosf.edconv.core.common.Error

sealed interface SettingsStatus {
    data object Initial: SettingsStatus
    data object Loading: SettingsStatus
    data object Complete: SettingsStatus
    data class Failure(val error: Error): SettingsStatus
}