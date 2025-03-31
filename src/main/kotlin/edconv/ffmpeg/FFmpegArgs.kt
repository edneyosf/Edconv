package edconv.ffmpeg

object FFmpegArgs {
    const val LOG_LEVEL = "-loglevel"
    const val INPUT = "-i"
    const val CODEC_AUDIO = "-c:a"
    const val CODEC_VIDEO = "-c:v"
    const val BITRATE_AUDIO = "-b:a"
    const val BITRATE_VIDEO = "-b:v"
    const val VBR = "-vbr"
    const val SAMPLE_RATE = "-ar"
    const val CHANNELS = "-ac"
    const val PRESET = "-preset"
    const val CRF = "-crf"
    const val PROFILE_VIDEO = "-profile:v"
    const val PROFILE_AUDIO = "-profile:a"
    const val PIXEL_FORMAT = "-pix_fmt"
    const val NO_AUDIO_VIDEO = "-an"
    const val FILTER_AUDIO = "-af"
    const val FILTER_VIDEO = "-vf"
}