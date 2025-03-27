package edconv.common

object SampleRates {
    const val HZ_44100 = "44100"
    const val HZ_48000 = "48000"

    fun getAll() = listOf(HZ_44100, HZ_48000)
}