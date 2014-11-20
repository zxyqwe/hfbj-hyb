package com.hfbj.hyb.app;

import android.app.Activity;
import android.app.Application;

import com.android.volley.Request;
import com.android.volley.Response;
import com.hfbj.hyb.net.GsonRequest;
import com.hfbj.hyb.net.RequestManager;
import com.hfbj.hyb.net.UrlManager;

/**
 * 应用程序全局环境变量
 * @author LiYanZhao
 * @date 14-11-14 下午12:43
 */
public class AppContext extends Application{
    private static AppContext appContext = null;
    private Activity currentRunningActivity = null; // 当前正在运行的Activity
    private Activity activity = null; // 保存Activity环境

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        //verify();
    }


    public static AppContext getInstance() {
        return appContext;
    }

    public Activity getCurrentRunningActivity() {
        return currentRunningActivity;
    }

    public void setCurrentRunningActivity(Activity currentRunningActivity) {
        this.currentRunningActivity = currentRunningActivity;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        if (this.activity != null)
            return;
        this.activity = activity;
    }

    public void isLogin(){

    }


}
