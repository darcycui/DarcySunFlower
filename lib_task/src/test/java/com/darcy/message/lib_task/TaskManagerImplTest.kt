package com.darcy.message.lib_task

import com.darcy.message.lib_common.exts.FOR_TEST
import com.darcy.message.lib_task.task.ITask
import com.darcy.message.lib_task.task.impl.TestTask1
import com.darcy.message.lib_task.task.impl.TestTask2
import com.darcy.message.lib_task.task.impl.TestTask3
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class TaskManagerImplTest {

    private val taskHelper = TaskHelper

    @Before
    fun setUp() {
        println("setup")
        FOR_TEST = true
    }

    @After
    fun destroy() {
        println("destroy")
    }

    @Test
    fun test_task1() {
        runBlocking {
            println("test start")
            val task1 = TestTask1()
            taskHelper.addTask(task1)
            println("test end")
        }
        Thread.sleep(10_000)
    }

    @Test
    fun test_task1_cancel() {
        runBlocking {
            println("test start")
            val taskA = TestTask1()
            val taskB = TestTask1()
            taskHelper.addTask(taskA)
            taskHelper.addTask(taskB)
            taskHelper.cancelTask(taskB) // task:TestTask1-2 is canceled
            println("test end")
        }
        Thread.sleep(10_000)
    }

    @Test
    fun test_task2_error() {
        runBlocking {
            println("test start")
            taskHelper.addTask(TestTask2())
            println("test end")
        }
        Thread.sleep(10_000)
    }

    @Test
    fun test_task_in_serial() {
        runBlocking {
            println("test start")
            val tasks: MutableList<ITask> = mutableListOf()
            repeat(3) {
                tasks.add(TestTask1())
            }
            tasks.add(TestTask2())
            repeat(3) {
                tasks.add(TestTask1())
            }
            taskHelper.addAllTaskSerial(tasks)
            println("test end")
        }
        Thread.sleep(20_000)
    }

    @Test
    fun test_task_in_parallel_allOf() {
        runBlocking {
            println("test start")
            val tasks: MutableList<ITask> = mutableListOf()
            repeat(10) {
                tasks.add(TestTask1())
            }
            taskHelper.addAllTaskParallelAllOf(tasks)
            println("test end")
        }
        Thread.sleep(10_000)
    }

    @Test
    fun test_task_in_parallel_anyOf() {
        runBlocking {
            println("test start")
            val tasks: MutableList<ITask> = mutableListOf()
            tasks.add(TestTask1())
            tasks.add(TestTask3())
            taskHelper.addAllTaskParallelAnyOf(tasks)
            println("test end")
        }
        Thread.sleep(10_000)
    }

    @Test
    fun test_task_dispatcher() {
        runBlocking {
            println("test start")
            val task1 = TestTask1()
            val task2 = TestTask2()
            taskHelper.addTask(task1)
            taskHelper.addTask(task2)
            println("test end")
        }
        Thread.sleep(10_000)
    }
}
