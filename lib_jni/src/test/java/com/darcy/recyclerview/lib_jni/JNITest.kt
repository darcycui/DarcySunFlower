package com.darcy.recyclerview.lib_jni

import jni.TestJni
import org.junit.Test

class JNITest {
    @Test
    fun test_get_string_from_jni() {
        // no AndroidJNI in java.library.path
        println(TestJni.getString())
    }
}