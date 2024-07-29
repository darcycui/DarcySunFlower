buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.aliyun.com/repository/gradle-plugin")
    }

    // 添加gradle插件
    dependencies {
        // navigation
        classpath (libs.navigation.safe.args.gradle.plugin)
        // hilt
        classpath (libs.dagger.hilt.android.gradle.plugin)

    }
}
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
}