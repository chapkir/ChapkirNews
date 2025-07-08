import java.util.Properties

plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp.plugin)
    alias(libs.plugins.hilt.plugin)
}

val localProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}
val newsApiKey = localProperties.getProperty("NEWS_API_KEY") ?: ""

android {
    namespace = "com.example.data"
    compileSdk = 34

    defaultConfig {
        buildConfigField("String", "NEWS_API_KEY", "\"$newsApiKey\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {

// Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

// Paging 3
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

// Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

// Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

// Project
    implementation(project(":domain"))
}