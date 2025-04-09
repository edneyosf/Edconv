package edneyosf.edconv.features.settings.events

import edneyosf.edconv.features.settings.states.SettingsStatus

sealed interface SettingsEvent {
    data class SetStatus(val status: SettingsStatus): SettingsEvent
    data class SetFFmpegPath(val path: String): SettingsEvent
    data class SetFFprobePath(val path: String): SettingsEvent
    data object OnSave: SettingsEvent
}