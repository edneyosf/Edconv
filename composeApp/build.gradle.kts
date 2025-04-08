import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val appName: String by project
val appVersion: String by project
val appDescriptionEn: String by project
val appDescriptionPt: String by project

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
            val resourceDir = File("resources")

            targetFormats(TargetFormat.Exe, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Rpm)
            packageName = appName
            packageVersion = appVersion
            description = appDescriptionEn

            linux { iconFile.set(resourceDir.resolve("icon.png")) }
            windows { iconFile.set(resourceDir.resolve("icon.ico")) }
        }
    }
}

tasks.register("createDeb") {
    dependsOn("createDistributable")

    val arch = "amd64"
    val build = layout.buildDirectory.dir("createDeb")
    val output = layout.buildDirectory.dir("compose/binaries/main/deb")
    val dist = layout.buildDirectory.dir("compose/binaries/main/app")

    doLast {
        val buildDir = build.get().asFile
        val outputDir = output.get().asFile
        val distDir = dist.get().asFile
        val outputDeb = outputDir.resolve("$appName-$appVersion.deb")
        val targetDir = buildDir.resolve("opt")
        val shareDir = buildDir.resolve("usr/share/applications")
        val desktopFile = shareDir.resolve("$appName.desktop")
        val debianDir = buildDir.resolve("DEBIAN")
        val controlFile = debianDir.resolve("control")
        val binFile = targetDir.resolve("$appName/bin/$appName")

        buildDir.run {
            deleteRecursively()
            mkdirs()
        }
        outputDir.run {
            deleteRecursively()
            mkdirs()
        }

        distDir.copyRecursively(targetDir, overwrite = true)
        shareDir.mkdirs()

        // .desktop
        desktopFile.writeText(
            """
            [Desktop Entry]
            Name=${appName.replaceFirstChar { it.uppercase() }}
            Comment=$appDescriptionEn
            Comment[pt_BR]=$appDescriptionPt
            Exec=/opt/$appName/bin/$appName
            Icon=/opt/$appName/lib/$appName.png
            Terminal=false
            Type=Application
            Categories=AudioVideo;Utility;
            StartupWMClass=edneyosf-edconv-MainKt
            """.trimIndent()
        )

        // DEBIAN/control
        debianDir.mkdirs()
        controlFile.writeText(
            """
            Package: $appName
            Version: $appVersion
            Section: video
            Priority: optional
            Architecture: $arch
            Maintainer: Edney Osf <edney.osf@gmail.com>
            Description: $appDescriptionEn
            
            """.trimIndent()
        )

        exec { commandLine("chmod", "+x", binFile.absolutePath) }
        exec { commandLine("fakeroot", "dpkg-deb", "--build", buildDir.absolutePath, outputDeb.absolutePath) }

        println(".deb generated at: ${outputDeb.absolutePath}")
    }
}