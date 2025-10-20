package edneyosf.edconv.app.extensions

import edneyosf.edconv.core.common.Languages
import androidx.compose.ui.text.intl.Locale
import edneyosf.edconv.app.AppConfigs

fun Locale.getAvailableLanguage() = when(val currentLanguage = this.language) {
    Languages.PT, Languages.EN -> currentLanguage
    else -> AppConfigs.LANGUAGE
}