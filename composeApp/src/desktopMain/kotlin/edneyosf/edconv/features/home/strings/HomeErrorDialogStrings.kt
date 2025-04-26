package edneyosf.edconv.features.home.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.common.Languages.EN
import edneyosf.edconv.core.common.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.ui.compositions.language
import edneyosf.edconv.features.home.strings.HomeErrorDialogStrings.Keys.*

inline val homeErrorDialogStrings: HomeErrorDialogStrings
    @ReadOnlyComposable
    @Composable
    get() = HomeErrorDialogStrings(language)

class HomeErrorDialogStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        LOAD_CONFIGS to "Falha ao carregar configurações. Tente novamente.",
        UNKNOWN_INPUT_MEDIA to "Não foi possível extrair informações do arquivo de mídia. Confirme se é um áudio ou vídeo válido e tente novamente.",
        NO_DURATION_INPUT_MEDIA to "Falha ao obter a duração do arquivo de mídia.",
        NO_CHANNELS_INPUT_MEDIA to "Falha ao obter a quantidade de canais do arquivo de áudio.",
        NO_RESOLUTION_INPUT_MEDIA to "Falha ao obter a resolução do arquivo de vídeo.",
        NO_STREAM_FOUND_INPUT_MEDIA to "Nenhum fluxo de mídia encontrado no arquivo."
    )

    override val en = mapOf(
        LOAD_CONFIGS to "Failed to load settings. Please try again.",
        UNKNOWN_INPUT_MEDIA to "Unable to retrieve information from the media file. Please check if it is a valid audio or video file and try again.",
        NO_DURATION_INPUT_MEDIA to "Failed to retrieve the media file duration.",
        NO_CHANNELS_INPUT_MEDIA to "Failed to retrieve the number of channels from the audio file.",
        NO_RESOLUTION_INPUT_MEDIA to "Failed to retrieve the video file resolution.",
        NO_STREAM_FOUND_INPUT_MEDIA to "No media stream found in the file."
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        LOAD_CONFIGS,
        UNKNOWN_INPUT_MEDIA,
        NO_DURATION_INPUT_MEDIA,
        NO_CHANNELS_INPUT_MEDIA,
        NO_RESOLUTION_INPUT_MEDIA,
        NO_STREAM_FOUND_INPUT_MEDIA
    }
}