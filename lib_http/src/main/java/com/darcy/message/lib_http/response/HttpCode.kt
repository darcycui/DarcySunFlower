package com.darcy.message.lib_http.response

sealed class HttpCode(val code: Int, val message: String) {
    override fun toString(): String {
        return "HttpCode {code=$code message=$message}"
    }

    object EmptyError : HttpCode(0, "message is null")
    object Success : HttpCode(200, "Success")
    object NetworkError :
        HttpCode(-9999, "Internet connect error, Please check your network connection and try again.")

    class UnknownError(code: Int = -1, message: String) : HttpCode(code, message)

    object ClientError : HttpCode(400, "Client Error")
    object ServerError : HttpCode(500, "Server Error")
}