package edneyosf.edconv.features.vmaf.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import edneyosf.edconv.core.extensions.LaunchedEffected
import edneyosf.edconv.features.converter.strings.ConverterScreenStrings.Keys.OUTPUT_FILE
import edneyosf.edconv.features.vmaf.strings.VMAFScreenStrings.Keys.*
import edneyosf.edconv.features.vmaf.VMAFArgs
import edneyosf.edconv.features.vmaf.events.VMAFEvent
import edneyosf.edconv.features.vmaf.states.VMAFState
import edneyosf.edconv.features.vmaf.strings.vmafScreenStrings
import edneyosf.edconv.features.vmaf.viewmodels.VMAFViewModel
import edneyosf.edconv.ui.components.ActionsTool
import edneyosf.edconv.ui.components.extensions.custom
import edneyosf.edconv.ui.compositions.dimens
import edneyosf.edconv.ui.compositions.strings
import edneyosf.edconv.ui.compositions.stringsComp


@Composable
fun VMAFScreen(args: VMAFArgs) {
    val viewModel = viewModel { VMAFViewModel(input = args.input) }
    val state by viewModel.state

    LaunchedEffected(key = args) { viewModel.refresh(newInput = it.input) }

    CompositionLocalProvider(value = stringsComp provides vmafScreenStrings) {
        state.Content(
            event = viewModel
        )
    }
}

@Composable
private fun VMAFState.Content(event: VMAFEvent) {
    Column(
        modifier = Modifier.padding(all = dimens.md),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = dimens.xl)
    ) {
        ActionsTool(
            startEnabled = true,
            stopEnabled = true,
            startDescription = strings[START_CONVERSION],
            stopDescription = strings[STOP_CONVERSION],
            onStart = {},
            onStop = {}
        )
        TextField(
            value = input.videos.first().width.toString(),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors().custom(),
            onValueChange = { /*event.setOutput(it)*/ },
            label = { Text(text = strings[OUTPUT_FILE]) }
        )
        TextField(
            value = input.videos.first().height.toString(),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors().custom(),
            onValueChange = { /*event.setOutput(it)*/ },
            label = { Text(text = strings[OUTPUT_FILE]) }
        )
        TextField(
            value = threads.toString(),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors().custom(),
            onValueChange = { /*event.setOutput(it)*/ },
            label = { Text(text = strings[OUTPUT_FILE]) }
        )
    }
}