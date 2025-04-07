package edneyosf.edconv.core.utils

import java.awt.FileDialog
import java.awt.Frame

object FileUtils {
    fun pickFile(title: String): String? {
        val dialog = FileDialog(Frame(), title, FileDialog.LOAD).apply { isVisible = true }
        val file = dialog.files.firstOrNull()

        return file?.absolutePath
    }
}