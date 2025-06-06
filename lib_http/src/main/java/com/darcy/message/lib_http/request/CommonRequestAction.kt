package com.darcy.message.lib_http.request

import com.darcy.message.lib_http.entity.base.BaseResult

open class CommonRequestAction<T> {
    /**
     * http request start
     */
    var start: (() -> Unit)? = null

    /**
     * object http request
     */
    var request: (suspend () -> BaseResult<T>?)? = null
        private set

    /**
     * list http request
     */
    var requestList: (suspend () -> BaseResult<List<T>>?)? = null
        private set

    /**
     * object http request success
     */
    var success: ((BaseResult<T>?) -> Unit)? = null
        private set

    /**
     * list http request success
     */
    var successList: ((BaseResult<List<T>>?) -> Unit)? = null

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

    fun requestList(block: (suspend () -> BaseResult<List<T>>?)?) {
        requestList = block
    }

    fun success(block: ((BaseResult<T>?) -> Unit)) {
        success = block
    }

    fun successList(block: ((BaseResult<List<T>>?) -> Unit)) {
        successList = block
    }

    fun error(block: (String) -> Unit) {
        error = block
    }

    fun finish(block: () -> Unit) {
        finish = block
    }
}