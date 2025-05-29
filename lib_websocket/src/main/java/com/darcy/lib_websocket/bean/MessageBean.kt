package com.darcy.lib_websocket.bean

data class MessageBean(
    var from: String = "",
    var to: String = "",
    var createTime: String? = "",
    var message: String = ""
)
