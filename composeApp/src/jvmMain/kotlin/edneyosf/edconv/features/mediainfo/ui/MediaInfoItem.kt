package edneyosf.edconv.features.mediainfo.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Subtitles
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import edneyosf.edconv.features.mediainfo.strings.mediaInfoItemStrings
import edneyosf.edconv.features.mediainfo.strings.MediaInfoItemStrings.Keys.*
import edneyosf.edconv.ffmpeg.common.MediaType
import edneyosf.edconv.ui.components.TextTooltip
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.compositions.strings
import edneyosf.edconv.ui.compositions.stringsComp
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview

@Composable
fun MediaInfoItem(
    selected: Boolean = false,
    mediaType: MediaType,
    fileName: String,
    onClick: (() -> Unit)? = null,
    onRemove: (() -> Unit)? = null
) {
    val backgroundColor = if(selected) MaterialTheme.colorScheme.surfaceContainerHigh else Color.Transparent
    val modifier = Modifier
        .fillMaxWidth()
        .height(height = dimens.jumbo)
        .background(color = backgroundColor)
        .clickable { onClick?.invoke() }
        .padding(horizontal = dimens.xs)

    CompositionLocalProvider(value = stringsComp provides mediaInfoItemStrings) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            Icon(
                imageVector = mediaType.toIcon(),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = strings[mediaType.toIconString()]
            )
            Spacer(modifier = Modifier.width(width = dimens.xs))
            Column(modifier = Modifier.weight(weight = 1f)) {
                Text(
                    text = fileName,
                    style = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
            TextTooltip(text = strings[REMOVE]) {
                IconButton(
                    onClick = { onRemove?.invoke() }
                ) {
                    Icon(
                        modifier = Modifier.size(size = dimens.ml),
                        imageVector = Icons.Rounded.Close,
                        contentDescription = strings[REMOVE]
                    )
                }
            }
        }
    }
}

private fun MediaType.toIcon() = when(this) {
    MediaType.AUDIO -> Icons.Rounded.MusicNote
    MediaType.VIDEO -> Icons.Rounded.Videocam
    MediaType.SUBTITLE -> Icons.Rounded.Subtitles
}

private fun MediaType.toIconString() = when(this) {
    MediaType.AUDIO -> AUDIO
    MediaType.VIDEO -> VIDEO
    MediaType.SUBTITLE -> SUBTITLE
}

@Composable
private fun DefaultPreview() {
    Column {
        MediaInfoItem(
            selected = true,
            mediaType = MediaType.SUBTITLE,
            fileName = "File.ed"
        )
        MediaInfoItem(
            mediaType = MediaType.AUDIO,
            fileName = "File.ed"
        )
        MediaInfoItem(
            mediaType = MediaType.AUDIO,
            fileName = "File.ed"
        )
        MediaInfoItem(
            mediaType = MediaType.VIDEO,
            fileName = "File.ed"
        )
        MediaInfoItem(
            mediaType = MediaType.VIDEO,
            fileName = "File.ed"
        )
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