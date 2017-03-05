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
	<form action="<%=path %>/activiti/login" method="post">
		<label>邮箱：</label>
		<input type="email" placeholder="请输入邮箱" name="email" />
		<label>密码：</label>
		<input type="password" placeholder="请输入密码" name="pwd" />
		<input type="submit" value="登录" />
	</form>
</body>
</html>