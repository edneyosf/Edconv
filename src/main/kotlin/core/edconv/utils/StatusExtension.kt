package core.edconv.utils

import core.edconv.EdconvConfigs.STATUS_COMPLETE
import core.edconv.EdconvConfigs.STATUS_ERROR

fun String.isStatusComplete() = this.contains(STATUS_COMPLETE)

fun String.isStatusError() = this.contains(STATUS_ERROR)