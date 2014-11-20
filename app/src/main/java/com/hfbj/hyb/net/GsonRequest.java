package com.hfbj.hyb.net;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.hfbj.hyb.app.AppConfig;
import com.hfbj.hyb.app.AppLogger;

import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
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
    private Response.Listener mListener; //网络加载监听
    private Response.ErrorListener mErrorListener; //网络加载错误监听


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
            mContentBodys.put("Cookie",ci_session);
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
        Map<String, String> headers = (mContentBodys != null ? mContentBodys : super.getHeaders());
        return headers;
    }

    /**
     * 实现支持Post请求
     *
     * @return
     * @throws AuthFailureError
     */
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mContentBodys != null ? mContentBodys : super.getParams();
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

            // T bean = mGson.fromJson(json, clazz);
            return (Response<T>) Response.success(json, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.success(null, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

}
