package edneyosf.edconv.features.home.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FileOpen
import androidx.compose.material.icons.rounded.NewReleases
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VolunteerActivism
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import edneyosf.edconv.core.utils.PropertyUtils
import edneyosf.edconv.features.common.models.Audio
import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.features.common.models.Video
import edneyosf.edconv.features.home.HomeEvent
import edneyosf.edconv.ffmpeg.common.MediaType
import edneyosf.edconv.features.home.states.HomeNavigationState
import edneyosf.edconv.features.home.states.HomeState
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Keys.*
import edneyosf.edconv.features.home.strings.homeScreenStrings
import edneyosf.edconv.ui.components.TextTooltip
import edneyosf.edconv.ui.components.VibratingIcon
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.compositions.strings
import edneyosf.edconv.ui.compositions.stringsComp
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview

@Composable
fun HomeState.HomeNavigation(
    event: HomeEvent,
    onSelected: (HomeNavigationState) -> Unit,
    onSettings: () -> Unit,
    onPickFile: () -> Unit
) {
    val version = remember { PropertyUtils.version }
    val items = listOf(
        Triple(
            first = HomeNavigationState.Media,
            second = Icons.Rounded.VideoLibrary,
            third = strings[MEDIA_NAVIGATION_ITEM]
        ),
        Triple(
            first = HomeNavigationState.Metrics,
            second =  Icons.Rounded.Visibility,
            third =  strings[METRICS_NAVIGATION_ITEM]
        )
    )

    NavigationRail(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest) {
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
        items.forEachIndexed { _, item ->
            val state = item.first
            val hasAudio = input?.audios?.isNotEmpty()
            val hasVideo = input?.videos?.isNotEmpty()
            val enabled = when(state) {
                is HomeNavigationState.Media -> hasAudio == true || hasVideo == true
                is HomeNavigationState.Metrics -> hasVideo == true
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
        if(!version.isNullOrBlank() && !latestVersion.isNullOrBlank()) {
            val text = "v$latestVersion ${strings[VERSION_AVAILABLE]}"

            TextTooltip(text = text) {
                IconButton(
                    onClick = { if (!releasesUrl.isNullOrBlank()) event.openLink(releasesUrl) }
                ) {
                    VibratingIcon(enabled = latestVersion != version) {
                        Icon(
                            imageVector = Icons.Rounded.NewReleases,
                            tint = MaterialTheme.colorScheme.tertiary,
                            contentDescription = text
                        )
                    }
                }
            }
        }
        if(!donationUrl.isNullOrBlank()) {
            TextTooltip(text = strings[DONATION]) {
                IconButton(onClick = { event.openLink(url = donationUrl) }) {
                    Icon(
                        imageVector = Icons.Rounded.VolunteerActivism,
                        tint = MaterialTheme.colorScheme.tertiary,
                        contentDescription = strings[DONATION]
                    )
                }
            }
        }
        TextTooltip(text = strings[SETTINGS]) {
            IconButton(enabled = !loading, onClick = { onSettings() }) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = strings[SETTINGS]
                )
            }
        }
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

        HomeState(navigation = HomeNavigationState.Media, input = inputData)
            .HomeNavigation(
                event = object : HomeEvent{},
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