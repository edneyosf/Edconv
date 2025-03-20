package core.common

import app.AppConfigs
import app.AppLanguages
import androidx.compose.ui.text.intl.Locale

fun Locale.getAvailableLanguage() = when(val currentLanguage = this.language) {
    AppLanguages.PT, AppLanguages.EN -> currentLanguage
    else -> AppConfigs.LANGUAGE_DEFAULT
}