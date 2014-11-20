package com.hfbj.hyb.bean;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * 会员资料实体
 *
 * @author LiYanZhao
 * @date 14-11-21 上午11:30
 */
public class MemberBean extends BaseBean {
    //11-21 17:20:39.791  23084-23102/com.hfbj I/hfbj﹕ com.hfbj.hyb.net.GsonRequest.parseNetworkResponse():
    // [{"id":"270","tieba_id":"\u6cb3\u5357\u3010\u4e09\u513f\u3011","gender":"\u7537","phone":"18211093634","QQ":"894768914",
    // "mail":"liyanzhaoO@gmail.com","pref":"\u8bfb\u4e66","web_name":"\u674e","unique_name":"\u79bb\u5e9a\u5348",
    // "master":"\u767d\u6d77\u68e0\u5c11\u7237\u6d77\u513f","card":"1"},
    // [{"id":"225","unique_name":"\u79bb\u5e9a\u5348","fee_time":"2014-06-16 11:17:40","year_time":"2013","oper":"zxyqwe","unoper":null,"unfee_time":null},
    // {"id":"228","unique_name":"\u79bb\u5e9a\u5348","fee_time":"2014-06-16 11:17:40","year_time":"2014","oper":"zxyqwe","unoper":null,"unfee_time":null}],
    // []]  |  class java.lang.String  |

    String id;
    String tieba_id;
    String master;
    String mail;
    String gender;
    String pref;
    String unique_name;
    List<Payment> payments;



    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getTieba_id() {
        return tieba_id;
    }

    public void setTieba_id(String tieba_id) {
        this.tieba_id = tieba_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPref() {
        return pref;
    }

    public void setPref(String pref) {
        this.pref = pref;
    }

    public String getUnique_name() {
        return unique_name;
    }

    public void setUnique_name(String unique_name) {
        this.unique_name = unique_name;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getPayLine(){
        int year = 0;
        for(Payment ment : payments){
            int time = ment.getYear_time();
            if(time > year)
                year = time;
        }

        return String.valueOf(year);
    }


    public class Payment {
        String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUnique_name() {
            return unique_name;
        }

        public void setUnique_name(String unique_name) {
            this.unique_name = unique_name;
        }

        public String getFee_time() {
            return fee_time;
        }

        public void setFee_time(String fee_time) {
            this.fee_time = fee_time;
        }

        public int getYear_time() {
            return year_time;
        }

        public void setYear_time(int year_time) {
            this.year_time = year_time;
        }

        String unique_name;
        String fee_time;
        int year_time;
    }

}
