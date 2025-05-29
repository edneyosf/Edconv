package edneyosf.edconv.features.vmaf.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import edneyosf.edconv.core.extensions.update
import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.features.vmaf.events.VMAFEvent
import edneyosf.edconv.features.vmaf.states.VMAFState

class VMAFViewModel(input: InputMedia): ViewModel(), VMAFEvent {

    private val _state = mutableStateOf(value = VMAFState(input = input , threads = getThreads()))
    val state: State<VMAFState> = _state

    override fun refresh(newInput: InputMedia) = _state.update { copy(input = newInput) }

    override fun pickSourceFile(title: String) {
        super.pickSourceFile(title)
    }

    override fun pickModelFile(title: String) {
        super.pickModelFile(title)
    }

    private fun getThreads() = Runtime.getRuntime().availableProcessors()
}