package com.darcy.message.lib_http.helper.impl

import com.darcy.message.lib_http.client.impl.OKHttpHttpClient
import com.darcy.message.lib_http.entity.base.BaseResult
import com.darcy.message.lib_http.helper.IHttpHelper
import com.darcy.message.lib_http.request.CommonRequestAction
import okhttp3.OkHttpClient
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

private const val SERVER_ERROR = "HTTP 500 Internal Server Error."
private const val HTTP_ERROR_TIP =
    "Internet connect error, Please check your network connection and try again."

object RetrofitHttpHelper : IHttpHelper {

    fun okHttpClient(): OkHttpClient {
        return OKHttpHttpClient.okHttpClient()
    }

    /**
     * 带有接收者的函数字面值
     *
     * 函数类型可以有一个额外的接收者类型，它在表示法中的点之前指定： 类型 A.(B) -> C
     * 表示可以在 A 的接收者对象上以一个 B 类型参数来调用并返回一个 C 类型值的函数
     * 在这样的函数字面值内部，传给调用的接收者对象成为隐式的this，以便访问接收者对象的成员而无需任何额外的限定符
     * 这里用于推断泛型T
     *
     * https://book.kotlincn.net/text/lambdas.html
     * https://book.kotlincn.net/text/lambdas.html#%E5%87%BD%E6%95%B0%E7%B1%BB%E5%9E%8B
     * @param block 请求体
     */
    override suspend fun <T> httpRequest(block: CommonRequestAction<T>.() -> Unit) {
        val action: CommonRequestAction<T> = CommonRequestAction<T>().apply(block)
        try {
            action.start?.invoke()
            val result = action.request?.invoke()
            if (isSuccess(result)) {
                action.success?.invoke(result)
            } else {
                action.error?.invoke(result?.reason ?: "Empty reason")
            }
        } catch (ex: Exception) {
            action.error?.invoke(getErrorTipMessage(ex))
        } finally {
            action.finish?.invoke()
        }
    }

    private fun getErrorTipMessage(ex: Throwable): String {
        return if (ex is ConnectException || ex is UnknownHostException || ex is SocketTimeoutException || SERVER_ERROR == ex.message.toString())
            HTTP_ERROR_TIP
        else
            ex.message.toString()
    }

    private fun <T> isSuccess(result: BaseResult<T>?): Boolean {
        val bool: Boolean = result?.run {
            resultcode == "200"
        } ?: run { false }
        return bool
    }

}