package com.darcy.lib_download

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import com.darcy.lib_download.statemachine.StateMachine
import com.darcy.lib_download.event.DownloadEvent
import com.darcy.lib_download.event.toMessage
import com.darcy.lib_download.statemachine.DownloadStateMachine
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.xlog.XLogHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class DownloadStateMachineTest {
    var mockLooper: Looper? = null
    var mockHandler: Handler? = null

    @Before
    fun setup() {
        // 1. 模拟Context
        val context = mockk<Context>(relaxed = true)
        XLogHelper.init(context, false)
        logD("-->DownloadStateMachineTest")
        // 2. 模拟 Looper 和 Handler
        mockLooper = mockk<Looper>(relaxed = true)
        mockHandler = mockk<Handler>(relaxed = true) // 使用 relaxed 避免为每个方法打桩

        // 3. 模拟 HandlerThread 的相关行为
        mockkStatic(Looper::class)
        every { Looper.getMainLooper() } returns mockLooper!!

        val handlerThread = mockk<HandlerThread>(relaxed = true)
        // 模拟run方法，使其不执行任何操作
        every { handlerThread.run() } answers {
            println("模拟的run()方法执行了")
            // 这里可以添加自定义逻辑
        }
        every { handlerThread.looper } returns mockLooper!!
        every { mockHandler!!.looper } returns mockLooper!!
        every { mockHandler!!.sendMessage(any<Message>()) } answers {
            // 在这里模拟处理消息的逻辑，例如直接调用 callback
            val msg = it.invocation.args[0] as Message
            println("message=${msg.what} ${msg.obj}")
            msg.callback?.run()
            true
        }
        // 4. 模拟 Message 的相关行为
        mockkStatic(Message::class)
        every { Message.obtain() } answers {
            val msg = mockk<Message>(relaxed = true)
            msg.what = 0 // 默认值
            msg.obj = null // 默认值
            msg
        }

    }

    @After
    fun tearDown() {
    }

    @Test
    fun `test-handler-message`() {
        // 3. 现在你可以使用这个 mockHandler 来测试你的代码
        mockHandler!!.sendMessage(Message.obtain().apply {
            what = 1
            obj = "test message"
        })
        verify {
            mockHandler!!.sendMessage(any())
        }
    }

    @Test
    fun `test-download-state-machine`() {
        val stateMachine: StateMachine = DownloadStateMachine.getInstance()
        runBlocking {
            delay(1_000)
            stateMachine.sendMessage(DownloadEvent.Start.toMessage())
            delay(3_000)
            stateMachine.sendMessage(DownloadEvent.Pause.toMessage())
            delay(3_000)
            stateMachine.sendMessage(DownloadEvent.Start.toMessage())
        }
    }
}