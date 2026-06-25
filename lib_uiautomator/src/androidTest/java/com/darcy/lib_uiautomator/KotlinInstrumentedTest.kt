package com.darcy.lib_uiautomator

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.uiAutomator
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class KotlinInstrumentedTest {

    @Test
    fun testTG() {
        uiAutomator {
//            startApp("org.telegram.messenger.web")
//            onElement { textAsString()?.contains("Qiao LL") ?: false }.click()
            onElement { className == "android.widget.EditText" }.setText("My input text")
        }
    }
}