import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

val binDir = File(project.rootDir, "bin")
val appName = "Edconv"
val appVersion = "1.0.0"
val core = "edconv"
val ffmpeg = "ffmpeg"
val ffprobe = "ffprobe"

group = "com.radiuere"
version = "${appVersion}-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Deb)
            packageName = appName
            packageVersion = appVersion
            description = "A multimedia converter for movies, tv shows and music"
            copyright = "© 2025 Radiuere. All rights reserved."

            appResourcesRootDir.set(project.layout.projectDirectory.dir("src/main/resources"))

            linux {
                iconFile.set(appResourcesRootDir.file("linux/icon.png"))
            }
        }
    }
}

afterEvaluate {
    tasks.named("run") {
        doFirst {
            val buildBinDir = file("${layout.buildDirectory.get()}/bin")
            val coreFile = File(binDir, core)
            val coreBuildFile = File(buildBinDir, core)
            val ffmpegFile = File(binDir, ffmpeg)
            val ffmpegBuildFile = File(buildBinDir, ffmpeg)
            val ffprobeFile = File(binDir, ffprobe)
            val ffprobeBuildFile = File(buildBinDir, ffprobe)

            if(!buildBinDir.exists()) buildBinDir.mkdirs()

            coreFile.copyTo(coreBuildFile, overwrite = true)
            ffmpegFile.copyTo(ffmpegBuildFile, overwrite = true)
            ffprobeFile.copyTo(ffprobeBuildFile, overwrite = true)

            coreBuildFile.setExecutable(true)
            ffmpegBuildFile.setExecutable(true)
            ffprobeBuildFile.setExecutable(true)
        }
    }
}

afterEvaluate {
    tasks.named("packageDeb") {
        doLast {
            val debDir = file("${layout.buildDirectory.get()}/compose/binaries/main/deb")
            val debFile = File(debDir, "${appName.lowercase()}_${appVersion}_amd64.deb")

            if (debFile.exists()) {
                val sourceDir = File(debDir, "source")
                val debianDir = File(sourceDir, "DEBIAN")
                val debianFile = File(debDir, "${appName.lowercase()}.deb")
                val sourceBinDir = File(sourceDir, "opt/${appName.lowercase()}/bin")

                sourceDir.deleteRecursively()
                if (!debianDir.exists()) debianDir.mkdirs()
                debFile.renameTo(debianFile)

                exec { commandLine("dpkg-deb", "-e", debianFile.absolutePath, debianDir.absolutePath) }
                exec { commandLine("dpkg", "-x", debianFile.absolutePath, sourceDir.absolutePath) }

                val coreFile = File(binDir, core)
                val coreSourceFile = File(sourceBinDir, core)
                val ffmpegFile = File(binDir, ffmpeg)
                val ffmpegSourceFile = File(sourceBinDir, ffmpeg)
                val ffprobeFile = File(binDir, ffprobe)
                val ffprobeSourceFile = File(sourceBinDir, ffprobe)

                coreFile.copyTo(coreSourceFile, overwrite = true)
                ffmpegFile.copyTo(ffmpegSourceFile, overwrite = true)
                ffprobeFile.copyTo(ffprobeSourceFile, overwrite = true)
                coreSourceFile.setExecutable(true)
                ffmpegSourceFile.setExecutable(true)
                ffprobeSourceFile.setExecutable(true)

                exec { commandLine("dpkg", "--build", sourceDir.absolutePath, debFile.absolutePath) }

                debianFile.delete()
                sourceDir.deleteRecursively()
            }
            else {
                throw Exception("${debFile.name} does not exist")
            }
        }
    }
}