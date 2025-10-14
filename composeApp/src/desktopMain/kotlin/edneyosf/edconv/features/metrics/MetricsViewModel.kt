package edneyosf.edconv.features.metrics

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edneyosf.edconv.core.common.DateTimePattern
import edneyosf.edconv.core.common.Error
import edneyosf.edconv.core.config.EdConfig
import edneyosf.edconv.core.extensions.notifyMain
import edneyosf.edconv.core.extensions.update
import edneyosf.edconv.core.process.EdProcess
import edneyosf.edconv.core.utils.DateTimeUtils
import edneyosf.edconv.core.utils.FileUtils
import edneyosf.edconv.features.home.mappers.toInputMedia
import edneyosf.edconv.features.metrics.states.MetricsState
import edneyosf.edconv.features.metrics.states.MetricsStatusState
import edneyosf.edconv.ffmpeg.data.ProgressData
import edneyosf.edconv.ffmpeg.ffmpeg.MetricsFFmpeg
import edneyosf.edconv.ffmpeg.ffprobe.FFprobe
import edneyosf.edconv.ffmpeg.metrics.Metrics
import edneyosf.edconv.ffmpeg.metrics.MetricsScore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.time.Instant

class MetricsViewModel(private val config: EdConfig, private val process: EdProcess) : ViewModel(), MetricsEvent {

    private var startTime: Instant? = null
    private val metrics: Metrics

    private var analysis: Job? = null

    private val _state: MutableState<MetricsState>
    val state: State<MetricsState>

    init {
        val stateValue = MetricsState(threads = getThreads())

        _state = mutableStateOf(value = stateValue)
        state = _state
        metrics = Metrics(
            process = process,
            scope = viewModelScope,
            onStart = ::onStart,
            onStdout = {},
            onError = ::onError,
            onProgress = ::onProgress,
            onStop = ::onStop
        )

        observeInput()
    }

    private fun observeInput() {
        viewModelScope.launch {
            process.input.collectLatest {
                _state.update { copy(reference = it) }
            }
        }
    }

    private fun onStart() {
        startTime = Instant.now()
        process.setAnalyzing(true)
    }

    private fun onProgress(it: ProgressData?) {
        val duration = _state.value.reference?.duration
        var percentage = 0f
        var speed = ""

        if(it != null && duration != null) {
            percentage = if(duration > 0) ((it.time * 100.0f) / duration) else 0.0f
            speed = it.speed
        }

        setStatus(MetricsStatusState.Progress(percentage, speed))
    }

    private fun onStop(score: MetricsScore) {
        process.setAnalyzing(false)
        startTime?.let {
            val finishTime = Instant.now()
            val startText = DateTimeUtils.instantToText(instant = it, pattern = DateTimePattern.TIME_HMS)
            val finishText = DateTimeUtils.instantToText(instant = finishTime, pattern = DateTimePattern.TIME_HMS)
            val duration = DateTimeUtils.durationText(it, finishTime)

            startTime = null

            if(score.vmaf == null && score.psnr == null && score.ssim == null) onError(Error.METRICS_SCORE_NULL)
            else if(_state.value.status !is MetricsStatusState.Failure) {
                setStatus(MetricsStatusState.Complete(
                    vmafScore = score.vmaf?.toString(),
                    psnrScore = score.psnr?.toString(),
                    ssimScore = score.ssim?.toString(),
                    startTime = startText,
                    finishTime = finishText,
                    duration = duration
                ))
            }
        } ?: run {
            onError(Error.START_TIME_NULL)
        }
    }

    private fun onError(error: Error) = setStatus(status = MetricsStatusState.Failure(error))

    override fun setStatus(status: MetricsStatusState) = _state.update { copy(status = status) }

    override fun start() {
        _state.value.run {
            val referenceInfo = reference?.videos?.firstOrNull()

            when {
                referenceInfo != null && distorted != null -> {
                    setStatus(MetricsStatusState.Loading)
                    viewModelScope.launch(context = Dispatchers.IO) {
                        val referenceDim = Pair(first = referenceInfo.width, second = referenceInfo.height)
                        val distortedFile = File(distorted)
                        val ffprobe = FFprobe(ffprobePath = config.ffprobePath, file = distortedFile)
                        val distortedData = ffprobe.analyze()
                        val error = distortedData == null || distortedData.duration == null
                        val distortedInputData = distortedData.takeIf { !error }?.toInputMedia()
                        val distortedInfo = distortedInputData?.videos?.firstOrNull()

                        if(distortedInfo != null) {
                            val distortedDim = Pair(first = distortedInfo.width, second = distortedInfo.height)
                            val data = MetricsFFmpeg(
                                reference = reference.path,
                                distorted = distorted,
                                referenceDim = referenceDim,
                                distortedDim = distortedDim,
                                vmaf = vmaf,
                                psnr = psnr,
                                ssim = ssim,
                                fps = fps,
                                threads = threads
                            )

                            analysis = metrics.run(ffmpeg = config.ffmpegPath, data = data)
                        }
                        else {
                            notifyMain { onError(Error.NO_VIDEO_INPUT_MEDIA) }
                        }
                    }
                }

                referenceInfo == null -> onError(Error.NO_VIDEO_INPUT_MEDIA)

                else -> onError(Error.ON_STARTING_METRICS_REQUIREMENTS)
            }
        }
    }

    override fun stop() {
        viewModelScope.launch(context = Dispatchers.Default) {
            try {
                metrics.destroyProcess()
                analysis?.cancelAndJoin()
                analysis = null
                notifyMain { setStatus(MetricsStatusState.Initial) }
            }
            catch (e: Exception) {
                e.printStackTrace()
                notifyMain { onError(Error.ON_STOPPING_METRICS) }
            }
        }
    }

    override fun pickDistortedFile(title: String) {
        val path = FileUtils.pickFile(title)

        path?.let { _state.update { copy(distorted = it) } }
    }

    override fun setFps(value: String) = _state.update { copy(fps = value.toInt()) }

    override fun setThread(value: String) = _state.update { copy(threads = value.toInt()) }

    override fun setVmaf(enabled: Boolean) { _state.update { copy(vmaf = enabled) } }

    override fun setPsnr(enabled: Boolean) { _state.update { copy(psnr = enabled) } }

    override fun setSsim(enabled: Boolean) { _state.update { copy(ssim = enabled) } }

    private fun getThreads() = Runtime.getRuntime().availableProcessors()
}