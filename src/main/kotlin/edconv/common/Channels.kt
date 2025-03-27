package edconv.common

enum class Channels(val value: String, val text: String) {
    STEREO(value = "2", text = "Stereo"),
    SURROUND_51(value = "6", text = "Surround 5.1"),
    DOWNMIXING_SURROUND_51(value = "62", text = "Stereo (Surround 5.1)");

    companion object {
        fun getAll() = entries.toList()
    }
}