package com.hfbj.hyb.app;

import android.app.Activity;

import java.util.Stack;

/**
 * Activity 管理类
 * @author LiYanZhao
 * @date 14-11-14 下午12:47
 */
public class AppManager {
    private static AppManager appManger = new AppManager();

    private static Stack<Activity> activitys = new Stack<Activity>();

    private AppManager() {

    }

    public static AppManager getAppManger() {
        return appManger;
    }

    /**
     * 添加Activity入管理栈
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activitys.add(activity);
    }

    /**
     * 获取当前正在显示的Activity
     *
     * @return
     */
    public Activity getActivity() {
        return activitys.lastElement();
    }


    /**
     * 获取指定Activity
     *
     * @param clazz
     * @return
     */
    public Activity getActivity(Class<?> clazz) {
        for (Activity activity : activitys) {
            if (activity.getClass().equals(clazz)) {
                return activity;
            }
        }

        return null;
    }


    /**
     * 销毁当前Activity
     */
    public void finishAcivity() {
        Activity activity = activitys.lastElement();
        finishActivity(activity);
    }

    /**
     * 销毁指定Activity
     *
     * @param clazz
     */
    public void finishActivity(Class<?> clazz) {
        for (Activity activity : activitys) {
            if (activity.getClass().equals(clazz)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 销毁Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activitys.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 销毁所有Activity
     */
    public void finishAllActivity() {
        for (Activity activity : activitys) {
            if (activity != null) {
                activity.finish();
            }
        }
        activitys.clear();
    }
}