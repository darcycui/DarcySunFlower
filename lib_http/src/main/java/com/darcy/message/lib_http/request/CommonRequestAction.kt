package com.darcy.message.lib_http.request

import com.darcy.message.lib_http.entity.base.BaseResult

open class CommonRequestAction<T> {
    /**
     * http request start
     */
    var start: (() -> Unit)? = null

    /**
     * http request
     */
    var request: (suspend () -> BaseResult<T>?)? = null
        private set

    /**
     * http request success
     */
    var success: ((BaseResult<T>?) -> Unit)? = null

    /**
     * http request error
     */
    var error: ((String) -> Unit)? = null
        private set

    /**
     * http request finish
     */
    var finish: (() -> Unit)? = null
        private set

    fun start(block: () -> Unit) {
        start = block
    }

    fun request(block: (suspend () -> BaseResult<T>?)?) {
        request = block
    }

    fun success(block: ((BaseResult<T>?) -> Unit)) {
        success = block
    }

    fun error(block: (String) -> Unit) {
        error = block
    }

    fun finish(block: () -> Unit) {
        finish = block
    }
}