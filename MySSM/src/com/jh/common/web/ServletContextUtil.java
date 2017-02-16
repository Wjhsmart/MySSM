package com.jh.common.web;

import org.springframework.web.context.ContextLoader;

import javax.servlet.ServletContext;

/**
 * Servlet工具类
 */
public class ServletContextUtil {

    public static ServletContext getServletContext() {
        return ContextLoader.getCurrentWebApplicationContext().getServletContext();
    }

    public static String getContextPath() {
        return ContextLoader.getCurrentWebApplicationContext().getServletContext().getContextPath();
    }
}
