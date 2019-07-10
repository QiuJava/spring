<ul id="menu" class="list-group">
	<li class="list-group-item">
		<a href="#" data-toggle="collapse" data-target="#permissionmanage_detail"><span>安全管理</span></a>
		<ul class="in" id="permissionmanage_detail">
			<li class="systemDictionary"><a href="/manage/systemDictionary"><span>系统数据字典</span></a></li>
			<li class="systemDictionaryItem"><a href="/manage/systemDictionaryItem"><span>系统数据字典条目</span></a></li>
			<li class="systemTimedTask"><a href="/systemTimedTask/pageQuery"><span>定时任务管理</span></a></li>
			<li class="loginLog"><a href="/manage/loginLog"><span>登录历史</span></a></li>
		</ul>
	</li>
	
	<li class="list-group-item">
		<a href="#" data-toggle="collapse" data-target="#auditmanage_detail">
			<span>审核项目</span>
		</a>
		<ul class="in" id="auditmanage_detail">
			<li class="realAuth"><a href="/manage/realAuth">实名认证审核</a></li>
			<li class="creditFileAuth"><a href="/manage/creditFile">认证材料审核</a></li>
			<li class="borrow_publish"><a href="/manage/borrow/publish">发标前审核</a></li>
			<li class="borrow_full"><a href="/manage/borrow/full">满标审核</a></li>
			<li class="recharge"><a href="/manage/recharge">充值审核</a></li>
			<li class="withdraw"><a href="/manage/withdraw">提现审核</a></li>
		</ul>
	</li>
	<li class="list-group-item">
		<a href="#" data-toggle="collapse" data-target="#systemmanage_detail">
			<span>平台管理</span>
		</a>
		<ul class="in" id="systemmanage_detail">
			<li class="companyBankCard"><a href="/manage/companyBankCard">平台账户管理</a></li>
		</ul>
	</li>
</ul>

<#if currentMenu??>
<script type="text/javascript">
	$("#menu li.${currentMenu}").addClass("active");
</script>
</#if>