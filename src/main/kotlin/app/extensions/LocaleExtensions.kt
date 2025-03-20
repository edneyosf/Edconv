package app.extensions

import core.Configs
import core.Languages
import androidx.compose.ui.text.intl.Locale

fun Locale.getAvailableLanguage() = when(val currentLanguage = this.language) {
    Languages.PT, Languages.EN -> currentLanguage
    else -> Configs.languageDefault
}