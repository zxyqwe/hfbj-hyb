package com.hfbj.hyb.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.hfbj.hyb.R;

public class UpdateManager {
	private static final int DOWNLOAD = 1;
	private static final int DOWNLOAD_FINISH = 2;
	private String mSavePath;
	private int progress;
	private boolean cancelUpdate = false;

	private Context mContext;
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 正在下载
			case DOWNLOAD:
				// 设置进度条位置
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				// 安装文件
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context) {
		this.mContext = context;
	}

	public boolean checkUpdate(int ver) {
		if (isUpdate(ver)) {
			return true;
		}
		else return false;
	}

	private boolean isUpdate(int ver) {
		// 获取当前软件版本
		int versionCode = getVersionCode(mContext);
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
		int serviceCode = ver;
		if (serviceCode > versionCode) {
			return true;
		}
		return false;
	}

	private int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			versionCode = context.getPackageManager().getPackageInfo(
					"com.hfbj.hyb", 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	public void showNoticeDialog() {
		// 构造对话框
		Builder builder = new Builder(mContext);
		builder.setTitle("在线更新");
		builder.setMessage("检测到新版本");
		// 更新
		builder.setPositiveButton("现在更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 显示下载对话框
				showDownloadDialog();
			}
		});
		// 稍后更新
		builder.setNegativeButton("稍后更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog() {
		// 构造软件下载对话框
		Builder builder = new Builder(mContext);
		builder.setTitle("正在更新");
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 设置取消状态
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 现在文件
		downloadApk();
	}

	private void downloadApk() {
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	private class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				File mediaStorageDir = new File(
						Environment
								.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
						"汉服北京会员部");
				if (!mediaStorageDir.exists()) {
					if (!mediaStorageDir.mkdirs()) {
						Log.d("HFBJ", "failed to create directory");
						mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
						return;
					}
				}
				mSavePath = mediaStorageDir.getPath();
				URL url = new URL("http://app.hanbj.cn/hyb.apk");
				// 创建连接
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				// 获取文件大小
				int length = conn.getContentLength();
				// 创建输入流
				InputStream is = conn.getInputStream();
				File apkFile = new File(mSavePath, "hyb.apk");
				FileOutputStream fos = new FileOutputStream(apkFile);
				int count = 0;
				// 缓存
				byte buf[] = new byte[1024];
				// 写入到文件中
				do {
					int numread = is.read(buf);
					count += numread;
					// 计算进度条位置
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWNLOAD);
					if (numread <= 0) {
						// 下载完成
						mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
						break;
					}
					// 写入文件
					fos.write(buf, 0, numread);
				} while (!cancelUpdate);// 点击取消就停止下载.
				fos.close();
				is.close();

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
		}
	};

	private void installApk() {
		File apkfile = new File(mSavePath, "hyb.apk");
		if (!apkfile.exists()) {
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}
