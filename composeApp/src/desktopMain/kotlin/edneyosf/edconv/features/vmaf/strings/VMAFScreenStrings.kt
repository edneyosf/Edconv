package edneyosf.edconv.features.vmaf.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.common.Languages.EN
import edneyosf.edconv.core.common.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.ui.compositions.language
import edneyosf.edconv.features.vmaf.strings.VMAFScreenStrings.Keys.*

inline val vmafScreenStrings: VMAFScreenStrings
    @ReadOnlyComposable
    @Composable
    get() = VMAFScreenStrings(language)

class VMAFScreenStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        START_CONVERSION to "Iniciar",
        STOP_CONVERSION to "Parar"
    )

    override val en = mapOf(
        START_CONVERSION to "Start",
        STOP_CONVERSION to "Stop"
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        START_CONVERSION,
        STOP_CONVERSION
    }
}