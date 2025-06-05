package edneyosf.edconv.features.vmaf.events

import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.features.converter.states.ConverterDialogState
import edneyosf.edconv.features.vmaf.states.VmafDialogState
import edneyosf.edconv.features.vmaf.states.VmafStatusState

interface VmafEvent {
    fun setStatus(status: VmafStatusState) = Unit
    fun setDialog(dialog: VmafDialogState) = Unit
    fun refresh(newInput: InputMedia) = Unit
    fun pickDistortedFile(title: String) = Unit
    fun pickModelFile(title: String) = Unit
    fun setFps(value: String) = Unit
    fun setThread(value: String) = Unit
    fun start() = Unit
    fun stop() = Unit
}