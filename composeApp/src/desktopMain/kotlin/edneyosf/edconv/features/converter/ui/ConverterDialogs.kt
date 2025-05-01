package edneyosf.edconv.features.converter.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import edneyosf.edconv.features.common.models.Audio
import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.features.common.models.Subtitle
import edneyosf.edconv.features.common.models.Video
import edneyosf.edconv.ffmpeg.common.MediaType
import edneyosf.edconv.features.converter.events.ConverterEvent
import edneyosf.edconv.features.converter.states.ConverterDialogState
import edneyosf.edconv.features.converter.states.ConverterState
import edneyosf.edconv.features.converter.states.ConverterStatusState
import edneyosf.edconv.features.converter.strings.converterDialogStrings
import edneyosf.edconv.features.converter.strings.ConverterDialogStrings.Keys.*
import edneyosf.edconv.features.settings.ui.SettingsDialog
import edneyosf.edconv.ui.components.dialogs.SimpleDialog
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.compositions.strings
import edneyosf.edconv.ui.compositions.stringsComp
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview
import java.io.File

@Composable
fun ConverterState.Dialogs(event: ConverterEvent) {
    status.run {
        when (this) {
            is ConverterStatusState.Failure -> {
                ConverterErrorDialog(
                    error = error,
                    onFinish = { event.setStatus(ConverterStatusState.Initial) }
                )
            }

            is ConverterStatusState.Complete -> {
                CompleteDialog(
                    startTime = startTime,
                    finishTime = finishTime,
                    duration = duration,
                    onFinish = { event.setStatus(ConverterStatusState.Initial) }
                )
            }

            is ConverterStatusState.FileExists -> {
                OverwriteFileDialog(
                    onCancel = { event.setStatus(ConverterStatusState.Initial) },
                    onConfirmation = { event.start(overwrite = true) }
                )
            }

            else -> Unit
        }
    }

    dialog.run {
        when(this) {
            is ConverterDialogState.Settings -> SettingsDialog { event.setDialog(ConverterDialogState.None) }

            is ConverterDialogState.MediaInfo -> {
                inputMedia.MediaInfoDialog(
                    onFinish = { event.setDialog(ConverterDialogState.None) }
                )
            }

            else -> Unit
        }
    }
}

@Composable
private fun CompleteDialog(startTime: String, finishTime: String, duration: String, onFinish: () -> Unit) {
    CompositionLocalProvider(stringsComp provides converterDialogStrings) {
        SimpleDialog(
            title = strings[COMPLETE_TITLE],
            content = {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = strings[START_TIME])
                        Spacer(modifier = Modifier.width(dimens.xxs))
                        Text(text = startTime, style = MaterialTheme.typography.labelLarge)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = strings[END_TIME])
                        Spacer(modifier = Modifier.width(dimens.xxs))
                        Text(text = finishTime, style = MaterialTheme.typography.labelLarge)
                    }
                    Spacer(modifier = Modifier.height(dimens.xs))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val color = MaterialTheme.colorScheme.tertiary

                        Text(
                            text = strings[DURATION_TIME],
                            style = MaterialTheme.typography.bodyLarge.copy(color = color)
                        )
                        Spacer(modifier = Modifier.width(dimens.xxs))
                        Text(text = duration, style = MaterialTheme.typography.titleMedium.copy(color = color))
                    }
                }
            },
            icon = Icons.Rounded.CheckCircle,
            onConfirmation = onFinish,
            confirmationText = strings[CONFIRMATION_BUTTON]
        )
    }
}

@Composable
private fun InputMedia.MediaInfoDialog(onFinish: () -> Unit) {
    val scroll = rememberScrollState()

    CompositionLocalProvider(stringsComp provides converterDialogStrings) {
        SimpleDialog(
            icon = Icons.Rounded.Info,
            title = strings[MEDIA_INFO_TITLE],
            content = {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(scroll)
                            .weight(1f)
                            .padding(end = dimens.sm)
                    ) {
                        if(path.isNotBlank()) {
                            val file = File(path)

                            ItemMediaInfo(label = strings[FILE_MEDIA_INFO], value = file.name)
                        }
                        ItemMediaInfo(label = strings[TYPE_MEDIA_INFO], value = type.TypeMediaInfoString())
                        ItemMediaInfo(label = strings[FORMAT_MEDIA_INFO], value = formatName)
                        ItemMediaInfo(label = strings[DURATION_MEDIA_INFO], value = durationText)
                        ItemMediaInfo(label = strings[BITRATE_MEDIA_INFO], value = bitrateText)
                        ItemMediaInfo(label = strings[SIZE_MEDIA_INFO], value = sizeText)
                        if(videos.isNotEmpty() || audios.isNotEmpty() || subtitles.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(dimens.xl))
                        }
                        if(videos.isNotEmpty()) {
                            TitleMediaInfo(strings[VIDEO_MEDIA_INFO])
                            Spacer(modifier = Modifier.height(dimens.md))
                            videos.forEachIndexed { index, it ->
                                ItemMediaInfo(label = strings[INDEX_MEDIA_INFO], value = index.toString())
                                ItemMediaInfo(label = strings[CODEC_MEDIA_INFO], value = it.codec)
                                ItemMediaInfo(label = strings[TITLE_MEDIA_INFO], value = it.title)
                                ItemMediaInfo(label = strings[LANGUAGE_MEDIA_INFO], value = it.language)
                                ItemMediaInfo(label = strings[PROFILE_MEDIA_INFO], value = it.profile)
                                ItemMediaInfo(label = strings[WIDTH_MEDIA_INFO], value = it.width.toString())
                                ItemMediaInfo(
                                    label = strings[HEIGHT_MEDIA_INFO],
                                    value = it.height.toString()
                                )
                                ItemMediaInfo(
                                    label = strings[BIT_DEPTH_MEDIA_INFO],
                                    value = it.bitDepth?.let { "$it-bit" }
                                )
                                ItemMediaInfo(label = strings[PIX_FMT_MEDIA_INFO], value = it.pixFmt)
                                ItemMediaInfo(label = strings[FPS_MEDIA_INFO], value = it.fps?.toString())
                                ItemMediaInfo(label = strings[LEVEL_MEDIA_INFO], value = it.level?.toString())
                                ItemMediaInfo(label = strings[FILM_GRAIN_MEDIA_INFO], value = it.filmGrain.toText())
                                ItemMediaInfo(
                                    label = strings[DISPLAY_ASPECT_RATIO_MEDIA_INFO],
                                    value = it.displayAspectRatio
                                )
                                ItemMediaInfo(label = strings[FIELD_ORDER_MEDIA_INFO], value = it.fieldOrder)
                                Spacer(modifier = Modifier.height(dimens.sm))
                            }
                        }
                        if(videos.isNotEmpty()) Spacer(modifier = Modifier.height(dimens.md))
                        if(audios.isNotEmpty()) {
                            TitleMediaInfo(strings[AUDIO_MEDIA_INFO])
                            Spacer(modifier = Modifier.height(dimens.md))
                            audios.forEachIndexed { index, it ->
                                ItemMediaInfo(label = strings[INDEX_MEDIA_INFO], value = index.toString())
                                ItemMediaInfo(label = strings[CODEC_MEDIA_INFO], value = it.codec)
                                ItemMediaInfo(label = strings[TITLE_MEDIA_INFO], value = it.title)
                                ItemMediaInfo(label = strings[LANGUAGE_MEDIA_INFO], value = it.language)
                                ItemMediaInfo(label = strings[PROFILE_MEDIA_INFO], value = it.profile)
                                ItemMediaInfo(label = strings[CHANNELS_MEDIA_INFO], value = it.channels.toString())
                                ItemMediaInfo(
                                    label = strings[SAMPLE_RATE_MEDIA_INFO],
                                    value = it.sampleRate?.let { "$it Hz" }
                                )
                                ItemMediaInfo(
                                    label = strings[BIT_DEPTH_MEDIA_INFO],
                                    value = it.bitDepth?.let { "$it-bit" }
                                )
                                Spacer(modifier = Modifier.height(dimens.sm))
                            }
                        }
                        if(videos.isNotEmpty() || audios.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(dimens.md))
                        }
                        if(subtitles.isNotEmpty()) {
                            TitleMediaInfo(strings[SUBTITLE_MEDIA_INFO])
                            Spacer(modifier = Modifier.height(dimens.md))
                            subtitles.forEachIndexed { index, it ->
                                ItemMediaInfo(label = strings[INDEX_MEDIA_INFO], value = index.toString())
                                ItemMediaInfo(label = strings[CODEC_MEDIA_INFO], value = it.codec)
                                ItemMediaInfo(label = strings[TITLE_MEDIA_INFO], value = it.title)
                                ItemMediaInfo(label = strings[LANGUAGE_MEDIA_INFO], value = it.language)
                                Spacer(modifier = Modifier.height(dimens.sm))
                            }
                        }
                    }
                    VerticalScrollbar(
                        adapter = rememberScrollbarAdapter(scroll),
                        style = LocalScrollbarStyle.current.copy(
                            hoverColor = MaterialTheme.colorScheme.surfaceContainer
                        ),
                        modifier = Modifier.fillMaxHeight()
                    )
                }
            },
            onDismissRequest = onFinish,
            onConfirmation = onFinish,
            confirmationText = strings[CONFIRMATION_BUTTON]
        )
    }
}

@Composable
private fun TitleMediaInfo(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(
            color = MaterialTheme.colorScheme.tertiary
        )
    )
}

@Composable
private fun ItemMediaInfo(label: String, value: String?) {
    if(!value.isNullOrBlank()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "$label:", maxLines = 1)
            Spacer(modifier = Modifier.width(dimens.xxs))
            Text(
                text = value,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun MediaType.TypeMediaInfoString() = when(this) {
    MediaType.VIDEO -> strings[VIDEO_MEDIA_INFO]
    MediaType.AUDIO -> strings[AUDIO_MEDIA_INFO]
    MediaType.SUBTITLE -> strings[SUBTITLE_MEDIA_INFO]
}

@Composable
private fun Boolean.toText() = if(this) strings[YES_MEDIA_INFO] else strings[NO_MEDIA_INFO]

@Composable
private fun CompletePreview() = CompleteDialog(startTime = "123", finishTime = "123", duration = "123", onFinish = {})

@Composable
private fun MediaInfoPreview() {
    val videoStreams = List(2) {
        Video(
            codec = "Sample",
            title = "Sample",
            language = "Sample",
            profile = "Sample",
            width = 123,
            height = 123,
            bitDepth = 123,
            pixFmt = "Sample",
            fps = 123f,
            level = 123,
            filmGrain = true,
            displayAspectRatio = "Sample",
            fieldOrder = "Sample"
        )
    }
    val audioStreams = List(2) {
        Audio(
            codec = "Sample",
            title = "Sample",
            language = "Sample",
            profile = "Sample",
            channels = 123,
            sampleRate = 123,
            bitDepth = 123
        )
    }
    val subtitlesStreams = List(2) {
        Subtitle(
            codec = "Sample",
            title = "Sample",
            language = "Sample"
        )
    }
    val inputMedia = InputMedia(
        path = "/home/test/sample.mkv",
        type = MediaType.VIDEO,
        formatName = "Sample",
        duration = 123456L,
        durationText = "123456",
        bitRate = 12345L,
        bitrateText = "12345",
        size = 123456L,
        sizeText = "123456",
        videos = videoStreams,
        audios = audioStreams,
        subtitles = subtitlesStreams
    )

    inputMedia.MediaInfoDialog(onFinish = {})
}

@Preview
@Composable
private fun CompleteEnglishLight() = EnglishLightPreview { CompletePreview() }

@Preview
@Composable
private fun CompleteEnglishDark() = EnglishDarkPreview { CompletePreview() }

@Preview
@Composable
private fun CompletePortugueseLight() = PortugueseLightPreview { CompletePreview() }

@Preview
@Composable
private fun CompletePortugueseDark() = PortugueseDarkPreview { CompletePreview() }

@Preview
@Composable
private fun MediaInfoLight() = PortugueseLightPreview { MediaInfoPreview() }

@Preview
@Composable
private fun MediaInfoDark() = PortugueseDarkPreview { MediaInfoPreview() }