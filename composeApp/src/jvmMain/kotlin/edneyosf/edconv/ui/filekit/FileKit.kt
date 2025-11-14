package edneyosf.edconv.ui.filekit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import edneyosf.edconv.ui.compositions.fileKitDialogSettings
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.openDirectoryPicker
import io.github.vinceglb.filekit.dialogs.openFilePicker
import kotlinx.coroutines.launch

@Composable
fun rememberFilePickerLauncher(
    type: FileKitType = FileKitType.File(),
    title: String? = null,
    directory: PlatformFile? = null,
    onResult: (PlatformFile?) -> Unit,
): PickerResultLauncher {
    val scope = rememberCoroutineScope()
    val dialogSettings = fileKitDialogSettings
    val currentType by rememberUpdatedState(newValue = type)
    val currentTitle by rememberUpdatedState(newValue = title)
    val currentDirectory by rememberUpdatedState(newValue = directory)
    val currentOnResult by rememberUpdatedState(newValue = onResult)

    return remember {
        PickerResultLauncher {
            scope.launch {
                currentOnResult(FileKit.openFilePicker(
                    type = currentType,
                    title = currentTitle,
                    directory = currentDirectory,
                    dialogSettings = dialogSettings,
                ))
            }
        }
    }
}

@Composable
fun rememberFilesPickerLauncher(
    type: FileKitType = FileKitType.File(),
    title: String? = null,
    directory: PlatformFile? = null,
    onResult: (List<PlatformFile>?) -> Unit,
): PickerResultLauncher {
    val scope = rememberCoroutineScope()
    val dialogSettings = fileKitDialogSettings
    val currentType by rememberUpdatedState(newValue = type)
    val currentTitle by rememberUpdatedState(newValue = title)
    val currentDirectory by rememberUpdatedState(newValue = directory)
    val currentOnResult by rememberUpdatedState(newValue = onResult)

    return remember {
        PickerResultLauncher {
            scope.launch {
                currentOnResult(FileKit.openFilePicker(
                    type = currentType,
                    mode = FileKitMode.Multiple(),
                    title = currentTitle,
                    directory = currentDirectory,
                    dialogSettings = dialogSettings,
                ))
            }
        }
    }
}

@Composable
fun rememberDirectoryPickerLauncher(
    title: String? = null,
    directory: PlatformFile? = null,
    onResult: (PlatformFile?) -> Unit,
): PickerResultLauncher {
    val scope = rememberCoroutineScope()
    val dialogSettings = fileKitDialogSettings
    val currentTitle by rememberUpdatedState(newValue = title)
    val currentDirectory by rememberUpdatedState(newValue = directory)
    val currentOnResult by rememberUpdatedState(newValue = onResult)

    return remember {
        PickerResultLauncher {
            scope.launch {
                currentOnResult(FileKit.openDirectoryPicker(
                    title = currentTitle,
                    directory = currentDirectory,
                    dialogSettings = dialogSettings,
                ))
            }
        }
    }
}