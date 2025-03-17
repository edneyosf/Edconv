package core.edconv.utils

private const val PROGRESS = "progress="

fun String.isProgress() = this.contains(PROGRESS)
fun String.getProgressJson() = this.substringAfter(PROGRESS)