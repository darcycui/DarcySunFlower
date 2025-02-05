package com.darcy.message.lib_task.exception

class RunTaskException(private val throwable: Throwable) : Exception(throwable) {
    override fun toString(): String {
        return "RunTaskException(type = ${throwable.javaClass.simpleName} message= $message)"
    }
}