package edneyosf.edconv.features.home

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edneyosf.edconv.core.process.EdProcess
import edneyosf.edconv.core.common.Error
import edneyosf.edconv.core.config.EdConfig
import edneyosf.edconv.core.extensions.notifyMain
import edneyosf.edconv.core.extensions.update
import edneyosf.edconv.core.utils.FileUtils
import edneyosf.edconv.features.home.mappers.toInputMedia
import edneyosf.edconv.features.home.states.HomeDialogState
import edneyosf.edconv.features.home.states.HomeNavigationState
import edneyosf.edconv.features.home.states.HomeState
import edneyosf.edconv.ffmpeg.common.MediaType
import edneyosf.edconv.ffmpeg.data.InputMediaData
import edneyosf.edconv.ffmpeg.ffprobe.FFprobe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.awt.datatransfer.DataFlavor
import java.io.File

class HomeViewModel(private val config: EdConfig, private val process: EdProcess): ViewModel(), HomeEvent {

    private val _state = MutableStateFlow(value = HomeState())
    val state: StateFlow<HomeState> = _state

    init {
        loadConfigs()
        observeInput()
    }

    private fun loadConfigs() {
        try {
            config.load()
            setNoConfigStatusIfNecessary()
        }
        catch (e: Exception) {
            e.printStackTrace()
            setDialog(HomeDialogState.Failure(Error.LOAD_CONFIGS))
        }
    }

    private fun observeInput() {
        viewModelScope.launch {
            process.input.collectLatest {
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
        val ffmpegPath = config.ffmpegPath
        val ffprobePath = config.ffprobePath

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
            val inputFile = File(path)
            val ffprobe = FFprobe(ffprobePath = config.ffprobePath, file = inputFile)
            val data = ffprobe.analyze()
            val error = data.run {
                when {
                    this == null -> Error.UNKNOWN_INPUT_MEDIA
                    duration == null -> Error.NO_DURATION_INPUT_MEDIA
                    type == MediaType.AUDIO -> validateAudioStream()
                    type == MediaType.VIDEO -> validateVideoStream()
                    else -> null
                }
            }

            val dialog = error
                ?.let { HomeDialogState.Failure(error = it) }
                ?: HomeDialogState.None

            val input = data
                .takeIf { error == null }
                ?.toInputMedia()

            notifyMain {
                process.setInput(inputMedia = input)
                _state.update { copy(input = input, loading = false, dialog = dialog) }
            }
        }
    }

    private fun InputMediaData.validateAudioStream(): Error? {
        val stream = audioStreams.firstOrNull()

        return when {
            stream == null -> Error.NO_STREAM_FOUND_INPUT_MEDIA
            stream.channels == null -> Error.NO_CHANNELS_INPUT_MEDIA
            else -> null
        }
    }

    private fun InputMediaData.validateVideoStream(): Error? {
        val stream = videoStreams.firstOrNull()

        return when {
            stream == null -> Error.NO_STREAM_FOUND_INPUT_MEDIA
            stream.width == null || stream.height == null -> Error.NO_RESOLUTION_INPUT_MEDIA
            else -> null
        }
    }

    override fun setNavigation(state: HomeNavigationState) {
        val mediaType = when(state) {
            HomeNavigationState.Audio -> MediaType.AUDIO
            HomeNavigationState.Video -> MediaType.VIDEO
            else -> null
        }

        process.setInputType(mediaType)
        _state.update { copy(navigation = state) }
    }

    override fun setDialog(state: HomeDialogState) = _state.update { copy(dialog = state) }

    override fun pickFile(title: String) {
        FileUtils.pickFile(title = title)?.let {
            setInput(path = it)
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onDragAndDropInput(event: DragAndDropEvent): Boolean {
        try {
            val data = event.awtTransferable.getTransferData(DataFlavor.javaFileListFlavor)

            if (data is List<*>) {
                val files = data.filterIsInstance<File>()
                val file = files.firstOrNull()

                file?.let { setInput(file.absolutePath) }

                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            setDialog(HomeDialogState.Failure(Error.DEFAULT))
        }

        return false
    }
}