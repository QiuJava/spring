<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<link href="${ctx}/css/plugins/select2/select2.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/select2/select2-skins.min.css" rel="stylesheet">
	<link rel="stylesheet" href="${ctx}/css/icheck-css/custom.css" />
	<link rel="stylesheet" href="${ctx}/css/skins/all.css" />
	 <!-- Sweet Alert -->
    <link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
    <link href="${ctx}/css/plugins/sweetalert/jquery.fileupload.css" rel="stylesheet"/>
    <link href="${ctx}/css/showLoading.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
                <div class="pull-left">当前位置</div>
                <em class=""></em>
                <div class="pull-left">代理商分润管理</div>
                <em class=""></em>
                <div class="pull-left active">代理商分润批量预冻结</div>
        </div>
	</div>
	    <!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
				<div class="ibox-content">
					 <form class="form-horizontal" id="form1">
					 
                          <div class="form-group">
                                   <label class="col-sm-1 control-label">文件名：</label>
                                   <div class="col-sm-6">
                                        <input type="file" id="fileupload"  name="fileupload"  data-url="${ctx}/agentsProfitAction/batchPreFreezeFileUpload.do" data-badge="false" >
                                    </div>  
						</div>
                             <div class="clearfix lastbottom"></div>
                                
                                   <div class="form-group">
                                        <label class="col-sm-1 control-label aaa"></label>
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                   <%-- <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> --%>
                                   
                                   <sec:authorize access="hasAuthority('agentsProfitPreFreezeQuery:downLoad')">
                                   <button class="btn btn-primary btn-xs " type="button" id="btnDownload"><span class="glyphicon glyphicon-send" aria-hidden="true"></span>&nbsp;下载模板</button>
                                   </sec:authorize>
                                   
                                    <sec:authorize access="hasAuthority('agentsProfitPreFreezeQuery:upload')"> 
                                       <button type="button" class=" btn btn-success" id="btnSubmit" value=""><span class="glyphicon gly-ok"></span>提交</button>
                                    </sec:authorize> 
                                   <button id="returnUp" type="button" class=" btn btn-default  col-sm-offset-14" onclick="javascript:history.back(-1);" value="" /><span class="glyphicon gly-return"></span>返回</button>
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
	
	<script src="${ctx}/js/jquery.showLoading.js"></script>
	<script src="${ctx}/js/jquery.showLoading.min.js"></script>
	
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
	        inputs+='<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />'; 
	        // request发送请求
	        jQuery('<form action="'+ url +'" method="'+ (method||'post') +'">'+inputs+'</form>')
	        .appendTo('body').submit().remove();
	    };
	};
	
	$('#btnDownload').bind('click', function() {
		 $.download('${ctx}/agentsProfitAction/DownloadBacthFreezeTemplate.do','find=commoncode','post');
	});
	
    function thenfileupload(){
            $('#fileupload').fileupload({
                dataType: 'json',
                add: function (e, data) {
                    var Filename = data.files[0].name;
                    $('.bootstrap-filestyle input').val(Filename);
                    console.log()
                    
                    data.headers={'X-CSRF-TOKEN' : "${_csrf.token}"};
                    data.context = $("#btnSubmit").off('click').on('click', function () {
                        data.submit();
                        $("body").showLoading();
                    });
                },
                done: function (e, data) {
                     $("body").hideLoading();
                    if(data.result.status){
                    	$("body").hideLoading();
                        toastr.success(data.result.msg, "提示");
                        //toastr.success(msg.msg,'提示1');
                    }else{
                    	$("body").hideLoading();
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