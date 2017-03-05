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
<title>员工登入成功</title>
</head>
<body>
	<h3>员工登录成功</h3>
	欢迎您，<shiro:principal />
	<shiro:hasRole name="customer">您的身份是：customer</shiro:hasRole>
	<a href="<%=path %>/activiti/logout">安全退出</a>
	<shiro:hasPermission name="customer:query">有customer:query权限</shiro:hasPermission>
	${sessionScope.user.email }
	 <form action="<%=path%>/activiti/leave" method="post">
	 	<label>请假理由:</label>
	 	<input type="text" name="des" />
	 	<input type="submit" value="申请请假" />
	 </form>
</body>
</html>