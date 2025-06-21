package edneyosf.edconv.core.process

import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.ffmpeg.common.MediaType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class EdProcess {
    private val _input = MutableStateFlow<InputMedia?>(value = null)
    val input = _input.asStateFlow()

    private val _inputType = MutableStateFlow<MediaType?>(value = null)
    val inputType = _inputType.asStateFlow()

    private val _converting = MutableStateFlow(value = false)
    val converting = _converting.asStateFlow()

    private val _analysis = MutableStateFlow(value = false)
    val analysis = _analysis.asStateFlow()

    private val _queue = MutableStateFlow(value = mutableListOf<MediaQueue>())
    val queue = _queue.asStateFlow()

    fun setInput(inputMedia: InputMedia?) { _input.value = inputMedia }

    fun setInputType(mediaType: MediaType?) { _inputType.value = mediaType }

    fun setConverting(status: Boolean) { _converting.value = status }

    fun setAnalysis(status: Boolean) { _analysis.value = status }

    fun addToQueue(item: MediaQueue) { _queue.value.add(item) }
}