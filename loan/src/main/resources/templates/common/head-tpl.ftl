<div class="el-header">
	<div class="container" style="position: relative;">
		<ul class="nav navbar-nav navbar-right">
			<li><a href="/website/index">首页</a></li> 
			<#if !SPRING_SECURITY_CONTEXT.authentication.principal??>
			<li><a href="/website/login">登录</a></li>
			<li><a href="/website/register">快速注册</a></li> 
			<#else>
			<li><a class="el-current-user" href="/website/personalCenter"> ${SPRING_SECURITY_CONTEXT.authentication.principal.username} </a></li>
			<li><a href="/website/recharge"> 账户充值 </a></li>
			<li><form action="/website/logout" method="post">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				<input type="submit" value="安全退出"/>
				</form>
			</li> 
			</#if>
			<li><a href="#">帮助</a></li>
		</ul>
	</div>
</div>

