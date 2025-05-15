package edneyosf.edconv.features.converter.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import edneyosf.edconv.features.common.CommonStrings.Keys.CONFIRMATION_BUTTON
import edneyosf.edconv.features.common.commonStrings
import edneyosf.edconv.features.common.models.Audio
import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.features.common.models.Subtitle
import edneyosf.edconv.features.common.models.Video
import edneyosf.edconv.features.converter.strings.ConverterMediaInfoDialogStrings.Keys.*
import edneyosf.edconv.features.converter.strings.converterMediaInfoDialogStrings
import edneyosf.edconv.ffmpeg.common.MediaType
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
fun InputMedia.ConverterMediaInfoDialog(onFinish: () -> Unit) {
    val scroll = rememberScrollState()

    CompositionLocalProvider(value = stringsComp provides converterMediaInfoDialogStrings) {
        SimpleDialog(
            icon = Icons.Rounded.Info,
            title = strings[TITLE],
            content = {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(state = scroll)
                            .weight(weight = 1f)
                            .padding(end = dimens.sm)
                    ) {
                        if(path.isNotBlank()) {
                            val file = File(path)

                            ItemMediaInfo(label = strings[FILE], value = file.name)
                        }
                        ItemMediaInfo(label = strings[TYPE], value = type.TypeMediaInfoString())
                        ItemMediaInfo(label = strings[FORMAT], value = formatName)
                        ItemMediaInfo(label = strings[DURATION], value = durationText)
                        ItemMediaInfo(label = strings[BITRATE], value = bitrateText)
                        ItemMediaInfo(label = strings[SIZE], value = sizeText)
                        if(videos.isNotEmpty() || audios.isNotEmpty() || subtitles.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(height = dimens.xl))
                        }
                        if(videos.isNotEmpty()) {
                            TitleMediaInfo(text = strings[VIDEO])
                            Spacer(modifier = Modifier.height(height = dimens.md))
                            videos.forEachIndexed { index, it ->
                                ItemMediaInfo(label = strings[INDEX], value = index.toString())
                                ItemMediaInfo(label = strings[CODEC], value = it.codec)
                                ItemMediaInfo(label = strings[STREAM_TITLE], value = it.title)
                                ItemMediaInfo(label = strings[LANGUAGE], value = it.language)
                                ItemMediaInfo(label = strings[PROFILE], value = it.profile)
                                ItemMediaInfo(label = strings[WIDTH], value = it.width.toString())
                                ItemMediaInfo(
                                    label = strings[HEIGHT],
                                    value = it.height.toString()
                                )
                                ItemMediaInfo(
                                    label = strings[BIT_DEPTH],
                                    value = it.bitDepth?.let { "$it-bit" }
                                )
                                ItemMediaInfo(label = strings[PIX_FMT], value = it.pixFmt)
                                ItemMediaInfo(label = strings[FPS], value = it.fps?.toString())
                                ItemMediaInfo(label = strings[LEVEL], value = it.level?.toString())
                                ItemMediaInfo(label = strings[FILM_GRAIN], value = it.filmGrain.toText())
                                ItemMediaInfo(
                                    label = strings[DISPLAY_ASPECT_RATIO],
                                    value = it.displayAspectRatio
                                )
                                ItemMediaInfo(label = strings[FIELD_ORDER], value = it.fieldOrder)
                                Spacer(modifier = Modifier.height(height = dimens.sm))
                            }
                        }
                        if(videos.isNotEmpty()) Spacer(modifier = Modifier.height(height = dimens.md))
                        if(audios.isNotEmpty()) {
                            TitleMediaInfo(text = strings[AUDIO])
                            Spacer(modifier = Modifier.height(height = dimens.md))
                            audios.forEachIndexed { index, it ->
                                ItemMediaInfo(label = strings[INDEX], value = index.toString())
                                ItemMediaInfo(label = strings[CODEC], value = it.codec)
                                ItemMediaInfo(label = strings[STREAM_TITLE], value = it.title)
                                ItemMediaInfo(label = strings[LANGUAGE], value = it.language)
                                ItemMediaInfo(label = strings[PROFILE], value = it.profile)
                                ItemMediaInfo(label = strings[CHANNELS], value = it.channels.toString())
                                ItemMediaInfo(
                                    label = strings[SAMPLE_RATE],
                                    value = it.sampleRate?.let { "$it Hz" }
                                )
                                ItemMediaInfo(
                                    label = strings[BIT_DEPTH],
                                    value = it.bitDepth?.let { "$it-bit" }
                                )
                                Spacer(modifier = Modifier.height(height = dimens.sm))
                            }
                        }
                        if(videos.isNotEmpty() || audios.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(height = dimens.md))
                        }
                        if(subtitles.isNotEmpty()) {
                            TitleMediaInfo(text = strings[SUBTITLE])
                            Spacer(modifier = Modifier.height(height = dimens.md))
                            subtitles.forEachIndexed { index, it ->
                                ItemMediaInfo(label = strings[INDEX], value = index.toString())
                                ItemMediaInfo(label = strings[CODEC], value = it.codec)
                                ItemMediaInfo(label = strings[STREAM_TITLE], value = it.title)
                                ItemMediaInfo(label = strings[LANGUAGE], value = it.language)
                                Spacer(modifier = Modifier.height(height = dimens.sm))
                            }
                        }
                    }
                    VerticalScrollbar(
                        adapter = rememberScrollbarAdapter(scrollState = scroll),
                        style = LocalScrollbarStyle.current.copy(
                            hoverColor = MaterialTheme.colorScheme.surfaceContainer
                        ),
                        modifier = Modifier.fillMaxHeight()
                    )
                }
            },
            onDismissRequest = onFinish,
            onConfirmation = onFinish,
            confirmationText = commonStrings[CONFIRMATION_BUTTON]
        )
    }
}

@Composable
private fun TitleMediaInfo(text: String) {
    val color = MaterialTheme.colorScheme.tertiary
    val style = MaterialTheme.typography.titleMedium.copy(color = color)

    Text(text = text, style = style)
}

@Composable
private fun ItemMediaInfo(label: String, value: String?) {
    if(!value.isNullOrBlank()) {
        val valueStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "$label:", maxLines = 1)
            Spacer(modifier = Modifier.width(width = dimens.xxs))
            Text(
                text = value,
                style = valueStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun MediaType.TypeMediaInfoString() = when(this) {
    MediaType.VIDEO -> strings[VIDEO]
    MediaType.AUDIO -> strings[AUDIO]
    MediaType.SUBTITLE -> strings[SUBTITLE]
}

@Composable
private fun Boolean.toText() = if(this) strings[YES] else strings[NO]

@Composable
private fun DefaultPreview() {
    val videoStreams = List(size = 1) {
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
    val audioStreams = List(size = 1) {
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
    val subtitlesStreams = List(size = 1) {
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

    inputMedia.ConverterMediaInfoDialog(onFinish = {})
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