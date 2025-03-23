package features.home.screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import core.Languages
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
import features.home.texts.HomeTexts
import features.home.texts.HomeTexts.Companion.SELECT_FILE_TEXT
import features.home.texts.HomeTexts.Companion.TITLE_PICK_FILE_TEXT
import features.home.texts.homeTexts
import ui.compositions.*
import ui.previews.ScreenDelimiter
import java.awt.FileDialog
import java.awt.Frame
import kotlin.math.roundToInt

@Composable
fun HomeScreen() {
    val scope = rememberCoroutineScope()
    val manager = remember { HomeManager(scope) }

    CompositionLocalProvider(textsComp provides homeTexts) {
        HomeView(
            state = manager.state.value,
            onEvent = manager::onEvent
        )
    }
}

@Composable
private fun HomeView(state: HomeState, onEvent: (HomeEvent) -> Unit) {
    val status = state.status
    val scrollState = rememberScrollState()
    val title = texts.get(TITLE_PICK_FILE_TEXT)
    val logsScrolls = rememberScrollState()
    val modifier = Modifier
        .padding(dimens.i)
        .verticalScroll(scrollState)

    LaunchedEffect(state.logs) {
        logsScrolls.animateScrollTo(scrollState.maxValue)
    }

    Scaffold {
        Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(dimens.d)) {

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(dimens.d)) {
                Button(onClick = {
                    pickFile(title)?.let { onEvent(HomeEvent.SetInputFile(it)) }
                }) {
                    Text(texts.get(SELECT_FILE_TEXT))
                }
                if(state.inputFile != null) {
                    Text(state.inputFile.toString())
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                IconButton(
                    enabled = status !is HomeStatus.Loading && status !is HomeStatus.Progress,
                    onClick = { onEvent(OnStart) }){
                        Icon(imageVector = Icons.Filled.PlayArrow, tint = if(status is HomeStatus.Loading || status is HomeStatus.Progress) MaterialTheme.colorScheme.surfaceDim else Color(0xFFA7D394), contentDescription = null)
                }
                IconButton(
                    enabled = status !is HomeStatus.Loading,
                    onClick = { onEvent(OnStop) }){
                    Icon(imageVector = Icons.Filled.Stop, tint = if(status is HomeStatus.Loading) MaterialTheme.colorScheme.surfaceDim else MaterialTheme.colorScheme.error, contentDescription = null)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(dimens.d)) {
                FilterChip(
                    onClick = { },
                    label = {
                        Text("Ipod")
                    },
                    selected = true,
                    leadingIcon = if (true) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    },
                )
                FilterChip(
                    onClick = { },
                    label = {
                        Text("Movie")
                    },
                    selected = true,
                    leadingIcon = if (false) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    },
                )
                FilterChip(
                    onClick = { },
                    label = {
                        Text("Anime")
                    },
                    selected = true,
                    leadingIcon = if (false) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    },
                )
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
                        valueRange = 0f..100f
                    )
                }
            }

            Row {
                Card {
                    Box(Modifier.fillMaxWidth().height(200.dp).padding(dimens.i)) {
                        Box(modifier = Modifier.verticalScroll(logsScrolls, reverseScrolling = true)) {
                            Text(state.logs)
                        }
                    }
                }
            }

            Row {
                TextField(
                    value = state.outputFile ?: "",
                    modifier = Modifier.fillMaxWidth(),
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

@Composable
private fun Progress(percentage: Float = 0f) {
    LinearProgressIndicator(
        progress = { percentage / 100 },
        drawStopIndicator = {},
        modifier = Modifier.fillMaxWidth(),
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
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
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
private fun English() = ScreenDelimiter {
    CompositionLocalProvider(textsComp provides HomeTexts(Languages.EN)) {
        HomeView(state = HomeManager.defaultState(), onEvent = {})
    }
}

@Preview
@Composable
private fun Portuguese() = ScreenDelimiter {
    CompositionLocalProvider(textsComp provides HomeTexts(Languages.PT)) {
        HomeView(state = HomeManager.defaultState(), onEvent = {})
    }
}