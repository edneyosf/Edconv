package app

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.intl.Locale
import app.extensions.getAvailableLanguage
import features.home.screens.HomeScreen
import ui.compositions.languagesComp

@Composable
fun App() {
    val language = Locale.current.getAvailableLanguage()

    CompositionLocalProvider(languagesComp provides language) {
        MaterialTheme { HomeScreen() }
    }
}