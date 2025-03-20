package edconv.core.utils

import edconv.core.EdconvConfigs.STATUS_COMPLETE
import edconv.core.EdconvConfigs.STATUS_ERROR

fun String.isStatusComplete() = this.contains(STATUS_COMPLETE)

fun String.isStatusError() = this.contains(STATUS_ERROR)