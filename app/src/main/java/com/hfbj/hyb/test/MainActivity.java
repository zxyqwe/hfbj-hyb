//package com.hfbj.hyb.test;//package com.hfbj.hyb;
//
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//import net.simonvt.menudrawer.MenuDrawer;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Message;
//import android.os.RemoteException;
//import android.os.StrictMode;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.ProgressDialog;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.ServiceConnection;
//import android.content.SharedPreferences;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class MainActivity extends Activity {
//	private SharedPreferences sp;
//	private ProgressDialog progressDialog;
//	private AlertDialog adi;
//	private Person p;
//	UpdateManager manager = new UpdateManager(this);
//	private MenuDrawer mDrawer;
//	ServiceConnection mcheckAsync = new ServiceConnection() {
//		@Override
//		public void onServiceConnected(ComponentName name, IBinder service) {
//			mcheckRequest = checkRequest.Stub.asInterface(service);
//			try {
//				mcheckRequest.check(mcheckCallback);
//			} catch (RemoteException e) {
//				e.printStackTrace();
//			}
//		}
//
//		@Override
//		public void onServiceDisconnected(ComponentName name) {
//			mcheckRequest = null;
//		}
//	};
//	checkRequest mcheckRequest;
//	checkCallback.Stub mcheckCallback = new checkCallback.Stub() {
//		@Override
//		public void sendCheck(final int res) {
//			boolean r = manager.checkUpdate(res);
//			if (!r) {
//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						Toast.makeText(MainActivity.this, "已经是最新版！",
//								Toast.LENGTH_SHORT).show();
//					}
//				});
//			}
//			else
//			{
//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						manager.showNoticeDialog();}});
//			}
//			if (mcheckRequest != null)
//				unbindService(mcheckAsync);
//		}
//	};
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		mDrawer = MenuDrawer.attach(this);
//		mDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
//		mDrawer.setContentView(R.layout.activity_main);
//		mDrawer.setMenuView(R.layout.menu_scrollview);
//		if (android.os.Build.VERSION.SDK_INT > 9) {
//			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//					.permitAll().build();
//			StrictMode.setThreadPolicy(policy);
//		}
//
//		this.setTitle("汉服北京会员部 测试版");
//		sp = getSharedPreferences("ZXY_MDB", 0);
//
//		@SuppressWarnings("unused")
//		util_str us = new util_str(sp);
//
//		progressDialog = ProgressDialog.show(this, "查询中...", "请等待...", true,
//				false);
//		progressDialog.dismiss();
//		new Thread() {
//			@Override
//			public void run() {
//				if (mcheckRequest == null)
//					bindService(updateCheckAsync.makeIntent(MainActivity.this),
//							mcheckAsync, BIND_AUTO_CREATE);
//			}
//		}.start();
//
//		LayoutInflater factory = LayoutInflater.from(this);
//		View DialogView = factory.inflate(R.layout.dilog, null);
//		AlertDialog.Builder ad = new AlertDialog.Builder(this);
//		ad.setTitle("账号登陆");
//		ad.setView(DialogView);
//		adi = ad.create();
//		adi.setButton(DialogInterface.BUTTON_POSITIVE, "登录",
//				new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						EditText username = (EditText) adi
//								.findViewById(R.id.txt_username);
//						EditText password = (EditText) adi
//								.findViewById(R.id.txt_password);
//						sp.edit()
//								.putString("username",
//										username.getText().toString()).commit();
//						sp.edit()
//								.putString("password",
//										password.getText().toString()).commit();
//						progressDialog.show();
//						new Thread() {
//							@Override
//							public void run() {
//								App_net.surfnet(
//										"index/" + sp.getString("username", "")
//												+ "/"
//												+ sp.getString("password", ""),
//										sp);
//								handler.sendEmptyMessage(0);
//							}
//						}.start();
//					}
//				});
//		adi.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
//				new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//					}
//				});
//		EditText username = (EditText) DialogView
//				.findViewById(R.id.txt_username);
//		EditText password = (EditText) DialogView
//				.findViewById(R.id.txt_password);
//		username.setText(sp.getString("username", null));
//		password.setText(sp.getString("password", null));
//
//		TextView textView = (TextView) findViewById(R.id.textView1);
//		textView.setTextSize(15);
//
//		TextView textView2 = (TextView) findViewById(R.id.textView2);
//		textView2.setTextSize(15);
//
//		TextView textView3 = (TextView) findViewById(R.id.textView3);
//		textView3.setTextSize(15);
//
//		textView.setText("会员信息");
//		textView2.setText("会费情况");
//		textView3.setText("参加活动信息");
//
//		Button btn = (Button) findViewById(R.id.button1);
//		btn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				long exp = sp.getLong("expire", 0);
//				long now = new Date().getTime();
//				if (exp < now) {
//					Toast.makeText(MainActivity.this, "请登录！",
//							Toast.LENGTH_SHORT).show();
//					return;
//				}
//				Intent inrent = new Intent();
//				inrent.setClass(MainActivity.this, testCamera.class);
//				startActivity(inrent);
//			}
//		});
//
//		Button btn2 = (Button) findViewById(R.id.button2);
//		btn2.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				long exp = sp.getLong("expire", 0);
//				long now = new Date().getTime();
//				if (exp < now) {
//					Toast.makeText(MainActivity.this, "请登录！",
//							Toast.LENGTH_SHORT).show();
//					return;
//				}
//				fee(p);
//			}
//		});
//
//		ImageButton btn3 = (ImageButton) findViewById(R.id.button3);
//		btn3.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				long exp = sp.getLong("expire", 0);
//				long now = new Date().getTime();
//				if (exp < now) {
//					Toast.makeText(MainActivity.this, "请登录！",
//							Toast.LENGTH_SHORT).show();
//					return;
//				}
//				fresh();
//			}
//		});
//
//		Button btn4 = (Button) findViewById(R.id.button4);
//		btn4.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				long exp = sp.getLong("expire", 0);
//				long now = new Date().getTime();
//				if (exp < now) {
//					Toast.makeText(MainActivity.this, "请登录！",
//							Toast.LENGTH_SHORT).show();
//					return;
//				}
//				unfee(p);
//			}
//		});
//
//		Button btn5 = (Button) findViewById(R.id.login);
//		btn5.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				mDrawer.closeMenu();
//				adi.show();
//			}
//		});
//
//		Button btn6 = (Button) findViewById(R.id.list);
//		btn6.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (sp.getString("list", "").compareTo("") == 0) {
//					Toast.makeText(MainActivity.this, "列表为空",
//							Toast.LENGTH_SHORT).show();
//					return;
//				}
//				long exp = sp.getLong("expire", 0);
//				long now = new Date().getTime();
//				if (exp < now) {
//					Toast.makeText(MainActivity.this, "请登录！",
//							Toast.LENGTH_SHORT).show();
//					return;
//				}
//				mDrawer.closeMenu();
//				Intent inrent = new Intent();
//				inrent.setClass(MainActivity.this, grouplist.class);
//				startActivity(inrent);
//			}
//		});
//
//		Button btn7 = (Button) findViewById(R.id.baocun);
//		btn7.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				int renshu = (sp.getString("list", "").compareTo("") == 0 ? 0
//						: sp.getString("list", "").split(",").length);
//				if (renshu <= 0) {
//					Toast.makeText(MainActivity.this, "人数为0",
//							Toast.LENGTH_SHORT).show();
//					return;
//				}
//				long exp = sp.getLong("expire", 0);
//				long now = new Date().getTime();
//				if (exp < now) {
//					Toast.makeText(MainActivity.this, "请登录！",
//							Toast.LENGTH_SHORT).show();
//					return;
//				}
//				mDrawer.closeMenu();
//				File m = File_save.save(sp.getString("activity", "")
//						+ "\r\n\r\n" + sp.getString("list", ""),
//						MainActivity.this);
//				note(m);
//				progressDialog.show();
//				new Thread() {
//					@Override
//					public void run() {
//						boolean tmpbool = App_net.upload_activity(
//								sp.getString("list", ""),
//								sp.getString("activity", ""), sp);
//						if (tmpbool) {
//							sp.edit().putString("list", "").commit();
//						} else {
//							Toast.makeText(MainActivity.this, "上传失败！",
//									Toast.LENGTH_SHORT).show();
//						}
//						handler.sendEmptyMessage(1);
//					}
//				}.start();
//			}
//		});
//
//		Button btn8 = (Button) findViewById(R.id.state);
//		btn8.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				mDrawer.closeMenu();
//				new AlertDialog.Builder(MainActivity.this)
//						.setMessage(
//								"活动名称：\r\n"
//										+ sp.getString("activity", "")
//										+ "\r\n已扫描人数：\r\n"
//										+ (sp.getString("list", "").compareTo(
//												"") == 0 ? 0 : sp.getString(
//												"list", "").split(",").length)
//										+ "\r\n登陆到期时间：\r\n"
//										+ new SimpleDateFormat(
//												"yyyy-MM-dd HH:mm:ss", Locale
//														.getDefault())
//												.format(new Date(sp.getLong(
//														"expire", 0))) + "\r\n")
//						.setTitle("当前状态")
//						.setPositiveButton("确定", null)
//						.setNeutralButton("设置活动名称",
//								new DialogInterface.OnClickListener() {
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										set_activity("设置活动名称", "activity");
//									}
//								}).show();
//			}
//		});
//
//		Button btn9 = (Button) findViewById(R.id.log);
//		btn9.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				long exp = sp.getLong("expire", 0);
//				long now = new Date().getTime();
//				if (exp < now) {
//					Toast.makeText(MainActivity.this, "请登录！",
//							Toast.LENGTH_SHORT).show();
//					return;
//				}
//				mDrawer.closeMenu();
//				Intent inrent = new Intent();
//				inrent.setClass(MainActivity.this, PullToRefreshListActivity.class);
//				startActivity(inrent);
//			}
//		});
//
//		Button btn10 = (Button) findViewById(R.id.renew);
//		btn10.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				mDrawer.closeMenu();
//				new Thread() {
//					@Override
//					public void run() {
//						if (mcheckRequest == null)
//							bindService(updateCheckAsync.makeIntent(MainActivity.this),
//									mcheckAsync, BIND_AUTO_CREATE);
//						else
//							try {
//								mcheckRequest.check(mcheckCallback);
//							} catch (RemoteException e) {
//								e.printStackTrace();
//							}
//					}
//				}.start();
//			}
//		});
//	}
//
//	private void unfee(Person p) {
//		if (p == null) {
//			Toast.makeText(this, "没有人可以扣费！", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		final int id = p.id;
//		final int fid = p.last_fee;
//		progressDialog.show();
//		new Thread() {
//			@Override
//			public void run() {
//				App_net.surfnet("unfee_js/" + fid, sp);
//				sp.edit()
//						.putString("result",
//								App_net.surfnet("view_an/" + id, sp)).commit();
//				handler.sendEmptyMessage(1);
//			}
//		}.start();
//	}
//
//	private void fee(Person p) {
//		if (p == null) {
//			Toast.makeText(this, "没有人可以缴费！", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		final int id = p.id;
//		progressDialog.show();
//		new Thread() {
//			@Override
//			public void run() {
//				App_net.surfnet("fee_js/" + id, sp);
//				sp.edit()
//						.putString("result",
//								App_net.surfnet("view_an/" + id, sp)).commit();
//				handler.sendEmptyMessage(1);
//			}
//		}.start();
//	}
//
//	@SuppressLint("HandlerLeak")
//	private Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 1:
//				TextView textView = (TextView) findViewById(R.id.textView1);
//				TextView textView2 = (TextView) findViewById(R.id.textView2);
//				TextView textView3 = (TextView) findViewById(R.id.textView3);
//				String ret = sp.getString("result", "");
//				p = Js_ret.person(ret);
//				textView.setText(p.text);
//				textView2.setText(p.fee);
//				textView3.setText(p.activity);
//				progressDialog.dismiss();
//				return;
//			case 0:
//				progressDialog.dismiss();
//				return;
//			}
//		}
//	};
//
//	private void fresh() {
//		progressDialog.show();
//		new Thread() {
//			@Override
//			public void run() {
//				String ostr = sp.getString(sp.getString("str", ""), "");
//				if (ostr.length() > 1) {
//					String tmp = Str_sort.sort(sp.getString("list", "")
//							+ ostr.split(",")[1] + ",");
//					sp.edit().putString("list", tmp).commit();
//					sp.edit()
//							.putString(
//									"result",
//									App_net.surfnet(
//											"view_an/" + ostr.split(",")[0], sp))
//							.commit();
//				}
//				handler.sendEmptyMessage(1);
//			}
//		}.start();
//	}
//
//	@SuppressWarnings("deprecation")
//	private void note(File m) {
//		Intent intent = new Intent("android.intent.action.VIEW");
//		intent.addCategory("android.intent.category.DEFAULT");
//		intent.setDataAndType(Uri.fromFile(m), "text/plain");
//		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//		Notification notification = new Notification(R.drawable.ic_launcher,
//				"保存至文件" + m.getPath(), System.currentTimeMillis());
//		notification.flags |= Notification.FLAG_AUTO_CANCEL;
//		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//				intent, 0);
//		notification.setLatestEventInfo(this, "保存至文件", m.getPath(),
//				contentIntent);
//		mNotificationManager.notify(0, notification);
//	}
//
//	private void set_activity(String name, final String item) {
//		final EditText et = new EditText(this);
//		new AlertDialog.Builder(this).setTitle("请输入" + name)
//				.setIcon(android.R.drawable.ic_dialog_info).setView(et)
//				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						sp.edit().putString(item, et.getText().toString())
//								.commit();
//					}
//				}).setNegativeButton("取消", null).show();
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		if (1 == sp.getInt("state", 0))
//			return;
//		sp.edit().putInt("state", 1).commit();
//		fresh();
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.activity_main, menu);
//		return false;
//	}
//}
