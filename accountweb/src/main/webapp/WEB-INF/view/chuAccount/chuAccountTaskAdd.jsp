
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<!-- jqGrid plugin -->
	<link href="${ctx}/css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/jqGrid/ui.jqgrid.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/bootstrap-datepicker/bootstrap-datepicker3.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
</head>
<body>
		<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">出账管理</div>
            <em class=""></em>
            <div class="pull-left active">新增出账任务</div>
        </div>
	</div>
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					<form class="form-horizontal" id="chuAccountBillManageForm" onsubmit="return false">
                          <div class="form-group" >
                          	  <label class="col-sm-2 control-label">交易日期:</label>
                           		<div class="col-sm-2">
								    <input type="text" class="form-control" disabled name="transTime" id="transTime" value="<fmt:formatDate value='${sysInfo.parentTransDate}' pattern='yyyy-MM-dd'/>"/>
								</div>
								<label class="col-sm-2 control-label">选择上游:</label>
    							<div class="col-sm-2">
		                            <select class="form-control" name="acqEnname" id="acqEnname" onchange="changeOutBilRange(this.options[this.options.selectedIndex].value)"> 
									         <option value="ALL" selected="selected">--请选择--</option>
									         <c:forEach var="acqOrg" items="${acqOrgList}">
												 <c:if test="${acqOrg.sysValue != 'ds_pay' && acqOrg.sysValue != 'ZY' && acqOrg.sysValue != 'ZYLD'}">
													 <option value="${acqOrg.sysValue}">
															 ${acqOrg.sysName}
													 </option>
												 </c:if>
											</c:forEach>
										</select>       
								</div>
								<label class="col-sm-2 control-label">出账范围:</label>
    							<div class="col-sm-2">
		                            <select class="form-control" name="outBillRange" id="outBillRange"> 
		                            	<option value="ALL" selected="selected">--请选择--</option>
									         <c:forEach var="outBillRange" items="${outBillRangeList}">
												<option value="${outBillRange.sysValue}"
												<c:if test="${outBillRange.sysValue == params.outBillRange}">selected="selected"</c:if>>
													${outBillRange.sysName}
												</option>
											</c:forEach>
										</select>     
								</div>
							</div>
							<div class="form-group" style="display:none;" id="moreDiv">
                          	  <label class="col-sm-2 control-label">上游结算中金额:</label>
                           		<div class="col-sm-2">
		                            <label class="control-label" id="upBalance">10000</label>
								</div>
								<label class="col-sm-2 control-label">出账任务金额:</label>
    							<div class="col-sm-2">
    								<input type="text" class="form-control" value="10000" id="outAccountTaskAmount"/>
								</div>
							</div>   
                            <div class="clearfix lastbottom"></div>
                            <div class="form-group" style="margin-bottom:0">
                            	<label class="col-sm-2 control-label aaa"></label>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            	<button class="btn btn-success" id="comfirnAdd" onclick="confirmAdd()" disabled><span class="glyphicon"></span>确认新增</button>
                              	<button id="returnUp" type="button" class=" btn btn-default  col-sm-offset-14" onclick="window.location.href='${ctx}/chuAccountAction/toChuAccountTasksManage.do'" value="" /><span class="glyphicon gly-return"></span>返回</button>
                            </div>
                             <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
                	</form>
				</div>
			</div>
		</div>
	</div>
		
</body>
    
	<title>
	<script src="${ctx}/js/plugins/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
	<script src="${ctx}/js/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js"></script>
	<script src="${ctx}/js/plugins/sweetalert/sweetalert.min.js"></script>
	<script type="text/javascript">
		function confirmAdd() {   
			var acqEnname = $("#acqEnname option:selected").val();
	    	var transTime = $("#transTime").val();
	    	var upBalance = $("#upBalance").text();
	    	var outBillRange = $("#outBillRange option:selected").val();
	    	var outAccountTaskAmount = $("#outAccountTaskAmount").val();
			$.ajax({
	    			url: "${ctx}/chuAccountAction/confirmAddTask.do",
	    			type: "post",
	    			dataType: "json",
	    			data: {acqEnname: acqEnname, transTime: transTime, upBalance: upBalance, outBillRange: outBillRange, outAccountTaskAmount: outAccountTaskAmount, '${_csrf.parameterName}': '${_csrf.token}'},
	    			success: function(data) {
	    				if (data.success) {
	    					swal({title:"提示" ,text: data.msg ,animation:"slide-from-top"}, function() {window.location.href='${ctx}/chuAccountAction/toChuAccountTasksManage.do';});
	    				} else {
	    					swal({title:"提示" ,text: data.msg ,animation:"slide-from-top"});
	    				}
	    			}
	    		});
	    		return false;
	    }
		
		 function changeOutBilRange(acqEnname) {
			$("#outBillRange").empty();
			$("#moreDiv").css("display", "none");
			if ("ZF_ZQ" != acqEnname) {
				$("#outBillRange").append("<option value='ALL'>--请选择--</option>");
				$("#outBillRange").append("<option value='No'>不限</option>");
			} else {
				$("#outBillRange").append("<option value='ALL'>--请选择--</option>");
				$("#outBillRange").append("<option value='T1'>T+1</option>");
				$("#outBillRange").append("<option value='Tn'>T+n</option>");
			}
		} 

	    $(function() {
	    	
	    	/*  $("#acqEnname").change(function() {
	    		 $("#outBillRange").empty();
	 			if ("neweptok" == acqEnname || "TFB_API" == acqEnname || "mo_pay" == acqEnname || "gzms" == acqEnname || "gzms_zfb" == acqEnname) {
	 				$("#outBillRange").append("<option value='No'>不限</option>");
	 			} else {
	 				$("#outBillRange").append("<option value='T1'>T+1</option>");
	 				$("#outBillRange").append("<option value='Tn'>T+n</option>");
	 			}
	    	});  */
	    	
	    	$("#outBillRange").change(function() {
	    		var acqEnname = $("#acqEnname option:selected").val();
	    		var outBillRange = $("#outBillRange option:selected").val();
	    		var transTime = $("#transTime").val();
	    		if (acqEnname != '' && transTime != '' && acqEnname != 'ALL'){
	    		$.ajax({
	    			url: "${ctx}/chuAccountAction/calcSettlingAmountByParam.do",
	    			type: "post",
	    			dataType: "json",
	    			data: {acqEnname: acqEnname, transTime: transTime, outBillRange:outBillRange, '${_csrf.parameterName}': '${_csrf.token}'},
	    			success: function(data) {
	    				if (data.success) {
	    					$("#upBalance").text(data.upBalance);
	    					$("#outAccountTaskAmount").val(data.upBalance);
	    					$("#moreDiv").css("display", "block");
	    					if (data.upBalance == 0) {
	    						swal({title:"提示" ,text: "当前上游结算中金额为零" ,animation:"slide-from-top"});
	    						$("#comfirnAdd").attr("disabled", "true");
	    					} else {
	    						$("#comfirnAdd").removeAttr("disabled");
	    					}
	    				} else {
	    					swal({title:"提示" ,text: data.msg ,animation:"slide-from-top"});
	    					$("#comfirnAdd").attr("disabled", "true");
	    				}
	    			}
	    		});
	    		}
	    	});
	    	
	    	$("#transTime").change(function() {
	    		var acqEnname = $("#acqEnname option:selected").val();
	    		var outBillRange = $("#outBillRange option:selected").val();
	    		var transTime = $("#transTime").val();
	    		if (acqEnname != '' && transTime != '' && acqEnname != 'ALL'){
	    		$.ajax({
	    			url: "${ctx}/chuAccountAction/calcSettlingAmountByParam.do",
	    			type: "post",
	    			dataType: "json",
	    			data: {acqEnname: acqEnname, transTime: transTime, outBillRange:outBillRange, '${_csrf.parameterName}': '${_csrf.token}'},
	    			success: function(data) {
	    				if (data.success) {
	    					$("#upBalance").text(data.upBalance);
	    					$("#outAccountTaskAmount").val(data.upBalance);
	    					$("#moreDiv").css("display", "block");
	    					if (data.upBalance == 0) {
	    						swal({title:"提示" ,text: "当前上游结算中金额为零" ,animation:"slide-from-top"});
	    						$("#comfirnAdd").attr("disabled", "true");
	    					} else {
	    						$("#comfirnAdd").removeAttr("disabled");
	    					}
	    				} else {
	    					swal({title:"提示" ,text: data.msg ,animation:"slide-from-top"});
	    					$("#comfirnAdd").attr("disabled", "true");
	    				}
	    			}
	    		});
	    		}
	    	});
	    	
	    	
	    });
	    
	    $('.input-daterange').datepicker({
                format: "yyyy-mm-dd",
                language: "zh-CN",
                todayHighlight: true,
                autoclose: true,
                clearBtn: true
            });
	</script>
	</title>
</html>  
      