<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

 <c:if test="${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal != null}">
    <c:redirect url="welcome.do"/>
</c:if>   
    
<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>登录</title>

    <link href="${ctx}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${ctx}/font-awesome/css/font-awesome.css" rel="stylesheet">

    <link href="${ctx}/css/animate.css" rel="stylesheet">
    <link href="${ctx}/css/style.css" rel="stylesheet">
	<script src="${ctx}/js/jquery-2.1.1.js"></script>
</head>

<body class="gray-bg">
    <div class="row login-bg">
        <h2 class="font-bold" style="color:#fff;width:1200px; margin:0 auto;padding:65px 0 0 100px;">欢迎登录账户及清结算系统</h2>
        <div class="loginColumns animated fadeInDown">
            <div class="row">
                <div class="col-md-8">
                    <div></div>
                </div>
                <div class="col-md-4">
                    <div class="ibox-content" style="border-radius:10px;width:320px;margin-top:90px">
                        <c:url value="/perform_login.do" var="loginUrl" />
                        <form action="${loginUrl}" method="post">
                            <c:if test="${param.logout != null}">
                            <p>你已经退出系统.</p>
                            </c:if>
                            <c:if test="${param.expired != null}">
                            <p>你的会话已经过期.</p>
                            </c:if>
                            <c:if test="${param.otherlogin != null}">
                            <p>你的账号在其他地方登录,你被迫退出.</p>
                            </c:if>
                            <c:if test="${param.error != null}">
                                <c:choose>
                                     <c:when test="${SPRING_SECURITY_LAST_EXCEPTION.message == 'blocked'}">
                                        <p>登录错误太多,已锁定IP,请稍后再试.</p>
                                     </c:when>
                                     <c:when test="${SPRING_SECURITY_LAST_EXCEPTION.message == 'UsernameNotFound'}">
                                        <p>用户名不存在.</p>
                                     </c:when>
                                     <c:when test="${SPRING_SECURITY_LAST_EXCEPTION.message == 'UserIsDisabled'}">
                                        <p>账户被禁用.</p>
                                     </c:when>
                                     <c:otherwise>
                                        <p>用户名或密码错误.</p>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                            <div class="form-group">
                                <input type="text" class="form-control" placeholder="请输入用户名" required=""  name="username">
                            </div>
                            <div class="form-group">
                                <input type="password" class="form-control" placeholder="请输入密码" required="" name="password">
                            </div>
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            <button type="submit" id="loginBtn" class="btn btn-primary block full-width m-b">登录</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        
            
        </div>
        <div class="row login-nav">
            <!-- <div class="container"> -->
                <div class="col-md-6" style="color:#fff;padding-left:30px">
                  
                </div>
                <div class="col-md-6 text-right" style="color:#fff">
                   
                </div>
                <br />
                <div>&nbsp;</div>
            <!-- </div> -->
        </div>
        
        <script type="text/javascript">
            // if (window.screen.width == 1920) {
            //     $('#logindev').css({
            //         'top' : '49%',
            //         'left' : '65.5%',
            //         'transform' : 'translate(-34.5%, -51%);'
            //     })
            // };
            
            $(document).keyup(function(event){
			  if(event.keyCode ==13){
			    //$("#loginBtn").trigger("click");
			    $('form').submit();
			  }
			});
        </script>
</body>

</html>

