package com.darcy.message.lib_ui.exts

import android.app.Activity
import android.view.View
import com.darcy.message.lib_common.exts.logD
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


/**
 * use snack bar to show tips
 * https://juejin.cn/post/7029216153469714445
 */
fun Activity.showSnackBar(
    str: String,
    length: Int = Snackbar.LENGTH_SHORT,
    onCLick: (() -> Unit)? = null,
    onShow: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null
) {
    val view: View = findViewById(android.R.id.content)
    val snackbar: Snackbar = Snackbar.make(view, str, length)
    snackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar?>() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)
            logD(message = "Snackbar onDismissed")
            onDismiss?.invoke()
        }

        override fun onShown(transientBottomBar: Snackbar?) {
            super.onShown(transientBottomBar)
            logD(message = "Snackbar onShown")
            onShow?.invoke()
        }
    })
    snackbar.setAction("Click") {
        onCLick?.invoke()
        logD(message = "Snackbar onClick")
    }
    snackbar.show()
}