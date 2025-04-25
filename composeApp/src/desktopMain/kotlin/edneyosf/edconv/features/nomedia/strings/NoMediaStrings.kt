package edneyosf.edconv.features.nomedia.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.Languages.EN
import edneyosf.edconv.core.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.ui.compositions.language
import edneyosf.edconv.features.nomedia.strings.NoMediaScreenStrings.Keys.*

inline val noMediaScreenStrings: NoMediaScreenStrings
    @ReadOnlyComposable
    @Composable
    get() = NoMediaScreenStrings(language)

class NoMediaScreenStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        NO_FILE_SELECTED to "Selecione um arquivo de mídia",
        VERSION to "Versão"
    )

    override val en = mapOf(
        NO_FILE_SELECTED to "Select a media file",
        VERSION to "Version"
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        NO_FILE_SELECTED,
        VERSION
    }
}