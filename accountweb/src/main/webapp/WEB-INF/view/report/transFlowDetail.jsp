
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
            <div class="pull-left">报表管理</div>
            <em class=""></em>
            <div class="pull-left active">交易详情</div>
		</div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
				<div class="ibox-content">
					<!--边框表格布局: table table-bordered \悬停表格 : table table-hover-->	
					<label class="col-sm-0 control-label">交易信息</label>		
					<br /><br />
					<form class="form-horizontal" >
                        <div class="form-group">
                  		    <div class="pull-left control-label labeldiv2">来源系统：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.fromSystem }</div>
						    <div class="pull-left control-label labeldiv2">来源系统交易日期：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.transDate }</div>
                            <div class="pull-left control-label labeldiv2">来源系统流水号：</div>
							<div class="col-sm-2 control-label" style="text-align:left;font-size:12px">
							         ${data.fromSerialNo }
							 </div>									  										
						</div>
						<div class="form-group">
                  		    <div class="pull-left control-label labeldiv2">记账标志：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.recordStatus }</div>
						    <div class="pull-left control-label labeldiv2">冲销交易标志：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.reverseFlag }</div>
                            <div class="pull-left control-label labeldiv2">冲销状态：</div>
							<div class="col-sm-2 control-label" style="text-align:left;font-size:12px">
							         ${data.reverseStatus }
							 </div>									  										
						</div>
						<div class="form-group">
                  		    <div class="pull-left control-label labeldiv2">交易类型：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.transType }</div>	
                            <c:if test="${data.isReverse==false}">
                            <div class="pull-left control-label labeldiv2">交易金额：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.transAmount }</div>	
                            <div class="pull-left control-label labeldiv2">交易订单号：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.transOrderNo }</div>
                            </c:if>								  										
						</div>
						<c:if test="${data.isReverse==false}">
						<div class="form-group">
                  		    <div class="pull-left control-label labeldiv2">交易卡号：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.cardNo }</div>	
                            <div class="pull-left control-label labeldiv2">交易卡类型：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.cardType }</div>	
						</div>
						<br />
						</c:if>		
					</form>
					
					<c:if test="${data.isReverse==false}">
					<label class="col-sm-0 control-label">商户信息</label>
					<br /><br />
					<form class="form-horizontal" >
						<div class="form-group">
                  		    <div class="pull-left control-label labeldiv2">商户名称：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.merchantName }</div>	
                            <div class="pull-left control-label labeldiv2">业务产品：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.bpName }</div>							  										
						</div>
                        <div class="form-group">
						    <div class="pull-left control-label labeldiv2">交易服务种类：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.serviceType1 }</div>
                            <div class="pull-left control-label labeldiv2">交易服务费率：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.merchantRate1 }</div>
                            <div class="pull-left control-label labeldiv2">交易手续费：</div>
							<div class="col-sm-2 control-label" style="text-align:left;font-size:12px">
							         ${data.merchantFee }
							 </div>									  										
						</div>
						<div class="form-group">
                  		    <div class="pull-left control-label labeldiv2">硬件产品种类：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.hpType }</div>
						    <div class="pull-left control-label labeldiv2">机具号：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.deviceSn }</div>
                            <div class="pull-left control-label labeldiv2">终端号：</div>
							<div class="col-sm-2 control-label" style="text-align:left;font-size:12px">
							         ${data.terminalNo }
							 </div>									  										
						</div>
						<div class="form-group">
                  		    <div class="pull-left control-label labeldiv2">关联提现服务种类：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.serviceType2 }</div>	
                            <div class="pull-left control-label labeldiv2">关联提现服务费率：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.merchantRate2 }</div>
                            <div class="pull-left control-label labeldiv2">提现手续费：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.merchantSettleFee }</div>							  										
						</div>
						<br />
					</form>

					
					<label class="col-sm-0 control-label">代理商信息</label>
					<br /><br />
					<form class="form-horizontal" >
						<div class="form-group">
                  		    <div class="pull-left control-label labeldiv2">一级代理商编号：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.oneAgentNo }</div>	
                            <div class="pull-left control-label labeldiv2">一级代理商名称：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.agentName }</div>							  										
						</div>
						<div class="form-group">
                  		    <div class="pull-left control-label labeldiv2">交易分润金额：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.agentShareAmount }</div>	
                            <div class="pull-left control-label labeldiv2">提现分润金额：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.agentSettleShareAmount }</div>							  										
						</div>
						<br />
					</form>
					
					<label class="col-sm-0 control-label">收单机构(交易通道)</label>
					<br /><br />
					<form class="form-horizontal" >
						<div class="form-group">
                  		    <div class="pull-left control-label labeldiv2">机构名称：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.acqOrgName }</div>	
                            <div class="pull-left control-label labeldiv2">收单商户名称：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.acqMerchantName }</div>							  										
						</div>
						<div class="form-group">
                  		    <div class="pull-left control-label labeldiv2">收单服务：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.acqServiceName }</div>	
                            <div class="pull-left control-label labeldiv2">收单服务费1：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.acqOrgFee1 }</div>	
                            <div class="pull-left control-label labeldiv2">交易参考号：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.acqReferenceNo }</div>						  										
						</div>
						<br />
					</form>
					
					<label class="col-sm-0 control-label">收单机构(出款通道)</label>
					<br /><br />
					<form class="form-horizontal" >
						<div class="form-group">
                  		    <div class="pull-left control-label labeldiv2">机构名称：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.outOrgName }</div>			
                            <div class="pull-left control-label labeldiv2">出款服务：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.outServiceName }</div>    				  										
						</div>
						<div class="form-group">
                  		    
                            <div class="pull-left control-label labeldiv2">出款服务费率1：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${fn:escapeXml(data.outRate1)}</div>	
                            <div class="pull-left control-label labeldiv2">出款服务费1：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.outOrgFee1 }</div>						  										
						</div>
						<div class="form-group">
                  		    <!-- <div class="pull-left control-label labeldiv2">&nbsp;</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">&nbsp;</div>	 -->
                            <div class="pull-left control-label labeldiv2">出款服务费率2：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${fn:escapeXml(data.outRate2)}</div>	
                            <div class="pull-left control-label labeldiv2">出款服务费2：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.outOrgFee2 }</div>						  										
						</div>
						<br />
					</form>
					</c:if>
					<c:if test="${data.isReverse==true}">
					<label class="col-sm-0 control-label">冲销交易信息</label>
                    <br /><br />
                    <form class="form-horizontal" >
                        <div class="form-group">
                            <div class="pull-left control-label labeldiv2">原交易来源系统：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.oldFromSystem }</div>    
                            <div class="pull-left control-label labeldiv2">原交易交易时间：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.oldTransDate }</div>    
                            <div class="pull-left control-label labeldiv2" style="width:135px;margin-left:-15px">原交易来源系统流水号：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${data.oldFromSerialNo }</div>                                                                
                        </div>
                        <br />
                    </form>
					</c:if>
					
					<br/>
	                <div class="col-sm-12 col-sm-offset-0  ">
		                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		                <%--<button id="returnUp" type="button" class=" btn btn-default" onclick="window.location.href='${ctx}/reportAction/toTransFlow.do?queryParams=${params.queryParams}'" value="" /><span class="glyphicon gly-return"></span>返回</button>--%>
		                <button id="returnUp" type="button" class=" btn btn-default" onclick="winClose()" value="" /></span>关闭</button>
	                </div>
	                <br/><br/><br/><br/>
				</div>
			</div>
		</div>
	</div>
	
   </body>
    
	<title>
	<script src="${ctx}/js/plugins/select2/select2.full.min.js"></script>
	<script src="${ctx}/js/plugins/select2/i18n/zh-CN.js"></script>
		<script src="${ctx}/js/plugins/select2/i18n/zh-CN.js"></script>
		<script type="text/javascript">
			function winClose() {
				window.top.opener=null;
				window.close();
			}

		</script>
	 </title>
</html>  
      