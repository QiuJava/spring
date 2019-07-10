<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>蓝源Eloan-P2P平台</title> <#include "common/links-tpl.ftl" />

<link type="text/css" rel="stylesheet" href="/css/account.css" />
<script type="text/javascript" src="/js/plugins/jquery.twbsPagination.min.js"></script>
<script type="text/javascript" src="/js/plugins-override.js"></script>
<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	$(function(){
		$("#pagination").twbsPagination({
			totalPages:${pageResult.totalPages},
			visiblePages:5,
			startPage:${pageResult.page},
			first : "首页",
			prev : "上一页",
			next : "下一页",
			last : "最后一页", 
			onPageClick:function(e,page){
				$("#currentPage").val(page);
				$("#searchForm").submit();
			}
		});
		
		$(".beginTime,.endTime").click(function(){
			WdatePicker({
				dateFmt:"yyyy-MM-dd"
			});
		});
		
		$("#query").click(function(){
			$("#searchForm").submit();
		})
	});
</script>
</head>
<body>
	<#assign currentNav="personal">
	<!-- 网页顶部导航 -->
	<#include "common/head-tpl.ftl" />
	<!-- 网页导航 -->
	<#include "common/navbar-tpl.ftl" />

	<div class="container">
		<div class="row">
			<!--导航菜单-->
			<div class="col-sm-3">
			<#assign currentMenu="loginLog" /> 
			<#include "common/leftmenu-tpl.ftl" /></div>
			<!-- 功能页面 -->
			<div class="col-sm-9">
				<form action="/website/loginLog/pageQuery" name="searchForm" id="searchForm" class="form-inline" method="post">
					<input type="hidden" id="currentPage" name="currentPage" value="1" />
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
					<div class="form-group">
						<label>时间范围</label> <input type="text" class="form-control beginTime" name="beginTime"
							value='${(qo.beginTime?date)!""}' />
					</div>
					<div class="form-group">
						<label></label> <input type="text" class="form-control endTime" name="endTime"
							value='${(qo.endTime?date)!""}' />
					</div>
					<div class="form-group">
						<label>状态</label> <select class="form-control" name="loginStatus">
							<option value="-1">全部</option>
							<#list items as item>
							<option value="${item.itemValue}">${item.itemName}</option>
							</#list>
						</select>
						<script type="text/javascript">
							/* 回显登陆状态 */
						    $('[name=loginStatus] option[value=${(qo.loginStatus)!"-1"}]').attr("selected","selected");
						</script>
					</div>
					<div class="form-group">
						<button id="query" class="btn btn-success">
							<i class="icon-search"></i> 查询
						</button>
					</div>
				</form>

				<div class="panel panel-default" style="margin-top: 20px;">
					<div class="panel-heading">登录日志</div>
					<table class="table table-striped">
						<thead>
							<tr>
								<th>用户</th>
								<th>登录时间</th>
								<th>登录ip</th>
								<th>登录状态</th>
							</tr>
						</thead>
						<tbody>
							<#list pageResult.content as log>
							<tr>
								<td>${log.username}</td>
								<td>${log.loginTime}</td>
								<td>${log.ipAddress}</td>
								<td>${log.displayStatus}</td>
							</tr>
							</#list>
						</tbody>
					</table>
					<div style="text-align: center;">
						<ul id="pagination" class="pagination"></ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>