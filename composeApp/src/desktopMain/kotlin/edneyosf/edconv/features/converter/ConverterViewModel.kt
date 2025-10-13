package edneyosf.edconv.features.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edneyosf.edconv.app.AppConfigs
import edneyosf.edconv.core.common.Error
import edneyosf.edconv.core.config.EdConfig
import edneyosf.edconv.core.extensions.durationUntil
import edneyosf.edconv.core.extensions.formatTime
import edneyosf.edconv.core.extensions.normalizeCommand
import edneyosf.edconv.core.extensions.notifyMain
import edneyosf.edconv.core.extensions.toReadableCommand
import edneyosf.edconv.core.extensions.update
import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.features.converter.enums.ConverterFileExistsAction as FileExistsAction
import edneyosf.edconv.core.process.MediaQueue
import edneyosf.edconv.core.process.EdProcess
import edneyosf.edconv.core.process.QueueStatus
import edneyosf.edconv.core.utils.DirUtils
import edneyosf.edconv.core.utils.FileUtils
import edneyosf.edconv.features.converter.states.ConverterDialogState
import edneyosf.edconv.features.converter.states.ConverterState
import edneyosf.edconv.features.converter.states.ConverterStatusState
import edneyosf.edconv.ffmpeg.common.Bitrate
import edneyosf.edconv.ffmpeg.common.Channels
import edneyosf.edconv.ffmpeg.common.Codec
import edneyosf.edconv.ffmpeg.common.CompressionType
import edneyosf.edconv.ffmpeg.common.MediaType
import edneyosf.edconv.ffmpeg.common.PixelFormat
import edneyosf.edconv.ffmpeg.common.Resolution
import edneyosf.edconv.ffmpeg.common.SampleRate
import edneyosf.edconv.ffmpeg.converter.Converter
import edneyosf.edconv.ffmpeg.data.ProgressData
import edneyosf.edconv.ffmpeg.ffmpeg.FFmpeg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File
import java.time.Instant
import java.util.UUID

class ConverterViewModel(private val config: EdConfig, private val process: EdProcess) : ViewModel(), ConverterEvent {

    private var conversion: Job? = null
    private var logMonitor: Job? = null

    private val converter: Converter
    private var currentMediaId: String? = null

    private val logsCache = mutableListOf<String>()

    private val _state = MutableStateFlow(value = ConverterState())
    val state: StateFlow<ConverterState> = _state

    val commandState: StateFlow<String> = process.command

    init {
        converter = Converter(
            process = process,
            onStdout = ::onStdout,
            onProgress = ::onProgress
        )
        observeCodec()
        observeInput()
        observeQueue()
    }

    private fun observeInput() {
        viewModelScope.launch {
            combine(
                flow = process.input,
                flow2 = process.inputType,
                transform = ::Pair
            )
            .collectLatest { (input, type) ->
                setInput(newInput = input, newType = type)
            }
        }
    }

    private fun observeCodec() {
        viewModelScope.launch {
            _state.map { it.codec }
            .distinctUntilChanged()
            .collectLatest {
                _state.updateAndSync {
                    val newState = copy(
                        bitrate = it?.defaultBitrate,
                        vbr = it?.defaultVBR,
                        crf = it?.defaultCRF,
                        preset = it?.defaultPreset,
                        compression = it?.compressions?.firstOrNull()
                    )

                    if(input != null) newState.copy(output = it?.toOutput(inputMedia = input))
                    else newState
                }
            }
        }
    }

    fun setInput(newInput: InputMedia?, newType: MediaType?) {
        if(newInput != null && newType != null) {
            _state.updateAndSync {
                val video = newInput.videos.firstOrNull()
                val audio = newInput.audios.firstOrNull()

                val newResolution = resolution
                    ?: Resolution.fromValues(width = video?.width, height = video?.height)

                val newPixelFormat = pixelFormat
                    ?: PixelFormat.fromValue(value = video?.bitDepth)
                    ?: PixelFormat.fromValue(value = video?.pixFmt)

                val newChannels = channels
                    ?: Channels.fromValue(value = audio?.channels)

                val newSampleRate = sampleRate
                    ?: SampleRate.fromValue(value = audio?.sampleRate)

                val defaultCodec = when (newType) {
                    MediaType.AUDIO -> Codec.OPUS
                    MediaType.VIDEO -> Codec.AV1
                    MediaType.SUBTITLE -> null
                }

                val newCodec = codec.takeIf { type == newType } ?: defaultCodec
                val newOutput = newCodec.toOutput(inputMedia = newInput)

                copy(
                    input = newInput,
                    type = newType,
                    codec = newCodec,
                    resolution = newResolution,
                    pixelFormat = newPixelFormat,
                    channels = newChannels,
                    sampleRate = newSampleRate,
                    output = newOutput
                )
            }
        }
    }

    private fun observeQueue() {
        viewModelScope.launch {
            process.queue.collectLatest {
                _state.update { copy(queueSize = it.size) }
            }
        }
    }

    private fun buildCommand() {
        val state = _state.value
        val codec = state.codec

        if (codec == null || state.output?.first.isNullOrBlank() || state.output.second.isBlank()) {
            process.setCommand("")
            return
        }

        state.run {
            val ffmpeg = when (type) {
                MediaType.AUDIO -> {
                    val stream = input?.audios?.firstOrNull()

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

                    val stream = input?.videos?.firstOrNull()

                    if(stream != null) {
                        val width = stream.width
                        val height = stream.height
                        val filter = resolution
                            ?.preserveAspectRatioFilter(sourceWidth = width, sourceHeight = height, hdr10ToSdr)

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

                else -> return@run
            }

            process.setCommand(ffmpeg.build().toReadableCommand())
        }
    }

    override fun addToQueue(fromStart: Boolean, overwrite: Boolean) {
        val state = _state.value
        val input = state.input
        val type = state.type
        val output = state.output
        val command = process.command.value

        if(input != null && type != null && !output?.first.isNullOrBlank() && !state.output.second.isBlank() && command.isNotBlank()) {
            try {
                val outputFile = File("${output.first}${output.second}")
                val outputExists = outputFile.exists()

                if(!overwrite && outputExists) {
                    val action = if(fromStart) FileExistsAction.START else FileExistsAction.ADD_TO_QUEUE

                    setDialog(ConverterDialogState.FileExists(action))
                    return
                }
                else if(overwrite) {
                    setDialog(ConverterDialogState.None)
                }

                if(process.queue.value.any { it.outputFile.path == outputFile.path }){
                    onError(Error.FILE_ALREADY_EXISTS)
                }
                else {
                    process.addToQueue(
                        item = MediaQueue(
                            id = UUID.randomUUID().toString(),
                            input = input,
                            type = type,
                            command = command.normalizeCommand(),
                            outputFile = outputFile
                        )
                    )
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
                onError(Error.ON_ADD_TO_QUEUE)
            }
        }
        else {
            onError(Error.ON_ADD_TO_QUEUE_REQUIREMENTS)
        }
    }

    override fun start(overwrite: Boolean) {
        if(process.queue.value.none { it.status == QueueStatus.NOT_STARTED }) {
            addToQueue(fromStart = true, overwrite = overwrite)
        }
        if(process.queue.value.any { it.status == QueueStatus.NOT_STARTED }) {
            setStatus(ConverterStatusState.Loading)
            conversion = viewModelScope.launch(context = Dispatchers.IO) {
                val startTime = Instant.now()

                notifyMain { process.setConverting(true) }

                while (true) {
                    val item = process.queue.value.firstOrNull { it.status == QueueStatus.NOT_STARTED } ?: break
                    val startTimeItem = Instant.now()

                    currentMediaId = item.id
                    notifyMain { updateCurrentStatus(status = QueueStatus.STARTED) }
                    logsCache.clear()
                    startLogMonitor()

                    val error = converter.run(
                        ffmpeg = config.ffmpegPath,
                        inputFile = File(item.input.path),
                        cmd = item.command,
                        outputFile = item.outputFile
                    )
                    val finishTimeItem = Instant.now()

                    notifyMain {
                        val status = if(error == null) QueueStatus.FINISHED else QueueStatus.ERROR

                        process.updateQueueItemById(id = item.id) {
                            copy(
                                status = status,
                                startTime = startTimeItem.formatTime(),
                                finishTime = finishTimeItem.formatTime(),
                                duration = startTimeItem.durationUntil(end = finishTimeItem),
                                error = error
                            )
                        }
                    }
                }

                currentMediaId = null
                notifyMain { process.setConverting(false) }
                notifyCompletion(startTime)
            }
        }
    }

    override fun stop() {
        viewModelScope.launch(context = Dispatchers.Default) {
            try {
                converter.destroyProcess()
                conversion?.cancelAndJoin()
                conversion = null
                notifyMain { updateCurrentStatus(status = QueueStatus.NOT_STARTED) }
                currentMediaId = null
                logMonitor?.cancelAndJoin()
                logMonitor = null
                notifyMain {
                    process.setConverting(false)
                    setStatus(ConverterStatusState.Initial)
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
                notifyMain { onError(Error.ON_STOPPING_CONVERSION) }
            }
        }
    }

    private fun onStdout(it: String) { logsCache.add(it) }

    private fun onError(error: Error) = setStatus(ConverterStatusState.Failure(error = error))

    private fun onProgress(it: ProgressData?) {
        val current = process.queue.value.find { it.id == currentMediaId }
        val duration = current?.input?.duration
        var step: Int? = null
        var percentage = 0f
        var speed = ""

        updateCurrentStatus(status = QueueStatus.IN_PROGRESS)

        if(it != null && duration != null) {
            val pendingQueueSize = process.pendingQueueSize()

            percentage = if(duration > 0) ((it.time * 100.0f) / duration) else 0.0f
            step = pendingQueueSize.takeIf { it > 0 }
            speed = it.speed
        }

        setStatus(ConverterStatusState.Progress(step, percentage, speed))
    }

    private fun notifyCompletion(startTime: Instant) {
        val finishTime = Instant.now()

        if(_state.value.status !is ConverterStatusState.Failure) {
            setStatus(ConverterStatusState.Complete(
                startTime = startTime.formatTime(),
                finishTime = finishTime.formatTime(),
                duration = startTime.durationUntil(end = finishTime)
            ))
        }
    }

    private fun startLogMonitor() {
        if (logMonitor?.isActive == true) return

        logMonitor = viewModelScope.launch(context = Dispatchers.Default) {
            while (true) {
                val cache = logsCache.toList()

                logsCache.clear()
                notifyMain { process.addLogs(data = cache) }
                delay(timeMillis = AppConfigs.LOG_MONITOR_DELAY)
            }
        }
    }

    override fun setStatus(status: ConverterStatusState) = _state.update { copy(status = status) }

    override fun setDialog(dialog: ConverterDialogState) = _state.update { copy(dialog = dialog) }

    override fun setOutput(fileName: String) = _state.update { copy(output = output?.copy(second = fileName)) }

    override fun setCodec(codec: Codec?) = _state.updateAndSync { copy(codec = codec) }

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

    override fun pickFolder(title: String, fileName: String) {
        val extension = _state.value.codec?.toFileExtension() ?: ""

        FileUtils.saveFile(title, fileName, extension)
            ?.let { _state.update { copy(output = it) } }
    }

    override fun setHdr10ToSdr(enabled: Boolean) { _state.updateAndSync { copy(hdr10ToSdr = enabled) } }

    private fun Codec?.toOutput(inputMedia: InputMedia): Pair<String, String>? {
        var output: Pair<String, String>? = null

        if(this != null) {
            val inputPath = inputMedia.path
            val outputName = File(inputPath).nameWithoutExtension
            val outputExtension = toFileExtension()

            output = Pair(first = DirUtils.outputDir, second = "$outputName.$outputExtension")
        }

        return output
    }

    private inline fun <T> MutableStateFlow<T>.updateAndSync(block: T.() -> T) {
        value = value.block()
        buildCommand()
    }

    private fun updateCurrentStatus(status: QueueStatus) {
        currentMediaId?.let {
            process.updateQueueItemStatus(
                id = it,
                status = status
            )
        }
    }
}