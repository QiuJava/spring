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
			$("#editForm input[name=title]").val(json.title);
			$("#editForm input[name=sn]").val(json.sn);
			$("#introId").html(json.intro);
			$("#systemDictionaryModal").modal("show");
		});
		
		$("#editForm").validate({
			rules : {
				title:"required",
				sn:"required"
			},
			messages: {
				title:"名称不能为空",
				sn:"编码不能为空"
			}
		});
		
		$("#saveBtn").click(function(){
			$("#editForm").submit();
		});
		
		$("#addSystemDictionaryBtn").click(function(){
			$("#editForm").resetForm();
			$("#systemDictionaryModal").modal("show");
		});
		
		$(".deleteClass").click(function(){
			var id = $(this).data("dataid");
			$.messager.confirm("确认","你确定删除这条数据吗?",function(){
				window.location.href='/systemDictionary/delete?id='+id; 
			});
		});
	});
</script>
</head>

<body>
	<div class="container">
		<#include "../common/top.ftl"/>
		<div class="row">
			<div class="col-sm-3"><#assign currentMenu="systemDictionary">
				<#include "../common/menu.ftl" /></div>
			<div class="col-sm-9">
				<div class="page-header">
					<h3>数据字典管理</h3>
				</div>
				<div class="row">
					<!-- 提交分页的表单 -->
					<form id="searchForm" class="form-inline" method="post"
						action="/systemDictionary/page">
						<input type="hidden" id="currentPage" name="currentPage" value="1" />
						<div class="form-group">
							<label>关键字</label> <input class="form-control" type="text"
								name="keyword" value="${(qo.keyword)!''}">
						</div>
						<div class="form-group">
							<button id="query" class="btn btn-success">
								<i class="icon-search"></i> 查询
							</button>
							<a href="javascript:void(-1);" class="btn btn-success"
								id="addSystemDictionaryBtn">添加数据字典</a>
						</div>
					</form>
				</div>
				<div class="row">
					<table class="table">
						<thead>
							<tr>
								<th>名称</th>
								<th>编码</th>
								<th>简介</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<#list page.content as data>
							<tr>
								<td>${data.title}</td>
								<td>${data.sn}</td>
								<td width="150px;">${data.intro}</td>
								<td><a href="javascript:void(-1);" class="edit_Btn"
									data-json='${data.jsonString}'>修改</a> &nbsp; <a
									href="javascript:void(-1);" class="deleteClass"
									data-dataid="${data.id}">删除</a></td>
							</tr>
							</#list>
						</tbody>
					</table>
					<div style="text-align: center;">
						<ul id="pagination" class="pagination"></ul>
					</div>
				</div>
				<div id="systemDictionaryModal" class="modal" tabindex="-1" role="dialog">
				  <div class="modal-dialog" role="document">
				    <div class="modal-content">
				      <div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				        <h4 class="modal-title">编辑/增加</h4>
				      </div>
				      <div class="modal-body">
			       		<form id="editForm" class="form-horizontal" method="post" action="/systemDictionary/update" style="margin: -3px 118px">
						    <input id="systemDictionaryId" type="hidden" name="id" value="" />
						   	<div class="form-group">
							    <label class="col-sm-2 control-label">名称</label>
							    <div class="col-sm-6">
							    	<input type="text" class="form-control" name="title" placeholder="字典分类名称">
							    </div>
							</div>
							<div class="form-group">
							    <label class="col-sm-2 control-label">编码</label>
							    <div class="col-sm-6">
							    	<input type="text" class="form-control" name="sn" placeholder="字典分类编码">
							    </div>
							</div>
							<div class="form-group">
							    <label class="col-sm-2 control-label">介绍</label>
							    <div class="col-sm-6">
							    	<textarea id="introId" rows="4" cols="24"  style="margin-top: 5px;" name="intro"></textarea>
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