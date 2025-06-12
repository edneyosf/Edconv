package edneyosf.edconv.features.home.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FileOpen
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import edneyosf.edconv.features.common.models.Audio
import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.features.common.models.Video
import edneyosf.edconv.ffmpeg.common.MediaType
import edneyosf.edconv.features.home.states.HomeNavigationState
import edneyosf.edconv.features.home.states.HomeState
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Keys.*
import edneyosf.edconv.features.home.strings.homeScreenStrings
import edneyosf.edconv.ui.components.TextTooltip
import edneyosf.edconv.ui.components.extensions.customColor
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.compositions.strings
import edneyosf.edconv.ui.compositions.stringsComp
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview

@Composable
fun HomeState.HomeNavigation(
    onSelected: (HomeNavigationState) -> Unit, onSettings: () -> Unit, onPickFile: () -> Unit
) {
    val items = listOf(
        Triple(
            first = HomeNavigationState.Audio,
            second = Icons.Rounded.MusicNote,
            third = strings[AUDIO_NAVIGATION_ITEM]
        ),
        Triple(
            first =  HomeNavigationState.Video,
            second = Icons.Rounded.Videocam,
            third = strings[VIDEO_NAVIGATION_ITEM]
        ),
        Triple(
            first = HomeNavigationState.Vmaf,
            second =  Icons.Rounded.Visibility,
            third =  strings[VMAF_NAVIGATION_ITEM]
        )
    )

    Row {
        NavigationRail {
            TextTooltip(text = strings[SELECT_MEDIA_FILE]) {
                FilledTonalIconButton(
                    enabled = !loading,
                    onClick = onPickFile
                ) {
                    BadgedBox(
                        badge = { input?.let { Badge() } }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.FileOpen,
                            contentDescription = strings[SELECT_MEDIA_FILE]
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(height = dimens.xl))
            items.forEachIndexed { index, item ->
                val state = item.first
                val hasAudio = input?.audios?.isNotEmpty()
                val hasVideo = input?.videos?.isNotEmpty()
                val enabled = when(state) {
                    is HomeNavigationState.Audio -> hasAudio == true
                    is HomeNavigationState.Video -> hasVideo == true
                    is HomeNavigationState.Vmaf -> hasVideo == true
                    else -> false
                }

                NavigationRailItem(
                    label = { Text(text = item.third) },
                    enabled = enabled && !loading,
                    selected = navigation == state,
                    onClick = { onSelected(state) },
                    icon = {
                        Icon(
                            imageVector = item.second,
                            contentDescription = null
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.weight(weight = 1f))
            TextTooltip(text = strings[SETTINGS]) {
                IconButton(enabled = !loading, onClick = { onSettings() }) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = strings[SETTINGS]
                    )
                }
            }
        }
        VerticalDivider(color = DividerDefaults.customColor())
    }
}

@Composable
private fun DefaultPreview() {
    CompositionLocalProvider(value = stringsComp provides homeScreenStrings) {
        val inputData = InputMedia(
            path = "Sample",
            type = MediaType.AUDIO,
            size = 123L,
            sizeText = "123",
            duration = 123L,
            durationText = "123",
            audios = listOf(Audio(channels = 2)),
            videos = listOf(Video(height = 123, width = 123))
        )

        HomeState(navigation = HomeNavigationState.Audio, input = inputData)
            .HomeNavigation(
                onSelected = {},
                onSettings = {},
                onPickFile = {}
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