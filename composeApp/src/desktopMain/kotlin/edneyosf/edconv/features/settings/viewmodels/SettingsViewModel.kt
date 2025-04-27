package edneyosf.edconv.features.settings.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edneyosf.edconv.core.ConfigManager
import edneyosf.edconv.core.common.Errors
import edneyosf.edconv.core.extensions.notifyMain
import edneyosf.edconv.core.extensions.update
import edneyosf.edconv.features.settings.events.SettingsEvent
import edneyosf.edconv.features.settings.states.SettingsState
import edneyosf.edconv.features.settings.states.SettingsStatus
import kotlinx.coroutines.*
import java.io.File

class SettingsViewModel(): ViewModel(), SettingsEvent {

    private val _state = mutableStateOf(value = SettingsState())
    val state: State<SettingsState> = _state

    init {
        val ffmpegFile = File(ConfigManager.getFFmpegPath())
        val ffprobeFile = File(ConfigManager.getFFprobePath())

        try {
            if(ffmpegFile.exists()) setFFmpegPath(ConfigManager.getFFmpegPath())
            if(ffprobeFile.exists()) setFFprobePath(ConfigManager.getFFprobePath())
        }
        catch (e: Exception) {
            e.printStackTrace()
            onError(id = Errors.FFMPEG_OR_FFPROBE_VERIFICATION)
        }
    }

    override fun onSave() {
        val ffmpegPath = _state.value.ffmpegPath
        val ffprobePath = _state.value.ffprobePath

        setStatus(SettingsStatus.Loading)

        viewModelScope.launch(context = Dispatchers.IO) {
            try {
                ConfigManager.setFFmpegPath(ffmpegPath)
                ConfigManager.setFFprobePath(ffprobePath)
                notifyMain { setStatus(SettingsStatus.Complete) }
            }
            catch (e: Exception) {
                e.printStackTrace()
                onError(id = Errors.CONFIGURATION_SAVE)
            }
        }
    }

    override fun setStatus(status: SettingsStatus) = _state.update { copy(status = status) }

    override fun setFFmpegPath(path: String) = _state.update { copy(ffmpegPath = path) }

    override fun setFFprobePath(path: String) = _state.update { copy(ffprobePath = path) }

    private fun onError(id: String) = setStatus(SettingsStatus.Error(id = id))
}