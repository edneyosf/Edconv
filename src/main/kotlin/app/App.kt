package app

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.intl.Locale
import core.common.getAvailableLanguage
import features.home.screens.HomeScreen
import ui.compositions.languages

@Composable
fun App() {
    val language = Locale.current.getAvailableLanguage()

    CompositionLocalProvider(languages provides language) {
        MaterialTheme { HomeScreen() }
    }
}