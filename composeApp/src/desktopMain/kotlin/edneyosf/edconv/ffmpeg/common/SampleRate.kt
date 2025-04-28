package edneyosf.edconv.ffmpeg.common

enum class SampleRate(val value: String, val text: String) {
    HZ_44100(value = "44100", text = "44100 Hz"),
    HZ_48000(value = "48000", text = "48000 Hz"),
    HZ_96000(value = "96000", text = "96000 Hz");

    companion object {
        fun getAll() = entries.toList()

        fun fromValue(value: Int?) = when(value) {
            44100 -> HZ_44100
            48000 -> HZ_48000
            96000 -> HZ_96000
            else -> null
        }
    }
}