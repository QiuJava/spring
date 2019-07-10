<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>蓝源Eloan-P2P平台->我要借款</title>
<#include "common/links-tpl.ftl" />
</head>
<body>
	<#assign currentNav="borrow"/>
	<!-- 网页头信息 -->
	<#include "common/head-tpl.ftl" />
	
	<!-- 网页导航 -->
	<#include "common/navbar-tpl.ftl" />
	
	<!-- 网页内容 -->
	<div class="container el-borrow">
		<div class="row">
			<div class="el-borrow-item col-sm-4">
				<div class="el-borrow-item-title" style="background-color: #40d47e;">
					信用贷</div>
				<div class="el-borrow-item-content">
					<p>
						认证后可借金额 <i>¥ ${initBorrowLimit}.00</i>
					</p>
					<a href="#" class="text-primary">申请条件</a>
					<p class="help-block">仅限深圳地区</p>
					<ul>
						<li>
							<#if !(userInfo.basicInfo)>
								<a href="/website/userInfo">填写基本资料</a>
								<span class="glyphicon glyphicon-remove" style="color:red;"></span>
							<#else>填写基本资料<span class="glyphicon glyphicon-ok" style="color:green;"></span></#if>
						</li>
						<li>
							<#if !(userInfo.realAuth)>
								<a href="/website/realAuth">身份认证</a>
								<span class="glyphicon glyphicon-remove" style="color:red;"></span>
							<#else>身份认证<span class="glyphicon glyphicon-ok" style="color:green;"></span></#if>
						</li>
						<li>
							<#if (userInfo.authScore < creditBorrowScore) >
								<a href="/website/creditFile">材料认证分数达到${creditBorrowScore}分</a>
								<span class="glyphicon glyphicon-remove" style="color:red;"></span>
							<#else>材料认证分数达到${creditBorrowScore}分<span class="glyphicon glyphicon-ok" style="color:green;"></span>
							</#if>
						</li>
						<li>视频认证
							<#if !(userInfo.vedioAuth)><span class="glyphicon glyphicon-remove" style="color:red;"></span>
							<#else><span class="glyphicon glyphicon-ok" style="color:green;"></span></#if>
						</li>
					</ul>
					<#if userInfo.basicInfo && userInfo.vedioAuth && userInfo.realAuth &&(userInfo.authScore >= creditBorrowScore) >
						<a href="/website/borrow/apply" class="el-borrow-apply">
							申请贷款
						</a>
					</#if>
				</div>
			</div>
		</div>
	</div>

</body>
</html>