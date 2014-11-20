package com.hfbj.hyb.app;


import com.hfbj.hyb.BuildConfig;

/**
 * 日志输出类
 *
 * @author LiYanZhao
 * @date 2014-11-14
 */
public class AppLogger {
    protected static final String TAG = "hfbj";

    private AppLogger() {

    }

    /**
     * 输出详细信息
     *
     * @param msg 详细信息
     */
    public static void v(String msg) {
        if (BuildConfig.DEBUG)
            android.util.Log.v(TAG, buildMessage(msg));
    }

    /**
     * 输出详细信息及异常日志
     *
     * @param msg 详细信息
     * @param thr 捕获的异常
     */
    public static void v(String msg, Throwable thr) {
        if (BuildConfig.DEBUG)
            android.util.Log.v(TAG, buildMessage(msg), thr);
    }

    /**
     * 输出调试信息
     *
     * @param msg
     */
    public static void d(String msg) {
        if (BuildConfig.DEBUG)
            android.util.Log.d(TAG, buildMessage(msg));
    }

    /**
     * 输出调试信息及异常日志
     *
     * @param msg 调试信息
     * @param thr 捕获的异常
     */
    public static void d(String msg, Throwable thr) {
        if (BuildConfig.DEBUG)
            android.util.Log.d(TAG, buildMessage(msg), thr);
    }

    /**
     * 输出信息日志
     *
     * @param msg 信息日志
     */
    public static void i(String msg) {
        if (BuildConfig.DEBUG)
            android.util.Log.i(TAG, buildMessage(msg));
    }

    /**
     * 输出信息日志及异常日志
     *
     * @param msg  想输出的信息日志
     * @param thr 捕获的异常
     */
    public static void i(String msg, Throwable thr) {
        if (BuildConfig.DEBUG)
            android.util.Log.i(TAG, buildMessage(msg), thr);
    }

    /**
     * 输出错误信息
     *
     * @param msg 错误信息
     */
    public static void e(String msg) {
        if (BuildConfig.DEBUG)
            android.util.Log.e(TAG, buildMessage(msg));
    }

    /**
     * 输出警告信息
     *
     * @param msg 警告信息
     */
    public static void w(String msg) {
        if (BuildConfig.DEBUG)
            android.util.Log.w(TAG, buildMessage(msg));
    }

    /**
     * 
     *输出警告信息及异常日志
     * @param msg 警告信息
     * @param thr 捕获的异常
     */
    public static void w(String msg, Throwable thr) {
        if (BuildConfig.DEBUG)
            android.util.Log.w(TAG, buildMessage(msg), thr);
    }

    /**
     * 输出异常日志和一个空警告信息
     * @param thr 捕获的异常
     */
    public static void w(Throwable thr) {
        if (BuildConfig.DEBUG)
            android.util.Log.w(TAG, buildMessage(""), thr);
    }

    /**
     * 输出错误信息及异常日志
     *
     * @param msg 错误信息
     * @param thr 捕获的异常
     */
    public static void e(String msg, Throwable thr) {
        if (BuildConfig.DEBUG)
            android.util.Log.e(TAG, buildMessage(msg), thr);
    }

    /**
     * 格式化信息日志（添加日志输出所在类名、方法名）
     */
    protected static String buildMessage(String msg) {
        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];

        return new StringBuilder()
                .append(caller.getClassName())
                .append(".")
                .append(caller.getMethodName())
                .append("(): ")
                .append(msg).toString();
    }
}
