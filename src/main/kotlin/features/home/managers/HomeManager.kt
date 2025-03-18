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

    private val _state = mutableStateOf<HomeState>(HomeState.Initial)
    val state: State<HomeState> = _state
    private val _logs = mutableStateOf("")
    val logs: State<String> = _logs

    val outputFile = mutableStateOf(OUTPUT_FILE_DEFAULT)
    val format = mutableStateOf("")
    val channels = mutableStateOf(CHANNELS_DEFAULT)
    val vbr = mutableStateOf(VBR_DEFAULT)
    val kbps = mutableStateOf(KBPS_DEFAULT)
    val sampleRate = mutableStateOf<String?>(null)
    val preset = mutableStateOf<String?>(null)
    val crf = mutableStateOf<Int?>(null)
    val resolution = mutableStateOf(RESOLUTION_DEFAULT)
    val bit = mutableStateOf<String?>(null)
    val noAudio = mutableStateOf(NO_AUDIO_DEFAULT)

    private val converter = Edconv(scope = scope, onStdout = ::onStdout, onStderr = ::onStderr)
    private var mediaInfo: MediaInfoData? = null

    fun convert(inputFile: String) {
        val mediaFormat = MediaFormat.fromString(format.value)

        prepareConversion()

        when(mediaFormat) {
            MediaFormat.AAC -> convertToAAC(inputFile)
            MediaFormat.EAC3 -> convertToEAC3(inputFile)
            MediaFormat.H265 -> convertToH265(inputFile)
            MediaFormat.AV1 -> convertToAV1(inputFile)
        }
    }

    private fun convertToAAC(inputFile: String) = converter.toAAC(
        inputFile = inputFile,
        outputFile = outputFile.value,
        channels = channels.value,
        vbr = vbr.value,
        kbps = kbps.value,
        sampleRate = sampleRate.value
    )

    private fun convertToEAC3(inputFile: String) = converter.toEAC3(
        inputFile = inputFile,
        outputFile = outputFile.value,
        channels = channels.value,
        kbps = kbps.value,
        sampleRate = sampleRate.value
    )

    private fun convertToH265(inputFile: String) {
        val preset = preset.value ?: H265_PRESET_DEFAULT
        val crf = crf.value ?: H265_CRF_DEFAULT

        converter.toH265(
            inputFile = inputFile,
            outputFile = outputFile.value,
            preset = preset,
            crf = crf,
            resolution = resolution.value,
            bit = bit.value,
            noAudio = noAudio.value
        )
    }

    private fun convertToAV1(inputFile: String) {
        val preset = preset.value ?: AV1_PRESET_DEFAULT
        val crf = crf.value ?: AV1_CRF_DEFAULT

        converter.toAV1(
            inputFile = inputFile,
            outputFile = outputFile.value,
            preset = preset,
            crf = crf,
            resolution = resolution.value,
            bit = bit.value,
            noAudio = noAudio.value
        )
    }

    private fun onStdout(it: String) {
        retrieveStatus(it)
        retrieveMediaInfo(it)
        retrieveProgress(it)
        setLog(it)
    }

    private fun onStderr(it: String) {
        _state.value = HomeState.Error(it)
        setLog(it)
        println("Error: "+it)
    }

    private fun prepareConversion() {
        _state.value = HomeState.Loading
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

            _state.value = HomeState.Progress(percentage)
        }
    }

    private fun retrieveStatus(it: String) {
        val isComplete = it.isStatusComplete()
        val isError = it.isStatusError()

        if(isComplete) _state.value = HomeState.Complete
        else if(isError) _state.value = HomeState.Error()
    }
}