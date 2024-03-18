package com.darcy.message.lib_im

import com.darcy.message.lib_im.coroutine.ProducerAndConsumer
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ProducerAndConsumerTest {

    @Test
    fun testProducerAndConsumer() {
        runBlocking {
            ProducerAndConsumer().test()

        }
    }
}