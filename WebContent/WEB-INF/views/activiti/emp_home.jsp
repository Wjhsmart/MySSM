<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
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
	欢迎您，
	<shiro:principal />
	<shiro:hasRole name="customer">您的身份是：customer</shiro:hasRole>
	<a href="<%=path%>/activiti/logout">安全退出</a>
	<shiro:hasPermission name="customer:query">有customer:query权限</shiro:hasPermission>
	${sessionScope.user.email }
	<h3>申请请假：</h3>
	<form action="<%=path %>/activiti/leave" method="post">
		<label>请假理由:</label> <input type="text" name="des" /> <input
			type="submit" value="申请请假" />
	</form>
	<h3>申请购买物品：</h3>
	<form action="<%=path %>/activiti/apply" method="post">
		<label>物品名称:</label> 
		<input type="text" name="title" /> 
		<label>物品价格:</label> 
		<input type="text" name="money" /> 
		<label>购买理由:</label> 
		<input type="text" name="des" /> 
		<input type="submit" value="申请购买" />
	</form>
	<br />
	<h3>申请请假1：</h3>
	<form action="<%=path %>/activiti/leave1" method="post">
		<label>请假理由:</label> 
		<input type="text" name="des" />
		<label>请假天数:</label> 
		<input type="text" name="days" />		
		<input type="submit" value="申请请假" />
	</form>
	
	<br />
	<h3>旅游申请：</h3>
	<form action="<%=path %>/activiti/tourism" method="post">
		<label>申请理由:</label> 
		<input type="text" name="des" />
		<label>旅游天数:</label> 
		<input type="text" name="days" />		
		<input type="submit" value="提交申请" />
	</form>
	
	<br />
	<c:if test="${requestScope.taskName != null }">
		您有待办任务：
		<form action="<%=path %>/activiti/tourism" method="post">
		<label>申请理由:</label> 
		<input type="text" name="des" value="${requestScope.reason }" />
		<label>旅游天数:</label> 
		<input type="text" name="days" value="${requestScope.days }" />		
		<input type="submit" value="重新提交申请" />
	</form>
	</c:if>
	<br />
	<a href="<%=path %>/activiti/email">发送邮件</a>
</body>
</html>