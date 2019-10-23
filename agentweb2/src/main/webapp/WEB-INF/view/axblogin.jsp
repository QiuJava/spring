<%@ page import="cn.eeepay.framework.util.OemTypeEnum" %>
<%@ page import="cn.eeepay.boss.security.ClientTeamIdUtil" %>
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

    <title>登录</title>

    <link href="${ctx}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${ctx}/font-awesome/css/font-awesome.css" rel="stylesheet">

    <link href="${ctx}/css/animate.css" rel="stylesheet">
    <link href="${ctx}/css/style.css" rel="stylesheet">
    <link href="${ctx}/css/login.css" rel="stylesheet">
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
<div class="lg-top"><img src="${ctx}/img/login/login_logo@2x.png"><span>欢迎登录银盛支付管理系统</span></div>
<div class="row login-bg">
    <div class="loginColumns animated fadeInDown">

        <div class="row loginbox" id="loginDiv">
            <div class="lg-left">
                <h3>登陆</h3>
                <c:url value="/perform_login.do" var="loginUrl" />
                <form action="${loginUrl}" method="post">
                    <div class="form-group">
                        <input type="text" class="form-control lg-txt1" placeholder="请输入用户名" required=""  name="username">
                    </div>
                    <div class="form-group">
                        <input id="password" autocomplete="off" type="password" class="form-control lg-txt2" placeholder="请输入密码" required="" name="password">
                        <input type="hidden" id="teamid" name="teamid" value="${teamId }">
                        <!-- 100010:直营组织 200010:盛钱包 -->
                        <input type="hidden" id="oemType" name="<%=ClientTeamIdUtil.CLIENT_FIELD_OEM_TYPE%>" value="<%=OemTypeEnum.YABPAY%>">
                    </div>

                    <div class="form-group forget-ps">
                        <input type="checkbox" class="form-chk"><label>记住账号</label>
                        <a href="javascript:findPwd()">忘记密码？</a>
                    </div>
                    <sec:csrfInput/>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <button id="submit_id" type="submit" class="btn btn-new lg-btn">确定</button>
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
                    <%--<a href="#" class="forgetpw">--%>
                    <%--<small>忘记密码</small>--%>
                    <%--</a>--%>

                </form>
                <!-- <p class="m-t">
                    <small>Inspinia we app framework base on Bootstrap 3 &copy; 2016</small>
                </p> -->
            </div>
            <div class="lg-right">
                <img src="${ctx}/img/yab-code.jpg" alt="宏博宏宇app">
                <p>宏博宏宇app下载</p>
            </div>
        </div>

        <jsp:include page="findPwd.jsp" />

    </div>
</div>
<div class="loginBottom">
    <div class="col-md-6" style="color:#505050; display:none">
        <!-- Copyright 深圳市移付宝科技有限公司.   All Rights Reserved.   -->
    </div>
    <div style="color:#fff; text-align:center; font-size:14px;">
        <!--             <small>粤ICP备15076873号-1 © 2015-2016</small> -->
        <a target="_blank" href="http://www.miitbeian.gov.cn">粤ICP备16045637</a>
    </div>
</div>
<jsp:include page="findPwdJS.jsp" />
</body>






<%--<body class="loginbg">--%>
    <%--<div class="row login-bg">--%>
        <%--<div class="loginColumns animated fadeInDown">--%>
            <%--<h2 class="">欢迎登录安信宝代理商系统</h2>--%>
            <%--<div class="row loginbox"  id="loginDiv">--%>
                <%--<div class="lg-left">--%>
                    <%--<h3>登陆</h3>--%>
                    <%--<c:url value="/perform_login.do" var="loginUrl" />--%>
                        <%--<form action="${loginUrl}" method="post">--%>
                            <%--<div class="form-group">--%>
                                <%--<input type="text" class="form-control lg-txt1" placeholder="请输入用户名" required=""  name="username">--%>
                            <%--</div>--%>
                            <%--<div class="form-group">--%>
                                <%--<input type="password" class="form-control lg-txt2" placeholder="请输入密码" required="" name="password">--%>
                                <%--<input type="hidden" id="teamid" name="teamid" value="${teamId }">--%>
                                <%--<input type="hidden" id="oemType" name="<%=ClientTeamIdUtil.CLIENT_FIELD_OEM_TYPE%>" value="<%=OemTypeEnum.YABPAY.name()%>">--%>
                                <%--<!-- 100010:直营组织 200010:中代宝 -->--%>
                            <%--</div>--%>
                            <%--<c:if test="${param.logout != null}">--%>
                            <%--<p>您已经退出系统.</p>--%>
                            <%--</c:if>--%>
                            <%--<c:if test="${param.error != null}">--%>
                                <%--<c:choose>--%>
                                    <%--<c:when test="${SPRING_SECURITY_LAST_EXCEPTION.message == 'blocked'}">--%>
                                        <%--<p>登录错误太多,30分钟后重试.</p>--%>
                                     <%--</c:when>--%>
                                      <%--<c:when test="${SPRING_SECURITY_LAST_EXCEPTION.message == 'close'}">--%>
                                        <%--<p>用户状态异常.</p>--%>
                                     <%--</c:when>--%>
                                     <%--<c:otherwise>--%>
                                        <%--<p>用户名或密码错误.</p>--%>
                                    <%--</c:otherwise>--%>
                                <%--</c:choose>--%>
                            <%--</c:if>--%>
                            <%--<div class="form-group forget-ps">--%>
                                <%--<input type="checkbox" class="form-chk"><label>记住账号</label>--%>
                                <%--<a href="javascript:findPwd()">忘记密码？</a>--%>
                            <%--</div>--%>
                            <%--<sec:csrfInput/>--%>
                            <%--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />--%>
                            <%--<button type="submit" class="btn btn-new lg-btn">确定</button>--%>

                            <%--&lt;%&ndash;<a href="#" class="forgetpw">&ndash;%&gt;--%>
                                <%--&lt;%&ndash;<small>忘记密码</small>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;</a>&ndash;%&gt;--%>

                        <%--</form>--%>
                        <%--<!-- <p class="m-t">--%>
                            <%--<small>Inspinia we app framework base on Bootstrap 3 &copy; 2016</small>--%>
                        <%--</p> -->--%>
                <%--</div>--%>
                <%--<div class="lg-right">--%>
                    <%--<img src="${ctx}/img/yab-code.jpg" alt="宏博宏宇app">--%>
                    <%--<p>宏博宏宇app下载</p>--%>
                <%--</div>--%>
            <%--</div>--%>

            <%--<jsp:include page="findPwd.jsp" />--%>

        <%--</div>--%>
    <%--</div>--%>
    <%--&lt;%&ndash;<div class="loginBottom" style=" position:fixed; bottom:18px; width:1000px; left:50%; margin-left:-500px;">&ndash;%&gt;--%>
        <%--&lt;%&ndash;<div class="col-md-6" style="color:#505050; display:none">&ndash;%&gt;--%>
        <%--&lt;%&ndash;<!-- Copyright 深圳市移付宝科技有限公司.   All Rights Reserved.   -->&ndash;%&gt;--%>
        <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
        <%--&lt;%&ndash;<div style="color:#fff; text-align:center; font-size:14px;">&ndash;%&gt;--%>
<%--&lt;%&ndash;<!--             <small>粤ICP备15076873号-1 © 2015-2016</small> -->&ndash;%&gt;--%>
            <%--&lt;%&ndash;<a rel="noopener noreferrer" target="_blank" href="http://www.miitbeian.gov.cn">粤ICP备16045637</a>&ndash;%&gt;--%>
        <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
    <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
    <%--<jsp:include page="findPwdJS.jsp" />--%>
<%--</body>--%>

</html>

