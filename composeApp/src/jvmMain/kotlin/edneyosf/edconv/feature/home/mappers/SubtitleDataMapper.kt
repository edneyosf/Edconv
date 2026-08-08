package edneyosf.edconv.feature.home.mappers

import edneyosf.edconv.feature.common.models.Subtitle
import edneyosf.edconv.ffmpeg.data.SubtitleData

fun List<SubtitleData>.toSubtitleList() = map { it.toSubtitle() }

fun SubtitleData.toSubtitle() = Subtitle(
    codecName = codecName,
    codecLongName = codecLongName,
    title = title,
    language = language
)