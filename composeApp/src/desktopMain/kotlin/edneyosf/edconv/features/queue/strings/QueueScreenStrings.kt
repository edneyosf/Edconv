package edneyosf.edconv.features.queue.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.common.Languages.EN
import edneyosf.edconv.core.common.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.ui.compositions.language
import edneyosf.edconv.features.queue.strings.QueueScreenStrings.Keys.*

inline val queueScreenStrings: QueueScreenStrings
    @ReadOnlyComposable
    @Composable
    get() = QueueScreenStrings(language)

class QueueScreenStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        WINDOW_TITLE to "Fila",
        CLEAN to "Limpar"
    )

    override val en = mapOf(
        WINDOW_TITLE to "Queue",
        CLEAN to "Clean"
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        WINDOW_TITLE,
        CLEAN
    }
}