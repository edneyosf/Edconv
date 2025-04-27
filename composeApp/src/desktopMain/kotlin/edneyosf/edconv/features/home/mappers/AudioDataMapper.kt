package edneyosf.edconv.features.home.mappers

import edneyosf.edconv.features.common.models.Audio
import edneyosf.edconv.ffmpeg.data.AudioData

fun List<AudioData>.toAudioList() = mapNotNull { data ->
    data.channels?.let {
        Audio(
            codec = data.codec,
            title = data.title,
            language = data.language,
            profile = data.profile,
            channels = it,
            sampleRate = data.sampleRate,
            bitDepth = data.bitDepth
        )
    }
}