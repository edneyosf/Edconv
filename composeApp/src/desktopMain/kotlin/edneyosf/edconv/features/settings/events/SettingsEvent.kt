package edneyosf.edconv.features.settings.events

import edneyosf.edconv.features.settings.states.SettingsStatusState

interface SettingsEvent {
    fun setStatus(status: SettingsStatusState) = Unit
    fun setFFmpegPath(path: String) = Unit
    fun setFFprobePath(path: String) = Unit
    fun onSave() = Unit
}