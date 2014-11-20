package com.hfbj.hyb.test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Js_ret {
	public static String member(String ret) {
		try {
			JSONTokener jsonParser = new JSONTokener(ret);
			JSONArray ar;
			ar = ((JSONArray) jsonParser.nextValue());
			JSONObject person;
			person = ar.getJSONObject(0);
			ret = "";
			ret += "贴吧id：" + person.getString("tieba_id");
			ret += "\r\n性别：" + person.getString("gender");
			ret += "\r\n电话：" + person.getString("phone");
			ret += "\r\nQQ：" + person.getString("QQ");
			ret += "\r\n邮箱：" + person.getString("mail");
			ret += "\r\n喜好：" + person.getString("pref");
			ret += "\r\n常用网名：" + person.getString("web_name");
			ret += "\r\n编号：" + person.getString("unique_name");
			ret += "\r\n审核人：" + person.getString("master");
			ret += "\r\n会员牌：" + person.getString("card") + "\r\n";
			JSONArray pa = ar.getJSONArray(1);
			int length = pa.length();
			for (int i = 0; i < length; i++) {
				JSONObject oj = pa.getJSONObject(i);
				ret += "缴费年份：" + oj.getString("year_time") + "\r\n登记时间："
						+ oj.getString("fee_time") + "\r\n登记人："
						+ oj.getString("oper") + "\r\n\r\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret = e.toString();
		}
		return ret;
	}

	public static Person person(String ret) {
		Person p = new Person();
		try {
			JSONTokener jsonParser = new JSONTokener(ret);
			JSONArray ar;
			ar = ((JSONArray) jsonParser.nextValue());
			JSONObject person;
			person = ar.getJSONObject(0);
			p.tieba_id = person.getString("tieba_id");
			p.gender = person.getString("gender");
			p.phone = person.getString("phone");
			p.QQ = person.getString("QQ");
			p.mail = person.getString("mail");
			p.pref = person.getString("pref");
			p.web_name = person.getString("web_name");
			p.unique_name = person.getString("unique_name");
			p.master = person.getString("master");
			p.card = person.getString("card");
			p.id = person.getInt("id");
			ret = "";
			ret += "贴吧id：" + person.getString("tieba_id");
			ret += "\r\n性别：" + person.getString("gender");
			ret += "\r\n电话：" + person.getString("phone");
			ret += "\r\nQQ：" + person.getString("QQ");
			ret += "\r\n邮箱：" + person.getString("mail");
			ret += "\r\n喜好：" + person.getString("pref");
			ret += "\r\n常用网名：" + person.getString("web_name");
			ret += "\r\n编号：" + person.getString("unique_name");
			ret += "\r\n审核人：" + person.getString("master");
			ret += "\r\n会员牌：" + person.getString("card") + "\r\n";
			p.text = ret;
			JSONArray pa = ar.getJSONArray(1);
			int length = pa.length();
			for (int i = 0; i < length; i++) {
				JSONObject oj = pa.getJSONObject(i);
				p.fee += "缴费年份：" + oj.getString("year_time") + "\r\n登记时间："
						+ oj.getString("fee_time") + "\r\n登记人："
						+ oj.getString("oper") + "\r\n\r\n";
				p.last_fee = Math.max(p.last_fee, oj.getInt("id"));
			}
			JSONArray ac = ar.getJSONArray(2);
			int l2 = ac.length();
			for (int i = 0; i < l2; i++) {
				JSONObject oj = ac.getJSONObject(i);
				p.activity += "活动名称：" + oj.getString("name") + "\r\n登记人："
						+ oj.getString("oper") + "\r\n\r\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
			p.tieba_id = e.toString();
			p.text = e.toString();
			p.activity = ret;
		}
		return p;
	}
}
