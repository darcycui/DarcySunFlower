package com.jni.vs;

public class VSNative {
    static {
        System.loadLibrary("SharedObject");
    }

    public static native String getVSString(int id, String name);
}
