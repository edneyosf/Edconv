package features.home.screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import core.Languages
import edconv.av1.AV1Preset
import edconv.common.*
import edconv.h265.H265Preset
import features.home.events.HomeEvent
import features.home.events.HomeEvent.OnStart
import features.home.events.HomeEvent.OnStop
import features.home.events.HomeEvent.SetOutputFile
import features.home.events.HomeEvent.SetFormat
import features.home.events.HomeEvent.SetChannels
import features.home.events.HomeEvent.SetSampleRate
import features.home.events.HomeEvent.SetPixelFormat
import features.home.events.HomeEvent.SetResolution
import features.home.events.HomeEvent.SetNoAudio
import features.home.events.HomeEvent.SetKbps
import features.home.managers.HomeManager
import features.home.states.HomeState
import features.home.states.HomeStatus
import features.home.texts.HomeTexts
import features.home.texts.HomeTexts.Companion.AUDIO_MEDIA_TYPE
import features.home.texts.HomeTexts.Companion.BIT_RATE_INPUT
import features.home.texts.HomeTexts.Companion.CHANNELS_INPUT
import features.home.texts.HomeTexts.Companion.FORMAT_INPUT
import features.home.texts.HomeTexts.Companion.NO_AUDIO_INPUT
import features.home.texts.HomeTexts.Companion.OUTPUT_FILE
import features.home.texts.HomeTexts.Companion.PIXEL_FORMAT_INPUT
import features.home.texts.HomeTexts.Companion.QUALITY_INPUT
import features.home.texts.HomeTexts.Companion.RESOLUTION_INPUT
import features.home.texts.HomeTexts.Companion.SAMPLE_RATE_INPUT
import features.home.texts.HomeTexts.Companion.START_CONVERSION
import features.home.texts.HomeTexts.Companion.STOP_CONVERSION
import features.home.texts.HomeTexts.Companion.TITLE_PICK_FILE_TEXT
import features.home.texts.HomeTexts.Companion.VIDEO_MEDIA_TYPE
import features.home.texts.homeTexts
import ui.components.Selector
import ui.compositions.*
import ui.theme.AppTheme
import ui.theme.extensions.custom
import java.awt.FileDialog
import java.awt.Frame
import kotlin.math.roundToInt

private const val AUDIO_MEDIA = 0
private const val VIDEO_MEDIA = 1
private const val CONSTANT_COMPRESSION = 0
private const val VARIABLE_COMPRESSION = 1

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
    var selectedMediaType by remember { mutableStateOf(value = AUDIO_MEDIA) }
    val titlePickFile = texts.get(TITLE_PICK_FILE_TEXT)
    val compressions = listOf(texts.get(QUALITY_INPUT), texts.get(BIT_RATE_INPUT))
    var selectedCompression by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(selectedMediaType) {
        onEvent(SetFormat(null))
    }

    LaunchedEffect(state.format) {
        selectedCompression = if(state.format == null) null
        else {
            if(state.format != MediaFormat.EAC3) compressions[CONSTANT_COMPRESSION]
            else compressions[VARIABLE_COMPRESSION]
        }
    }

    Scaffold { innerPadding ->
        Row(modifier = Modifier.padding(innerPadding)) {
            Navigation(
                selected = selectedMediaType,
                onSelected = { selectedMediaType = it },
                onPickFile = { pickFile(titlePickFile)?.let { onEvent(HomeEvent.SetInputFile(it)) } }
            )

            VerticalDivider(color = MaterialTheme.colorScheme.surfaceContainer)

            Column(
                modifier = Modifier.padding(dimens.i),
                verticalArrangement = Arrangement.spacedBy(dimens.i)
            ) {

                Actions(
                    status = status,
                    enabled = state.inputFile != null,
                    onStart = { onEvent(OnStart) },
                    onStop = { onEvent(OnStop) }
                )

                // Common Settings
                Row(horizontalArrangement = Arrangement.spacedBy(dimens.d)) {
                    FormatInput(
                        value = state.format,
                        isVideo = selectedMediaType == VIDEO_MEDIA,
                        onValueChange = { onEvent(SetFormat(it)) }
                    )

                    Column {
                        Column(Modifier.selectableGroup()) {
                            compressions.forEachIndexed { index, text ->
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                        .selectable(
                                            selected = text == selectedCompression,
                                            onClick = { selectedCompression = text },
                                            enabled = if(index == 0) state.format != MediaFormat.EAC3 && state.format != null else state.format != MediaFormat.H265 && state.format != MediaFormat.AV1 && state.format != null,
                                            role = Role.RadioButton
                                        )
                                        .padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        val teste = remember { mutableStateOf(0.0f) }
                                        Row {
                                            RadioButton(
                                              selected = text == selectedCompression,
                                                onClick = {}
                                            )
                                            Text(
                                                text = text,
                                                style = TextStyle(fontSize = fontSizes.a),
                                                modifier = Modifier.padding(start = 16.dp)
                                            )
                                            Text(String.format("%.0f", teste.value))

                                        }

                                        if(index == CONSTANT_COMPRESSION) {
                                            Slider(
                                                value = teste.value,
                                                onValueChange = { teste.value = it.roundToInt().toFloat() },
                                                valueRange = ((if(index == 0) state.format?.minCrf?.toFloat() else state.format?.minVbr?.toFloat()) ?: 0f)..((if(index == 1) state.format?.maxCrf?.toFloat() else state.format?.maxVbr?.toFloat()) ?: 1f),
                                            )
                                        }
                                        else if(index == VARIABLE_COMPRESSION) {
                                            VariableCompressionInput(
                                                value = state.kbps,
                                                onValueChange = { onEvent(SetKbps(it)) }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Audio Settings
                if(selectedMediaType == AUDIO_MEDIA) {
                    Row(horizontalArrangement = Arrangement.spacedBy(dimens.f)) {
                        ChannelsInput(
                            value = state.channels,
                            onValueChange = { onEvent(SetChannels(it)) }
                        )

                        SampleRateInput(
                            value = state.sampleRate,
                            onValueChange = { onEvent(SetSampleRate(it)) }
                        )
                    }
                }
                // Video Settings
                else if(selectedMediaType == VIDEO_MEDIA) {
                    Row(horizontalArrangement = Arrangement.spacedBy(dimens.f)) {
                        val teste = remember { mutableStateOf(0.0f) }

                        Column {
                            ResolutionInput(
                                value = state.resolution,
                                onValueChange = { onEvent(SetResolution(it)) }
                            )

                            PixelFormatInput(
                                value = state.pixelFormat,
                                onValueChange = { onEvent(SetPixelFormat(it)) }
                            )
                        }

                        Column {
                            Text((if(state.format == MediaFormat.H265) H265Preset.fromId(teste.value.toInt())?.value else AV1Preset.fromId(teste.value.toInt())?.value) ?: "")
                            Slider(
                                value = teste.value,
                                enabled = state.format != null,
                                onValueChange = { teste.value = it.roundToInt().toFloat() },
                                valueRange = (when(state.format) {
                                    MediaFormat.H265 -> H265Preset.MIN_ID
                                    MediaFormat.AV1 -> AV1Preset.MIN_ID
                                    else -> 0
                                }).toFloat() .. (when(state.format) {
                                    MediaFormat.H265 -> H265Preset.MAX_ID
                                    MediaFormat.AV1 -> AV1Preset.MAX_ID
                                    else -> 1
                                }).toFloat()
                            )

                            Row {
                                Switch(
                                    checked = state.noAudio,
                                    onCheckedChange = {
                                        onEvent(SetNoAudio(it))
                                    }
                                )

                                Text(texts.get(NO_AUDIO_INPUT))
                            }
                        }
                    }
                }

                LogsView(state.logs)

                Progress(status)

                TextField(
                    value = state.outputFile ?: "",
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.inputFile != null,
                    colors = TextFieldDefaults.colors().custom(),
                    onValueChange = { onEvent(SetOutputFile(it)) },
                    label = { Text(text = texts.get(OUTPUT_FILE)) }
                )
            }
        }
    }
}

@Composable
private fun Navigation(selected: Int, onSelected: (Int) -> Unit, onPickFile: () -> Unit) {
    val mediaTypes = listOf(texts.get(AUDIO_MEDIA_TYPE), texts.get(VIDEO_MEDIA_TYPE))
    val icons = listOf(Icons.Rounded.MusicNote, Icons.Rounded.Videocam)

    NavigationRail {
        FloatingActionButton(
            elevation = FloatingActionButtonDefaults.elevation(0.dp),
            onClick = onPickFile
        ) {
            Icon(Icons.Rounded.FileOpen, contentDescription = null)
        }

        Spacer(modifier = Modifier.height(dimens.m))

        mediaTypes.forEachIndexed { index, item ->
            NavigationRailItem(
                icon = { Icon(icons[index], contentDescription = item) },
                label = { Text(item) },
                selected = selected == index,
                onClick = { onSelected(index) }
            )
        }
    }
}

@Composable
private fun Actions(status: HomeStatus, enabled: Boolean, onStart: () -> Unit, onStop: () -> Unit) {
    val isLoading = status is HomeStatus.Loading
    val startEnabled = !isLoading && status !is HomeStatus.Progress
    val stopEnabled = !isLoading
    val modifier =  Modifier
        .fillMaxWidth()
        .padding(bottom = dimens.f)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            FilledIconButton(
                enabled = startEnabled && enabled,
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
                enabled = stopEnabled && enabled,
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
private fun FormatInput(value: MediaFormat?, isVideo: Boolean, onValueChange: (MediaFormat) -> Unit) {
    var expanded by remember { mutableStateOf(value = false) }
    val medias = MediaFormat.getAll().filter { it.isVideo == isVideo }

    Selector(
        text = value?.text ?: "",
        label = texts.get(FORMAT_INPUT),
        expanded = expanded,
        onExpanded = { expanded = it }
    ) {
        medias.forEach { item ->
            DropdownMenuItem(
                text = { Text(item.text) },
                onClick = {
                    expanded = false
                    onValueChange(item)
                }
            )
        }
    }
}

@Composable
private fun ChannelsInput(value: Channels?, onValueChange: (Channels) -> Unit) {
    var expanded by remember { mutableStateOf(value = false) }

    Selector(
        text = value?.text ?: "",
        label = texts.get(CHANNELS_INPUT),
        expanded = expanded,
        onExpanded = { expanded = it }
    ) {
        Channels.getAll().forEach { item ->
            DropdownMenuItem(
                text = { Text(item.text) },
                onClick = {
                    expanded = false
                    onValueChange(item)
                }
            )
        }
    }
}

@Composable
private fun SampleRateInput(value: String?, onValueChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(value = false) }
    var text = ""

    if(value != null) text = "$value Hz"

    Selector(
        text = text,
        label = texts.get(SAMPLE_RATE_INPUT),
        expanded = expanded,
        onExpanded = { expanded = it }
    ) {
        SampleRates.getAll().forEach { item ->
            DropdownMenuItem(
                text = { Text(item) },
                onClick = {
                    expanded = false
                    onValueChange(item)
                }
            )
        }
    }
}

@Composable
private fun PixelFormatInput(value: PixelFormat?, onValueChange: (PixelFormat) -> Unit) {
    var expanded by remember { mutableStateOf(value = false) }
    var text = ""

    if(value != null) text = value.text

    Selector(
        text = text,
        label = texts.get(PIXEL_FORMAT_INPUT),
        expanded = expanded,
        onExpanded = { expanded = it }
    ) {
        PixelFormat.getAll().forEach { item ->
            DropdownMenuItem(
                text = { Text(item.text) },
                onClick = {
                    expanded = false
                    onValueChange(item)
                }
            )
        }
    }
}

@Composable
private fun ResolutionInput(value: Resolution?, onValueChange: (Resolution) -> Unit) {
    var expanded by remember { mutableStateOf(value = false) }
    var text = ""

    if(value != null) text = value.text

    Selector(
        text = text,
        label = texts.get(RESOLUTION_INPUT),
        expanded = expanded,
        onExpanded = { expanded = it }
    ) {
        Resolution.getAll().forEach { item ->
            DropdownMenuItem(
                text = { Text(item.text) },
                onClick = {
                    expanded = false
                    onValueChange(item)
                }
            )
        }
    }
}

@Composable
private fun VariableCompressionInput(value: Kbps?, onValueChange: (Kbps) -> Unit) {
    var expanded by remember { mutableStateOf(value = false) }
    var text = ""

    if(value != null) text = value.value

    Selector(
        text = text,
        label = texts.get(RESOLUTION_INPUT),
        expanded = expanded,
        onExpanded = { expanded = it }
    ) {
        Kbps.getAll().forEach { item ->
            DropdownMenuItem(
                text = { Text(item.value) },
                onClick = {
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