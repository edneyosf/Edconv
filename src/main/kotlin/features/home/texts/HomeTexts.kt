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
        TITLE_PICK_FILE_TXT to "Escolha um arquivo",
        SELECT_FILE_TXT to "Selecionar arquivo",
        START_CONVERSION_TXT to "Iniciar",
        STOP_CONVERSION_TXT to "Parar",
        OUTPUT_FILE_TXT to "Salvar como",
        FORMAT_INPUT_TXT to "Formato",
        CHANNELS_INPUT_TXT to "Canais",
        SAMPLE_RATE_INPUT_TXT to "Taxa de amostragem",
        PIXEL_FORMAT_INPUT_TXT to "Formato de pixel",
        RESOLUTION_INPUT_TXT to "Resolução",
        NO_AUDIO_INPUT_TXT to "Sem áudio",
        QUALITY_INPUT_TXT to "Qualidade constante",
        BIT_RATE_INPUT_TXT to "Taxa de bits",
        AUDIO_MEDIA_TYPE_TXT to "Áudio",
        VIDEO_MEDIA_TYPE_TXT  to "Vídeo",
    )

    override val en = mapOf(
        TITLE_PICK_FILE_TXT to "Choose a file",
        SELECT_FILE_TXT to "Select file",
        START_CONVERSION_TXT to "Start",
        STOP_CONVERSION_TXT to "Stop",
        OUTPUT_FILE_TXT to "Save as",
        FORMAT_INPUT_TXT to "Format",
        CHANNELS_INPUT_TXT to "Channels",
        SAMPLE_RATE_INPUT_TXT to "Sample rate",
        PIXEL_FORMAT_INPUT_TXT to "Pixel format",
        RESOLUTION_INPUT_TXT to "Resolution",
        NO_AUDIO_INPUT_TXT to "No audio",
        QUALITY_INPUT_TXT to "Constant quality",
        BIT_RATE_INPUT_TXT to "Bitrate",
        AUDIO_MEDIA_TYPE_TXT to "Audio",
        VIDEO_MEDIA_TYPE_TXT  to "Video",
    )

    override val texts = mapOf(PT to pt, EN to en)

    companion object {
        const val TITLE_PICK_FILE_TXT = 1L
        const val SELECT_FILE_TXT = 2L
        const val START_CONVERSION_TXT = 3L
        const val STOP_CONVERSION_TXT = 4L
        const val OUTPUT_FILE_TXT = 5L
        const val FORMAT_INPUT_TXT = 6L
        const val CHANNELS_INPUT_TXT = 7L
        const val SAMPLE_RATE_INPUT_TXT = 8L
        const val PIXEL_FORMAT_INPUT_TXT = 9L
        const val RESOLUTION_INPUT_TXT = 10L
        const val NO_AUDIO_INPUT_TXT = 11L
        const val QUALITY_INPUT_TXT = 12L
        const val BIT_RATE_INPUT_TXT = 13L
        const val AUDIO_MEDIA_TYPE_TXT = 14L
        const val VIDEO_MEDIA_TYPE_TXT = 15L
    }
}