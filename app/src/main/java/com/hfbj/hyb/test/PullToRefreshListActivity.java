//package com.hfbj.hyb.test;
//
//import android.annotation.SuppressLint;
//import android.app.ListActivity;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.text.format.DateUtils;
//import android.view.Menu;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
//import com.handmark.pulltorefresh.library.PullToRefreshListView;
//import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
//import com.hfbj.hyb.R;
//
//import org.json.JSONArray;
//import org.json.JSONTokener;
//
//import java.text.SimpleDateFormat;
//import java.util.LinkedList;
//
//public final class PullToRefreshListActivity extends ListActivity {
//
//	private LinkedList<String> mListItems;
//	private PullToRefreshListView mPullRefreshListView;
//	private ArrayAdapter<String> mAdapter;
//	private static long refreshTime;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_ptr_list);
//		setTitle("上拉刷新7天");
//
//		refreshTime = System.currentTimeMillis();
//		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
//
//		mPullRefreshListView
//				.setOnRefreshListener(new OnRefreshListener<ListView>() {
//					@Override
//					public void onRefresh(
//							PullToRefreshBase<ListView> refreshView) {
//						PullToRefreshListActivity.this.setTitle("操作日志 "+DateUtils.formatDateTime(
//								getApplicationContext(), refreshTime - 1000
//										* 60 * 60 * 24 * 7,
//								DateUtils.FORMAT_SHOW_TIME
//										| DateUtils.FORMAT_SHOW_DATE
//										| DateUtils.FORMAT_ABBREV_ALL)+" 之后");
//						String label = DateUtils.formatDateTime(
//								getApplicationContext(), refreshTime - 1000
//										* 60 * 60 * 24 * 7,
//								DateUtils.FORMAT_SHOW_TIME
//										| DateUtils.FORMAT_SHOW_DATE
//										| DateUtils.FORMAT_ABBREV_ALL)
//								+ "-"
//								+ DateUtils.formatDateTime(
//										getApplicationContext(), refreshTime
//												- 1000 * 60 * 60 * 24 * 7 * 2,
//										DateUtils.FORMAT_SHOW_TIME
//												| DateUtils.FORMAT_SHOW_DATE
//												| DateUtils.FORMAT_ABBREV_ALL);
//
//						refreshView.getLoadingLayoutProxy()
//								.setLastUpdatedLabel(label);
//
//						new GetDataTask().execute();
//					}
//				});
//
//		mPullRefreshListView
//				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
//
//					@Override
//					public void onLastItemVisible() {
//						Toast.makeText(PullToRefreshListActivity.this,
//								"上拉刷新7天", Toast.LENGTH_SHORT).show();
//					}
//				});
//
//		ListView actualListView = mPullRefreshListView.getRefreshableView();
//
//		mListItems = new LinkedList<String>();
//
//		mAdapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_list_item_1, mListItems);
//
//		SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(
//				this);
//		soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
//		soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
//		soundListener.addSoundEvent(State.REFRESHING, R.raw.refreshing_sound);
//		mPullRefreshListView.setOnPullEventListener(soundListener);
//
//		actualListView.setAdapter(mAdapter);
//
//		mPullRefreshListView.setScrollingWhileRefreshingEnabled(false);
//		mPullRefreshListView.setMode(Mode.PULL_FROM_END);
//	}
//
//	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
//
//		@Override
//		protected String[] doInBackground(Void... params) {
//			String res = "";
//			try {
//				res = App_net.surfnet("log_js/" + (refreshTime / 1000),
//						getSharedPreferences("ZXY_MDB", 0));
//			} catch (Exception e) {
//			}
//			return new String[] { res };
//		}
//
//		private String sw(int j) {
//			switch (j) {
//			case 0:
//				return "fee_time";
//			case 1:
//				return "unfee_time";
//			case 2:
//				return "act_time";
//			default:
//				return "";
//			}
//		}
//
//		@SuppressLint("SimpleDateFormat")
//		@Override
//		protected void onPostExecute(String[] result) {
//			try {
//				String ret = result[0];
//				JSONTokener jsonParser = new JSONTokener(ret);
//				JSONArray all = ((JSONArray) jsonParser.nextValue());
//				JSONArray[] ar = new JSONArray[3];
//				for (int i = 0; i < 3; i++)
//					ar[i] = all.getJSONArray(i);
//				SimpleDateFormat formatter = new SimpleDateFormat(
//						"yyyy-MM-dd HH:mm:ss");
//				int[] a = new int[3];
//				for (int j = 0; j < 3; j++)
//					a[j] = 0;
//				long[] s = new long[3];
//				for (int j = 0; j < 3; j++)
//					s[j] = 0;//Long.MAX_VALUE;
//				for (int i = 0; i < ar[0].length() + ar[1].length()
//						+ ar[2].length(); i++) {
//					for (int j = 0; j < 3; j++)
//					{
//						if (a[j] < ar[j].length()) {
//							s[j] = formatter.parse(
//									ar[j].getJSONObject(a[j]).getString(sw(j)))
//									.getTime();
//						}else {s[j] = 0;//Long.MAX_VALUE;
//
//						}
//					}
//					if (s[0] > s[1]) {
//						if (s[0] > s[2]) {
//							mListItems.addLast("缴费登记\r\n缴费时间："
//									+ ar[0].getJSONObject(a[0]).getString(
//											"fee_time")
//									+ "\r\n会员编号："
//									+ ar[0].getJSONObject(a[0]).getString(
//											"unique_name")
//									+ "\r\n缴费年份："
//									+ ar[0].getJSONObject(a[0]).getString(
//											"year_time")
//									+ "\r\n操作者："
//									+ ar[0].getJSONObject(a[0]).getString(
//											"oper"));
//							a[0]++;
//						} else {
//							mListItems.addLast("活动签到\r\n签到时间："
//									+ ar[2].getJSONObject(a[2]).getString(
//											"act_time")
//									+ "\r\n会员编号："
//									+ ar[2].getJSONObject(a[2]).getString(
//											"unique_name")
//									+ "\r\n活动名称："
//									+ ar[2].getJSONObject(a[2]).getString(
//											"name")
//									+ "\r\n操作者："
//									+ ar[2].getJSONObject(a[2]).getString(
//											"oper"));
//							a[2]++;
//						}
//					} else {
//						if (s[1] > s[2]) {
//							mListItems.addLast("撤销缴费\r\n撤销时间："
//									+ ar[1].getJSONObject(a[1]).getString(
//											"unfee_time")
//									+ "\r\n会员编号："
//									+ ar[1].getJSONObject(a[1]).getString(
//											"unique_name")
//									+ "\r\n缴费年份："
//									+ ar[1].getJSONObject(a[1]).getString(
//											"year_time")
//									+ "\r\n操作者："
//									+ ar[1].getJSONObject(a[1]).getString(
//											"unoper"));
//							a[1]++;
//						} else {
//							mListItems.addLast("活动签到\r\n签到时间："
//									+ ar[2].getJSONObject(a[2]).getString(
//											"act_time")
//									+ "\r\n会员编号："
//									+ ar[2].getJSONObject(a[2]).getString(
//											"unique_name")
//									+ "\r\n活动名称："
//									+ ar[2].getJSONObject(a[2]).getString(
//											"name")
//									+ "\r\n操作者："
//									+ ar[2].getJSONObject(a[2]).getString(
//											"oper"));
//							a[2]++;
//						}
//					}
//				}
//				Toast.makeText(PullToRefreshListActivity.this, "更新"+(ar[0].length() + ar[1].length()
//						+ ar[2].length())+"条记录", Toast.LENGTH_SHORT).show();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			mAdapter.notifyDataSetChanged();
//
//			refreshTime -= 1000 * 60 * 60 * 24 * 7;
//			// Call onRefreshComplete when the list has been refreshed.
//			mPullRefreshListView.onRefreshComplete();
//
//			super.onPostExecute(result);
//		}
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		return false;
//	}
//}
