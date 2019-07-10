<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统管理平台</title>
<#include "../common/header.ftl"/>
<link rel="stylesheet" href="/css/bank.css">
<script type="text/javascript" src="/js/bank.js"></script>
<script type="text/javascript" src="/js/plugins/jquery.form.js"></script>
<script type="text/javascript" src="/js/plugins/jquery.twbsPagination.min.js"></script>
<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js" ></script>

<script type="text/javascript" event="">
	$(function() {
		$("#beginTime,#endTime").click(function(){
			WdatePicker();
		});
		
		$('#pagination').twbsPagination({
			totalPages : ${pageResult.totalPages},
			startPage : ${pageResult.page},
			visiblePages : 5,
			first : "首页",
			prev : "上一页",
			next : "下一页",
			last : "最后一页",
			onPageClick : function(event, page) {
				$("#currentPage").val(page);
				$("#searchForm").submit();
			}
		});
		
		$("#query").click(function(){
			$("#currentPage").val(1);
			//$("#searchForm").submit();
		})
		
		$(".btn_audit").click(function(){
			var form=$("#editform");
			form.find("[name=auditStatus]").val($(this).val());
			$("#myModal").modal("hide");
			form.ajaxSubmit(function(data){
				if(data.success){
					$.messager.confirm("提示","审核成功!",function(){
						window.location.href="/manage/withdraw";
					});
				}else{
					$.messager.alert("提示",data.msg);
				}
			});
			return false;
		});
		
		$(".auditClass").click(function(){
			var json=$(this).data("json");
			var form=$("#editform");
			form.find("[name=id]").val(json.id);
			form.find("[name=username]").text(json.username);
			form.find("[name=realName]").text(json.realName);
			form.find("[name=cardNumber]").text(json.cardNumber);
			form.find("[name=bankForkName]").text(json.bankForkName);
			form.find("[name=amount]").text(json.amount);
			form.find("[name=bankName]").text(SITE_BANK_TYPE_NAME_MAP[json.bankName]);
			$("#myModal").modal("show");
		});
	});
</script>
</head>
<body>
	<div class="container">
		<#include "../common/top.ftl"/>
		<div class="row">
			<div class="col-sm-3">
				<#assign currentMenu="withdraw" />
				<#include "../common/menu.ftl" />
			</div>
			<div class="col-sm-9">
				<div class="page-header">
					<h3>提现审核管理</h3>
				</div>
				<form id="searchForm" class="form-inline" method="post" action="/manage/withdraw">
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
					<input type="hidden" id="currentPage" name="currentPage" value="1"/>
					<div class="form-group">
					    <label>状态</label>
					    <select class="form-control" name="auditStatus">
					    	<option value="-1">全部</option>
					    	<option value="0">申请中</option>
					    	<option value="1">审核通过</option>
					    	<option value="2">审核拒绝</option>
					    </select>
					    <script type="text/javascript">
					    	$("[name=auditStatus] option[value='${(qo.auditStatus)!''}']").attr("selected","selected");
					    </script>
					</div>
					<div class="form-group">
					    <label>申请时间</label>
					    <input class="form-control" type="text" name="beginTime" id="beginTime" value="${(qo.beginTime?date)!''}" />到
					    <input class="form-control" type="text" name="endTime" id="endTime" value="${(qo.endTime?date)!''}" />
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
								<th>银行</th>
								<th>账号</th>
								<th>开户名</th>
								<th>提现金额</th>
								<th>状态</th>
								<th>审核人</th>
							</tr>
						</thead>
						<tbody>
						<#list pageResult.content as wthdraw>
							<tr>
								<td>${wthdraw.submitter.username}</td>
								<td><div class="bank bank_${wthdraw.bankName}" /></td>
								<td>${wthdraw.cardNumber}</td>
								<td>${wthdraw.realName}</td>
								<td>${wthdraw.amount}</td>
								<td>${wthdraw.auditStatusDisplay}</td>
								<td>${(wthdraw.auditor.username)!""}</td>
								<td>
									<#if (wthdraw.auditStatus == 0)>
									<a href="javascript:void(-1);" class="auditClass" data-json='${wthdraw.jsonString}'>审核</a>
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
	</div>
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog">
	  <div class="modal-dialog modal-lg" role="document">
	    <div class="modal-content">
	      <div class="modal-body">
	      	<form class="form-horizontal" id="editform" method="post" action="/manage/withdraw/audit">
	      		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				<fieldset>
					<div id="legend" class="">
						<legend>实名认证审核</legend>
					</div>
					<input type="hidden" name="id" value="" />
					<input type="hidden" name="auditStatus" value="" /> 
					<div class="row">
			        	<label class="col-sm-2 control-label" for="username">用户名</label>
			        	<div class="col-sm-4">
			        		<label class="form-control" name="username"></label>
			        	</div>
			        	<label class="col-sm-2 control-label" for="bankName">银行</label>
			        	<div class="col-sm-4">
			        		<label class="form-control" name="bankName"></label>
			        	</div>
					</div>
					<div class="row">
			        	<label class="col-sm-2 control-label" for="cardNumber">账号</label>
			        	<div class="col-sm-4">
			        		<label class="form-control" name="cardNumber"></label>
			        	</div>
			        	<label class="col-sm-2 control-label" for="realName">开户名</label>
			        	<div class="col-sm-4">
			        		<label class="form-control" name="realName"></label>
			        	</div>
					</div>
					<div class="row">
			        	<label class="col-sm-2 control-label" for="amount">提现金额</label>
			        	<div class="col-sm-4">
			        		<label class="form-control" name="amount"></label>
			        	</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label" for="remark">审核备注</label>
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
</body>
</html>