package features.home.views

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import core.edconv.common.Resolutions
import features.home.states.HomeState
import features.home.values.HomeTexts
import features.home.values.HomeTexts.Companion.SELECT_FILE_TEXT
import ui.previews.Desktop

@Composable
fun HomeView(
    state: HomeState,
    inputFile: String,
    outputFile: MutableState<String>,
    format: MutableState<String>,
    channels: MutableState<String>,
    vbr: MutableState<String>,
    kbps: MutableState<String>,
    sampleRate: MutableState<String?>,
    preset: MutableState<String?>,
    crf: MutableState<Int?>,
    resolution: MutableState<Resolutions>,
    bit: MutableState<String?>,
    noAudio: MutableState<Boolean>,
    onSelectedFile: () -> Unit, onStart: () -> Unit) {

    val texts = HomeTexts()
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.verticalScroll(scrollState)) {
        Row {
            Button(onClick = {
                onSelectedFile()
            }) {
                Text(texts.retrieve(SELECT_FILE_TEXT))
            }
            IconButton(onClick = { onStart() }){
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
            }
        }
        Row {
            Column {
                Text("Fonte: " + inputFile)
                TextField(
                    outputFile.value,
                    modifier = Modifier,
                    onValueChange = { outputFile.value = it },
                    label = { Text("Saida") }
                )
            }
        }
        Row {
            TextField(
                format.value,
                modifier = Modifier,
                onValueChange = { format.value = it },
                label = { Text("Formato") }
            )
        }

        TextField(
            channels.value,
            modifier = Modifier,
            onValueChange = { channels.value = it },
            label = { Text("Canais") }
        )
        TextField(
            vbr.value,
            modifier = Modifier,
            onValueChange = { vbr.value = it },
            label = { Text("VBR") }
        )
        TextField(
            kbps.value,
            modifier = Modifier,
            onValueChange = { kbps.value = it },
            label = { Text("Kbps") }
        )
        TextField(
            sampleRate.value ?: "",
            modifier = Modifier,
            onValueChange = { sampleRate.value = it },
            label = { Text("Taxa de Amostragem") }
        )
        TextField(
            preset.value ?: "",
            modifier = Modifier,
            onValueChange = { preset.value = it },
            label = { Text("Preset") }
        )
        TextField(
            crf.value.toString(),
            modifier = Modifier,
            onValueChange = { crf.value = it.toIntOrNull() },
            label = { Text("CRF") }
        )
        TextField(
            resolution.value.toString(),
            modifier = Modifier,
            onValueChange = { resolution.value = Resolutions.P1080 },
            label = { Text("Resolução") }
        )
        TextField(
            bit.value ?: "",
            modifier = Modifier,
            onValueChange = { bit.value = it },
            label = { Text("Bit") }
        )
        TextField(
            noAudio.value.toString(),
            modifier = Modifier,
            onValueChange = { noAudio.value = it.toBoolean() },
            label = { Text("Sem audio") }
        )

        if(state is HomeState.Progress) {
            LinearProgressIndicator(
                state.percentage / 100,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun Default() = Desktop {
    HomeView(
        state = HomeState.Initial,
        inputFile = "",
        outputFile = mutableStateOf(""),
        format = mutableStateOf(""),
        channels = mutableStateOf(""),
        vbr = mutableStateOf(""),
        kbps = mutableStateOf(""),
        sampleRate = mutableStateOf(""),
        preset = mutableStateOf(""),
        crf = mutableStateOf(0),
        resolution = mutableStateOf(Resolutions.P1080),
        bit = mutableStateOf(""),
        noAudio = mutableStateOf(false),
        onSelectedFile = {},
        onStart = {}
    )
}