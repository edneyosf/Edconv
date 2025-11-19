package edneyosf.edconv.features.mediainfo.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.common.Languages.EN
import edneyosf.edconv.core.common.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.ui.compositions.language
import edneyosf.edconv.features.mediainfo.strings.MediaInfoDialogStrings.Keys.*

inline val mediaInfoDialogStrings: MediaInfoDialogStrings
    @ReadOnlyComposable
    @Composable
    get() = MediaInfoDialogStrings(language)

class MediaInfoDialogStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        TITLE to "Informações da Mídia",
        FILE to "Arquivo",
        TYPE to "Tipo",
        AUDIO to "Áudio",
        VIDEO to "Vídeo",
        SUBTITLE to "Legenda",
        FORMAT to "Formato",
        DURATION to "Duração",
        BITRATE to "Taxa de bits",
        SIZE to "Tamanho",
        INDEX to "Índice",
        CODEC to "Codec",
        STREAM_TITLE to "Título",
        LANGUAGE to "Idioma",
        PROFILE to "Perfil",
        WIDTH to "Largura",
        HEIGHT to "Altura",
        BIT_DEPTH to "Profundidade de bits",
        PIX_FMT to "Formato de pixel",
        FPS to "Quadros por segundo",
        LEVEL to "Nível",
        FILM_GRAIN to "Grão de filme",
        YES to "Sim",
        NO to "Não",
        DISPLAY_ASPECT_RATIO to "Proporção de tela",
        FIELD_ORDER to "Ordem de campo",
        CHANNELS to "Canais",
        SAMPLE_RATE to "Taxa de amostragem"
    )

    override val en = mapOf(
        TITLE to "Media Information",
        FILE to "File",
        TYPE to "Type",
        AUDIO to "Audio",
        VIDEO to "Video",
        SUBTITLE to "Subtitle",
        FORMAT to "Format",
        DURATION to "Duration",
        BITRATE to "Bitrate",
        SIZE to "Size",
        INDEX to "Index",
        CODEC to "Codec",
        STREAM_TITLE to "Title",
        LANGUAGE to "Language",
        PROFILE to "Profile",
        WIDTH to "Width",
        HEIGHT to "Height",
        BIT_DEPTH to "Bit depth",
        PIX_FMT to "Pixel format",
        FPS to "Frames per second",
        LEVEL to "Level",
        FILM_GRAIN to "Film grain",
        YES to "Yes",
        NO to "No",
        DISPLAY_ASPECT_RATIO to "Aspect ratio",
        FIELD_ORDER to "Field order",
        CHANNELS to "Channels",
        SAMPLE_RATE to "Sample rate"
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        TITLE,
        FILE,
        TYPE,
        AUDIO,
        VIDEO,
        SUBTITLE,
        FORMAT,
        DURATION,
        BITRATE,
        SIZE,
        INDEX,
        CODEC,
        STREAM_TITLE,
        LANGUAGE,
        PROFILE,
        WIDTH,
        HEIGHT,
        BIT_DEPTH,
        PIX_FMT,
        FPS,
        LEVEL,
        FILM_GRAIN,
        YES,
        NO,
        DISPLAY_ASPECT_RATIO,
        FIELD_ORDER,
        CHANNELS,
        SAMPLE_RATE
    }
}