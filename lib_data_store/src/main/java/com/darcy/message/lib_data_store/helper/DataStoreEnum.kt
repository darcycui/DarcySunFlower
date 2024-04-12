package com.darcy.message.lib_data_store.helper

enum class DataStoreEnum(private val dataStoreName :String) {
    MAIN_DATA_STORE("main_data_store"),
    ;
    fun getName() = dataStoreName
}