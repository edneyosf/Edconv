package edneyosf.edconv.features.converter.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edneyosf.edconv.app.AppConfigs
import edneyosf.edconv.core.ConfigManager
import edneyosf.edconv.core.common.DateTimePattern
import edneyosf.edconv.core.common.Error
import edneyosf.edconv.core.extensions.normalizeCommand
import edneyosf.edconv.core.extensions.notifyMain
import edneyosf.edconv.core.extensions.toReadableCommand
import edneyosf.edconv.core.extensions.update
import edneyosf.edconv.core.utils.DateTimeUtils
import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.ffmpeg.common.*
import edneyosf.edconv.ffmpeg.converter.Converter
import edneyosf.edconv.ffmpeg.data.ProgressData
import edneyosf.edconv.ffmpeg.ffmpeg.FFmpeg
import edneyosf.edconv.features.converter.events.ConverterEvent
import edneyosf.edconv.features.converter.states.ConverterDialogState
import edneyosf.edconv.features.converter.states.ConverterState
import edneyosf.edconv.features.converter.states.ConverterStatusState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import java.io.File
import java.time.Instant

class ConverterViewModel(input: InputMedia, type: MediaType) : ViewModel(), ConverterEvent {

    private var startTime: Instant? = null
    private var conversion: Job? = null
    private val converter: Converter

    private val _state = MutableStateFlow(value = ConverterState(input = input, type = type))
    val state: StateFlow<ConverterState> = _state

    private val codecFlow: Flow<Codec?> = state
        .map { it.codec }
        .distinctUntilChanged()
        .drop(count = 1)

    init {
        converter = Converter(
            scope = viewModelScope,
            onStart = ::onStart,
            onStdout = ::onStdout,
            onError = ::onError,
            onProgress = ::onProgress,
            onStop = ::onStop
        )
        observeCodec()
    }

    private fun observeCodec() {
        viewModelScope.launch {
            codecFlow.collectLatest {
                _state.updateAndSync {
                    copy(
                        bitrate = it?.defaultBitrate,
                        vbr = it?.defaultVBR,
                        crf = it?.defaultCRF,
                        preset = it?.defaultPreset,
                        compression = it?.compressions?.firstOrNull()
                    )
                }
            }
        }
    }

    fun refresh(newInput: InputMedia, newType: MediaType) = _state.updateAndSync {
        val video = newInput.videos.firstOrNull()
        val audio = newInput.audios.firstOrNull()

        val resolvedResolution = resolution
            ?: Resolution.fromValues(width = video?.width, height = video?.height)

        val resolvedPixelFormat = pixelFormat
            ?: PixelFormat.fromValue(value = video?.bitDepth)
            ?: PixelFormat.fromValue(value = video?.pixFmt)

        val resolvedChannels = channels
            ?: Channels.fromValue(value = audio?.channels)

        val resolvedSampleRate = sampleRate
            ?: SampleRate.fromValue(value = audio?.sampleRate)

        val defaultCodec = when (newType) {
            MediaType.AUDIO -> Codec.OPUS
            MediaType.VIDEO -> Codec.AV1
            MediaType.SUBTITLE -> null
        }

        val resolvedCodec = codec.takeIf { type == newType } ?: defaultCodec

        copy(
            input = newInput,
            type = newType,
            codec = resolvedCodec,
            resolution = resolvedResolution,
            pixelFormat = resolvedPixelFormat,
            channels = resolvedChannels,
            sampleRate = resolvedSampleRate
        )
    }

    private fun buildCommand() {
        val state = _state.value
        val codec = state.codec

        if (codec == null || state.output.isNullOrBlank()) {
            setCommand("")
            return
        }

        state.run {
            val ffmpeg = when (type) {
                MediaType.AUDIO -> {
                    val stream = input.audios.firstOrNull()

                    if(stream != null) {
                        val inputChannels = stream.channels
                        val filter = channels?.downmixingFilter(sourceChannels = inputChannels)
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
                    else return@run
                }

                MediaType.VIDEO -> {
                    if(preset.isNullOrBlank() || (crf == null && bitrate == null)) return@run

                    val stream = input.videos.firstOrNull()

                    if(stream != null) {
                        val width = stream.width
                        val height = stream.height
                        val filter = resolution
                            ?.preserveAspectRatioFilter(sourceWidth = width, sourceHeight = height)

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
                    else return@run
                }

                MediaType.SUBTITLE -> return@run
            }

            setCommand(ffmpeg.build().toReadableCommand())
        }
    }

    override fun start(overwrite: Boolean) = _state.value.run {
        if(!output.isNullOrBlank() && command.isNotBlank()) {
            try {
                val inputFile = File(input.path)
                val outputFile = File(output)
                val outputExists = outputFile.exists()

                if(!overwrite && outputExists) {
                    setStatus(ConverterStatusState.FileExists)
                    return
                }

                conversion = converter.run(
                    source = ConfigManager.getFFmpegPath(),
                    inputFile = inputFile,
                    cmd = command.normalizeCommand(),
                    outputFile = outputFile
                )
            }
            catch (e: Exception) {
                e.printStackTrace()
                onError(Error.ON_STARTING_CONVERSION)
            }
        }
        else {
            onError(Error.ON_STARTING_CONVERSION_REQUIREMENTS)
        }
    }

    override fun stop() {
        viewModelScope.launch(context = Dispatchers.IO) {
            try {
                converter.destroyProcess()
                conversion?.cancelAndJoin()
                conversion = null
                notifyMain { setStatus(ConverterStatusState.Initial) }
            }
            catch (e: Exception) {
                e.printStackTrace()
                notifyMain { onError(Error.ON_STOPPING_CONVERSION) }
            }
        }
    }

    private fun onStart() {
        startTime = Instant.now()
        _state.update {
            copy(
                status = ConverterStatusState.Loading,
                logs = input.toString() + "\n"
            )
        }
    }

    private fun onStdout(it: String) = setLogs(_state.value.logs + "$it\n")

    private fun onError(error: Error) = setStatus(ConverterStatusState.Failure(error = error))

    private fun onProgress(it: ProgressData) {
        val duration = _state.value.input.duration
        val percentage = if(duration > 0) ((it.time * 100.0f) / duration) else 0.0f

        setStatus(ConverterStatusState.Progress(percentage, speed = it.speed))
    }

    private fun onStop() {
        startTime?.let {
            val finishTime = Instant.now()
            val startText = DateTimeUtils.instantToText(instant = it, pattern = DateTimePattern.TIME_HMS)
            val finishText = DateTimeUtils.instantToText(instant = finishTime, pattern = DateTimePattern.TIME_HMS)
            val duration = DateTimeUtils.durationText(it, finishTime)

            startTime = null

            if(_state.value.status !is ConverterStatusState.Failure) {
                setStatus(ConverterStatusState.Complete(
                    startTime = startText,
                    finishTime = finishText,
                    duration = duration
                ))
            }
        } ?: run {
            onError(Error.START_TIME_NULL)
        }
    }

    override fun setCodec(codec: Codec?) {
        val inputPath = _state.value.input.path
        var output: String? = null

        if(codec != null) {
            val outputName = File(inputPath).nameWithoutExtension
            val outputExtension = codec.toFileExtension()

            output = "${AppConfigs.outputDefault}$outputName.$outputExtension"
        }

        _state.updateAndSync { copy(codec = codec, output = output) }
    }

    override fun setCommand(cmd: String) = _state.update { copy(command = cmd) }

    override fun setStatus(status: ConverterStatusState) = _state.update { copy(status = status) }

    override fun setDialog(dialog: ConverterDialogState) = _state.update { copy(dialog = dialog) }

    override fun setOutput(path: String) = _state.update { copy(output = path) }

    private fun setLogs(log: String) = _state.update { copy(logs = log) }

    override fun setCompression(type: CompressionType?) = _state.updateAndSync { copy(compression = type) }

    override fun setChannels(channels: Channels?) = _state.updateAndSync { copy(channels = channels) }

    override fun setVbr(vbr: Int?) = _state.updateAndSync { copy(vbr = vbr) }

    override fun setBitrate(bitrate: Bitrate?) = _state.updateAndSync { copy(bitrate = bitrate) }

    override fun setSampleRate(sampleRate: SampleRate?) = _state.updateAndSync { copy(sampleRate = sampleRate) }

    override fun setPreset(preset: String?) = _state.updateAndSync { copy(preset = preset) }

    override fun setCrf(crf: Int?) = _state.updateAndSync { copy(crf = crf) }

    override fun setResolution(resolution: Resolution?) = _state.updateAndSync { copy(resolution = resolution) }

    override fun setPixelFormat(pixelFormat: PixelFormat?) = _state.updateAndSync { copy(pixelFormat = pixelFormat) }

    override fun setNoAudio(noAudio: Boolean) = _state.updateAndSync { copy(noAudio = noAudio) }

    override fun setNoSubtitle(noSubtitle: Boolean) = _state.updateAndSync { copy(noSubtitle = noSubtitle) }

    override fun setNoMetadata(noMetadata: Boolean) = _state.updateAndSync { copy(noMetadata = noMetadata) }

    private inline fun <T> MutableStateFlow<T>.updateAndSync(block: T.() -> T) {
        value = value.block()
        buildCommand()
    }
}