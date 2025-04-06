import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    `maven-publish`
}

val appVersion = "1.0.0"

group = "com.radiuere"
version = "${appVersion}-SNAPSHOT"

publishing {
    publications {
        create<MavenPublication>("gpr") {
            groupId = "com.radiuere"
            artifactId = "edconv"
            version = appVersion
        }
    }

    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        maven {
            name = "github"
            url = uri("https://maven.pkg.github.com/edneyosf/Edconv")
            credentials {
                username = System.getenv("USERNAME")
                password = System.getenv("TOKEN")
            }
        }
    }
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.swing)
    implementation(libs.serialization.json)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Deb, TargetFormat.Exe)
            packageName = "Edconv"
            packageVersion = appVersion
            description = "A multimedia converter for movies, tv shows and music"
            copyright = "© 2025 Radiuere. All rights reserved."

            appResourcesRootDir.set(project.layout.projectDirectory.dir("src/main/resources"))

            linux {
                iconFile.set(appResourcesRootDir.file("linux/icon.png"))
            }
            windows {
                iconFile.set(appResourcesRootDir.file("windows/icon.ico"))
            }
        }
    }
}