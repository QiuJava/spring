<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
    var app = angular.module('myApp', []);
    app.controller('validateCtrl', function($scope) {
        $scope.newPwd = '';
        $scope.newPwd2 = '';
    });
    function findPwd() {
        $("#loginDiv").toggle();
        $("#findPwdDiv").toggle();
        $("#check_code").val("");
        $("#checkMsgCode").attr("disabled","disabled");
    }
    function sendMsg() {
        //alert("开始发送短信验证码");
        var teamid = $("#teamid").val();
        var mobile = $("#mobile").val();
        if(11 == mobile.length){
            /***
             * ajax校验手机号并发送短信验证码，成功则确认按钮可用
             */
            $.post("${ctx}/findPwd/sendMsgCode.do",{mobile:mobile,teamid:teamid},function(data){
                //alert(data.status+"<>"+data.title+"<>"+data.body);
                if("true"===data.status){
                    $("#checkMsgCode").removeAttr("disabled");
                    setTime();
                }else{
                    alert(data.body);
                }
            });
        }else{
            alert("请输入11位手机号");
        }
    }
    function checkMsg(){
        //alert("开始检验短信验证码");
        var mobile = $("#mobile").val();
        var check_code = $("#check_code").val();
        /***
         * ajax校验短信验证码，成功则切换至密码设置界面
         */

        $.post("${ctx}/findPwd/checkMsgCode.do",{mobile:mobile,check_code:check_code},function(data){
            //alert(data.status+"<>"+data.title+"<>"+data.body);
            if("true"===data.status){
                $("#sign").val(data.body);
                $("#newPwd").val("");
                $("#newPwd2").val("");
                $("#findPwdDiv").toggle();
                $("#setPwdDiv").toggle();
            }else{
                alert(data.body);
            }
        });
    }
    function upPwd() {
        var mobile = $("#mobile").val();
        var pwd = $("#newPwd").val();
        var pwd2 = $("#newPwd2").val();
        var sign = $("#sign").val();
        var teamid = $("#teamid").val();
        if(pwd.length>5 && pwd === pwd2){
            //alert("修改密码");
            $.post("${ctx}/findPwd/upPwd.do",{mobile:mobile,newPwd:pwd,sign:sign,teamid:teamid},function(data){
                if("true"===data.status){
                    alert("密码设置成功，点击返回登录界面");
                    $("#setPwdDiv").toggle();
                    $("#loginDiv").toggle();
                }else{
                    alert(data.body);
                }
            });
        }
    }
    var countdown=60;
    function setTime() {
        if (countdown == 0) {
            $("#sendMsgBtn").removeAttr("disabled");
            $("#sendMsgBtn").html("点击获取验证码");
            countdown = 60;
        } else {
            $("#sendMsgBtn").attr("disabled","disabled");
            $("#sendMsgBtn").html("重新发送(" + countdown + ")");
            countdown--;
            setTimeout(function() {setTime()},1000);
        }
    }
</script>