package com.darcy.message.lib_app_status.service.task


interface ITask<T> {
    fun execute(count: Int): Result<T>
}