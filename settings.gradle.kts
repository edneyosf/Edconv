pluginManagement {
    repositories {
        mavenLocal()
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    val githubActor: String by settings
    val githubToken: String by settings

    repositories {
        mavenLocal()
        maven {
            url = uri("https://maven.pkg.github.com/edneyosf/FileKit")
            credentials {
                username = githubActor
                password = githubToken
            }
        }
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "Edconv"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":composeApp")