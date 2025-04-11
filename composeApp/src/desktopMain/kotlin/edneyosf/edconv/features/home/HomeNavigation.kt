package edneyosf.edconv.features.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FileOpen
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edneyosf.edconv.edconv.common.MediaType
import edneyosf.edconv.edconv.core.data.MediaData
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Companion.AUDIO_MEDIA_TYPE
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Companion.TITLE_PICK_FILE
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Companion.VIDEO_MEDIA_TYPE
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.compositions.strings

@Composable
fun Navigation(
    selected: MediaType?, input: MediaData?, onSelected: (MediaType) -> Unit, pickFileEnabled: Boolean = true,
    onPickFile: () -> Unit, onSettings: () -> Unit) {

    val mediaTypes = listOf(strings[AUDIO_MEDIA_TYPE], strings[VIDEO_MEDIA_TYPE])
    val icons = listOf(Icons.Rounded.MusicNote, Icons.Rounded.Videocam)

    NavigationRail {
        FilledTonalIconButton(
            enabled = pickFileEnabled,
            onClick = onPickFile
        ) {
            BadgedBox(badge = { input?.let { Badge() } }) {
                Icon(Icons.Rounded.FileOpen, contentDescription = strings[TITLE_PICK_FILE])
            }
        }
        Spacer(modifier = Modifier.height(dimens.xl))
        mediaTypes.forEachIndexed { index, item ->
            NavigationRailItem(
                icon = { Icon(icons[index], contentDescription = null) },
                label = { Text(item) },
                enabled = input?.type?.index == index,
                selected = selected?.index == index,
                onClick = { MediaType.fromIndex(index)?.let { onSelected(it) } }
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { onSettings() }) {
            Icon(Icons.Rounded.Settings, contentDescription = null)
        }
    }
}