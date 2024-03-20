package com.darcy.message.lib_common.entity

interface IUIBean<T> where T : IDataEntity {
    fun generate(dataEntity: T): IUIBean<T>
}