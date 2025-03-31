import edconv.ffmpeg.FFmpeg
import edconv.ffmpeg.FFmpegLogLevel
import kotlin.test.Test

class FFmpegTest {

    @Test
    fun oi() {
        val ffmpeg = FFmpeg.createConstantAudio(
            source = "ffmpeg",
            logLevel = FFmpegLogLevel.INFO,
            input = "music.opus",
            output = "teste.aac",
            codec = "aac",
            bitRate = "192k",
            sampleRate = "44100",
            channels = "2"
        )

        println(ffmpeg.build())
    }
}