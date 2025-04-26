package edneyosf.edconv.features.settings.events

import edneyosf.edconv.features.settings.states.SettingsStatus

interface SettingsEvent {
    fun setStatus(status: SettingsStatus) = Unit
    fun setFFmpegPath(path: String) = Unit
    fun setFFprobePath(path: String) = Unit
    fun onSave() = Unit
}