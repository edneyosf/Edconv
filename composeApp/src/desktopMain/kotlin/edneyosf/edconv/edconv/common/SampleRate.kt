package edneyosf.edconv.edconv.common

enum class SampleRate(val value: String, val text: String) {
    HZ_44100(value = "44100", text = "44100 Hz"),
    HZ_48000(value = "48000", text = "48000 Hz");

    companion object {
        fun getAll() = entries.toList()
    }
}