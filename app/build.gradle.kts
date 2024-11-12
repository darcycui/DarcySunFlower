import com.github.megatronking.stringfog.plugin.StringFogExtension
import java.io.FileInputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    // stringfog
    id("stringfog")
}
apply(plugin = "stringfog")
configure<StringFogExtension> {
    // 必要：加解密库的实现类路径，需和上面配置的加解密算法库一致。
    implementation = "com.github.megatronking.stringfog.xor.StringFogImpl"
    // 可选：加密开关，默认开启。
    enable = true
    // 可选：指定需加密的代码包路径，可配置多个，未指定将默认全部加密。
    // fogPackages = arrayOf("com.xxx.xxx")
    kg = com.github.megatronking.stringfog.plugin.kg.RandomKeyGenerator()
    // base64或者bytes
    mode = com.github.megatronking.stringfog.plugin.StringFogMode.bytes
}

android {
    namespace = "com.darcy.message.sunflower"
    compileSdk = Configs.compileSdks

    // use .so in libs folder
    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("libs")
        }
    }

    defaultConfig {
        applicationId = "com.darcy.message.sunflower"
        minSdk = Configs.minSdks
        targetSdk = Configs.targetSdks
        versionCode = Configs.versionCodes
        versionName = Configs.versionNames

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86_64")
        }
        // todo Set the resource file prefix to prevent duplicate resource names
        resourcePrefix = "app_"

        // set build config field
        buildConfigField("String", "API_URL", "\"https://www.default.com\"")
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

    // set flavorDimensions
    flavorDimensions.add("version")

    // set productFlavors
    productFlavors {
        // add flavor
        create("huawei") {
            // set dimension
            dimension = "version"
            // set build config field
            buildConfigField("String", "API_URL", "\"https://www.hauwei.com\"")
            // set res value(not recommended! use src/huawei/res/values/strings.xml instead)
            resValue("string", "app_name", "Sunflower Huawei")
//            applicationIdSuffix = ".huawei"
            versionNameSuffix = "-huawei"
        }
        // add flavor
        create("google") {
            // set dimension
            dimension = "version"
            // set build config field
            buildConfigField("String", "API_URL", "\"https://www.google.com\"")
            // set res value(not recommended! use src/google/res/values/strings.xml instead)
//            resValue("string", "app_name", "Sunflower Google")
            applicationIdSuffix = ".google"
            versionNameSuffix = "-google"
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
//            resValue("xml", "network_security_config", "@xml/network_security_config_release")
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
    buildFeatures {
        // 开启viewBinding
        viewBinding = true
        // enable buildConfig
        buildConfig = true
    }
    sourceSets {
        getByName("huawei") {
            res {
                srcDirs("src\\huawei\\res", "src\\huawei\\res")
            }
        }
        getByName("google") {
            res {
                srcDirs("src\\google\\res", "src\\google\\res")
            }
        }
    }
    // 修改apk名字
    android.applicationVariants.all {
        // 编译类型
        val buildType = this.buildType.name
        val date = SimpleDateFormat("yyyy_MMdd_HHmm").format(Date())
        val flavor = this.flavorName
        outputs.all {
            // 判断是否是输出 apk 类型
            if (buildType.contains("release", ignoreCase = true)) {
                if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                    this.outputFileName =
                        "Sunflower_" + flavor + "_v${android.defaultConfig.versionName}_${date}_${buildType}.apk"
                }
                // 复制到指定文件夹
                assembleProvider.get().doLast {
                    copy {
                        from(File(project.buildDir, "outputs/apk/$flavor/$buildType"))
                        into(File(rootDir, "apks"))
//                rename{"Sunflower_" + flavor + "_v${android.defaultConfig.versionName}_${date}_${buildType}.apk"}
                        include("*.apk")
                    }
                }
            }
            // 将获取到的名称首字母变为大写，比如：release变为Release
            val combineName = "${flavorName.capitalize()}${buildType.capitalize()}"
            // 为我们的任务命名：比如叫packRelease
            val taskName = "pack$combineName"
            // 找到打包的任务，比如release就是assembleRelease任务
            val originName = "assemble$combineName"
            // 创建一个任务专门做我们的自定义打包任务
            project.task(taskName) {
                // 为任务分组
                group = "Pack apk"
                // 执行我们的任务之前会先执行的任务，比如，打release包时会先执行assembleRelease任务
                dependsOn(originName)
                // 执行完任务后，我们将得到的APK 重命名并输出到根目录下的apks文件夹下
//                doLast {
//                    copy {
//                        from(File(project.buildDir, "outputs/apk/$buildType"))
//                        into(File(rootDir, "apks"))
//                        rename { "AppPackDemo-V-$versionName-$date.apk" }
//                        include("*.apk")
//                    }
//                }
            }
        }
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
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation(libs.androidx.ui.test.junit4)

    // hilt di
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    api(project(":lib_common"))
    api(project(":lib_theme"))
    implementation(project(":lib_im"))
    implementation(project(":lib_db"))
    api(project(":lib_ui"))
    api(project(":lib_data_store"))
    api(project(":lib_http"))
    implementation(project(":lib_report"))
    implementation(project(":lib_jni"))
    implementation(project(":lib_security"))
    implementation(project(":lib_status"))

    implementation(libs.androidx.preference.ktx)
    // codelocator依赖
    implementation(libs.codelocator.core)
    // stringfog
    implementation(libs.stringfog.xor)
}

// disable dependency verification foe one library
// use this command to test it: ./gradlew checkDetachedDependencies
tasks.register("checkDetachedDependencies") {
    val detachedConf: FileCollection =
        configurations.detachedConfiguration(dependencies.create("com.guolindev.glance:glance:1.1.0"))
            .apply {
                resolutionStrategy.disableDependencyVerification()
            }
    doLast {
        println("-->checkDetachedDependencies")
        println(detachedConf.files)
    }
}
