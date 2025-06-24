package edneyosf.edconv.core.process

import edneyosf.edconv.core.common.Error
import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.ffmpeg.common.MediaType
import java.io.File

data class MediaQueue(
    val id: String,
    var status: QueueStatus = QueueStatus.NOT_STARTED,
    val input: InputMedia,
    val type: MediaType,
    val command: String,
    val outputFile: File,
    var startTime: String? = null,
    var finishTime: String? = null,
    var duration: String? = null,
    var error: Error? = null
)