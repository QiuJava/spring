<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav">
			<div class="pull-left">当前位置</div>
			<em class=""></em>
			<div class="pull-left">系统管理</div>
			<em class=""></em>
			<div class="pull-left active">当前在线用户</div>
		</div>
	</div>
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
						<table class="table table-bordered" style="width:100;max-width:100%">
							<thead>
								<tr>
									<td>*</td>
									<td>登录名</td>
									<td>用户名</td>
									<td>登录IP</td>
									<td>最近活动时间</td>
									<td>会话ID</td>
									<!-- <td>最后访问的页面</td> -->
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${onlineUserInfoList}" var="userInfo" varStatus="status">
									<tr>
										<td>${status.count}</td>
										<td>${userInfo.username}</td>
										<td>${userInfo.realName}</td>
										<td>${userInfo.loginIp}</td>
										<td><fmt:formatDate value="${userInfo.lastRequest}" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
										<td>${userInfo.sessionId}</td>
										<%-- <td>${userInfo.lastRequestUrl}</td> --%>
									</tr>
								</c:forEach>
							</tbody>
						</table>
				</div>
			</div>
		</div>
	</div>
	<!-- 填充内容结束 -->
	<title>
	</title>
</body>