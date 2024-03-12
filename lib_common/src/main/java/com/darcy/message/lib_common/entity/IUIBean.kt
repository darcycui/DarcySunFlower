package com.darcy.message.lib_common.entity

interface IUIBean<T> where T : IDataBean {
    fun generate(dataBean: T): IUIBean<T>
}