package edneyosf.edconv.features.home.texts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.Languages.EN
import edneyosf.edconv.core.Languages.PT
import edneyosf.edconv.core.common.Texts
import edneyosf.edconv.ui.compositions.languages

inline val settingsDialogTexts: SettingsDialogTexts
    @ReadOnlyComposable
    @Composable
    get() = SettingsDialogTexts(languages)

class SettingsDialogTexts(override val language: String): Texts(language) {

    override val pt = mapOf(
        TITLE_TXT to "Configuração",
        NO_DEFINED_TXT to "Para continuar, selecione os caminhos para os binários do FFmpeg e do FFprobe.",
        SELECT_FFMPEG to "Selecionar FFmpeg",
        SELECT_FFPROBE to "Selecionar FFprobe",
    )

    override val en = mapOf(
        TITLE_TXT to "Settings",
        NO_DEFINED_TXT to "To proceed, please select the paths to the FFmpeg and FFprobe binaries.",
        SELECT_FFMPEG to "Select FFmpeg",
        SELECT_FFPROBE to "Select FFprobe",
    )

    override val texts = mapOf(PT to pt, EN to en)

    companion object {
        const val TITLE_TXT = 1L
        const val NO_DEFINED_TXT = 2L
        const val SELECT_FFMPEG = 3L
        const val SELECT_FFPROBE = 4L
    }
}