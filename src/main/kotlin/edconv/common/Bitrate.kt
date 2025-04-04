package edconv.common

enum class Bitrate(val value: String, val text: String, val mediaType: MediaType) {
    K32(value = "32k", text = "32 kbps", mediaType = MediaType.AUDIO),
    K48(value = "48k", text = "48 kbps", mediaType = MediaType.AUDIO),
    K64(value = "64k", text = "64 kbps", mediaType = MediaType.AUDIO),
    K96(value = "96k", text = "96 kbps", mediaType = MediaType.AUDIO),
    K128(value = "128k", text = "128 kbps", mediaType = MediaType.AUDIO),
    K160(value = "160k", text = "160 kbps", mediaType = MediaType.AUDIO),
    K192(value = "192k", text = "192 kbps", mediaType = MediaType.AUDIO),
    K224(value = "224k", text = "224 kbps", mediaType = MediaType.AUDIO),
    K256(value = "256k", text = "256 kbps", mediaType = MediaType.AUDIO),
    K320(value = "320k", text = "320 kbps", mediaType = MediaType.AUDIO),
    K384(value = "384k", text = "384 kbps", mediaType = MediaType.AUDIO),
    K448(value = "448k", text = "448 kbps", mediaType = MediaType.AUDIO),
    K512(value = "512k", text = "512 kbps", mediaType = MediaType.AUDIO),
    K640(value = "640k", text = "640 kbps", mediaType = MediaType.AUDIO),
    K768(value = "768k", text = "768 kbps", mediaType = MediaType.AUDIO),
    K960(value = "960k", text = "960 kbps", mediaType = MediaType.AUDIO),

    M1(value = "1M", text = "1 Mbps", mediaType = MediaType.VIDEO),
    M1_5(value = "1.5M", text = "1.5 Mbps", mediaType = MediaType.VIDEO),
    M2(value = "2M", text = "2 Mbps", mediaType = MediaType.VIDEO),
    M2_5(value = "2.5M", text = "2.5 Mbps", mediaType = MediaType.VIDEO),
    M3(value = "3M", text = "3 Mbps", mediaType = MediaType.VIDEO),
    M3_5(value = "3.5M", text = "3.5 Mbps", mediaType = MediaType.VIDEO),
    M4(value = "4M", text = "4 Mbps", mediaType = MediaType.VIDEO),
    M4_5(value = "4.5M", text = "4.5 Mbps", mediaType = MediaType.VIDEO),
    M5(value = "5M", text = "5 Mbps", mediaType = MediaType.VIDEO),
    M6(value = "6M", text = "6 Mbps", mediaType = MediaType.VIDEO),
    M8(value = "8M", text = "8 Mbps", mediaType = MediaType.VIDEO),
    M9(value = "9M", text = "9 Mbps", mediaType = MediaType.VIDEO),
    M10(value = "10M", text = "10 Mbps", mediaType = MediaType.VIDEO),
    M12(value = "12M", text = "12 Mbps", mediaType = MediaType.VIDEO),
    M15(value = "15M", text = "15 Mbps", mediaType = MediaType.VIDEO),
    M18(value = "18M", text = "18 Mbps", mediaType = MediaType.VIDEO),
    M20(value = "20M", text = "20 Mbps", mediaType = MediaType.VIDEO),
    M25(value = "25M", text = "25 Mbps", mediaType = MediaType.VIDEO),
    M30(value = "30M", text = "30 Mbps", mediaType = MediaType.VIDEO),
    M40(value = "40M", text = "40 Mbps", mediaType = MediaType.VIDEO),
    M50(value = "50M", text = "50 Mbps", mediaType = MediaType.VIDEO);

    companion object {
        fun getAllForAudio() = entries.toList().filter { it.mediaType == MediaType.AUDIO }
        fun getAllForVideo() = entries.toList().filter { it.mediaType == MediaType.VIDEO }
    }
}