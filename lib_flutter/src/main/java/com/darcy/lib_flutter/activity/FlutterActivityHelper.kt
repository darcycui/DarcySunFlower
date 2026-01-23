package com.darcy.lib_flutter.activity

import android.content.Context
import com.darcy.lib_flutter.preload.FlutterPreloadHelper
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterActivityLaunchConfigs

object FlutterActivityHelper {
    fun startFlutterActivity(
        context: Context,
        engineId: String?,
        initRoute: String,
        dialogStyle: Boolean = false
    ) {
        val backgroundMode = if (dialogStyle) {
            FlutterActivityLaunchConfigs.BackgroundMode.transparent
        } else {
            FlutterActivityLaunchConfigs.BackgroundMode.opaque
        }
        if (engineId != null && FlutterPreloadHelper.hasCachedEngine(engineId)) {
            if (FlutterPreloadHelper.isInitRoute(initRoute)) {
                context.startActivity(FlutterActivity.withCachedEngine(engineId)
                    .backgroundMode(backgroundMode).build(context))
            } else {
                context.startActivity(
                    FlutterActivity.withNewEngine().initialRoute(initRoute)
                        .backgroundMode(backgroundMode).build(context)
                )
            }
        } else {
            context.startActivity(FlutterActivity.createDefaultIntent(context))
        }
    }
}