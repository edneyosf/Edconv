import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.io.ByteArrayOutputStream

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

val appName = "edconv"
val version = "1.0.0"

compose.desktop {
    application {
        mainClass = "edneyosf.edconv.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Exe, TargetFormat.Deb)
            packageName = appName
            packageVersion = version
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

val createCustomDeb by tasks.registering {
    dependsOn("createDistributable")

    val arch = "amd64"
    val buildDirDeb = file("$buildDir/compose/binaries/main/deb")
    val outputDeb = file("$buildDir/$appName-$version.deb")

    doLast {
        // Limpa diretório
        buildDirDeb.deleteRecursively()
        buildDirDeb.mkdirs()

        // Copia binários gerados
        val appDist = file("$buildDir/compose/binaries/main/app")
        val targetAppDir = buildDirDeb.resolve("opt")
        appDist.copyRecursively(targetAppDir, overwrite = true)

        // Cria ícone e .desktop
        val shareAppDir = buildDirDeb.resolve("usr/share/applications").apply { mkdirs() }
        val shareIconDir = buildDirDeb.resolve("usr/share/icons/hicolor/128x128/apps").apply { mkdirs() }

        // Ícone (ajuste o caminho se necessário)
        file("resources/icon.png").copyTo(shareIconDir.resolve("$appName.png"))

        // .desktop
        shareAppDir.resolve("$appName.desktop").writeText("""
            [Desktop Entry]
            Name=Edconv
            Comment=A multimedia converter for movies, tv shows and music
            Exec=/opt/$appName/bin/$appName
            Icon=/opt/$appName/lib/$appName.png
            Terminal=false
            Type=Application
            Categories=Utility;
            StartupWMClass=edneyosf-edconv-MainKt
        """.trimIndent())

        // DEBIAN/control
        val debianDir = buildDirDeb.resolve("DEBIAN").apply { mkdirs() }
        debianDir.resolve("control").writeText("""
            Package: $appName
            Version: $version
            Section: base
            Priority: optional
            Architecture: $arch
            Maintainer: Edney Osf - edney.osf@gmail.com
            Description: A multimedia converter for movies, tv shows and music
            
        """.trimIndent())

        exec {
            commandLine("chmod", "+x", targetAppDir.resolve("$appName/bin/$appName").absolutePath)
        }

        // Cria o pacote .deb
        val output = ByteArrayOutputStream()
        exec {
            commandLine("fakeroot", "dpkg-deb", "--build", buildDirDeb.absolutePath, outputDeb.absolutePath)
            standardOutput = output
            errorOutput = output
            isIgnoreExitValue = false
        }

        println("✅ .deb gerado em: ${outputDeb.absolutePath}")
    }
}