
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<!-- jqGrid plugin -->
	<link href="${ctx}/css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/jqGrid/ui.jqgrid.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/select2/select2.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/select2/select2-skins.min.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">记账处理</div>
            <em class=""></em>
            <div class="pull-left active">查看交易类型与记账规则定义</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
                          
                          <table style="margin-left: -0%;margin-right: 5% ; width: 90%" >
                          	<tr >
                          		<td style="text-align: right;"><label>交易类型编号：</label></td>
                          		<td >${transType.transTypeCode }</td>
                          		<td style="text-align: right;"><label>交易类型名称：</label></td>
                          		<td >${transType.transTypeName }</td>
                          	</tr>
                          	<tr><td>&nbsp;</td></tr>
                          	<tr>
                          		<td width="100" style="text-align: right;"><label>记账规则：</label></td>
                          		<td width="30%" >[${transType.recordAccountRule.ruleNo }]${transType.recordAccountRule.ruleName }</td>
                          		<td width="100" style="text-align: right;"><label>来源系统：</label></td>
                          		<td>
                          			<c:forEach items="${fromSystemList }" var="fromSystem">
                          				<c:if test="${fromSystem.sysValue == transType.fromSystem }">${fromSystem.sysName} [ ${fromSystem.sysValue } ]</c:if>
                          			</c:forEach>
                          		</td>                          		
                          	</tr>
                          	<tr><td>&nbsp;</td></tr>
                          	<tr>
                          	<td width="100" style="text-align: right;"><label>交易分组：</label></td>
                          		<td colspan="5">${transType.transGroup }</td>
                          	</tr>	
                          	<tr><td>&nbsp;</td></tr>
                          	<tr>
                          		<td width="100" style="text-align: right;"><label>说明：</label></td>
                          		<td colspan="5">${transType.remark }</td>
                          	</tr>
                          </table>

				</div>
			</div>
		</div>
    <div class="col-lg-12">
      <div class="ibox float-e-margins">
        <div class="ibox-content">
          <div class="" style="margin-left:-0.203333%">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <button id="returnUp" type="button" class=" btn btn-default " onclick="window.location.href='${ctx}/recordAccountAction/toTransTypeListQuery.do?queryParams=${params.queryParams}'" value="" /><span class="glyphicon gly-return"></span>返回</button>
                <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />

            </div>
        </div>
      </div>
    </div>   
		
	</div>
	<!-- 填充内容结束 -->
		
</body>
    
	<title>
	<script src="${ctx}/js/plugins/select2/select2.full.min.js"></script>
	<script src="${ctx}/js/plugins/select2/i18n/zh-CN.js"></script>
	 </title>
</html>  
      