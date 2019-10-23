<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="cn.eeepay.framework.util.OemTypeEnum" %>
<%@ page import="cn.eeepay.boss.security.ClientTeamIdUtil" %>
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
        <style>
            .loginColumns{ padding:100px 0 0;}
        </style>
        <script src="js/jquery/jquery-2.1.1.min.js"></script>
        <script src="js/angular/angular.min.js"></script>


        <script src="js/jsencrypt/jsencrypt.min.js"></script>
        <script type="text/javascript">
            var publicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJ9s1qlOyv9qpuaTqauW6fUftzE50rVk3yVPZwv1aO1Ch/XSEz76xCwkyvqpaqceRXrPpdBmO5+ruJ+I8osOHo7L5GWEOcMOO+8izp9hXKBBrmRMD4Egpn00k9DhVIEKp/vyddZPS/doxB8onhN6poTJDLdFLFVEicMf52caN9GQIDAQAB";
            $(function() {
                $("#submit_id").click(
                    function() {
                        var encrypt = new JSEncrypt();
                        encrypt.setPublicKey(publicKey);
                        var password =  $("#password").val();
                        var newPasword =  encrypt.encrypt(password);
                        $("#password").val(newPasword);
                        console.info(newPasword);
                    });
            });
        </script>
    </head>

    <body class="loginbg">
        <div class="row login-bg">
            <div class="loginColumns animated fadeInDown">
                <h2 class="" style="color:#3baaff; margin-bottom:20px;">欢迎登录移联支付代理商系统</h2>
                <div class="row">
                    <div id="loginDiv" class="col-md-5" style="float: right;margin-right:50px;width:370px;">
                        <div class="row findPwdbox" style="border:none; border-radius:5px;padding: 30px 50px 0px 50px;">
                            <p style="text-align: center;margin-bottom:25px;color: #2ab7f9;font-size: 20px;font-weight: normal;">登陆</p>
                            <c:url value="/perform_login.do" var="loginUrl" />
                            <form action="${loginUrl}" method="post">
                                <div class="form-group">
                                    <input type="text" class="form-control" placeholder="请输入用户名" required=""  name="username">
                                </div>
                                <div class="form-group">
                                    <input id="password" autocomplete="off" type="password" class="form-control" placeholder="请输入密码" required="" name="password">
                                    <input type="hidden" name="teamid" id="teamid" value="${teamId }">
                                    <input type="hidden" id="oemType" name="<%=ClientTeamIdUtil.CLIENT_FIELD_OEM_TYPE%>" value="<%=OemTypeEnum.ADMIN%>">
                                    <!-- 100010:直营组织 200010:盛钱包 -->
                                </div>
                                <c:if test="${param.logout != null}">
                                    <p>您已经退出系统.</p>
                                </c:if>
                                <c:if test="${param.error != null}">
                                    <c:choose>
                                        <c:when test="${SPRING_SECURITY_LAST_EXCEPTION.message == 'blocked'}">
                                            <p>登录错误太多,30分钟后重试.</p>
                                        </c:when>
                                        <c:when test="${SPRING_SECURITY_LAST_EXCEPTION.message == 'close'}">
                                            <p>用户状态异常.</p>
                                        </c:when>
                                        <c:when test="${param.error == 'lock'}">
                                            <p>用户被锁定 ${param.lock != null ? param.lock : '30'}分钟</p>
                                        </c:when>
                                        <c:when test="${param.error == 'notoem'}">
                                            <p>您不是该品牌代理商，暂不支持登录</p>
                                        </c:when>
                                        <c:otherwise>
                                            <p>用户名或密码错误.</p>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                <button id="submit_id" type="submit" class="btn btn-new block full-width m-b">登录</button>

                                <%--<a href="#" class="forgetpw">--%>
                                <%--<small>忘记密码</small>--%>
                                <%--</a>--%>
                                <div class="form-group forget-ps">
                                    <a href="javascript:findPwd()">忘记密码？</a>
                                </div>

                            </form>
                            <!-- <p class="m-t">
                                <small>Inspinia we app framework base on Bootstrap 3 &copy; 2016</small>
                            </p> -->
                        </div>
                    </div>

                    <div class="col-md-5" style="float: right;padding: 0px;">
                        <jsp:include page="findPwd.jsp" />
                    </div>

                </div>
            </div>
        </div>
        <div class="loginBottom" style=" position:fixed; bottom:10px; width:1000px; left:50%; margin-left:-500px;">
            <div class="col-md-6" style="color:#505050">
            Copyright 深圳市移付宝科技有限公司.   All Rights Reserved.
            </div>
            <div class="col-md-6 text-right" style="color:#505050">
            <small>粤ICP备15076873号-1 © 2015-2016</small>
            </div>
        </div>
        <jsp:include page="findPwdJS.jsp" />
    </body>
</html>

