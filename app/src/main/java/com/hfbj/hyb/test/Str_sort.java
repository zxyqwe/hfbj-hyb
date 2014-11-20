package com.hfbj.hyb.test;

import java.util.Arrays;

public class Str_sort {
	public static String delName(String a, String delNmae) {
		a = a.replace(delNmae, "");
		return sort(a);
	}

	public static String sort(String a) {
		String[] mlist = a.split(",");
		Arrays.sort(mlist);
		for (int i = 1; i < mlist.length; i++) {
			if (mlist[i].compareTo(mlist[i - 1]) == 0)
				mlist[i - 1] = "";
		}
		Arrays.sort(mlist);
		String ret = "";
		for (String m : mlist) {
			if (m.compareTo("") != 0)
				ret += m + ",";
		}
		return ret;
	}
}
