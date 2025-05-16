package edneyosf.edconv.features.converter.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import edneyosf.edconv.core.extensions.LaunchedEffected
import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.features.converter.ConverterArgs
import edneyosf.edconv.ffmpeg.common.*
import edneyosf.edconv.features.converter.events.ConverterEvent
import edneyosf.edconv.features.converter.viewmodels.ConverterViewModel
import edneyosf.edconv.features.converter.states.ConverterDialogState
import edneyosf.edconv.features.converter.states.ConverterState
import edneyosf.edconv.features.converter.states.ConverterStatusState
import edneyosf.edconv.features.converter.strings.converterScreenStrings
import edneyosf.edconv.features.converter.strings.ConverterScreenStrings.Keys.*
import edneyosf.edconv.ui.components.Selector
import edneyosf.edconv.ui.components.TextTooltip
import edneyosf.edconv.ui.components.extensions.custom
import edneyosf.edconv.ui.compositions.*
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview
import edneyosf.edconv.ui.theme.firaCodeFont

@Composable
fun ConverterScreen(args: ConverterArgs) {
    val viewModel = viewModel { ConverterViewModel(input = args.input, type = args.type) }
    val state by viewModel.state.collectAsState()

    LaunchedEffected(key = args) { viewModel.refresh(newInput = it.input, newType = it.type) }

    CompositionLocalProvider(value = stringsComp provides converterScreenStrings) {
        state.Dialogs(event = viewModel)
        state.Content(
            logs = viewModel.logsState,
            event = viewModel
        )
    }
}

@Composable
private fun ConverterState.Content(logs: List<String>, event: ConverterEvent) {
    Column(
        modifier = Modifier.padding(all = dimens.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = dimens.xl)
    ) {
        Actions(
            startEnabled = canStart(),
            onStart = { event.start() },
            onStop = { event.stop() },
            onMediaInfo = { event.setDialog(ConverterDialogState.MediaInfo(inputMedia = input)) }
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(space = dimens.xl),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FormatInput(
                value = codec,
                mediaType = type,
                onValueChange = { event.setCodec(it) }
            )
            VBRInput(
                onClick = { event.setCompression(CompressionType.VBR) },
                onValueChange = { event.setVbr(it) }
            )
            CRFInput(
                onClick = { event.setCompression(CompressionType.CRF) },
                onValueChange = { event.setCrf(it) }
            )
            CBRInput(
                mediaType = codec?.mediaType,
                onClick = { event.setCompression(CompressionType.CBR) },
                onValueChange = { event.setBitrate(it) }
            )
        }
        if(type == MediaType.AUDIO) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = dimens.xl)
            ) {
                ChannelsInput(
                    value = channels,
                    onValueChange = { event.setChannels(it) }
                )
                SampleRateInput(
                    value = sampleRate,
                    onValueChange = { event.setSampleRate(it) }
                )
            }
        }
        else if(type == MediaType.VIDEO) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = dimens.xl)
            ) {
                ResolutionInput(
                    value = resolution,
                    onValueChange = { event.setResolution(it) }
                )
                PixelFormatInput(
                    value = pixelFormat,
                    onValueChange = { event.setPixelFormat(it) }
                )
                PresetInput(onValueChange = { event.setPreset(it) })
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = dimens.xl)
        ) {
            CheckboxInput(
                checked = noMetadata,
                label = strings[NO_METADATA_INPUT],
                onCheckedChange = { event.setNoMetadata(it) }
            )
            if(type == MediaType.VIDEO) {
                CheckboxInput(
                    checked = noAudio,
                    label = strings[NO_AUDIO_INPUT],
                    onCheckedChange = { event.setNoAudio(it) }
                )
                CheckboxInput(
                    checked = noSubtitle,
                    label = strings[NO_SUBTITLE_INPUT],
                    onCheckedChange = { event.setNoSubtitle(it) }
                )
            }
        }
        Row(
            modifier = Modifier.weight(weight = 1f),
            horizontalArrangement = Arrangement.spacedBy(space = dimens.md)
        ) {
            LogsView(data = logs)
            if(command.isNotBlank()) {
                TextField(
                    value = command,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(weight = 1f).fillMaxHeight(),
                    colors = TextFieldDefaults.colors().custom(),
                    onValueChange = { event.setCommand(it) },
                    label = { Text(text = strings[COMMAND_INPUT]) }
                )
            }
        }
        Progress(status)
        TextField(
            value = output ?: "",
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors().custom(),
            onValueChange = { event.setOutput(it) },
            label = { Text(text = strings[OUTPUT_FILE]) }
        )
    }
}

@Composable
private fun ConverterState.Actions(startEnabled: Boolean, onStart: () -> Unit, onStop: () -> Unit, onMediaInfo: () -> Unit) {
    val stopEnabled = status is ConverterStatusState.Progress
    val modifier =  Modifier
        .fillMaxWidth()
        .padding(bottom = dimens.sm)

    Row(modifier = modifier) {
        Spacer(modifier = Modifier.weight(weight = 1f))
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
        Spacer(modifier = Modifier.width(width = dimens.md))
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
        Spacer(modifier = Modifier.weight(weight = 1f))
        TextTooltip(text = strings[MEDIA_INFO]) {
            IconButton(onClick = onMediaInfo) {
                Icon(imageVector = Icons.Rounded.Info, contentDescription = strings[MEDIA_INFO])
            }
        }
    }
}

@Composable
private fun FormatInput(value: Codec?, mediaType: MediaType?, onValueChange: (Codec) -> Unit) {
    var expanded by remember { mutableStateOf(value = false) }
    val medias = Codec.getAll().filter {
        it.mediaType == when(mediaType) {
            MediaType.AUDIO -> MediaType.AUDIO
            MediaType.VIDEO -> MediaType.VIDEO
            else -> null
        }
    }

    Selector(
        text = value?.text ?: "",
        label = strings[FORMAT_INPUT],
        enabled = medias.isNotEmpty(),
        expanded = expanded,
        onExpanded = { expanded = it }
    ) {
        medias.forEach { item ->
            DropdownMenuItem(
                text = { Text(text = item.text) },
                onClick = {
                    expanded = false
                    onValueChange(item)
                }
            )
        }
    }
}

@Composable
private fun ConverterState.VBRInput(onClick: () -> Unit, onValueChange: (Int) -> Unit) {
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
                    Spacer(modifier = Modifier.width(width = dimens.xs))
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
                        modifier = Modifier.width(width = 220.dp),
                        enabled = isVBR,
                        onValueChange = { onValueChange(it.toInt()) },
                        valueRange = minVBR.toFloat() .. maxVBR.toFloat()
                    )
                }
            }
        }
    }
}

@Composable
private fun ConverterState.CBRInput(mediaType: MediaType?, onClick: () -> Unit, onValueChange: (Bitrate) -> Unit) {
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
                        text = { Text(text = item.text) },
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
private fun ConverterState.CRFInput(onClick: () -> Unit, onValueChange: (Int) -> Unit) {
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
                    Spacer(modifier = Modifier.width(width = dimens.xs))
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
                        modifier = Modifier.width(width = 320.dp),
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
                text = { Text(text = item.text) },
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
                text = { Text(text = item.text) },
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
                text = { Text(text = item.text) },
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
                text = { Text(text = item.text) },
                onClick = {
                    expanded = false
                    onValueChange(item)
                }
            )
        }
    }
}

@Composable
private fun ConverterState.PresetInput(onValueChange: (String?) -> Unit) {
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
                    Spacer(modifier = Modifier.width(width = dimens.xs))
                    Text(
                        text = preset,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
                Spacer(modifier = Modifier.height(height = dimens.xs))
                Slider(
                    value = codec.indexByPresetValue(value = preset)?.toFloat() ?: 0f,
                    modifier = Modifier.width(width = 320.dp),
                    enabled = codec.mediaType == MediaType.VIDEO,
                    onValueChange = { onValueChange(codec.presetValueByIndex(index = it.toInt())) },
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
        Spacer(modifier = Modifier.width(width = dimens.xxs))
        Text(
            text = label,
            style = TextStyle(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Composable
private fun RowScope.LogsView(data: List<String>) {
    val scrollState = rememberLazyListState()
    val modifier = Modifier
        .weight(weight = 2f)
        .fillMaxHeight()

    LaunchedEffect(data.size) {
        if(data.isNotEmpty()) {
            scrollState.animateScrollToItem(index = data.size - 1)
        }
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(size = dimens.xs)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(
                    vertical = dimens.xs,
                    horizontal = dimens.md)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Terminal,
                    modifier = Modifier.size(size = dimens.lg),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(width = dimens.xs))
                Text(
                    text = strings[LOGS_VIEW],
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
            HorizontalDivider()
            Box(modifier = Modifier.fillMaxSize()) {
                SelectionContainer(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = dimens.md,
                            end = dimens.sm, // Space for VerticalScrollbar
                            top = dimens.sm,
                            bottom = dimens.sm
                        )
                ) {
                    LazyColumn(state = scrollState) {
                        items(items = data) {
                            Text(
                                text = it,
                                style = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    fontFamily = firaCodeFont
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(bottom = dimens.md)
                            )
                        }
                    }
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
private fun Progress(status: ConverterStatusState) {
    val modifier = Modifier
        .fillMaxWidth()
        .height(height = dimens.xxl)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        if(status is ConverterStatusState.Progress && status.percentage > 0f) {
            val text = "${String.format("%.2f", status.percentage)}% (${status.speed})"

            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = { status.percentage / 100 },
                strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap
            )
            Spacer(modifier = Modifier.height(height = dimens.xs))
            Text(
                text = text,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
        else if(status is ConverterStatusState.Progress || status is ConverterStatusState.Loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}

private fun ConverterState.canStart(): Boolean {
    if (
        output.isNullOrBlank() ||
        command.isBlank() ||
        codec == null ||
        status is ConverterStatusState.Loading ||
        status is ConverterStatusState.Progress
    ) return false

    return when (type) {
        MediaType.AUDIO -> codec.compressions.isEmpty() || (bitrate != null || vbr != null)
        MediaType.VIDEO -> preset != null && crf != null
        MediaType.SUBTITLE -> false
    }
}

@Composable
private fun DefaultPreview() {
    CompositionLocalProvider(value = stringsComp provides converterScreenStrings) {
        val type = MediaType.VIDEO
        val logs = listOf<String>()
        val input = InputMedia(
            path = "/sdfsd",
            type = type,
            size = 123456L,
            sizeText = "123456",
            duration = 123456L,
            durationText = "123456"
        )

        ConverterState(input = input, type = type)
            .Content(logs, event = object : ConverterEvent {})
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