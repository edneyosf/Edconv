package features.home.screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FileOpen
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edconv.common.Channels
import edconv.common.MediaFormat
import features.home.events.HomeEvent
import features.home.events.HomeEvent.OnStart
import features.home.events.HomeEvent.OnStop
import features.home.events.HomeEvent.SetOutputFile
import features.home.events.HomeEvent.SetFormat
import features.home.events.HomeEvent.SetChannels
import features.home.managers.HomeManager
import features.home.states.HomeState
import features.home.states.HomeStatus
import features.home.texts.HomeTexts.Companion.TITLE_PICK_FILE_TEXT
import features.home.texts.homeTexts
import ui.compositions.*
import ui.theme.AppTheme
import ui.theme.extensions.custom
import java.awt.FileDialog
import java.awt.Frame
import kotlin.math.roundToInt

@Composable
fun HomeScreen() {
    val scope = rememberCoroutineScope()
    val manager = remember { HomeManager(scope) }

    Providers {
        HomeView(
            state = manager.state.value,
            onEvent = manager::onEvent
        )
    }
}

@Composable
private fun Providers(content: @Composable () -> Unit) = CompositionLocalProvider(
    value = textsComp provides homeTexts,
    content = content
)

@Composable
private fun HomeView(state: HomeState, onEvent: (HomeEvent) -> Unit) {
    val status = state.status
    val scrollState = rememberScrollState()
    val titlePickFile = texts.get(TITLE_PICK_FILE_TEXT)
    val logsScroll = rememberScrollState()

    LaunchedEffect(state.logs) { logsScroll.animateScrollTo(scrollState.maxValue) }

    Scaffold(
        topBar =  {
            Row(modifier = Modifier.padding(vertical = dimens.b, horizontal = dimens.c), verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        pickFile(titlePickFile)?.let { onEvent(HomeEvent.SetInputFile(it)) }
                    },
                ) {
                    Icon(imageVector = Icons.Rounded.FileOpen, contentDescription = null)
                }
                if(state.inputFile != null) {
                    Text(state.inputFile.toString(), style = TextStyle(fontSize = fontSizes.a, fontWeight = FontWeight.Normal))
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                    },
                ) {
                    Icon(imageVector = Icons.Rounded.Settings, contentDescription = null)
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState)) {
            Column(modifier = Modifier.padding(dimens.i), verticalArrangement = Arrangement.spacedBy(dimens.f)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        FilledIconButton(
                            enabled = status !is HomeStatus.Loading && status !is HomeStatus.Progress,
                            onClick = { onEvent(OnStart) }){
                            Icon(imageVector = Icons.Rounded.PlayArrow, contentDescription = null)
                        }
                        Text("Iniciar", style = TextStyle(fontSize = fontSizes.a))
                    }
                    Spacer(modifier = Modifier.width(dimens.i))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        FilledTonalIconButton(
                            enabled = status !is HomeStatus.Loading,
                            onClick = { onEvent(OnStop) }){
                            Icon(imageVector = Icons.Rounded.Stop, contentDescription = null)
                        }
                        Text("Parar", style = TextStyle(fontSize = fontSizes.a))
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(dimens.d), verticalAlignment = Alignment.CenterVertically) {
                    Format(value = state.format, onValueChange = {
                        onEvent(SetFormat(it))
                    })
                    Channels(value = state.channels, onValueChange = {
                        onEvent(SetChannels(it))
                    })
                    Switch(
                        checked = true,
                        onCheckedChange = {
                        }
                    )
                    Column {
                        val teste = remember { mutableStateOf(0.0f) }
                        Text(String.format("%.0f", teste.value))
                        Slider(
                            value = teste.value,
                            onValueChange = { teste.value = it.roundToInt().toFloat() },
                            valueRange = 0f..63f,
                            //colors = SliderDefaults.colors().copy(inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerHighest)

                        )
                    }
                }

                Row {
                    Card {
                        Box(Modifier.fillMaxWidth().height(200.dp).padding(dimens.i)) {
                            Box(modifier = Modifier.verticalScroll(logsScroll, reverseScrolling = true)) {
                                Text(state.logs)
                            }
                        }
                    }
                }

                Row {
                    TextField(
                        value = state.outputFile ?: "",
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors().custom(),
                        onValueChange = { onEvent(SetOutputFile(it)) },
                        label = { Text("Output") }
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(dimens.d), horizontalAlignment = Alignment.CenterHorizontally) {
                    when(status) {
                        is HomeStatus.Loading -> Progress()
                        is HomeStatus.Progress -> {
                            Progress(status.percentage)
                            Text("${String.format("%.2f", status.percentage)}%")
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
}

@Composable
private fun Progress(percentage: Float = 0f) {
    LinearProgressIndicator(
        progress = { percentage / 100 },
        drawStopIndicator = {},
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Format(value: MediaFormat?, onValueChange: (MediaFormat) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(value) }
    val options = listOf(MediaFormat.AAC, MediaFormat.EAC3, MediaFormat.H265, MediaFormat.AV1)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            value = selectedOption?.text ?: "",
            readOnly = true,
            onValueChange = {  },
            label = { Text("Formato") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.colors().custom(),
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).width(140.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption.text) },
                    onClick = {
                        selectedOption = selectionOption
                        expanded = false
                        onValueChange(selectionOption)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Channels(value: Channels?, onValueChange: (Channels) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(value) }
    val options = listOf(Channels.STEREO, Channels.DOWNMIXING_SURROUND_51, Channels.SURROUND_51)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            value = selectedOption?.text ?: "",
            readOnly = true,
            onValueChange = {  },
            label = { Text("Canais") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).width(220.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption.text) },
                    onClick = {
                        selectedOption = selectionOption
                        expanded = false
                        onValueChange(selectionOption)
                    }
                )
            }
        }
    }
}

private fun pickFile(title: String): String? {
    val dialog = FileDialog(null as Frame?, title, FileDialog.LOAD)
        .apply { isVisible = true }
    val file = dialog.files.firstOrNull()

    return file?.absolutePath
}

@Preview
@Composable
private fun EnglishLight() = Providers {
    AppTheme(darkTheme = false) {
        HomeView(state = HomeManager.defaultState(), onEvent = {})
    }
}

@Preview
@Composable
private fun EnglishDark() = Providers {
    AppTheme(darkTheme = true) {
        HomeView(state = HomeManager.defaultState(), onEvent = {})
    }
}

@Preview
@Composable
private fun PortugueseLight() = Providers {
    AppTheme(darkTheme = false) {
        HomeView(state = HomeManager.defaultState(), onEvent = {})
    }
}

@Preview
@Composable
private fun PortugueseDark() = Providers {
    AppTheme(darkTheme = true) {
        HomeView(state = HomeManager.defaultState(), onEvent = {})
    }
}