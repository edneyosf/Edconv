package app.extensions

import core.Languages
import androidx.compose.ui.text.intl.Locale
import app.AppConfigs

fun Locale.getAvailableLanguage() = when(val currentLanguage = this.language) {
    Languages.PT, Languages.EN -> currentLanguage
    else -> AppConfigs.LANGUAGE
}