package com.jh.common.web;

import com.jh.common.Constants;

import javax.servlet.http.HttpSession;

/**
 * Session工具类
 */
public class SessionUtil {

    public static boolean isAdmin(HttpSession session) {
        return session.getAttribute(Constants.SESSION_ADMIN) != null;
    }

    public static boolean isUser(HttpSession session) {
        return session.getAttribute(Constants.SESSION_USER) != null;
    }
}
