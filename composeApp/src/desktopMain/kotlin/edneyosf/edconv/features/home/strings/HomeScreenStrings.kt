package edneyosf.edconv.features.home.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.common.Languages.EN
import edneyosf.edconv.core.common.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.ui.compositions.language
import edneyosf.edconv.features.home.strings.HomeScreenStrings.Keys.*

inline val homeScreenStrings: HomeScreenStrings
    @ReadOnlyComposable
    @Composable
    get() = HomeScreenStrings(language)

class HomeScreenStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        TITLE_PICK_FILE to "Escolha um arquivo",
        AUDIO_NAVIGATION_ITEM to "Áudio",
        VIDEO_NAVIGATION_ITEM to "Vídeo",
        METRICS_NAVIGATION_ITEM to "Métricas",
        SELECT_MEDIA_FILE to "Selecionar mídia",
        SETTINGS to "Configurações"
    )

    override val en = mapOf(
        TITLE_PICK_FILE to "Choose a file",
        AUDIO_NAVIGATION_ITEM to "Audio",
        VIDEO_NAVIGATION_ITEM to "Video",
        METRICS_NAVIGATION_ITEM to "Metrics",
        SELECT_MEDIA_FILE to "Select media",
        SETTINGS to "Settings"
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        TITLE_PICK_FILE,
        AUDIO_NAVIGATION_ITEM,
        VIDEO_NAVIGATION_ITEM,
        METRICS_NAVIGATION_ITEM,
        SELECT_MEDIA_FILE,
        SETTINGS
    }
}