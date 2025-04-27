package edneyosf.edconv.features.converter.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edneyosf.edconv.app.AppConfigs
import edneyosf.edconv.core.ConfigManager
import edneyosf.edconv.core.common.DateTimePattern
import edneyosf.edconv.core.extensions.notifyMain
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
import java.io.File
import java.time.Instant

class ConverterViewModel: ViewModel(), ConverterEvent {

    lateinit var input: InputMedia
    lateinit var mediaType: MediaType

    private var startTime: Instant? = null
    private var conversion: Job? = null
    private val converter: Converter

    private val _state = mutableStateOf(value = ConverterState())
    val state: State<ConverterState> = _state

    init {
        converter = Converter(
            scope = viewModelScope,
            onStart = ::onStart,
            onStdout = ::onStdout,
            onError = ::onError,
            onProgress = ::onProgress,
            onStop = ::onStop
        )
    }

    /*private fun defaultState() = defaultState.run {
        val steam = input.videos.firstOrNull()
        val defaultResolution = Resolution.entries.find { it.width == steam?.width } ?: Resolution.entries.find { it.height == steam?.height }
        val defaultCodec = when(mediaType) {
            MediaType.AUDIO -> Codec.OPUS
            MediaType.VIDEO -> Codec.AV1
            MediaType.SUBTITLE -> null
        }.takeIf { codec == null }

        copy(
            codec = defaultCodec,
            resolution = defaultResolution
        )
    }*/

    private fun buildCommand() = _state.value.run {
        if (input.path.isBlank() || codec == null || output.isNullOrBlank()) {
            setCmd("")
            return Unit
        }

        val ffmpeg = when (codec.mediaType) {
            MediaType.AUDIO -> {
                val stream = input.audios.firstOrNull()

                stream?.let {
                    val inputChannels = stream.channels

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
            }
            MediaType.VIDEO -> {
                if(preset.isNullOrBlank() || (crf == null && bitrate == null)) return@run

                val stream = input.videos.firstOrNull()

                stream?.let {
                    val width = stream.width
                    val height = stream.height
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
            else -> null
        }
        val cmd = ffmpeg?.build()

        cmd?.let {
            val regex = Regex(" (?=-[a-zA-Z])")
            val command = cmd.replace(regex, "\n")

            setCmd(command)
        }
    }

    override fun start(overwrite: Boolean) = _state.value.run {
        val inputPath = input.path

        if(!inputPath.isBlank() && !output.isNullOrBlank() && cmd.isNotBlank()) {
            try {
                val outputFile = File(output)
                val outputExists = outputFile.exists()

                if(!overwrite && outputExists) {
                    setStatus(ConverterStatusState.FileExists)
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

    override fun stop() {
        viewModelScope.launch(context = Dispatchers.IO) {
            try {
                notifyMain { setStatus(ConverterStatusState.Loading) }
                converter.destroyProcess()
                conversion?.cancelAndJoin()
                conversion = null
                notifyMain { setStatus(ConverterStatusState.Initial) }
            }
            catch (e: Exception) {
                e.printStackTrace()
                notifyMain { onError(e) }
            }
        }
    }

    private fun onStart() {
        startTime = Instant.now()
        setStatus(ConverterStatusState.Loading)
        setLogs(input.toString() + "\n")
    }

    private fun onStdout(it: String) = setLogs(_state.value.logs + "$it\n")

    private fun onError(it: Throwable) = setStatus(ConverterStatusState.Error(it.message))

    private fun onProgress(it: ProgressData) {
        val duration = input.duration
        val percentage = if(duration > 0) ((it.time * 100.0f) / duration) else 0.0f

        setStatus(ConverterStatusState.Progress(percentage, it.speed))
    }

    private fun onStop() {
        startTime?.let {
            val finishTime = Instant.now()
            val startText = DateTimeUtils.instantToText(instant = it, DateTimePattern.TIME_HMS)
            val finishText = DateTimeUtils.instantToText(instant = finishTime, DateTimePattern.TIME_HMS)
            val duration = DateTimeUtils.durationText(it, finishTime)

            startTime = null

            if(_state.value.status !is ConverterStatusState.Error) {
                setStatus(ConverterStatusState.Complete(startText, finishText, duration))
            }
        } ?: run {
            onError(Throwable("Start time is null"))
        }
    }

    override fun setCodec(codec: Codec?) {
        val inputPath = input.path
        var output: String? = null

        if(!inputPath.isBlank() && codec != null) {
            val outputName = File(inputPath).nameWithoutExtension
            val outputExtension = codec.toFileExtension()

            output = "${AppConfigs.outputDefault}$outputName.$outputExtension"
        }

        _state.updateAndSync { copy(codec = codec, output = output) }
    }

    override fun setCmd(cmd: String) = _state.update { copy(cmd = cmd) }

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

    private inline fun <T> MutableState<T>.updateAndSync(block: T.() -> T) {
        value = value.block()
        buildCommand()
    }
}