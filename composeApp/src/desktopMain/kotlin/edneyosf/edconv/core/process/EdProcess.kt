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

    private val _queue = MutableStateFlow(value = listOf<MediaQueue>())
    val queue = _queue.asStateFlow()

    fun setInput(inputMedia: InputMedia?) { _input.value = inputMedia }

    fun setInputType(mediaType: MediaType?) { _inputType.value = mediaType }

    fun setConverting(status: Boolean) { _converting.value = status }

    fun setAnalysis(status: Boolean) { _analysis.value = status }

    fun addToQueue(item: MediaQueue) { _queue.value = _queue.value + item }

    fun updateQueueItemById(id: String?, block: MediaQueue.() -> MediaQueue) {
        if(id != null) {
            _queue.value = _queue.value.map {
                if(it.id == id) it.block() else it
            }
        }
    }

    fun updateQueueItemStatus(id: String?, status: QueueStatus) {
        updateQueueItemById(id) { copy(status = status) }
    }

    fun queueSize() = _queue.value.size

    fun pendingQueueSize() = _queue.value.filter { it.status == QueueStatus.NOT_STARTED }.size
}