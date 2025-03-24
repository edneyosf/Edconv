package app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.intl.Locale
import app.extensions.getAvailableLanguage
import features.home.screens.HomeScreen
import ui.compositions.languagesComp
import ui.theme.AppTheme

@Composable
fun App() {
    val language = Locale.current.getAvailableLanguage()

    CompositionLocalProvider(
        value = languagesComp provides language
    ) {
        AppTheme { HomeScreen() }
    }
}