package edconv.core.utils

import edconv.core.EdconvConfigs.MEDIA_INFO

fun String.isMediaInfo() = this.contains(MEDIA_INFO)

fun String.retrieveMediaInfoJson() = this.substringAfter(MEDIA_INFO)