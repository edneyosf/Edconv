package edneyosf.edconv.features.converter.data.mappers

import edneyosf.edconv.features.converter.domain.model.Subtitle
import edneyosf.edconv.ffmpeg.data.SubtitleData

fun List<SubtitleData>.toSubtitleList() = map { it.toSubtitle() }

fun SubtitleData.toSubtitle() = Subtitle(
    codec = codec,
    title = title,
    language = language
)