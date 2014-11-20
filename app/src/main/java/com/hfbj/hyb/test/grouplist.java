//package com.hfbj.hyb.test;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.View;
//import android.widget.*;
//
//import com.hfbj.hyb.R;
//import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;
//
//public class grouplist extends Activity {
//	ArrayAdapter<String> all;
//	SharedPreferences sp;
//	String[] data;
//	private ProgressDialog progressDialog;
//
//	@Override
//	public void onCreate(Bundle savedData) {
//		super.onCreate(savedData);
//		sp = getSharedPreferences("ZXY_MDB", 0);
//		this.setContentView(R.layout.single_expandable_list);
//		ActionSlideExpandableListView list = (ActionSlideExpandableListView) this
//				.findViewById(R.id.list);
//		list.setAdapter(buildDummyData());
//		list.setItemActionListener(
//				new ActionSlideExpandableListView.OnActionClickListener() {
//					@Override
//					public void onClick(View listView, View buttonview,
//							int position) {
//						progressDialog.show();
//						String delName = all.getItem(position);
//						if (buttonview.getId() == R.id.buttonA) {
//							Message msg=new Message();
//							msg.what=0;
//							msg.obj=delName;
//							handler.sendMessage(msg);
//						} else {
//							String tmp = Str_sort.delName(
//									sp.getString("list", ""), delName);
//							sp.edit().putString("list", tmp).commit();
//							handler.sendEmptyMessage(1);
//						}
//					}
//				}, R.id.buttonA, R.id.buttonB);
//
//		progressDialog = ProgressDialog.show(grouplist.this, "查询中...", "请等待...", true,
//				false);
//		progressDialog.dismiss();
//	}
//
//	@SuppressLint("HandlerLeak")
//	private Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 0:
//				SharedPreferences.Editor editor = sp.edit();
//				sp.edit()
//						.putString(
//								"result",
//								App_net.surfnet(
//										"view_an/"
//												+ sp.getInt(
//														(String)msg.obj, 0),
//										sp)).commit();
//				editor.putString("str", "");
//				editor.putInt("state", 0);
//				editor.commit();
//			case 1:
//				progressDialog.dismiss();
//				finish();
//				return;
//			}
//		}
//	};
//
//	public ListAdapter buildDummyData() {
//		data = sp.getString("list", "").split(",");
//		all = new ArrayAdapter<String>(this, R.layout.expandable_list_item,
//				R.id.text, data);
//		return all;
//	}
//}
