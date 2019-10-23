<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">内部账号管理</div>
            <em class=""></em>
            <div class="pull-left active">开立内部账户</div>
		</div>
	</div>
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
                            <form method="post" id="insAccountForm" class="form-horizontal">
  									<div class="form-group">
                                    <label class="col-sm-2 control-label">支付机构:</label>                               
									<div class="col-sm-2">	
									
													
   											 <select class="form-control" name="orgNo"> 
     											<c:forEach var="orgInfo" items="${orgInfoList}">
													<option value="${orgInfo.orgNo}">${orgInfo.orgName}&nbsp;${orgInfo.orgNo}</option> 
												</c:forEach>	
											</select>
 									 </div>	
 									</div>
                                    <div class="clearfix lastbottom"></div>
                                <div class="form-group">
                                        <label class="col-sm-2 control-label aaa"></label>
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                        <sec:authorize access="hasAuthority('createInsAccount:insert')">        
                                            <button class="ladda-button btn btn-success" type="button" data-style="expand-left"><span class="glyphicon gly-open"></span>开立账户</button>
                                        </sec:authorize>
                                </div>
                                <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
 							</form>
				</div>	
			</div>	
		</div>
	</div>
	<!-- 填充内容结束 -->
	<title>
	    <!-- Ladda style -->
    <link href="${ctx}/css/plugins/ladda/ladda-themeless.min.css" rel="stylesheet">
	
	    <!-- Ladda -->
    <script src="${ctx}/js/plugins/ladda/spin.min.js"></script>
    <script src="${ctx}/js/plugins/ladda/ladda.min.js"></script>
    <script src="${ctx}/js/plugins/ladda/ladda.jquery.min.js"></script>
	
		<script type="text/javascript">
		$(document).ready(function() {
			var l = $( '.ladda-button' ).ladda();

	        l.click(function(){
	            // Start loading
	            l.ladda( 'start' );
	            // Timeout example
	            $("#insAccountForm").submit();
	            // Do something in backend and then stop ladda
	            setTimeout(function(){
	                l.ladda('stop');
	            },500)
	        });
		});
		
		$("#insAccountForm").submit(function(){
			var data=$("#insAccountForm").serialize();
			//console.info(data);
			$.ajax({
				url:"${ctx}/insAccountAction/createInsAccount.do",
				type:"POST",
				data:data,
				success :function(msg){
					if(!msg.status){
						//alert(msg.msg);
			                // Display a success toast, with a title
			            toastr.error(msg.msg,'错误');
					}else{
						toastr.success(msg.msg,'提示');
					}
				}
			});
			return false;
		});
		
	</script>
		</title>
</body>


 