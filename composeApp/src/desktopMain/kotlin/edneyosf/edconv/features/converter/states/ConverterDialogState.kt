package edneyosf.edconv.features.converter.states

import edneyosf.edconv.features.common.models.InputMedia

sealed interface ConverterDialogState {
    data object None: ConverterDialogState
    data object Settings: ConverterDialogState
    data class MediaInfo(val inputMedia: InputMedia): ConverterDialogState
}