package edneyosf.edconv.features.converter.managers

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import edneyosf.edconv.app.AppConfigs
import edneyosf.edconv.core.ConfigManager
import edneyosf.edconv.core.common.DateTimePattern
import edneyosf.edconv.core.common.Manager
import edneyosf.edconv.core.extensions.update
import edneyosf.edconv.core.utils.DateTimeUtils
import edneyosf.edconv.ffmpeg.common.*
import edneyosf.edconv.ffmpeg.converter.Converter
import edneyosf.edconv.ffmpeg.data.ProgressData
import edneyosf.edconv.ffmpeg.ffmpeg.FFmpeg
import edneyosf.edconv.features.converter.events.ConverterEvent
import edneyosf.edconv.features.converter.states.ConverterDialog
import edneyosf.edconv.features.converter.states.ConverterState
import edneyosf.edconv.features.converter.states.ConverterStatus
import kotlinx.coroutines.*
import java.io.File
import java.time.Instant

class ConverterManager(val defaultState: ConverterState, override val scope: CoroutineScope): Manager(scope) {

    private var startTime: Instant? = null
    private var conversion: Job? = null
    private val converter: Converter

    private val _state = mutableStateOf(value = teste())
    val state: State<ConverterState> = _state

    init {
        converter = Converter(
            scope = scope,
            onStart = ::onStart,
            onStdout = ::onStdout,
            onError = ::onError,
            onProgress = ::onProgress,
            onStop = ::onStop
        )
    }

    private fun teste(): ConverterState {
        val steam = defaultState.input.videoStreams.firstOrNull()
        val resolution = Resolution.entries.find { it.width == steam?.width } ?: Resolution.entries.find { it.height == steam?.height }

        return defaultState.copy(resolution = resolution)
    }

    fun onEvent(event: ConverterEvent) = event.run {
        when(this) {
            is ConverterEvent.SetStatus -> setStatus(status)
            is ConverterEvent.SetDialog -> setDialog(dialog)
            is ConverterEvent.SetCmd -> setCmd(cmd)
            //is ConverterEvent.SetInput -> setInput(inputMedia)
            is ConverterEvent.SetOutput -> setOutput(path)
            is ConverterEvent.SetCompression -> setCompression(compression)
            is ConverterEvent.SetCodec -> setCodec(codec)
            is ConverterEvent.SetChannels -> setChannels(channels)
            is ConverterEvent.SetVbr -> setVbr(vbr)
            is ConverterEvent.SetBitrate -> setBitrate(bitrate)
            is ConverterEvent.SetSampleRate -> setSampleRate(sampleRate)
            is ConverterEvent.SetPreset -> setPreset(preset)
            is ConverterEvent.SetCrf -> setCrf(crf)
            is ConverterEvent.SetResolution -> setResolution(resolution)
            is ConverterEvent.SetPixelFormat -> setPixelFormat(pixelFormat)
            is ConverterEvent.SetNoAudio -> setNoAudio(noAudio)
            is ConverterEvent.SetNoSubtitle -> setNoSubtitle(noSubtitle)
            is ConverterEvent.SetNoMetadata -> setNoMetadata(noMetadata)
            is ConverterEvent.OnStart -> startConversion(overwrite)
            is ConverterEvent.OnStop -> stopConversion()
        }

        buildCommand(event = this)
    }

    private fun buildCommand(event: ConverterEvent? = null) = _state.value.run {
        val canBuild = when (event) {
            is ConverterEvent.SetCmd,
            is ConverterEvent.OnStart,
            is ConverterEvent.OnStop,
            //is ConverterEvent.SetInput,
            is ConverterEvent.SetOutput,
            is ConverterEvent.SetDialog,
            is ConverterEvent.SetStatus -> false
            else -> true
        }

        if (!canBuild) return

        if (input.path.isBlank() || codec == null || output.isNullOrBlank()) {
            setCmd("")
            return
        }

        val ffmpeg = when (codec.mediaType) {
            MediaType.AUDIO -> {
                val stream = input.audioStreams.firstOrNull()
                val inputChannels = stream?.channels

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

                val stream = input.videoStreams.firstOrNull()
                val width = stream?.width
                val height = stream?.height

                if(width == null || height == null) {
                    onError(Throwable("Input resolution is null"))
                    return@run
                }

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
        val cmd = ffmpeg.build()
        val regex = Regex(" (?=-[a-zA-Z])")
        val command = cmd.replace(regex, "\n")

        setCmd(command)
    }

    private fun startConversion(overwrite: Boolean) = _state.value.run {
        val inputPath = input.path

        if(!inputPath.isBlank() && !output.isNullOrBlank() && cmd.isNotBlank()) {
            try {
                val outputFile = File(output)
                val outputExists = outputFile.exists()

                if(!overwrite && outputExists) {
                    setStatus(ConverterStatus.FileExists)
                    return
                }

                val command = cmd
                    .replace("\n", " ")
                    .replace(Regex("\\s+"), " ")
                    .trim()

                conversion = converter.run(
                    source = ConfigManager.getFFmpegPath(),
                    inputFile = File(inputPath),
                    cmd = command,
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
            notifyMain { setStatus(ConverterStatus.Loading) }
            converter.destroyProcess()
            conversion?.cancelAndJoin()
            conversion = null
            notifyMain { setStatus(ConverterStatus.Initial) }
        }
        catch (e: Exception) {
            e.printStackTrace()
            notifyMain { onError(e) }
        }
    }

    private fun onStart() {
        startTime = Instant.now()
        setStatus(ConverterStatus.Loading)
        setLogs(_state.value.input.toString() + "\n")
    }

    private fun onStdout(it: String) = setLogs(_state.value.logs + "$it\n")

    private fun onError(it: Throwable) = setStatus(ConverterStatus.Error(it.message))

    private fun onProgress(it: ProgressData) {
        val inputFile = _state.value.input
        val duration = inputFile.duration ?: 0
        val percentage = if(duration > 0) ((it.time * 100.0f) / duration) else 0.0f

        setStatus(ConverterStatus.Progress(percentage, it.speed))
    }

    private fun onStop() {
        startTime?.let {
            val finishTime = Instant.now()
            val startText = DateTimeUtils.instantToText(instant = it, DateTimePattern.TIME_HMS)
            val finishText = DateTimeUtils.instantToText(instant = finishTime, DateTimePattern.TIME_HMS)
            val duration = DateTimeUtils.durationText(it, finishTime)

            startTime = null

            if(_state.value.status !is ConverterStatus.Error) {
                setStatus(ConverterStatus.Complete(startText, finishText, duration))
            }
        } ?: run {
            onError(Throwable("Start time is null"))
        }
    }

    /*private fun setInput(inputMedia: InputMedia) {
        setStatus(ConverterStatus.Loading)
        val inputFile = File(inputMedia.path)
        val outputFileName = inputFile.nameWithoutExtension
        val codec = _state.value.codec
        val extension = codec?.toFileExtension() ?: inputFile.extension
        val output = "${AppConfigs.outputDefault}$outputFileName.$extension"

        setLogs("")
        print("Nao e possivekl")

        val newState = when (inputMedia.type) {
            MediaType.AUDIO -> {
                val stream = inputMedia.audioStreams.firstOrNull()
                val duration = inputMedia.duration
                val channels = stream?.channels

                if(duration != null && channels != null) {
                    _state.value.copy(
                        input = inputMedia,
                        output = output,
                        codec = if(inputMedia.type != codec?.mediaType) null else codec,
                        status = ConverterStatus.Initial
                    )
                }
                else {
                    _state.value.copy(
                        //input = null, TODO
                        output = null,
                        codec = null,
                        status = ConverterStatus.Error("Could not retrieve the audio duration or channels")
                    )
                }
            }
            MediaType.VIDEO -> {
                val stream = inputMedia.videoStreams.firstOrNull()
                val duration = inputMedia.duration
                val height = stream?.height
                val width = stream?.width

                if(duration != null && width != null && height != null) {
                    print("width: "+width)
                    print("height: "+height)
                    val resolution = Resolution.entries.find { it.width == width } ?: Resolution.entries.find { it.height == height }

                    _state.value.copy(
                        input = inputMedia,
                        output = output,
                        codec = if(inputMedia.type != codec?.mediaType) null else codec,
                        status = ConverterStatus.Initial,
                        resolution = resolution
                    )
                }
                else {
                    _state.value.copy(
                        //input = null, TODO
                        output = null,
                        codec = null,
                        status = ConverterStatus.Error("Could not retrieve the video duration or resolution")
                    )
                }
            }
        }

        _state.value = newState
        buildCommand()
    }*/

    private fun setCodec(codec: Codec?) {
        val inputPath = _state.value.input.path

        if(!inputPath.isBlank() && codec != null) {
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
    private fun setStatus(status: ConverterStatus) = _state.update { copy(status = status) }
    private fun setDialog(dialog: ConverterDialog) = _state.update { copy(dialog = dialog) }
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