<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>蓝源Eloan-P2P平台</title>
<#include "common/links-tpl.ftl" />
<script type="text/javascript" src="/js/plugins/jquery.form.js"></script>
<link type="text/css" rel="stylesheet" href="/css/account.css" />

<script type="text/javascript">
	// 给绑定手机按钮添加事件
	$(function() {
		$("#goto_bindphone").click(function() {
			$("#bindPhoneModal").modal('show');
		})
		$("#sendVerifyCode").click(function() {
			var pn = $("#phoneNumber").val();
			var _this = $(this);
			// 只有用户输入了电话才能发送验证码
			if (pn) {
				_this.attr("disabled", "disabled");
				$.ajax({
					dataType : "json",
					type : "POST",
					url : "/userInfo/verifyCode",
					data : {
						phoneNumber : pn
					},
					success : function(data) {
						if (data.success) {
							var time = 120;
							var timer = window.setInterval(function() {
								time--;
								if (time > 0) {
									_this.html(time + "秒重新发送");
								} else {
									_this.html("重新发送");
									_this.attr("disabled", false);
									window.clearInterval(timer);
								}

							}, 1000);
						} else {
							_this.attr("disable", false);
							$.messager.alert("温馨提示", data.msg);
						}
					}
				});
			}
		});
		// 绑定手机
		$("#bindPhone").click(function() {
			$("#bindForm").ajaxSubmit(function(data) {
				if (data.success) {
					$.messager.confirm("提示", "手机绑定成功!", function() {
						window.location.reload();
					});
				} else {
					$.messager.alert(data.msg);
				}
			});
		});

		// 给绑定邮箱按钮添加事件
		$("#goto_bindemail").click(function() {
			$("#bindEmailModal").modal("show");// 打开模式窗口
		})
		// 绑定邮箱
		$("#bindEmail").click(
				function() {
					var em = $("#email").val();
					if (em) {
						$.ajax({
							dataType : "json",
							type : "POST",
							url : "/email/send",
							data : {
								email : em
							},
							success : function(data) {
								if (data.success) {
									$.messager.confirm("提示", "已发送验证邮件,请尽快激活!",
											function() {
												window.location.reload();
											});
								} else {
									$.messager.alert("发送验证邮件失败");
								}
							}
						})
					}
				});
	});
</script>
</head>
<body>
	<!-- assign指令 在freemarker的上下文中添加一个变量-->
	<#assign currentNav="personal"> 
	<!-- 网页顶部导航 -->
	<#include "common/head-tpl.ftl" />
	<!-- 网页导航 --> 
	<#include "common/navbar-tpl.ftl" />

	<div class="container">
		<div class="row">
			<!--导航菜单-->
			<div class="col-sm-3">
				<!-- 添加一个变量 -->
				<#assign currentMenu="account"> 
				<#include "common/leftmenu-tpl.ftl" />
			</div>
			<!-- 功能页面 -->
			<div class="col-sm-9">
				<div class="panel panel-default">
					<div class="panel-body el-account">
						<div class="el-account-info">
							<div class="pull-left el-head-img">
								<img class="icon" src="/images/person_icon.png" />
							</div>
							<div class="pull-left el-head">
								<p>用户名： ${SPRING_SECURITY_CONTEXT.authentication.principal.username}</p>
								<p>最后登录时间：${loginLog.loginTime}</p>
							</div>
							<div class="pull-left"
								style="text-align: center; width: 400px; margin: 30px auto 0px auto;">
								<a class="btn btn-primary btn-lg" href="/website/recharge">账户充值</a> <a
									class="btn btn-danger btn-lg" href="/website/withdraw">账户提现</a>
							</div>
							<div class="clearfix"></div>
						</div>

						<div class="row h4 account-info">
							<div class="col-sm-4">
								账户总额：<span class="text-primary">${account.totalAmount}元</span>
							</div>
							<div class="col-sm-4">
								可用金额：<span class="text-primary">${account.usableBalance}元</span>
							</div>
							<div class="col-sm-4">
								冻结金额：<span class="text-primary">${account.freezedAmount}元</span>
							</div>
						</div>

						<div class="row h4 account-info">
							<div class="col-sm-4">
								待收利息：<span class="text-primary">${account.unReceiveInterest}元</span>
							</div>
							<div class="col-sm-4">
								待收本金：<span class="text-primary">${account.unReceivePrincipal}元</span>
							</div>
							<div class="col-sm-4">
								待还本息：<span class="text-primary">${account.unReturnAmount}元</span>
							</div>
						</div>

						<div class="el-account-info top-margin">
							<div class="row">
								<div class="col-sm-4">
									<div class="el-accoun-auth">
										<div class="el-accoun-auth-left">
											<img src="/images/shiming.png" />
										</div>
										<div class="el-accoun-auth-right">
											<h5>实名认证</h5>
											<#if userInfo.realAuth>
											<p>
												已认证 <a href="/website/realAuth">查看</a>
											</p>
											<#else>
											<p>
												未认证 <a href="/website/realAuth">马上认证</a>
											</p></#if>
										</div>
										<div class="clearfix"></div>
										<p class="info">实名认证之后才能在平台投资</p>
									</div>
								</div>
								<div class="col-sm-4">
									<div class="el-accoun-auth">
										<div class="el-accoun-auth-left">
											<img src="/images/shouji.jpg" />
										</div>
										<div class="el-accoun-auth-right">
											<h5>手机认证</h5>
											<#if userInfo.bindPhone>
											<p>
												已认证 <a href="#">修改绑定</a>
											</p>
											<#else>
											<p>
												未认证 <a id="goto_bindphone" href="javascript:;">立刻绑定</a>
											</p></#if>
										</div>
										<div class="clearfix"></div>
										<p class="info">可以收到系统操作信息,并增加使用安全性</p>
									</div>
								</div>
								<div class="col-sm-4">
									<div class="el-accoun-auth">
										<div class="el-accoun-auth-left">
											<img src="/images/youxiang.jpg" />
										</div>
										<div class="el-accoun-auth-right">
											<h5>邮箱认证</h5>
											<#if userInfo.bindEmail>
											<p>
												已认证 <a href="#">修改绑定</a>
											</p>
											<#else>
											<p>
												未认证 <a id="goto_bindemail" href="javascript:;">立刻绑定</a>
											</p></#if>
										</div>
										<div class="clearfix"></div>
										<p class="info">您可以设置邮箱来接收重要信息</p>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-4">
									<div class="el-accoun-auth">
										<div class="el-accoun-auth-left">
											<img src="/images/baozhan.jpg" />
										</div>
										<div class="el-accoun-auth-right">
											<h5>VIP会员</h5>
											<p>
												普通用户 <a href="#">查看</a>
											</p>
										</div>
										<div class="clearfix"></div>
										<p class="info">VIP会员，让你更快捷的投资</p>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<#if !userInfo.bindPhone>
	<div class="modal fade" id="bindPhoneModal" tabindex="-1" role="dialog"
		aria-labelledby="exampleModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="exampleModalLabel">绑定手机</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" id="bindForm" method="post"
						action="/userInfo/bindPhone">
						<div class="form-group">
							<label for="phoneNumber" class="col-sm-2 control-label">手机号:</label>
							<div class="col-sm-4">
								<input type="text" class="form-control" id="phoneNumber"
									name="phoneNumber" />
								<button id="sendVerifyCode" class="btn btn-primary"
									type="button">发送验证码</button>
							</div>
						</div>
						<div class="form-group">
							<label for="verifyCode" class="col-sm-2 control-label">验证码:</label>
							<div class="col-sm-4">
								<input type="text" class="form-control" id="verifyCode"
									name="verifyCode" />
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="bindPhone">绑定</button>
				</div>
			</div>
		</div>
	</div>
	</#if> <#if !userInfo.bindEmail>
	<div class="modal fade" id="bindEmailModal" tabindex="-1" role="dialog"
		aria-labelledby="bindEmailModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="bindEmailModalLabel">绑定邮箱</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" id="bindEmailForm" method="post"
						action="/email/send">
						<div class="form-group">
							<label for="email" class="col-sm-2 control-label">邮箱地址:</label>
							<div class="col-sm-6">
								<input type="text" class="form-control" id="email" name="email" />
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="bindEmail">发送验证邮件</button>
				</div>
			</div>
		</div>
	</div>
	</#if>
</body>
</html>