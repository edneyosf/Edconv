package edneyosf.edconv.ffmpeg.common

enum class CompressionType {
    CBR, // Fixed bitrate from start to finish
    VBR, // Variable bitrate, average defined
    CRF  // Variable bitrate, constant quality
}