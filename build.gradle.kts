// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.kover)
}

rootProject.ext {
    val minSdkVersion = 28
    val compileSdkVersion = 34
    val targetSdkVersion = 34
    val versionCode = 1
    val versionName = "0.1"
}

dependencies {
    kover(project(":app"))
    kover(project(":view"))
    kover(project(":domain"))
    kover(project(":data"))
    kover(project(":datasource-remote"))
    kover(project(":datasource-local"))
}

kover {
    reports {
        // filters for all report types of all build variants
        filters {
            excludes {
                androidGeneratedClasses()
            }
        }
    }
}