package ui.compositions

import androidx.compose.runtime.compositionLocalOf
import app.AppConfigs

val languages = compositionLocalOf { AppConfigs.LANGUAGE_DEFAULT }