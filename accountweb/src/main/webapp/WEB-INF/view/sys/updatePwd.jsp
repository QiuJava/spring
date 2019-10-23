<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>  
<c:set var="ctx" value="${pageContext.request.contextPath}" /> 
<!DOCTYPE html>
<html>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
        <div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">系统管理</div>
            <em class=""></em>
            <div class="pull-left active">修改密码</div>
        </div>
	</div>
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
                         <form method="post" id="updatePwdForm" class="form-horizontal">
							<div class="form-group">
                                <label class="col-sm-2 control-label">用户名：</label>
								<div class="col-sm-3" style="padding-top: 7px;">
									<sec:authentication property="principal.realName" />
								</div>
                             </div>
                             <div class="form-group">
                             	<label class="col-sm-2 control-label">旧密码：</label>
								<div class="col-sm-3">
									<input type="password" class="form-control" name="oldPass" id="oldPass" placeholder="请输入旧密码" required>
								</div>
                             </div>
                             <div class="form-group">
                             	<label class="col-sm-2 control-label">密码：</label>
								<div class="col-sm-3">
									<input type="password" class="form-control" name="password" id="password" placeholder="请输入密码" required>
								</div>
                             </div>
                             <div class="form-group lastbottom">
                             	<label class="col-sm-2 control-label">再次密码：</label>
								<div class="col-sm-3">
									<input type="password" class="form-control" name="password2" id="password2" placeholder="请再次输入密码" required>
								</div>
                             </div>
                             <div class="form-group">
	                             <div class="col-sm-3  col-sm-offset-2">
	                             		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	                             		<sec:authorize access="hasAuthority('updatePwd:update')">
	                                     	<button class="btn btn-success" type="submit" data-style="expand-left">修改密码</button>
	                                     </sec:authorize>
	                             </div>
                             </div>
                             <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
						</form>
				</div>	
			</div>
		</div>
	</div>
	<!-- 填充内容结束 -->
	<title>
		<script type="text/javascript">
        //去除空格啊
        $('input').blur(function(){
            replaceSpace(this);
        })
        function replaceSpace(obj){
            obj.value = obj.value.replace(/\s/gi,'')
        }
		$("#updatePwdForm").submit(function(){
			var data=$("#updatePwdForm").serialize();
			//console.info(data);
			$.ajax({
				url:"${ctx}/sysAction/updatePwd.do",
				type:"POST",
				data:data,
				success :function(msg){
					if(!msg.state){
						toastr.error(msg.msg,'错误');
                        $("input[type='password']").val('');
					}else{
						toastr.success(msg.msg,'提示');
                        $("input[type='password']").val('');
					}
				}
			});
			return false;
		});
		
	</script>
		</title>
</body>


 