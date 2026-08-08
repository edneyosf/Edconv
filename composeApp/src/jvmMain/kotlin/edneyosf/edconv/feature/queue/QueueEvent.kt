package edneyosf.edconv.feature.queue

import edneyosf.edconv.core.process.MediaQueue

interface QueueEvent {
    fun removeItem(item: MediaQueue) = Unit
    fun clear() = Unit

    fun setShutdown(value: Boolean) = Unit
}