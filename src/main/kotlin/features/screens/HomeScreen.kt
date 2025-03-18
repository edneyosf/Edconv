package features.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import core.edconv.common.MediaFormat
import features.states.HomeUiState
import features.vms.HomeViewModel
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
fun HomeScreen() {
    val scope = rememberCoroutineScope()
    val viewModel = remember { HomeViewModel(scope) }
    var selectedFile by remember { mutableStateOf<File?>(null) }

    LaunchedEffect(selectedFile) {
        val path = selectedFile?.absolutePath
        val out = "/home/edney/teste.mkv"

        if(path != null) { viewModel.convert(inputFile = path, outputFile = out, format = MediaFormat.AV1) }
    }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

        Button(onClick = {
            selectedFile = pickFile()
        }) {
            Text("Selecionar Arquivo")
        }

        selectedFile?.let {
            Text("Arquivo Selecionado: ${it.absolutePath}")
        }
        Text(viewModel.data.value)

        val state = viewModel.uiState.value

        if(state is HomeUiState.Progress) {
            LinearProgressIndicator(
                state.percentage / 100,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

fun pickFile(): File? {
    val dialog = FileDialog(null as Frame?, "Escolha um arquivo", FileDialog.LOAD)
    dialog.isVisible = true

    return dialog.files.firstOrNull()
}