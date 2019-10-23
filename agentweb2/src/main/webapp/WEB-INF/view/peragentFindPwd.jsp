<%@ page contentType="text/html; charset=utf-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div class="row findPwdbox" id="findPwdDiv" style="display: none;">
    <div class="lg-left">
        <h3>忘记密码</h3>
        <form action="${ctx}/findPwd.do" method="post">
            <div class="form-group">
                <input type="text" id="mobile" class="form-control lg-txt1" placeholder="请输入手机号" name="mobile" maxlength="11">
            </div>
            <div class="form-group col-sm-6 col-md-6 col-lg-6" style="padding-left: 0px;margin-left: 0px;">
                <input type="text" placeholder="请输入验证码" class="form-control" name="check_code" id="check_code" maxlength="6">
            </div>
            <div class="form-group col-sm-6 col-md-6 col-lg-6">
                <button type="button" id="sendMsgBtn" class="btn btn-new" style="background: #D1AC6D;border-color: #D1AC6D" onclick="sendMsg()">点击获取验证码</button>
            </div>
            <div class="form-group">
                <button type="button" class="btn" style="background: transparent;color: #D1AC6D;" onclick="findPwd();">我又想起来了&gt;&gt;</button>
            </div>
            <button id="checkMsgCode" type="button" onclick="checkMsg()" class="btn btn-new lg-btn" style="background: #D1AC6D;border-color: #D1AC6D" disabled>确定</button>
        </form>
    </div>
</div>

<div class="row findPwdbox" id="setPwdDiv" style="display: none;min-height: 250px;">
    <form class="form-horizontal" ng-app="myApp" ng-controller="validateCtrl" name="myForm" novalidate>
        <input type="hidden" id="sign" name="sign" value="">
        <div class="form-group col-xs-12 col-sm-6 col-md-12 col-lg-12 text-center" style="margin-top: 20px;"><h3>重置密码</h3></div>
        <div class="form-group col-xs-12 col-sm-6 col-md-12 col-lg-12">
            <label for="newPwd" class="col-xs-12 col-sm-3 col-md-3 col-lg-3 control-label">新密码</label>
            <div class="col-xs-12 col-sm-7 col-md-7 col-lg-7">
                <input type="password" autocomplete="off"  id="newPwd" name="newPwd" class="form-control" placeholder="新密码" ng-model="newPwd" ng-pattern="/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$/" maxlength="16" required>
                <span style="color:red" ng-show="myForm.newPwd.$dirty && myForm.newPwd.$invalid">
                    <span ng-show="myForm.newPwd.$error.required">密码是必须的。</span>
                    <span ng-show="myForm.newPwd.$error.pattern">密码必须同时包含英文和数字，且大于6位低于16位</span>
                </span>
            </div>
        </div>

        <div class="form-group col-xs-12 col-sm-6 col-md-12 col-lg-12">
            <label for="newPwd2" class="col-xs-12 col-sm-3 col-md-3 col-lg-3 control-label">确认密码</label>
            <div class="col-xs-12 col-sm-7 col-md-7 col-lg-7">
                <input type="password" autocomplete="off"  id="newPwd2" name="newPwd2" class="form-control" placeholder="确认密码" ng-model="newPwd2" ng-pattern="/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$/" maxlength="16" required>
                <span style="color:red" ng-show="myForm.newPwd2.$dirty && myForm.newPwd2.$invalid">
                <span ng-show="myForm.newPwd2.$error.required">确认密码是必须的。</span>
                <span ng-show="myForm.newPwd2.$error.pattern">密码必须同时包含英文和数字，且大于6位低于16位</span>
            </span>
            </div>
        </div>

        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
            <input type="button" value="确认修改" class="btn btn-primary"
                   onclick="upPwd()"
                   ng-disabled="myForm.newPwd.$dirty && myForm.newPwd.$invalid || myForm.newPwd2.$dirty && myForm.newPwd2.$invalid">
            <button class="btn btn-default" onclick="$('#setPwdDiv').toggle();$('#findPwdDiv').toggle();">取消</button>
        </div>
    </form>
</div>