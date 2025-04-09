package edneyosf.edconv.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import edconv.composeapp.generated.resources.*
import edconv.composeapp.generated.resources.Res
import edconv.composeapp.generated.resources.Roboto_Bold
import edconv.composeapp.generated.resources.Roboto_Medium
import edconv.composeapp.generated.resources.Roboto_Regular
import org.jetbrains.compose.resources.Font

@Composable
fun appTypography(): Typography {
    val baseline = Typography()
    val robotoFont = FontFamily(
        Font(resource = Res.font.Roboto_Regular, weight = FontWeight.Normal),
        Font(resource = Res.font.Roboto_Medium, weight = FontWeight.Medium),
        Font(resource = Res.font.Roboto_SemiBold, weight = FontWeight.SemiBold),
        Font(resource = Res.font.Roboto_Bold, weight = FontWeight.Bold),
    )

    return Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = robotoFont),
        displayMedium = baseline.displayMedium.copy(fontFamily = robotoFont),
        displaySmall = baseline.displaySmall.copy(fontFamily = robotoFont),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = robotoFont),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = robotoFont),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = robotoFont),
        titleLarge = baseline.titleLarge.copy(fontFamily = robotoFont),
        titleMedium = baseline.titleMedium.copy(fontFamily = robotoFont),
        titleSmall = baseline.titleSmall.copy(fontFamily = robotoFont),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = robotoFont),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = robotoFont),
        bodySmall = baseline.bodySmall.copy(fontFamily = robotoFont),
        labelLarge = baseline.labelLarge.copy(fontFamily = robotoFont),
        labelMedium = baseline.labelMedium.copy(fontFamily = robotoFont),
        labelSmall = baseline.labelSmall.copy(fontFamily = robotoFont),
    )
}