package com.hfbj.hyb.net;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hfbj.hyb.app.AppConfig;
import com.hfbj.hyb.app.AppLogger;
import com.hfbj.hyb.bean.MemberBean;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 网络请求类
 *
 * @author LiYanZhao
 * @date 14-11-15 下午2:20
 */
public class GsonRequest<T> extends Request<T> {
    private static final String SET_COOKIE = "Set-Cookie";
    private static final String SESSION_COOKIE = "sessionid";
    private static final String COOKIE = "Cookie";

    private static final String FAILED = "404.jpg"; //失败标志

    private final Gson mGson;
    private Class<T> clazz; //返回数据解析实体
    private Map<String, String> mContentBodys;  //网络请求参数
    private Map<String, String> mHeaders = new HashMap<String, String>();
    private Response.Listener mListener; //网络加载监听
    private Response.ErrorListener mErrorListener; //网络加载错误监听

/**
 * 11-21 16:53:38.081  21583-21583/com.hfbj E/hfbj﹕ com.hfbj.hyb.app.AppConfig.getCookie(): 获取 cookie
 * ci_session=a%3A5%3A%7Bs%3A10%3A%22session_id%22%3Bs%3A32%3A%22afeac57bf2216bcc83b4f1ae04d24990%22%3Bs%3A10%3A%22
 * ip_address%22%3Bs%3A14%3A%22159.226.189.45%22%3Bs%3A10%3A%22user_agent%22%3Bs%3A40%3A%22Apache-HttpClient%2FUNAVAILABLE+%28java+1.4%29%22%3Bs%3A13%3A%22
 * last_activity%22%3Bi%3A1416559989%3Bs%3A9%3A%22user_data%22%3Bs%3A0%3A%22%22%3B%7Dc85f9e9070605c5abf2340f2af5ff48a; expires=Fri, 21-Nov-2014 10:53:09 GMT; path=/
 *
 *
 * 11-21 16:54:40.317  21898-21898/com.hfbj E/hfbj﹕ com.hfbj.hyb.app.AppConfig.getCookie(): 获取 cookie
 * ci_session=a%3A7%3A%7Bs%3A10%3A%22session_id%22%3Bs%3A32%3A%221c496b46c12be4350709fad41c0160d5%22%3Bs%3A10%3A%22
 * ip_address%22%3Bs%3A14%3A%22159.226.189.45%22%3Bs%3A10%3A%22user_agent%22%3Bs%3A40%3A%22Apache-HttpClient%2FUNAVAILABLE+%28java+1.4%29%22%3Bs%3A13%3A%22
 * last_activity%22%3Bi%3A1416560051%3Bs%3A9%3A%22user_data%22%3Bs%3A0%3A%22%22%3Bs%3A8%3A%22username%22%3Bs%3A9%3A%22LiYanZhao%22%3Bs%3A9%3A%22logged_in%22%3Bb%3A1%3B%7D72725ca818be85d3a6dacf48f87ce2a5;
 * expires=Fri, 21-Nov-2014 10:54:11 GMT; path=/
 */
    /**
     * 构造函数
     *
     * @param method        请求方式 （Get/Post）
     * @param url           请求路径
     * @param clazz         返回数据解析实体
     * @param params        网络请求参数
     * @param listener      网络加载监听
     * @param errorlistener 网络加载错误监听
     */
    public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> params, Response.Listener<T> listener, Response.ErrorListener errorlistener) {
        super(method, url, errorlistener);
        this.clazz = clazz;
        this.mContentBodys = (params == null ? new HashMap<String, String>() : params);
        this.mListener = listener;
        this.mErrorListener = errorlistener;
        this.mGson = new Gson();

        String ci_session = AppConfig.getCookie();
        if (!"".equals(ci_session)) {
            //mContentBodys.put("charset", HTTP.UTF_8);
            // mHeaders.put("Cookie",ci_session);
            mContentBodys.put("Cookie", ci_session);
        }
        AppLogger.i(url);
    }


    /**
     * 实现支持Get请求
     *
     * @return
     * @throws AuthFailureError
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = ((mContentBodys != null && !mContentBodys.isEmpty()) ? mContentBodys : super.getHeaders());
        return super.getHeaders();
    }

    /**
     * 实现支持Post请求
     *
     * @return
     * @throws AuthFailureError
     */
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return (mContentBodys != null && !mContentBodys.isEmpty()) ? mContentBodys : super.getParams();
    }

    /**
     * 传递数据解析结果
     *
     * @param response
     */
    @Override
    protected void deliverResponse(T response) {
        if (mListener != null)
            mListener.onResponse(response);
    }

    /**
     * 传递错误信息
     *
     * @param error
     */
    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
        if (mErrorListener != null)
            mErrorListener.onErrorResponse(error);
    }


    /**
     * 解析网络请求结果
     *
     * @param networkResponse
     * @return
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        try {

            String json = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            AppLogger.i(json + "  |  " + clazz + "  |  ");

            //请求失败
            if (json.contains(FAILED)) {
                return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
            }


            Map<String, String> headers = networkResponse.headers;
            AppConfig.saveCookie(headers.get(SET_COOKIE)); //保存Cookie

            T bean;
            if (clazz.newInstance() instanceof MemberBean) {
                bean = (T) parseMember(json);
            } else
                bean = mGson.fromJson(json, clazz);
            return (Response<T>) Response.success(bean, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }


    public MemberBean parseMember(String value) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(value);

            JSONObject json = (JSONObject) jsonArray.get(0);
            MemberBean bean = new Gson().fromJson(json.toString(), MemberBean.class);

            JSONArray pays = (JSONArray) jsonArray.get(1);
            List<MemberBean.Payment> payments = new Gson().fromJson(pays.toString(), new TypeToken<List<MemberBean.Payment>>() {
            }.getType());

            bean.setPayments(payments);
            return bean;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
