package edneyosf.edconv.features.home.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.draganddrop.dragAndDropTarget
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import edneyosf.edconv.core.utils.PropertyUtils
import edneyosf.edconv.features.converter.ui.ConverterScreen
import edneyosf.edconv.features.home.HomeEvent
import edneyosf.edconv.features.home.HomeViewModel
import edneyosf.edconv.features.home.states.HomeDialogState
import edneyosf.edconv.features.home.states.HomeNavigationState
import edneyosf.edconv.features.home.states.HomeState
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Keys.*
import edneyosf.edconv.features.home.strings.homeScreenStrings
import edneyosf.edconv.features.nomedia.NoMediaScreen
import edneyosf.edconv.features.metrics.ui.MetricsScreen
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.compositions.strings
import edneyosf.edconv.ui.compositions.stringsComp
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview
import edneyosf.edconv.features.common.commonStrings
import edneyosf.edconv.features.common.CommonStrings.Keys.VERSION
import edneyosf.edconv.ui.compositions.fileKitDialogSettings
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen() {
    val viewModel = koinViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsState()

    CompositionLocalProvider(value = stringsComp provides homeScreenStrings) {
        state.Content(event = viewModel)
        state.Dialogs(event = viewModel)
    }
}

@Composable
private fun HomeState.Content(event: HomeEvent) {
    val version = remember { PropertyUtils.version }
    val singleFilePicker = rememberFilePickerLauncher(
        title = strings[TITLE_PICK_FILE],
        onResult = { it?.file?.let { file -> event.setInput(path = file.absolutePath) } },
        dialogSettings = fileKitDialogSettings
    )
    val modifier = Modifier
        .dragAndDropTarget(
            shouldStartDragAndDrop = { true },
            target = buildDropTarget(homeEvent = event)
        )

    Scaffold(modifier = modifier) { innerPadding ->
        Row(modifier = Modifier.padding(paddingValues = innerPadding)) {
            HomeNavigation(
                onSelected = event::setNavigation,
                onSettings = { event.setDialog(state = HomeDialogState.Settings) },
                onPickFile = { singleFilePicker.launch() }
            )
            when {
                loading -> Loading(appVersion = version)
                input == null -> NoMediaScreen(appVersion = version)
                navigation is HomeNavigationState.Media -> ConverterScreen()
                navigation is HomeNavigationState.Metrics -> MetricsScreen()
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

private fun buildDropTarget(homeEvent: HomeEvent): DragAndDropTarget =
    object : DragAndDropTarget {
        override fun onDrop(event: DragAndDropEvent): Boolean {
            return homeEvent.onDragAndDropInput(event)
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