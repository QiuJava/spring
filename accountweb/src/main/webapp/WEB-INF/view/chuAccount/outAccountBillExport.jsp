<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">出账管理</div>
            <em class=""></em>
            <div class="pull-left active">出账单导出</div>
		</div>
	</div>
	
	
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox-content">
				 <form class="form-horizontal" id="form1">
                      <div class="form-group">
                  		   <label class="col-sm-2 control-label">出款通道：</label>
                           <div class="col-sm-2">
								<select class="form-control" name="settleBank" id="settleBank"> 
							         <c:forEach var="acqOrg" items="${acqOrgList}">
										<option value="${acqOrg.sysValue}">
											${acqOrg.sysName}
										</option>
									</c:forEach>
								</select>      
							</div>
						</div>
                        <div class="clearfix lastbottom"></div>
						<div class="form-group">
                            <label class="col-sm-2 control-label aaa"></label>
							<!-- <div class="col-sm-12 col-sm-offset-13  "> -->
							<%-- <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> --%>
							   <button type="button" class=" btn btn-success" id="btnSubmit" onclick="exportFile();" value="">上传FTP</button>
							   <button id="returnUp" type="button" class=" btn btn-default col-sm-offset-14" onclick="goBack();" value=""><span class="glyphicon gly-return"></span>返回</button>
							<!-- </div> -->
						</div>
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
		        	if(!data.result.success){
		        		toastr.error(data.result.msg,'错误');
		        	}else{
		        		//thenswal(data.result.msg);
		        		toastr.success(data.result.msg,'提示');
		        	}
		        }
		    });
		}
		function goBack(){
			window.location.href='${ctx}/chuAccountAction/toChuAccountBillManage.do';
		}
		function exportFile() {
	        var out_bill_id = '${outBill.id}';
	        var settleBank = $("#settleBank option:selected").val();
	        var data = {'outBillId':out_bill_id,'${_csrf.parameterName}':'${_csrf.token}','settleBank':settleBank};
   			//console.info(data);
   			$.ajax({
   				type: "post",
   				url: "${ctx}/chuAccountAction/billExport.do",
   				data: data,
   				dataType: "json",
   				success: function(result) {
   					if (result.success) {
   						swal({title:"提示" ,text:result.msg ,animation:"slide-from-top"});
                            $('.confirm').click(function(event) {
                                location.href = '${ctx}/chuAccountAction/toChuAccountBillManage.do';
                            });
   					} else {
   						swal({title:"提示" ,text:result.msg ,animation:"slide-from-top"});
   					}
   				}
   			});
		}
	</script>
	</title>
</html>