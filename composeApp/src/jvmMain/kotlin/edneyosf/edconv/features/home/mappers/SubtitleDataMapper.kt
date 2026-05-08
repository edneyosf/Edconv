package edneyosf.edconv.features.home.mappers

import edneyosf.edconv.features.common.models.Subtitle
import edneyosf.edconv.ffmpeg.data.SubtitleData

fun List<SubtitleData>.toSubtitleList() = map { it.toSubtitle() }

fun SubtitleData.toSubtitle() = Subtitle(
    codecName = codecName,
    codecLongName = codecLongName,
    title = title,
    language = language
)