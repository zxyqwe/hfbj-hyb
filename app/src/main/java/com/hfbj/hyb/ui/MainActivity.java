package com.hfbj.hyb.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.hfbj.hyb.R;
import com.hfbj.hyb.app.AppConfig;
import com.hfbj.hyb.app.AppLogger;
import com.hfbj.hyb.bean.MemberBean;
import com.hfbj.hyb.net.GsonRequest;
import com.hfbj.hyb.net.UrlManager;
import com.hfbj.hyb.utils.AESHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 应用程序主页面
 *
 * @author LiYanZhao
 * @date 14-11-19 下午13:30
 */
public class MainActivity extends BaseActivity {

    private ActionBarHelper mActionBarHelper;
    private DrawerListener mDrawerToggle;

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mActionBarHelper = new ActionBarHelper();
        mDrawerToggle = new DrawerListener(this, mDrawerLayout, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        String content = "li894768914     ";
        String password = "123456";
        String encrypt = AESHelper.encrypt(content);
        AppLogger.e("加密后  " + encrypt);
        AppLogger.e("解密后  " + AESHelper.decrypt(encrypt));

        login();
    }

    public void login() {
        AppConfig.removeCookie();
        executeRequest(new GsonRequest<String>(Request.Method.GET, UrlManager.formatUrl(UrlManager.LOGIN,"",""), String.class, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String value) {
                test();
            }
        }, null));
    }


//    public void login() {
//        AppConfig.removeCookie();
//        Map<String,String> params = new HashMap<String, String>();
//        executeRequest(new GsonRequest<String>(Request.Method.POST, "http://app.hanbj.cn/hyb/app/login", String.class, params, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String value) {
//                try {
//                    JSONArray array = new JSONArray(value);
//                    String v = (String) array.get(3);
//                    AppLogger.e("获取后  " + v +"   " + v.trim());
//
//                    test();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, null));
//    }

    public void test() {
        executeRequest(new GsonRequest<MemberBean>(Request.Method.POST, UrlManager.formatUrl(UrlManager.INFO, "270"), MemberBean.class, null, new Response.Listener<MemberBean>() {
            @Override
            public void onResponse(MemberBean value) {
                value.getGender();
            }
        }, null));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            switch (item.getItemId()) {
                case R.id.qrcode:
                    startActivity(new Intent(this, QRCodeScanActivity.class));
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    /**
     * drawer 展开、收回监听
     */
    public class DrawerListener extends ActionBarDrawerToggle {

        public DrawerListener(Activity activity, DrawerLayout drawerLayout, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            mActionBarHelper.onDrawerOpend();
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            mActionBarHelper.onDrawerClosed();
        }
    }


    /**
     * drawer 展开、收回处理
     */
    private class ActionBarHelper {
        private final ActionBar mActionBar;
        private String mTitle;
        private String mDrawerTitle;

        public ActionBarHelper() {
            mActionBar = getSupportActionBar();
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(false);
            mTitle = (String) getTitle();
            mDrawerTitle = getString(R.string.button1);
        }

        public void onDrawerClosed() {
            mActionBar.setTitle(mTitle);
        }

        public void onDrawerOpend() {
            mActionBar.setTitle(mDrawerTitle);
        }
    }

}
