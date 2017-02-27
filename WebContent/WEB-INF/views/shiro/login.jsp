<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%
    	String path = request.getContextPath();
    %>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>登录页面</title>
</head>
<body>
	<div style="color:red;font-size:16px;">${requestScope.error }</div>
	<form action="<%=path %>/user/login" method="post">
		<label>用户名：</label>
		<input type="text" placeholder="请输入用户名" name="username" />
		<label>密码：</label>
		<input type="password" placeholder="请输入密码" name="password" />
		<input type="submit" value="登录" />
	</form>
</body>
</html>