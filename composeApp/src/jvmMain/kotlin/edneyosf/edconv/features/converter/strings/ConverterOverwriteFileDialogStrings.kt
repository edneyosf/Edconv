package edneyosf.edconv.features.converter.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.common.Languages.EN
import edneyosf.edconv.core.common.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.ui.compositions.language
import edneyosf.edconv.features.converter.strings.ConverterOverwriteFileDialogStrings.Keys.*

inline val converterOverwriteFileDialogStrings: ConverterOverwriteFileDialogStrings
    @ReadOnlyComposable
    @Composable
    get() = ConverterOverwriteFileDialogStrings(language)

class ConverterOverwriteFileDialogStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        DESCRIPTION to "O arquivo existe, deseja sobrescrever?"
    )

    override val en = mapOf(
        DESCRIPTION to "The file already exists. Do you want to overwrite it?"
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        DESCRIPTION
    }
}