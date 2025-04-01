package features.home.managers

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import core.Configs
import core.Configs.outputFileDefault
import core.common.Manager
import core.extensions.update
import core.utils.MediaUtils
import edconv.common.*
import edconv.core.Edconv
import edconv.core.data.MediaData
import edconv.core.data.ProgressData
import edconv.core.utils.*
import edconv.ffmpeg.FFmpeg
import features.home.events.HomeEvent
import features.home.states.HomeState
import features.home.states.HomeStatus
import kotlinx.coroutines.*
import java.io.File

class HomeManager(override val scope: CoroutineScope): Manager(scope) {

    private val _state = mutableStateOf(defaultState())
    val state: State<HomeState> = _state

    private var conversionJob: Job? = null
    private var mediaInfo: MediaData? = null
    private val converter = Edconv(
        scope = scope,
        onStart = ::onStart,
        onStdout = ::onStdout,
        onError = ::onError,
        onProgress = ::onProgress,
        onStop = ::onStop
    )

    fun onEvent(event: HomeEvent) = event.run {
        when(this) {
            is HomeEvent.SetInputFile -> setInputFile(inputFile)
            is HomeEvent.SetOutputFile -> setOutputFile(outputFile)
            is HomeEvent.SetCodec -> setFormat(codec)
            is HomeEvent.SetChannels -> setChannels(channels)
            is HomeEvent.SetVbr -> setVbr(vbr)
            is HomeEvent.SetBitrate -> setKbps(bitrate)
            is HomeEvent.SetSampleRate -> setSampleRate(sampleRate)
            is HomeEvent.SetPreset -> setPreset(preset)
            is HomeEvent.SetCrf -> setCrf(crf)
            is HomeEvent.SetResolution -> setResolution(resolution)
            is HomeEvent.SetPixelFormat -> setPixelFormat(pixelFormat)
            is HomeEvent.SetNoAudio -> setNoAudio(noAudio)
            is HomeEvent.OnStart -> startConversion()
            is HomeEvent.OnStop -> stopConversion()
        }
    }

    private fun startConversion() {
        val inputFile = state.value.inputFile
        val outputFile = state.value.outputFile
        val codec = state.value.codec

        if(inputFile != null && codec != null && outputFile != null) {
            prepareConversion()

            when(codec) {
                Codec.AAC -> convertToAAC(inputFile, outputFile, codec)
                Codec.AAC_FDK -> convertToAAC(inputFile, outputFile, codec)
                Codec.EAC3 -> convertToEAC3(inputFile, outputFile, codec)
                Codec.H265 -> convertToH265(inputFile, outputFile, codec)
                Codec.AV1 -> convertToAV1(inputFile, outputFile, codec)
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

    private fun convertToAAC(inputFile: MediaData, outputFile: String, codec: Codec) = _state.value.run {
        var ffmpeg: FFmpeg? = null

        vbr?.let {
            ffmpeg = FFmpeg.createVariableAudio(
                source = Configs.ffmpegPath,
                logLevel = Configs.ffmpegLogLevel,
                input = inputFile.path,
                output = outputFile,
                codec = codec.value,
                vbr = it.toString(),
                sampleRate = sampleRate?.value,
                channels = channels?.value,
                filter = channels?.downmixingFilter
            )
        }
        bitrate?.let {
            ffmpeg = FFmpeg.createConstantAudio(
                source = Configs.ffmpegPath,
                logLevel = Configs.ffmpegLogLevel,
                input = inputFile.path,
                output = outputFile,
                codec = codec.value,
                bitRate = it.value,
                sampleRate = sampleRate?.value,
                channels = channels?.value,
                filter = channels?.downmixingFilter
            )
        }

        ffmpeg?.let { conversionJob = converter.run(it) }
    }

    private fun convertToEAC3(inputFile: MediaData, outputFile: String, codec: Codec) = _state.value.run {
        bitrate?.let {
            val ffmpeg = FFmpeg.createConstantAudio(
                source = Configs.ffmpegPath,
                logLevel = Configs.ffmpegLogLevel,
                input = inputFile.path,
                output = outputFile,
                codec = codec.value,
                bitRate = it.value,
                sampleRate = sampleRate?.value,
                channels = channels?.value,
                filter = null
            )

            conversionJob = converter.run(ffmpeg)
        }
    }

    private fun convertToH265(inputFile: MediaData, outputFile: String, codec: Codec) = _state.value.run {
        preset?.let {
            /*val ffmpeg = FFmpeg.createVariableVideo(
                source = Configs.ffmpegPath,
                logLevel = Configs.ffmpegLogLevel,
                input = inputFile,
                output = outputFile,
                codec = codec.value,
                preset = it,
                crf = crf,
                filter = resolution?.preserveAspectRatioFilter()
            )

            conversionJob = converter.toH265(
                inputFile = inputFile,
                outputFile = outputFile,
                preset = it,
                crf = crf,
                resolution = resolution,
                pixelFormat = pixelFormat,
                noAudio = noAudio
            )*/
        }
    }

    private fun convertToAV1(inputFile: MediaData, outputFile: String, codec: Codec) = _state.value.run {
        preset?.let {
            /*conversionJob = converter.toAV1(
                inputFile = inputFile,
                outputFile = outputFile,
                preset = it,
                crf = crf,
                resolution = resolution,
                pixelFormat = pixelFormat,
                noAudio = noAudio
            )*/
        }
    }

    private fun onStart() {
        setStatus(HomeStatus.Loading)
    }

    private fun onStdout(it: String) {
        //retrieveStatus(it)
        //retrieveMediaInfo(it)
        //retrieveProgress(it)
        setLog(it)
    }

    private fun onError(it: Throwable) {
        setStatus(HomeStatus.Error(it.message))
    }

    private fun onProgress(it: ProgressData) {
        val percentage = (_state.value.inputFile?.duration ?: 0) calculateProgress it.time

        setStatus(HomeStatus.Progress(percentage))
    }

    infix fun Long.calculateProgress(duration: Long): Float {
        return if (this > 0) (duration.toFloat() / this.toFloat()) * 100 else 0.0f
    }

    private fun onStop() {
        setStatus(HomeStatus.Complete)
    }

    private fun prepareConversion() {
        val output = _state.value.outputFile

        if(output != null) {
            val outputFile = File(output).parentFile

            if(!outputFile.exists()) outputFile.mkdirs()
        }

        setLogs("")
        mediaInfo = null
    }

    private fun setLog(it: String) = setLogs(_state.value.logs + "\n$it")

    private fun setFormat(format: Codec?) = _state.update {
        val inputFile = inputFile

        if(inputFile != null && format != null) {
            val outputFileName = File(inputFile.path).nameWithoutExtension
            val extension = format.toFileExtension()
            val output = "$outputFileDefault$outputFileName.$extension"

            copy(codec = format, outputFile = output)
        }
        else copy(codec = format)
    }

    private fun setInputFile(path: String) {
        val input = File(path)
        val outputFileName = input.nameWithoutExtension
        val extension = _state.value.codec?.toFileExtension() ?: input.extension
        val output = "$outputFileDefault$outputFileName.$extension"

        scope.launch(context = Dispatchers.IO) {
            val type = MediaUtils.getType(input)
            val size = MediaUtils.getSize(input)
            val duration: Long?
            val resolution: Pair<Int, Int>?

            if(type == MediaType.AUDIO) {
                duration = MediaUtils.getDuration(input)
                val media = MediaData(path = path, type = type, duration = duration!!, size = size)

                println("Media: $media")

                withContext(context = Dispatchers.Main) {
                    _state.value = _state.value.copy(inputFile = media, outputFile = output)
                }
            }
            else if(type == MediaType.VIDEO) {
                duration = MediaUtils.getDuration(input)
                resolution = MediaUtils.getVideoResolution(input)
                val media = MediaData(path = path, type = type, duration = duration!!, resolution = resolution, size = size)

                println("Media: $media")

                withContext(context = Dispatchers.Main) {
                    _state.value = _state.value.copy(inputFile = media, outputFile = output)
                }
            }
            else {
                println("Media: desconhecida")
            }
        }
    }

    private fun setStatus(status: HomeStatus) = _state.update { copy(status = status) }
    private fun setOutputFile(outputFile: String) = _state.update { copy(outputFile = outputFile) }
    private fun setChannels(channels: Channels?) = _state.update { copy(channels = channels) }
    private fun setVbr(vbr: Int?) = _state.update { copy(vbr = vbr) }
    private fun setKbps(bitrate: Bitrate) = _state.update { copy(bitrate = bitrate) }
    private fun setSampleRate(sampleRate: SampleRate?) = _state.update { copy(sampleRate = sampleRate) }
    private fun setPreset(preset: String) = _state.update { copy(preset = preset) }
    private fun setCrf(crf: Int) = _state.update { copy(crf = crf) }
    private fun setResolution(resolution: Resolution?) = _state.update { copy(resolution = resolution) }
    private fun setPixelFormat(pixelFormat: PixelFormat?) = _state.update { copy(pixelFormat = pixelFormat) }
    private fun setNoAudio(noAudio: Boolean) = _state.update { copy(noAudio = noAudio) }
    private fun setLogs(logs: String) = _state.update { copy(logs = logs) }

    companion object {
        fun defaultState() = HomeState(
            status = HomeStatus.Initial,
            logs = "",
            inputFile = null,
            outputFile = null,
            codec = null,
            channels = null,
            vbr = null,
            bitrate = null,
            sampleRate = null,
            preset = null,
            crf = 0,
            resolution = null,
            pixelFormat = null,
            noAudio = false
        )
    }
}