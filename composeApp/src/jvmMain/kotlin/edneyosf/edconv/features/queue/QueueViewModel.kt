package edneyosf.edconv.features.queue

import androidx.lifecycle.ViewModel
import edneyosf.edconv.core.process.EdProcess
import edneyosf.edconv.core.process.MediaQueue
import kotlinx.coroutines.flow.StateFlow

class QueueViewModel(private val process: EdProcess) : ViewModel(), QueueEvent {

    val state: StateFlow<List<MediaQueue>> = process.queue

    override fun removeItem(item: MediaQueue) = process.removeFromQueue(item)

    override fun clear() = process.clearQueue()
}