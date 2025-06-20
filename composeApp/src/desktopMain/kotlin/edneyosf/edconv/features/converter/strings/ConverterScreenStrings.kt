package edneyosf.edconv.features.converter.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.common.Languages.EN
import edneyosf.edconv.core.common.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.ui.compositions.language
import edneyosf.edconv.features.converter.strings.ConverterScreenStrings.Keys.*

inline val converterScreenStrings: ConverterScreenStrings
    @ReadOnlyComposable
    @Composable
    get() = ConverterScreenStrings(language)

class ConverterScreenStrings(override val language: String): Strings(language) {

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
        NO_METADATA_INPUT to "Sem metadados",
        NO_SUBTITLE_INPUT to "Sem legenda",
        NO_AUDIO_INPUT to "Sem áudio",
        CBR_INPUT to "Taxa de Bits Constante:",
        VBR_INPUT to "Taxa de Bits Variável:",
        CRF_INPUT to "Fator de Taxa Constante:",
        PRESET_INPUT to "Predefinição:",
        AUDIO_NAVIGATION_ITEM to "Áudio",
        VIDEO_NAVIGATION_ITEM  to "Vídeo",
        VMAF_NAVIGATION_ITEM to "VMAF",
        COMMAND_INPUT to "Comando",
        LOGS_VIEW to "Registros",
        NO_FILE_SELECTED to "Selecione um arquivo de mídia",
        VERSION to "Versão",
        DEFAULT_ERROR to "Opa! Algo deu errado.",
        SELECT_MEDIA_FILE to "Selecionar mídia",
        SETTINGS to "Configurações",
        MEDIA_INFO to "Informações da mídia"
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
        NO_METADATA_INPUT to "No metadata",
        NO_SUBTITLE_INPUT to "No subtitle",
        NO_AUDIO_INPUT to "No audio",
        CBR_INPUT to "Constant Bit Rate:",
        VBR_INPUT to "Variable Bit Rate:",
        CRF_INPUT to "Constant Rate Factor:",
        PRESET_INPUT to "Preset:",
        AUDIO_NAVIGATION_ITEM to "Audio",
        VIDEO_NAVIGATION_ITEM  to "Video",
        VMAF_NAVIGATION_ITEM to "VMAF",
        COMMAND_INPUT to "Command",
        LOGS_VIEW to "Logs",
        NO_FILE_SELECTED to "Select a media file",
        VERSION to "Version",
        DEFAULT_ERROR to "Oops! Something went wrong.",
        SELECT_MEDIA_FILE to "Select media",
        SETTINGS to "Settings",
        MEDIA_INFO to "Media information"
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
        NO_METADATA_INPUT,
        NO_AUDIO_INPUT,
        NO_SUBTITLE_INPUT,
        CBR_INPUT,
        VBR_INPUT,
        CRF_INPUT,
        PRESET_INPUT,
        AUDIO_NAVIGATION_ITEM,
        VIDEO_NAVIGATION_ITEM,
        VMAF_NAVIGATION_ITEM,
        COMMAND_INPUT,
        LOGS_VIEW,
        NO_FILE_SELECTED,
        VERSION,
        DEFAULT_ERROR,
        SELECT_MEDIA_FILE,
        SETTINGS,
        MEDIA_INFO
    }
}