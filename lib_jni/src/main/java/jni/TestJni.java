package jni;

public class TestJni {
    static {
        System.loadLibrary("AndroidJNI");
    }
    public static native String getString();
}
