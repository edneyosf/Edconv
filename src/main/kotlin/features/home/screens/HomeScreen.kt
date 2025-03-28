package features.home.screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import features.home.events.HomeEvent.SetCrf
import features.home.events.HomeEvent.SetVbr
import features.home.managers.HomeManager
import features.home.states.HomeState
import features.home.states.HomeStatus
import features.home.texts.HomeTexts
import features.home.texts.HomeTexts.Companion.AUDIO_MEDIA_TYPE_TXT
import features.home.texts.HomeTexts.Companion.BIT_RATE_INPUT_TXT
import features.home.texts.HomeTexts.Companion.CHANNELS_INPUT_TXT
import features.home.texts.HomeTexts.Companion.FORMAT_INPUT_TXT
import features.home.texts.HomeTexts.Companion.NO_AUDIO_INPUT_TXT
import features.home.texts.HomeTexts.Companion.OUTPUT_FILE_TXT
import features.home.texts.HomeTexts.Companion.PIXEL_FORMAT_INPUT_TXT
import features.home.texts.HomeTexts.Companion.PRESET_INPUT_TXT
import features.home.texts.HomeTexts.Companion.QUALITY_INPUT_TXT
import features.home.texts.HomeTexts.Companion.RESOLUTION_INPUT_TXT
import features.home.texts.HomeTexts.Companion.SAMPLE_RATE_INPUT_TXT
import features.home.texts.HomeTexts.Companion.START_CONVERSION_TXT
import features.home.texts.HomeTexts.Companion.STOP_CONVERSION_TXT
import features.home.texts.HomeTexts.Companion.TITLE_PICK_FILE_TXT
import features.home.texts.HomeTexts.Companion.VIDEO_MEDIA_TYPE_TXT
import features.home.texts.homeTexts
import ui.components.Selector
import ui.compositions.*
import ui.theme.AppTheme
import ui.components.extensions.custom
import java.awt.FileDialog
import java.awt.Frame
import kotlin.math.max
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
    val isDarkTheme = isSystemInDarkTheme()
    val status = state.status
    var selectedMediaType by remember { mutableStateOf(value = AUDIO_MEDIA) }
    val isAudio = selectedMediaType == AUDIO_MEDIA
    val isVideo = selectedMediaType == VIDEO_MEDIA
    val titlePickFile = texts.get(TITLE_PICK_FILE_TXT)
    val compressions = listOf(texts.get(QUALITY_INPUT_TXT), texts.get(BIT_RATE_INPUT_TXT))
    var selectedCompression by remember { mutableStateOf<Int?>(value = null) }
    var quality by remember { mutableStateOf<Int?>(value = null) }
    var preset by remember { mutableStateOf<Int?>(value = null) }

    // Media Type
    LaunchedEffect(selectedMediaType) {
        onEvent(SetFormat(null))
    }
    // Format
    LaunchedEffect(state.format) { state.format.let {
            quality = 0
            preset = 0
            selectedCompression = if(it == null) null
            else {
                if(it != MediaFormat.EAC3) CONSTANT_COMPRESSION
                else VARIABLE_COMPRESSION
            }
        }
    }
    // Quality
    LaunchedEffect(quality) {
        if(isAudio) onEvent(SetVbr(quality))
        else if(isVideo) quality?.let { onEvent(SetCrf(it)) }
    }
    // Preset
    LaunchedEffect(preset) {
        onEvent(HomeEvent.SetPreset(preset))
    }

    Scaffold { innerPadding ->
        Row(modifier = Modifier.padding(innerPadding)) {
            Navigation(
                selected = selectedMediaType,
                onSelected = { selectedMediaType = it },
                hasInputFile = state.inputFile != null,
                onPickFile = { pickFile(titlePickFile)?.let { onEvent(HomeEvent.SetInputFile(it)) } }
            )

            VerticalDivider(
                color = if(isDarkTheme) MaterialTheme.colorScheme.surfaceContainer
                else MaterialTheme.colorScheme.surfaceDim
            )

            Column(
                modifier = Modifier.padding(dimens.i),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimens.i)
            ) {
                Actions(
                    status = status,
                    enabled = state.inputFile != null,
                    onStart = { onEvent(OnStart) },
                    onStop = { onEvent(OnStop) }
                )

                // Common Settings
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimens.m),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FormatInput(
                        value = state.format,
                        isVideo = isVideo,
                        onValueChange = { onEvent(SetFormat(it)) }
                    )

                    Column {
                        if(isAudio) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = CONSTANT_COMPRESSION == selectedCompression,
                                    onClick = { selectedCompression = CONSTANT_COMPRESSION }
                                )

                                Text(
                                    text = compressions[CONSTANT_COMPRESSION],
                                    style = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )

                                Spacer(modifier = Modifier.width(dimens.d))

                                Text(
                                    text = quality?.toString() ?: "0",
                                    style = TextStyle(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = fontSizes.c
                                    )
                                )
                            }
                        }
                        else if(isVideo) {
                            Row {
                                Text(
                                    text = compressions[CONSTANT_COMPRESSION],
                                    style = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )

                                Spacer(modifier = Modifier.width(dimens.d))

                                Text(
                                    text = quality?.toString() ?: "0",
                                    style = TextStyle(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = fontSizes.c
                                    )
                                )
                            }
                        }
                        val minRate = when(state.format) {
                            MediaFormat.AAC -> state.format.minVbr?.toFloat()
                            MediaFormat.H265, MediaFormat.AV1 -> state.format.minCrf?.toFloat()
                            else -> 0f
                        } ?: 0f
                        val maxRate = when(state.format) {
                            MediaFormat.AAC -> state.format.maxVbr?.toFloat()
                            MediaFormat.H265, MediaFormat.AV1 -> state.format.maxCrf?.toFloat()
                            else -> 0f
                        } ?: 0f

                        // TODO
                        Slider(
                            value = quality?.toFloat() ?: 0.0f,
                            modifier = Modifier.width(320.dp),
                            enabled = state.format != null && selectedCompression == CONSTANT_COMPRESSION,
                            colors = SliderDefaults.custom(),
                            onValueChange = { quality = it.roundToInt() },
                            valueRange = minRate .. maxRate,
                        )
                    }

                    if(isAudio) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = VARIABLE_COMPRESSION == selectedCompression,
                                onClick = { selectedCompression = VARIABLE_COMPRESSION }
                            )
                            VariableCompressionInput(
                                value = state.kbps,
                                enabled = VARIABLE_COMPRESSION == selectedCompression,
                                onValueChange = { onEvent(SetKbps(it)) }
                            )
                        }
                    }
                }

                // Audio Settings
                if(isAudio) {
                    Row(horizontalArrangement = Arrangement.spacedBy(dimens.m), verticalAlignment = Alignment.CenterVertically) {
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
                else if(isVideo) {
                    Row(horizontalArrangement = Arrangement.spacedBy(dimens.m), verticalAlignment = Alignment.CenterVertically) {
                        ResolutionInput(
                            value = state.resolution,
                            onValueChange = { onEvent(SetResolution(it)) }
                        )

                        PixelFormatInput(
                            value = state.pixelFormat,
                            onValueChange = { onEvent(SetPixelFormat(it)) }
                        )

                        Column {
                            var presetText = ""

                            if(state.format == MediaFormat.H265) {
                                preset?.let {
                                    presetText = H265Preset.fromId(it)?.value ?: ""
                                }
                            }
                            else if(state.format == MediaFormat.AV1) {
                                preset?.let {
                                    presetText = AV1Preset.fromId(it)?.value ?: ""
                                }
                            }

                            Row {
                                Text(text = texts.get(PRESET_INPUT_TXT), style = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant))
                                Spacer(modifier = Modifier.width(dimens.d))
                                Text(
                                    presetText,
                                    style = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = fontSizes.c))
                            }
                            val minPreset = when(state.format) {
                                MediaFormat.H265 -> H265Preset.MIN_ID.toFloat()
                                MediaFormat.AV1 -> AV1Preset.MIN_ID.toFloat()
                                else -> 0f
                            }
                            val maxPreset = when(state.format) {
                                MediaFormat.H265 -> H265Preset.MAX_ID.toFloat()
                                MediaFormat.AV1 -> AV1Preset.MAX_ID.toFloat()
                                else -> 0f
                            }

                            Slider(
                                value = preset?.toFloat() ?: 0f,
                                modifier = Modifier.width(320.dp),
                                enabled = state.format != null,
                                colors = SliderDefaults.custom(),
                                onValueChange = { preset = it.roundToInt() },
                                valueRange = minPreset .. maxPreset
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Switch(
                                checked = state.noAudio,
                                onCheckedChange = {
                                    onEvent(SetNoAudio(it))
                                }
                            )
                            Spacer(modifier = Modifier.width(dimens.f))
                            Text(texts.get(NO_AUDIO_INPUT_TXT), style = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant))
                        }
                    }
                }
                //TODO
                LogsView(state.logs)

                Progress(status)

                TextField(
                    value = state.outputFile ?: "",
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.inputFile != null,
                    colors = TextFieldDefaults.colors().custom(),
                    onValueChange = { onEvent(SetOutputFile(it)) },
                    label = { Text(text = texts.get(OUTPUT_FILE_TXT)) }
                )
            }
        }
    }
}

@Composable
private fun Navigation(selected: Int, hasInputFile: Boolean, onSelected: (Int) -> Unit, onPickFile: () -> Unit) {
    val mediaTypes = listOf(texts.get(AUDIO_MEDIA_TYPE_TXT), texts.get(VIDEO_MEDIA_TYPE_TXT))
    val icons = listOf(Icons.Rounded.MusicNote, Icons.Rounded.Videocam)

    NavigationRail {
        FloatingActionButton(
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp, focusedElevation = 0.dp),
            onClick = onPickFile
        ) {
            BadgedBox(
                badge = {
                    if(hasInputFile) Badge()
                }
            ) {
                Icon(Icons.Rounded.FileOpen, contentDescription = texts.get(TITLE_PICK_FILE_TXT))
            }
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
                    contentDescription = texts.get(START_CONVERSION_TXT)
                )
            }

            Text(
                text = texts.get(START_CONVERSION_TXT),
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
                    contentDescription = texts.get(STOP_CONVERSION_TXT)
                )
            }

            Text(
                text = texts.get(STOP_CONVERSION_TXT),
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
        label = texts.get(FORMAT_INPUT_TXT),
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
        label = texts.get(CHANNELS_INPUT_TXT),
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
private fun SampleRateInput(value: SampleRate?, onValueChange: (SampleRate) -> Unit) {
    var expanded by remember { mutableStateOf(value = false) }

    Selector(
        text = value?.text ?: "",
        label = texts.get(SAMPLE_RATE_INPUT_TXT),
        expanded = expanded,
        onExpanded = { expanded = it }
    ) {
        SampleRate.getAll().forEach { item ->
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
private fun PixelFormatInput(value: PixelFormat?, onValueChange: (PixelFormat) -> Unit) {
    var expanded by remember { mutableStateOf(value = false) }

    Selector(
        text = value?.text ?: "",
        label = texts.get(PIXEL_FORMAT_INPUT_TXT),
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

    Selector(
        text = value?.text ?: "",
        label = texts.get(RESOLUTION_INPUT_TXT),
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
private fun VariableCompressionInput(value: Kbps?, enabled: Boolean = true, onValueChange: (Kbps) -> Unit) {
    var expanded by remember { mutableStateOf(value = false) }

    Selector(
        text = value?.text ?: "",
        label = texts.get(BIT_RATE_INPUT_TXT),
        enabled = enabled,
        expanded = expanded,
        onExpanded = { expanded = it }
    ) {
        Kbps.getAll().forEach { item ->
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

// Previews

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