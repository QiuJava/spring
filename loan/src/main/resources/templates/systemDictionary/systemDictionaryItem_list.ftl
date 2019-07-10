<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- html <head>标签部分  -->
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>系统管理平台</title>
	<#include "../common/header.ftl"/>
	<script type="text/javascript" src="/js/plugins/jquery.form.js"></script>
	<script type="text/javascript" src="/js/plugins/jquery-validation/jquery.validate.js"></script>
	<script type="text/javascript" src="/js/plugins/jquery.twbsPagination.min.js"></script>
	
	<script type="text/javascript">
		$(function(){
			$.extend($.fn.twbsPagination.defaults, {
				first : "首页",
				prev : "上一页",
				next : "下一页",
				last : "最后一页"
			});

			$('#pagination').twbsPagination({
				totalPages : ${pageResult.totalPages},
				startPage : ${pageResult.page},
				visiblePages : 5,
				onPageClick : function(event, page) {
					$("#currentPage").val(page);
					$("#searchForm").submit();
				}
			});
			
			$("[id^=sysDict_]").click(function(){
				var pid=$(this).attr("id").substring(8);
				$("#systemDictionaryId").val(pid);
				$("#currentPage").val(1);
				$("#searchForm").submit();
			});
			
			var pid="sysDict_"+$("#systemDictionaryId").val();
			$("#systemDictionaryList li a[id="+pid+"]").parent().addClass("active");
			
			$("#addSystemDictionaryItemBtn").click(function(){
				var pid=$("#systemDictionaryId").val();
				if(pid != null && pid != ''){
					$("#itemSystemDictionaryId").val(pid);
					$("#systemDictionaryItemModal").modal("show");
				}else{
					$.messager.alert("请先选择一个字典分类!");
				}
			});
		
			$("#editForm").validate({
				rules : {
					itemName:"required",
					itemKey:"required",
					itemValue:"required",
					sequence:{
						required:true,
						number:true
					}
				},
				messages: {
					itemName:"不能为空",
					itemKey:"不能为空",
					itemValue:"不能为空",
					sequence:{
						required:"序号不能为空",
						number : "只能是数字",
					}
				}
			});
			
			$("#saveBtn").click(function(){
				if($("#editForm").valid()){
					$("#editForm").submit();
				}
			});
			
			$(".edit_Btn").click(function(){
				var json = $(this).data("json");
				$("#itemId").val(json.id);
				$("#itemName").val(json.itemName);
				$("#itemKey").val(json.itemKey);
				$("#itemValue").val(json.itemValue);
				$("#itemSequence").val(json.sequence);
				$("#itemSystemDictionaryId").val(json.systemDictionaryId);
				$("#systemDictionaryItemModal").modal("show");
			});
			// 删除一条字典明细
			$(".deleteClass").click(function(){
				var json = $(this).data("json");
				$.messager.confirm("确定","确定删除吗",function(){
					window.location.href="/manage/systemDictionaryItem/delete?id="+json.id+"&systemDictionary.id="+json.systemDictionaryId;
				})
			})
		});
		</script>
</head>
<body>
	<div class="container">
		<#include "../common/top.ftl"/>
		<div class="row">
			<div class="col-sm-3">
				<#assign currentMenu="systemDictionaryItem" />
				<#include "../common/menu.ftl" />
			</div>
			<div class="col-sm-9">
				<div class="page-header">
					<h3>数据字典明细管理</h3>
				</div>
				<div class="col-sm-12">
					<!-- 提交分页的表单 -->
					<form id="searchForm" class="form-inline" method="post" action="/manage/systemDictionaryItem">
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
						<input type="hidden" id="currentPage" name="currentPage" value="1"/>
						<input type="hidden" id="systemDictionaryId" name="systemDictionaryId" value="${(qo.systemDictionaryId)!''}" />
						<div class="form-group">
						    <label>条目关键字</label>
						    <input class="form-control" type="text" name="keyword" value="${(qo.keyword)!''}">
						</div>
						<div class="form-group">
							<button id="query" class="btn btn-success"><i class="icon-search"></i> 查询</button>
							<a href="javascript:void(-1);" class="btn btn-success" id="addSystemDictionaryItemBtn">添加数据字典条目</a>
						</div>
					</form>
					<div class="row"  style="margin-top:20px;">
						<div class="col-sm-3">
							<ul id="menu" class="list-group">
								<li class="list-group-item">
									<a href="#" data-toggle="collapse" data-target="#systemDictionaryList"><span>数据字典分组</span></a>
									<ul class="in" id="systemDictionaryList">
										<#list systemDictionaryList as sysDict>
										   <li><a id="sysDict_${sysDict.id}" data-dataid="${sysDict.id}" href="javascript:;"><span>${sysDict.dictName}</span></a></li>
										</#list>
									</ul>
								</li>
							</ul>
						</div>
						<div class="col-sm-9">
							<table class="table">
								<thead>
									<tr>
										<th>名称</th>
										<th>条目键</th>
										<th>条目值</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
								<#list pageResult.content as sysDictItem>
									<tr>
										<td>${sysDictItem.itemName}</td>
										<td>${sysDictItem.itemKey}</td>
										<td>${sysDictItem.itemValue}</td>
										<td>
											<a href="javascript:void(-1);" class="edit_Btn" data-json='${sysDictItem.jsonString}'>修改</a> &nbsp; 
											<a href="javascript:void(-1);" class="deleteClass" data-json='${sysDictItem.jsonString}'>删除</a>
										</td>
									</tr>
								</#list>
								</tbody>
							</table>
							
							<div style="text-align: center;">
								<ul id="pagination" class="pagination"></ul>
							</div>
							
							
							<div id="systemDictionaryItemModal" class="modal" tabindex="-1" role="dialog">
							  <div class="modal-dialog" role="document">
							    <div class="modal-content">
							      <div class="modal-header">
							        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
							        <h4 class="modal-title">编辑/增加</h4>
							      </div>
							      <div class="modal-body">
							       	  <form id="editForm" class="form-horizontal" method="post" action="/manage/systemDictionaryItem/save" style="margin: -3px 118px">
							       	  		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
										    <input type="hidden" id="itemId"  name="id" value="" />
									    	<input id="itemSystemDictionaryId" type="hidden" name="systemDictionary.id" value="" />
										   	<div class="form-group">
											    <label class="col-sm-3 control-label">名称</label>
											    <div class="col-sm-6">
											    	<input type="text" id="itemName" class="form-control" name="itemName" >
											    </div>
											</div>
											<div class="form-group">
											    <label class="col-sm-3 control-label">条目键</label>
											    <div class="col-sm-6">
											    	<input type="text" id="itemKey"  class="form-control" name="itemKey">
											    </div>
											</div>
											<div class="form-group">
											    <label class="col-sm-3 control-label">条目值</label>
											    <div class="col-sm-6">
											    	<input type="text" id="itemValue"  class="form-control" name="itemValue">
											    </div>
											</div>
											<div class="form-group">
											    <label class="col-sm-3 control-label">顺序</label>
											    <div class="col-sm-6">
											    	<input type="text" id="itemSequence"  class="form-control" name="sequence">
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
			</div>
		</div>
	</div>
</body>
</html>