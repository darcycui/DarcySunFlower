buildscript {
    repositories {
        maven ("https://maven.aliyun.com/repository/public")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/jcenter")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        maven("https://jitpack.io")
        mavenCentral()
        gradlePluginPortal()
        // umeng
//        maven ("https://repo1.maven.org/maven2/")
    }

    // 添加gradle插件
    dependencies {
        // navigation
        classpath (libs.navigation.safe.args.gradle.plugin)
        // hilt
        classpath (libs.dagger.hilt.android.gradle.plugin)
        // StringFog
        classpath (libs.stringfog.gradle.plugin)
        // 选用加解密算法库，默认实现了xor算法，也可以使用自己的加解密库。
        classpath (libs.stringfog.xor)

    }
}
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.androidLibrary) apply false
}