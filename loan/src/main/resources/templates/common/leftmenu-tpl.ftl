<ul id="menu" class="list-group">
	<li class="list-group-item">
		<a href="javascript:;"><span>借贷项目</span></a>
		<ul>
			<li name="repaymentPlan"><a href="/website/repaymentPlan"><span>还款明细</span></a></li>
		</ul>
	</li>
	<li class="list-group-item">
		<a href="#"><span class="text-title">我的账户</span></a>
		<ul class="in">
			<li name="account"><a href="/website/personalCenter">账户信息</a></li>
			<li name="realAuth"><a href="/website/realAuth">实名认证</a></li>
			<li name="creditFile"><a href="/website/creditFile">风控资料认证</a></li>
			<li name="bankCard"><a href="/website/bankCard">银行卡管理</a></li>
			<li name="loginLog"><a href="/website/loginLog">登录记录</a></li>
			<li name="userInfo"><a href="/website/userInfo"> <span>个人资料</span></a></li>
		</ul>
	</li>
	<li class="list-group-item">
		<a href="#"><span>资产详情</span></a>
		<ul class="in">
			<li name="recharge"><a href="/website/recharge/pageQuery">充值明细</a></li>
			<li name="withdraw"><a href="/website/withdraw">提现申请</a></li>
		</ul>
	</li>
</ul>

<#if currentMenu??>
<script type="text/javascript">
	$("#menu li[name=${currentMenu}]").addClass("active");
</script>
</#if>
