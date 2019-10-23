
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<!-- jqGrid plugin -->
	<link href="${ctx}/css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/jqGrid/ui.jqgrid.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/bootstrapTour/bootstrap-tour.min.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">出账管理</div>
            <em class=""></em>
            <div class="pull-left active">出账任务详情</div>
		</div>
	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
				<div class="ibox-content">
					<!-- 出账任务 -->
					<!--边框表格布局: table table-bordered \悬停表格 : table table-hover-->					 
					<br/>
					<label class="col-sm-0 control-label">出账任务</label>
					<table class="table table-bordered" style="width:100;max-width:100%">
					   <thead>
					      <tr style="text-align: center;">
					         <td width="200">出账任务 ID</td>
					         <td width="200">交易日期</td>
					         <td width="200" style="text-align: right;">总交易额（万）</td>
					         <td width="200" style="text-align: right;">上游结算中金额</td>
					         <td width="200" style="text-align: right;">出账任务金额</td>
					         <td width="200">上游个数</td>
					         <td width="200">出账单ID</td>
					         <td width="200">系统计算时间</td>
					      </tr>
					   </thead>
					   <tbody>
					      <tr>
					         <td>${outAccountTask.id }</td>
					         <td>
					         	<fmt:formatDate value="${outAccountTask.transTime }" pattern="yyyy-MM-dd HH:mm:ss"/> 
					         </td>
					         <td style="text-align: right;">${outAccountTask.transAmount }</td>
					         <td style="text-align: right;">${outAccountTask.upBalance }</td>
					         <td style="text-align: right;">${outAccountTask.outAccountTaskAmount }</td>
					         <td>${outAccountTask.upCompanyCount }</td>
					         <td>${outAccountTask.outAccountId }</td>
					         <td><fmt:formatDate value="${outAccountTask.sysTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					      </tr>
					   </tbody>
					</table>
					
					<br/><br/><br/>
					<label class="col-sm-0 control-label">出账任务明细</label>
					<!-- 出账任务详细 -->
					<!--边框表格布局: table table-bordered \悬停表格 : table table-hover-->					 
					<table class="table table-bordered" style="width:100;max-width:100%">
					   <thead>
					      <tr style="text-align: center;">
					         <td width="200">出账任务明细 ID</td>
					         <td width="200">出账任务 ID</td>
					         <td width="200">上游</td>
					         <td width="200">交易金额</td>
					         <td width="200">上游结算中金额</td>
					         <td width="200">上游当日余额</td>
					         <td width="200">出账金额</td>
					      </tr>
					   </thead>
					   <tbody>
					      <c:forEach items="${outAccountTaskDetailList }" var="outAccountTaskDetail">
					      	<tr>
						         <td>${outAccountTaskDetail.id }</td>
						         <td>${outAccountTaskDetail.outAccountTaskId }</td>
						         <td>${outAccountTaskDetail.acqOrgNo}</td>
						         <td style="text-align: right;">${outAccountTaskDetail.todayAmount }</td>
						         <td style="text-align: right;">${outAccountTaskDetail.upBalance }</td>
						         <td style="text-align: right;">${outAccountTaskDetail.todayBalance }</td>
						         <td style="text-align: right;">${outAccountTaskDetail.outAccountAmount }</td>
					      	</tr>
					      </c:forEach>
					      
					   </tbody>
					</table>
					<div class="col-sm-0" style="margin-top:30px">
	                 	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	                 	<button id="returnUp" type="button" class=" btn btn-default" onclick="window.location.href='${ctx}/chuAccountAction/toChuAccountTasksManage.do?queryParams=${params.queryParams}'" value="" /><span class="glyphicon gly-return"></span>返回</button>
	                </div>
	                <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
				</div>
				
				<br/>
                
                     
			</div>
		</div>
			
	</div>
	
   </body>
    
	<title>
	 </title>
</html>  
      