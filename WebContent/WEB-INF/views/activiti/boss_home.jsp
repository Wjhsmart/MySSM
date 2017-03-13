<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
    <%
    	String path = request.getContextPath();
    %>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>老板登入成功</title>
</head>
<body>
	<h3>老板登录成功</h3>
	欢迎您，<shiro:principal />
	<a href="<%=path %>/activiti/logout">安全退出</a>
	<br />
	<shiro:hasRole name="admin">您的身份是：admin</shiro:hasRole>
	<br />
	<shiro:hasPermission name="customer:query">有customer:query权限</shiro:hasPermission>
	<br />
	${sessionScope.user.email }:
	<br />
	请假流程：
	<a href="<%=path %>/activiti/deploy?processImgName=leave_process&processName=请假流程名称">部署流程</a>
	<a href="<%=path %>/activiti/del_process">删除流程</a>
	<br />
	物品申购流程：
	<a href="<%=path %>/activiti/deploy?processImgName=goods_apply&processName=物品的申购流程名称">部署流程</a>
	<a href="<%=path %>/activiti/del_process">删除流程</a>
	<br />
	请假流程1：
	<a href="<%=path %>/activiti/deploy?processImgName=leave_proc&processName=请假流程的名称1">部署流程</a>
	<a href="<%=path %>/activiti/del_process">删除流程</a>
	<br />
	旅游申请流程：
	<a href="<%=path %>/activiti/deploy?processImgName=tourism_process&processName=旅游申请流程">部署流程</a>
	<a href="<%=path %>/activiti/del_process">删除流程</a>
	<br />
	查看当前正在运行的流程图：
	<a href="<%=path %>/activiti/view_proc">查看流程图</a>
	<a href="<%=path %>/activiti/view_proc_now">查看流程图</a>
	
	<br />
	<c:if test="${requestScope.taskName != null }">
		您有待办任务：
		<br />
		任务名称：${requestScope.taskName }
		<br />
		任务信息：${requestScope.reason }
		<br />
		申请任务的人：${requestScope.email }
		<br />
		申请天数：${requestScope.days }
		<br />
		物品名称：${requestScope.title }
		<br />
		物品价格：${requestScope.money }
		<br />
		<a href="<%=path %>/activiti/approval/agree">同意旅游申请</a>
		<a href="<%=path %>/activiti/approval/refuse">拒绝旅游申请</a>
		<br />
		<a href="<%=path %>/activiti/compelete">同意申请</a>
		<a href="<%=path %>/activiti/del_task">删除该申请</a>
	</c:if>
	
</body>
</html>