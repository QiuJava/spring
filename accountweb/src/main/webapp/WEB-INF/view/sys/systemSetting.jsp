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
			<div class="pull-left active">系统设置</div>
		</div>
	</div>
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-6">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
						<table class="table table-bordered" style="width:100;max-width:100%">
							<thead>
								<tr>
									<td>IP地址</td>
									<td>错误次数</td>
									<td>解除时间</td>
									<td>操作</td>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${blockedIpList}" var="blocked">
									<tr>
										<td>${blocked.denyIp}</td>
										<td>${blocked.denyNum}</td>
										<td><fmt:formatDate value="${blocked.denyTime}" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
										<td>
										<sec:authorize access="hasAuthority('systemSetting:refreshCache')">  
											<button type="button" class=" btn btn-danger" onclick="deleteBlocked('${blocked.denyIp}','${blocked.denyDay}')">删除</button>
										</sec:authorize> 
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
				</div>
			</div>
		</div>
		<div class="col-lg-6">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="refreshCacheForm">
							<div class="form-group">
								<label class="col-sm-4 control-label">上次手动刷新时间:</label>
								<div class="col-sm-4" style="padding-top: 7px;">${params.parentRefreshDate}</div>
                             </div>
                             <div class="form-group">
								<div class="col-sm-12">
									<sec:authorize access="hasAuthority('systemSetting:refreshCache')">  
										<input type="button" class="ladda-button btn btn-success"  data-style="expand-left" value="刷新缓存">
									</sec:authorize> 
								</div>
                             </div>
                             <div class="form-group ">
                          	 	 <label class="col-sm-4 control-label">在线用户数:</label>
                              	<div class="col-sm-4">
                              		<a href="${ctx}/shiroUserAction/toOnlineUsers.do">${params.numberOnlineUsers}</a>
                                </div>
							</div>
                     </form>
				</div>
			</div>
		</div>
	</div>
	<!-- 填充内容结束 -->
	<title>
	<!-- Ladda style -->
    <link href="${ctx}/css/plugins/ladda/ladda-themeless.min.css" rel="stylesheet">
	
	<!-- Ladda -->
    <script src="${ctx}/js/plugins/ladda/spin.min.js"></script>
    <script src="${ctx}/js/plugins/ladda/ladda.min.js"></script>
    <script src="${ctx}/js/plugins/ladda/ladda.jquery.min.js"></script>
	<script type="text/javascript">
	
		$(document).ready(function() {
			var l = $( '.ladda-button' ).ladda();
	
	        l.click(function(){
	            // Start loading
	            l.ladda( 'start' );
	            // Timeout example
	            refreshCache();
	            // Do something in backend and then stop ladda
	            setTimeout(function(){
	                l.ladda('stop');
	            },1000)
	        });
		});
	
		function refreshCache(){
    	    $.post('${ctx}/sysAction/refreshCache.do', 
    				{'${_csrf.parameterName}':'${_csrf.token}' },
    				function(msg) {
    					if(!msg.status){
    						toastr.error(msg.msg,'错误');
    					}else{
    						var parentRefreshDate = msg.parentRefreshDate;
    						$("#refreshCacheForm > div:first > div:first").text(parentRefreshDate);
    						toastr.success(msg.msg,'提示');
    					}
    				});
			}
		
		function deleteBlocked(denyIp,denyDay){
    	    $.post('${ctx}/sysAction/deleteBlocked.do', 
    				{'denyIp': denyIp,'denyDay': denyDay,'${_csrf.parameterName}':'${_csrf.token}' },
    				function(msg) {
    					if(!msg.status){
    						toastr.error(msg.msg,'错误');
    					}else{
    						toastr.success(msg.msg,'提示');
    						location.reload();
    					}
    				});
			}
	</script>
	</title>
</body>