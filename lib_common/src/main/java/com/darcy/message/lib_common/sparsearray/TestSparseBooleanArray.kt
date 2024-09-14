package com.darcy.message.lib_common.sparsearray

import android.util.SparseBooleanArray
import android.util.SparseLongArray


/**
 * SparseArray
 * 使用2个数组分别存储key和value 没有装箱操作,没有Entry对象,内存占用比HashMap小
 * 使用二分查找,效率略差于HashMap(二分查找返回值的巧妙处理,既表示查找结果,也表示查找结果的下标)
 * 适用于数据量少但频繁修改的场景
 */
object TestSparseBooleanArray {
    private val sparseArray: SparseBooleanArray = SparseBooleanArray()

    fun put(key: Int, value: Boolean) {
        sparseArray.put(key, value)
    }

    fun get(key: Int): Boolean {
        return sparseArray.get(key, false)
    }

    fun remove(key: Int) {
        sparseArray.delete(key)
    }

    fun clear() {
        sparseArray.clear()
    }

    fun print() {
        for (i in 0 until sparseArray.size()) {
            println("sparseArray[$i]: ${sparseArray.keyAt(i)} = ${sparseArray.valueAt(i)}")
        }
    }

    fun test() {
        println("test SparseBooleanArray")
        put(1, true)
        put(2, true)
        put(3, true)

        println("print")
        print()

        remove(2)
        println("print after remove")
        print()
    }
}