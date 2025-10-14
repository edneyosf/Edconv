package edneyosf.edconv.features.metrics.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.common.Languages.EN
import edneyosf.edconv.core.common.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.features.metrics.strings.MetricsCompleteDialogStrings.Keys.*
import edneyosf.edconv.ui.compositions.language

inline val metricsCompleteDialogStrings: MetricsCompleteDialogStrings
    @ReadOnlyComposable
    @Composable
    get() = MetricsCompleteDialogStrings(language)

class MetricsCompleteDialogStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        TITLE to "Análise Concluída",
        START_TIME to "Horário de Início:",
        END_TIME to "Horário de Término:",
        DURATION_TIME to "Duração total:",
        VMAF_SCORE to "VMAF:",
        PSNR_SCORE to "PSNR:",
        SSIM_SCORE to "SSIM:"
    )

    override val en = mapOf(
        TITLE to "Analysis Completed",
        START_TIME to "Start time:",
        END_TIME to "End time:",
        DURATION_TIME to "Total duration:",
        VMAF_SCORE to "VMAF:",
        PSNR_SCORE to "PSNR:",
        SSIM_SCORE to "SSIM:"
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        TITLE,
        START_TIME,
        END_TIME,
        DURATION_TIME,
        VMAF_SCORE,
        PSNR_SCORE,
        SSIM_SCORE
    }
}