package edneyosf.edconv.features.vmaf.states

import edneyosf.edconv.features.common.models.InputMedia
import java.io.File

data class VMAFState(
    val status: VMAFStatusState = VMAFStatusState.Initial,
    val input: InputMedia,
    val source: File? = null,
    val fps: Int = 24,
    val modelFile: File? = null,
    val widthScale: Int? = null,
    val heightScale: Int? = null,
    val threads: Int = 1
)