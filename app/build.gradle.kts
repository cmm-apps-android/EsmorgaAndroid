import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kover)
}

android {
    namespace = "cmm.apps.esmorga"
    compileSdk = 34

    defaultConfig {
        applicationId = "cmm.apps.esmorga"
        minSdk = 28
        targetSdk = 34
        versionCode = 101000
        versionName = "1.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    signingConfigs {
        create("release") {
            storeFile = file("../esmorgabuild.keystore.jks")
            keyAlias = System.getenv("BUILD_KEY_ALIAS")
            keyPassword = System.getenv("BUILD_KEY_PASSWORD")
            storePassword = System.getenv("BUILD_STORE_PASSWORD")
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    buildFeatures {
        flavorDimensions += "environment"
    }

    productFlavors {
        create("prod") {
            dimension = "environment"
        }
        create("qa") {
            dimension = "environment"
        }
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    implementation(project(":view"))
    implementation(project(":datasource-remote"))
    implementation(project(":datasource-local"))

    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.koin.test)
    testImplementation(libs.room.ktx)
    testImplementation(libs.room)
    testImplementation(libs.androidx.junit.ktx)
    testImplementation(libs.robolectric)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)

}
