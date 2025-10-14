package edneyosf.edconv.features.metrics.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import edneyosf.edconv.core.common.Languages.EN
import edneyosf.edconv.core.common.Languages.PT
import edneyosf.edconv.core.common.Strings
import edneyosf.edconv.ui.compositions.language
import edneyosf.edconv.features.metrics.strings.MetricsScreenStrings.Keys.*

inline val metricsScreenStrings: MetricsScreenStrings
    @ReadOnlyComposable
    @Composable
    get() = MetricsScreenStrings(language)

class MetricsScreenStrings(override val language: String): Strings(language) {

    override val pt = mapOf(
        START_ANALYSIS to "Iniciar",
        STOP_ANALYSIS to "Parar",
        DISTORTED_FILE_INPUT to "Vídeo de distorcido",
        FPS_INPUT to "Quadros por segundo",
        THREAD_INPUT to "Número de threads",
        MEDIA_INFO to "Informações da mídia",
        TITLE_PICK_FILE to "Escolha um arquivo",
        SELECT_FILE to "Selecionar arquivo",
        VMAF to "VMAF",
        PSNR to "PSNR",
        SSIM to "SSIM"
    )

    override val en = mapOf(
        START_ANALYSIS to "Start",
        STOP_ANALYSIS to "Stop",
        DISTORTED_FILE_INPUT to "Distorted video",
        FPS_INPUT to "Frames per Second",
        THREAD_INPUT to "Number of threads",
        MEDIA_INFO to "Media information",
        TITLE_PICK_FILE to "Choose a file",
        SELECT_FILE to "Select file",
        VMAF to "VMAF",
        PSNR to "PSNR",
        SSIM to "SSIM"
    )

    override val texts = mapOf(PT to pt, EN to en)

    enum class Keys {
        START_ANALYSIS,
        STOP_ANALYSIS,
        DISTORTED_FILE_INPUT,
        FPS_INPUT,
        THREAD_INPUT,
        MEDIA_INFO,
        TITLE_PICK_FILE,
        SELECT_FILE,
        VMAF,
        PSNR,
        SSIM
    }
}