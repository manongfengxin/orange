package com.shuai.util;

/**
 * 字符串工具类
 * @author 码农封心
 *
 */
public class StringUtil {
    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        }else {
            return false;
        }

    }

    /**
     * 判断字符串是否不是空
     */
    public static boolean isNotEmpty(String str) {
        if (str != null && !"".equals(str.trim())) {
            return true;
        }else {
            return false;
        }

    }
}
