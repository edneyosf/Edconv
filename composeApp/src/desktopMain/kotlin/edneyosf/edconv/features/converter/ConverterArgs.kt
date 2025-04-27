package edneyosf.edconv.features.converter

import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.ffmpeg.common.MediaType

data class ConverterArgs(
    val mediaType: MediaType,
    val input: InputMedia
)