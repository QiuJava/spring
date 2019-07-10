<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统管理平台</title>
<#include "../common/header.ftl"/>
<link type="text/css" rel="stylesheet" href="/css/account.css" />
<script type="text/javascript" src="/js/bank.js"></script>
<script type="text/javascript" src="/js/plugins/jquery.form.js"></script>
<script type="text/javascript" src="/js/plugins/jquery.twbsPagination.min.js"></script>
<script type="text/javascript" src="/js/My97DatePicker/WdatePicker.js" ></script>

<script type="text/javascript">

// 自定义时间显示格式
function customTime(time){
    var date = new Date(time);
    return date.getFullYear() + '-' + (date.getMonth()+1) + '-' +date.getDate();
}

</script>

<script type="text/javascript">
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
		
		$(".beginTime,.endTime").click(function(){
			WdatePicker();
		});
		
		$(".auditClass").click(function(){
			$("#myModal").modal("show");
			var data=$(this).data("json");
			var currentDate = customTime(data.tradeTime);
			$("#myModal [name=id]").val(data.id);
			$("#myModal [name=username]").html(data.username);
			$("#myModal [name=serialNumber]").html(data.serialNumber);
			$("#myModal [name=amount]").html(data.amount);
			$("#myModal [name=tradeTime]").html(currentDate);
		});
		
		$(".btn_audit").click(function(){
			var form=$("#editform");
			form.find("[name=auditStatus]").val($(this).val());
			$("#myModal").modal("hide");
			form.ajaxSubmit(function(data){
				if(data.success){
					$.messager.confirm("提示","审核成功!",function(){
						window.location.reload();
					});
				}
			});
			return false;
		})
	});
	
</script>
</head>
<body>
	<div class="container">
		<#include "../common/top.ftl"/>
		<div class="row">
			<div class="col-sm-3">
				<#assign currentMenu="recharge"/>
				<#include "../common/menu.ftl" />
			</div>
			<div class="col-sm-9">
				<div class="page-header">
					<h3>用户线下充值审核管理</h3>
				</div>
				<form id="searchForm" class="form-inline" method="post" action="/manage/recharge">
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
					<input type="hidden" id="currentPage" name="currentPage" value=""/>
					<div class="form-group">
					    <label>状态</label>
					    <select class="form-control" name="auditStatus">
					    	<option value="-1">全部</option>
					    	<option value="0">待审核</option>
					    	<option value="1">审核通过</option>
					    	<option value="2">审核拒绝</option>
					    </select>
					    <script type="text/javascript">
					    	$("[name=auditStatus] option[value='${(qo.auditStatus)!''}']").attr("selected","selected");
					    </script>
					</div>
					<div class="form-group">
					    <label>开户行</label>
					    <select class="form-control" name="companyBankCard.id">
					    	<option value="-1">全部</option>
					    	<#list companyBankCards as bank>
	    						<option value="${bank.id}">
									<script>
										var str="("+SITE_BANK_TYPE_NAME_MAP["${bank.bankName}"]+")${bank.cardNumber}${bank.bankForkName}";
										document.write(str);
									</script>
								</option>
	    					</#list>
					    </select>
					    <script type="text/javascript">
					    	$("[name=companyBankCard.id] option[value='${(qo.companyBankCard.id)!''}']").attr("selected","selected");
					    </script>
					</div>
					<div class="form-group">
					    <label>交易时间</label>
					    <input class="form-control" type="text" name="beginTime" id="beginTime" value="${(qo.beginTime?date)!''}" />到
					    <input class="form-control" type="text" name="endTime" id="endTime" value="${(qo.endTime?date)!''}" />
					</div>
					<div class="form-group">
					    <label>交易号</label>
					    <input class="form-control" type="text" name="serialNumber" value="${(qo.serialNumber)!''}" />
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
								<th>交易号</th>
								<th>交易时间</th>
								<th>充值金额</th>
								<th>平台账号</th>
								<th>状态</th>
								<th>审核人</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
						<#list pageResult.content as recharge>
							<tr>
								<td>${recharge.submitter.username}</td>
								<td>${recharge.serialNumber}</td>
								<td>${recharge.tradeTime?date}</td>
								<td>${recharge.amount}</td>
								<td>
									<script>
										var str="("+SITE_BANK_TYPE_NAME_MAP["${recharge.companyBankCard.bankName}"]+")${recharge.companyBankCard.cardNumber}${recharge.companyBankCard.bankForkName}";
										document.write(str);
									</script>
								</td>
								<td>${recharge.auditStatusDisplay}</td>
								<td>${(recharge.submitter.username)!""}</td>
								<td>
									<#if (recharge.auditStatus == 0)>
									<a href="javascript:void(-1);" class="auditClass" data-json='${recharge.jsonString}'>审核</a>
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
		      	<form class="form-horizontal" id="editform" method="post" action="/manage/recharge/audit">
		      	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
					<fieldset>
						<div id="legend" class="">
							<legend>线下充值审核</legend>
						</div>
						<input type="hidden" name="id" value="" />
						<input type="hidden" name="auditStatus" value="" /> 
						<div class="form-group">
				        	<label class="col-sm-2 control-label" for="username">用户名</label>
				        	<div class="col-sm-6">
				        		<label class="form-control" name="username"></label>
				        	</div>
			        	</div>
			        	<div class="form-group">
				        	<label class="col-sm-2 control-label" for="serialNumber">交易号</label>
				        	<div class="col-sm-6">
				        		<label class="form-control" name="serialNumber"></label>
				        	</div>
			        	</div>
			        	<div class="form-group">
				        	<label class="col-sm-2 control-label" for="amount">交易金额</label>
				        	<div class="col-sm-6">
				        		<label class="form-control" name="amount"></label>
				        	</div>
			        	</div>
			        	<div class="form-group">
				        	<label class="col-sm-2 control-label" for="tradeTime">交易时间</label>
				        	<div class="col-sm-6">
				        		<label class="form-control" name="tradeTime"></label>
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
	</div>
</body>
</html>