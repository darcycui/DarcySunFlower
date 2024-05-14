buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    // 添加gradle插件
    dependencies {
        val navigationVersion = "2.6.0"
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:$navigationVersion")
        val hiltVersion = "2.51"
        classpath ("com.google.dagger:hilt-android-gradle-plugin:$hiltVersion")
    }
}
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
}