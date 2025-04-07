package edneyosf.edconv.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.intl.Locale
import edneyosf.edconv.app.extensions.getAvailableLanguage
import edneyosf.edconv.features.home.screens.HomeScreen
import edneyosf.edconv.ui.compositions.languagesComp
import edneyosf.edconv.ui.theme.AppTheme

@Composable
fun App() {
    val language = Locale.current.getAvailableLanguage()

    CompositionLocalProvider(
        value = languagesComp provides language
    ) {
        AppTheme { HomeScreen() }
    }
}