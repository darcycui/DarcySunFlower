package com.darcy.lib_flutter.activity

import android.content.Context
import com.darcy.lib_flutter.preload.FlutterPreloadHelper
import io.flutter.embedding.android.FlutterActivity

object FlutterActivityHelper {
    fun startFlutterActivity(context: Context, engineId: String?, initRoute: String) {
        if (engineId != null && FlutterPreloadHelper.hasCachedEngine(engineId)) {
            if (FlutterPreloadHelper.isInitRoute(initRoute)) {
                context.startActivity(FlutterActivity.withCachedEngine(engineId).build(context))
            } else {
                context.startActivity(FlutterActivity.withNewEngine().initialRoute(initRoute).build(context))
            }
        } else {
            context.startActivity(FlutterActivity.createDefaultIntent(context))
        }
    }
}