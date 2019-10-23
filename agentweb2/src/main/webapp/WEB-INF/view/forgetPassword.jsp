<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

 <c:if test="${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal != null}">
    <c:redirect url="welcome.do"/>
</c:if>   
    
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>忘记密码</title>

    <link href="${ctx}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${ctx}/font-awesome/css/font-awesome.css" rel="stylesheet">

    <link href="${ctx}/css/animate.css" rel="stylesheet">
    <link href="${ctx}/css/style.css" rel="stylesheet">
	<style>
		.loginbg{background-image: url("${ctx}/img/oemloginbg.png");}
        .loginColumns{ padding:100px 0 0;}
        .loginColumns h2{ color:#fff; font-size:30px; text-align:center; margin:0 0 64px; line-height:30px;}
	</style>
</head>
<script type="text/javascript">
function sendMsg(){
	var phoneNum = $("#phoneNum").val();
	if(phoneNum){
		console.log(99)
// 		$.ajax({
// 			dataType:"json",
// 			type:"POST",
// 			url:"/sendMsg.do",
// 			data:{phoneNum:phoneNum},
// 			success:function(data){
// 				if(data.success){
// 					var sec=5;
// 					var timer=vindow.setInterval(function(){
// 						sec--;
// 						if(sec==0){
// 							alert(0)
// 							window.clearInterval(timer);
// 						}
// 					})
// 				}
// 			}
// 		})
	}
}
</script>

<body class="loginbg">
<script src="js/jquery/jquery-2.1.1.min.js"></script>
    <div class="row login-bg">
    <div class="loginColumns animated fadeInDown">
    <h2 class="">欢迎登录盛钱包代理商系统</h2>
        <div class="row loginbox">
            <div class="lg-left">
                <h3>修改密码</h3>
                <c:url value="/perform_login.do" var="loginUrl" />
                    <form action="${loginUrl}" method="post">
                        
                        
                        <div class="form-group">
                            <input type="text" class="form-control" id="phoneNum" placeholder="请输入手机号" required=""  name="phoneNum">
                        </div>
                        <div class="form-group">
                            <input type="text" class="form-control" placeholder="请输入验证码" name="code">
                            <input type="button" class="btn btn-new lg-btn" onclick="sendMsg()" value="点击获取验证码"/>
                            <input type="hidden" name="teamid" value="${teamId }">
                        </div>
                        <div class="form-group forget-ps">
                             <a href="oemlogin.do">又想起来了>></a>
                        </div>
                        <sec:csrfInput/>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                        <button type="submit" class="btn btn-new lg-btn" >确定</button>

                    </form>
            </div>
            <div class="lg-right">
                <img src="${ctx}/img/s-code.png">
                <p>盛代宝app下载</p>
            </div>
        </div>

    </div>
    </div>
    <div class="loginBottom" style=" position:fixed; bottom:18px; width:1000px; left:50%; margin-left:-500px;">
        <div class="col-md-6" style="color:#505050; display:none">
        <!-- Copyright 深圳市移付宝科技有限公司.   All Rights Reserved.   -->
        </div>
        <div style="color:#fff; text-align:center; font-size:14px;">
        <small>粤ICP备15076873号-1 © 2015-2016</small>
        </div>
    </div>
</body>

</html>

