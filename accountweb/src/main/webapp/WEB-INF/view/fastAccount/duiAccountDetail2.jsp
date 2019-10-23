
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<!-- jqGrid plugin -->
	<link href="${ctx}/css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/jqGrid/ui.jqgrid.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/bootstrapTour/bootstrap-tour.min.css" rel="stylesheet">
    <script src="${ctx}/js/jquery-2.1.1.js"></script>
    <script src="${ctx}/js/bootstrap.min.js"></script>
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">对账管理</div>
            <em class=""></em>
            <div class="pull-left active">对账信息详情2</div>
		</div>
	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
				<div class="ibox-content">
					<!--边框表格布局: table table-bordered \悬停表格 : table table-hover-->					 
					<table class="table table-hover" style="width:600px;">
					   <thead>
					      <tr>
					         <td style="width:111px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;对账批次号：</td>
					         <td style="width:111px;">${map.checkBatchNo }</td>
					         <td style="width:131px;">&nbsp;&nbsp;&nbsp;&nbsp;收单机构名称：</td>
					         <td style="width:111px;">${map.acqCnname }</td>
					      </tr>
					   </thead>
					   <tbody>
					      <tr>
					         <td>渠道对平总金额：</td>
					         <td>${map.plateTransAmountSum }</td>
					         <td>渠道对平总笔数：</td>
					         <td>${map.plateTransAmountCount }</td>
					      </tr>
					      <tr>
					         <td>我方对平总金额：</td>
					         <td>${map.plateTransAmountSumMe }</td>
					         <td>我方对平总笔数：</td>
					         <td>${map.plateTransAmountCountMe }</td>
					      </tr>
					      <tr>
					         <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;长款总金额：</td>
					         <td>${map.longAcqTransAmountSum }</td>
					         <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;长款总笔数：</td>
					         <td>${map.longAcqTransAmountCount }</td>
					      </tr>
					      <tr>
					         <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;短款总金额：</td>
					         <td>${map.shortPlateTransAmountSum }</td>
					         <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;短款总笔数：</td>
					         <td>${map.shortPlateTransAmountCount }</td>
					      </tr>
					      <tr>
					         <td>渠道存疑总金额：</td>
					         <td>${map.acqTransAmountSum }</td>
					         <td>渠道存疑总笔数：</td>
					         <td>${map.longAcqTransAmountCount }</td>
					      </tr>
					      <tr>
					         <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;渠道手续费：</td>
					         <td>${map.acqRefundAmountSum }</td>
					         <td></td>
					         <td></td>
					      </tr>
					   </tbody>
					</table>
					 <br/>
	                <div class="col-sm-0 col-sm-offset-0  ">
		                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		                <button id="returnUp" type="button" class=" btn btn-default" onclick="window.location.href='${ctx}/fastDuiAccount/toDuiAccountQuery.do'" value="" /><span class="glyphicon gly-return"></span>返回</button>
	                </div>
	                <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
				</div>
				
				
                     
			</div>
		</div>
			
	</div>
	
   </body>
    
	<title>
	<script src="${ctx}/js/plugins/select2/select2.full.min.js"></script>
	<script src="${ctx}/js/plugins/select2/i18n/zh-CN.js"></script>
	 </title>
</html>  
      