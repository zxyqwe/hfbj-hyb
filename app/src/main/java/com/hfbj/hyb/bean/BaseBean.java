package com.hfbj.hyb.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author LiYanZhao
 * @date 14-11-18 下午2:11
 */
public class BaseBean<T> implements Serializable {

    List<T> data;

}
