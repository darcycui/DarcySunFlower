package com.jni.rust;

public class RustNative {
    static {
        System.loadLibrary("RustAndroidJni");
    }

    public static native String getEncryptStringFromRust(String plainText);
    public static native String getDecryptStringFromRust(String encryptText);
}
