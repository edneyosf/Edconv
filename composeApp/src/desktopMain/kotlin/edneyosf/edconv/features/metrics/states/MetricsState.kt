package edneyosf.edconv.features.metrics.states

import edneyosf.edconv.features.common.models.InputMedia

data class MetricsState(
    val status: MetricsStatusState = MetricsStatusState.Initial,
    val reference: InputMedia? = null,
    val distorted: String? = null,
    val fps: Int = 24,
    val threads: Int = 1,
    val vmaf: Boolean = true,
    val psnr: Boolean = false,
    val ssim: Boolean = false
)