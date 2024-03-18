package com.darcy.message.lib_im.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * use mutex ensure synchronization
 */
class Concurrency {
    suspend fun testNoMutex() {
        val scope = CoroutineScope(Dispatchers.Default) // may change thread after suspend/resume
        var count = 0
        repeat(1_000) {
            scope.launch {
                count++
                println("count=$count thread=${Thread.currentThread().id}")
            }
        }
        println("count no mutex =$count")
    }

    suspend fun testNoMutexUnconfined() {
        val scope = CoroutineScope(Dispatchers.Unconfined) // do not change thread after suspend/resume
        var count = 0
        repeat(1_000) {
            scope.launch {
                count++
                println("count=$count thread=${Thread.currentThread().id}")
            }
        }
        println("count no mutex =$count")
    }

    suspend fun testWithMutex() {
        val scope = CoroutineScope(Dispatchers.Default)
        val mutex = Mutex(false)
        var count = 0
        repeat(1_000) {
            scope.launch {
                // use mutex only one coroutine can touch the block
                mutex.withLock {
                    count++
                    println("count=$count thread=${Thread.currentThread().id}")
                }
            }
        }
        println("count with mutex =$count")
    }
}