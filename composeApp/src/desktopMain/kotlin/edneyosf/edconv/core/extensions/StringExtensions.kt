package edneyosf.edconv.core.extensions

fun String.toReadableCommand(): String {
    val regex = Regex(pattern = " (?=-[a-zA-Z])")

    return replace(regex, replacement = "\n")
}

fun String.normalizeCommand(): String {
    val regex = Regex(pattern = "\\s+")

    return replace(oldValue = "\n", newValue = " ")
        .replace(regex, replacement = " ")
        .trim()
}