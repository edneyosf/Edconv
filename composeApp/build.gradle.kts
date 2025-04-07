import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serialization)
}

kotlin {
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)
            implementation(libs.serialization.json)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

compose.desktop {
    application {
        mainClass = "edneyosf.edconv.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Exe, TargetFormat.Deb)
            packageName = "Edconv"
            packageVersion = "1.0.0"
            description = "A multimedia converter for movies, tv shows and music"
            copyright = "© 2025 Radiuere. All rights reserved."

            linux {
                iconFile.set(File("resources/icon.png"))
            }

            windows {
                iconFile.set(File("resources/icon.ico"))
            }
        }
    }
}
