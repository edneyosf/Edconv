package edneyosf.edconv.features.queue.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.window.Window
import edneyosf.edconv.app.extensions.getAvailableLanguage
import edneyosf.edconv.core.process.MediaQueue
import edneyosf.edconv.features.queue.QueueViewModel
import edneyosf.edconv.ui.compositions.languageComp
import edneyosf.edconv.ui.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun QueueScreen(onClose: () -> Unit) {
    val viewModel = koinViewModel<QueueViewModel>()
    val state by viewModel.state.collectAsState()

    state.Content(onClose)
}

@Composable
private fun List<MediaQueue>.Content(onClose: () -> Unit) {
    val language = Locale.current.getAvailableLanguage()

    Window(
        onCloseRequest = onClose,
        title = "Fila"
    ) {
        CompositionLocalProvider(value = languageComp provides language) {
            AppTheme {
                LazyColumn {
                    items(this@Content){ item ->
                        HorizontalDivider()
                        Text("Nome: ${item.input.path}")
                        Text("Comando: ${item.command}")
                        Text("Status: ${item.status.name}")
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}