package edneyosf.edconv.features.converter.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.Languages.EN
import edneyosf.edconv.core.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.ui.compositions.language
import edneyosf.edconv.features.converter.strings.ConverterDialogStrings.Keys.*

inline val converterDialogStrings: ConverterDialogStrings
    @ReadOnlyComposable
    @Composable
    get() = ConverterDialogStrings(language)

class ConverterDialogStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        ERROR_TITLE to "Algo deu errado",
        WARNING_TITLE to "Atenção",
        COMPLETE_TITLE to "Conversão Concluída",
        MEDIA_INFO_TITLE to "Informações da Mídia",
        CONFIRMATION_BUTTON to "Confirmar",
        CANCEL_BUTTON to "Cancelar",
        OVERWRITE_FILE to "O arquivo existe, deseja sobrescrever?",
        START_TIME to "Horário de Início:",
        END_TIME to "Horário de Término:",
        DURATION_TIME to "Duração total:",
        FILE_MEDIA_INFO to "Arquivo",
        TYPE_MEDIA_INFO to "Tipo",
        AUDIO_MEDIA_INFO to "Áudio",
        VIDEO_MEDIA_INFO to "Vídeo",
        SUBTITLE_MEDIA_INFO to "Legenda",
        FORMAT_MEDIA_INFO to "Formato",
        DURATION_MEDIA_INFO to "Duração",
        BITRATE_MEDIA_INFO to "Taxa de bits",
        SIZE_MEDIA_INFO to "Tamanho",
        INDEX_MEDIA_INFO to "Índice",
        CODEC_MEDIA_INFO to "Codec",
        TITLE_MEDIA_INFO to "Título",
        LANGUAGE_MEDIA_INFO to "Idioma",
        PROFILE_MEDIA_INFO to "Perfil",
        WIDTH_MEDIA_INFO to "Largura",
        HEIGHT_MEDIA_INFO to "Altura",
        BIT_DEPTH_MEDIA_INFO to "Profundidade de bits",
        PIX_FMT_MEDIA_INFO to "Formato de pixel",
        FPS_MEDIA_INFO to "Quadros por segundo",
        LEVEL_MEDIA_INFO to "Nível",
        FILM_GRAIN_MEDIA_INFO to "Grão de filme",
        YES_MEDIA_INFO to "Sim",
        NO_MEDIA_INFO to "Não",
        DISPLAY_ASPECT_RATIO_MEDIA_INFO to "Proporção de tela",
        FIELD_ORDER_MEDIA_INFO to "Ordem de campo",
        CHANNELS_MEDIA_INFO to "Canais",
        SAMPLE_RATE_MEDIA_INFO to "Taxa de amostragem"
    )

    override val en = mapOf(
        ERROR_TITLE to "Something went wrong",
        WARNING_TITLE to "Warning",
        COMPLETE_TITLE to "Conversion Completed",
        MEDIA_INFO_TITLE to "Media Information",
        CONFIRMATION_BUTTON to "Confirm",
        CANCEL_BUTTON to "Cancel",
        OVERWRITE_FILE to "The file already exists. Do you want to overwrite it?",
        START_TIME to "Start time:",
        END_TIME to "End time:",
        DURATION_TIME to "Total duration:",
        FILE_MEDIA_INFO to "File",
        TYPE_MEDIA_INFO to "Type",
        AUDIO_MEDIA_INFO to "Audio",
        VIDEO_MEDIA_INFO to "Video",
        SUBTITLE_MEDIA_INFO to "Subtitle",
        FORMAT_MEDIA_INFO to "Format",
        DURATION_MEDIA_INFO to "Duration",
        BITRATE_MEDIA_INFO to "Bitrate",
        SIZE_MEDIA_INFO to "Size",
        INDEX_MEDIA_INFO to "Index",
        CODEC_MEDIA_INFO to "Codec",
        TITLE_MEDIA_INFO to "Title",
        LANGUAGE_MEDIA_INFO to "Language",
        PROFILE_MEDIA_INFO to "Profile",
        WIDTH_MEDIA_INFO to "Width",
        HEIGHT_MEDIA_INFO to "Height",
        BIT_DEPTH_MEDIA_INFO to "Bit depth",
        PIX_FMT_MEDIA_INFO to "Pixel format",
        FPS_MEDIA_INFO to "Frames per second",
        LEVEL_MEDIA_INFO to "Level",
        FILM_GRAIN_MEDIA_INFO to "Film grain",
        YES_MEDIA_INFO to "Yes",
        NO_MEDIA_INFO to "No",
        DISPLAY_ASPECT_RATIO_MEDIA_INFO to "Aspect ratio",
        FIELD_ORDER_MEDIA_INFO to "Field order",
        CHANNELS_MEDIA_INFO to "Channels",
        SAMPLE_RATE_MEDIA_INFO to "Sample rate"
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        ERROR_TITLE,
        WARNING_TITLE,
        COMPLETE_TITLE,
        MEDIA_INFO_TITLE,
        CONFIRMATION_BUTTON,
        CANCEL_BUTTON,
        OVERWRITE_FILE,
        START_TIME,
        END_TIME,
        DURATION_TIME,
        FILE_MEDIA_INFO,
        TYPE_MEDIA_INFO,
        AUDIO_MEDIA_INFO,
        VIDEO_MEDIA_INFO,
        SUBTITLE_MEDIA_INFO,
        FORMAT_MEDIA_INFO,
        DURATION_MEDIA_INFO,
        BITRATE_MEDIA_INFO,
        SIZE_MEDIA_INFO,
        INDEX_MEDIA_INFO,
        CODEC_MEDIA_INFO,
        TITLE_MEDIA_INFO,
        LANGUAGE_MEDIA_INFO,
        PROFILE_MEDIA_INFO,
        WIDTH_MEDIA_INFO,
        HEIGHT_MEDIA_INFO,
        BIT_DEPTH_MEDIA_INFO,
        PIX_FMT_MEDIA_INFO,
        FPS_MEDIA_INFO,
        LEVEL_MEDIA_INFO,
        FILM_GRAIN_MEDIA_INFO,
        YES_MEDIA_INFO,
        NO_MEDIA_INFO,
        DISPLAY_ASPECT_RATIO_MEDIA_INFO,
        FIELD_ORDER_MEDIA_INFO,
        CHANNELS_MEDIA_INFO,
        SAMPLE_RATE_MEDIA_INFO
    }
}