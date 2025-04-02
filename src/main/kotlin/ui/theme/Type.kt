package ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font

private const val FONTS_PATH = "fonts/"

private val interFontFamily = FontFamily(
    Font(resource = "${FONTS_PATH}inter_regular.ttf", weight = FontWeight.Normal),
    Font(resource = "${FONTS_PATH}inter_medium.ttf", weight = FontWeight.Medium),
    Font(resource = "${FONTS_PATH}inter_semi_bold.ttf", weight = FontWeight.SemiBold),
    Font(resource = "${FONTS_PATH}inter_bold.ttf", weight = FontWeight.Bold)
)

val AppTypography @Composable get() = Typography(
    displayLarge = MaterialTheme.typography.displayLarge.copy(fontFamily = interFontFamily),
    displayMedium = MaterialTheme.typography.displayMedium.copy(fontFamily = interFontFamily),
    displaySmall = MaterialTheme.typography.displaySmall.copy(fontFamily = interFontFamily),
    headlineLarge = MaterialTheme.typography.headlineLarge.copy(fontFamily = interFontFamily),
    headlineMedium = MaterialTheme.typography.headlineMedium.copy(fontFamily = interFontFamily),
    headlineSmall = MaterialTheme.typography.headlineSmall.copy(fontFamily = interFontFamily),
    titleLarge = MaterialTheme.typography.titleLarge.copy(fontFamily = interFontFamily),
    titleMedium = MaterialTheme.typography.titleMedium.copy(fontFamily = interFontFamily),
    titleSmall = MaterialTheme.typography.titleSmall.copy(fontFamily = interFontFamily),
    bodyLarge = MaterialTheme.typography.bodyLarge.copy(fontFamily = interFontFamily),
    bodyMedium = MaterialTheme.typography.bodyMedium.copy(fontFamily = interFontFamily),
    bodySmall = MaterialTheme.typography.bodySmall.copy(fontFamily = interFontFamily),
    labelLarge = MaterialTheme.typography.labelLarge.copy(fontFamily = interFontFamily),
    labelMedium = MaterialTheme.typography.labelMedium.copy(fontFamily = interFontFamily),
    labelSmall = MaterialTheme.typography.labelSmall.copy(fontFamily = interFontFamily)
)
