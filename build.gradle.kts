// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.kover)
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.firebase) apply false
    alias(libs.plugins.crashlytics) apply false
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
    // Execute all tests with ./gradlew koverHtmlReport
    reports {
        // filters for all report types of all build variants
        filters {
            excludes {
                androidGeneratedClasses()
                classes(
                    "*Module", //Dependency Injection Koin modules
                    "*.EsmorgaApp", //Application
                    "*.EsmorgaBuildConfigImpl", //Build config
                    "*.data.*.datasource.*", //Datasource interfaces defined in data
                    "*.domain.*.repository.*", //Repository interfaces defined in domain
                    "*.datasource_local.database.*" //Database description files
                )
                annotatedBy("cmm.apps.esmorga.view.Screen") //Screens that receive a ViewModel and cannot be tested with Paparazzi
            }
        }
    }
}