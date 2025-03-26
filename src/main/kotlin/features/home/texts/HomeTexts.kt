package features.home.texts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import core.Languages.EN
import core.Languages.PT
import core.common.Texts
import ui.compositions.languages

inline val homeTexts: HomeTexts
    @ReadOnlyComposable
    @Composable
    get() = HomeTexts(languages)

class HomeTexts(override val language: String): Texts(language) {

    override val pt = mapOf(
        TITLE_PICK_FILE_TEXT to "Escolha um arquivo",
        SELECT_FILE_TEXT to "Selecionar arquivo",
        START_CONVERSION to "Iniciar",
        STOP_CONVERSION to "Parar",
        OUTPUT_FILE to "Salvar como",
        FORMAT_INPUT to "Formato",
        CHANNELS_INPUT to "Canais",
        AUDIO_MEDIA_TYPE to "Áudio",
        VIDEO_MEDIA_TYPE  to "Vídeo",
    )

    override val en = mapOf(
        TITLE_PICK_FILE_TEXT to "Choose a file",
        SELECT_FILE_TEXT to "Select file",
        START_CONVERSION to "Start",
        STOP_CONVERSION to "Stop",
        OUTPUT_FILE to "Save as",
        FORMAT_INPUT to "Format",
        CHANNELS_INPUT to "Channels",
        AUDIO_MEDIA_TYPE to "Audio",
        VIDEO_MEDIA_TYPE  to "Video",
    )

    override val texts = mapOf(PT to pt, EN to en)

    companion object {
        const val TITLE_PICK_FILE_TEXT = 1L
        const val SELECT_FILE_TEXT = 2L
        const val START_CONVERSION = 3L
        const val STOP_CONVERSION = 4L
        const val OUTPUT_FILE = 5L
        const val FORMAT_INPUT = 6L
        const val CHANNELS_INPUT = 7L
        const val AUDIO_MEDIA_TYPE = 8L
        const val VIDEO_MEDIA_TYPE = 9L
    }
}