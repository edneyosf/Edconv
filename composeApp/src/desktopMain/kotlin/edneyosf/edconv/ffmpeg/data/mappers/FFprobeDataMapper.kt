package edneyosf.edconv.ffmpeg.data.mappers

import edneyosf.edconv.ffmpeg.common.MediaType
import edneyosf.edconv.ffmpeg.data.AudioData
import edneyosf.edconv.ffmpeg.data.FFprobeData
import edneyosf.edconv.ffmpeg.data.InputMediaData
import edneyosf.edconv.ffmpeg.data.SubtitleData
import edneyosf.edconv.ffmpeg.data.VideoData
import edneyosf.edconv.ffmpeg.data.extensions.toDuration
import java.io.File

fun FFprobeData.toInputMediaData(inputFile: File): InputMediaData? {
    val videoStreams = mutableListOf<VideoData>()
    val audioStreams = mutableListOf<AudioData>()
    val subtitleStreams = mutableListOf<SubtitleData>()

    streams.forEach { stream ->
        when (stream.codecType) {
            MediaType.VIDEO.name.lowercase() -> videoStreams.add(stream.toVideoStream())
            MediaType.AUDIO.name.lowercase() -> audioStreams.add(stream.toAudioStream())
            MediaType.SUBTITLE.name.lowercase() -> subtitleStreams.add(stream.toSubtitleStream())
        }
    }

    val hasVideo = videoStreams.isNotEmpty()
    val hasAudio = audioStreams.isNotEmpty()
    val type = when {
        hasAudio && !hasVideo -> MediaType.AUDIO
        hasVideo -> MediaType.VIDEO
        else -> return null
    }

    return InputMediaData(
        path = inputFile.path,
        type = type,
        formatName = format.formatLongName,
        duration = format.duration?.toDuration(),
        bitRate = format.bitRate,
        size = inputFile.length(),
        videoStreams = videoStreams,
        audioStreams = audioStreams,
        subtitleStreams = subtitleStreams
    )
}