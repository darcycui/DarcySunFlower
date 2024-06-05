package com.darcy.message.lib_ui.exts

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.darcy.message.lib_common.exts.toasts

fun Fragment.toasts(message: String, length: Int = Toast.LENGTH_SHORT) {
    requireContext().toasts(message, length)
}