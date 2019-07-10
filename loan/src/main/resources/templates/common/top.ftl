<div class="navbar cm-navbar">
	<img class="logo" alt="Brand" src="/images/logo.png">
	<span class="pageTitle">&nbsp;</span>
	<ul class="nav navbar-nav navbar-right cm-navbar-nav">
		<li>
			<p class="navbar-text text-info">${SPRING_SECURITY_CONTEXT.authentication.principal.username}</p>
		</li>
		<li><form action="/manage/logout" method="post">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<input type="submit" value="安全退出"/>
		</form></li>
		<li><a href="">个人设置</a></li>
	</ul>
</div>
