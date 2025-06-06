package com.darcy.message.lib_http.response

sealed class HttpError(val code: Int, val message: String) {
    override fun toString(): String {
        return "HttpCode {code=$code message=$message}"
    }

    object EmptyError : HttpError(0, "message is null")
    object Success : HttpError(200, "Success")
    object NetworkError :
        HttpError(-9999, "Internet connect error, Please check your network connection and try again.")

    class UnknownError(code: Int = -1, message: String) : HttpError(code, message)

    object ClientError : HttpError(400, "Client Error")
    object ServerError : HttpError(500, "Server Error")
}