package edneyosf.edconv.features.vmaf.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edneyosf.edconv.core.common.DateTimePattern
import edneyosf.edconv.core.common.Error
import edneyosf.edconv.core.config.ConfigManager
import edneyosf.edconv.core.extensions.notifyMain
import edneyosf.edconv.core.extensions.update
import edneyosf.edconv.core.utils.DateTimeUtils
import edneyosf.edconv.core.utils.FileUtils
import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.features.home.mappers.toInputMedia
import edneyosf.edconv.features.vmaf.events.VmafEvent
import edneyosf.edconv.features.vmaf.states.VmafDialogState
import edneyosf.edconv.features.vmaf.states.VmafState
import edneyosf.edconv.features.vmaf.states.VmafStatusState
import edneyosf.edconv.ffmpeg.data.ProgressData
import edneyosf.edconv.ffmpeg.ffmpeg.VmafFFmpeg
import edneyosf.edconv.ffmpeg.ffprobe.FFprobe
import edneyosf.edconv.ffmpeg.vmaf.Vmaf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import java.io.File
import java.time.Instant

class VmafViewModel(input: InputMedia): ViewModel(), VmafEvent {

    private var startTime: Instant? = null
    private val vmaf: Vmaf

    private var analysis: Job? = null

    private val _state: MutableState<VmafState>
    val state: State<VmafState>

    init {
        val modelPath = ConfigManager.getVmafModelPath()
        val model = modelPath.takeIf { it.isNotEmpty() }
        val stateValue = VmafState(reference = input, model = model, threads = getThreads())

        _state = mutableStateOf(value = stateValue)
        state = _state
        vmaf = Vmaf(
            scope = viewModelScope,
            onStart = ::onStart,
            onStdout = {},
            onError = ::onError,
            onProgress = ::onProgress,
            onStop = ::onStop
        )
    }

    private fun onStart() {
        startTime = Instant.now()
    }

    private fun onProgress(it: ProgressData?) {
        var percentage = 0f
        var speed = ""

        if(it != null) {
            val duration = _state.value.reference.duration

            percentage = if(duration > 0) ((it.time * 100.0f) / duration) else 0.0f
            speed = it.speed
        }

        setStatus(VmafStatusState.Progress(percentage, speed))
    }

    private fun onStop(score: Double?) {
        startTime?.let {
            val finishTime = Instant.now()
            val startText = DateTimeUtils.instantToText(instant = it, pattern = DateTimePattern.TIME_HMS)
            val finishText = DateTimeUtils.instantToText(instant = finishTime, pattern = DateTimePattern.TIME_HMS)
            val duration = DateTimeUtils.durationText(it, finishTime)

            startTime = null
            if(_state.value.status !is VmafStatusState.Failure && score != null) {
                setStatus(VmafStatusState.Complete(
                    score = score.toString(),
                    startTime = startText,
                    finishTime = finishText,
                    duration = duration
                ))
            }
            else if(score == null) {
                onError(Error.VMAF_SCORE_NULL)
            }
        } ?: run {
            onError(Error.START_TIME_NULL)
        }
    }

    private fun onError(error: Error) = setStatus(status = VmafStatusState.Failure(error))

    override fun setStatus(status: VmafStatusState) = _state.update { copy(status = status) }

    override fun setDialog(dialog: VmafDialogState) = _state.update { copy(dialog = dialog) }

    override fun refresh(newInput: InputMedia) = _state.update { copy(reference = newInput) }

    override fun start() {
        _state.value.run {
            val referenceInfo = reference.videos.firstOrNull()

            when {
                referenceInfo != null && distorted != null && model != null -> {
                    setStatus(VmafStatusState.Loading)
                    viewModelScope.launch(context = Dispatchers.IO) {
                        val referenceDim = Pair(first = referenceInfo.width, second = referenceInfo.height)
                        val distortedFile = File(distorted)
                        val distortedInputData = FFprobe(distortedFile).analyze()?.toInputMedia()
                        val distortedInfo = distortedInputData?.videos?.firstOrNull()

                        if(distortedInfo != null) {
                            val distortedDim = Pair(first = distortedInfo.width, second = distortedInfo.height)
                            val data = VmafFFmpeg(
                                reference = reference.path,
                                distorted = distorted,
                                referenceDim = referenceDim,
                                distortedDim = distortedDim,
                                fps = fps,
                                threads = threads,
                                model = model
                            )

                            analysis = vmaf.run(ffmpeg = ConfigManager.getFFmpegPath(), data = data)
                        }
                        else {
                            notifyMain { onError(Error.NO_VIDEO_INPUT_MEDIA) }
                        }
                    }
                }

                referenceInfo == null -> onError(Error.NO_VIDEO_INPUT_MEDIA)

                else -> onError(Error.ON_STARTING_VMAF_REQUIREMENTS)
            }
        }
    }

    override fun stop() {
        viewModelScope.launch(context = Dispatchers.Default) {
            try {
                vmaf.destroyProcess()
                analysis?.cancelAndJoin()
                analysis = null
                notifyMain { setStatus(VmafStatusState.Initial) }
            }
            catch (e: Exception) {
                e.printStackTrace()
                notifyMain { onError(Error.ON_STOPPING_VMAF) }
            }
        }
    }

    override fun pickDistortedFile(title: String) {
        val path = FileUtils.pickFile(title)

        path?.let { _state.update { copy(distorted = it) } }
    }

    override fun pickModelFile(title: String) {
        val path = FileUtils.pickFile(title)

        path?.let {
            viewModelScope.launch(context = Dispatchers.IO) {
                ConfigManager.setVmafModelPath(it)
                notifyMain { _state.update { copy(model = it) } }
            }
        }
    }

    override fun setFps(value: String) = _state.update { copy(fps = value.toInt()) }

    override fun setThread(value: String) = _state.update { copy(threads = value.toInt()) }

    private fun getThreads() = Runtime.getRuntime().availableProcessors()
}