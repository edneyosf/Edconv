package edneyosf.edconv.feature.settings

import edneyosf.edconv.feature.settings.states.SettingsStatusState

interface SettingsEvent {
    fun setStatus(status: SettingsStatusState) = Unit
    fun setFFmpegPath(path: String) = Unit
    fun setFFprobePath(path: String) = Unit
    fun onSave() = Unit
}