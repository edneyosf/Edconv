import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.util.Properties

loadEnvFile()

private val appName = System.getProperty("APP_NAME") ?: ""
private val appVersion = System.getProperty("APP_VERSION") ?: ""
private val appAuthor = System.getProperty("APP_AUTHOR") ?: ""
private val appDescriptionEn = System.getProperty("APP_DESCRIPTION_EN") ?: ""

plugins {
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvm()
    
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)
            implementation(libs.kotlinx.serialization)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.lifecycle.runtime.compose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

compose.desktop {
    application {
        mainClass = "edneyosf.edconv.MainKt"

        nativeDistributions {
            val resourceDir = File("resources")

            targetFormats(
                TargetFormat.Exe,
                TargetFormat.Msi,
                TargetFormat.Deb,
                TargetFormat.Rpm,
                TargetFormat.Dmg
            )
            packageName = appName
            packageVersion = appVersion
            description = appDescriptionEn
            copyright = "© 2025 $appAuthor. All rights reserved."
            vendor = appAuthor

            licenseFile.set(File("../LICENSE"))

            linux { iconFile.set(resourceDir.resolve("icon.png")) }
            windows { iconFile.set(resourceDir.resolve("icon.ico")) }
            macOS { iconFile.set(resourceDir.resolve("icon.icns")) }
        }
    }
}

private fun loadEnvFile() {
    val envFile = rootProject.file(".env")

    if (envFile.exists()) {
        val props = Properties()

        envFile.inputStream().use { props.load(it) }
        props.forEach { (key, value) ->
            val cleanValue = value.toString()
                .removeSurrounding(delimiter = "\"")
                .trim()

            System.setProperty(key.toString(), cleanValue)
        }
    } else {
        println(".env file not found")
    }
}