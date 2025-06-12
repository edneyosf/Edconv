package edneyosf.edconv.features.converter

import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.ffmpeg.common.MediaType

data class ConverterArgs(
    val type: MediaType,
    val input: InputMedia
)