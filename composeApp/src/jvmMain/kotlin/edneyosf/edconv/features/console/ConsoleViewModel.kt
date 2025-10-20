package edneyosf.edconv.features.console

import androidx.lifecycle.ViewModel
import edneyosf.edconv.core.process.EdProcess
import kotlinx.coroutines.flow.StateFlow

class ConsoleViewModel(private val process: EdProcess) : ViewModel(), ConsoleEvent {

    val logsState: StateFlow<List<String>> = process.logs
    val commandState: StateFlow<String> = process.command

    override fun setCommand(it: String) { process.setCommand(it) }
}