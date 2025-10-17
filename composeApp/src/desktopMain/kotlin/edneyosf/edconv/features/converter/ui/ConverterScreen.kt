package edneyosf.edconv.features.converter.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edneyosf.edconv.features.common.models.Audio
import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.features.common.models.Video
import edneyosf.edconv.features.console.ui.ConsoleScreen
import edneyosf.edconv.ffmpeg.common.*
import edneyosf.edconv.features.converter.ConverterEvent
import edneyosf.edconv.features.converter.ConverterViewModel
import edneyosf.edconv.features.converter.states.ConverterState
import edneyosf.edconv.features.converter.states.ConverterStatusState
import edneyosf.edconv.features.converter.strings.converterScreenStrings
import edneyosf.edconv.features.converter.strings.ConverterScreenStrings.Keys.*
import edneyosf.edconv.features.mediainfo.MediaInfoScreen
import edneyosf.edconv.features.queue.ui.QueueScreen
import edneyosf.edconv.ui.components.ActionsTool
import edneyosf.edconv.ui.components.Selector
import edneyosf.edconv.ui.components.TextTooltip
import edneyosf.edconv.ui.components.extensions.custom
import edneyosf.edconv.ui.compositions.*
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview
import org.koin.compose.viewmodel.koinViewModel
import java.io.File

private const val SLIDER_DENSITY = 0.6f

@Composable
fun ConverterScreen() {
    val viewModel = koinViewModel<ConverterViewModel>()
    val state by viewModel.state.collectAsState()
    val commandState by viewModel.commandState.collectAsState()

    CompositionLocalProvider(value = stringsComp provides converterScreenStrings) {
        state.Dialogs(event = viewModel)
        state.Content(command = commandState, event = viewModel)
    }
}

@Composable
private fun ConverterState.Content(command: String, event: ConverterEvent) {
    val stringSaveFile = strings[OUTPUT_SAVE_FILE]
    val outputDir = output?.first?.let { File(it) }
    val outputFile = output?.second?.let { File(it) }
    val invalidOutputFile = outputFile?.extension?.isBlank() == true
    val hasVideo = input?.videos?.isNotEmpty() == true
    val hasAudio = input?.audios?.isNotEmpty() == true
    val videoEnabled = indexVideo != -1
    val audioEnabled = indexAudio != -1

    Column(
        modifier = Modifier.padding(all = dimens.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = dimens.xl)
    ) {
        Actions(
            command,
            invalidOutputFile,
            onAddToQueue = event::addToQueue,
            onStart = event::start,
            onStop = event::stop
        )
        Column(
            modifier = Modifier.weight(weight = 1f).verticalScroll(state = rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = dimens.xl)
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(space = dimens.xl),
                verticalArrangement = Arrangement.spacedBy(space = dimens.xl),
            ) {
                if(hasVideo) {
                    MediaSection(
                        imageVector = Icons.Rounded.Movie,
                        title = strings[VIDEO]
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(space = dimens.xl),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if(input.videos.size > 1) {
                                IndexInput(
                                    value = indexVideo?.takeIf { it >= 0 },
                                    max = input.videos.size,
                                    enabled = indexVideo != null && indexVideo >= 0,
                                    onValueChange = event::setIndexVideo,
                                    onClick = { event.setIndexVideo(0) }
                                )
                                AllIndexesInput(
                                    enabled = indexVideo == null,
                                    onClick = { event.setIndexVideo(null) }
                                )
                            }
                            if(indexAudio != -1){
                                DisableIndexInput(
                                    enabled = !videoEnabled,
                                    onClick = {
                                        event.setIndexAudio(0)
                                        event.setIndexVideo(-1)
                                    }
                                )
                            }
                        }
                        if(videoEnabled) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(space = dimens.xl),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                EncoderInput(
                                    value = encoderVideo,
                                    compression = compressionTypeVideo,
                                    mediaType = MediaType.VIDEO,
                                    onValueChange = event::setEncoderVideo,
                                    onClick = { event.setCompressionTypeVideo(null) }
                                )
                                CopyInput(compressionType = compressionTypeVideo) {
                                    event.setCompressionTypeVideo(CompressionType.COPY)
                                }
                                CBRInput(
                                    encoder = encoderVideo,
                                    bitrate = bitrateVideo,
                                    compressionType = compressionTypeVideo,
                                    mediaType = encoderVideo?.mediaType,
                                    onClick = { event.setCompressionTypeVideo(CompressionType.CBR) },
                                    onValueChange = event::setBitrateVideo
                                )
                                CRFInput(
                                    onClick = { event.setCompressionTypeVideo(CompressionType.CRF) },
                                    onValueChange = event::setBitrateControlVideo
                                )
                            }
                            if(compressionTypeVideo != CompressionType.COPY) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(space = dimens.xl)
                                ) {
                                    ResolutionInput(
                                        value = resolution,
                                        onValueChange = event::setResolution
                                    )
                                    PixelFormatInput(
                                        value = pixelFormat,
                                        onValueChange = event::setPixelFormat
                                    )
                                    PresetInput(onValueChange = event::setPresetVideo)
                                }
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(space = dimens.xl)
                            ){
                                CheckboxInput(
                                    checked = noSubtitle,
                                    label = strings[NO_SUBTITLE_INPUT],
                                    onCheckedChange = event::setNoSubtitle
                                )
                                CheckboxInput(
                                    checked = hdr10ToSdr,
                                    label = strings[HDR10_TO_SDR],
                                    onCheckedChange = event::setHdr10ToSdr
                                )
                            }
                        }
                    }
                }
                if(hasAudio) {
                    MediaSection(
                        imageVector = Icons.Rounded.MusicNote,
                        title = strings[AUDIO]
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(space = dimens.xl),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if(input.audios.size > 1) {
                                IndexInput(
                                    value = indexAudio?.takeIf { it >= 0 },
                                    max = input.audios.size,
                                    enabled = indexAudio != null && indexAudio >= 0,
                                    onValueChange = event::setIndexAudio,
                                    onClick = { event.setIndexAudio(0) }
                                )
                            }
                            if(input.audios.size > 1 && indexVideo != -1) {
                                AllIndexesInput(
                                    enabled = indexAudio == null,
                                    onClick = { event.setIndexAudio(null) }
                                )
                            }
                            if(indexVideo != -1){
                                DisableIndexInput(
                                    enabled = !audioEnabled,
                                    onClick = { event.setIndexAudio(-1) }
                                )
                            }
                        }
                        if(audioEnabled) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(space = dimens.xl),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                EncoderInput(
                                    value = encoderAudio,
                                    compression = compressionTypeAudio,
                                    mediaType = MediaType.AUDIO,
                                    onValueChange = event::setEncoderAudio,
                                    onClick = { event.setCompressionTypeAudio(null) }
                                )
                                CopyInput(compressionType = compressionTypeAudio) {
                                    event.setCompressionTypeAudio(CompressionType.COPY)
                                }
                                CBRInput(
                                    encoder = encoderAudio,
                                    bitrate = bitrateAudio,
                                    compressionType = compressionTypeAudio,
                                    mediaType = encoderAudio?.mediaType,
                                    onClick = { event.setCompressionTypeAudio(CompressionType.CBR) },
                                    onValueChange = event::setBitrateAudio
                                )
                                VBRInput(
                                    onClick = { event.setCompressionTypeAudio(CompressionType.VBR) },
                                    onValueChange = event::setBitrateControlAudio
                                )
                            }
                            if(compressionTypeAudio != CompressionType.COPY) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(space = dimens.xl)
                                ) {
                                    ChannelsInput(
                                        value = channels,
                                        onValueChange = event::setChannels
                                    )
                                    SampleRateInput(
                                        value = sampleRate,
                                        onValueChange = event::setSampleRate
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = dimens.xl)
            ) {
                CheckboxInput(
                    checked = noMetadata,
                    label = strings[NO_METADATA_INPUT],
                    onCheckedChange = event::setNoMetadata
                )
                CheckboxInput(
                    checked = noChapters,
                    label = strings[NO_CHAPTERS],
                    onCheckedChange = event::setNoChapters
                )
            }
        }
        Progress(status)
        Row(
            horizontalArrangement = Arrangement.spacedBy(space = dimens.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = output?.second ?: "",
                modifier = Modifier.weight(weight = 1f),
                colors = TextFieldDefaults.colors().custom(),
                onValueChange = event::setOutput,
                label = { Text(text = strings[OUTPUT_FILE]) },
                maxLines = 1,
                isError = invalidOutputFile,
                trailingIcon = {
                    if(invalidOutputFile) {
                        Icon(
                            imageVector = Icons.Rounded.Error,
                            contentDescription = null
                        )
                    }
                }
            )
            Text(
                text = strings[OUTPUT_TO],
                style = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
            ElevatedButton(
                onClick = {
                    event.pickFolder(
                        title = stringSaveFile,
                        fileName = output?.second ?: ""
                    )
                }
            ){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.height(height = dimens.ml),
                        imageVector =  Icons.Rounded.FolderOpen,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(width = dimens.xs))
                    Text(
                        modifier = Modifier.widthIn(max = 150.dp),
                        text = outputDir?.name ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun MediaSection(imageVector: ImageVector, title: String, content: (@Composable ColumnScope.() -> Unit)) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .border(
                border = BorderStroke(width = 1.5.dp, color = MaterialTheme.colorScheme.surfaceContainerHighest),
                shape = RoundedCornerShape(size = dimens.sm)
            ).padding(all = dimens.md)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = imageVector,
                tint = MaterialTheme.colorScheme.tertiary,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(width = dimens.xs))
            Text(
                text = title,
                style = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.tertiary)
            )
        }
        Spacer(modifier = Modifier.height(height = dimens.sm))
        Column(
            verticalArrangement = Arrangement.spacedBy(space = dimens.sm),
            content = content
        )
    }
}

@Composable
private fun ConverterState.Actions(
    command: String,
    invalidOutputFile: Boolean,
    onAddToQueue: () -> Unit,
    onStart: () -> Unit,
    onStop: () -> Unit
) {
    var showQueue by remember { mutableStateOf(value = false) }
    var showMediaInfo by remember { mutableStateOf(value = false) }
    var showConsole by remember { mutableStateOf(value = false) }

    if(showQueue) {
        QueueScreen(
            onClose = { showQueue = false }
        )
    }

    if(showMediaInfo) {
        input?.MediaInfoScreen(
            onFinish = { showMediaInfo = false }
        )
    }

    if(showConsole) {
        ConsoleScreen(
            onFinish = { showConsole = false }
        )
    }

    ActionsTool(
        addToQueueEnabled = canAddToQueue(command, invalidOutputFile),
        startEnabled = canStart(command, invalidOutputFile),
        stopEnabled = canStop(),
        addToQueueDescription = strings[ADD_TO_QUEUE_CONVERSION],
        startDescription = strings[START_CONVERSION],
        stopDescription = strings[STOP_CONVERSION],
        onAddToQueue = onAddToQueue,
        onStart = onStart,
        onStop = onStop,
        lefties = {
            TextTooltip(text = strings[QUEUE]) {
                BadgedBox(
                    badge = {
                        queueSize.takeIf { it > 0 }?.let {
                            Badge {
                                Text(text = if (it > 99) "99+" else it.toString())
                            }
                        }
                    }
                ) {
                    IconButton(
                        onClick = { showQueue = true }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Queue,
                            contentDescription = strings[QUEUE]
                        )
                    }
                }
            }
        },
        righties = {
            TextTooltip(text = strings[CONSOLE]) {
                IconButton(
                    onClick = { showConsole = true }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Terminal,
                        contentDescription = strings[CONSOLE]
                    )
                }
            }
            input?.let {
                TextTooltip(text = strings[MEDIA_INFO]) {
                    IconButton(
                        onClick = { showMediaInfo = true }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Info,
                            contentDescription = strings[MEDIA_INFO]
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun IndexInput(value: Int?, max: Int, enabled: Boolean, onValueChange: (Int) -> Unit, onClick: () -> Unit) {
    var expanded by remember { mutableStateOf(value = false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = enabled,
            onClick = onClick
        )
        Selector(
            modifier = Modifier.width(width = 128.dp),
            text = value?.toString() ?: "",
            label = strings[INDEX],
            enabled = value != null && value >= 0 && max > 0,
            expanded = expanded,
            onExpanded = { expanded = it }
        ) {
            for (index in 0..< max) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = index.toString(),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    },
                    onClick = {
                        expanded = false
                        onValueChange(index)
                    }
                )
            }
        }
    }
}

@Composable
private fun EncoderInput(
    value: Encoder?, compression: CompressionType?, mediaType: MediaType?,
    onValueChange: (Encoder) -> Unit, onClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(value = false) }
    val encoders = Encoder.getAll().filter { it.mediaType == mediaType }

    Row(verticalAlignment = Alignment.CenterVertically) {
        if(value?.compressions?.isEmpty() == true) {
            RadioButton(selected = compression == null, onClick = onClick)
        }
        Selector(
            modifier = Modifier.width(width = 164.dp),
            text = value?.text ?: "",
            label = strings[ENCODER_INPUT],
            enabled = encoders.isNotEmpty(),
            expanded = expanded,
            onExpanded = { expanded = it }
        ) {
            encoders.forEach { item ->
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

@Composable
private fun ConverterState.VBRInput(onClick: () -> Unit, onValueChange: (Int) -> Unit) {
    if(encoderAudio?.compressions?.contains(CompressionType.VBR) == true && bitrateControlAudio != null) {
        val minVBR = encoderAudio.minVBR
        val maxVBR = encoderAudio.maxVBR

        if(minVBR != null && maxVBR != null) {
            val isVBR = CompressionType.VBR == compressionTypeAudio
            val valueColor = MaterialTheme.colorScheme.run { if(isVBR) onSurface else onSurface.copy(alpha = 0.38f) }

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    if(encoderAudio.compressions.size > 1) {
                        RadioButton(selected = isVBR, onClick = onClick)
                    }
                    Text(
                        text = strings[VBR_INPUT],
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Spacer(modifier = Modifier.width(width = dimens.xs))
                    Text(
                        text = bitrateControlAudio.toString(),
                        style = TextStyle(
                            color = valueColor,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                Box(modifier = Modifier.padding(start = 14.dp)) {
                    CompositionLocalProvider(LocalDensity provides Density(density = SLIDER_DENSITY)) {
                        Slider(
                            value = bitrateControlAudio.toFloat(),
                            modifier = Modifier.width(width = 280.dp),
                            enabled = isVBR,
                            onValueChange = { onValueChange(it.toInt()) },
                            valueRange = minVBR.toFloat() .. maxVBR.toFloat()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CBRInput(
    encoder: Encoder?, compressionType: CompressionType?, bitrate: Bitrate?, mediaType: MediaType?, onClick: () -> Unit,
    onValueChange: (Bitrate) -> Unit
) {
    if(encoder?.compressions?.contains(CompressionType.CBR) == true) {
        var expanded by remember { mutableStateOf(value = false) }
        val isCBR = CompressionType.CBR == compressionType

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = isCBR, onClick = onClick)
            Selector(
                modifier = Modifier.width(width = 148.dp),
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
private fun AllIndexesInput(enabled: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        RadioButton(
            selected = enabled,
            onClick = onClick
        )
        Text(
            text = strings[INDEX_ALL],
            style = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
    }
}

@Composable
private fun DisableIndexInput(enabled: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        RadioButton(
            selected = enabled,
            onClick = onClick
        )
        Text(
            text = strings[INDEX_DISABLED],
            style = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
    }
}

@Composable
private fun CopyInput(compressionType: CompressionType?, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        RadioButton(
            selected = CompressionType.COPY == compressionType,
            onClick = onClick
        )
        Text(
            text = strings[COPY_INPUT],
            style = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
    }
}

@Composable
private fun ConverterState.CRFInput(onClick: () -> Unit, onValueChange: (Int) -> Unit) {
    if(encoderVideo?.compressions?.contains(CompressionType.CRF) == true && bitrateControlVideo != null) {
        val minCRF = encoderVideo.minCRF
        val maxCRF = encoderVideo.maxCRF

        if(minCRF != null && maxCRF != null) {
            val isCRF = CompressionType.CRF == compressionTypeVideo
            val valueColor = MaterialTheme.colorScheme.run { if(isCRF) onSurface else onSurface.copy(alpha = 0.38f) }

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    if(encoderVideo.compressions.size > 1) RadioButton(selected = isCRF, onClick = onClick)
                    Text(
                        text = strings[CRF_INPUT],
                        style = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                    Spacer(modifier = Modifier.width(width = dimens.xs))
                    Text(
                        text = bitrateControlVideo.toString(),
                        style = TextStyle(
                            color = valueColor,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                Box(modifier = Modifier.padding(start = 14.dp)) {
                    CompositionLocalProvider(LocalDensity provides Density(density = SLIDER_DENSITY)) {
                        Slider(
                            value = bitrateControlVideo.toFloat(),
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
}

@Composable
private fun ChannelsInput(value: Channels?, onValueChange: (Channels) -> Unit) {
    var expanded by remember { mutableStateOf(value = false) }

    Selector(
        modifier = Modifier.width(width = 196.dp),
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
        modifier = Modifier.width(width = 196.dp),
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
        modifier = Modifier.width(width = 232.dp),
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
        modifier = Modifier.width(width = 200.dp),
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
    if(encoderVideo != null && presetVideo != null) {
        val minPreset = encoderVideo.minPreset
        val maxPreset = encoderVideo.maxPreset

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
                        text = presetVideo,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
                Spacer(modifier = Modifier.height(height = dimens.xs))
                CompositionLocalProvider(LocalDensity provides Density(density = SLIDER_DENSITY)) {
                    Slider(
                        value = encoderVideo.indexByPresetValue(value = presetVideo)?.toFloat() ?: 0f,
                        modifier = Modifier.width(width = 320.dp),
                        enabled = encoderVideo.mediaType == MediaType.VIDEO,
                        onValueChange = { onValueChange(encoderVideo.presetValueByIndex(index = it.toInt())) },
                        valueRange = minPreset.toFloat() .. maxPreset.toFloat()
                    )
                }
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
private fun Progress(status: ConverterStatusState) {
    val modifier = Modifier
        .fillMaxWidth()
        .height(height = dimens.xxl)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        if(status is ConverterStatusState.Progress && status.percentage > 0f) {
            var text = "${String.format("%.2f", status.percentage)}% (${status.speed})"

            status.step?.let { text += "\t${strings[PENDING_JOBS]} $it" }

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

private fun ConverterState.canAddToQueue(command: String, invalidOutputFile: Boolean): Boolean {
    if (
        output?.first.isNullOrBlank() ||
        output.second.isBlank() ||
        command.isBlank() ||
        (encoderAudio == null && encoderVideo == null) ||
        invalidOutputFile ||
        status is ConverterStatusState.Loading
    ) return false

    return when (type) {
        MediaType.AUDIO -> encoderAudio == null || encoderAudio.compressions.isEmpty()
                || (bitrateAudio != null || bitrateControlAudio != null)
        MediaType.VIDEO -> encoderVideo == null || presetVideo != null && bitrateControlVideo != null
        else -> false
    }
}

private fun ConverterState.canStart(command: String, invalidOutputFile: Boolean): Boolean {
    if (
        output?.first.isNullOrBlank() ||
        output.second.isBlank() ||
        command.isBlank() ||
        invalidOutputFile ||
        (encoderAudio == null && encoderVideo == null) ||
        status is ConverterStatusState.Loading ||
        status is ConverterStatusState.Progress
    ) return false

    return when (type) {
        MediaType.AUDIO -> encoderAudio == null || encoderAudio.compressions.isEmpty()
                || (bitrateAudio != null || bitrateControlAudio != null)
        MediaType.VIDEO -> encoderVideo == null || presetVideo != null && bitrateControlVideo != null
        else -> false
    }
}

private fun ConverterState.canStop(): Boolean = status is ConverterStatusState.Progress

@Composable
private fun DefaultPreview() {
    CompositionLocalProvider(value = stringsComp provides converterScreenStrings) {
        val type = MediaType.VIDEO
        val output = Pair(first = "Edconv", second = "video.mkv")
        val input = InputMedia(
            path = "/sdfsd",
            type = type,
            size = 123456L,
            sizeText = "123456",
            duration = 123456L,
            durationText = "123456",
            videos = listOf(Video(width = 100, height = 100)),
            audios = listOf(Audio(channels = 2))
        )

        ConverterState(
            status = ConverterStatusState.Initial,
            input = input,
            type = type,
            output = output,
            encoderAudio = Encoder.OPUS,
            encoderVideo = Encoder.AV1,
            presetVideo = "4",
            bitrateControlVideo = 22,
            bitrateAudio = Bitrate.K384,
            compressionTypeAudio = CompressionType.CBR,
            compressionTypeVideo = CompressionType.CRF,
            resolution = Resolution.P1080,
            pixelFormat = PixelFormat.BIT_10,
        ).Content(command = "Command", event = object : ConverterEvent {})
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