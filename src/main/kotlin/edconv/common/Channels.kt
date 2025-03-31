package edconv.common

enum class Channels(val value: String, val text: String, val downmixingFilter: String? = null) {
    STEREO(value = "2", text = "Stereo"),
    SURROUND_51(value = "6", text = "Surround 5.1"),
    DOWNMIXING_SURROUND_51(
        value = "2",
        text = "Stereo (Surround 5.1)",
        downmixingFilter = "lowpass=c=LFE:f=120,volume=1.6,pan=stereo|FL=0.8*FL+0.5*FC+0.6*BL+0.4*LFE|FR=0.8*FR+0.5*FC+0.6*BR+0.4*LFE"
    );

    companion object {
        fun getAll() = entries.toList()
    }
}