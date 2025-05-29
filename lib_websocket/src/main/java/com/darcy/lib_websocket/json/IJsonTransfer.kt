package com.darcy.lib_websocket.json

interface IJsonTransfer {
    fun <T> toJson(data: T): String
    fun <T> fromJson(json: String, clazz: Class<T>): T
}