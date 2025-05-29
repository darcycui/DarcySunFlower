pluginManagement {
    plugins {
        // [GitHub] https://github.com/google/ksp
//        id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
    }

    repositories {
        maven ("https://maven.aliyun.com/repository/public")
        // 使用 repo1 maven 代替 mavenCentral, 注释mavenCentral()
        maven("https://repo1.maven.org/maven2") {
            // 禁用元数据重定向 强制只从此仓库解析
            metadataSources {
                mavenPom()
                artifact()
                ignoreGradleMetadataRedirection()
            }
        }
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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven ("https://maven.aliyun.com/repository/public")
        // 使用 repo1 maven 代替 mavenCentral, 注释mavenCentral()
        maven("https://repo1.maven.org/maven2") {
            // 禁用元数据重定向 强制只从此仓库解析
            metadataSources {
                mavenPom()
                artifact()
                ignoreGradleMetadataRedirection()
            }
        }
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/jcenter")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        // umeng
        maven ("https://repo1.maven.org/maven2/")
        google()
        mavenCentral()
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
