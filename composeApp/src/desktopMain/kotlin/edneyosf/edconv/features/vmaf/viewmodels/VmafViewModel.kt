package edneyosf.edconv.features.vmaf.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edneyosf.edconv.core.common.Error
import edneyosf.edconv.core.config.ConfigManager
import edneyosf.edconv.core.extensions.notifyMain
import edneyosf.edconv.core.extensions.update
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

class VmafViewModel(input: InputMedia): ViewModel(), VmafEvent {

    private val vmaf: Vmaf

    private var analysis: Job? = null

    private val _state = mutableStateOf(value = VmafState(
        reference = input,
        model = ConfigManager.getVmafModelPath().takeIf { it.isNotEmpty() },
        threads = getThreads()
    ))
    val state: State<VmafState> = _state

    init {
        vmaf = Vmaf(
            scope = viewModelScope,
            onStart = ::onStart,
            onStdout = ::onStdout,
            onError = ::onError,
            onProgress = ::onProgress,
            onStop = ::onStop
        )
    }

    private fun onStart() {
        setStatus(VmafStatusState.Loading)
    }

    private fun onStdout(it: String) {
        println("onStdout: "+ it)
    }

    private fun onError(error: Error) = setStatus(status = VmafStatusState.Failure(error))

    private fun onStop() {
        if(_state.value.status !is VmafStatusState.Failure) {
            setStatus(VmafStatusState.Complete)
        }
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

    override fun setStatus(status: VmafStatusState) = _state.update { copy(status = status) }

    override fun setDialog(dialog: VmafDialogState) = _state.update { copy(dialog = dialog) }

    override fun refresh(newInput: InputMedia) = _state.update { copy(reference = newInput) }

    override fun start() {
        val state = _state.value
        val referenceInfo = state.reference.videos.first()
        val referenceDimens = Pair(referenceInfo.width, referenceInfo.height)
        val distortedInfo = FFprobe(File(state.distorted!!)).analyze()!!.toInputMedia().videos.first()
        val distortedDimens = Pair(distortedInfo.width, distortedInfo.height)
        val data = VmafFFmpeg(
            reference = state.reference.path,
            distorted = state.distorted,
            referenceDimens = referenceDimens,
            distortedDimens = distortedDimens,
            fps = state.fps,
            threads = state.threads,
            model = state.model!!
        )

        analysis = vmaf.run(
            ffmpeg = ConfigManager.getFFmpegPath(),
            data = data
        )
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
                notifyMain { onError(Error.ON_STOPPING_CONVERSION) }
            }
        }
    }

    override fun pickDistortedFile(title: String) {
        val path = FileUtils.pickFile(title)

        if(path != null) {
            _state.update { copy(distorted = path) }
        }
    }

    override fun pickModelFile(title: String) {
        val path = FileUtils.pickFile(title)

        if(path != null) {
            viewModelScope.launch(context = Dispatchers.IO) {
                ConfigManager.setVmafModelPath(path)
                notifyMain { _state.update { copy(model = path) } }
            }
        }
    }

    override fun setFps(value: String) = _state.update { copy(fps = value.toInt()) }

    override fun setThread(value: String) = _state.update { copy(threads = value.toInt()) }

    private fun getThreads() = Runtime.getRuntime().availableProcessors()
}