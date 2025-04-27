package edneyosf.edconv.features.converter.data.mappers

import edneyosf.edconv.features.converter.domain.model.Audio
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