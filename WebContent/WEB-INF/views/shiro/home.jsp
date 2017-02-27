<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
    <%
    	String path = request.getContextPath();
    %>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>用户登入成功</title>
</head>
<body>
	<h3>用户登录成功</h3>
	欢迎您，<shiro:principal />
	<shiro:hasRole name="admin">您的身份是：admin</shiro:hasRole>
	<a href="<%=path %>/user/logout">安全退出</a>
	<shiro:hasPermission name="customer:add">有add权限</shiro:hasPermission>
</body>
</html>