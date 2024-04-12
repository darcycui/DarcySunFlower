package com.darcy.message.lib_ui.exts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun Int.inflate(
    context: Context,
    parent: ViewGroup? = null,
    attachToRoot: Boolean = false
): View {
    return LayoutInflater.from(context).inflate(this, parent, attachToRoot)
}