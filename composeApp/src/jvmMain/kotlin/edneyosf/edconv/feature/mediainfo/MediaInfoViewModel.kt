package edneyosf.edconv.feature.mediainfo

import androidx.lifecycle.ViewModel
import edneyosf.edconv.core.process.EdProcess
import edneyosf.edconv.feature.common.models.InputMedia

class MediaInfoViewModel(private val process: EdProcess) : ViewModel(), MediaInfoEvent {

    override fun removeItem(item: InputMedia) = process.removeFromInputs(item)
}