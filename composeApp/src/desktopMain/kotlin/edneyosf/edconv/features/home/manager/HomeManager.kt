package edneyosf.edconv.features.home.manager

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import edneyosf.edconv.app.AppConfigs
import edneyosf.edconv.core.ConfigManager
import edneyosf.edconv.core.common.Errors
import edneyosf.edconv.core.common.Manager
import edneyosf.edconv.core.extensions.update
import edneyosf.edconv.core.utils.FileUtils
import edneyosf.edconv.ffmpeg.common.MediaType
import edneyosf.edconv.ffmpeg.data.InputMedia
import edneyosf.edconv.ffmpeg.ffprobe.FFprobe
import edneyosf.edconv.features.home.events.HomeEvent
import edneyosf.edconv.features.home.states.HomeDialogState
import edneyosf.edconv.features.home.states.HomeNavigationState
import edneyosf.edconv.features.home.states.HomeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class HomeManager(override val scope: CoroutineScope): Manager(scope), HomeEvent {

    private val _state = mutableStateOf(value = HomeState())
    val state: State<HomeState> = _state

    init { loadConfigs() }

    private fun loadConfigs() {
        try {
            ConfigManager.load(appName = AppConfigs.NAME)
            setNoConfigStatusIfNecessary()
        }
        catch (e: Exception) {
            e.printStackTrace()
            onError(id = Errors.LOAD_CONFIGS)
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

    override fun setNavigation(state: HomeNavigationState) = _state.update { copy(navigation = state) }

    override fun setDialog(state: HomeDialogState) = _state.update { copy(dialog = state) }

    override fun pickFile(title: String) {
        FileUtils.pickFile(title = title)?.let {
            setInput(path = it)
        }
    }

    private fun setInput(path: String) {
        val inputFile = File(path)

        setLoading(status = true)

        scope.launch(context = Dispatchers.IO) {
            val inputMedia = FFprobe.analyze(inputFile)
            val error = inputMedia.run {
                when {
                    this == null -> Errors.UNKNOWN_INPUT_MEDIA
                    duration == null -> Errors.NO_DURATION_INPUT_MEDIA
                    type == MediaType.AUDIO -> validateAudioStream()
                    type == MediaType.VIDEO -> validateVideoStream()
                    else -> null
                }
            }

            notifyMain {
                _state.update {
                    copy(
                        dialog = error?.let { HomeDialogState.Error(id = it) } ?: HomeDialogState.None,
                        loading = false,
                        input = inputMedia.takeIf { error == null }
                    )
                }
            }
        }
    }

    private fun InputMedia.validateAudioStream(): String? {
        val stream = audioStreams.firstOrNull()

        return when {
            stream == null -> Errors.NO_STREAM_FOUND_INPUT_MEDIA
            stream.channels == null -> Errors.NO_CHANNELS_INPUT_MEDIA
            else -> null
        }
    }

    private fun InputMedia.validateVideoStream(): String? {
        val stream = videoStreams.firstOrNull()

        return when {
            stream == null -> Errors.NO_STREAM_FOUND_INPUT_MEDIA
            stream.width == null || stream.height == null -> Errors.NO_RESOLUTION_INPUT_MEDIA
            else -> null
        }
    }

    private fun setLoading(status: Boolean) = _state.update { copy(loading = status) }

    private fun onError(id: String) = setDialog(state = HomeDialogState.Error(id = id))
}