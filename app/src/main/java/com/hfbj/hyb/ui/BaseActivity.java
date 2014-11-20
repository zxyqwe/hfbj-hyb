package com.hfbj.hyb.ui;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.Switch;

import com.android.volley.Request;
import com.android.volley.Response;
import com.hfbj.hyb.R;
import com.hfbj.hyb.app.AppContext;
import com.hfbj.hyb.app.AppManager;
import com.hfbj.hyb.net.RequestManager;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

/**
 * Activity 基类
 *
 * @author LiYanZhao
 * @date 14-11-14 下午12:49
 */
public class BaseActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showOverflowMenu();
        setDisplayHomeAsUpEnabled(true); //默认ActionBar  显示home键
        AppManager.getAppManger().addActivity(this); // 将当前Activity添加入管理栈
    }

    /**
     * 设置ActionBar home键是否显示
     *
     * @param isEnabled
     */
    public void setDisplayHomeAsUpEnabled(boolean isEnabled) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(isEnabled);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppContext.getInstance().setCurrentRunningActivity(this); // 设置当前正在运行的Activity
        AppContext.getInstance().setActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManger().finishActivity(this); // /将当前Activity移出管理栈。
    }


    /**
     * 执行网络请求
     * @param request
     */
    public void executeRequest(Request<?> request) {
        RequestManager.addRequest(request, this.getClass().getName());
    }


    /**
     * Overflow在有物理菜单键时不显示，利用反射强制显示
     */
    private void showOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
