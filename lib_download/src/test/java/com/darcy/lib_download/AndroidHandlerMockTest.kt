package com.darcy.lib_download

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.darcy.message.lib_common.exts.logD
import com.darcy.message.lib_common.xlog.XLogHelper
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class AndroidHandlerMockTest {
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

        mockkStatic(Looper::class)
        every { Looper.getMainLooper() } returns mockLooper!!
        every { mockHandler!!.looper } returns mockLooper!!
        every { mockHandler!!.sendMessage(any<Message>()) } answers {
            // 在这里模拟处理消息的逻辑，例如直接调用 callback
            val msg = it.invocation.args[0] as Message
            println("message=${msg.what} ${msg.obj}")
            msg.callback?.run()
            true
        }
        // 3. 模拟 Message 的相关行为
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
        verify(exactly = 1) {
            Message.obtain()
        }
        verify(exactly = 1) {
            mockHandler!!.sendMessage(any())
        }
    }
}