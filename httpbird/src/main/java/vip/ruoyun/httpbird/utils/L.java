package vip.ruoyun.httpbird.utils;

import android.util.Log;

import vip.ruoyun.httpbird.core.HttpBirdConfiguration;

/**
 * Created by ruoyun on 14-7-24. 日志输出控制类 (Description)
 */
public class L {
    /**
     * 日志输出时的TAG
     */
    private static String mTag = "zyh";

    public static boolean isDebug = HttpBirdConfiguration.isDebug;


    public static void d(String message) {
        if (isDebug) {
            Log.d(mTag, message);
        }
    }

    public static void e(Exception e) {
        if (isDebug) {
            Log.e(mTag, e.getMessage(), e);
        }
    }
}
