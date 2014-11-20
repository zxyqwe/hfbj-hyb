package com.hfbj.hyb.net;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.Volley;
import com.hfbj.hyb.app.AppContext;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;

/**
 * 网络请求管理
 * @author LiYanZhao
 * @date 14-11-15 下午3:07
 */
public class RequestManager {

    private static RequestQueue mRequestQueue;
    private static DefaultHttpClient mHttpClient;

    static{
        mHttpClient = new DefaultHttpClient();
        mRequestQueue = Volley.newRequestQueue(AppContext.getInstance(), new HttpClientStack(mHttpClient)); //网络请求队列
      //  mRequestQueue = Volley.newRequestQueue(AppContext.getInstance()); //网络请求队列
    }

    /**
     * 添加网络请求
     * @param request
     * @param tag
     */
    public static void addRequest(Request<?> request, String tag) {
        if (tag != null)
            request.setTag(tag);
        mRequestQueue.add(request);
    }

    public static void setCookie(String cookie){
        CookieStore cookieStore = mHttpClient.getCookieStore();
        cookieStore.addCookie(new BasicClientCookie("Cookie",cookie));
    }

    /**
     * 销毁网络请求
     * @param tag
     */
    public static void cancel(String tag) {
        mRequestQueue.cancelAll(tag);
    }


}
