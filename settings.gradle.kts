rootProject.name = "Biblioteka"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
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
    repositories {
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

include(":desktopApp")
include(":core:presentation")
include(":core:data")
include(":core:domain")
include(":books:presentation")
include(":books:domain")
include(":books:data")
include(":movies:domain")
include(":movies:data")
include(":movies:presentation")
include(":shows:data")
include(":shows:domain")
include(":shows:presentation")
include(":update_checker")
