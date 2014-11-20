package com.hfbj.hyb.adapter;

import android.util.SparseArray;
import android.view.View;

/**
 *
 * @author LiYanZhao
 * @date 14-11-26 下午3:03
 */
public class ViewHolder {
    SparseArray<View> views = new SparseArray<View>();
    private View convertView;

    public ViewHolder(View convertView){
        this.convertView = convertView;
    }

    public <T extends View> T getView(int resId){
        View view = views.get(resId);
        if(view == null){
            view = convertView.findViewById(resId);
            views.put(resId,view);
        }
        return (T)view;
    }


//    Error:Gradle version 2.1 is required. Current version is 2.2.1. If using the gradle wrapper, try editing the distributionUrl in /Users/liyanzhao/Documents/Studio/hfbj-hyb/gradle/wrapper/gradle-wrapper.properties to gradle-2.1-all.zip.
//
//    Please fix the project's Gradle settings.

}
