package edneyosf.edconv.feature.home

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edneyosf.edconv.core.utils.PropertyUtils
import edneyosf.edconv.feature.converter.ui.ConverterScreen
import edneyosf.edconv.feature.home.states.HomeDialogState
import edneyosf.edconv.feature.home.states.HomeNavigationState
import edneyosf.edconv.feature.home.states.HomeState
import edneyosf.edconv.feature.home.strings.HomeScreenStrings.Keys.*
import edneyosf.edconv.feature.home.strings.homeScreenStrings
import edneyosf.edconv.feature.nomedia.NoMediaScreen
import edneyosf.edconv.feature.metrics.ui.MetricsScreen
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.compositions.strings
import edneyosf.edconv.ui.compositions.stringsComp
import edneyosf.edconv.ui.previews.EnglishDarkPreview
import edneyosf.edconv.ui.previews.EnglishLightPreview
import edneyosf.edconv.ui.previews.PortugueseDarkPreview
import edneyosf.edconv.ui.previews.PortugueseLightPreview
import edneyosf.edconv.feature.common.commonStrings
import edneyosf.edconv.feature.common.CommonStrings.Keys.VERSION
import edneyosf.edconv.feature.common.models.InputMedia
import edneyosf.edconv.feature.home.composables.Dialogs
import edneyosf.edconv.feature.home.composables.HomeNavigation
import org.koin.compose.viewmodel.koinViewModel
import edneyosf.edconv.ui.filekit.rememberFilesPickerLauncher
import io.github.vinceglb.filekit.PlatformFile

@Composable
fun HomeScreen() {
    val viewModel = koinViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val inputs by viewModel.inputs.collectAsStateWithLifecycle()

    CompositionLocalProvider(value = stringsComp provides homeScreenStrings) {
        state.Content(inputs, event = viewModel)
        state.Dialogs(event = viewModel)
    }
}

@Composable
private fun HomeState.Content(inputs: List<InputMedia>, event: HomeAction) {
    val version = remember { PropertyUtils.version }
    val singleFilePicker = rememberFilesPickerLauncher(
        title = strings[TITLE_PICK_FILE],
        onResult = { event.setInputs(it.toPaths()) }
    )
    val modifier = Modifier
        .dragAndDropTarget(
            shouldStartDragAndDrop = { true },
            target = buildDropTarget(homeAction = event)
        )

    Scaffold(modifier = modifier) { innerPadding ->
        Row(modifier = Modifier.padding(paddingValues = innerPadding)) {
            HomeNavigation(
                event = event,
                inputs = inputs,
                onSelected = event::setNavigation,
                onSettings = { event.setDialog(state = HomeDialogState.Settings) },
                onPickFile = { singleFilePicker.launch() }
            )
            when {
                loading -> Loading(appVersion = version)
                inputs.isEmpty() -> NoMediaScreen(appVersion = version)
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

private fun buildDropTarget(homeAction: HomeAction): DragAndDropTarget =
    object : DragAndDropTarget {
        override fun onDrop(event: DragAndDropEvent): Boolean {
            return homeAction.onDragAndDropInput(event)
        }
    }

private fun List<PlatformFile>?.toPaths(): List<String> {
    val paths = mutableListOf<String>()

    this?.forEach { paths.add(it.file.absolutePath) }

    return paths
}

@Composable
private fun DefaultPreview() {
    CompositionLocalProvider(value = stringsComp provides homeScreenStrings) {
        HomeState().Content(
            inputs = listOf(),
            event = object : HomeAction {}
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