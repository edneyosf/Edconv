package edneyosf.edconv.core.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext

abstract class Manager(protected open val scope: CoroutineScope) {
    protected suspend inline fun <T> notifyMain(crossinline block: () -> T): Unit =
        withContext(context = Dispatchers.Swing) { block() }
}