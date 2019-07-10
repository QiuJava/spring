<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>蓝源Eloan-P2P平台</title>
		<#include "common/links-tpl.ftl" />
		<link rel="stylesheet" href="/css/website-bank.css">
		<script type="text/javascript" src="/js/bank.js"></script>
		<link type="text/css" rel="stylesheet" href="/css/account.css" />
		<script type="text/javascript" src="/js/plugins/uploadify/jquery.uploadify.min.js"></script>
		<script type="text/javascript">
		
		</script>
	</head>
	<body>
		<!-- 网页顶部导航 -->
		<#include "common/head-tpl.ftl"/>
		<#assign currentNav="personal" />
		<#include "common/navbar-tpl.ftl" />

		<div class="container">
			<div class="row">
				<!--导航菜单-->
				<div class="col-sm-3">
					<#assign currentMenu="bankCard"/>
					<#include "common/leftmenu-tpl.ftl" />
				</div>
				<!-- 功能页面 -->
				<div class="col-sm-9">
					<div class="panel panel-default">
						<div class="panel-heading">
							绑定银行卡
						</div>
						<#if userInfo.realAuth>
						<form class="form-horizontal" id="bankForm" method="post" action="/website/bankCard/save" novalidate="novalidate">
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
							<fieldset>
								<div class="form-group">
									<p class="text-center text-danger">为保护您账户安全，绑定银行卡之后，不能自己修改，请认真填写！</p>
								</div>
								<div class="form-group">
						        	<label class="col-sm-4 control-label ">开户名</label>
					        		<div class="col-sm-8">
						        		<p class="form-control-static">${userInfo.realName}</p>
						        	</div>
						        	<input id="accountName" class="form-control" name="accountName"  type="hidden" value="${userInfo.realName}">
						        </div>
						        <div class="form-group">
						        	<label class="col-sm-4  control-label" for="bankCode">开户行</label>
					        		<div class="col-sm-8">
						        		<select class="form-control" name="bankCode" size="1">
											<script>
											for(var k in SITE_BANK_TYPE_NAME_MAP){
												var v = SITE_BANK_TYPE_NAME_MAP[k];
												var html = '<option value="'+ k +'">' + v + '</option>' 
												document.write(html);
											}
											</script>
										</select>
						        	</div>
						        </div>
						        <div class="form-group">
						        	<label class="col-sm-4  control-label" for="bankForkName">支行名称</label>
					        		<div class="col-sm-8">
						        		<input id="bankForkName" class="form-control" name="bankForkName"  type="text" value="">
						        	</div>
						        </div>
						        <div class="form-group">
						        	<label class="col-sm-4  control-label" for="cardNumber">银行账号</label>
					        		<div class="col-sm-8">
						        		<input id="cardNumber" class="form-control" name="cardNumber"  type="text" value="">
						        	</div>
						        </div>
						        <div class="form-group">
						        	<button type="submit" id="asubmit" class="btn btn-primary col-sm-offset-4" data-loading-text="正在提交"><i class="icon-ok"></i> 绑定银行卡</button>
						        </div>
							</fieldset>
						</form>
						<#else>
							<div class="el-tip-info">
								<p class="text-info">请先完成实名认证之后，才能进行银行卡绑定；</p>
							</div>
						</#if>
					</div>
				</div>
			</div>
		</div>		
	</body>
</html>