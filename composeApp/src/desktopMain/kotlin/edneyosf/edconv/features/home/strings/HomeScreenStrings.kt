package edneyosf.edconv.features.home.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.Languages.EN
import edneyosf.edconv.core.Languages.PT
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
        CBR_INPUT to "Taxa de Bits Constante:",
        VBR_INPUT to "Taxa de Bits Variável:",
        CRF_INPUT to "Fator de Taxa Constante:",
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
        CBR_INPUT to "Constant Bit Rate:",
        VBR_INPUT to "Variable Bit Rate:",
        CRF_INPUT to "Constant Rate Factor:",
        PRESET_INPUT to "Preset:",
        AUDIO_MEDIA_TYPE to "Audio",
        VIDEO_MEDIA_TYPE  to "Video",
        DEFAULT_ERROR to "Oops! Something went wrong.",
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        TITLE_PICK_FILE,
        SELECT_FILE,
        START_CONVERSION,
        STOP_CONVERSION,
        OUTPUT_FILE,
        FORMAT_INPUT,
        CHANNELS_INPUT,
        SAMPLE_RATE_INPUT,
        PIXEL_FORMAT_INPUT,
        RESOLUTION_INPUT,
        NO_AUDIO_INPUT,
        CBR_INPUT,
        VBR_INPUT,
        CRF_INPUT,
        PRESET_INPUT,
        AUDIO_MEDIA_TYPE,
        VIDEO_MEDIA_TYPE,
        DEFAULT_ERROR
    }
}