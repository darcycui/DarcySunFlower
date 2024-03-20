package com.darcy.message.lib_http.request

import com.darcy.message.lib_http.entity.base.BaseResult

class RequestAction<T> {
    /**
     * http request start
     */
    var start: (() -> Unit)? = null
        private set

    /**
     * http request
     */
    var request: (suspend () -> BaseResult<T>)? = null
        private set

    /**
     * http request success
     */
    var success: ((T?) -> Unit)? = null

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

    fun request(block: suspend () -> BaseResult<T>) {
        request = block
    }

    fun start(block: () -> Unit) {
        start = block
    }

    fun success(block: ((T?) -> Unit)) {
        success = block
    }

    fun error(block: (String) -> Unit) {
        error = block
    }

    fun finish(block: () -> Unit) {
        finish = block
    }
}