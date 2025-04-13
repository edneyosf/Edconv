package edneyosf.edconv.edconv.common

enum class CompressionType(val index: Int) {
    CBR(index = 0), // Fixed bitrate from start to finish
    VBR(index = 1), // Variable bitrate, average defined
    CRF(index = 2) // Variable bitrate, constant quality
}