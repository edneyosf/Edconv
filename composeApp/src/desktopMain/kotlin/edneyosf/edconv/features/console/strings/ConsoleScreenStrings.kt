package edneyosf.edconv.features.console.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.common.Languages.EN
import edneyosf.edconv.core.common.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.ui.compositions.language
import edneyosf.edconv.features.console.strings.ConsoleScreenStrings.Keys.*

inline val consoleScreenStrings: ConsoleScreenStrings
    @ReadOnlyComposable
    @Composable
    get() = ConsoleScreenStrings(language)

class ConsoleScreenStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        TITLE to "Console",
        COMMAND_INPUT to "Comando",
    )

    override val en = mapOf(
        TITLE to "Console",
        COMMAND_INPUT to "Command",
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        TITLE,
        COMMAND_INPUT
    }
}