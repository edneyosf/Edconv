package edneyosf.edconv.features.home.managers

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import edneyosf.edconv.app.AppConfigs
import edneyosf.edconv.core.ConfigManager
import edneyosf.edconv.core.common.DateTimePattern
import edneyosf.edconv.core.common.Manager
import edneyosf.edconv.core.extensions.update
import edneyosf.edconv.core.utils.DateTimeUtils
import edneyosf.edconv.edconv.utils.MediaUtils
import edneyosf.edconv.edconv.common.*
import edneyosf.edconv.edconv.core.Edconv
import edneyosf.edconv.edconv.core.data.MediaData
import edneyosf.edconv.edconv.core.data.ProgressData
import edneyosf.edconv.edconv.ffmpeg.FFmpeg
import edneyosf.edconv.features.home.events.HomeEvent
import edneyosf.edconv.features.home.states.HomeState
import edneyosf.edconv.features.home.states.HomeStatus
import kotlinx.coroutines.*
import java.io.File
import java.time.Instant

class HomeManager(override val scope: CoroutineScope): Manager(scope) {

    private var startTime: Instant? = null
    private var conversion: Job? = null
    private val converter: Edconv

    private val _state = mutableStateOf(HomeState.default())
    val state: State<HomeState> = _state

    init {
        loadConfigs()
        converter = Edconv(
            scope = scope,
            onStart = ::onStart,
            onStdout = ::onStdout,
            onError = ::onError,
            onProgress = ::onProgress,
            onStop = ::onStop
        )
    }

    fun onEvent(event: HomeEvent) = event.run {
        setNoConfigStatusIfNecessary()

        when(this) {
            is HomeEvent.SetStatus -> setStatus(status)
            is HomeEvent.SetCmd -> setCmd(cmd)
            is HomeEvent.SetInput -> setInput(path)
            is HomeEvent.SetOutput -> setOutput(path)
            is HomeEvent.SetCompression -> setCompression(compression)
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
            is HomeEvent.SetNoSubtitle -> setNoSubtitle(noSubtitle)
            is HomeEvent.SetNoMetadata -> setNoMetadata(noMetadata)
            is HomeEvent.OnStart -> startConversion(overwrite)
            is HomeEvent.OnStop -> stopConversion()
        }

        buildCommand(event = this)
    }

    private fun setNoConfigStatusIfNecessary() {
        val ffmpegPath = ConfigManager.getFFmpegPath()
        val ffprobePath = ConfigManager.getFFprobePath()

        if (ffmpegPath.isBlank() || ffprobePath.isBlank()) {
            setStatus(HomeStatus.Settings)
            return
        }

        val ffmpegFile = File(ffmpegPath)
        val ffprobeFile = File(ffprobePath)

        if (!ffmpegFile.exists() || !ffmpegFile.isFile ||
            !ffprobeFile.exists() || !ffprobeFile.isFile) {
            setStatus(HomeStatus.Settings)
        }
    }

    private fun loadConfigs() {
        try {
            ConfigManager.load(AppConfigs.NAME)
            setNoConfigStatusIfNecessary()
        }
        catch (e: Exception) {
            e.printStackTrace()
            onError(e)
        }
    }

    private fun buildCommand(event: HomeEvent? = null) = _state.value.run {
        val canBuild = when (event) {
            is HomeEvent.SetCmd,
            is HomeEvent.OnStart,
            is HomeEvent.OnStop,
            is HomeEvent.SetInput,
            is HomeEvent.SetStatus -> false
            else -> true
        }

        if (!canBuild) return

        if (input == null || input.path.isBlank() || codec == null || output.isNullOrBlank()) {
            setCmd("")
            return
        }

        val ffmpeg = when (codec.mediaType) {
            MediaType.AUDIO -> {
                val inputChannels = input.channels

                if(inputChannels == null) {
                    onError(Throwable("Input channels is null"))
                    return@run
                }

                val filter = channels?.downmixingFilter(inputChannels)
                val customChannelsArgs = Channels.customArguments(
                    channels = channels?.value?.toInt(),
                    inputChannels = inputChannels,
                    codec = codec
                )

                FFmpeg.createAudio(
                    logLevel = AppConfigs.FFMPEG_LOG_LEVEL,
                    codec = codec.value,
                    compressionType = compression,
                    sampleRate = sampleRate?.value,
                    channels = channels?.value,
                    vbr = vbr?.toString(),
                    bitrate = bitrate?.value,
                    filter = filter,
                    noMetadata = noMetadata,
                    custom = customChannelsArgs
                )
            }
            MediaType.VIDEO -> {
                if(preset.isNullOrBlank() || (crf == null && bitrate == null)) return@run

                val inputResolution = input.resolution
                if(inputResolution == null) {
                    onError(Throwable("Input resolution is null"))
                    return@run
                }

                val width = inputResolution.first
                val height = inputResolution.second
                val filter = resolution?.preserveAspectRatioFilter(sourceWidth = width, sourceHeight = height)

                FFmpeg.createVideo(
                    logLevel = AppConfigs.FFMPEG_LOG_LEVEL,
                    codec = codec.value,
                    compressionType = compression,
                    preset = preset,
                    crf = crf,
                    bitrate = bitrate?.value,
                    profile = codec.getVideoProfile(pixelFormat),
                    pixelFormat = pixelFormat?.value,
                    filter = filter,
                    noAudio = noAudio,
                    noSubtitle = noSubtitle,
                    noMetadata = noMetadata
                )
            }
        }

        setCmd(ffmpeg.build())
    }

    private fun startConversion(overwrite: Boolean) = _state.value.run {
        val inputPath = input?.path

        if(!inputPath.isNullOrBlank() && !output.isNullOrBlank() && cmd.isNotBlank()) {
            try {
                val outputFile = File(output)
                val outputExists = outputFile.exists()

                if(!overwrite && outputExists) {
                    setStatus(HomeStatus.FileExists)
                    return
                }

                conversion = converter.run(
                    source = ConfigManager.getFFmpegPath(),
                    inputFile = File(inputPath),
                    cmd = cmd,
                    outputFile = outputFile
                )
            }
            catch (e: Exception) {
                e.printStackTrace()
                onError(e)
            }
        }
        else {
            onError(Throwable("Input file, command or output file is invalid"))
        }
    }

    private fun stopConversion() = scope.launch(context = Dispatchers.IO) {
        try {
            notifyMain { setStatus(HomeStatus.Loading) }
            converter.destroyProcess()
            conversion?.cancelAndJoin()
            conversion = null
            notifyMain { setStatus(HomeStatus.Initial) }
        }
        catch (e: Exception) {
            e.printStackTrace()
            notifyMain { onError(e) }
        }
    }

    private fun onStart() {
        startTime = Instant.now()
        setStatus(HomeStatus.Loading)
        setLogs(_state.value.input.toString() + "\n")
    }

    private fun onStdout(it: String) = setLogs(_state.value.logs + "$it\n")

    private fun onError(it: Throwable) = setStatus(HomeStatus.Error(it.message))

    private fun onProgress(it: ProgressData) {
        val inputFile = _state.value.input
        val duration = inputFile?.duration ?: 0
        val percentage = if(duration > 0) ((it.time * 100.0f) / duration) else 0.0f

        setStatus(HomeStatus.Progress(percentage, it.speed))
    }

    private fun onStop() {
        startTime?.let {
            val finishTime = Instant.now()
            val startText = DateTimeUtils.instantToText(instant = it, DateTimePattern.TIME_HMS)
            val finishText = DateTimeUtils.instantToText(instant = finishTime, DateTimePattern.TIME_HMS)
            val duration = DateTimeUtils.durationText(it, finishTime)

            startTime = null

            if(_state.value.status !is HomeStatus.Error) {
                setStatus(HomeStatus.Complete(startText, finishText, duration))
            }
        } ?: run {
            onError(Throwable("Start time is null"))
        }
    }

    private fun setInput(path: String) {
        setStatus(HomeStatus.Loading)
        val inputFile = File(path)
        val outputFileName = inputFile.nameWithoutExtension
        val codec = _state.value.codec
        val extension = codec?.toFileExtension() ?: inputFile.extension
        val output = "${AppConfigs.outputDefault}$outputFileName.$extension"

        setLogs("")

        scope.launch(context = Dispatchers.IO) {
            val contentType = MediaUtils.getContentType(inputFile)
            val duration: Long?
            val channels: Int?
            val size = MediaUtils.getSize(inputFile)
            val type = when {
                contentType.audio && !contentType.video -> MediaType.AUDIO
                contentType.video -> MediaType.VIDEO
                else -> null
            }

            val newState = when (type) {
                MediaType.AUDIO -> {
                    channels = MediaUtils.getAudioChannels(inputFile)
                    duration = MediaUtils.getDuration(inputFile)

                    if(duration != null && channels != null) {
                        val media = MediaData(
                            path = path,
                            type = type,
                            contentType = contentType,
                            duration = duration,
                            channels = channels,
                            size = size
                        )

                        _state.value.copy(
                            input = media,
                            output = output,
                            status = HomeStatus.Initial
                        )
                    }
                    else {
                        _state.value.copy(
                            input = null,
                            output = null,
                            status = HomeStatus.Error("Could not retrieve the audio duration or channels")
                        )
                    }
                }
                MediaType.VIDEO -> {
                    val resolution = MediaUtils.getVideoResolution(inputFile)
                    channels = MediaUtils.getAudioChannels(inputFile)
                    duration = MediaUtils.getDuration(inputFile)

                    if(duration != null && resolution != null) {
                        val media = MediaData(
                            path = path,
                            type = type,
                            contentType = contentType,
                            duration = duration,
                            channels = channels,
                            resolution = resolution,
                            size = size
                        )

                        _state.value.copy(
                            input = media,
                            output = output,
                            status = HomeStatus.Initial
                        )
                    }
                    else {
                        _state.value.copy(
                            input = null,
                            output = null,
                            status = HomeStatus.Error("Could not retrieve the video duration or resolution")
                        )
                    }
                }
                else -> {
                    _state.value.copy(
                        input = null,
                        output = null,
                        status = HomeStatus.Error("Could not identify media type")
                    )
                }
            }

            notifyMain {
                _state.value = newState
                buildCommand()
            }
        }
    }

    private fun setCodec(codec: Codec?) {
        val inputPath = _state.value.input?.path

        if(!inputPath.isNullOrBlank() && codec != null) {
            val outputName = File(inputPath).nameWithoutExtension
            val outputExtension = codec.toFileExtension()
            val output = "${AppConfigs.outputDefault}$outputName.$outputExtension"

            _state.update { copy(codec = codec, output = output) }
        }
        else {
            _state.update { copy(codec = codec) }
        }
    }

    private fun setCmd(cmd: String) = _state.update { copy(cmd = cmd) }
    private fun setStatus(status: HomeStatus) = _state.update { copy(status = status) }
    private fun setOutput(path: String) = _state.update { copy(output = path) }
    private fun setLogs(log: String) = _state.update { copy(logs = log) }
    private fun setCompression(type: CompressionType?) = _state.update { copy(compression = type) }
    private fun setChannels(channels: Channels?) = _state.update { copy(channels = channels) }
    private fun setVbr(vbr: Int?) = _state.update { copy(vbr = vbr) }
    private fun setBitrate(bitrate: Bitrate?) = _state.update { copy(bitrate = bitrate) }
    private fun setSampleRate(sampleRate: SampleRate?) = _state.update { copy(sampleRate = sampleRate) }
    private fun setPreset(preset: String?) = _state.update { copy(preset = preset) }
    private fun setCrf(crf: Int?) = _state.update { copy(crf = crf) }
    private fun setResolution(resolution: Resolution?) = _state.update { copy(resolution = resolution) }
    private fun setPixelFormat(pixelFormat: PixelFormat?) = _state.update { copy(pixelFormat = pixelFormat) }
    private fun setNoAudio(noAudio: Boolean) = _state.update { copy(noAudio = noAudio) }
    private fun setNoSubtitle(noSubtitle: Boolean) = _state.update { copy(noSubtitle = noSubtitle) }
    private fun setNoMetadata(noMetadata: Boolean) = _state.update { copy(noMetadata = noMetadata) }
}