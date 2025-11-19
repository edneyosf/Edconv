package edneyosf.edconv.features.mediainfo.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.common.Languages.EN
import edneyosf.edconv.core.common.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.ui.compositions.language
import edneyosf.edconv.features.mediainfo.strings.MediaInfoItemStrings.Keys.*

inline val mediaInfoItemStrings: MediaInfoItemStrings
    @ReadOnlyComposable
    @Composable
    get() = MediaInfoItemStrings(language)

class MediaInfoItemStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        AUDIO to "Áudio",
        VIDEO to "Vídeo",
        SUBTITLE to "Legenda",
        REMOVE to "Remover"
    )

    override val en = mapOf(
        AUDIO to "Audio",
        VIDEO to "Video",
        SUBTITLE to "Subtitle",
        REMOVE to "Remove"
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        AUDIO,
        VIDEO,
        SUBTITLE,
        REMOVE
    }
}