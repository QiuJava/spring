<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>蓝源Eloan-P2P平台->用户注册</title>
<link rel="stylesheet" href="/js/bootstrap-3.3.2-dist/css/bootstrap.css"
	type="text/css" />
<link rel="stylesheet" href="/css/website-core.css" type="text/css" />
<script type="text/javascript" src="/js/jquery/jquery-2.1.3.js"></script>
<script type="text/javascript"
	src="/js/bootstrap-3.3.2-dist/js/bootstrap.js"></script>
<script type="text/javascript"
	src="/js/plugins/jquery-validation/jquery.validate.js"></script>
<script type="text/javascript"
	src="/js/plugins/jquery-validation/localization/messages_zh.js"></script>
<script type="text/javascript" src="/js/plugins/jquery.form.js"></script>
<script type="text/javascript" src="/js/jquery.bootstrap.min.js"></script>

<style type="text/css">
.el-register-form {
	width: 600px;
	margin-left: auto;
	margin-right: auto;
	margin-top: 20px;
}

.el-register-form .form-control {
	width: 220px;
	display: inline;
}
</style>
<script type="text/javascript">
	// 使用自定义jQuery-vaildate方法：
	jQuery.validator.addMethod("checkLoginName", function(value, element) {
		var ret = false;
		$.ajax({
			url : "/website/loginUser/usernameExist",
			dataType : "json",
			type : "get",
			// 使用同步提交的方式
			async : false,
			data : {
				username : value
			},
			success : function(data) {
				ret = !data.data;
			}
		});
		return ret;
	}, "用户名已存在");
	$(function() {
		$("#registerForm").validate({
			// 验证规则
			rules : {
				"username" : {
					required : true,
					rangelength : [ 4, 16 ],
					checkLoginName : true
				},
				"password" : {
					required : true,
					rangelength : [ 6, 16 ]
				},
				"confirmPwd" : {
					equalTo : "#password"
				}

			},
			// 提示信息
			messages : {
				"username" : {
					required : "用户名不能为空",
					rangelength : "名字长度{0}~{1}之间"
				/* ,remote:"用户名已经被注册" */
				},
				"password" : {
					required : "密码不能为空",
					rangelength : "密码长度{0}~{1}之间"
				},
				"confirmPwd" : {
					equalTo : "两次输入的密码不一致"
				}
			},
			// 处理表单的ajax请求
			submitHandler : function(form) {
				$("#registerForm").ajaxSubmit({
					dataType : "json",
					success : function(data) {
						if (data.success) {
							$.messager.confirm("提示信息","注册成功", function() {
								window.location.href = "/website/login";
							})
						} else {
							$.messager.popup(data.msg);
						}
					}
				});// 把普通提交变为ajax提交
			},
			errorClass : "text-danger",
			highlight : function(element, errorClass) {
				// 添加错误的样式
				$(element).closest("div.form-group").addClass("has-error");
			},
			unhighlight : function(element, errorClass) {
				// 删除错误的样式
				$(element).closest("div.form-group").removeClass("has-error");
			}

		})
	})
</script>
</head>
<body>
	<!-- 网页头信息 -->
	<div class="el-header">
		<div class="container" style="position: relative;">
			<ul class="nav navbar-nav navbar-right">
				<li><a href="/home">首页</a></li>
				<li><a href="/website/login">登录</a></li>
				<li><a href="#">帮助</a></li>
			</ul>
		</div>
	</div>

	<!-- 网页导航 -->
	<div class="navbar navbar-default el-navbar">
		<div class="container">
			<div class="navbar-header">
				<a href=""><img alt="Brand" src="/images/logo.png"></a> <span
					class="el-page-title">用户注册</span>
			</div>
		</div>
	</div>

	<!-- 网页内容 -->
	<div class="container" style="min-height: 433px;">
		<form id="registerForm" class="form-horizontal el-register-form"
			action="/website/loginUser/register" method="post">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<p class="h4" style="margin: 10px 10px 20px; color: #999;">请填写注册信息，点击“提交注册”即可完成注册！</p>
			<div class="form-group">
				<label class="control-label col-sm-2">用户名</label>
				<div class="col-sm-10">
					<input type="text" autocomplete="off" id="username" name="username"
						class="form-control" />
					<p class="help-block">用户名为4~16位字母，数字，符号或中文</p>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-2">密&emsp;码</label>
				<div class="col-sm-10">
					<input type="password" autocomplete="off" name="password"
						id="password" class="form-control" />
					<p class="help-block">密码为6~16位字符组成,采用数字、字母、符号安全性更高</p>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-2">确认密码</label>
				<div class="col-sm-10">
					<input type="password" autocomplete="off" name="confirmPwd"
						class="form-control" />
					<p class="help-block">请再次填写密码</p>
				</div>
			</div>
			<div class="form-gorup">
				<div class="col-sm-offset-2">
					<button type="submit" class="btn btn-success">同意协议并注册</button>
					&emsp;&emsp; <a href="/website/login" class="text-primary">已有账号，马上登录</a>

					<p style="padding-left: 50px; margin-top: 15px;">
						<a href="#">《使用协议说明书》</a>
					</p>
				</div>
			</div>
		</form>
	</div>
</body>
</html>