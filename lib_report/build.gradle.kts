plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.darcy.message.lib_umeng"
    compileSdk = Configs.compileSdks

    defaultConfig {
        minSdk = Configs.minSdks

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        // todo Set the resource file prefix to prevent duplicate resource names
        resourcePrefix = "lib_report_"
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
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    api(project(":lib_common"))

    // 友盟统计SDK
    implementation("com.umeng.umsdk:common:9.8.9")// 必选
    implementation("com.umeng.umsdk:asms:1.8.7.2")// 必选
    implementation("com.umeng.umsdk:apm:2.0.6")// U-APM产品包依赖，必选
}