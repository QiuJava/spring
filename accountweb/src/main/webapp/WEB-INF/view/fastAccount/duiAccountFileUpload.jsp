
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
	<head>
		<link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
		<link href="${ctx}/css/plugins/ladda/ladda-themeless.min.css" rel="stylesheet">
	</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">对账管理</div>
            <em class=""></em>
            <div class="pull-left active">快捷对账上传</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
				<div class="ibox-content">
					 <form class="form-horizontal" id="form1">
					 
                          <div class="form-group">
                          		   <label class="col-sm-2 control-label">收单机构：</label>
                                   <div class="col-sm-2">
										<select class="form-control" name="acqOrg" id="acqOrg"> 
									         <c:forEach var="acqOrg" items="${acqOrgList}">
												<option value="${acqOrg.acqEnname}">
													${acqOrg.acqEnname}
												</option>
											</c:forEach>
										</select>      
									</div>
                                  
                                    <label class="col-sm-1 control-label">文件名：</label>
                                   <div class="col-sm-6">
										<input type="file" id="fileupload"  name="fileupload"  data-url="${ctx}/fastDuiAccount/duiAccountFileUpload.do" data-badge="false" >
									</div>									  														  
						</div>
						
						
					
                          
                             <div class="clearfix lastbottom"></div>
                                
                                   <div class="form-group">
                                        <label class="col-sm-2 control-label aaa"></label>
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                   <%-- <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> --%>
                                   <sec:authorize access="hasAuthority('fastDuiAccountFileUpload:insert')">
                                       <button type="button" class=" btn btn-success" id="btnSubmit" value=""><span class="glyphicon gly-ok"></span>提交</button>
                                   </sec:authorize>
                                   <!-- </div> -->
                              	   </div>
                            <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
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
	<script src="${ctx}/js/plugins/sweetalert/sweetalert.min.js"></script> 
	<script src="${ctx}/js/plugins/ladda/spin.min.js"></script>
	<script src="${ctx}/js/plugins/ladda/ladda.min.js"></script>
	<script type="text/javascript">
		function thenfileupload(){
			$('#fileupload').fileupload({
		        dataType: 'json',
		        add: function (e, data) {
                    var Filename = data.files[0].name;
                    $('.bootstrap-filestyle input').val(Filename);
		        	data.headers={'X-CSRF-TOKEN' : "${_csrf.token}"};
		            data.context = $("#btnSubmit").off('click').on('click', function () {
		                data.submit();
		            });
		        },
		        done: function (e, data) {
		        	if(data.result.statu){
		        		toastr.success(data.result.msg, "提示");
		        	}else{
		        		toastr.error(data.result.msg, "错误");
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
		
	</script>
	 </title>
</html>  
      