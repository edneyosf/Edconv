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
        ADD_TO_QUEUE_CONVERSION to "Adicionar",
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
        COMMAND_INPUT to "Comando",
        LOGS_VIEW to "Registros",
        MEDIA_INFO to "Informações da mídia",
        QUEUE to "Fila"
    )

    override val en = mapOf(
        ADD_TO_QUEUE_CONVERSION to "Add",
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
        COMMAND_INPUT to "Command",
        LOGS_VIEW to "Logs",
        MEDIA_INFO to "Media information",
        QUEUE to "Queue"
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        ADD_TO_QUEUE_CONVERSION,
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
        COMMAND_INPUT,
        LOGS_VIEW,
        MEDIA_INFO,
        QUEUE
    }
}