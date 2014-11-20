package com.hfbj.hyb.app;

import android.content.Context;

import com.hfbj.hyb.utils.SettingHelper;

/**
 * @author LiYanZhao
 * @date 14-11-18 上午10:49
 */
public class AppConfig {

    private static final String COOKIE = "Cookie";
    private static Context mContext = AppContext.getInstance();

    /**
     * 获取Cookie
     * @return
     */
    public static String getCookie(){

        AppLogger.e("获取 cookie   "+ getString(COOKIE));

        return getString(COOKIE);

    }

    /**
     * 保存Cookie
     * @param cookie
     */
    public static void saveCookie(String cookie){
        AppLogger.e("保存 cookie   "+ cookie);
        save(COOKIE,cookie);
    }

    /**
     * 清除Cookie
     */
    public static void removeCookie(){
        remove(COOKIE);
    }

    private static void save(String key, String value) {
        SettingHelper.setEditor(mContext, key, value);
    }

    private static String getString(String key) {
        return SettingHelper.getSharedPreferences(mContext, key, "");
    }

    private static void remove(String key){
        SettingHelper.remove(mContext,key);
    }



}
