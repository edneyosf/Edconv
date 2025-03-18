package features.vms

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import core.edconv.Edconv
import core.aac.AACVBRs
import core.av1.AV1Preset
import core.common.ViewModel
import core.edconv.common.*
import core.edconv.data.MediaInfoData
import core.edconv.data.ProgressData
import core.edconv.utils.*
import core.h265.H265Preset
import features.states.HomeUiState
import kotlinx.coroutines.CoroutineScope

class HomeViewModel(override val scope: CoroutineScope) : ViewModel(scope) {

    private val convert = Edconv(scope = scope, onStdout = ::onStdout, onStderr = ::onStderr)
    private var mediaInfo: MediaInfoData? = null

    private val _uiState = mutableStateOf<HomeUiState>(HomeUiState.Initial)
    val uiState: State<HomeUiState> = _uiState
    private val _data = mutableStateOf("")
    val data: State<String> = _data

    fun convert(inputFile: String, outputFile: String, format: MediaFormat) {
        prepareConversion()

        when(format) {
            MediaFormat.AAC -> {
                convert.toAAC(
                    inputFile = inputFile,
                    outputFile = outputFile,
                    channels = Channels.DOWNMIXING_SURROUND_51,
                    vbr = AACVBRs.Q5
                )
            }
            MediaFormat.EAC3 -> {
                convert.toEAC3(
                    inputFile = inputFile,
                    outputFile = outputFile,
                    channels = Channels.SURROUND_51,
                    kbps = Kbps.K384
                )
            }
            MediaFormat.H265 -> {
                convert.toH265(
                    inputFile = inputFile,
                    outputFile = outputFile,
                    preset = H265Preset.SLOW,
                    crf = 21,
                    resolution = Resolutions.P1080,
                    bit = PixelFormats.bit8,
                    noAudio = true
                )
            }
            MediaFormat.AV1 -> {
                convert.toAV1(
                    inputFile = inputFile,
                    outputFile = outputFile,
                    preset = AV1Preset.P4,
                    crf = 27,
                    resolution = Resolutions.P1080,
                    bit = PixelFormats.bit8,
                    noAudio = true
                )
            }
        }
    }

    private fun prepareConversion() {
        _uiState.value = HomeUiState.Loading
        mediaInfo = null
    }

    private fun onStdout(it: String) {
        val isProgress = it.isProgress()
        val isMediaInfo = it.isMediaInfo()

        if(isMediaInfo) {
            val json = it.retrieveMediaInfoJson()
            mediaInfo = MediaInfoData.fromJsonString(json)
        }

        val mediaInfo = this.mediaInfo

        if(isProgress && mediaInfo != null) {
            val json = it.retrieveProgressJson()
            val progress = ProgressData.fromJsonString(json)
            _uiState.value = HomeUiState.Progress(mediaInfo.duration calculateProgress progress.time)
        }
    }

    private fun onStderr(it: String) {
        _data.value += "\n$it"
    }
}