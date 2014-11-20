package com.hfbj.hyb.app;

import android.content.Context;

import com.hfbj.hyb.utils.SettingHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiYanZhao
 * @date 14-11-18 上午10:49
 */
public class AppConfig {

    private static final String COOKIE = "Cookie";
    private static Context mContext = AppContext.getInstance();

    public final static int FEE = 15;

    private static List<String> numbers = new ArrayList<String>();

    static{
        numbers.add("270,离庚午");

        for (String number : numbers){
            save(String.valueOf(number.hashCode()),number);
        }
    }


    /**
     * 获取编号Id
     * @param key
     * @return
     */
    public static String getNumberId(String key){
        return getString(key).split(",")[0];
    }


    /**
     * 获取编号
     * @param key
     * @return
     */
    public static String getNumber(String key){
        return getString(key).split(",")[1];
    }


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
        if(cookie != null && !"".equals(cookie)){
            AppLogger.e("保存 cookie   "+ cookie);
            save(COOKIE,cookie);
        }
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
