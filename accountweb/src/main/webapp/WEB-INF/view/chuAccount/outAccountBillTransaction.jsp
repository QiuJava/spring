<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/bootstrap-datepicker/bootstrap-datepicker3.min.css" rel="stylesheet">
	<link href="${ctx}/css/showLoading.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">出账管理</div>
            <em class=""></em>
            <div class="pull-left active">回盘文件导入</div>
		</div>
	</div>
	
	
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox-content">
				 <form class="form-horizontal" id="form1">
                      <div class="form-group">
                  		   <label class="col-sm-2 control-label">出款通道: </label>
                           <div class="col-sm-2">
								<select class="form-control" name="settleBank" id="settleBank"> 
							         <c:forEach var="acqOrg" items="${acqOrgList}">
										<option value="${acqOrg.sysValue}">
											${acqOrg.sysName}
										</option>
									</c:forEach>
								</select>      
							</div>
                            <label class="col-sm-2 control-label">选择日期: </label>
                            <div class="col-sm-3">
                                <div id="datepicker" class="input-daterange input-group">
                                    <input type="text" class="input-sm form-control" name="mdate" id="mdate" />
                                </div>
                            </div>  
						</div>
						
						
                     	<div class="clearfix lastbottom"></div>
                        
						<div class="form-group">
                            <label class="col-sm-2 control-label aaa"></label>
							<!-- <div class="col-sm-12 col-sm-offset-13  "> -->
							<%-- <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> --%>
							   <button type="button" class=" btn btn-success" id="btnSubmit" onclick="sync();" value="">同步</button>
							   <button id="returnUp" type="button" class=" btn btn-default col-sm-offset-14" onclick="goBack();" value=""><span class="glyphicon gly-return"></span>返回</button>
							<!-- </div> -->
						</div>
						<br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
                    </form>
				</div>
			</div>
		</div>
			
	</div>
</body>
	<title>
		<script src="${ctx}/js/plugins/filestyle/bootstrap-filestyle.js"></script>
	<script src="${ctx}/js/plugins/fileupload/jquery.fileupload.js"></script>
	<script src="${ctx}/js/plugins/fileupload/jquery.ui.widget.js"></script>  
	<script src="${ctx}/js/plugins/fileupload/jquery.iframe-transport.js"></script>  
	<script src="${ctx}/js/plugins/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
	<script src="${ctx}/js/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js"></script>
	<!-- Sweet alert -->
    <script src="${ctx}/js/plugins/sweetalert/sweetalert.min.js"></script>
    
    <script src="${ctx}/js/jquery.showLoading.js"></script>
	<script src="${ctx}/js/jquery.showLoading.min.js"></script>
    
    <script type="text/javascript">
		$(function () {
			$('.input-daterange').datepicker({
                format: "yyyy-mm-dd",
                language: "zh-CN",
                todayHighlight: true,
                autoclose: true,
                clearBtn: true
            });		    
		});
		
		
		function goBack(){
			window.location.href='${ctx}/chuAccountAction/toChuAccountBillManage.do';
		}
		
		
		function sync() {
	        var out_bill_id = '${outBill.id}';
	        var settleBank = $("#settleBank option:selected").val();
	        var mdate = $.trim($("#mdate").val());
	        var data = {'outBillId':out_bill_id,'${_csrf.parameterName}':'${_csrf.token}','settleBank':settleBank,'mdate':mdate};
   			//console.info(data);
   			 $("body").showLoading();
   			$.ajax({
   				type: "post",
   				url: "${ctx}/chuAccountAction/transactionFileUpload.do",
   				data: data,
   				dataType: "json",
   				success: function(result) {
   					if (result.success) {
   						$("body").hideLoading();
   						swal({title:"提示" ,text:result.msg ,animation:"slide-from-top"});
                            $('.confirm').click(function(event) {
                                location.href = '${ctx}/chuAccountAction/toChuAccountBillManage.do';
                            });
   					} else {
   						$("body").hideLoading();
   						swal({title:"提示" ,text:result.msg ,animation:"slide-from-top"});
   					}
   				}
   			});
		}
	</script>
	</title>
</html>