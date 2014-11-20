package com.hfbj.hyb.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiYanZhao
 * @date 14-11-26 下午3:00
 */
public abstract class SimpleBaseAdapter<T> extends BaseAdapter {

   public List<T> data = new ArrayList<T>();
   public Context mContext;

    public SimpleBaseAdapter(Context context, List<T> data) {
        this.data = data;
        this.mContext = context;

//        Error:Gradle version 2.1 is required. Current version is 2.2.1. If using the gradle wrapper, try editing the distributionUrl in /Users/liyanzhao/Documents/studio/hfbj-hyb/gradle/wrapper/gradle-wrapper.properties to gradle-2.1-all.zip.
//
//                Please fix the project's Gradle settings.
//                <a href="openGradleSettings">Gradle settings</a>
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        if (position < getCount())
            return data.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, getItemResource(), null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return getView(position, convertView, holder);
    }

    public abstract View getView(int position, View convertView, ViewHolder holder);

    public abstract int getItemResource();
}
