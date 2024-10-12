package com.jni.rust;

public class RustNative {
    static {
        System.loadLibrary("LibRust");
    }

    public static native String getStringFromRust();
}
