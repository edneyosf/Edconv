package edneyosf.edconv.features.vmaf.events

import edneyosf.edconv.features.common.models.InputMedia

interface VMAFEvent {
    fun refresh(newInput: InputMedia) = Unit
    fun pickSourceFile(title: String) = Unit
    fun pickModelFile(title: String) = Unit
}