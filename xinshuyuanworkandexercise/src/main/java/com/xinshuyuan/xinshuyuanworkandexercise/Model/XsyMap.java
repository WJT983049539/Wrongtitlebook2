package com.xinshuyuan.xinshuyuanworkandexercise.Model;

import java.util.HashMap;

/**
 * 得到一个空map集合
 * Created by Administrator on 2017/5/22.
 */

public class XsyMap<K, V> extends HashMap<K, V> {

    /**
     * 得到一个hashMap集合
     */
    private static final long serialVersionUID = 9028880490967787501L;
    public static XsyMap<String,String> getInterface(){
        return new XsyMap<String,String>();
    }
}
