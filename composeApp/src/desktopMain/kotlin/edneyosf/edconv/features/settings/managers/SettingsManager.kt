package edneyosf.edconv.features.settings.managers

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import edneyosf.edconv.core.ConfigManager
import edneyosf.edconv.core.common.Manager
import edneyosf.edconv.core.extensions.update
import edneyosf.edconv.features.settings.events.SettingsEvent
import edneyosf.edconv.features.settings.states.SettingsState
import edneyosf.edconv.features.settings.states.SettingsStatus
import kotlinx.coroutines.*
import java.io.File

class SettingsManager(override val scope: CoroutineScope): Manager(scope) {

    private val _state = mutableStateOf(SettingsState.default())
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
            setStatus(SettingsStatus.Error(e.message))
        }
    }

    fun onEvent(event: SettingsEvent) = event.run {
        when(this) {
            is SettingsEvent.SetStatus -> setStatus(status)
            is SettingsEvent.SetFFmpegPath -> setFFmpegPath(path)
            is SettingsEvent.SetFFprobePath -> setFFprobePath(path)
            is SettingsEvent.OnSave -> saveFFmpegProbePath()
        }
    }

    private fun saveFFmpegProbePath() {
        val ffmpegPath = _state.value.ffmpegPath
        val ffprobePath = _state.value.ffprobePath

        setStatus(SettingsStatus.Loading)

        scope.launch(context = Dispatchers.IO) {
            try {
                ConfigManager.setFFmpegPath(ffmpegPath)
                ConfigManager.setFFprobePath(ffprobePath)
                withContext(context = Dispatchers.Main) { setStatus(SettingsStatus.Complete) }
            }
            catch (e: Exception) {
                e.printStackTrace()
                withContext(context = Dispatchers.Main) { setStatus(SettingsStatus.Error(e.message)) }
            }
        }
    }

    private fun setStatus(status: SettingsStatus) = _state.update { copy(status = status) }
    private fun setFFmpegPath(path: String) = _state.update { copy(ffmpegPath = path) }
    private fun setFFprobePath(path: String) = _state.update { copy(ffprobePath = path) }
}