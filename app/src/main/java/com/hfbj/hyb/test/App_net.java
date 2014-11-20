package com.hfbj.hyb.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.SharedPreferences;

public class App_net {
	public static int version() {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
			HttpPost httpPost = new HttpPost(
					"http://app.hanbj.cn/hyb/bulletin/version");
			httpPost.setHeader("charset", HTTP.UTF_8);
			
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				String ret = EntityUtils.toString(entity);
				return Integer.parseInt(ret);
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static boolean upload_activity(String member, String activity,
			SharedPreferences sp) {
		try {
			String[] mall = member.split(",");
			JSONObject object = new JSONObject();
			object.put("activity", activity);
			JSONArray jsonarray = new JSONArray();
			for (String n : mall)
				jsonarray.put(n);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(
					"http://app.hanbj.cn/hyb/welcome/onact");
			httpPost.setHeader("charset", HTTP.UTF_8);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("activity", object.toString()));
			params.add(new BasicNameValuePair("list", jsonarray.toString()));
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			String ci_session = sp.getString("ci_session", null);
			if (null != ci_session) {
				httpPost.setHeader("Cookie", "ci_session=" + ci_session);
			}
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				CookieStore mCookieStore = httpClient.getCookieStore();
				List<Cookie> cookies = mCookieStore.getCookies();
				for (int i = 0; i < cookies.size(); i++) {
					if ("ci_session".equals(cookies.get(i).getName())) {
						sp.edit()
								.putString("ci_session",
										cookies.get(i).getValue()).commit();
						break;
					}
				}
				return true;
			}
			sp.edit().putLong("expire", 0).commit();
			sp.edit().putString("ci_session", null).commit();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String surfnet(String path, SharedPreferences sp) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://app.hanbj.cn/hyb/welcome/"
					+ path);
			httpPost.setHeader("charset", HTTP.UTF_8);
			String ci_session = sp.getString("ci_session", null);
			if (null != ci_session) {
				httpPost.setHeader("Cookie", "ci_session=" + ci_session);
			}
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = httpResponse.getEntity();
				String ret = EntityUtils.toString(entity);
				CookieStore mCookieStore = httpClient.getCookieStore();
				List<Cookie> cookies = mCookieStore.getCookies();
				for (int i = 0; i < cookies.size(); i++) {
					if ("ci_session".equals(cookies.get(i).getName())) {
						sp.edit()
								.putString("ci_session",
										cookies.get(i).getValue()).commit();
						sp.edit()
								.putLong(
										"expire",
										cookies.get(i).getExpiryDate()
												.getTime()).commit();

						break;
					}
				}
				if(ret.contains("404.jpg"))
				{
					sp.edit().putLong("expire", 0).commit();
				}
				return ret;
			}
			sp.edit().putLong("expire", 0).commit();
			sp.edit().putString("ci_session", null).commit();
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "访问："+path+"失败！\r\n\r\n"+e.toString();
		}
	}
}
