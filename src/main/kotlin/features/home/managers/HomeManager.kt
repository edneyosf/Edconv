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
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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
            is HomeEvent.OnStart -> startConversion(overwrite)
            is HomeEvent.OnStop -> stopConversion()
        }
    }

    private fun startConversion(overwrite: Boolean) {
        val input = state.value.inputFile
        val output = state.value.outputFile
        val codec = state.value.codec

        if(input != null && codec != null && output != null) {
            try {
                val outputFile = File(output)
                val outputExists = outputFile.exists()

                if(!overwrite && outputExists) {
                    setStatus(HomeStatus.FileExists)
                    return
                }

                setLogs("MediaInfo = { $input }\n")

                when(codec.mediaType) {
                    MediaType.AUDIO -> convertToAudio(input, output, codec)
                    MediaType.VIDEO -> convertToVideo(input, output, codec)
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
                onError(e)
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

    private fun convertToAudio(inputFile: MediaData, output: String, codec: Codec) = _state.value.run {
        val outputFile = File(output)
        val ffmpeg = FFmpeg.createAudio(
            source = Configs.ffmpegPath,
            logLevel = Configs.ffmpegLogLevel,
            input = inputFile.path,
            output = output,
            codec = codec.value,
            sampleRate = sampleRate?.value,
            channels = channels?.value,
            filter = channels?.downmixingFilter
        )

        vbr?.let { ffmpeg.vbr = it.toString() }
        bitrate?.let { ffmpeg.bitRate = bitrate.value }

        println("FFmpeg: ${ffmpeg.build().joinToString(" ")}")

        conversion = converter.run(ffmpeg, outputFile)
    }

    private fun convertToVideo(inputFile: MediaData, output: String, codec: Codec) = _state.value.run {
        val outputFile = File(output)
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
                output = output,
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

        println("FFmpeg: ${ffmpeg.build().joinToString(" ")}")

        conversion = converter.run(ffmpeg, outputFile)
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

        setStatus(HomeStatus.Progress(percentage, it.speed))
    }

    private fun onStop() {
        val finishTime = Instant.now()
        val systemZone = ZoneId.systemDefault()
        val start = startTime?.atZone(systemZone)?.toLocalTime()
        val finish = finishTime?.atZone(systemZone)?.toLocalTime()
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val startText = start?.format(formatter)
        val finishText = finish?.format(formatter)
        val duration = Duration.between(startTime, finishTime)
        val horas = (duration.toHours() % 24).toString().padStart(2, '0')
        val minutos = (duration.toMinutes() % 60).toString().padStart(2, '0')
        val segundos = (duration.seconds % 60).toString().padStart(2, '0')

        if(_state.value.status !is HomeStatus.Error) {
            setStatus(HomeStatus.Complete(startText ?: "", finishText ?: "", "$horas:$minutos:$segundos"))
        }
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
            val duration: Long?

            when (type) {
                MediaType.AUDIO -> {
                    val channels = MediaUtils.getAudioChannels(inputFile)
                    duration = MediaUtils.getDuration(inputFile)

                    if(duration != null && channels != null) {
                        val media = MediaData(
                            path = path,
                            type = type,
                            duration = duration,
                            channels = channels,
                            size = size
                        )

                        println("MediaInfo: $media")

                        withContext(context = Dispatchers.Main) {
                            _state.value = _state.value.copy(inputFile = media, outputFile = output)
                        }
                    }
                    else {
                        onError(Throwable("Could not retrieve the audio duration or channels"))
                    }
                }
                MediaType.VIDEO -> {
                    val resolution = MediaUtils.getVideoResolution(inputFile)
                    duration = MediaUtils.getDuration(inputFile)

                    if(duration != null && resolution != null) {
                        val media = MediaData(
                            path = path,
                            type = type,
                            duration = duration,
                            resolution = resolution,
                            size = size
                        )

                        println("MediaInfo: $media")

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