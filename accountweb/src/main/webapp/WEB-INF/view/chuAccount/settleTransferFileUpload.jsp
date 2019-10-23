
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	    <!-- Sweet Alert -->
    <link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">

</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">出账管理</div>
            <em class=""></em>
            <div class="pull-left active">结算转账文件上传</div>
        </div>	
	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
				<div class="ibox-content">
					 <form class="form-horizontal" id="form1">
					 
                          <div class="form-group">
                          		   <label class="col-sm-2 control-label">结算通道：</label>
                                   <div class="col-sm-2">
										<select class="form-control" name="settleBank" id="settleBank"> 
									         <c:forEach var="settleBank" items="${settleBankList}">
												<option value="${settleBank.sysValue}">
													${settleBank.sysName}
												</option>
											</c:forEach>
										</select>      
									</div>
                                  
                                  	<label class="col-sm-1 control-label">文件名：</label>
                                   <div class="col-sm-6">

										<input type="file" id="fileupload"  name="fileupload"  data-url="${ctx}/chuAccountAction/settleTransferFileUpload.do" data-badge="false" >
									</div>							  									
													  
						</div>
					    <div class="form-group">
                            <label class="col-sm-2 control-label">摘要：</label>
                                   <div class="col-sm-2">
                                        <input type="text" id="summary"  name="summary"  class="form-control" >
                                    </div>                 
                        </div>
                          
                             <div class="clearfix lastbottom"></div>
                                
                                   <div class="form-group">
                                        <label class="col-sm-2 control-label aaa"></label>
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                   <%-- <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> --%>
                                       <button type="button" class=" btn btn-success" id="btnSubmit" value=""><span class="glyphicon gly-ok"></span>提交</button>
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
	<!-- Sweet alert -->
    <script src="${ctx}/js/plugins/sweetalert/sweetalert.min.js"></script>
	
	<script type="text/javascript">
		function thenfileupload(){
			$('#fileupload').fileupload({
		        dataType: 'json',
		        add: function (e, data) {
		        	data.headers={'X-CSRF-TOKEN' : "${_csrf.token}"};
		            data.context = $("#btnSubmit").off('click').on('click', function () {
		                data.submit();
		            });
		        },
		        done: function (e, data) {
		        	//console.info(data.result);
		        	if(!data.result.state){
		        		toastr.error(data.result.msg,'错误');
		        	}else{
		        		thenswal(data.result.msg);
		        	}
		        }
		    });
		}
		$(function () {
			$('#fileupload').filestyle({
				buttonText : '上传文件',
				buttonName : 'btn-primary'
			});
			thenfileupload();		    
		});
		
		function thenswal(msg){
			swal({  
			 	title: "是否继续保存?", 
			   	text: msg,   
			   	type: "warning",  
			    showCancelButton: true,   
			    cancelButtonText: "取消",  
			   	confirmButtonColor: "#DD6B55",  
			    confirmButtonText: "继续保存",  
			    closeOnConfirm: false 
			    }, 
			    function(){   
			    	// ajax post handler
			    	$.post('${ctx}/chuAccountAction/saveSettleTransFileUpload.do', 
						{ '${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) {
							if(!msg.state){
								//alert(msg.msg);
					                // Display a success toast, with a title
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
							}
							swal.close();
						});
			    });
		}
		
	</script>
	 </title>
</html>  
      