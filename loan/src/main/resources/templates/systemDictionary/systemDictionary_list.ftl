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
		
		$(".edit_Btn").click(function(){
			var json = $(this).data("json");
			$("#editForm input[name=id]").val(json.id);
			$("#editForm input[name=dictName]").val(json.dictName);
			$("#editForm input[name=dictKey]").val(json.dictKey);
			$("#editForm input[name=dictValue]").val(json.dictValue);
			$("#editForm input[name=sequence]").val(json.sequence);
			$("#systemDictionaryModal").modal("show");
		});
		
		$("#editForm").validate({
			rules : {
				dictKey:"required",
				dictValue:"required",
				sequence:"required",
				dictName:"required"
			},
			messages: {
				dictName:"不能为空",
				dictKey:"不能为空",
				dictValue:"不能为空",
				sequence:"不能为空",
			}
		});
		
		$("#saveBtn").click(function(){
			$("#editForm").submit();
		});
		
		$("#addSystemDictionaryBtn").click(function(){
			$("#editForm").resetForm();
			$("#editForm input[name=id]").val("");
			$("#systemDictionaryModal").modal("show");
		});
		
		$(".deleteClass").click(function(){
			var id = $(this).data("dataid");
			$.messager.confirm("确认","你确定删除这条数据吗?",function(){
				window.location.href='/manage/systemDictionary/delete?id='+id; 
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
					<form id="searchForm" class="form-inline" method="get"
						action="/manage/systemDictionary">
						<input type="hidden" id="currentPage" name="currentPage" value="1" />
						<div class="form-group">
							<label>字典名称</label> <input class="form-control" type="text"
								name="dictName" value="${(qo.dictName)!''}">
						</div>
						<div class="form-group">
							<label>字典键</label> <input class="form-control" type="text"
								name="dictKey" value="${(qo.dictKey)!''}">
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
								<th>字典键</th>
								<th>字典值</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<#list pageResult.content as sysDict>
							<tr>
								<td>${sysDict.dictName}</td>
								<td>${sysDict.dictKey}</td>
								<td width="150px;">${sysDict.dictValue}</td>
								<td><a href="javascript:void(-1);" class="edit_Btn"
									data-json='${sysDict.jsonString}'>修改</a> &nbsp; <a
									href="javascript:void(-1);" class="deleteClass"
									data-dataid="${sysDict.id}">删除</a></td>
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
			       		<form id="editForm" class="form-horizontal" method="post" action="/manage/systemDictionary/save" style="margin: -3px 118px">
			       			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
						    <input id="systemDictionaryId" type="hidden" name="id" value="" />
						   	<div class="form-group">
							    <label class="col-sm-2 control-label">字典名称</label>
							    <div class="col-sm-6">
							    	<input type="text" class="form-control" name="dictName" >
							    </div>
							</div>
							<div class="form-group">
							    <label class="col-sm-2 control-label">字典键</label>
							    <div class="col-sm-6">
							    	<input type="text" class="form-control" name="dictKey" >
							    </div>
							</div>
							<div class="form-group">
							    <label class="col-sm-2 control-label">字典值</label>
							    <div class="col-sm-6">
							    	<input type="text" class="form-control" name="dictValue" >
							    </div>
							</div>
							<div class="form-group">
							    <label class="col-sm-2 control-label">顺序</label>
							    <div class="col-sm-6">
							    	<input type="text" class="form-control" name="sequence" >
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