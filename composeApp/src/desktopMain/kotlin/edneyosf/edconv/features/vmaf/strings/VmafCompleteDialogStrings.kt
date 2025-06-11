package edneyosf.edconv.features.vmaf.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.common.Languages.EN
import edneyosf.edconv.core.common.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.features.vmaf.strings.VmafCompleteDialogStrings.Keys.*
import edneyosf.edconv.ui.compositions.language

inline val vmafCompleteDialogStrings: VmafCompleteDialogStrings
    @ReadOnlyComposable
    @Composable
    get() = VmafCompleteDialogStrings(language)

class VmafCompleteDialogStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        TITLE to "Análise Concluída",
        START_TIME to "Horário de Início:",
        END_TIME to "Horário de Término:",
        DURATION_TIME to "Duração total:",
        SCORE to "Pontuação:"
    )

    override val en = mapOf(
        TITLE to "Analysis Completed",
        START_TIME to "Start time:",
        END_TIME to "End time:",
        DURATION_TIME to "Total duration:",
        SCORE to "Score:"
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        TITLE,
        START_TIME,
        END_TIME,
        DURATION_TIME,
        SCORE
    }
}