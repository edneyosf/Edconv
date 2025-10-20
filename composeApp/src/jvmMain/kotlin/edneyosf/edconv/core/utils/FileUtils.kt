package edneyosf.edconv.core.utils

import java.awt.FileDialog
import java.awt.Frame
import java.io.File

object FileUtils {

    fun pickFile(title: String): String? {
        val dialog = FileDialog(Frame(), title, FileDialog.LOAD).apply { isVisible = true }
        val file = dialog.files.firstOrNull()

        return file?.absolutePath
    }

    fun saveFile(
        title: String,
        fileName: String = "",
        extension: String
    ): Pair<String, String>? {
        val dialog = FileDialog(Frame(), title, FileDialog.SAVE).apply {
            file = fileName
            isVisible = true
        }
        val directory = dialog.directory ?: return null
        val file = dialog.file ?: return null
        val selectedFile = File(directory, file)
        val finalName = when {
            selectedFile.extension.isBlank() -> "${selectedFile.name}.$extension"
            else -> selectedFile.name
        }

        return directory to finalName
    }
}