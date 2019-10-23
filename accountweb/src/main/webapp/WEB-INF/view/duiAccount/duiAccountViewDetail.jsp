
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
    
<style> 
   tr{
    display:block;
    margin:10px 0;
   }
</style> 
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">对账管理</div>
            <em class=""></em>
            <div class="pull-left active">对账明细</div>
		</div>
	</div>
	<!-- 填充内容开始 -->
    
    <!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
				<div class="ibox-content">
					<span style="font-size:14px;font-weight:bold;">核对信息: </span> 					 
					<table  border="0" style="border:1px solid #000000;" cellpadding="0"cellspacing="1"style="width:120px;">
					      <tr>
					         <td style="width:180px;" align="right">ID:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.id }</td>
					         <td style="width:180px;" align="right">对账批次号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.checkBatchNo }</td>
					         <td style="width:180px;" align="right">订单参考号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.orderReferenceNo }</td>
					      </tr>
					      <tr>
					         <td style="width:180px;" align="right">对账状态:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.checkAccountStatus }</td>
					         <td style="width:180px;" align="right">存疑记账状态:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.recordStatus }</td>
					         <td style="width:180px;" align="right">创建时间:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.createTime }</td>
					      </tr>
					</table>
					 <br/>
					 <br/>
					<span style="font-size:14px;font-weight:bold;">平台信息: </span> 					 
					<table  border="0" style="border:1px solid #000000;" cellpadding="0"cellspacing="1"style="width:120px;">
					      <tr>
					         <td style="width:180px;" align="right">上游名称:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.acqEnname }</td>
					         <td style="width:180px;" align="right">平台商户号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.plateMerchantNo }</td>
					         <td style="width:180px;" align="right">平台交易金额:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.plateTransAmount }</td>
					      </tr>
					      <tr>
					         <td style="width:180px;" align="right">平台收单机构商户手续费 :</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.plateAcqMerchantFee }</td>
					         <td style="width:180px;" align="right">平台商户手续费:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.plateMerchantFee }</td>
					         <td style="width:180px;" align="right">平台交易账号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.plateAccountNo }</td>
					      </tr>
					      <tr>
					         <td style="width:180px;" align="right">平台交易状态:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.plateTransStatus }</td>
					         <td style="width:180px;" align="right">平台交易类型:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.plateTransType }</td>
					         <td style="width:180px;" align="right">交易记账:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.account }</td>
					      </tr>
					      <tr>
					         <td style="width:180px;" align="right">平台收单机构商户号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.plateAcqMerchantNo }</td>
					         <td style="width:180px;" align="right">平台收单机构终端号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.plateAcqTerminalNo }</td>
					         <td style="width:180px;" align="right">平台终端号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.plateTerminalNo }</td>
					      </tr>
					      <tr>
					         <td style="width:180px;" align="right">出账任务金额:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.taskAmount }</td>
					         <td style="width:180px;" align="right">结算周期:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.settlementMethod }</td>
					         <td style="width:180px;" align="right">结算状态:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.settleStatus }</td>
					      </tr>
					      <tr>
					         <td style="width:180px;" align="right">出账方式:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.settleType }</td>
					         <td style="width:180px;" align="right">平台订单号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.plateOrderNo }</td>
					         <td style="width:180px;" align="right">平台流水号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.plateSerialNo }</td>
					      </tr>
					      <tr>
					         <td style="width:180px;" align="right">平台批次号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.plateBatchNo }</td>
					         <td style="width:180px;" align="right">平台收单机构批次号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.plateAcqBatchNo }</td>
					         <td style="width:180px;" align="right">平台收单机构参考号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.plateAcqReferenceNo }</td>
					      </tr>
					      <tr>
					         <td style="width:180px;" align="right">收单机构商户扣率:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.plateAcqMerchantRate }</td>
					         <td style="width:180px;" align="right">商户扣率:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.plateMerchantRate }</td>
					         <td style="width:180px;" align="right">平台收单机构流水号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.plateAcqSerialNo }</td>
					      </tr>
					      <tr>
					         <td style="width:180px;" align="right">平台交易方式:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.payMethod }</td>
					         <td style="width:180px;" align="right">平台收单机构交易时间:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.plateAcqTransTime }</td>
					         <td style="width:180px;" align="right">平台商户结算日期:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.plateMerchantSettleDate }</td>
					      </tr>
					</table> 
					
					 <br/>
					 <br/>
					<span style="font-size:14px;font-weight:bold;">收单机构信息: </span> 					 
					<table  border="0" style="border:1px solid #000000;" cellpadding="0"cellspacing="1"style="width:120px;">
					      <tr>
					         <td style="width:180px;" align="right">收单机构英文名称:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.acqEnname }</td>
					         <td style="width:180px;" align="right">收单机构交易金额:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.acqTransAmount }</td>
					         <td style="width:180px;" align="right">收单机构退货金额:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.acqRefundAmount }</td>
					      </tr>
					      <tr>
					         <td style="width:180px;" align="right">收单机构交易订单号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.acqTransOrderNo }</td>
					         <td style="width:180px;" align="right">收单机构交易状态:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.acqTransStatus }</td>
					         <td style="width:180px;" align="right">收单机构卡号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.acqAccountNo }</td>
					      </tr>
					      <tr>
					         <td style="width:180px;" align="right">收单机构交易流水号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.acqTransSerialNo }</td>
					         <td style="width:180px;" align="right">收单机构流水号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.acqSerialNo }</td>
					         <td style="width:180px;" align="right">收单机构订单号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.acqOrderNo }</td>
					      </tr>
					      <tr>
					         <td style="width:180px;" align="right">收单机构商户号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.acqMerchantNo }</td>
					         <td style="width:180px;" align="right">接入机构编号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.accessOrgNo }</td>
					         <td style="width:180px;" align="right">收单机构系统参考号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.acqReferenceNo }</td>
					      </tr>
					      <tr>
					         <td style="width:180px;" align="right">收单机构商户名称:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.acqMerchantName }</td>
					         <td style="width:180px;" align="right">收单机构原交易流水号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.acqOriTransSerialNo }</td>
					         <td style="width:180px;" align="right">收单机构终端号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.acqTerminalNo }</td>
					      </tr>
					      <tr>
					         <td style="width:180px;" align="right">收单机构批次号:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.acqBatchNo }</td>
					         <td style="width:180px;" align="right">卡类型:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.acqCardSequenceNo }</td>
					         <td style="width:180px;" align="right">收单机构交易码:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.acqTransCode }</td>
					      </tr>
					      <tr>
					         <td style="width:180px;" align="right">收单机构交易时间:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.acqTransTime }</td>
					         <td style="width:180px;" align="right">收单机构对账日期:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.acqCheckDate }</td>
					         <td style="width:180px;" align="right">收单机构入账日期:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;${data.acqSettleDate }</td>
					      </tr>
					      <tr>
							  <td style="width:180px;" align="right">凭证号:</td>
							  <td style="width:200px;" align="left">&nbsp;&nbsp;${data.acqSerialNo }</td>
					         <td style="width:180px;" align="right">预留字段1:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;</td>
					         <td style="width:180px;" align="right">预留字段2:</td>
					         <td style="width:200px;" align="left">&nbsp;&nbsp;</td>
					      </tr>
					</table> 
					
					 <br/>
					 <br/>
					 
	                <div class="col-sm-0 col-sm-offset-0  ">
		                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		                <button id="returnUp" type="button" class=" btn btn-default" onclick="window.location.href='${ctx}/duiAccountAction/toDuiAccountDetailQuery.do?queryParams=${params.queryParams}'" value="" /><span class="glyphicon gly-return"></span>返回</button>
	                </div>
	                <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
				</div>
				
				     
			</div>
		</div>
   </body>

</html>  
      