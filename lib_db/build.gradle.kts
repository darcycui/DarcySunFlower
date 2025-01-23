plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
}

android {
    namespace = "com.darcy.message.lib_db"
    compileSdk = Configs.compileSdks

    defaultConfig {
        minSdk = Configs.minSdks

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        // 设置room schema导出目录
        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
        // todo Set the resource file prefix to prevent duplicate resource names
        resourcePrefix = "lib_db_"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        // generate buildConfig
        buildConfig = true
    }
}

dependencies {

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    api (project(":lib_common"))
    // todo: use KSP instead of kapt
    kapt (libs.androidx.room.compiler)
    api (libs.androidx.room.runtime)
    api (libs.androidx.room.ktx)

    api (libs.androidx.paging.runtime.ktx)
    // Room Paging library
    implementation (libs.androidx.room.paging)
    implementation (libs.sqlcipher.android)
    debugImplementation (libs.glance)
}