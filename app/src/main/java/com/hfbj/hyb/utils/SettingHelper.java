package com.hfbj.hyb.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
/**
 * 保存配置项
 * 
 * @author LiYanZhao
 * @date 2014-9-29 下午2:17:53
 */
public class SettingHelper {
    private static SharedPreferences.Editor editor = null;
    private static SharedPreferences sharedPreferences = null;

    private SettingHelper() {

    }

    private static SharedPreferences.Editor getEditorObject(Context paramContext) {
        if (editor == null)
            editor = PreferenceManager.getDefaultSharedPreferences(paramContext).edit();
        return editor;
    }

    public static int getSharedPreferences(Context paramContext, String key, int defInt) {
        return getSharedPreferencesObject(paramContext).getInt(key, defInt);
    }

    public static long getSharedPreferences(Context paramContext, String key, long defLong) {
        return getSharedPreferencesObject(paramContext).getLong(key, defLong);
    }

    public static Boolean getSharedPreferences(Context paramContext, String key, Boolean defBoolean) {
        return getSharedPreferencesObject(paramContext).getBoolean(key, defBoolean);
    }

    public static String getSharedPreferences(Context paramContext, String key, String defString) {
        return getSharedPreferencesObject(paramContext).getString(key, defString);
    }

    private static SharedPreferences getSharedPreferencesObject(Context paramContext) {
        if (sharedPreferences == null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(paramContext);
        return sharedPreferences;
    }

    public static void setEditor(Context paramContext, String paramString, int valueInt) {
        getEditorObject(paramContext).putInt(paramString, valueInt).commit();
    }

    public static void setEditor(Context paramContext, String paramString, long valueLong) {
        getEditorObject(paramContext).putLong(paramString, valueLong).commit();
    }

    public static void setEditor(Context paramContext, String key, Boolean valueBoolean) {
        getEditorObject(paramContext).putBoolean(key, valueBoolean).commit();
    }

    public static void setEditor(Context paramContext, String key, String valueString) {
        getEditorObject(paramContext).putString(key, valueString).commit();
    }
    
    public static void remove(Context context,String key){
    	getEditorObject(context).remove(key).commit();
    }
    
}
