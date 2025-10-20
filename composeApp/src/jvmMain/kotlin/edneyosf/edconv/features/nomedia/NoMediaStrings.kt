package edneyosf.edconv.features.nomedia

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.common.Languages.EN
import edneyosf.edconv.core.common.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.ui.compositions.language
import edneyosf.edconv.features.nomedia.NoMediaScreenStrings.Keys.*

inline val noMediaScreenStrings: NoMediaScreenStrings
    @ReadOnlyComposable
    @Composable
    get() = NoMediaScreenStrings(language)

class NoMediaScreenStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        NO_FILE_SELECTED to "Arraste ou selecione um arquivo de mídia"
    )

    override val en = mapOf(
        NO_FILE_SELECTED to "Drag or select a media file"
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        NO_FILE_SELECTED
    }
}