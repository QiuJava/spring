
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">银行账户管理</div>
            <em class=""></em>
            <div class="pull-left active">银行账户详情</div>
        </div>

	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
				<div class="ibox-content">
					<!--边框表格布局: table table-bordered \悬停表格 : table table-hover-->					 
					<table class="table table-hover" style="width:100%;max-width:100%">
					   <thead>
					      <tr>
					         <td style='text-align: right;' width="120">开户银行名称：</td>
					         <td >${bankAccount.bankName }</td>
					         <td style='text-align: right;' width="120">开户名：</td>
					         <td >${bankAccount.accountName }</td>
					         <td style='text-align: right;' width="120">开户账号：</td>
					         <td >${bankAccount.accountNo }</td>
					      </tr>
					     <%--  <tr>
					         <td width="300">对账批次号</td>
					         <td width="300">${map.checkBatchNo }</td>
					         <td width="300">收单机构名称</td>
					         <td width="300">${map.acqCnname }</td>
					      </tr> --%>
					   </thead>
					   <tbody>
					      <tr>
					         <td style='text-align: right;'>支付机构：</td>
					         <td>${bankAccount.orgNo }</td>
					         <td style='text-align: right;'>币种号：</td>
					         <td>${bankAccount.currencyNo }</td>
					         <td style='text-align: right;'>账号类别：</td>
					         <td>${bankAccount.accountType }</td>
					      </tr>
					      <tr>
					         <td style='text-align: right;'>科目号：</td>
					         <td>${bankAccount.subjectNo }</td>
					         <td style='text-align: right;'>联行行号：</td>
					         <td>${bankAccount.cnapsNo }</td>
					         <td style='text-align: right;'>状态：</td>
					         <td>
					         	<%-- <c:if test="${bankAccount.inputAccount.accountStatus =='1'}">正常</c:if>
					         	<c:if test="${bankAccount.inputAccount.accountStatus =='2'}">销户</c:if>
					         	<c:if test="${bankAccount.inputAccount.accountStatus =='3'}">冻结</c:if> --%>
					         	${bankAccount.insAccount.accountStatus }
					         </td>
					      </tr>
					   </tbody>
					</table>
					 <br/>
		                <div class="col-sm-0 col-sm-offset-0  ">

		                 <button id="returnUp" type="button" class=" btn btn-default " onclick="window.location.href='${ctx}/bankAccountAction/bankAccountManage.do'" value="" /><span class="glyphicon gly-return"></span>返回</button>
		                </div>
		                <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
				
				
                     
			</div>
		</div>
			
	</div>
	
   </body>
    
	<title>
	 </title>
</html>  
      