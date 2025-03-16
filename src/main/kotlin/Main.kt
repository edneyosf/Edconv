import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.FileDialog
import java.awt.Frame
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

@Composable
@Preview
fun App() {
    var selectedFile by remember { mutableStateOf<File?>(null) }
    val output = remember { mutableStateOf("") }

    LaunchedEffect(selectedFile) {
        val path = selectedFile?.absolutePath

        if(path != null) {
            val root = File(System.getProperty("compose.application.resources.dir")).parentFile.parentFile.parentFile
            val command = root.absolutePath + "/bin/core"
            executeCommand(listOf(command, "-ffmpeg", "${root.absolutePath}/bin/ffmpeg", "-input", path, "-output", "/home/edney/teste.aac", "-format", "aac", "-channels", "62"), output)
        }
    }

    MaterialTheme {
        Column {

            Button(onClick = {
                selectedFile = pickFile()
            }) {
                Text("Selecionar Arquivo")
            }

            selectedFile?.let {
                Text("Arquivo Selecionado: ${it.absolutePath}")
                Text(output.value)
            }
        }
    }
}

fun pickFile(): File? {
    val dialog = FileDialog(null as Frame?, "Escolha um arquivo", FileDialog.LOAD)
    dialog.isVisible = true

    return dialog.files.firstOrNull()
}

fun main() = application {

    Window(title = "Edconv", onCloseRequest = ::exitApplication) {
        App()
    }
}

fun executeCommand(command: List<String>, output: MutableState<String>) {
    try {
        val process = ProcessBuilder(command).start()
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val errorReader = BufferedReader(InputStreamReader(process.errorStream))

        val successOutput = StringBuilder()
        var line: String?

        // Lendo a saída padrão do comando
        while (reader.readLine().also { line = it } != null) {
            successOutput.append(line).append("\n")
        }

        // Lendo a saída de erro do comando
        val errorOutput = StringBuilder()
        while (errorReader.readLine().also { line = it } != null) {
            errorOutput.append(line).append("\n")
        }

        // Atualizando a variável de estado com o sucesso ou erro
        if (successOutput.isNotEmpty()) {
            output.value = "Success: \n$successOutput"
        } else if (errorOutput.isNotEmpty()) {
            output.value = "Error: \n$errorOutput"
        } else {
            output.value = "No output"
        }

    } catch (e: Exception) {
        output.value = "Error executing command: ${e.message}"
    }
}