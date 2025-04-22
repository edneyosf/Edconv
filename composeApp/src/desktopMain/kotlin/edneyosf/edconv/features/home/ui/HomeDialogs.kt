package edneyosf.edconv.features.home.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
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
import edneyosf.edconv.core.extensions.toDurationString
import edneyosf.edconv.core.extensions.toReadableBitrate
import edneyosf.edconv.core.extensions.toReadableSize
import edneyosf.edconv.edconv.common.MediaType
import edneyosf.edconv.edconv.core.data.AudioData
import edneyosf.edconv.edconv.core.data.ContentTypeData
import edneyosf.edconv.edconv.core.data.MediaData
import edneyosf.edconv.edconv.core.data.SubtitleData
import edneyosf.edconv.edconv.core.data.VideoData
import edneyosf.edconv.features.home.events.HomeEvent
import edneyosf.edconv.features.home.events.HomeEvent.OnStart
import edneyosf.edconv.features.home.events.HomeEvent.SetStatus
import edneyosf.edconv.features.home.events.HomeEvent.SetDialog
import edneyosf.edconv.features.home.states.HomeDialog
import edneyosf.edconv.features.home.states.HomeState
import edneyosf.edconv.features.home.states.HomeStatus
import edneyosf.edconv.features.home.strings.homeDialogStrings
import edneyosf.edconv.features.home.strings.HomeDialogStrings.Keys.*
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Keys.DEFAULT_ERROR
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
fun HomeState.Dialogs(onEvent: (HomeEvent) -> Unit) {
    when (this.status) {
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

    when(this.dialog) {
        is HomeDialog.Settings -> SettingsDialog { onEvent(SetDialog(HomeDialog.None)) }

        is HomeDialog.MediaInfo -> {
            dialog.mediaData.MediaInfoDialog(
                onFinish = { onEvent(SetDialog(HomeDialog.None)) }
            )
        }

        else -> Unit
    }
}

@Composable
private fun ErrorDialog(message: String, onFinish: () -> Unit) {
    CompositionLocalProvider(stringsComp provides homeDialogStrings) {
        SimpleDialog(
            icon = Icons.Rounded.Error,
            title = strings[ERROR_TITLE],
            content = { Text(message) },
            onConfirmation = onFinish,
            confirmationText = strings[CONFIRMATION_BUTTON]
        )
    }
}

@Composable
private fun OverwriteFileDialog(onCancel: () -> Unit, onConfirmation: () -> Unit) {
    CompositionLocalProvider(stringsComp provides homeDialogStrings) {
        SimpleDialog(
            icon = Icons.Rounded.Warning,
            title = strings[WARNING_TITLE],
            content = { Text(strings[OVERWRITE_FILE]) },
            confirmationText = strings[CONFIRMATION_BUTTON],
            onConfirmation = onConfirmation,
            cancelText = strings[CANCEL_BUTTON],
            onCancel = onCancel
        )
    }
}

@Composable
private fun CompleteDialog(startTime: String, finishTime: String, duration: String, onFinish: () -> Unit) {
    CompositionLocalProvider(stringsComp provides homeDialogStrings) {
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
private fun MediaData.MediaInfoDialog(onFinish: () -> Unit) {
    val scroll = rememberScrollState()

    CompositionLocalProvider(stringsComp provides homeDialogStrings) {
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
                        ItemMediaInfo(label = strings[DURATION_MEDIA_INFO], value = duration?.toDurationString())
                        ItemMediaInfo(label = strings[BITRATE_MEDIA_INFO], value = bitRate?.toReadableBitrate())
                        ItemMediaInfo(label = strings[SIZE_MEDIA_INFO], value = size.toReadableSize())
                        if(contentType.video || contentType.audio || contentType.subtitle) {
                            Spacer(modifier = Modifier.height(dimens.xl))
                        }
                        if(contentType.video) {
                            TitleMediaInfo(strings[VIDEO_MEDIA_INFO])
                            Spacer(modifier = Modifier.height(dimens.md))
                            videoStreams.forEachIndexed { index, it ->
                                ItemMediaInfo(label = strings[INDEX_MEDIA_INFO], value = index.toString())
                                ItemMediaInfo(label = strings[CODEC_MEDIA_INFO], value = it.codec)
                                ItemMediaInfo(label = strings[TITLE_MEDIA_INFO], value = it.title)
                                ItemMediaInfo(label = strings[LANGUAGE_MEDIA_INFO], value = it.language)
                                ItemMediaInfo(label = strings[PROFILE_MEDIA_INFO], value = it.profile)
                                ItemMediaInfo(label = strings[WIDTH_MEDIA_INFO], value = it.width?.toString())
                                ItemMediaInfo(
                                    label = strings[HEIGHT_MEDIA_INFO],
                                    value = it.height?.toString()
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
                        if(contentType.video) Spacer(modifier = Modifier.height(dimens.md))
                        if(contentType.audio) {
                            TitleMediaInfo(strings[AUDIO_MEDIA_INFO])
                            Spacer(modifier = Modifier.height(dimens.md))
                            audioStreams.forEachIndexed { index, it ->
                                ItemMediaInfo(label = strings[INDEX_MEDIA_INFO], value = index.toString())
                                ItemMediaInfo(label = strings[CODEC_MEDIA_INFO], value = it.codec)
                                ItemMediaInfo(label = strings[TITLE_MEDIA_INFO], value = it.title)
                                ItemMediaInfo(label = strings[LANGUAGE_MEDIA_INFO], value = it.language)
                                ItemMediaInfo(label = strings[PROFILE_MEDIA_INFO], value = it.profile)
                                ItemMediaInfo(label = strings[CHANNELS_MEDIA_INFO], value = it.channels?.toString())
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
                        if(contentType.video || contentType.audio) {
                            Spacer(modifier = Modifier.height(dimens.md))
                        }
                        if(contentType.subtitle) {
                            TitleMediaInfo(strings[SUBTITLE_MEDIA_INFO])
                            Spacer(modifier = Modifier.height(dimens.md))
                            subtitleStreams.forEachIndexed { index, it ->
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
}

@Composable
private fun Boolean.toText() = if(this) strings[YES_MEDIA_INFO] else strings[NO_MEDIA_INFO]

@Composable
private fun ErrorPreview() = ErrorDialog("Sample", onFinish = {})

@Composable
private fun OverwriteFilePreview() = OverwriteFileDialog(onConfirmation = {}, onCancel = {})

@Composable
private fun CompletePreview() = CompleteDialog(startTime = "123", finishTime = "123", duration = "123", onFinish = {})

@Composable
private fun MediaInfoPreview() {
    val videoStreams = List(2) {
        VideoData(
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
        AudioData(
            codec = "Sample",
            title = "Sample",
            language = "Sample",
            profile = "Sample",
            channels = 123,
            sampleRate = 123,
            bitDepth = 123
        )
    }
    val subtitles = List(2) {
        SubtitleData(
            codec = "Sample",
            title = "Sample",
            language = "Sample"
        )
    }
    val mediaData = MediaData(
        path = "/home/test/sample.mkv",
        type = MediaType.VIDEO,
        formatName = "Sample",
        contentType = ContentTypeData(video = true, audio = true, subtitle = true),
        duration = 123456L,
        bitRate = 12345L,
        size = 123456L,
        videoStreams = videoStreams,
        audioStreams = audioStreams,
        subtitleStreams = subtitles
    )

    mediaData.MediaInfoDialog(onFinish = {})
}

@Preview
@Composable
private fun ErrorEnglishLight() = EnglishLightPreview { ErrorPreview() }

@Preview
@Composable
private fun ErrorEnglishDark() = EnglishDarkPreview { ErrorPreview() }

@Preview
@Composable
private fun ErrorPortugueseLight() = PortugueseLightPreview { ErrorPreview() }

@Preview
@Composable
private fun ErrorPortugueseDark() = PortugueseDarkPreview { ErrorPreview() }

@Preview
@Composable
private fun OverwriteFileEnglishLight() = EnglishLightPreview { OverwriteFilePreview() }

@Preview
@Composable
private fun OverwriteFileEnglishDark() = EnglishDarkPreview { OverwriteFilePreview() }

@Preview
@Composable
private fun OverwriteFilePortugueseLight() = PortugueseLightPreview { OverwriteFilePreview() }

@Preview
@Composable
private fun OverwriteFilePortugueseDark() = PortugueseDarkPreview { OverwriteFilePreview() }

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