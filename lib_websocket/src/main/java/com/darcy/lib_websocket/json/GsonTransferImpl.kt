package com.darcy.lib_websocket.json

import com.google.gson.Gson

class GsonTransferImpl: IJsonTransfer {
    companion object {
        private var gson: Gson? = null
        fun getGsonInstance(): Gson {
            if (gson == null) {
                gson = Gson()
            }
            return gson!!
        }
        private var gsonTransferImpl: GsonTransferImpl? = null
        fun getInstance(): GsonTransferImpl {
            if (gsonTransferImpl == null) {
                gsonTransferImpl = GsonTransferImpl()
            }
            return gsonTransferImpl!!
        }
    }
    override fun <T> toJson(data: T): String {
        return getGsonInstance().toJson(data)
    }

    override fun <T> fromJson(json: String, clazz: Class<T>): T {
        return getGsonInstance().fromJson(json, clazz)
    }
}