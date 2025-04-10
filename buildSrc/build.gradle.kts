// buildSrc/build.gradle.kts
plugins {
    `kotlin-dsl`
}

repositories {
    maven ("https://maven.aliyun.com/repository/public")
    maven("https://maven.aliyun.com/repository/google")
    maven("https://maven.aliyun.com/repository/jcenter")
    maven("https://maven.aliyun.com/repository/gradle-plugin")
    maven("https://jitpack.io")
    mavenCentral()
}
