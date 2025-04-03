package edconv.common

enum class Channels(val value: String, val text: String) {
    STEREO(value = "2", text = "Stereo (2.0)"),
    SURROUND_51(value = "6", text = "Surround (5.1)");

    fun downmixingFilter(sourceChannels: Int): String? {
        var filter: String? = null

        if(sourceChannels == SURROUND_51.value.toInt() && this == STEREO) {
            filter = DOWNMIXING_51_20_FILTER
        }

        return filter
    }

    companion object {
        fun getAll() = entries.toList()
    }
}

private const val DOWNMIXING_51_20_FILTER = "lowpass=c=LFE:f=120,volume=1.6,pan=stereo|FL=0.8*FL+0.5*FC+0.6*BL+0.4*LFE|FR=0.8*FR+0.5*FC+0.6*BR+0.4*LFE"