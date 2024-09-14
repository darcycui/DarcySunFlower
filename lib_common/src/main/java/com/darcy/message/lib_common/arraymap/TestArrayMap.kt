package com.darcy.message.lib_common.arraymap

import android.util.ArrayMap

/**
 * ArrayMap
 * 使用同一个数组存储key和value, 内存占用比HashMap小
 * 数据插入和删除效率比HashMap差
 * (插入和删除后会压缩数组,以减少内存占用)
 * 适用于数据量少, 修改不频繁但查询频繁的场景
 */
object TestArrayMap {
    private val arrayMap: ArrayMap<String, String> = ArrayMap()

    fun put(key: String, value: String) {
        arrayMap.put(key, value)
    }

    fun get(key: String): String {
        return arrayMap.get(key) ?: ""
    }

    fun remove(key: String) {
        arrayMap.remove(key)
    }

    fun clear() {
        arrayMap.clear()
    }

    fun print() {
        for (entry in arrayMap) {
            println("key: ${entry.key}, value: ${entry.value}")
        }
    }

    fun test(){
        println("test ArrayMap")
        put("key1", "value1")
        put("key2", "value2")
        put("key3", "value3")

        println("print")
        print()

        remove("key2")
        println("print after remove")
        print()
    }
}