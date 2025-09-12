package com.darcy.lib_flutter.preload

import android.content.Context
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

object FlutterPreloadHelper {
    private lateinit var flutterEngine: FlutterEngine
    private var initRoute: String? =  null

    const val FLUTTER_ENGINE_ID_1 = "my_engine_id_1"

    fun preloadFlutterEngine(context: Context, engineId: String, initRoute: String) {
        // Instantiate a FlutterEngine.
        flutterEngine = FlutterEngine(context)

        // Configure an initial route.
        flutterEngine.navigationChannel.setInitialRoute(initRoute)
        this.initRoute = initRoute

        // Start executing Dart code to pre-warm the FlutterEngine.
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        // Cache the FlutterEngine to be used by FlutterActivity.
        FlutterEngineCache
            .getInstance()
            .put(engineId, flutterEngine)
    }

    fun hasCachedEngine(engineId: String): Boolean {
        return FlutterEngineCache.getInstance().contains(engineId)
    }

    fun isInitRoute(targetRoute: String): Boolean {
        return (initRoute?.isNotEmpty() == true) && (initRoute == targetRoute)
    }
}