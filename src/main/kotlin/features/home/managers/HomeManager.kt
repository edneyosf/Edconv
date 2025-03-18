package features.home.managers

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import app.AppConfigs.AV1_CRF_DEFAULT
import app.AppConfigs.AV1_PRESET_DEFAULT
import app.AppConfigs.CHANNELS_DEFAULT
import app.AppConfigs.H265_CRF_DEFAULT
import app.AppConfigs.H265_PRESET_DEFAULT
import app.AppConfigs.KBPS_DEFAULT
import app.AppConfigs.NO_AUDIO_DEFAULT
import app.AppConfigs.OUTPUT_FILE_DEFAULT
import app.AppConfigs.RESOLUTION_DEFAULT
import app.AppConfigs.VBR_DEFAULT
import core.edconv.Edconv
import core.common.Manager
import core.edconv.common.*
import core.edconv.data.MediaInfoData
import core.edconv.data.ProgressData
import core.edconv.utils.*
import features.home.states.HomeState
import kotlinx.coroutines.CoroutineScope

class HomeManager(override val scope: CoroutineScope): Manager(scope) {

    private val _uiState = mutableStateOf<HomeState>(HomeState.Initial)
    val uiState: State<HomeState> = _uiState

    private val _logs = mutableStateOf("")
    val logs: State<String> = _logs
    private val _outputFile = mutableStateOf(OUTPUT_FILE_DEFAULT)
    val outputFile: State<String> = _outputFile
    private val _channels = mutableStateOf(CHANNELS_DEFAULT)
    val channels: State<String> = _channels
    private val _vbr = mutableStateOf(VBR_DEFAULT)
    val vbr: State<String> = _vbr
    private val _kbps = mutableStateOf(KBPS_DEFAULT)
    val kbps: State<String> = _kbps
    private val _sampleRate = mutableStateOf<String?>(null)
    val sampleRate: State<String?> = _sampleRate
    private val _preset = mutableStateOf<String?>(null)
    val preset: State<String?> = _preset
    private val _crf = mutableStateOf<Int?>(null)
    val crf: State<Int?> = _crf
    private val _resolution = mutableStateOf(RESOLUTION_DEFAULT)
    val resolution: State<Resolutions> = _resolution
    private val _bit = mutableStateOf<String?>(null)
    val bit: State<String?> = _bit
    private val _noAudio = mutableStateOf(NO_AUDIO_DEFAULT)
    val noAudio: State<Boolean> = _noAudio

    private val converter = Edconv(scope = scope, onStdout = ::onStdout, onStderr = ::onStderr)
    private var mediaInfo: MediaInfoData? = null

    fun convert(inputFile: String, format: MediaFormat) {
        prepareConversion()

        when(format) {
            MediaFormat.AAC -> convertToAAC(inputFile)
            MediaFormat.EAC3 -> convertToEAC3(inputFile)
            MediaFormat.H265 -> convertToH265(inputFile)
            MediaFormat.AV1 -> convertToAV1(inputFile)
        }
    }

    private fun convertToAAC(inputFile: String) = converter.toAAC(
        inputFile = inputFile,
        outputFile = _outputFile.value,
        channels = _channels.value,
        vbr = _vbr.value,
        kbps = kbps.value,
        sampleRate = sampleRate.value
    )

    private fun convertToEAC3(inputFile: String) = converter.toEAC3(
        inputFile = inputFile,
        outputFile = _outputFile.value,
        channels = _channels.value,
        kbps = _kbps.value,
        sampleRate = _sampleRate.value
    )

    private fun convertToH265(inputFile: String) {
        val preset = _preset.value ?: H265_PRESET_DEFAULT
        val crf = _crf.value ?: H265_CRF_DEFAULT

        converter.toH265(
            inputFile = inputFile,
            outputFile = _outputFile.value,
            preset = preset,
            crf = crf,
            resolution = _resolution.value,
            bit = _bit.value,
            noAudio = _noAudio.value
        )
    }

    private fun convertToAV1(inputFile: String) {
        val preset = _preset.value ?: AV1_PRESET_DEFAULT
        val crf = _crf.value ?: AV1_CRF_DEFAULT

        converter.toAV1(
            inputFile = inputFile,
            outputFile = _outputFile.value,
            preset = preset,
            crf = crf,
            resolution = _resolution.value,
            bit = _bit.value,
            noAudio = _noAudio.value
        )
    }

    private fun onStdout(it: String) {
        retrieveStatus(it)
        retrieveMediaInfo(it)
        retrieveProgress(it)
        setLog(it)
    }

    private fun onStderr(it: String) {
        retrieveStatus(it)
        setLog(it)
    }

    private fun prepareConversion() {
        _uiState.value = HomeState.Loading
        _logs.value = ""
        mediaInfo = null
    }

    private fun setLog(it: String) {
        _logs.value += "\n$it"
    }

    private fun retrieveMediaInfo(it: String) {
        val isMediaInfo = it.isMediaInfo()

        if(isMediaInfo) {
            mediaInfo = MediaInfoData.fromJsonString(it.retrieveMediaInfoJson())
        }
    }

    private fun retrieveProgress(it: String) {
        val isProgress = it.isProgress()
        val mediaInfo = this.mediaInfo

        if(isProgress && mediaInfo != null) {
            val progress = ProgressData.fromJsonString(it.retrieveProgressJson())
            val duration = mediaInfo.duration
            val currentDuration = progress.time
            val percentage = duration calculateProgress currentDuration

            _uiState.value = HomeState.Progress(percentage)
        }
    }

    private fun retrieveStatus(it: String) {
        val isComplete = it.isStatusComplete()
        val isError = it.isStatusError()

        if(isComplete) _uiState.value = HomeState.Complete
        else if(isError) _uiState.value = HomeState.Error()
    }

    fun setOutputFile(value: String) { _outputFile.value = value }
    fun setChannels(value: String) { _channels.value = value }
    fun setVBR(value: String) { _vbr.value = value }
    fun setKbps(value: String) { _kbps.value = value }
    fun setSampleRate(value: String?) { _sampleRate.value = value }
    fun setPreset(value: String?) { _preset.value = value }
    fun setCRF(value: Int?) { _crf.value = value }
    fun setResolution(value: Resolutions) { _resolution.value = value }
    fun setBit(value: String?) { _bit.value = value }
    fun setNoAudio(value: Boolean) { _noAudio.value = value }
}