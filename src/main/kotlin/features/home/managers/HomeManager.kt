package features.home.managers

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import core.Configs.aacKbpsDefault
import core.Configs.av1PresetDefault
import core.Configs.eac3KbpsDefault
import core.Configs.h265PresetDefault
import core.Configs.noAudioDefault
import core.Configs.outputFileDefault
import core.Configs.vbrDefault
import core.common.Manager
import core.extensions.update
import core.utils.DirUtils
import edconv.common.Channels
import edconv.common.MediaFormat
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

    private var conversionJob: Job? = null
    private var mediaInfo: MediaInfoData? = null
    private val converter = Edconv(
        binDir = DirUtils.binDir,
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
            is HomeEvent.OnStart -> startConversion()
            is HomeEvent.OnStop -> stopConversion()
        }
    }

    private fun startConversion() {
        val inputFile = state.value.inputFile
        val outputFile = state.value.outputFile
        val mediaFormat = state.value.format

        if(inputFile != null && mediaFormat != null && outputFile != null) {
            prepareConversion()

            when(mediaFormat) {
                MediaFormat.AAC -> convertToAAC(inputFile, outputFile)
                MediaFormat.EAC3 -> convertToEAC3(inputFile, outputFile)
                MediaFormat.H265 -> convertToH265(inputFile, outputFile)
                MediaFormat.AV1 -> convertToAV1(inputFile, outputFile)
            }
        }
    }

    private fun stopConversion() = scope.launch(context = Dispatchers.IO) {
        withContext(context = Dispatchers.Main) { setStatus(HomeStatus.Loading) }
        converter.destroyProcess()
        conversionJob?.cancelAndJoin()
        conversionJob = null
        withContext(context = Dispatchers.Main) { setStatus(HomeStatus.Initial) }
    }

    private fun convertToAAC(inputFile: String, outputFile: String) = _state.value.run {
        val kbps = kbps ?: aacKbpsDefault

        conversionJob = converter.toAAC(
            inputFile = inputFile,
            outputFile = outputFile,
            channels = channels,
            vbr = vbr,
            kbps = kbps,
            sampleRate = sampleRate
        )
    }

    private fun convertToEAC3(inputFile: String, outputFile: String) = _state.value.run {
        val kbps = kbps ?: eac3KbpsDefault

        conversionJob = converter.toEAC3(
            inputFile = inputFile,
            outputFile = outputFile,
            channels = channels,
            kbps = kbps,
            sampleRate = sampleRate
        )
    }

    private fun convertToH265(inputFile: String, outputFile: String) = _state.value.run {
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

    private fun convertToAV1(inputFile: String, outputFile: String) = _state.value.run {
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
        val output = _state.value.outputFile

        setStatus(HomeStatus.Loading)

        if(output != null) {
            val outputFile = File(output).parentFile

            if(!outputFile.exists()) outputFile.mkdirs()
        }

        setLogs("")
        mediaInfo = null
    }

    private fun setLog(it: String) = setLogs(_state.value.logs + "\n$it")

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

    private fun setFormat(format: MediaFormat?) = _state.update {
        val inputFile = inputFile

        if(inputFile != null && format != null) {
            val outputFileName = File(inputFile).nameWithoutExtension
            val extension = format.toFileExtension()
            val output = "$outputFileDefault$outputFileName.$extension"

            copy(format = format, outputFile = output)
        }
        else copy(format = format)
    }

    private fun setInputFile(inputFile: String) = _state.update {
        val input = File(inputFile)
        val outputFileName = input.nameWithoutExtension
        val extension = format?.toFileExtension() ?: input.extension
        val output = "$outputFileDefault$outputFileName.$extension"

        copy(inputFile = inputFile, outputFile = output)
    }

    private fun setStatus(status: HomeStatus) = _state.update { copy(status = status) }
    private fun setOutputFile(outputFile: String) = _state.update { copy(outputFile = outputFile) }
    private fun setChannels(channels: Channels?) = _state.update { copy(channels = channels) }
    private fun setVbr(vbr: String) = _state.update { copy(vbr = vbr) }
    private fun setKbps(kbps: String) = _state.update { copy(kbps = kbps) }
    private fun setSampleRate(sampleRate: String?) = _state.update { copy(sampleRate = sampleRate) }
    private fun setPreset(preset: String) = _state.update { copy(preset = preset) }
    private fun setCrf(crf: Int) = _state.update { copy(crf = crf) }
    private fun setResolution(resolution: Resolutions?) = _state.update { copy(resolution = resolution) }
    private fun setBit(bit: String?) = _state.update { copy(bit = bit) }
    private fun setNoAudio(noAudio: Boolean) = _state.update { copy(noAudio = noAudio) }
    private fun setLogs(logs: String) = _state.update { copy(logs = logs) }

    companion object {
        fun defaultState() = HomeState(
            status = HomeStatus.Initial,
            logs = "",
            inputFile = null,
            outputFile = null,
            format = null,
            channels = null,
            vbr = vbrDefault,
            kbps = null,
            sampleRate = null,
            preset = null,
            crf = 0,
            resolution = null,
            bit = null,
            noAudio = noAudioDefault
        )
    }
}