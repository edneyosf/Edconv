package core.edconv.utils

fun String.isProgress() = this.contains("progress=")
fun String.getProgressJson() = this.substringAfter("progress=")