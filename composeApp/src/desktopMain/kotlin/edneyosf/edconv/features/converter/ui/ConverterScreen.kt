package edneyosf.edconv.features.converter.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import edneyosf.edconv.core.extensions.LaunchedEffected
import edneyosf.edconv.ffmpeg.common.*
import edneyosf.edconv.ffmpeg.data.ContentTypeData
import edneyosf.edconv.ffmpeg.data.InputMedia
import edneyosf.edconv.features.converter.events.ConverterEvent
import edneyosf.edconv.features.converter.events.ConverterEvent.*
import edneyosf.edconv.features.converter.managers.ConverterManager
import edneyosf.edconv.features.converter.states.ConverterDialog
import edneyosf.edconv.features.converter.states.ConverterState
import edneyosf.edconv.features.converter.states.ConverterStatus
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
fun ConverterScreen(state: ConverterState) {
    val coroutineScope = rememberCoroutineScope()
    val manager = remember { ConverterManager(defaultState = state, scope = coroutineScope) }
    val state by manager.state

    LaunchedEffected(key = state.codec) {
        manager.onEvent(SetBitrate(bitrate = it?.defaultBitrate))
        manager.onEvent(SetVbr(it?.defaultVBR))
        manager.onEvent(SetCrf(it?.defaultCRF))
        manager.onEvent(SetPreset(it?.defaultPreset))
        manager.onEvent(SetCompression(compression = it?.compressions?.firstOrNull()))
    }

    LaunchedEffected(state.mediaType) {
        val defaultCodec = when(it) {
            MediaType.AUDIO -> Codec.OPUS
            MediaType.VIDEO -> Codec.AV1
        }

        manager.onEvent(SetCodec(codec = defaultCodec))
    }

    CompositionLocalProvider(stringsComp provides converterScreenStrings) {
        state.Dialogs(onEvent = manager::onEvent)
        state.Content(onEvent = manager::onEvent)
    }
}

@Composable
private fun ConverterState.Content(onEvent: (ConverterEvent) -> Unit) {
    Column(
        modifier = Modifier.padding(dimens.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimens.xl)
    ) {
        Actions(
            startEnabled = canStart(mediaType),
            onStart = { onEvent(OnStart()) },
            onStop = { onEvent(OnStop) },
            onMediaInfo = { onEvent(SetDialog(ConverterDialog.MediaInfo(inputMedia = input)))}
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
                mediaType = codec?.mediaType,
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
                PresetInput(onValueChange = { onEvent(SetPreset(it)) })
            }
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
            if(mediaType == MediaType.VIDEO) {
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
            if(cmd.isNotBlank()) {
                TextField(
                    value = cmd,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    colors = TextFieldDefaults.colors().custom(),
                    onValueChange = { onEvent(SetCmd(it)) },
                    label = { Text(strings[COMMAND_INPUT]) }
                )
            }
        }
        Progress(status)
        TextField(
            value = output ?: "",
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors().custom(),
            onValueChange = { onEvent(SetOutput(it)) },
            label = { Text(strings[OUTPUT_FILE]) }
        )
    }
}

@Composable
private fun ConverterState.Actions(startEnabled: Boolean, onStart: () -> Unit, onStop: () -> Unit, onMediaInfo: () -> Unit) {
    val stopEnabled = status is ConverterStatus.Progress
    val modifier =  Modifier
        .fillMaxWidth()
        .padding(bottom = dimens.sm)

    Row(modifier = modifier) {
        Spacer(modifier = Modifier.weight(1f))
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
        Spacer(modifier = Modifier.weight(1f))
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
                    enabled = codec.mediaType == MediaType.VIDEO,
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

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(dimens.xs)
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
                    modifier = Modifier.size(dimens.lg),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(dimens.xs))
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
                        .verticalScroll(scrollState)
                        .fillMaxSize()
                        .padding(
                            start = dimens.md,
                            end = dimens.sm, // Space for VerticalScrollbar
                            top = dimens.sm,
                            bottom = dimens.sm
                        )
                ) {
                    Text(
                        text = text,
                        style = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            fontFamily = firaCodeFont
                        ),
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
private fun Progress(status: ConverterStatus) {
    val modifier = Modifier
        .fillMaxWidth()
        .height(dimens.xxl)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        if(status is ConverterStatus.Progress) {
            val text = "${String.format("%.2f", status.percentage)}% (${status.speed})"

            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = { status.percentage / 100 },
                strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap
            )
            Spacer(modifier = Modifier.height(dimens.xs))
            Text(
                text = text,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
        else if(status is ConverterStatus.Loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}

private fun ConverterState.canStart(mediaType: MediaType?): Boolean {
    if (
        mediaType == null ||
        output.isNullOrBlank() ||
        codec == null ||
        status is ConverterStatus.Loading ||
        status is ConverterStatus.Progress
    ) return false

    return when (mediaType) {
        MediaType.AUDIO -> codec.compressions.isEmpty() || (bitrate != null || vbr != null)
        MediaType.VIDEO -> preset != null && crf != null
    }
}

@Composable
private fun DefaultPreview() {
    CompositionLocalProvider(stringsComp provides converterScreenStrings) {
        val type = MediaType.VIDEO
        val inputMedia = InputMedia(
            path = "/sdfsd",
            type = type,
            contentType = ContentTypeData(video = true, audio = true, subtitle = true),
            size = 123456L
        )

        ConverterState(input = inputMedia, mediaType = type).Content(onEvent = {})
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