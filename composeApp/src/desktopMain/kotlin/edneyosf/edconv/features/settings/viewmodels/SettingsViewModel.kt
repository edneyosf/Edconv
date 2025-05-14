package edneyosf.edconv.features.settings.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edneyosf.edconv.core.ConfigManager
import edneyosf.edconv.core.common.Error
import edneyosf.edconv.core.extensions.notifyMain
import edneyosf.edconv.core.extensions.update
import edneyosf.edconv.features.settings.events.SettingsEvent
import edneyosf.edconv.features.settings.states.SettingsState
import edneyosf.edconv.features.settings.states.SettingsStatusState
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
            onError(Error.FFMPEG_OR_FFPROBE_VERIFICATION)
        }
    }

    override fun onSave() {
        val ffmpegPath = _state.value.ffmpegPath
        val ffprobePath = _state.value.ffprobePath

        setStatus(SettingsStatusState.Loading)

        viewModelScope.launch(context = Dispatchers.IO) {
            try {
                ConfigManager.setFFmpegPath(ffmpegPath)
                ConfigManager.setFFprobePath(ffprobePath)
                notifyMain { setStatus(SettingsStatusState.Complete) }
            }
            catch (e: Exception) {
                e.printStackTrace()
                onError(Error.CONFIGURATION_SAVE)
            }
        }
    }

    override fun setStatus(status: SettingsStatusState) = _state.update { copy(status = status) }

    override fun setFFmpegPath(path: String) = _state.update { copy(ffmpegPath = path) }

    override fun setFFprobePath(path: String) = _state.update { copy(ffprobePath = path) }

    private fun onError(error: Error) = setStatus(SettingsStatusState.Failure(error))
}