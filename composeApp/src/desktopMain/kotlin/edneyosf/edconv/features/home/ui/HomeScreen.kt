package edneyosf.edconv.features.home.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import edneyosf.edconv.core.extensions.LaunchedEffected
import edneyosf.edconv.core.utils.PropertyUtils
import edneyosf.edconv.ffmpeg.common.MediaType
import edneyosf.edconv.features.converter.ui.ConverterScreen
import edneyosf.edconv.features.home.events.HomeEvent
import edneyosf.edconv.features.home.viewmodels.HomeViewModel
import edneyosf.edconv.features.home.states.HomeDialogState
import edneyosf.edconv.features.home.states.HomeNavigationState
import edneyosf.edconv.features.home.states.HomeState
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Keys.*
import edneyosf.edconv.features.home.strings.homeScreenStrings
import edneyosf.edconv.features.nomedia.ui.NoMediaScreen
import edneyosf.edconv.features.vmaf.ui.VMAFScreen
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.compositions.strings
import edneyosf.edconv.ui.compositions.stringsComp
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview
import edneyosf.edconv.features.common.commonStrings
import edneyosf.edconv.features.common.CommonStrings.Keys.VERSION
import edneyosf.edconv.features.converter.ConverterArgs

@Composable
fun HomeScreen() {
    val manager = viewModel { HomeViewModel() }
    val state by manager.state

    LaunchedEffected(key = state.input) {
        val navigation = when(it?.type) {
            MediaType.AUDIO -> HomeNavigationState.Audio
            MediaType.VIDEO -> HomeNavigationState.Video
            else -> HomeNavigationState.Initial
        }

        manager.setNavigation(navigation)
    }

    CompositionLocalProvider(value = stringsComp provides homeScreenStrings) {
        state.Content(event = manager)
        state.Dialogs(event = manager)
    }
}

@Composable
private fun HomeState.Content(event: HomeEvent) {
    val version = remember { PropertyUtils.version }
    val stringPickFile = strings[TITLE_PICK_FILE]

    Scaffold { innerPadding ->
        Row(modifier = Modifier.padding(paddingValues = innerPadding)) {
            HomeNavigation(
                onSelected = { event.setNavigation(state = it) },
                onSettings = { event.setDialog(state = HomeDialogState.Settings) },
                onPickFile = { event.pickFile(title = stringPickFile) }
            )
            when {
                loading -> Loading(appVersion = version)
                input == null -> NoMediaScreen(appVersion = version)
                navigation is HomeNavigationState.Audio -> {
                    val argument = ConverterArgs(
                        input = input,
                        mediaType = MediaType.AUDIO
                    )

                    ConverterScreen(argument = argument)
                }
                navigation is HomeNavigationState.Video -> {
                    val argument = ConverterArgs(
                        input = input,
                        mediaType = MediaType.VIDEO
                    )

                    ConverterScreen(argument = argument)
                }
                navigation is HomeNavigationState.VMAF -> VMAFScreen()
            }
        }
    }
}

@Composable
private fun Loading(appVersion: String?) {
    Column(
        modifier = Modifier.fillMaxSize().padding(all = dimens.md),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(weight = 1f))
        CircularProgressIndicator()
        Spacer(modifier = Modifier.weight(weight = 1f))
        if(appVersion != null) Text(text = "${commonStrings[VERSION]} $appVersion")
    }
}

@Composable
private fun DefaultPreview() {
    CompositionLocalProvider(value = stringsComp provides homeScreenStrings) {
        HomeState().Content(event = object : HomeEvent {})
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