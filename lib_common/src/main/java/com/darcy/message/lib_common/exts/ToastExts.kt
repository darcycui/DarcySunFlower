package com.darcy.message.lib_common.exts

import android.content.Context
import android.widget.Toast

fun Context.toasts(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}
