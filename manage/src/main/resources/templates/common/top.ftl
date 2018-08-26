<div class="navbar cm-navbar">
	<img class="logo" alt="Brand" src="/images/logo.png">
	<span class="pageTitle">&nbsp;</span>
	<ul class="nav navbar-nav navbar-right cm-navbar-nav">
		<li>
			<p class="navbar-text text-info">${loginInfo.username}</p>
		</li>
		<li><a href="/loginInfo/logout">安全退出</a></li>
		<li><a href="">个人设置</a></li>
		<li>
			<@security.authorize access="hasRole('后台首页')">
				我拥有后台首页权限
			</@security.authorize>
		</li>
		
	</ul>
</div>
