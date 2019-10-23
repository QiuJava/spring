
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<!-- jqGrid plugin -->
<link href="${ctx}/css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
<link href="${ctx}/css/plugins/jqGrid/ui.jqgrid.css" rel="stylesheet">
<link href="${ctx}/css/plugins/select2/select2.min.css" rel="stylesheet">
<link href="${ctx}/css/plugins/select2/select2-skins.min.css" rel="stylesheet">
<!-- Sweet Alert -->
<link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">对账管理</div>
            <em class=""></em>
            <div class="pull-left active">快捷差错查询</div>
		</div>
	</div>
	<!-- 填充内容开始 -->

	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">

		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content" style="padding-bottom:0">
					<form class="form-horizontal" id="duiAccountForm" method="post">


						<div class="form-group">
							<label class="col-sm-2 control-label">对账批次号：</label>
							<div class="col-sm-2 ">
								<input type="text" class="form-control" name="checkBatchNo"
									id="checkBatchNo">
							</div>
							<label class="col-sm-2 control-label">收单机构商户名称：</label>
							<div class="col-sm-2 ">
								<input type="text" class="form-control" name="acqMerchantName"
									id="acqMerchantName">
							</div>

							<label class="col-sm-2 control-label">收单机构商户号：</label>
							<div class="col-sm-2 ">
								<input type="text" class="form-control" name="acqMerchantNo"
									id="acqMerchantNo">
							</div>

							
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label">收单机构终端号：</label>
							<div class="col-sm-2 ">
								<input type="text" class="form-control" name="acqTerminalNo"
									id="acqTerminalNo">
							</div>
							
							<label class="col-sm-2 control-label">收单机构卡号：</label>
							<div class="col-sm-2">
								<input type="text" class="form-control" name="acqAccountNo"
									id="acqCardSequenceNo">
							</div>

							<label class="col-sm-2 control-label">收单机构交易流水号：</label>
							<div class="col-sm-2">
								<input type="text" class="form-control" name="acqTransSerialNo"
									id="acqTransSerialNo">
							</div>

						</div>
						<div class="form-group">
							
							<label class="col-sm-2 control-label">收单机构参考号：</label>
							<div class="col-sm-2">
								<input type="text" class="form-control" name="acqReferenceNo"
									id="acqReferenceNo">
							</div>
							
							<label class="col-sm-2 control-label">平台收单机构参考号：</label>
							<div class="col-sm-2">
								<input type="text" class="form-control"
									name="plateAcqReferenceNo" id="plateAcqReferenceNo">
							</div>

							<label class="col-sm-2 control-label">平台商户号：</label>
							<div class="col-sm-2">
								<input type="text" class="form-control" name="plateMerchantNo"
									id="plateMerchantNo">
							</div>

							
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">平台终端号：</label>
							<div class="col-sm-2">
								<input type="text" class="form-control" name="plateTerminalNo"
									id="plateTerminalNo">
							</div>
							<label class="col-sm-2 control-label">平台交易订单号：</label>
							<div class="col-sm-2">
								<input type="text" class="form-control"
									name="plateTransId" id="plateTransId">
							</div>
							<label class="col-sm-2 control-label">平台交易类型：</label>
							<div class="col-sm-2">
								<select class="form-control" name="plateTransType"> 
                                    <option value="ALL" selected="selected">全部</option>
                                   	<c:forEach var="plateTransType" items="${plateTransTypeList}">
                    				<option value="${plateTransType.sysValue}">${plateTransType.sysName}</option>
            		 				</c:forEach>
                               </select>  
							</div>
							
							
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label">平台交易状态：</label>
								<div class="col-sm-2">
									<select class="form-control" name="plateTransStatus"> 
	                                	<option value="ALL" selected="selected">全部</option>
	                                    <c:forEach var="plateTransStatus" items="${plateTransStatusList}">
	              						<option value="${plateTransStatus.sysValue}">${plateTransStatus.sysName}</option>
	              						</c:forEach>  
	                                </select>
								</div>
							<label class="col-sm-2 control-label">对账状态：</label>
							<div class="col-sm-2">
								<select class="form-control" name="checkAccountStatus"> 
                                	<option value="ALL" selected="selected">全部</option>
                                    <c:forEach var="checkAccountStatus" items="${checkAccountStatusList}">
              						<option value="${checkAccountStatus.sysValue}">${checkAccountStatus.sysName}</option>
              						</c:forEach>
                                </select>
							</div>
							
							<label class="col-sm-2 control-label">差错处理状态：</label>
							<div class="col-sm-2">
								<select class="form-control" name="errorHandleStatus"> 
                                    <option value="ALL" selected="selected">全部</option>
                                   	<c:forEach var="errorHandleStatus" items="${errorHandleStatusList}">
                    				<option value="${errorHandleStatus.sysValue}">${errorHandleStatus.sysName}</option>
            		 				</c:forEach>
                               </select>  
							</div>
							
						</div>



						<div class="clearfix lastbottom"></div>

						<div class="form-group">
								<label class="col-sm-2 control-label aaa"></label>
							<!-- <div class="col-sm-12 col-sm-offset-13  "> -->
								<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> 
								<button id="submitBut" type="submit" style="margin-right:20px" class=" btn btn-success" value=""><span class="glyphicon gly-search"></span>查询</button>
                                <button type="reset" class=" btn btn-default" value="" style="margin-right:20px"><span class="glyphicon gly-trash"></span>清空</button>
                                <sec:authorize access="hasAuthority('duiAccountErrorHandler:export')">
                                	<button id="export" type="button" class=" btn btn-info" style="margin-right:20px" value="" onclick="exportExcel()"><span class="glyphicon gly-out"></span>导出</button>
								</sec:authorize>
							<!-- </div> -->
						</div>
					<!-- <form class="form-horizontal" id="duiAccountOprationForm"> -->
						<div class="form-group" style="margin-bottom:0">
							<!-- <div class="col-sm-11 col-sm-offset-13  "> -->
								<!-- <button class="btn btn-info" type="button" id="platformForzen">平台单边对账冻结</button> -->
							<!-- </div>
							<div class="col-sm-2 col-sm-offset-0  "> -->
								<label class="col-sm-2 control-label aaa"></label>

								<sec:authorize access="hasAuthority('duiAccountErrorHandler:errorHandler')">
	
									<button class="btn btn-info2 " type="button" id="platFormThaw" style="margin-right:20px">平台单边正常解冻结算</button>
									<!-- </div>
								<div class="col-sm-2 col-sm-offset-0  "> -->
									<button class="btn btn-info2 " type="button" id="platformPayment" style="margin-right:20px">平台单边赔付商户</button>
									
									
								</sec:authorize>
							<!-- </div> -->
						</div>
						<div class="form-group" style="margin-bottom:0">
								<label class="col-sm-2 control-label aaa"></label>
								<button class="btn btn-info2 " type="button" id="upstreamRecordAccount" style="padding:6px 31.18px;margin-right:20px">上游单边补记账结算商户</button>
	
								<button class="btn btn-info2 " type="button" id="upstreamRefund" style="padding:6px 24.18px;margin-right:20px">上游单边退款给持卡人</button>
								
								<button class="btn btn-info2 " type="button" id="upstreamThaw" style="padding:6px 38.18px;margin-right:20px">上游单边确认是日切</button>
						</div>
					
					</form>
				</div>
			</div>
		</div>
		<div class="col-lg-12">
			<div class="ibox ">
				<div class="ibox-content" style="padding-top:5px">
					<div class="jqGrid_wrapper">
						<table id="table_list_dui_account"></table>
						<div id="pager_list_dui_account"></div>
					</div>
				</div>
			</div>
		</div>
						<!-- </form> -->

	</div>

	<div class="modal inmodal" id="myModalUpdateRemark" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
        	<div class="modal-content">
            	<div class="modal-header">
                	<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                	<h5 class="modal-title">修改</h5>
                </div>
                <div class="modal-body">
                	<form method="post" class="form-horizontal" id="updatePrivilegeForm">
					<div class="form-group">
						<label class="col-sm-3 control-label">备注：</label>
						<div class="col-sm-8">
							<input type="text" class="form-control" name="remark" id="remark" />
						</div>
					</div>
					</form>
         		</div>
				<div class="modal-footer">
					<input type="hidden" id="detailId"/>
            		<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            		<button type="button" class="btn btn-success" onclick="Save()">保存</button>
         		</div>
  			</div>
		</div>
	</div>

</body>

<title>
<script src="${ctx}/js/plugins/select2/select2.full.min.js"></script>
<script src="${ctx}/js/plugins/select2/i18n/zh-CN.js"></script> 
<!-- Sweet alert -->
<script src="${ctx}/js/plugins/sweetalert/sweetalert.min.js"></script>

	<script type="text/javascript">
	    var num = 0;//判断多选时的变量
		// 去除空格啊
        $('input').blur(function(){
            replaceSpace(this);
        })
		function replaceSpace(obj){
            obj.value = obj.value.replace(/\s/gi,'')
        }
        function chkNum(num){
       	    return (num%2 ==0) ?true:false;  //判断是否能整除2
	    }
	    function hideCheckBox(){
	       	 var rowIds = jQuery("#table_list_dui_account").jqGrid('getDataIDs');//获取jqgrid中所有数据行的id
				 for(var k=0; k<rowIds.length; k++) {
				  var curRowData = jQuery("#table_list_dui_account").jqGrid('getRowData', rowIds[k]);//获取指定id所在行的所有数据.
				  if("记账成功" != curRowData.recordStatus)
					var checkboxFailID = "jqg_table_list_dui_account_"+rowIds[k];
				    $("#"+checkboxFailID).hide();
				 }
	       	
	     }
		//============================================平台单边==========
		
		//平台单边正常解冻结算
		$("#platFormThaw").click(function() {
			//获取到所有选中的chekbox的值，即选中行的id值.	
			var s = jQuery("#table_list_dui_account").jqGrid('getGridParam', 'selarrrow');
			if(s==''){
				toastr.warning('你还没有选择任何内容！', '警告'); 
			}else{
				
				swal({  
				 	title: "确认是否解冻结算?", 
				   	type: "warning",  
				    showCancelButton: true,   
				    cancelButtonText: "取消",  
				   	confirmButtonColor: "#DD6B55",  
				    confirmButtonText: "继续解冻",  
				    closeOnConfirm: false 
				    }, 
				    function(){   
						var data = "" ;
						var ss = s.toString().split(',');
						for (var i in ss) {
							data += "selectId["+i+"]="+ss[i]+"&" ;
						}
						data += "${_csrf.parameterName}=${_csrf.token}" ;
						//alert(data) ;
					//访问后台
					$.ajax({
						url : "${ctx}/fastDuiAccount/platformThawSettle.do" ,
						data : data ,
						type : "POST" ,  
						success :function(msg){
							if(!msg.state){
					                // Display a success toast, with a title
					            toastr.error(msg.msg,'错误');
							}else{
								swal.close();
								toastr.success(msg.msg,'提示');
								//重新加载
								setTimeout(function(){
								  window.location.reload();
								},1000)
							}
						}
					}) ;
					swal.close();
				    });
			}
			
		});
		//平台单边赔付商户
		$("#platformPayment").click(function() {
			//获取到所有选中的chekbox的值，即选中行的id值.	
			var s = jQuery("#table_list_dui_account").jqGrid('getGridParam', 'selarrrow');
			if(s==''){
				toastr.warning('你还没有选择任何内容！', '警告');
			}else{
				
				swal({  
				 	title: "确认是否赔付商户?", 
				   	type: "warning",  
				    showCancelButton: true,   
				    cancelButtonText: "取消",  
				   	confirmButtonColor: "#DD6B55",  
				    confirmButtonText: "继续赔付",  
				    closeOnConfirm: false 
				    }, 
				    function(){   
						var data = "" ;
						var ss = s.toString().split(',');
						for (var i in ss) {
							data += "selectId["+i+"]="+ss[i]+"&" ;
						}
						data += "${_csrf.parameterName}=${_csrf.token}" ;
						//alert(data) ;
					//访问后台
					$.ajax({
						url : "${ctx}/fastDuiAccount/platformPayment.do" ,
						data : data ,
						type : "POST" ,  
						success :function(msg){
							if(!msg.state){
					                // Display a success toast, with a title
					            toastr.error(msg.msg,'错误');
							}else{
								
								toastr.success(msg.msg,'提示');
								//重新加载
								setTimeout(function(){
								  window.location.reload();
								},1000)
							}
							
						}
					}) ;
				  	    swal.close();
				    });
			}
			
		});
		
		//============================================上游单边==========
		
		//上游单边补记账结算商户
		$("#upstreamRecordAccount").click(function() {
			//获取到所有选中的chekbox的值，即选中行的id值.	
			var s = jQuery("#table_list_dui_account").jqGrid('getGridParam', 'selarrrow');
			if(s==''){
				toastr.warning('你还没有选择任何内容！', '警告'); 
			}else{
				
				swal({  
				 	title: "确认是否补记账结算商户?", 
				   	type: "warning",  
				    showCancelButton: true,   
				    cancelButtonText: "取消",  
				   	confirmButtonColor: "#DD6B55",  
				    confirmButtonText: "继续结算",  
				    closeOnConfirm: false 
				    }, 
				    function(){   
						var data = "" ;
						var ss = s.toString().split(',');
						for (var i in ss) {
							data += "selectId["+i+"]="+ss[i]+"&" ;
						}
						data += "${_csrf.parameterName}=${_csrf.token}" ;
						//alert(data) ;
						//访问后台
						$.ajax({
							url : "${ctx}/fastDuiAccount/acqSingleSettleToMerchant.do" ,
							data : data ,
							type : "POST" ,  
							success :function(msg){
								if(!msg.state){
					                // Display a success toast, with a title
					            	toastr.error(msg.msg,'错误');
					            	if (msg.result == 1) {
					            		swal({  
				 							title: "上游单边，交易系统未查询到对应的交易，请确认是否标记成已处理?", 
				   							type: "warning",  
				    						showCancelButton: true,   
				    						cancelButtonText: "取消",  
				   							confirmButtonColor: "#DD6B55",  
				    						confirmButtonText: "继续处理",  
				    						closeOnConfirm: false 
				    					}, 
				    					function(){   
											var data = "" ;
											var ss = s.toString().split(',');
											for (var i in ss) {
												data += "selectId["+i+"]="+ss[i]+"&" ;
											}
											data += "${_csrf.parameterName}=${_csrf.token}" ;
											data += "&type=upstreamRecordAccount";
											//访问后台
											$.ajax({
												url : "${ctx}/fastDuiAccount/markToHandled.do" ,
												data : data ,
												type : "POST" ,  
												success :function(msg){
													if(!msg.state){
					            						toastr.error(msg.msg,'错误');
													}else{
														toastr.success(msg.msg,'提示');
														//重新加载
														setTimeout(function(){
								  						window.location.reload();
														},1000)
													}
												}
											}) ;
				  	    
											swal.close();
				    						});
					            } else {
					           		swal.close();
					            }
							}else{
								swal.close();
								toastr.success(msg.msg,'提示');
								//重新加载
								setTimeout(function(){
								  window.location.reload();
								},1000)
							}
						}
					}) ;
				    });
			}
			
		});
		
		//上游单边退款给持卡人
		$("#upstreamRefund").click(function() {
			//获取到所有选中的chekbox的值，即选中行的id值.	
			var s = jQuery("#table_list_dui_account").jqGrid('getGridParam', 'selarrrow');
			if(s==''){
				toastr.warning('你还没有选择任何内容！', '警告');
			}else{
				
				swal({  
				 	title: "确认是否退款给持卡人?", 
				   	type: "warning",  
				    showCancelButton: true,   
				    cancelButtonText: "取消",  
				   	confirmButtonColor: "#DD6B55",  
				    confirmButtonText: "继续退款",  
				    closeOnConfirm: false 
				    }, 
				    function(){   
						var data = "" ;
						var ss = s.toString().split(',');
						for (var i in ss) {
							data += "selectId["+i+"]="+ss[i]+"&" ;
						}
						data += "${_csrf.parameterName}=${_csrf.token}" ;
						//alert(data) ;
					//访问后台
					$.ajax({
						url : "${ctx}/fastDuiAccount/acqSingleBackMoneyToOwner.do" ,
						data : data ,
						type : "POST" ,  
						success :function(msg){
							if(!msg.state){
					                // Display a success toast, with a title
					            toastr.error(msg.msg,'错误');
					            if (msg.result == 1) {
					            		swal({  
				 							title: "上游单边，交易系统未查询到对应的交易，请确认是否标记成已处理?", 
				   							type: "warning",  
				    						showCancelButton: true,   
				    						cancelButtonText: "取消",  
				   							confirmButtonColor: "#DD6B55",  
				    						confirmButtonText: "继续处理",  
				    						closeOnConfirm: false 
				    					}, 
				    					function(){   
											var data = "" ;
											var ss = s.toString().split(',');
											for (var i in ss) {
												data += "selectId["+i+"]="+ss[i]+"&" ;
											} 
											data += "${_csrf.parameterName}=${_csrf.token}" ;
											data += "&type=financialRefund";
											//访问后台
											$.ajax({
												url : "${ctx}/fastDuiAccount/markToHandled.do" ,
												data : data ,
												type : "POST" ,  
												success :function(msg){
													if(!msg.state){
					            						toastr.error(msg.msg,'错误');
													}else{
														toastr.success(msg.msg,'提示');
														//重新加载
														setTimeout(function(){
								  						window.location.reload();
														},1000)
													}
												}
											}) ;
				  	    
											swal.close();
				    						});
					            } else {
					           		swal.close();
					            }
							}else{
								swal.close();
								toastr.success(msg.msg,'提示');
								//重新加载
								setTimeout(function(){
								  window.location.reload();
								},1000)
							}
						}
					}) ;
				    });
			}
			
		});

		//上游单边确认是日切
		$("#upstreamThaw").click(function() {
			//获取到所有选中的chekbox的值，即选中行的id值.	
			var s = jQuery("#table_list_dui_account").jqGrid('getGridParam', 'selarrrow');
			if(s==''){
				toastr.warning('你还没有选择任何内容！', '警告');
			}else{
				
				swal({  
				 	title: "确认是否执行上游单边日切?", 
				   	type: "warning",  
				    showCancelButton: true,   
				    cancelButtonText: "取消",  
				   	confirmButtonColor: "#DD6B55",  
				    confirmButtonText: "继续",  
				    closeOnConfirm: false 
				    }, 
				    function(){   
						var data = "" ;
						var ss = s.toString().split(',');
						for (var i in ss) {
							data += "selectId["+i+"]="+ss[i]+"&" ;
						}
						data += "${_csrf.parameterName}=${_csrf.token}" ;
						//alert(data) ;
					//访问后台
					$.ajax({
						url : "${ctx}/fastDuiAccount/acqSingleThaw.do" ,
						data : data ,
						type : "POST" ,  
						success :function(msg){
							if(!msg.state){
					                // Display a success toast, with a title
					            toastr.error(msg.msg,'错误');
					            if (msg.result == 1) {
					            		swal({  
				 							title: "上游单边，交易系统未查询到对应的交易，请确认是否标记成已处理?", 
				   							type: "warning",  
				    						showCancelButton: true,   
				    						cancelButtonText: "取消",  
				   							confirmButtonColor: "#DD6B55",  
				    						confirmButtonText: "继续处理",  
				    						closeOnConfirm: false 
				    					}, 
				    					function(){   
											var data = "" ;
											var ss = s.toString().split(',');
											for (var i in ss) {
												data += "selectId["+i+"]="+ss[i]+"&" ;
											}
											data += "${_csrf.parameterName}=${_csrf.token}" ;
											data += "&type=financialRefund";
											//访问后台
											$.ajax({
												url : "${ctx}/fastDuiAccount/markToHandled.do" ,
												data : data ,
												type : "POST" ,  
												success :function(msg){
													if(!msg.state){
					            						toastr.error(msg.msg,'错误');
													}else{
														toastr.success(msg.msg,'提示');
														//重新加载
														setTimeout(function(){
								  						window.location.reload();
														},1000)
													}
												}
											}) ;
				  	    
											swal.close();
				    						});
					            } else {
					           		swal.close();
					            }
							}else{
								swal.close();
								toastr.success(msg.msg,'提示');
								//重新加载
								setTimeout(function(){
								  window.location.reload();
								},1000)
							}
						}
					}) ;
				    });
			}
			
		});

   		// Ajax 文件下载
		jQuery.download = function(url, data, method){
		    // 获得url和data
		    if( url && data ){ 
		        // data 是 string 或者 array/object
		        data = typeof data == 'string' ? data : jQuery.param(data);
		        // 把参数组装成 form的  input
		        var inputs = '';
		        jQuery.each(data.split('&'), function(){ 
		            var pair = this.split('=');
		            inputs+='<input type="hidden" name="'+ pair[0] +'" value="'+ pair[1] +'" />';
		        });
		        // request发送请求
		        jQuery('<form action="'+ url +'" method="'+ (method||'post') +'">'+inputs+'</form>')
		        .appendTo('body').submit().remove();
		    };
		};	
		$("#duiAccountForm").submit(function() {
			$("#table_list_dui_account").setGridParam({
				datatype : 'json',
				page : 1
			//Replace the '1' here
			}).trigger("reloadGrid");
			return false;
		});
   		/*表单提交时的处理*/
   		function exportExcel() {  
   			var data = $("#duiAccountForm").serialize();
   			//console.info(data);
   			$.download('${ctx}/fastDuiAccount/exportDuiAccount.do',data,'post');
   	    }
   		 	
		function customRecordStatusFormatter(cellvalue, options, rowObject){  
			<c:forEach var="checkAccountStatus" items="${recordStatusList}">
				  if(cellvalue == '${checkAccountStatus.sysValue}'){
					  return '${checkAccountStatus.sysName}';
				  }
			 </c:forEach>	
			 return "" ;
		}
		 
	 	function customCheckAccountStatusFormatter(cellvalue, options, rowObject){  
			<c:forEach var="checkAccountStatus" items="${checkAccountStatusList}">
				  if(cellvalue == '${checkAccountStatus.sysValue}'){
					  return '${checkAccountStatus.sysName}';
				  }
			 </c:forEach>	
			 return "" ;
		}
 
 		function customErrorHandleStatusFormatter(cellvalue, options, rowObject){  
			<c:forEach var="errorHandleStatus" items="${errorHandleStatusList}">
				  if(cellvalue == '${errorHandleStatus.sysValue}'){
					  return '${errorHandleStatus.sysName}';
				  }
			 </c:forEach>	
			 return "" ;
		} 
 		
 		function customPlateTransStatusFormatter(cellvalue, options, rowObject){  
 			<c:forEach var="plateTransStatus" items="${plateTransStatusList}">
 				  if(cellvalue == '${plateTransStatus.sysValue}'){
 					  return '${plateTransStatus.sysName}';
 				  }
 			 </c:forEach>	
 			 return "" ;
 		}
 		
 		function customPlateTransTypeFormatter(cellvalue, options, rowObject){  
 			<c:forEach var="plateTransType" items="${plateTransTypeList}">
 				  if(cellvalue == '${plateTransType.sysValue}'){
 					  return '${plateTransType.sysName}';
 				  }
 			 </c:forEach>	
 			 return "" ;
 		}
 		
 		function Modify(id) {   //单击修改链接的操作         
			var model = $("#table_list_dui_account").jqGrid('getRowData',id);
			
			$("#remark").val(model.remark); 
			$("#detailId").val(model.id);
		}
 		
 		function Save(id) {    
			var remark = $("#remark").val();
			var _id =  $("#detailId").val();
			
			$.post('${ctx}/fastDuiAccount/updateRemark.do', 
						{ id:_id,remark:remark,'${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) {
							if(!msg.status){
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
								$("#myModalUpdateRemark").modal("hide");
								$("#table_list_dui_account").jqGrid('setGridParam',{
							           url : "${ctx}/fastDuiAccount/findErrorDuiAccountDetailList.do",
							           datatype : 'json',
							        }).trigger('reloadGrid');//重新载入
							}
						});
		}
		
 		function getParams(o) {
			var data = $("#duiAccountForm").serializeArray();
			$.each(data, function() {
				o[this.name] = this.value || '';
			});
		}

		$(document).ready(function() {
							var lastsel;
							//var data=$("#duiAccountForm").serialize();
							//alert(data);
							// 初始化表格
							$("#table_list_dui_account").jqGrid({
									url : "${ctx}/fastDuiAccount/findErrorDuiAccountDetailList.do",
									datatype : "json",
									mtype: "POST",
					                height:"auto",
									autowidth: true,
									shrinkToFit: false,
									autoScroll: false,
									multiselect: true,
									rowNum: 10,
									rowList: [ 10, 20 ],
									colNames: ['操作', 'ID','对账批次号','收单机构订单号', '收单机构交易订单号', 
												'收单机构交易金额','收单机构退货金额', '收单机构对账日期',
												'收单机构英文名称','平台订单号','平台收单机构商户号', '平台商户号',
												'平台交易金额','平台收单机构商户手续费',
												'平台商户手续费', '平台交易类型',
												'平台交易状态', '平台交易订单号','对账状态','记账状态','创建时间','差错处理状态' ,'差错处理人' ,'原因','备注'],
									colModel: [
												{name:'Detail',index:'id',width:100,align:"center",sortable:false,frozen:true},
												{name : 'id',index : 'id',width : 120,align : "left"},
												{name : 'checkBatchNo',index : 'checkBatchNo',width : 190,align : "left"},
												{name: 'acqOrderNo', index: 'checkBatchNo', width: 190, align: "center"},
                                     			{name: 'acqTransOrderNo', index: 'checkBatchNo', width: 200, align: "center"},
												{name : 'acqTransAmount',index : 'acqTransAmount',width : 140,align : "right",formatter: 'number'},
												{name : 'acqRefundAmount',index : 'acqRefundAmount',width : 150,align : "right",formatter: 'number'},
												{name : 'acqCheckDate',index : 'acqCheckDate',width : 180,align : "center",
													formatter : function(val) {
														return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");
													}},
												{name : 'acqEnname',index : 'acqEnname',width : 150,align : "left", sortable:false},
												{name: 'plateOrderNo', index: 'checkBatchNo', width: 190, align: "center"},
												{name : 'plateAcqMerchantNo',index : 'plateAcqMerchantNo',width : 150,align : "left", sortable:false},
												{name : 'plateMerchantNo',index : 'plateMerchantNo',width : 150,align : "left", sortable:false},
												{name : 'plateTransAmount',index : 'plateTransAmount',width : 120,align : "right",formatter: 'number'},
												{name : 'plateAcqMerchantFee',index : 'plateAcqMerchantFee',width : 180,align : "right",formatter: 'number'},
												{name : 'plateMerchantFee',index : 'plateMerchantFee',width : 120,align : "right",formatter: 'number'},
												{name : 'plateTransType',index : 'plateTransType',width : 120,align : "center", sortable:false,formatter : customPlateTransTypeFormatter},
												{name : 'plateTransStatus',index : 'plateTransStatus',width : 120,align : "center", sortable:false,formatter : customPlateTransStatusFormatter},
												{name : 'plateTransId',index : 'plateTransId',width : 120,align : "center", sortable:false},
												{name : 'checkAccountStatus',index : 'checkAccountStatus',width : 120,align : "center", sortable:false,formatter : customCheckAccountStatusFormatter},
												{name : 'recordStatus',index : 'recordStatus',width : 120,align : "center", sortable:false,formatter : customRecordStatusFormatter},
												{name : 'createTime',index : 'createTime',width : 180,align : "center",
													formatter : function(val) {
														return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");
													}},
												{name : 'errorHandleStatus',index : 'errorHandleStatus',width : 200,align : "center", sortable:false,formatter : customErrorHandleStatusFormatter},
												{name : 'errorHandleCreator',index : 'errorHandleCreator',width : 100,align : "left", sortable:false},
												{name : 'errorMsg',index : 'errorMsg',width : 260,align : "center", sortable:false},
												{name : 'remark',index : 'remark',width : 330,align : "center", sortable:false},
										],
										multiselectWidth : 100,
										multiboxonly : false ,
										pager : "#pager_list_dui_account",
										viewrecords : true,
										hidegrid : false,
										reloadAfterSubmit: true,
										jsonReader : {
											root : "result",
											total : "totalPages",
											page : "pageNo",
											pageSize : "pageSize",
											records : "totalCount",
											repeatitems : false
										},
										prmNames : {
											page : "pageNo",
											rows : "pageSize"
										},
										serializeGridData : function(postData) {
											getParams(postData);
											return postData;
										},
										gridComplete:function(){  
                    						var ids=$("#table_list_dui_account").jqGrid('getDataIDs');
                    						for(var i=0; i<ids.length; i++){
                    							var id=ids[i];  
                        						var getRow = $('#table_list_dui_account').getRowData(id);//获取当前的数据行
                        						var detail = "<a href='javascript:void(0)' class='default-details' data-toggle='modal' data-target='#myModalUpdateRemark' onclick='Modify(" + id + ")' title='备注'>备注</a>";
                        						jQuery("#table_list_dui_account").jqGrid('setRowData', ids[i], { Detail: detail });
                    						}
                    						hideCheckBox();
                    					},
                    					onSelectAll:function(rowid, status) { //点击全选时触发事件
                   						  var rowIds = jQuery("#table_list_dui_account").jqGrid('getDataIDs');//获取jqgrid中所有数据行的id
                   						    for(var k=0; k<rowIds.length; k++) {
                   						     var curRowData = jQuery("#table_list_dui_account").jqGrid('getRowData', rowIds[k]);//获取指定id所在行的所有数据.
                   						     if("记账成功" != curRowData.recordStatus)
                   						       $("#table_list_dui_account").jqGrid("setSelection", rowIds[k],false);//设置改行不能被选中。
                   						    }
                   						    if(!chkNum(num)){
                   							  $("#table_list_dui_account").resetSelection();
                   						     }
                   						     num ++;
                   					     } ,
                   					     onSelectRow:function(id){//选择某行时触发事件
                   					      var curRowData = jQuery("#table_list_dui_account").jqGrid('getRowData', id);
                   					      if("记账成功" != curRowData.recordStatus)
                   					        $("#table_list_dui_account").jqGrid("setSelection", id,false);
                   				         }
									});
									jQuery("#table_list_dui_account").jqGrid('setFrozenColumns');
							// Add responsive to jqGrid
							$(window).bind('resize',function() {
										var width = $('.jqGrid_wrapper').width();
										$('#table_list_dui_account').setGridWidth(width);
									});

						});
	</script></title>
</html>
