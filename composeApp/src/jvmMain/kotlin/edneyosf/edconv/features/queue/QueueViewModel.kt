package edneyosf.edconv.features.queue

import androidx.lifecycle.ViewModel
import edneyosf.edconv.core.process.EdProcess
import edneyosf.edconv.core.process.MediaQueue

class QueueViewModel(private val process: EdProcess) : ViewModel(), QueueEvent {

    val queue = process.queue
    val shutdown = process.shutdown

    override fun removeItem(item: MediaQueue) = process.removeFromQueue(item)

    override fun clear() = process.clearQueue()

    override fun setShutdown(value: Boolean) = process.setShutdown(value)
}