package edneyosf.edconv.features.vmaf.states

import edneyosf.edconv.features.common.models.InputMedia

data class VmafState(
    val status: VmafStatusState = VmafStatusState.Initial,
    val dialog: VmafDialogState = VmafDialogState.None,
    val reference: InputMedia? = null,
    val distorted: String? = null,
    val fps: Int = 24,
    val model: String? = null,
    val threads: Int = 1
)