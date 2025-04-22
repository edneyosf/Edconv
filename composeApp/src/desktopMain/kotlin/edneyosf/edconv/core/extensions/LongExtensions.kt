package edneyosf.edconv.core.extensions

fun Long.toDurationString(): String {
    val totalSeconds = this / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

fun Long.toReadableSize(): String {
    if (this < 1024) return "$this B"

    val unitLabels = listOf("KB", "MB", "GB", "TB")
    var size = this.toDouble()
    var unitIndex = -1

    while (size >= 1024 && unitIndex < unitLabels.lastIndex) {
        size /= 1024
        unitIndex++
    }

    return "%.2f %s".format(size, unitLabels[unitIndex])
}

fun Long.toReadableBitrate(): String {
    return when {
        this >= 1_000_000 -> "%d Mbps".format((this / 1_000_000.0).toInt())
        this >= 1_000     -> "%d Kbps".format((this / 1_000.0).toInt())
        else -> "$this bps"
    }
}