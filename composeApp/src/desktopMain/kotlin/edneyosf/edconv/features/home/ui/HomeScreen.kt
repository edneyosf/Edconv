package edneyosf.edconv.features.home.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edneyosf.edconv.core.common.compose.LaunchedEffected
import edneyosf.edconv.core.utils.FileUtils
import edneyosf.edconv.edconv.common.*
import edneyosf.edconv.features.home.events.HomeEvent
import edneyosf.edconv.features.home.events.HomeEvent.*
import edneyosf.edconv.features.home.managers.HomeManager
import edneyosf.edconv.features.home.states.HomeState
import edneyosf.edconv.features.home.states.HomeStatus
import edneyosf.edconv.features.home.strings.homeScreenStrings
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Keys.*
import edneyosf.edconv.ui.components.Selector
import edneyosf.edconv.ui.components.extensions.custom
import edneyosf.edconv.ui.compositions.*
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview

@Composable
fun HomeScreen() {
    val scope = rememberCoroutineScope()
    val manager = remember { HomeManager(scope) }
    val state by manager.state

    CompositionLocalProvider(stringsComp provides homeScreenStrings) {
        state.Dialogs(onEvent = manager::onEvent)
        state.Content(onEvent = manager::onEvent)
    }
}

@Composable
private fun HomeState.Content(onEvent: (HomeEvent) -> Unit) {
    val titlePickFile = strings[TITLE_PICK_FILE]
    var mediaType by remember { mutableStateOf<MediaType?>(value = null) }

    LaunchedEffected(input) { mediaType = it?.type }

    LaunchedEffected(codec) {
        onEvent(SetBitrate(bitrate = it?.defaultBitrate))
        onEvent(SetVbr(it?.defaultVBR))
        onEvent(SetCrf(it?.defaultCRF))
        onEvent(SetPreset(it?.defaultPreset))
        onEvent(SetCompression(compression = it?.compressions?.firstOrNull()))
    }

    LaunchedEffected(mediaType) {
        val defaultCodec = when(it) {
            MediaType.AUDIO -> Codec.OPUS
            MediaType.VIDEO -> Codec.AV1
            else -> null
        }

        onEvent(SetCodec(codec = defaultCodec))
    }

    Scaffold { innerPadding ->
        Row(modifier = Modifier.padding(innerPadding)) {
            HomeNavigation(
                selected = mediaType,
                inputMediaType = input?.type,
                onSelected = { mediaType = it },
                pickFileEnabled = status !is HomeStatus.Loading,
                onPickFile = { FileUtils.pickFile(titlePickFile)?.let { onEvent(SetInput(it)) } },
                onSettings = { onEvent(SetStatus(HomeStatus.Settings)) }
            )
            Column(
                modifier = Modifier.padding(dimens.md),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimens.xl)
            ) {
                Actions(
                    startEnabled = canStart(mediaType),
                    onStart = { onEvent(OnStart()) },
                    onStop = { onEvent(OnStop) }
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimens.xl),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FormatInput(
                        value = codec,
                        mediaType = mediaType,
                        onValueChange = { onEvent(SetCodec(it)) }
                    )
                    VBRInput(
                        onClick = { onEvent(SetCompression(CompressionType.VBR)) },
                        onValueChange = { onEvent(SetVbr(it)) }
                    )
                    CRFInput(
                        onClick = { onEvent(SetCompression(CompressionType.CRF)) },
                        onValueChange = { onEvent(SetCrf(it)) }
                    )
                    CBRInput(
                        mediaType = mediaType,
                        onClick = { onEvent(SetCompression(CompressionType.CBR)) },
                        onValueChange = { onEvent(SetBitrate(it)) }
                    )
                }
                if(mediaType == MediaType.AUDIO) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimens.xl)
                    ) {
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
                else if(mediaType == MediaType.VIDEO) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimens.xl)
                    ) {
                        ResolutionInput(
                            value = resolution,
                            onValueChange = { onEvent(SetResolution(it)) }
                        )
                        PixelFormatInput(
                            value = pixelFormat,
                            onValueChange = { onEvent(SetPixelFormat(it)) }
                        )
                        PresetInput(
                            mediaType = mediaType,
                            onValueChange = { onEvent(SetPreset(it)) }
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimens.xl)
                    ) {
                        CheckboxInput(
                            checked = noMetadata,
                            label = strings[NO_METADATA_INPUT],
                            onCheckedChange = { onEvent(SetNoMetadata(it)) }
                        )
                        CheckboxInput(
                            checked = noAudio,
                            label = strings[NO_AUDIO_INPUT],
                            onCheckedChange = { onEvent(SetNoAudio(it)) }
                        )
                        CheckboxInput(
                            checked = noSubtitle,
                            label = strings[NO_SUBTITLE_INPUT],
                            onCheckedChange = { onEvent(SetNoSubtitle(it)) }
                        )
                    }
                }
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(dimens.md)
                ) {
                    LogsView(logs)
                    TextField(
                        value = cmd,
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        colors = TextFieldDefaults.colors().custom(),
                        onValueChange = { onEvent(SetCmd(it)) },
                        label = { Text(strings[COMMAND_INPUT]) }
                    )
                }
                Progress(status)
                TextField(
                    value = output ?: "",
                    modifier = Modifier.fillMaxWidth(),
                    enabled = input != null,
                    colors = TextFieldDefaults.colors().custom(),
                    onValueChange = { onEvent(SetOutput(it)) },
                    label = { Text(strings[OUTPUT_FILE]) }
                )
            }
        }
    }
}

@Composable
private fun HomeState.Actions(startEnabled: Boolean, onStart: () -> Unit, onStop: () -> Unit) {
    val stopEnabled = status is HomeStatus.Progress
    val modifier =  Modifier
        .fillMaxWidth()
        .padding(bottom = dimens.sm)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            FilledIconButton(
                enabled = startEnabled,
                onClick = onStart
            ) {
                Icon(
                    imageVector = Icons.Rounded.PlayArrow,
                    contentDescription = strings[START_CONVERSION]
                )
            }
            Text(
                text = strings[START_CONVERSION],
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.width(dimens.md))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            FilledTonalIconButton(
                enabled = stopEnabled,
                onClick = onStop
            ) {
                Icon(
                    imageVector = Icons.Rounded.Stop,
                    contentDescription = strings[STOP_CONVERSION]
                )
            }
            Text(
                text = strings[STOP_CONVERSION],
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun FormatInput(value: Codec?, mediaType: MediaType?, onValueChange: (Codec) -> Unit) {
    var expanded by remember { mutableStateOf(value = false) }
    val medias = Codec.getAll().filter { it.mediaType == mediaType }

    Selector(
        text = value?.text ?: "",
        label = strings[FORMAT_INPUT],
        enabled = medias.isNotEmpty(),
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
private fun HomeState.VBRInput(onClick: () -> Unit,  onValueChange: (Int) -> Unit) {
    if(codec?.compressions?.contains(CompressionType.VBR) == true && vbr != null) {
        val minVBR = codec.minVBR
        val maxVBR = codec.maxVBR

        if(minVBR != null && maxVBR != null) {
            val isVBR = CompressionType.VBR == compression
            val valueColor = MaterialTheme.colorScheme.run { if(isVBR) onSurface else onSurface.copy(alpha = 0.38f) }

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    if(codec.compressions.size > 1) RadioButton(selected = isVBR, onClick = onClick)
                    Text(
                        text = strings[VBR_INPUT],
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Spacer(modifier = Modifier.width(dimens.xs))
                    Text(
                        text = vbr.toString(),
                        style = TextStyle(
                            color = valueColor,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                Box(modifier = Modifier.padding(start = 14.dp)) {
                    Slider(
                        value = vbr.toFloat(),
                        modifier = Modifier.width(220.dp),
                        enabled = isVBR,
                        steps = maxVBR - 2,
                        onValueChange = { onValueChange(it.toInt()) },
                        valueRange = minVBR.toFloat() .. maxVBR.toFloat()
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeState.CBRInput(mediaType: MediaType?, onClick: () -> Unit,  onValueChange: (Bitrate) -> Unit) {
    if(codec?.compressions?.contains(CompressionType.CBR) == true) {
        var expanded by remember { mutableStateOf(value = false) }
        val isCBR = CompressionType.CBR == compression

        Row(verticalAlignment = Alignment.CenterVertically) {
            if(codec.compressions.size > 1) RadioButton(selected = isCBR, onClick = onClick)
            Selector(
                text = bitrate?.text ?: "",
                label = strings[CBR_INPUT],
                enabled = isCBR,
                expanded = expanded,
                onExpanded = { expanded = it }
            ) {
                Bitrate.getAllByMediaType(mediaType).forEach { item ->
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
    }
}

@Composable
private fun HomeState.CRFInput(onClick: () -> Unit,  onValueChange: (Int) -> Unit) {
    if(codec?.compressions?.contains(CompressionType.CRF) == true && crf != null) {
        val minCRF = codec.minCRF
        val maxCRF = codec.maxCRF

        if(minCRF != null && maxCRF != null) {
            val isCRF = CompressionType.CRF == compression

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    if(codec.compressions.size > 1) RadioButton(selected = isCRF, onClick = onClick)
                    Text(
                        text = strings[CRF_INPUT],
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Spacer(modifier = Modifier.width(dimens.xs))
                    Text(
                        text = crf.toString(),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                Box(modifier = Modifier.padding(start = 14.dp)) {
                    Slider(
                        value = crf.toFloat(),
                        modifier = Modifier.width(320.dp),
                        enabled = isCRF,
                        onValueChange = { onValueChange(it.toInt()) },
                        valueRange = minCRF.toFloat() .. maxCRF.toFloat()
                    )
                }
            }
        }
    }
}

@Composable
private fun ChannelsInput(value: Channels?, onValueChange: (Channels) -> Unit) {
    var expanded by remember { mutableStateOf(value = false) }

    Selector(
        text = value?.text ?: "",
        label = strings[CHANNELS_INPUT],
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
        label = strings[SAMPLE_RATE_INPUT],
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
        label = strings[PIXEL_FORMAT_INPUT],
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
        label = strings[RESOLUTION_INPUT],
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
private fun HomeState.PresetInput(mediaType: MediaType?, onValueChange: (String?) -> Unit) {
    if(codec != null && preset != null) {
        val minPreset = codec.minPreset
        val maxPreset = codec.maxPreset

        if(minPreset != null && maxPreset != null) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = strings[PRESET_INPUT],
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Spacer(modifier = Modifier.width(dimens.xs))
                    Text(
                        text = preset,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
                Spacer(modifier = Modifier.height(dimens.xs))
                Slider(
                    value = codec.indexByPresetValue(preset)?.toFloat() ?: 0f,
                    modifier = Modifier.width(320.dp),
                    enabled = mediaType == MediaType.VIDEO,
                    steps = maxPreset,
                    onValueChange = { onValueChange(codec.presetValueByIndex(it.toInt())) },
                    valueRange = minPreset.toFloat() .. maxPreset.toFloat()
                )
            }
        }
    }
}

@Composable
private fun CheckboxInput(checked: Boolean, label: String, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(dimens.xxs))
        Text(
            text = label,
            style = TextStyle(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Composable
private fun RowScope.LogsView(text: String) {
    val scrollState = rememberScrollState()
    val modifier = Modifier
        .weight(2f)
        .fillMaxHeight()

    LaunchedEffect(text) { scrollState.animateScrollTo(scrollState.maxValue) }

    Card(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = strings[LOGS_VIEW],
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(
                    top = dimens.md,
                    start = dimens.md,
                    end = dimens.md
                )
            )
            Spacer(modifier = Modifier.height(dimens.sm))
            HorizontalDivider()
            Box(modifier = Modifier.fillMaxSize()) {
                SelectionContainer(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .fillMaxSize()
                        .padding(
                            start = dimens.md,
                            end = 12.dp, // Space for VerticalScrollbar
                            top = dimens.sm,
                            bottom = dimens.sm
                        )
                ) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = dimens.md)
                    )
                }
                VerticalScrollbar(
                    adapter = rememberScrollbarAdapter(scrollState),
                    style = LocalScrollbarStyle.current.copy(
                        hoverColor = MaterialTheme.colorScheme.surfaceContainer
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun Progress(status: HomeStatus) {
    val modifier = Modifier
        .fillMaxWidth()
        .height(dimens.xxl)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        if(status is HomeStatus.Progress) {
            val text = "${String.format("%.2f", status.percentage)}% (${status.speed})"

            LinearProgress(status.percentage)
            Spacer(modifier = Modifier.height(dimens.xs))
            Text(
                text = text,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
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

private fun HomeState.canStart(mediaType: MediaType?): Boolean {
    if (
        mediaType == null ||
        input == null ||
        output.isNullOrBlank() ||
        codec == null ||
        status is HomeStatus.Loading ||
        status is HomeStatus.Progress
    ) return false

    return when (mediaType) {
        MediaType.AUDIO -> codec.compressions.isEmpty() || (bitrate != null || vbr != null)
        MediaType.VIDEO -> preset != null && crf != null
    }
}

@Composable
private fun DefaultPreview() {
    CompositionLocalProvider(stringsComp provides homeScreenStrings) {
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