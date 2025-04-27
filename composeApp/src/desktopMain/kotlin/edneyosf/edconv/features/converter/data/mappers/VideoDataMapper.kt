package edneyosf.edconv.features.converter.data.mappers

import edneyosf.edconv.features.converter.domain.model.Video
import edneyosf.edconv.ffmpeg.data.VideoData

fun List<VideoData>.toVideoList() = mapNotNull { data ->
    if (data.width != null && data.height != null) {
        Video(
            codec = data.codec,
            title = data.title,
            language = data.language,
            profile = data.profile,
            width = data.width,
            height = data.height,
            bitDepth = data.bitDepth,
            pixFmt = data.pixFmt,
            fps = data.fps,
            level = data.level,
            filmGrain = data.filmGrain,
            displayAspectRatio = data.displayAspectRatio,
            fieldOrder = data.fieldOrder
        )
    }
    else {
        null
    }
}