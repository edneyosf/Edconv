package edneyosf.edconv.features.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edneyosf.edconv.app.AppConfigs
import edneyosf.edconv.core.ConfigManager
import edneyosf.edconv.core.common.Errors
import edneyosf.edconv.core.extensions.notifyMain
import edneyosf.edconv.core.extensions.update
import edneyosf.edconv.core.utils.FileUtils
import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.features.home.mappers.toInputMedia
import edneyosf.edconv.ffmpeg.common.MediaType
import edneyosf.edconv.ffmpeg.data.InputMediaData
import edneyosf.edconv.ffmpeg.ffprobe.FFprobe
import edneyosf.edconv.features.home.events.HomeEvent
import edneyosf.edconv.features.home.states.HomeDialogState
import edneyosf.edconv.features.home.states.HomeNavigationState
import edneyosf.edconv.features.home.states.HomeState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import kotlin.String

class HomeViewModel(): ViewModel(), HomeEvent {

    private val _state = MutableStateFlow(value = HomeState())
    val state: StateFlow<HomeState> = _state

    private val inputFlow: Flow<InputMedia?> = state
        .map { it.input }
        .distinctUntilChanged()
        .drop(count = 1)

    init {
        loadConfigs()
        observeInputFlow()
    }

    private fun loadConfigs() {
        try {
            ConfigManager.load(appName = AppConfigs.NAME)
            setNoConfigStatusIfNecessary()
        }
        catch (e: Exception) {
            e.printStackTrace()
            setDialog(HomeDialogState.Error(error = Errors.LOAD_CONFIGS))
        }
    }

    private fun observeInputFlow() {
        viewModelScope.launch {
            inputFlow.collectLatest {
                val navigation = when(it?.type) {
                    MediaType.AUDIO -> HomeNavigationState.Audio
                    MediaType.VIDEO -> HomeNavigationState.Video
                    else -> HomeNavigationState.Initial
                }

                setNavigation(navigation)
            }
        }
    }

    private fun setNoConfigStatusIfNecessary() {
        val ffmpegPath = ConfigManager.getFFmpegPath()
        val ffprobePath = ConfigManager.getFFprobePath()

        if (ffmpegPath.isBlank() || ffprobePath.isBlank()) {
            setDialog(HomeDialogState.Settings)
            return
        }

        val ffmpegFile = File(ffmpegPath)
        val ffprobeFile = File(ffprobePath)

        if (!ffmpegFile.exists() || !ffmpegFile.isFile ||
            !ffprobeFile.exists() || !ffprobeFile.isFile) {
            setDialog(HomeDialogState.Settings)
        }
    }

    private fun setInput(path: String) {
        _state.update { copy(loading = true) }

        viewModelScope.launch(context = Dispatchers.IO) {
            val ffprobe = FFprobe(file = File(path))
            val data = ffprobe.analyze()
            val error = data.run {
                when {
                    this == null -> Errors.UNKNOWN_INPUT_MEDIA
                    duration == null -> Errors.NO_DURATION_INPUT_MEDIA
                    type == MediaType.AUDIO -> validateAudioStream()
                    type == MediaType.VIDEO -> validateVideoStream()
                    else -> null
                }
            }

            val dialog = error
                ?.let { HomeDialogState.Error(error = it) }
                ?: HomeDialogState.None

            val input = data
                .takeIf { error == null }
                ?.toInputMedia()

            notifyMain {
                _state.update {
                    copy(
                        loading = false,
                        dialog = dialog,
                        input = input
                    )
                }
            }
        }
    }

    private fun InputMediaData.validateAudioStream(): String? {
        val stream = audioStreams.firstOrNull()

        return when {
            stream == null -> Errors.NO_STREAM_FOUND_INPUT_MEDIA
            stream.channels == null -> Errors.NO_CHANNELS_INPUT_MEDIA
            else -> null
        }
    }

    private fun InputMediaData.validateVideoStream(): String? {
        val stream = videoStreams.firstOrNull()

        return when {
            stream == null -> Errors.NO_STREAM_FOUND_INPUT_MEDIA
            stream.width == null || stream.height == null -> Errors.NO_RESOLUTION_INPUT_MEDIA
            else -> null
        }
    }

    override fun setNavigation(state: HomeNavigationState) = _state.update { copy(navigation = state) }

    override fun setDialog(state: HomeDialogState) = _state.update { copy(dialog = state) }

    override fun pickFile(title: String) {
        FileUtils.pickFile(title = title)?.let {
            setInput(path = it)
        }
    }
}