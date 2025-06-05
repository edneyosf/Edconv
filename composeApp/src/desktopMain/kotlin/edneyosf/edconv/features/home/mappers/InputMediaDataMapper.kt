package edneyosf.edconv.features.home.mappers

import edneyosf.edconv.core.extensions.toDurationString
import edneyosf.edconv.core.extensions.toReadableBitrate
import edneyosf.edconv.core.extensions.toReadableSize
import edneyosf.edconv.features.common.models.InputMedia
import edneyosf.edconv.ffmpeg.data.InputMediaData

@Throws(IllegalStateException::class)
fun InputMediaData.toInputMedia(): InputMedia {
    val safeDuration = requireNotNull(value = duration) {
        "Cannot map InputMediaData to InputMedia: duration must not be null"
    }

    return InputMedia(
        path = path,
        type = type,
        size = size,
        sizeText = size.toReadableSize(),
        formatName = formatName,
        duration = safeDuration,
        durationText = safeDuration.toDurationString(),
        bitRate = bitRate,
        bitRateText = bitRate?.toReadableBitrate(),
        videos = videoStreams.toVideoList(),
        audios = audioStreams.toAudioList(),
        subtitles = subtitleStreams.toSubtitleList()
    )
}