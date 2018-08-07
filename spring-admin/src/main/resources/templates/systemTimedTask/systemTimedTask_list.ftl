<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统管理平台</title> 
<#include "../common/header.ftl"/>
<script type="text/javascript" src="/js/plugins/jquery.form.js"></script>
<script type="text/javascript"
	src="/js/plugins/jquery-validation/jquery.validate.js"></script>
<script type="text/javascript"
	src="/js/plugins/jquery.twbsPagination.min.js"></script>

<script type="text/javascript">
	$(function(){
		$("#pagination").twbsPagination({
			totalPages:${page.totalPages}||1,
			visiblePages:5,
			startPage:${page.currentPage},
			first : "首页",
			prev : "上一页",
			next : "下一页",
			last : "最后一页",
			onPageClick:function(e,page){
				$("#currentPage").val(page);
				$("#searchForm").submit();
			}
		});
		
		$(".edit_Btn").click(function(){
			var json = $(this).data("json");
			$("#editForm input[name=id]").val(json.id);
			$("#editForm input[name=jobName]").val(json.jobName);
			$("#editForm input[name=groupName]").val(json.groupName);
			$("#editForm input[name=cronExpression]").val(json.cronExpression);
			$("#description").html(json.description);
			$("#systemTimedTaskModel").modal("show");
		});
		
		$("#editForm").validate({
			rules : {
				jobName:"required",
				groupName:"required",
				cronExpression:"required"
			},
			messages: {
				jobName:"名称不能为空",
				groupName:"组不能为空",
				cronExpression:"表达式不能为空"
			}
		});
		
		$("#saveBtn").click(function(){
			$("#editForm").submit();
		});
		
		$("#addSystemTimedTaskBtn").click(function(){
			$("#editForm").resetForm();
			$("#systemTimedTaskModel").modal("show");
		});
		
		$(".deleteClass").click(function(){
			var id = $(this).data("dataid");
			$.messager.confirm("确认","你确定删除这个定时任务吗?",function(){
				window.location.href='/systemTimedTask/delete?id='+id; 
			});
		});
		$(".pauseClass").click(function(){
			var id = $(this).data("dataid");
			$.messager.confirm("确认","你确定暂停这个定时任务吗?",function(){
				window.location.href='/systemTimedTask/pause?id='+id; 
			});
		});
		$(".resumeClass").click(function(){
			var id = $(this).data("dataid");
			$.messager.confirm("确认","你确定重启这个定时任务吗?",function(){
				window.location.href='/systemTimedTask/resume?id='+id; 
			});
		});
	});
</script>
</head>

<body>
	<div class="container">
		<#include "../common/top.ftl"/>
		<div class="row">
			<div class="col-sm-3"><#assign currentMenu="systemTimedTask_list">
				<#include "../common/menu.ftl" /></div>
			<div class="col-sm-9">
				<div class="page-header">
					<h3>系统定时任务管理</h3>
				</div>
				<div class="row">
					<!-- 提交分页的表单 -->
					<form id="searchForm" class="form-inline" method="post"
						action="/systemTimedTask/list">
						<input type="hidden" id="currentPage" name="currentPage" value="1" />
						<div class="form-group">
							<label>组名</label> <input class="form-control" type="text"
								name="groupName" value="${(qo.groupName)!''}">
						</div>
						<div class="form-group">
							<button id="query" class="btn btn-success">
								<i class="icon-search"></i> 查询
							</button>
							<a href="javascript:void(-1);" class="btn btn-success"
								id="addSystemTimedTaskBtn">添加定时任务</a>
						</div>
					</form>
				</div>
				<div class="row">
					<table class="table">
						<thead>
							<tr>
								<th>定时任务名称</th>
								<th>定时任务组名</th>
								<th>任务计划表达式</th>
								<th>简介</th>
								<th>状态</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<#list page.content as data>
							<tr>
								<td>${data.jobName}</td>
								<td>${data.groupName}</td>
								<td>${data.cronExpression}</td>
								<td>${(data.description)!''}</td>
								<td>${data.statusDisplay}</td>
								<td><a href="javascript:void(-1);" class="edit_Btn"
									data-json='${data.jsonString}'>修改</a>&nbsp; <a
									href="javascript:void(-1);" class="deleteClass"
									data-dataid='${data.id}'>删除</a>&nbsp; <a
									href="javascript:void(-1);" class="pauseClass"
									data-dataid='${data.id}'>暂停</a>&nbsp; <a
									href="javascript:void(-1);" class="resumeClass"
									data-dataid='${data.id}'>重新开启</a></td>
							</tr>
							</#list>
						</tbody>
					</table>
					<div style="text-align: center;">
						<ul id="pagination" class="pagination"></ul>
					</div>
				</div>
				<div id="systemTimedTaskModel" class="modal" tabindex="-1" role="dialog">
				  <div class="modal-dialog" role="document">
				    <div class="modal-content">
				      <div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				        <h4 class="modal-title">编辑/保存</h4>
				      </div>
				      <div class="modal-body">
			       		<form id="editForm" class="form-horizontal" method="post" action="/systemTimedTask/update" style="margin: -3px 118px">
						    <input id="systemTimedTaskId" type="hidden" name="id" value="" />
						   	<div class="form-group">
							    <label class="col-sm-2 control-label">名称</label>
							    <div class="col-sm-6">
							    	<input type="text" class="form-control" name="jobName" placeholder="定时任务名称">
							    </div>
							</div>
							<div class="form-group">
							    <label class="col-sm-2 control-label">组名</label>
							    <div class="col-sm-6">
							    	<input type="text" class="form-control" name="groupName" placeholder="定时任务组名">
							    </div>
							</div>
							<div class="form-group">
							    <label class="col-sm-2 control-label">表达式</label>
							    <div class="col-sm-6">
							    	<input type="text" class="form-control" name="cronExpression" placeholder="任务计划表达式">
							    </div>
							</div>
							<div class="form-group">
							    <label class="col-sm-2 control-label">简介</label>
							    <div class="col-sm-6">
							    	<textarea id="description" rows="4" cols="24"  style="margin-top: 5px;" name="description"></textarea>
							    </div>
							</div>
					   </form>
					  </div>
				      <div class="modal-footer">
				      	<a href="javascript:void(0);" class="btn btn-success" id="saveBtn" aria-hidden="true">保存</a>
					    <a href="javascript:void(0);" class="btn" data-dismiss="modal" aria-hidden="true">关闭</a>
				      </div>
				    </div>
				  </div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>