package edneyosf.edconv.core.extensions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext

suspend inline fun <T> notifyMain(crossinline block: () -> T): Unit =
    withContext(context = Dispatchers.Swing) { block() }