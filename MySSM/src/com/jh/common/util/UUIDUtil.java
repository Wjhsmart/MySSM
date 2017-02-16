package com.jh.common.util;

import java.util.UUID;

/**
 * 获取UUID的工具类
 */
public class UUIDUtil {

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

}
