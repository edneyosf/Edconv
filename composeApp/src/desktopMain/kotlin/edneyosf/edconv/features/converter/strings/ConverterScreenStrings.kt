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
        ENCODER_INPUT to "Codificador",
        CHANNELS_INPUT to "Canais",
        SAMPLE_RATE_INPUT to "Taxa de amostragem",
        PIXEL_FORMAT_INPUT to "Formato de pixel",
        RESOLUTION_INPUT to "Resolução",
        NO_METADATA_INPUT to "Sem metadados",
        NO_SUBTITLE_INPUT to "Sem legenda",
        NO_AUDIO_INPUT to "Sem áudio",
        COPY_INPUT to "Copiar",
        CBR_INPUT to "Bitrate",
        VBR_INPUT to "VBR:",
        CRF_INPUT to "CRF:",
        PRESET_INPUT to "Predefinição:",
        LOGS_VIEW to "Registros",
        MEDIA_INFO to "Informações da mídia",
        QUEUE to "Fila",
        PENDING_JOBS to "Trabalhos pendentes",
        OUTPUT_SAVE_FILE to "Salvar arquivo",
        OUTPUT_TO to "Para:",
        CONSOLE to "Console",
        HDR10_TO_SDR to "HDR10 para SDR",
        VIDEO to "Vídeo",
        AUDIO to "Áudio",
        NO_CHAPTERS to "Sem capítulos",
        INDEX to "Índice",
        INDEX_ALL to "Todos",
        INDEX_DISABLED to "Desabilitado"
    )

    override val en = mapOf(
        ADD_TO_QUEUE_CONVERSION to "Add",
        START_CONVERSION to "Start",
        STOP_CONVERSION to "Stop",
        OUTPUT_FILE to "Save as",
        ENCODER_INPUT to "Encoder",
        CHANNELS_INPUT to "Channels",
        SAMPLE_RATE_INPUT to "Sample rate",
        PIXEL_FORMAT_INPUT to "Pixel format",
        RESOLUTION_INPUT to "Resolution",
        NO_METADATA_INPUT to "No metadata",
        NO_SUBTITLE_INPUT to "No subtitle",
        NO_AUDIO_INPUT to "No audio",
        COPY_INPUT to "Copy",
        CBR_INPUT to "Bitrate",
        VBR_INPUT to "VBR:",
        CRF_INPUT to "CRF:",
        PRESET_INPUT to "Preset:",
        LOGS_VIEW to "Logs",
        MEDIA_INFO to "Media information",
        QUEUE to "Queue",
        PENDING_JOBS to "Pending jobs",
        OUTPUT_SAVE_FILE to "Save file",
        OUTPUT_TO to "To:",
        CONSOLE to "Console",
        HDR10_TO_SDR to "HDR10 to SDR",
        VIDEO to "Video",
        AUDIO to "Audio",
        NO_CHAPTERS to "No chapters",
        INDEX to "Index",
        INDEX_ALL to "All",
        INDEX_DISABLED to "Disabled"
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        ADD_TO_QUEUE_CONVERSION,
        START_CONVERSION,
        STOP_CONVERSION,
        OUTPUT_FILE,
        ENCODER_INPUT,
        CHANNELS_INPUT,
        SAMPLE_RATE_INPUT,
        PIXEL_FORMAT_INPUT,
        RESOLUTION_INPUT,
        NO_METADATA_INPUT,
        NO_AUDIO_INPUT,
        NO_SUBTITLE_INPUT,
        COPY_INPUT,
        CBR_INPUT,
        VBR_INPUT,
        CRF_INPUT,
        PRESET_INPUT,
        LOGS_VIEW,
        MEDIA_INFO,
        QUEUE,
        PENDING_JOBS,
        OUTPUT_SAVE_FILE,
        OUTPUT_TO,
        CONSOLE,
        HDR10_TO_SDR,
        VIDEO,
        AUDIO,
        NO_CHAPTERS,
        INDEX,
        INDEX_ALL,
        INDEX_DISABLED
    }
}