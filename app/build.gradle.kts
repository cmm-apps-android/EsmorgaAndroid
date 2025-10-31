import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kover)
    alias(libs.plugins.firebase)
    alias(libs.plugins.crashlytics)
}

android {
    namespace = "cmm.apps.esmorga"
    compileSdk = 36

    defaultConfig {
        applicationId = "cmm.apps.esmorga"
        minSdk = 28
        targetSdk = 34
        versionCode = 10100
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

        create("testsigningconfig") {
            storeFile = file("../debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }

        debug {
            isDebuggable = true
            isJniDebuggable = true
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("testsigningconfig")
        }
    }

    buildFeatures {
        flavorDimensions += "environment"
        buildConfig = true
    }

    productFlavors {
        create("prod") {
            dimension = "environment"
            buildConfigField("String", "FLAVOR", "\"prod\"")
        }
        create("qa") {
            dimension = "environment"
            buildConfigField("String", "FLAVOR", "\"qa\"")
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
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.koin.test)
    testImplementation(libs.room.ktx)
    testImplementation(libs.room)
    testImplementation(libs.androidx.junit.ktx)
    testImplementation(libs.robolectric)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.gson)
    testImplementation(libs.retrofit)
    testImplementation(libs.retrofit.converter.gson)
}
