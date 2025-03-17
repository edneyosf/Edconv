package core.edconv

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class Edconv(private val scope: CoroutineScope) {

    private fun toAV1() {
        //TODO
    }

    private fun toH265() {
        //TODO
    }

    private fun toEAC3() {
        //TODO
    }

    private fun toAAC() {
        //TODO
    }

    fun run(command: List<String>, onStdout: (String) -> Unit, onStderr: (String) -> Unit) {
        scope.launch(context = Dispatchers.IO) {
            try {
                val process = ProcessBuilder(command).start()
                val outReader = BufferedReader(InputStreamReader(process.inputStream))
                val errReader = BufferedReader(InputStreamReader(process.errorStream))
                var line: String?

                while (true) {
                    line = outReader.readLine() ?: break
                    notify(line, onStdout)
                }
                while (true) {
                    line = errReader.readLine() ?: break
                    notify(line, onStderr)
                }
            }
            catch (e: Exception) {
                notify(e.message, onStderr)
            }
        }
    }

    private suspend fun notify(content: String?, onStd: (String) -> Unit) = content?.let {
        withContext(context = Dispatchers.Main) { onStd(it) }
    }
}