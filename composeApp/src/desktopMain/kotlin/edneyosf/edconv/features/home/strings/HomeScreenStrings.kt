package edneyosf.edconv.features.home.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.Languages.EN
import edneyosf.edconv.core.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.ui.compositions.language

inline val homeScreenStrings: HomeScreenStrings
    @ReadOnlyComposable
    @Composable
    get() = HomeScreenStrings(language)

class HomeScreenStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        TITLE_PICK_FILE to "Escolha um arquivo",
        SELECT_FILE to "Selecionar arquivo",
        START_CONVERSION to "Iniciar",
        STOP_CONVERSION to "Parar",
        OUTPUT_FILE to "Salvar como",
        FORMAT_INPUT to "Formato",
        CHANNELS_INPUT to "Canais",
        SAMPLE_RATE_INPUT to "Taxa de amostragem",
        PIXEL_FORMAT_INPUT to "Formato de pixel",
        RESOLUTION_INPUT to "Resolução",
        NO_AUDIO_INPUT to "Sem áudio",
        QUALITY_INPUT to "Qualidade:",
        BIT_RATE_INPUT to "Taxa de bits",
        PRESET_INPUT to "Predefinição:",
        AUDIO_MEDIA_TYPE to "Áudio",
        VIDEO_MEDIA_TYPE  to "Vídeo",
        DEFAULT_ERROR to "Opa! Algo deu errado.",
    )

    override val en = mapOf(
        TITLE_PICK_FILE to "Choose a file",
        SELECT_FILE to "Select file",
        START_CONVERSION to "Start",
        STOP_CONVERSION to "Stop",
        OUTPUT_FILE to "Save as",
        FORMAT_INPUT to "Format",
        CHANNELS_INPUT to "Channels",
        SAMPLE_RATE_INPUT to "Sample rate",
        PIXEL_FORMAT_INPUT to "Pixel format",
        RESOLUTION_INPUT to "Resolution",
        NO_AUDIO_INPUT to "No audio",
        QUALITY_INPUT to "Constant:",
        BIT_RATE_INPUT to "Bitrate",
        PRESET_INPUT to "Preset:",
        AUDIO_MEDIA_TYPE to "Audio",
        VIDEO_MEDIA_TYPE  to "Video",
        DEFAULT_ERROR to "Oops! Something went wrong.",
    )

    override val texts = mapOf(PT to pt, EN to en)

    companion object {
        const val TITLE_PICK_FILE = 1L
        const val SELECT_FILE = 2L
        const val START_CONVERSION = 3L
        const val STOP_CONVERSION = 4L
        const val OUTPUT_FILE = 5L
        const val FORMAT_INPUT = 6L
        const val CHANNELS_INPUT = 7L
        const val SAMPLE_RATE_INPUT = 8L
        const val PIXEL_FORMAT_INPUT = 9L
        const val RESOLUTION_INPUT = 10L
        const val NO_AUDIO_INPUT = 11L
        const val QUALITY_INPUT = 12L
        const val BIT_RATE_INPUT = 13L
        const val PRESET_INPUT = 14L
        const val AUDIO_MEDIA_TYPE = 15L
        const val VIDEO_MEDIA_TYPE = 16L
        const val DEFAULT_ERROR = 17L
    }
}