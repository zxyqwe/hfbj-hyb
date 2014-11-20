package com.hfbj.hyb.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class File_save {

	public static File save(String sss,Context pa_this) {
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
				"汉服北京会员部");
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("HFBJ", "failed to create directory");
				return new File("");
			}
		}
		try {
			String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss",
					Locale.getDefault()).format(new Date());
			File mediaFile;
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ timeStamp + ".txt");
			OutputStreamWriter out = new OutputStreamWriter(
					new FileOutputStream(mediaFile), "UTF8");
			BufferedWriter writer = new BufferedWriter(out);
			writer.write(sss);
			writer.close();
			out.close();
			return mediaFile;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new File("");
	}
}
