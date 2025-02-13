package com.darcy.message.lib_task.exception

class CancelException(private val msg: String) : Exception(msg) {
    override fun toString(): String {
        return "CancelException(msg='$msg')"
    }
}