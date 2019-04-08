<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<#include "../common.ftl"/>
</head>
<body>
	<h1>登录</h1>
	<form action="/login" method="post">
		用户名：<input name="username" type="text"> 密码：<input
			name="password" type="password"> <input type="submit">
	</form>
</body>
</html>