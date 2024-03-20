package com.darcy.message.lib_im.coroutine

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

/**
 * producer thread dispatcher
 */
private val foodProducerThread = Executors.newFixedThreadPool(1).asCoroutineDispatcher()

/**
 * consumer thread dispatcher
 */
private val foodConsumerThread = Executors.newFixedThreadPool(1).asCoroutineDispatcher()

class ProducerAndConsumer {
    private var count = 0
    private val foodChannel = Channel<Int>(5)

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun test() {
        val produceJob = GlobalScope.launch(foodProducerThread) {
            while (true) {
                // produce one food
                foodChannel.send(++count)
                println("Producer:count=$count")
                delay(1000)
            }
        }
        delay(2000)
        val consumeJob = GlobalScope.launch(foodConsumerThread) {
            while (true) {
                // consume one food
                foodChannel.consumeEach {
                    println("Consumer:count=$it")
                    delay(100)
                }
            }
        }
//        produceJob.join()
//        consumeJob.join()
    }

    suspend fun produce(num: Int) {
        GlobalScope.launch(foodProducerThread) {
            // produce one food
            foodChannel.send(num)
            println("Producer:num=$num")
            delay(1000)
        }
    }

    suspend fun consume() {
        GlobalScope.launch(foodConsumerThread) {
            println("Consumer:")
            // consume one food
            foodChannel.consumeEach {
                println("Consumer:num-->$it")
                delay(100)
            }
        }
    }
}