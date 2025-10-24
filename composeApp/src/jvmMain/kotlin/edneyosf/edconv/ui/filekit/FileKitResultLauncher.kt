package edneyosf.edconv.ui.filekit

class PickerResultLauncher(private val onLaunch: () -> Unit) {
    fun launch() { onLaunch() }
}