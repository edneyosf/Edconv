package edneyosf.edconv.ffmpeg.data.mappers

import edneyosf.edconv.ffmpeg.data.AudioData
import edneyosf.edconv.ffmpeg.data.StreamData
import edneyosf.edconv.ffmpeg.data.SubtitleData
import edneyosf.edconv.ffmpeg.data.VideoData
import edneyosf.edconv.ffmpeg.data.extensions.toFrameRate

fun StreamData.toVideoStream() = VideoData(
    codecName = codecName,
    codecLongName = codecLongName,
    title = tags?.title,
    language = tags?.language,
    profile = profile,
    width = width,
    height = height,
    bitDepth = bitDepth,
    pixFmt = pixFmt,
    fps = frameRate?.toFrameRate(),
    level = level,
    filmGrain = filmGrain == 1,
    displayAspectRatio = displayAspectRatio,
    fieldOrder = fieldOrder
)

fun StreamData.toAudioStream() = AudioData(
    codecName = codecName,
    codecLongName = codecLongName,
    title = tags?.title,
    language = tags?.language,
    profile = profile,
    channels = channels,
    sampleRate = sampleRate,
    bitDepth = bitDepth,
    bitRate = bitRate
)

fun StreamData.toSubtitleStream() = SubtitleData(
    codecName = codecName,
    codecLongName = codecLongName,
    title = tags?.title,
    language = tags?.language
)