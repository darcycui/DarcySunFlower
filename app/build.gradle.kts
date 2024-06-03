import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}
val compileSdks = 34
val minSdks = 26
val targetSdks = 34
val versionCodes = 1
val versionNames = "1.0"

android {
    namespace = "com.darcy.message.sunflower"
    compileSdk = compileSdks

    defaultConfig {
        applicationId = "com.darcy.message.sunflower"
        minSdk = minSdks
        targetSdk = targetSdks
        versionCode = versionCodes
        versionName = versionNames

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86_64")
        }
        // todo 设置资源文件前缀 防止资源名重复
        resourcePrefix = "app_"
    }
    // load key store from local.properties
    val localProperties = gradleLocalProperties(File(project.rootDir.absolutePath))
    signingConfigs {
        create("debugSign") {
            storeFile = file(localProperties.getProperty("keystore_path"))
            keyAlias = localProperties.getProperty("key_alias")
            storePassword = localProperties.getProperty("store_password")
            keyPassword = localProperties.getProperty("key_password")
        }

        create("releaseSign") {
            storeFile = file(localProperties.getProperty("keystore_path"))
            keyAlias = localProperties.getProperty("key_alias")
            storePassword = localProperties.getProperty("store_password")
            keyPassword = localProperties.getProperty("key_password")
        }
    }
    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = signingConfigs.getByName("debugSign")
            // network security config
            resValue("xml", "network_security_config", "@xml/network_security_config_debug")
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("releaseSign")
            // network security config
            resValue("xml", "network_security_config", "@xml/network_security_config_release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    // 开启viewBinding
    buildFeatures {
        viewBinding = true
    }
}

fun gradleLocalProperties(projectRootDir: File): Properties {
    val properties = Properties()
    val localProperties = File(projectRootDir, "local.properties")

    if (localProperties.isFile) {
        InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
            properties.load(reader)
        }
    } else {
        println("Gradle local properties file not found at $localProperties")
    }
    return properties
}


dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation(libs.androidx.ui.test.junit4)

    // hilt di
    implementation (libs.hilt.android)
    kapt (libs.hilt.android.compiler)

    api (project(":lib_common"))
    implementation (project(":lib_im"))
    implementation (project(":lib_db"))
    api (project(":lib_ui"))
    api (project(":lib_data_store"))
    api (project(":lib_http"))
}

// disable dependency verification foe one library
// use this command to test it: ./gradlew checkDetachedDependencies
tasks.register("checkDetachedDependencies") {
    val detachedConf: FileCollection = configurations.detachedConfiguration(dependencies.create("com.guolindev.glance:glance:1.1.0")).apply {
        resolutionStrategy.disableDependencyVerification()
    }
    doLast {
        println("-->checkDetachedDependencies")
        println(detachedConf.files)
    }
}
