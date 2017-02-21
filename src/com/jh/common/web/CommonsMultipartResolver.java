package com.jh.common.web;

import javax.servlet.http.HttpServletRequest;

public class CommonsMultipartResolver extends org.springframework.web.multipart.commons.CommonsMultipartResolver {

	@Override
	public boolean isMultipart(HttpServletRequest request) {
		String uri = request.getRequestURI();
		if (uri.indexOf("/ueditor/core") > 0) {
			System.out.println("不使用SpringMVC的过滤器");
			return false;
		} else {
			System.out.println("使用SpringMVC的文件上传");
			return super.isMultipart(request);
		}
	}

}
