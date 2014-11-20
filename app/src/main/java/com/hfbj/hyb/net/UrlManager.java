package com.hfbj.hyb.net;

/**
 * Url管理
 * @author LiYanZhao
 * @date 14-11-15 下午3:13
 */
public class UrlManager {

    public static final String SERVER_DMAINO = "http://app.hanbj.cn";
    public static final String VERSION = SERVER_DMAINO + "/hyb/bulletin/version";   //获取版本号
    public static final String PAY = SERVER_DMAINO +"";
    public static final String GENERAL = SERVER_DMAINO + "/hyb/welcome"; //通用接口地址


    /**
     * GENERAL
     */
    public static final String LOGIN = "index"; //登录
    public static final String INFO = "view_an";




    /**
     * 格式化使用通用接口的url
     * @param params
     * @return
     */
    public static String formatUrl(String... params){
        String url = GENERAL;
        for(String param : params){
            url += ("/"+param);
        }
        return url;
    }


}
