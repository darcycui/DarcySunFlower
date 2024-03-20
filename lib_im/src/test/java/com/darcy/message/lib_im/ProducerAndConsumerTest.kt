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
        Thread.sleep(11_000)
    }


    @Test
    fun testProducerAndConsumer2() {
        val producerAndConsumer = ProducerAndConsumer()
        runBlocking {
            repeat(10) {
                producerAndConsumer.produce(it + 1)
            }
            producerAndConsumer.consume()
        }
        Thread.sleep(11_000)
    }
}