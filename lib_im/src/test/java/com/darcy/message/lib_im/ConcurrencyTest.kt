package com.darcy.message.lib_im

import com.darcy.message.lib_im.coroutine.Concurrency
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ConcurrencyTest {

    @Test
    fun testNoMutex() {
        runBlocking {
            Concurrency().testNoMutex()
        }
        Thread.sleep(3_000)
    }

    @Test
    fun testNoMutexUnconfined() {
        runBlocking {
            Concurrency().testNoMutexUnconfined()
        }
        Thread.sleep(3_000)
    }
    @Test
    fun testWithMutex() {
        runBlocking {
            Concurrency().testWithMutex()
        }
        Thread.sleep(3_000)
    }
}