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
		
		$("#query").click(function(){
			$("#currentPage").val(1);
			//$("#searchForm").submit();
		})
		
		$(".auditClass").click(function(){
			$("#myModal").modal("show");
			var data=$(this).data("json");
			$("#myModal [name=id]").val(data.id);
			$("#myModal [name=username]").html(data.username);
			$("#myModal [name=title]").html(data.title);
			$("#myModal [name=borrowAmount]").html(data.borrowAmount);
			$("#myModal [name=rate]").html(data.rate);
			$("#myModal [name=repaymentMonths]").html(data.repaymentMonths);
			$("#myModal [name=repaymentMethodDisplay]").html(data.repaymentMethodDisplay);
			$("#myModal [name=grossInterest]").html(data.grossInterest);
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
		});
	});
</script>
</head>
<body>
	<div class="container">
		<#include "../common/top.ftl"/>
		<div class="row">
			<div class="col-sm-3">
				<#assign currentMenu="borrow_publish">
				<#include "../common/menu.ftl" />
			</div>
			<div class="col-sm-9">
				<div class="page-header">
					<h3>发标前审核管理</h3>
				</div>
				<form id="searchForm" class="form-inline" method="post" action="/manage/borrowt/publish">
					<input type="hidden" id="currentPage" name="currentPage" value="1"/>
				</form>
				<div class="panel panel-default">
					<table class="table">
						<thead>
							<tr>
								<th>标题</th>
								<th>借款人</th>
								<th>申请时间</th>
								<th>借款金额(元)</th>
								<th>期限</th>
								<th>利率</th>
								<th>总利息</th>
								<th>状态</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
						<#list pageResult.content as borrow>
							<tr>
								<td>
									<a target="_blank" href="/manage/borrow/info?id=${borrow.id}">${borrow.title}</a>&emsp;<span class="label label-primary">信</span>
								</td>
								<td>${borrow.borrower.username}</td>
								<td>${(borrow.applyTime?datetime)!'未发布'}</td>
								<td>${borrow.borrowAmount}</td>
								<td>${borrow.repaymentMonths}月</td>
								<td>${borrow.rate}%</td>
								<td>${borrow.grossInterest}</td>
								<td>${borrow.borrowStatusDisplay}</td>
								<td>
									<a href="javascript:void(-1);" class="auditClass" data-json='${borrow.jsonString}'>审核</a>
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
		      	<form class="form-horizontal" id="editform" method="post" action="/manage/borrow/publish_audit">
		      		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
					<fieldset>
						<div id="legend" class="">
							<legend>发标前审核</legend>
						</div>
						<input type="hidden" name="id" value="" />
						<input type="hidden" name="auditStatus" value="" /> 
						<div class="form-group">
				        	<label class="col-sm-2 control-label" for="title">标题</label>
				        	<div class="col-sm-6">
				        		<label class="form-control" name="title"></label>
				        	</div>
			        	</div>
			        	<div class="form-group">
				        	<label class="col-sm-2 control-label" for="username">借款人</label>
				        	<div class="col-sm-6">
				        		<label class="form-control" name="username"></label>
				        	</div>
			        	</div>
			        	<div class="form-group">
				        	<label class="col-sm-2 control-label" for="borrowAmount">借款金额(元)</label>
				        	<div class="col-sm-6">
				        		<label class="form-control" name="borrowAmount"></label>
				        	</div>
			        	</div>
			        	<div class="form-group">
				        	<label class="col-sm-2 control-label" for="repaymentMonths">期限</label>
				        	<div class="col-sm-6">
				        		<label class="form-control" name="repaymentMonths"></label>
				        	</div>
			        	</div>
			        	<div class="form-group">
				        	<label class="col-sm-2 control-label" for="repaymentMethodDisplay">还款方式</label>
				        	<div class="col-sm-6">
				        		<label class="form-control" name="repaymentMethodDisplay"></label>
				        	</div>
			        	</div>
			        	<div class="form-group">
				        	<label class="col-sm-2 control-label" for="rate">利率</label>
				        	<div class="col-sm-6">
				        		<label class="form-control" name="rate"></label>
				        	</div>
			        	</div>
			        	<div class="form-group">
				        	<label class="col-sm-2 control-label" for="grossInterest">总利息</label>
				        	<div class="col-sm-6">
				        		<label class="form-control" name="grossInterest"></label>
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