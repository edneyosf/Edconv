package edneyosf.edconv.ffmpeg.common

/**
 * Defines the type of compression to use for encoding.
 *
 * COPY: No compression is applied. The input is copied directly to the output.
 * CBR (Constant Bitrate): The bitrate is fixed from start to finish, regardless of content complexity.
 * VBR (Variable Bitrate): The bitrate varies depending on content complexity, but aims for a defined average bitrate.
 * CRF (Constant Rate Factor): The bitrate varies dynamically to maintain a constant visual quality throughout the video.
 */
enum class CompressionType { COPY, CBR, VBR, CRF }