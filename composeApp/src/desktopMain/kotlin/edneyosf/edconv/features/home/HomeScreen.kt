package edneyosf.edconv.features.home

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import edneyosf.edconv.core.utils.FileUtils
import edneyosf.edconv.edconv.av1.AV1Preset
import edneyosf.edconv.edconv.common.*
import edneyosf.edconv.edconv.core.data.MediaData
import edneyosf.edconv.edconv.h265.H265Preset
import edneyosf.edconv.features.home.events.HomeEvent
import edneyosf.edconv.features.home.events.HomeEvent.*
import edneyosf.edconv.features.home.managers.HomeManager
import edneyosf.edconv.features.home.states.HomeState
import edneyosf.edconv.features.home.states.HomeStatus
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Companion.AUDIO_MEDIA_TYPE
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Companion.BIT_RATE_INPUT
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Companion.CHANNELS_INPUT
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Companion.DEFAULT_ERROR
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Companion.FORMAT_INPUT
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Companion.NO_AUDIO_INPUT
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Companion.OUTPUT_FILE
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Companion.PIXEL_FORMAT_INPUT
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Companion.PRESET_INPUT
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Companion.QUALITY_INPUT
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Companion.RESOLUTION_INPUT
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Companion.SAMPLE_RATE_INPUT
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Companion.START_CONVERSION
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Companion.STOP_CONVERSION
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Companion.TITLE_PICK_FILE
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Companion.VIDEO_MEDIA_TYPE
import edneyosf.edconv.features.home.strings.homeScreenStrings
import edneyosf.edconv.features.settings.SettingsDialog
import edneyosf.edconv.ui.components.Selector
import edneyosf.edconv.ui.components.extensions.custom
import edneyosf.edconv.ui.compositions.*
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview
import kotlin.math.roundToInt

@Composable
fun HomeScreen() {
    val scope = rememberCoroutineScope()
    val manager = remember { HomeManager(scope) }
    val state by manager.state

    state.Dialogs(onEvent = manager::onEvent)

    CompositionLocalProvider(stringsComp provides homeScreenStrings) {
        state.Content(onEvent = manager::onEvent)
    }
}

//TODO
@Composable
private fun HomeState.Content(onEvent: (HomeEvent) -> Unit) {
    val isDarkTheme = isSystemInDarkTheme()
    var mediaType by remember { mutableStateOf(value = MediaType.AUDIO) }
    val titlePickFile = strings.get(TITLE_PICK_FILE)
    val compressions = listOf(strings.get(QUALITY_INPUT), strings.get(BIT_RATE_INPUT))
    var quality by remember { mutableStateOf<Int?>(value = null) }
    var preset by remember { mutableStateOf<Int?>(value = null) }

    // Input
    LaunchedEffect(input) {
        input?.let {
            mediaType = it.type
        }
    }
    // Media Type
    LaunchedEffect(mediaType) {
        onEvent(SetCodec(null))
    }
    // Format
    LaunchedEffect(codec) { codec.let {
            quality = 0
            preset = 0
        }
    }
    // Quality
    LaunchedEffect(quality) {
        if(mediaType == MediaType.AUDIO) onEvent(SetVbr(quality))
        else quality?.let { onEvent(SetCrf(it)) }
    }
    // Preset
    LaunchedEffect(preset) {
        if(codec == Codec.AV1) {
            AV1Preset.fromId(preset ?: -1)?.value?.let{
                onEvent(SetPreset(it))
            }
        }
        else if(codec == Codec.H265) {
            H265Preset.fromId(preset ?: -1)?.value?.let{
                onEvent(SetPreset(it))
            }
        }
    }
    // Compression
    LaunchedEffect(compression) {
        compression?.let {
            if(it == CompressionType.CONSTANT) {
                onEvent(SetVbr(null))
            }
            else {
                onEvent(SetBitrate(null))
            }
        }
    }

    Scaffold { innerPadding ->
        Row(modifier = Modifier.padding(innerPadding)) {
            Navigation(
                selected = input?.type,
                onSelected = { mediaType = it },
                input = input,
                onPickFile = { FileUtils.pickFile(titlePickFile)?.let { onEvent(SetInput(it)) } },
                onSettings = {
                    onEvent(SetStatus(HomeStatus.Settings))
                }
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
                    enabled = input != null && !output.isNullOrBlank() && codec != null,
                    onStart = {
                        onEvent(OnStart())
                    },
                    onStop = { onEvent(OnStop) }
                )

                // Common Settings
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimens.m),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FormatInput(
                        value = codec,
                        mediaType = mediaType,
                        onValueChange = { onEvent(SetCodec(it)) }
                    )

                    Column {
                        if(mediaType == MediaType.AUDIO) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = CompressionType.CONSTANT == compression,
                                    onClick = { onEvent(SetCompression(CompressionType.CONSTANT)) }
                                )

                                Text(
                                    text = compressions[CompressionType.CONSTANT.index],
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
                        else {
                            Row {
                                Text(
                                    text = compressions[CompressionType.CONSTANT.index],
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
                        val minRate = when(codec) {
                            Codec.AAC_FDK -> codec.minVbr?.toFloat()
                            Codec.H265, Codec.AV1 -> codec.minCrf?.toFloat()
                            else -> 0f
                        } ?: 0f
                        val maxRate = when(codec) {
                            Codec.AAC_FDK -> codec.maxVbr?.toFloat()
                            Codec.H265, Codec.AV1 -> codec.maxCrf?.toFloat()
                            else -> 0f
                        } ?: 0f

                        // TODO
                        Slider(
                            value = quality?.toFloat() ?: 0.0f,
                            modifier = Modifier.width(320.dp),
                            enabled = codec != null && compression == CompressionType.CONSTANT,
                            colors = SliderDefaults.custom(),
                            onValueChange = { quality = it.roundToInt() },
                            valueRange = minRate .. maxRate,
                        )
                    }

                    if(mediaType == MediaType.AUDIO) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = CompressionType.VARIABLE == compression,
                                onClick = { onEvent(SetCompression(CompressionType.VARIABLE)) }
                            )
                            VariableCompressionInput(
                                value = bitrate,
                                enabled = CompressionType.VARIABLE == compression,
                                onValueChange = { onEvent(SetBitrate(it)) }
                            )
                        }
                    }
                }

                // Audio Settings
                if(mediaType == MediaType.AUDIO) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ChannelsInput(
                            value = channels,
                            onValueChange = { onEvent(SetChannels(it)) }
                        )

                        SampleRateInput(
                            value = sampleRate,
                            onValueChange = { onEvent(SetSampleRate(it)) }
                        )
                    }
                }
                // Video Settings
                else {
                    Row(horizontalArrangement = Arrangement.spacedBy(dimens.m), verticalAlignment = Alignment.CenterVertically) {
                        ResolutionInput(
                            value = resolution,
                            onValueChange = { onEvent(SetResolution(it)) }
                        )

                        PixelFormatInput(
                            value = pixelFormat,
                            onValueChange = { onEvent(SetPixelFormat(it)) }
                        )

                        Column {
                            var presetText = ""

                            if(codec == Codec.H265) {
                                preset?.let {
                                    presetText = H265Preset.fromId(it)?.value ?: ""
                                }
                            }
                            else if(codec == Codec.AV1) {
                                preset?.let {
                                    presetText = AV1Preset.fromId(it)?.value ?: ""
                                }
                            }

                            Row {
                                Text(text = strings.get(PRESET_INPUT), style = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant))
                                //Spacer(modifier = Modifier.width(dimens.d))
                                Text(
                                    presetText,
                                    style = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = fontSizes.c))
                            }
                            val minPreset = when(codec) {
                                Codec.H265 -> H265Preset.MIN_ID.toFloat()
                                Codec.AV1 -> AV1Preset.MIN_ID.toFloat()
                                else -> 0f
                            }
                            val maxPreset = when(codec) {
                                Codec.H265 -> H265Preset.MAX_ID.toFloat()
                                Codec.AV1 -> AV1Preset.MAX_ID.toFloat()
                                else -> 0f
                            }

                            Slider(
                                value = preset?.toFloat() ?: 0f,
                                modifier = Modifier.width(320.dp),
                                enabled = codec != null,
                                colors = SliderDefaults.custom(),
                                onValueChange = { preset = it.roundToInt() },
                                valueRange = minPreset .. maxPreset
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Switch(
                                checked = noAudio,
                                onCheckedChange = {
                                    onEvent(SetNoAudio(it))
                                }
                            )
                            Spacer(modifier = Modifier.width(dimens.f))
                            Text(strings.get(NO_AUDIO_INPUT), style = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant))
                        }
                    }
                }
                //TODO
                Row(modifier = Modifier.weight(1f)) {
                    LogsView(logs)

                    TextField(
                        value = cmd,
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        colors = TextFieldDefaults.colors().custom(),
                        onValueChange = { onEvent(SetCmd(it)) },
                        label = { Text(text = "Comando") }
                    )
                }

                Progress(status)

                TextField(
                    value = output ?: "",
                    modifier = Modifier.fillMaxWidth(),
                    enabled = input != null,
                    colors = TextFieldDefaults.colors().custom(),
                    onValueChange = { onEvent(SetOutput(it)) },
                    label = { Text(text = strings.get(OUTPUT_FILE)) }
                )
            }
        }
    }
}

@Composable
private fun HomeState.Dialogs(onEvent: (HomeEvent) -> Unit) {
    when (this.status) {
        is HomeStatus.Settings -> SettingsDialog { onEvent(SetStatus(HomeStatus.Initial)) }

        is HomeStatus.Error -> {
            ErrorDialog(
                message = status.message ?: strings[DEFAULT_ERROR],
                onFinish = { onEvent(SetStatus(HomeStatus.Initial)) }
            )
        }

        is HomeStatus.Complete -> {
            CompleteDialog(
                startTime = status.startTime,
                finishTime = status.finishTime,
                duration = status.duration,
                onFinish = { onEvent(SetStatus(HomeStatus.Initial)) }
            )
        }

        is HomeStatus.FileExists -> {
            OverwriteFileDialog(
                onCancel = { onEvent(SetStatus(HomeStatus.Initial)) },
                onConfirmation = { onEvent(OnStart(overwrite = true)) }
            )
        }

        else -> Unit
    }
}

//TODO
@Composable
private fun Navigation(selected: MediaType?, input: MediaData?, onSelected: (MediaType) -> Unit, onPickFile: () -> Unit, onSettings: () -> Unit) {
    val mediaTypes = listOf(strings.get(AUDIO_MEDIA_TYPE), strings.get(VIDEO_MEDIA_TYPE))
    val icons = listOf(Icons.Rounded.MusicNote, Icons.Rounded.Videocam)

    NavigationRail {
        //TODO desabiliar carregando
        FloatingActionButton(
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp, focusedElevation = 0.dp),
            onClick = onPickFile
        ) {
            BadgedBox(
                badge = {
                    if(input != null) Badge()
                }
            ) {
                Icon(Icons.Rounded.FileOpen, contentDescription = strings.get(TITLE_PICK_FILE))
            }
        }

        Spacer(modifier = Modifier.height(dimens.m))

        NavigationRailItem(
            icon = { Icon(icons[0], contentDescription = mediaTypes[0]) },
            label = { Text(mediaTypes[0]) },
            enabled = input?.type == MediaType.AUDIO,
            selected = selected?.index == 0,
            onClick = {
                MediaType.fromIndex(0)?.let {
                    onSelected(it)
                }
            }
        )
        NavigationRailItem(
            icon = { Icon(icons[1], contentDescription = mediaTypes[1]) },
            label = { Text(mediaTypes[1]) },
            enabled = input?.type == MediaType.VIDEO,
            selected = selected?.index == 1,
            onClick = {
                MediaType.fromIndex(1)?.let {
                    onSelected(it)
                }
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = {
                onSettings()
            }
        ) {
            Icon(Icons.Rounded.Settings, contentDescription = null)
        }
    }
}

//TODO
@Composable
private fun Actions(status: HomeStatus, enabled: Boolean, onStart: () -> Unit, onStop: () -> Unit) {
    val isLoading = status is HomeStatus.Loading
    val startEnabled = !isLoading && status !is HomeStatus.Progress
    val stopEnabled = status is HomeStatus.Progress
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
                    contentDescription = strings.get(START_CONVERSION)
                )
            }

            Text(
                text = strings.get(START_CONVERSION),
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
                    contentDescription = strings.get(STOP_CONVERSION)
                )
            }

            Text(
                text = strings.get(STOP_CONVERSION),
                style = TextStyle(fontSize = fontSizes.a)
            )
        }
    }
}

@Composable
private fun FormatInput(value: Codec?, mediaType: MediaType, onValueChange: (Codec) -> Unit) {
    var expanded by remember { mutableStateOf(value = false) }
    val medias = Codec.getAll().filter { it.mediaType == mediaType }

    Selector(
        text = value?.text ?: "",
        label = strings.get(FORMAT_INPUT),
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
        label = strings.get(CHANNELS_INPUT),
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
        label = strings.get(SAMPLE_RATE_INPUT),
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
        label = strings.get(PIXEL_FORMAT_INPUT),
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
        label = strings.get(RESOLUTION_INPUT),
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

//TODO
@Composable
private fun VariableCompressionInput(value: Bitrate?, enabled: Boolean = true, onValueChange: (Bitrate) -> Unit) {
    var expanded by remember { mutableStateOf(value = false) }

    Selector(
        text = value?.text ?: "",
        label = strings.get(BIT_RATE_INPUT),
        enabled = enabled,
        expanded = expanded,
        onExpanded = { expanded = it }
    ) {
        Bitrate.getAllForAudio().forEach { item ->
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

//TODO
@Composable
private fun RowScope.LogsView(text: String) {
    val logsScroll = rememberScrollState()
    val modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .verticalScroll(state = logsScroll)

    LaunchedEffect(text) { logsScroll.animateScrollTo(logsScroll.maxValue) }

    Column(modifier = Modifier
        .weight(2f)) {
        Text("Logs", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(dimens.f))
        Card(modifier = modifier) {
            SelectionContainer(modifier = Modifier.padding(dimens.i)) {
                Text(text, style = MaterialTheme.typography.labelMedium)
            }
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
        if(status is HomeStatus.Loading) LinearProgress()
        else if(status is HomeStatus.Progress) {
            val text = "${String.format("%.2f", status.percentage)}% (${status.speed})"

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

@Composable
private fun DefaultPreview() {
    CompositionLocalProvider(languageComp provides language) {
        HomeState.default().Content(onEvent = {})
    }
}

@Preview
@Composable
private fun EnglishLight() = EnglishLightPreview { DefaultPreview() }

@Preview
@Composable
private fun EnglishDark() = EnglishDarkPreview { DefaultPreview() }

@Preview
@Composable
private fun PortugueseLight() = PortugueseLightPreview { DefaultPreview() }

@Preview
@Composable
private fun PortugueseDark() = PortugueseDarkPreview { DefaultPreview() }