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
import features.home.events.HomeEvent
import features.home.states.HomeState
import features.home.states.status.HomeStatus
import kotlinx.coroutines.CoroutineScope

class HomeManager(override val scope: CoroutineScope): Manager(scope) {

    private val _state = mutableStateOf(defaultState())
    val state: State<HomeState> = _state
    private val _logs = mutableStateOf("")
    val logs: State<String> = _logs

    private val converter = Edconv(scope = scope, onStdout = ::onStdout, onStderr = ::onStderr)
    private var mediaInfo: MediaInfoData? = null

    fun onEvent(it: HomeEvent) {
        when(it) {
            is HomeEvent.OnStart -> convert(it.inputFile)
        }
    }

    fun setState(
        inputFile: String? = _state.value.inputFile,
        outputFile: String = _state.value.outputFile,
        format: String = _state.value.format,
        channels: String = _state.value.channels,
        vbr: String = _state.value.vbr,
        kbps: String = _state.value.kbps,
        sampleRate: String? = _state.value.sampleRate,
        preset: String? = _state.value.preset,
        crf: Int = _state.value.crf,
        resolution: Resolutions = _state.value.resolution,
        bit: String = _state.value.bit,
        noAudio: Boolean = _state.value.noAudio,
    ) {
        _state.value = _state.value.copy(
            inputFile = inputFile,
            outputFile = outputFile,
            format = format,
            channels = channels,
            vbr = vbr,
            kbps = kbps,
            sampleRate = sampleRate,
            preset = preset,
            crf = crf,
            resolution = resolution,
            bit = bit,
            noAudio = noAudio
        )
    }

    private fun convert(inputFile: String) {
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

    private fun defaultState() = HomeState(
        status = HomeStatus.Initial,
        inputFile = null,
        outputFile = OUTPUT_FILE_DEFAULT,
        format = MediaFormat.AAC.codec,
        channels = CHANNELS_DEFAULT,
        vbr = VBR_DEFAULT,
        kbps = KBPS_DEFAULT,
        sampleRate = null,
        preset = null,
        crf = 0,
        resolution = RESOLUTION_DEFAULT,
        bit = PixelFormats.bit8,
        noAudio = NO_AUDIO_DEFAULT
    )
}