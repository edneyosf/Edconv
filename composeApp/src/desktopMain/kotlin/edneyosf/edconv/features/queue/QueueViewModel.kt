package edneyosf.edconv.features.queue

import androidx.lifecycle.ViewModel
import edneyosf.edconv.core.process.EdProcess

class QueueViewModel(private val process: EdProcess) : ViewModel() {

    val state get() = process.queue

    init {

    }
}