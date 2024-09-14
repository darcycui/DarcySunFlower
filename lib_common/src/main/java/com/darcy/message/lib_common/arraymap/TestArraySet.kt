package com.darcy.message.lib_common.arraymap

import android.util.ArraySet

/**
 * ArraySet
 * 使用数组存储value, 内存占用比HashSet小
 * 数据插入和删除效率比HashSet差
 * (插入和删除后会压缩数组,以减少内存占用)
 * 适用于数据量少, 修改不频繁但查询频繁的场景
 */
object TestArraySet {
    private val arraySet: ArraySet<String> = ArraySet()

    fun put(value: String) {
        arraySet.add(value)
    }

    fun get(value: String): String {
        return arraySet.valueAt(arraySet.indexOf(value))
    }

    fun remove(value: String) {
        arraySet.remove(value)
    }

    fun clear() {
        arraySet.clear()
    }

    fun print() {
        for (entry in arraySet) {
            println("value: $entry")
        }
    }

    fun test() {
        println("test ArraySet")
        put("value1")
        put("value2")
        put("value3")

        println("print")
        print()

        remove("value2")
        println("print after remove")
        print()
    }
}