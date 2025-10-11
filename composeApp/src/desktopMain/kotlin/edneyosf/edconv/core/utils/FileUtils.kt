package edneyosf.edconv.core.utils

import java.awt.FileDialog
import java.awt.Frame

object FileUtils {
    fun pickFile(title: String): String? {
        val dialog = FileDialog(Frame(), title, FileDialog.LOAD).apply { isVisible = true }
        val file = dialog.files.firstOrNull()

        return file?.absolutePath
    }

    fun pickDirectory(title: String, fileName: String = ""): Pair<String, String>? {
        val dialog = FileDialog(Frame(), title, FileDialog.SAVE)
            .apply {
                file = fileName
                isVisible = true
            }
        val directory = dialog.directory

        return if(directory != null) Pair(first = directory, second = dialog.file ?: "") else null
    }
}