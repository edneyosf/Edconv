package features.home.managers

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import core.Configs.av1PresetDefault
import core.Configs.channelsDefault
import core.Configs.h265PresetDefault
import core.Configs.kbpsDefault
import core.Configs.noAudioDefault
import core.Configs.outputFileDefault
import core.Configs.vbrDefault
import core.common.Manager
import core.extensions.update
import core.utils.PropertyUtils
import edconv.common.MediaFormat
import edconv.common.PixelFormats
import edconv.common.Resolutions
import edconv.core.Edconv
import edconv.core.data.MediaInfoData
import edconv.core.data.ProgressData
import edconv.core.utils.*
import features.home.events.HomeEvent
import features.home.states.HomeState
import features.home.states.HomeStatus
import kotlinx.coroutines.*
import java.io.File

class HomeManager(override val scope: CoroutineScope): Manager(scope) {

    private val _state = mutableStateOf(defaultState())
    val state: State<HomeState> = _state
    private val _logs = mutableStateOf("")
    val logs: State<String> = _logs

    private var conversionJob: Job? = null
    private var mediaInfo: MediaInfoData? = null
    private val converter = Edconv(
        binDir = PropertyUtils.binDir,
        scope = scope,
        onStdout = ::onStdout,
        onStderr = ::onStderr
    )

    fun onEvent(event: HomeEvent) = event.run {
        when(this) {
            is HomeEvent.SetInputFile -> setInputFile(inputFile)
            is HomeEvent.SetOutputFile -> setOutputFile(outputFile)
            is HomeEvent.SetFormat -> setFormat(format)
            is HomeEvent.SetChannels -> setChannels(channels)
            is HomeEvent.SetVbr -> setVbr(vbr)
            is HomeEvent.SetKbps -> setKbps(kbps)
            is HomeEvent.SetSampleRate -> setSampleRate(sampleRate)
            is HomeEvent.SetPreset -> setPreset(preset)
            is HomeEvent.SetCrf -> setCrf(crf)
            is HomeEvent.SetResolution -> setResolution(resolution)
            is HomeEvent.SetBit -> setBit(bit)
            is HomeEvent.SetNoAudio -> setNoAudio(noAudio)
            is HomeEvent.OnStart -> convert()
            is HomeEvent.OnStop -> stopConversion()
        }
    }

    private fun convert() {
        val inputFile = state.value.inputFile
        val format = state.value.format

        if(inputFile != null) {
            val mediaFormat = MediaFormat.fromString(format)

            prepareConversion()

            when(mediaFormat) {
                MediaFormat.AAC -> convertToAAC(inputFile)
                MediaFormat.EAC3 -> convertToEAC3(inputFile)
                MediaFormat.H265 -> convertToH265(inputFile)
                MediaFormat.AV1 -> convertToAV1(inputFile)
                else -> {
                    //TODO
                }
            }
        }
        else {
            //TODO
        }
    }

    private fun stopConversion() = scope.launch(context = Dispatchers.Main) {
        conversionJob?.cancelAndJoin()
        setStatus(HomeStatus.Initial)
    }

    private fun convertToAAC(inputFile: String) = _state.value.run {
        conversionJob = converter.toAAC(
            inputFile = inputFile,
            outputFile = outputFile,
            channels = channels,
            vbr = vbr,
            kbps = kbps,
            sampleRate = sampleRate
        )
    }

    private fun convertToEAC3(inputFile: String) = _state.value.run {
        conversionJob = converter.toEAC3(
            inputFile = inputFile,
            outputFile = outputFile,
            channels = channels,
            kbps = kbps,
            sampleRate = sampleRate
        )
    }

    private fun convertToH265(inputFile: String) = _state.value.run {
        val preset = preset ?: h265PresetDefault

        conversionJob = converter.toH265(
            inputFile = inputFile,
            outputFile = outputFile,
            preset = preset,
            crf = crf,
            resolution = resolution,
            bit = bit,
            noAudio = noAudio
        )
    }

    private fun convertToAV1(inputFile: String) = _state.value.run {
        val preset = preset ?: av1PresetDefault

        conversionJob = converter.toAV1(
            inputFile = inputFile,
            outputFile = outputFile,
            preset = preset,
            crf = crf,
            resolution = resolution,
            bit = bit,
            noAudio = noAudio
        )
    }

    private fun onStdout(it: String) {
        retrieveStatus(it)
        retrieveMediaInfo(it)
        retrieveProgress(it)
        setLog(it)
    }

    private fun onStderr(it: String) {
        setStatus(HomeStatus.Error(it))
        setLog(it)
    }

    private fun prepareConversion() {
        val output = File(_state.value.outputFile).parentFile

        if(!output.exists()) output.mkdirs()

        setStatus(HomeStatus.Loading)
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

            setStatus(HomeStatus.Progress(percentage))
        }
    }

    private fun retrieveStatus(it: String) {
        val isComplete = it.isStatusComplete()
        val isError = it.isStatusError()

        if(isComplete) setStatus(HomeStatus.Complete)
        else if(isError) setStatus(HomeStatus.Error())
    }

    private fun setStatus(status: HomeStatus) = _state.update { copy(status = status) }
    private fun setInputFile(inputFile: String) = _state.update { copy(inputFile = inputFile) }
    private fun setOutputFile(outputFile: String) = _state.update { copy(outputFile = outputFile) }
    private fun setFormat(format: String) = _state.update { copy(format = format) }
    private fun setChannels(channels: String) = _state.update { copy(channels = channels) }
    private fun setVbr(vbr: String) = _state.update { copy(vbr = vbr) }
    private fun setKbps(kbps: String) = _state.update { copy(kbps = kbps) }
    private fun setSampleRate(sampleRate: String?) = _state.update { copy(sampleRate = sampleRate) }
    private fun setPreset(preset: String?) = _state.update { copy(preset = preset) }
    private fun setCrf(crf: Int) = _state.update { copy(crf = crf) }
    private fun setResolution(resolution: Resolutions) = _state.update { copy(resolution = resolution) }
    private fun setBit(bit: String) = _state.update { copy(bit = bit) }
    private fun setNoAudio(noAudio: Boolean) = _state.update { copy(noAudio = noAudio) }

    companion object {
        fun defaultState() = HomeState(
            status = HomeStatus.Initial,
            inputFile = null,
            outputFile = outputFileDefault,
            format = MediaFormat.AAC.codec,
            channels = channelsDefault,
            vbr = vbrDefault,
            kbps = kbpsDefault,
            sampleRate = null,
            preset = null,
            crf = 0,
            resolution = null,
            bit = PixelFormats.bit8,
            noAudio = noAudioDefault
        )
    }
}