package edneyosf.edconv.features.queue

import edneyosf.edconv.core.process.MediaQueue

interface QueueEvent {
    fun removeItem(item: MediaQueue) = Unit
    fun clear() = Unit
}