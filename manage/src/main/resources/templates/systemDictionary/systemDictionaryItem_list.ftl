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
				totalPages : ${pageResult.totalPages}||1,
				startPage : ${pageResult.currentPage},
				visiblePages : 5,
				onPageClick : function(event, page) {
					$("#currentPage").val(page);
					$("#searchForm").submit();
				}
			});
			
			$("[id^=pg_]").click(function(){
				var pid=$(this).attr("id").substring(3);
				$("#systemDictionaryId").val(pid);
				$("#currentPage").val(1);
				$("#searchForm").submit();
			});
			
			var pid="pg_"+$("#systemDictionaryId").val();
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
					title:"required",
					sequence:{
						required:true,
						number:true
					}
				},
				messages: {
					title:"名称不能为空",
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
				$("#itemTitle").val(json.title);
				$("#itemSequence").val(json.sequence);
				$("#itemIntro").html(json.intro);
				$("#itemSystemDictionaryId").val(json.systemDictionaryId);
				$("#systemDictionaryItemModal").modal("show");
			});
			// 删除一条字典明细
			$(".deleteClass").click(function(){
				var idAndSysDictIdJsonStr = $(this).data("id");
				$.messager.confirm("确定","确定删除吗",function(){
					window.location.href="/systemDictionaryItem/delete?itemId="+idAndSysDictIdJsonStr.id+"&sysDictId="+idAndSysDictIdJsonStr.sysDictId;
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
					<form id="searchForm" class="form-inline" method="post" action="/systemDictionaryItem/pageQuery">
						<input type="hidden" id="currentPage" name="currentPage" value="1"/>
						<input type="hidden" id="systemDictionaryId" name="systemDictionaryId" value="${(qo.systemDictionaryId)!''}" />
						<div class="form-group">
						    <label>关键字</label>
						    <input class="form-control" type="text" name="title" value="${(qo.title)!''}">
						</div>
						<div class="form-group">
							<button id="query" class="btn btn-success"><i class="icon-search"></i> 查询</button>
							<a href="javascript:void(-1);" class="btn btn-success" id="addSystemDictionaryItemBtn">添加数据字典明细</a>
						</div>
					</form>
					<div class="row"  style="margin-top:20px;">
						<div class="col-sm-3">
							<ul id="menu" class="list-group">
								<li class="list-group-item">
									<a href="#" data-toggle="collapse" data-target="#systemDictionaryList"><span>数据字典分组</span></a>
									<ul class="in" id="systemDictionaryList">
										<#list systemDictionaryList as sysDict>
										   <li><a id="pg_${sysDict.id}" data-dataid="${sysDict.id}" href="javascript:;"><span>${sysDict.title}</span></a></li>
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
										<th>序列</th>
										<th>说明</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
								<#list pageResult.content as sysDictItem>
									<tr>
										<td>${sysDictItem.title}</td>
										<td>${sysDictItem.sequence}</td>
										<td width="150px;">${sysDictItem.intro}</td>
										<td>
											<a href="javascript:void(-1);" class="edit_Btn" data-json='${sysDictItem.jsonString}'>修改</a> &nbsp; 
											<a href="javascript:void(-1);" class="deleteClass" data-id='${sysDictItem.idAndSysDictIdJsonStr}'>删除</a>
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
							       	  <form id="editForm" class="form-horizontal" method="post" action="/systemDictionaryItem/saveOrupdate" style="margin: -3px 118px">
										    <input type="hidden" id="itemId"  name="id" value="" />
									    	<input id="itemSystemDictionaryId" type="hidden" name="systemDictionary.id" value="" />
										   	<div class="form-group">
											    <label class="col-sm-3 control-label">名称</label>
											    <div class="col-sm-6">
											    	<input type="text" id="itemTitle" class="form-control" name="title" placeholder="字典值名称">
											    </div>
											</div>
											<div class="form-group">
											    <label class="col-sm-3 control-label">顺序</label>
											    <div class="col-sm-6">
											    	<input type="text" id="itemSequence"  class="form-control" name="sequence" placeholder="字典值显示顺序">
											    </div>
											</div>
											<div class="form-group">
											    <label class="col-sm-3 control-label">介绍</label>
											    <div class="col-sm-6">
											    	<textarea id="itemIntro" rows="4" cols="24"  style="margin-top: 5px;" name="intro"></textarea>
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