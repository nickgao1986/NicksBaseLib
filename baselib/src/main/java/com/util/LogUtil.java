package com.util;

import android.util.Log;


public class LogUtil {

    public static void d(String tag, String content) {
        Log.d(tag,content);
    }

    public static void i(String tag, String content) {
        Log.d(tag,content);
    }

    public static void v(String tag, String content) {
        Log.d(tag,content);
    }


    public static void w(String tag, String content) {
        Log.w(tag,content);
    }

    public static void w(String content) {
        Log.w("TAG",content);
    }
    public static void d(String content) {
        Log.d("TAG",content);
    }

    public static void i(String content) {
        Log.i("TAG",content);
    }

    public static void e(String tag, String content) {
        Log.e(tag,content);
    }
    public static void e(String tag, String content, Exception e) {
        Log.e(tag,content+e);
    }
}
