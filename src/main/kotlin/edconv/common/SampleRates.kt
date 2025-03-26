package edconv.common

object SampleRates {
    const val hz44100 = "44100"
    const val hz48000 = "48000"

    fun getAll() = listOf(hz44100, hz48000)
}