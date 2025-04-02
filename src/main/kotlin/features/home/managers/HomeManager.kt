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
import edconv.ffmpeg.FFmpeg
import features.home.events.HomeEvent
import features.home.states.HomeState
import features.home.states.HomeStatus
import kotlinx.coroutines.*
import java.io.File
import java.time.Duration
import java.time.Instant

class HomeManager(override val scope: CoroutineScope): Manager(scope) {

    private val _state = mutableStateOf(HomeState.default())
    val state: State<HomeState> = _state

    private var startTime: Instant? = null
    private var conversion: Job? = null
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
            is HomeEvent.SetStatus -> setStatus(status)
            is HomeEvent.SetInputFile -> setInputFile(inputFile)
            is HomeEvent.SetOutputFile -> setOutputFile(outputFile)
            is HomeEvent.SetCodec -> setCodec(codec)
            is HomeEvent.SetChannels -> setChannels(channels)
            is HomeEvent.SetVbr -> setVbr(vbr)
            is HomeEvent.SetBitrate -> setBitrate(bitrate)
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
            when(codec) {
                Codec.MP3, Codec.AAC, Codec.AAC_FDK,
                Codec.OPUS, Codec.AC3, Codec.EAC3, Codec.FLAC -> {
                    convertToAudio(inputFile, outputFile, codec)
                }
                Codec.H264, Codec.H265,
                Codec.VP9, Codec.AV1 -> {
                    convertToVideo(inputFile, outputFile, codec)
                }
            }
        }
        else {
            onError(Throwable("Input file, codec or output file is null"))
        }
    }

    private fun stopConversion() = scope.launch(context = Dispatchers.IO) {
        withContext(context = Dispatchers.Main) { setStatus(HomeStatus.Loading) }
        converter.destroyProcess()
        conversion?.cancelAndJoin()
        conversion = null
        withContext(context = Dispatchers.Main) { setStatus(HomeStatus.Initial) }
    }

    private fun convertToAudio(inputFile: MediaData, outputFile: String, codec: Codec) = _state.value.run {
        val ffmpeg = FFmpeg.createAudio(
            source = Configs.ffmpegPath,
            logLevel = Configs.ffmpegLogLevel,
            input = inputFile.path,
            output = outputFile,
            codec = codec.value,
            sampleRate = sampleRate?.value,
            channels = channels?.value,
            filter = channels?.downmixingFilter
        )

        vbr?.let { ffmpeg.vbr = it.toString() }
        bitrate?.let { ffmpeg.bitRate = bitrate.value }

        conversion = converter.run(ffmpeg)
    }

    private fun convertToVideo(inputFile: MediaData, outputFile: String, codec: Codec) = _state.value.run {
        val preset = preset
        val sourceResolution = inputFile.resolution
        val ffmpeg: FFmpeg

        if(preset != null && sourceResolution != null) {
            val width = sourceResolution.first
            val height = sourceResolution.second

            //TODO profile
            ffmpeg = FFmpeg.createVideo(
                source = Configs.ffmpegPath,
                logLevel = Configs.ffmpegLogLevel,
                input = inputFile.path,
                output = outputFile,
                codec = codec.value,
                preset = preset,
                crf = crf.toString(),
                pixelFormat = pixelFormat?.value,
                filter = resolution?.preserveAspectRatioFilter(sourceWidth = width, sourceHeight = height),
                noAudio = noAudio
            )
        }
        else if(sourceResolution == null) {
            onError(Throwable("Source resolution is null"))
            return@run
        }
        else {
            onError(Throwable("Preset is null"))
            return@run
        }

        conversion = converter.run(ffmpeg)
    }

    private fun createOutputDirIfNotExist() {
        _state.value.outputFile?.let {
            try { File(it).parentFile.mkdirs() }
            catch (e: Exception) {
                e.printStackTrace()
                onError(Throwable(e))
            }
        }
    }

    private fun onStart() {
        startTime = Instant.now()
        setStatus(HomeStatus.Loading)
        createOutputDirIfNotExist()
        setLogs("")
    }

    private fun onStdout(it: String) { setLogs(_state.value.logs + "$it\n") }

    private fun onError(it: Throwable) {
        println("onError: "+it.message)
        setStatus(HomeStatus.Error(it.message))
    }

    private fun onProgress(it: ProgressData) {
        val inputFile = _state.value.inputFile
        val duration = inputFile?.duration ?: 0
        val percentage = if(duration > 0) ((it.time * 100.0f) / duration) else 0.0f

        setStatus(HomeStatus.Progress(percentage))
    }

    private fun onStop() {
        val finishTime = Instant.now()
        val duration = Duration.between(startTime, finishTime)
        val dias = duration.toDays()
        val horas = duration.toHours() % 24
        val minutos = duration.toMinutes() % 60
        val segundos = duration.seconds % 60
        val milissegundos = duration.toMillis() % 1000

        setStatus(HomeStatus.Complete("startTime", "finishTime", "$dias dias, $horas:$minutos:$segundos.$milissegundos"))
        startTime = null
    }

    private fun setInputFile(path: String) {
        val inputFile = File(path)
        val outputFileName = inputFile.nameWithoutExtension
        val codec = _state.value.codec
        val extension = codec?.toFileExtension() ?: inputFile.extension
        val output = "$outputFileDefault$outputFileName.$extension"

        scope.launch(context = Dispatchers.IO) {
            val type = MediaUtils.getType(inputFile)
            val size = MediaUtils.getSize(inputFile)
            val resolution: Pair<Int, Int>?
            val duration: Long?

            when (type) {
                MediaType.AUDIO -> {
                    duration = MediaUtils.getDuration(inputFile)

                    if(duration != null) {
                        val media = MediaData(path = path, type = type, duration = duration, size = size)

                        withContext(context = Dispatchers.Main) {
                            _state.value = _state.value.copy(inputFile = media, outputFile = output)
                        }
                    }
                    else {
                        onError(Throwable("Could not retrieve the audio duration"))
                    }
                }
                MediaType.VIDEO -> {
                    duration = MediaUtils.getDuration(inputFile)
                    resolution = MediaUtils.getVideoResolution(inputFile)

                    if(duration != null && resolution != null) {
                        val media = MediaData(
                            path = path,
                            type = type,
                            duration = duration,
                            resolution = resolution,
                            size = size
                        )

                        withContext(context = Dispatchers.Main) {
                            _state.value = _state.value.copy(inputFile = media, outputFile = output)
                        }
                    }
                    else {
                        onError(Throwable("Could not retrieve the video duration or resolution"))
                    }
                }
                else -> {
                    withContext(context = Dispatchers.Main) {
                        onError(Throwable("Could not identify media type"))
                    }
                }
            }
        }
    }

    private fun setCodec(codec: Codec?) {
        val inputFile = _state.value.inputFile

        if(inputFile != null && codec != null) {
            try {
                val outputName = File(inputFile.path).nameWithoutExtension
                val outputExtension = codec.toFileExtension()
                val output = "$outputFileDefault$outputName.$outputExtension"

                _state.value = _state.value.copy(codec = codec, outputFile = output)
            }
            catch (e: Exception) {
                e.printStackTrace()
                onError(Throwable(e))
            }
        }
        else {
            _state.value = _state.value.copy(codec = codec)
        }
    }

    private fun setLogs(log: String) = _state.update { copy(logs = log) }
    private fun setStatus(status: HomeStatus) = _state.update { copy(status = status) }
    private fun setOutputFile(outputFile: String) = _state.update { copy(outputFile = outputFile) }
    private fun setChannels(channels: Channels?) = _state.update { copy(channels = channels) }
    private fun setVbr(vbr: Int?) = _state.update { copy(vbr = vbr) }
    private fun setBitrate(bitrate: Bitrate) = _state.update { copy(bitrate = bitrate) }
    private fun setSampleRate(sampleRate: SampleRate?) = _state.update { copy(sampleRate = sampleRate) }
    private fun setPreset(preset: String) = _state.update { copy(preset = preset) }
    private fun setCrf(crf: Int) = _state.update { copy(crf = crf) }
    private fun setResolution(resolution: Resolution?) = _state.update { copy(resolution = resolution) }
    private fun setPixelFormat(pixelFormat: PixelFormat?) = _state.update { copy(pixelFormat = pixelFormat) }
    private fun setNoAudio(noAudio: Boolean) = _state.update { copy(noAudio = noAudio) }
}