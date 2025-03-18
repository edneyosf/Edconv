package features.home.screens

import androidx.compose.runtime.*
import features.home.managers.HomeManager
import features.home.values.HomeTexts
import features.home.values.HomeTexts.Companion.TITLE_PICK_FILE_TEXT
import features.home.views.HomeView
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
fun HomeScreen() {
    val scope = rememberCoroutineScope()
    val manager = remember { HomeManager(scope) }
    val texts = HomeTexts()
    val state by manager.state
    var selectedFile by remember { mutableStateOf<File?>(null) }

    LaunchedEffect(selectedFile) {
        manager.outputFile.value = "/home/edney/teste.eac3"
    }

    HomeView(state,
        inputFile = selectedFile?.absolutePath ?: "",
        outputFile = manager.outputFile,
        format = manager.format,
        channels = manager.channels,
        vbr = manager.vbr,
        kbps = manager.kbps,
        sampleRate = manager.sampleRate,
        preset = manager.preset,
        crf = manager.crf,
        resolution = manager.resolution,
        bit = manager.bit,
        noAudio = manager.noAudio,
        onSelectedFile = {
            selectedFile = pickFile(title = texts.retrieve(TITLE_PICK_FILE_TEXT))
        },
        onStart = {
        val path = selectedFile?.absolutePath
        if(path != null) {
            manager.convert(inputFile = path)
        }
    })
}

private fun pickFile(title: String): File? {
    val dialog = FileDialog(null as Frame?, title, FileDialog.LOAD)
        .apply { isVisible = true }

    return dialog.files.firstOrNull()
}