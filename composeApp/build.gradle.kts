import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.util.Properties

loadEnvFile()

private val appName = System.getProperty("APP_NAME") ?: ""
private val appVersion = System.getProperty("APP_VERSION") ?: ""
private val appAuthor = System.getProperty("APP_AUTHOR") ?: ""
private val appDescriptionEn = System.getProperty("APP_DESCRIPTION_EN") ?: ""
private val appCopyright = System.getProperty("APP_COPYRIGHT") ?: ""

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
            implementation(dependencyNotation = compose.runtime)
            implementation(dependencyNotation = compose.foundation)
            implementation(dependencyNotation = compose.material3)
            implementation(dependencyNotation = compose.ui)
            implementation(dependencyNotation = compose.components.resources)
            implementation(dependencyNotation = compose.components.uiToolingPreview)
            implementation(dependencyNotation = compose.materialIconsExtended)
            implementation(dependencyNotation = libs.kotlinx.serialization)
            implementation(dependencyNotation = libs.lifecycle.viewmodel.compose)
            implementation(dependencyNotation = libs.lifecycle.runtime.compose)
            implementation(dependencyNotation = libs.koin.core)
            implementation(dependencyNotation = libs.koin.compose)
            implementation(dependencyNotation = libs.koin.compose.viewmodel)
            implementation(dependencyNotation = libs.filekit.dialogs)
        }
       
        jvmMain.dependencies {
            implementation(dependencyNotation = compose.desktop.currentOs)
            implementation(dependencyNotation = libs.kotlinx.coroutines.swing)
            implementation(dependencyNotation = libs.jna)
            implementation(dependencyNotation = libs.jna.platform)
        }
    }
}

compose.desktop {

    application {
        
        mainClass = "edneyosf.edconv.MainKt"

        buildTypes.release.proguard {
            configurationFiles.from(project.file("compose-desktop.pro"))
        }

        nativeDistributions {
            val resourceDir = File("resources")

            targetFormats(TargetFormat.Exe, TargetFormat.Deb, TargetFormat.Dmg)
            packageName = appName
            packageVersion = appVersion
            description = appDescriptionEn
            copyright = appCopyright
            vendor = appAuthor

            licenseFile.set(File("../LICENSE"))

            linux {
                modules("jdk.security.auth")
                iconFile.set(resourceDir.resolve(relative = "icon.png"))
                shortcut = true
            }

            windows {
                iconFile.set(resourceDir.resolve(relative = "icon.ico"))
                dirChooser = true
                shortcut = true
                menu = true
            }
            
            macOS { 
                iconFile.set(resourceDir.resolve(relative = "icon.icns"))
            }
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