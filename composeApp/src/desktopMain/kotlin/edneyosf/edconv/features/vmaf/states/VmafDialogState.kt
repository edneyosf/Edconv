package edneyosf.edconv.features.vmaf.states

import edneyosf.edconv.features.common.models.InputMedia

sealed interface VmafDialogState {
    data object None: VmafDialogState
    data class MediaInfo(val inputMedia: InputMedia): VmafDialogState
}