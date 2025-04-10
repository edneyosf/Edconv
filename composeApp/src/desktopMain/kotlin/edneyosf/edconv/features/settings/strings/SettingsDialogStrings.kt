package edneyosf.edconv.features.settings.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.Languages.EN
import edneyosf.edconv.core.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.ui.compositions.language

inline val settingsDialogStrings: SettingsDialogStrings
    @ReadOnlyComposable
    @Composable
    get() = SettingsDialogStrings(language)

class SettingsDialogStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        TITLE to "Configuração",
        DESCRIPTION to "FFmpeg e FFprobe não foram encontrados. Para continuar, indique os caminhos onde esses arquivos estão localizados.",
        SELECT_FFMPEG to "Selecionar FFmpeg",
        SELECT_FFPROBE to "Selecionar FFprobe",
        CONFIRMATION_BUTTON to "Confirmar",
        CANCEL_BUTTON to "Cancelar",
        PICK_FFMPEG_TITLE to "Selecione o arquivo FFmpeg",
        PICK_FFPROBE_TITLE to "Selecione o arquivo FFprobe",
    )

    override val en = mapOf(
        TITLE to "Settings",
        DESCRIPTION to "FFmpeg and FFprobe were not found. To continue, please specify the paths where these files are located.",
        SELECT_FFMPEG to "Select FFmpeg",
        SELECT_FFPROBE to "Select FFprobe",
        CONFIRMATION_BUTTON to "Confirm",
        CANCEL_BUTTON to "Cancel",
        PICK_FFMPEG_TITLE to "Select FFmpeg file",
        PICK_FFPROBE_TITLE to "Select FFprobe file",
    )

    override val texts = mapOf(PT to pt, EN to en)

    companion object Key {
        const val TITLE = 1L
        const val DESCRIPTION = 2L
        const val SELECT_FFMPEG = 3L
        const val SELECT_FFPROBE = 4L
        const val CONFIRMATION_BUTTON = 5L
        const val CANCEL_BUTTON = 6L
        const val PICK_FFMPEG_TITLE = 7L
        const val PICK_FFPROBE_TITLE = 8L
    }
}