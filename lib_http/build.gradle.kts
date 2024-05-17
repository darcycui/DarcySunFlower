plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}
val compileSdks = 34
val minSdks = 26
val targetSdks = 34
val versionCodes = 1
val versionNames = "1.0"

android {
    namespace = "com.darcy.message.lib_http"
    compileSdk = compileSdks

    defaultConfig {
        minSdk = minSdks

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        // todo 设置资源文件前缀 防止资源名重复
        resourcePrefix = "lib_http_"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        // generate buildConfig
        buildConfig = true
    }
    // 单元测试配置
    testOptions {
        unitTests{
            isReturnDefaultValues = true
        }
    }
}

dependencies {

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    api (project(":lib_common"))

    api(libs.retrofit)
    api(libs.retrofit.converter.gson)
    api(libs.okhttp3.logging.interceptor)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.serialization.gson)
    implementation(libs.ktor.client.logging)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
}