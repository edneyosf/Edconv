package edneyosf.edconv.ffmpeg.ffmpeg.extensions

fun MutableList<String>.addCmd(param: String, value: String?) = value?.let {
    add(param)
    add(it)
}