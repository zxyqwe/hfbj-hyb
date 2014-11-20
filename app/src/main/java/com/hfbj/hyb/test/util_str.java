package com.hfbj.hyb.test;

import java.util.Arrays;

import android.content.SharedPreferences;

public class util_str {
	util_str() {
		Arrays.sort(aaa);
	}
	
	util_str(SharedPreferences sp) {
		SharedPreferences.Editor editor = sp.edit();
		for (String a : aaa) {
			editor.putString("" + a.hashCode(), a);
			editor.putInt(a.split(",")[1], Integer.parseInt(a.split(",")[0]));
		}
		editor.commit();
	}

	public String[] aaa = {"270,离庚午" };;
}
