package edneyosf.edconv.features.converter

import edneyosf.edconv.core.common.Error
import edneyosf.edconv.features.common.models.InputMedia
import java.io.File

data class ConverterQueue(
    val id: String,
    var status: ConverterQueueStatus = ConverterQueueStatus.NOT_STARTED,
    val inputMedia: InputMedia,
    val command: String,
    val outputFile: File,
    var startTime: String? = null,
    var finishTime: String? = null,
    var duration: String? = null,
    var error: Error? = null
)

enum class ConverterQueueStatus {
    NOT_STARTED,
    STARTED,
    IN_PROGRESS,
    ERROR,
    FINISHED
}