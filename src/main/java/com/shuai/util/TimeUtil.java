package com.shuai.util;

import java.text.SimpleDateFormat;

/**
 * @Author: fengxin
 * @CreateTime: 2023-04-25  21:06
 * @Description: TODO
 */
public class TimeUtil {

    // 获取当前时间
    public static String getNowTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(System.currentTimeMillis());
    }
}
