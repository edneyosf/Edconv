package edneyosf.edconv.features.home

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edneyosf.edconv.core.process.EdProcess
import edneyosf.edconv.core.common.Error
import edneyosf.edconv.core.common.OS
import edneyosf.edconv.core.config.EdConfig
import edneyosf.edconv.core.config.RemoteConfig
import edneyosf.edconv.core.extensions.notifyMain
import edneyosf.edconv.core.extensions.update
import edneyosf.edconv.core.utils.PlatformUtils
import edneyosf.edconv.features.common.models.InputMedia
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

class HomeViewModel(
    private val config: EdConfig,
    private val remoteConfig: RemoteConfig,
    private val process: EdProcess
): ViewModel(), HomeEvent {

    private val _state = MutableStateFlow(value = HomeState())
    val state: StateFlow<HomeState> = _state

    val inputsState: StateFlow<List<InputMedia>> = process.inputs

    init {
        loadConfigs()
        observeInput()
    }

    private fun loadConfigs() {
        try {
            config.load()
            setNoConfigStatusIfNecessary()
            viewModelScope.launch {
                remoteConfig.loadEnv()
                _state.update {
                    copy(
                        latestVersion = remoteConfig.latestVersion,
                        donationUrl = remoteConfig.donationUrl,
                        releasesUrl = remoteConfig.releasesUrl
                    )
                }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            setDialog(HomeDialogState.Failure(Error.LOAD_CONFIGS))
        }
    }

    private fun observeInput() {
        viewModelScope.launch {
            inputsState.collectLatest {
                val first = it.firstOrNull()
                val navigation = when(first?.type) {
                    MediaType.AUDIO, MediaType.VIDEO -> HomeNavigationState.Media
                    else -> HomeNavigationState.Initial
                }

                process.setInputType(first?.type)
                setNavigation(navigation)
            }
        }
    }

    private fun setNoConfigStatusIfNecessary() {
        viewModelScope.launch(context = Dispatchers.IO) {
            updateFFBinaryPath(
                currentPath = config.ffmpegPath,
                name = "ffmpeg"
            ) {  config.ffmpegPath  = it }
            updateFFBinaryPath(
                currentPath = config.ffprobePath,
                name = "ffprobe"
            ) {  config.ffprobePath  = it }

            val ffmpegPath = config.ffmpegPath
            val ffprobePath = config.ffprobePath

            if (ffmpegPath.isBlank() || ffprobePath.isBlank()) {
                setDialog(HomeDialogState.Settings)
                return@launch
            }

            val ffmpegFile = File(ffmpegPath)
            val ffprobeFile = File(ffprobePath)

            if (!ffmpegFile.exists() || !ffmpegFile.isFile ||
                !ffprobeFile.exists() || !ffprobeFile.isFile) {
                setDialog(HomeDialogState.Settings)
            }
        }
    }

    override fun setInputs(paths: List<String>) {
        if(paths.isNotEmpty()) {
            _state.update { copy(loading = true) }
            viewModelScope.launch(context = Dispatchers.IO) {
                val data = mutableListOf<InputMedia>()
                var error: Error? = null

                for (path in paths) {
                    val inputFile = File(path)
                    val ffprobe = FFprobe(ffprobePath = config.ffprobePath, file = inputFile)
                    val inputData = ffprobe.analyze()

                    error = inputData.run {
                        when {
                            this == null -> Error.UNKNOWN_INPUT_MEDIA
                            duration == null -> Error.NO_DURATION_INPUT_MEDIA
                            type == MediaType.AUDIO -> validateAudioStream()
                            type == MediaType.VIDEO -> validateVideoStream()
                            else -> null
                        }
                    }

                    if(error != null) {
                        data.clear()
                        break
                    }
                    else {
                        val input = inputData?.toInputMedia()

                        input?.let { data.add(it) }
                    }
                }

                val dialog = error
                    ?.let { HomeDialogState.Failure(error = it) }
                    ?: HomeDialogState.None

                notifyMain {
                    process.setInputs(data)
                    _state.update { copy(loading = false, dialog = dialog) }
                }
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

    override fun setNavigation(state: HomeNavigationState) { _state.update { copy(navigation = state) } }

    override fun setDialog(state: HomeDialogState) = _state.update { copy(dialog = state) }

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onDragAndDropInput(event: DragAndDropEvent): Boolean {
        try {
            val data = event.awtTransferable.getTransferData(DataFlavor.javaFileListFlavor)

            if (data is List<*>) {
                val files = data.filterIsInstance<File>()
                val inputs = mutableListOf<String>()

                files.forEach { inputs.add(it.absolutePath) }
                setInputs(inputs)

                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            setDialog(HomeDialogState.Failure(Error.DEFAULT))
        }

        return false
    }

    override fun openLink(url: String) {
        if (!PlatformUtils.openLink(url)) setDialog(HomeDialogState.Failure(Error.OPEN_LINK))
    }

    private fun findFFBinary(name: String): String? {
        val platform = PlatformUtils.current
        val candidates = when(platform) {
            OS.WINDOWS -> listOf(
                "C:\\ProgramData\\chocolatey\\bin\\${name}.exe",
                "C:\\Program Files\\ffmpeg\\bin\\${name}.exe",
                "C:\\ffmpeg\\bin\\${name}.exe"
            )
            OS.MACOS -> listOf(
                "/usr/local/bin/${name}",
                "/opt/homebrew/bin/${name}"
            )
            else -> listOf(
                "/usr/bin/${name}",
                "/usr/local/bin/${name}"
            )
        }

        return candidates.firstOrNull { File(it).exists() }
    }

    private suspend fun updateFFBinaryPath(currentPath: String, name: String, savePath: (String) -> Unit) {
        if (currentPath.isBlank()) {
            findFFBinary(name)?.takeIf { it.isNotBlank() }?.let { path ->
                try { savePath(path) }
                catch (e: Exception) {
                    e.printStackTrace()
                    notifyMain { setDialog(HomeDialogState.Failure(Error.CONFIGURATION_SAVE)) }
                }
            }
        }
    }
}