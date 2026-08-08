package edneyosf.edconv.feature.mediainfo

import edneyosf.edconv.feature.common.models.InputMedia

interface MediaInfoEvent {
    fun removeItem(item: InputMedia) = Unit
}