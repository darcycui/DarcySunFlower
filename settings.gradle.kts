pluginManagement {

    repositories {
        maven ("https://maven.aliyun.com/repository/public")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/jcenter")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        maven("https://jitpack.io")
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    // 注意这块改为 PREFER_SETTINGS
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)

    val storageUrl: String = System.getenv("FLUTTER_STORAGE_BASE_URL") ?: "https://storage.googleapis.com"

    repositories {
        maven ("https://maven.aliyun.com/repository/public")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/jcenter")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        maven("https://jitpack.io")
        // umeng
        maven ("https://repo1.maven.org/maven2/")
        google()
        mavenCentral()
        // flutter 库
        maven("$storageUrl/download.flutter.io")
    }
}

rootProject.name = "Sunflower"
include (":app")
include (":lib_common")
include (":lib_im")
include (":lib_db")
include (":lib_ui")
include (":lib_http")
include (":lib_data_store")
include(":lib_theme")
include(":lib_report")
include(":lib_jni")
include(":lib_security")
include(":lib_status")
include(":lib_camera")
include(":lib_permission")
include(":lib_repackage")
include(":lib_startup")
include(":lib_task")
include(":lib_login")
include(":lib_brand")
include(":lib_websocket")
include(":lib_network_okhttp")
include(":lib_flutter")

// flutter 库
val filePath = settingsDir.parentFile.toString() + "/flutter_module/.android/include_flutter.groovy"
apply(from = File(filePath))
