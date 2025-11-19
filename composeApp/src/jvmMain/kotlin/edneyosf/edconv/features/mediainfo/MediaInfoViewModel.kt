package edneyosf.edconv.features.mediainfo

import androidx.lifecycle.ViewModel
import edneyosf.edconv.core.process.EdProcess
import edneyosf.edconv.features.common.models.InputMedia

class MediaInfoViewModel(private val process: EdProcess) : ViewModel(), MediaInfoEvent {

    override fun removeItem(item: InputMedia) = process.removeFromInputs(item)
}