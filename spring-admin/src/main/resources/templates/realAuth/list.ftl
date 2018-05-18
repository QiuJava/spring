<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统管理平台</title>
<#include "../common/header.ftl"/>
<script type="text/javascript" src="/js/plugins/jquery.form.js"></script>
<script type="text/javascript" src="/js/plugins/jquery.twbsPagination.min.js"></script>
<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js" ></script>

<script type="text/javascript">
	$(function() {
		$("#beginDate,#endDate").click(function(){
			WdatePicker();
		});
		
		$.extend($.fn.twbsPagination.defaults, {
			first : "首页",
			prev : "上一页",
			next : "下一页",
			last : "最后一页"
		});

		$('#pagination').twbsPagination({
			totalPages : ${page.totalPages}||1,
			startPage : ${page.currentPage},
			visiblePages : 5,
			onPageClick : function(event, page) {
				$("#currentPage").val(page);
				$("#searchForm").submit();
			}
		});
		
		$("#query").click(function(){
			$("#currentPage").val(1);
			$("#searchForm").submit();
		})
		
		$(".auditClass").click(function(){
			var json=$(this).data("json");
			var form=$("#editform");
			form.find("[name=id]").val(json.id);
			form.find("[name=username]").html(json.username);
			form.find("[name=realName]").html(json.realname);
			form.find("[name=idNumber]").html(json.idNumber);
			form.find("[name=sex]").html(json.sex);
			form.find("[name=birthDate]").html(json.birthDate);
			form.find("[name=address]").html(json.address);
			form.find("[name=image1]").attr("src",json.image1);
			form.find("[name=image2]").attr("src",json.image2);
			$("#myModal").modal("show");
		})
		
		$(".btn_audit").click(function(){
			var form=$("#editform");
			form.find("[name=state]").val($(this).val());
			$("#myModal").modal("hide");
			form.ajaxSubmit(function(data){
				if(data.success){
					$.messager.confirm("提示","审核成功!",function(){
						window.location.href="/realAuth/page.do";
					});
				}else{
					$.messager.alert("提示",data.msg);
				}
			});
			return false;
		});
	});
</script>
</head>
<body>
	<div class="container">
		<#include "../common/top.ftl"/>
		<div class="row">
			<div class="col-sm-3">
				<#assign currentMenu="realAuth">
				<#include "../common/menu.ftl"/>
			</div>
			<div class="col-sm-9">
				<div class="page-header">
					<h3>实名审核管理</h3>
				</div>
				<form id="searchForm" class="form-inline" method="post" action="/realAuth/page.do">
					<input type="hidden" id="currentPage" name="currentPage" value="1"/>
					<div class="form-group">
					    <label>状态</label>
					    <select class="form-control" name="state">
					    	<option value="-1">全部</option>
					    	<option value="0">申请中</option>
					    	<option value="1">审核通过</option>
					    	<option value="2">审核拒绝</option>
					    </select>
					    <script type="text/javascript">
					    	$("[name=state] option[value='${(qo.state)!''}']").attr("selected","selected");
					    </script>
					</div>
					<div class="form-group">
					    <label>申请时间</label>
					    <input class="form-control" type="text" name="beginDate" id="beginDate" value="${(qo.beginDate?date)!''}" />到
					    <input class="form-control" type="text" name="endDate" id="endDate" value="${(qo.endDate?date)!''}" />
					</div>
					<div class="form-group">
						<button id="query" class="btn btn-success"><i class="icon-search"></i> 查询</button>
					</div>
				</form>
				<div class="panel panel-default">
					<table class="table">
						<thead>
							<tr>
								<th>用户名</th>
								<th>真实姓名</th>
								<th>性别</th>
								<th>身份证号码</th>
								<th>身份证地址</th>
								<th>状态</th>
								<th>审核人</th>
							</tr>
						</thead>
						<tbody>
						<#list page.content as vo>
							<tr>
								<td>${vo.applier.username}</td>
								<td>${vo.realname}</td>
								<td>${vo.sexDisplay}</td>
								<td>${vo.idNumber}</td>
								<td>${vo.address}</td>
								<td>${vo.stateDisplay}</td>
								<td>${(vo.auditor.username)!""}</td>
								<td>
									<#if (vo.state == 0)>
									<a href="javascript:void(-1);" class="auditClass" data-json='${vo.jsonString}'>审核</a>
									</#if>
								</td>
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
		
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-body">
		      	<form class="form-horizontal" id="editform" method="post" action="/realAuth/audit.do">
					<fieldset>
						<div id="legend" class="">
							<legend>实名认证审核</legend>
						</div>
						<input type="hidden" name="id" value="" />
						<input type="hidden" name="state" value="" /> 
						<div class="row">
				        	<label class="col-sm-2 control-label" for="name">用户名</label>
				        	<div class="col-sm-4">
				        		<label class="form-control" name="username"></label>
				        	</div>
				        	<label class="col-sm-2 control-label" for="name">真实姓名</label>
				        	<div class="col-sm-4">
				        		<label class="form-control" name="realName"></label>
				        	</div>
						</div>
						<div class="row">
				        	<label class="col-sm-2 control-label" for="name">身份证号</label>
				        	<div class="col-sm-4">
				        		<label class="form-control" name="idNumber"></label>
				        	</div>
				        	<label class="col-sm-2 control-label" for="name">性别</label>
				        	<div class="col-sm-4">
				        		<label class="form-control" name="sex"></label>
				        	</div>
						</div>
						<div class="row">
				        	<label class="col-sm-2 control-label" for="name">生日</label>
				        	<div class="col-sm-4">
				        		<label class="form-control" name="birthDate"></label>
				        	</div>
				        	<label class="col-sm-2 control-label" for="name">身份证地址</label>
				        	<div class="col-sm-4">
				        		<label class="form-control" name="address"></label>
				        	</div>
						</div>
						<div class="row">
				        	<label class="col-sm-2 control-label" for="name">身份证正面</label>
				        	<div class="col-sm-4">
				        		<img src="" name="image1" style="width: 150px;"/>
				        	</div>
				        	<label class="col-sm-2 control-label" for="name">身份证背面</label>
				        	<div class="col-sm-4">
				        		<img src="" name="image2" style="width: 150px;"/>
				        	</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="name">审核备注</label>
				        	<div class="col-sm-6">
				        		<textarea name="remark" rows="4" cols="60"></textarea>
				        	</div>
						</div>
					</fieldset>
				</form>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
		        <button type="button" class="btn btn-success btn_audit" value="1">审核通过</button>
		        <button type="button" class="btn btn-warning btn_audit" value="2">审核拒绝</button>
		      </div>
		    </div>
		  </div>
		</div>
	</div>
</body>
</html>