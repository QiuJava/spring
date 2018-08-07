<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统管理平台</title> <#include "../common/header.ftl"/>
<script type="text/javascript" src="/js/plugins/jquery.form.js"></script>
<script type="text/javascript"
	src="/js/plugins/jquery.twbsPagination.min.js"></script>
<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	$(function() {
		// 使用M97
		$(".beginDate,.endDate").click(function() {
			WdatePicker({
				dateFmt : "yyyy-MM-dd"
			});
		})
		// 设置当前页默认为一
		$("#query").click(function() {
			$("#currentPage").val(1);
		})
		var searchForm = $("#searchForm");
		var gridBody = $("#gridBody");
		searchForm.ajaxForm(function(data) {
			gridBody.hide();
			gridBody.html(data);
			gridBody.show(500);
		}).submit();
	});
</script>
</head>
<body>
	<div class="container">
		<#include "../common/top.ftl"/>
		<div class="row">
			<div class="col-sm-3">
				<!-- 自定义变量 -->
				<#assign currentMenu="ipLog">
				 <#include "../common/menu.ftl" />
			</div>
			<div class="col-sm-9">
				<div class="page-header">
					<h3>登录日志查询</h3>
				</div>
				<form id="searchForm" class="form-inline" method="post"
					action="/iplog/page">
					<input type="hidden" id="currentPage" name="currentPage" value="1" />
					<div class="form-group">
						<label>状态</label> <select class="form-control" name="state">
							<option value="-1">全部</option>
							<option value="0">登录失败</option>
							<option value="1">登录成功</option>
						</select>
					</div>
					<div class="form-group">
						<label>登陆时间</label> <input class="form-control beginDate"
							type="text" name="beginDate" value='' />到 <input
							class="form-control endDate" type="text" name="endDate" value='' />
					</div>
					<div class="form-group">
						<label>用户名</label> <input class="form-control" type="text"
							name="username" value='' />
					</div>
					<div class="form-group">
						<button id="query" class="btn btn-success">
							<i class="icon-search"></i> 查询
						</button>
					</div>
				</form>
				<div class="panel panel-default">
					<table class="table">
						<thead>
							<tr>
								<th>用户</th>
								<th>登录时间</th>
								<th>登录ip</th>
								<th>登录状态</th>
							</tr>
						</thead>
						<tbody id="gridBody">

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