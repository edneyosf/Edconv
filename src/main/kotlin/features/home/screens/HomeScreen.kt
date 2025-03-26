package features.home.screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FileOpen
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
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
import features.home.texts.HomeTexts.Companion.CHANNELS_INPUT
import features.home.texts.HomeTexts.Companion.FORMAT_INPUT
import features.home.texts.HomeTexts.Companion.OUTPUT_FILE
import features.home.texts.HomeTexts.Companion.SELECT_FILE_TEXT
import features.home.texts.HomeTexts.Companion.START_CONVERSION
import features.home.texts.HomeTexts.Companion.STOP_CONVERSION
import features.home.texts.HomeTexts.Companion.TITLE_PICK_FILE_TEXT
import features.home.texts.homeTexts
import ui.components.Selector
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
    val titlePickFile = texts.get(TITLE_PICK_FILE_TEXT)

    Scaffold(
        topBar =  {
            TopBar(
                inputFile = state.inputFile,
                onPickFile = {
                    pickFile(titlePickFile)?.let { onEvent(HomeEvent.SetInputFile(it)) }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier.padding(start = dimens.i, end = dimens.i, bottom = dimens.i),
                verticalArrangement = Arrangement.spacedBy(dimens.i)
            ) {
                Actions(
                    status = status,
                    onStart = { onEvent(OnStart) },
                    onStop = { onEvent(OnStop) }
                )

                // Settings
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimens.d),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FormatInput(
                        value = state.format,
                        onValueChange = { onEvent(SetFormat(it)) }
                    )

                    ChannelsInput(
                        value = state.channels,
                        onValueChange = { onEvent(SetChannels(it)) }
                    )

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

                LogsView(state.logs)

                Progress(status)

                TextField(
                    value = state.outputFile ?: "",
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors().custom(),
                    onValueChange = { onEvent(SetOutputFile(it)) },
                    label = { Text(text = texts.get(OUTPUT_FILE)) }
                )
            }
        }
    }
}

@Composable
private fun TopBar(inputFile: String?, onPickFile: () -> Unit) {
    val modifier = Modifier.padding(vertical = dimens.b, horizontal = dimens.c)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPickFile) {
            Icon(imageVector = Icons.Rounded.FileOpen, contentDescription = texts.get(SELECT_FILE_TEXT))
        }

        inputFile?.let {
            Spacer(modifier = Modifier.width(dimens.d))
            Text(it, style = TextStyle(fontSize = fontSizes.a, color = MaterialTheme.colorScheme.onSurface))
        }
    }
}

@Composable
private fun Actions(status: HomeStatus, onStart: () -> Unit, onStop: () -> Unit) {
    val isLoading = status is HomeStatus.Loading
    val startEnabled = !isLoading && status !is HomeStatus.Progress
    val stopEnabled = !isLoading

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            FilledIconButton(
                enabled = startEnabled,
                onClick = onStart
            ) {
                Icon(
                    imageVector = Icons.Rounded.PlayArrow,
                    contentDescription = texts.get(START_CONVERSION)
                )
            }

            Text(
                text = texts.get(START_CONVERSION),
                style = TextStyle(fontSize = fontSizes.a)
            )
        }

        Spacer(modifier = Modifier.width(dimens.i))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            FilledTonalIconButton(
                enabled = stopEnabled,
                onClick = onStop
            ){
                Icon(
                    imageVector = Icons.Rounded.Stop,
                    contentDescription = texts.get(STOP_CONVERSION)
                )
            }

            Text(
                text = texts.get(STOP_CONVERSION),
                style = TextStyle(fontSize = fontSizes.a)
            )
        }
    }
}

@Composable
fun FormatInput(value: MediaFormat?, onValueChange: (MediaFormat) -> Unit) {
    var expanded by remember { mutableStateOf(value = false) }
    var selected by remember { mutableStateOf(value) }

    Selector(
        text = selected?.text ?: "",
        label = texts.get(FORMAT_INPUT),
        expanded = expanded,
        onExpanded = { expanded = it }
    ) {
        MediaFormat.getAll().forEach { item ->
            DropdownMenuItem(
                text = { Text(item.text) },
                onClick = {
                    selected = item
                    expanded = false
                    onValueChange(item)
                }
            )
        }
    }
}

@Composable
fun ChannelsInput(value: Channels?, onValueChange: (Channels) -> Unit) {
    var expanded by remember { mutableStateOf(value = false) }
    var selected by remember { mutableStateOf(value) }

    Selector(
        text = selected?.text ?: "",
        label = texts.get(CHANNELS_INPUT),
        expanded = expanded,
        onExpanded = { expanded = it }
    ) {
        Channels.getAll().forEach { item ->
            DropdownMenuItem(
                text = { Text(item.text) },
                onClick = {
                    selected = item
                    expanded = false
                    onValueChange(item)
                }
            )
        }
    }
}

@Composable
private fun ColumnScope.LogsView(text: String) {
    val logsScroll = rememberScrollState()
    val modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
        .verticalScroll(state = logsScroll)

    LaunchedEffect(text) { logsScroll.animateScrollTo(logsScroll.maxValue) }

    Card(modifier = modifier) {
        Box(modifier = Modifier.padding(dimens.i)) {
            Text(text)
        }
    }
}

@Composable
private fun Progress(status: HomeStatus) {
    val modifier = Modifier
        .fillMaxWidth()
        .height(dimens.s)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        if(status is HomeStatus.Loading) {
            LinearProgress()
        }
        else if(status is HomeStatus.Progress) {
            val text = "${String.format("%.2f", status.percentage)}%"

            LinearProgress(status.percentage)
            Spacer(modifier = Modifier.height(dimens.d))
            Text(text, style = TextStyle(color = MaterialTheme.colorScheme.onSurface))
        }
    }
}

@Composable
private fun LinearProgress(percentage: Float = 0f) {
    LinearProgressIndicator(
        modifier = Modifier.fillMaxWidth(),
        progress = { percentage / 100 },
        strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap
    )
}

private fun pickFile(title: String): String? {
    val dialog = FileDialog(Frame(), title, FileDialog.LOAD).apply { isVisible = true }
    val file = dialog.files.firstOrNull()

    return file?.absolutePath
}

@Composable
private fun HomeScreenPreview(language: String, darkTheme: Boolean) {
    CompositionLocalProvider(textsComp provides HomeTexts(language)) {
        AppTheme(darkTheme = darkTheme) {
            HomeView(state = HomeManager.defaultState(), onEvent = {})
        }
    }
}

@Preview
@Composable
private fun EnglishLight() = HomeScreenPreview(language = Languages.EN, darkTheme = false)

@Preview
@Composable
private fun EnglishDark() = HomeScreenPreview(language = Languages.EN, darkTheme = true)

@Preview
@Composable
private fun PortugueseLight() = HomeScreenPreview(language = Languages.PT, darkTheme = false)

@Preview
@Composable
private fun PortugueseDark() = HomeScreenPreview(language = Languages.PT, darkTheme = true)