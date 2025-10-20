package edneyosf.edconv.features.converter.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.common.Languages.EN
import edneyosf.edconv.core.common.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.features.converter.strings.ConverterCompleteDialogStrings.Keys.*
import edneyosf.edconv.ui.compositions.language

inline val converterCompleteDialogStrings: ConverterCompleteDialogStrings
    @ReadOnlyComposable
    @Composable
    get() = ConverterCompleteDialogStrings(language)

class ConverterCompleteDialogStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        TITLE to "Conversão Concluída",
        START_TIME to "Horário de Início:",
        END_TIME to "Horário de Término:",
        DURATION_TIME to "Duração total:"
    )

    override val en = mapOf(
        TITLE to "Conversion Completed",
        START_TIME to "Start time:",
        END_TIME to "End time:",
        DURATION_TIME to "Total duration:"
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        TITLE,
        START_TIME,
        END_TIME,
        DURATION_TIME,
    }
}