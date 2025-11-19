package edneyosf.edconv.features.mediainfo

import edneyosf.edconv.features.common.models.InputMedia

interface MediaInfoEvent {
    fun removeItem(item: InputMedia) = Unit
}