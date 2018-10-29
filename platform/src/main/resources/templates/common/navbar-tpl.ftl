<div class="navbar navbar-default el-navbar">
	<div class="container">
		<div class="navbar-header">
			<a href="/">
				<img alt="Brand" src="/images/logo.png">
			</a>
		</div>
		<ul class="nav navbar-nav">
			<li id="index"><a href="/index.html">首页</a></li>
			<li id="invest"><a href="/invest">我要投资</a></li>
			<li id="borrow"><a href="/borrow/home">我要借款</a></li>
			<li id="personal"><a href="/personalCenter">个人中心</a></li>
			<li><a href="#">新手指引</a></li>
			<li><a href="#">关于我们</a></li>
		</ul>
	</div>
</div>
<#if currentNav??>
<script type="text/javascript">
// 拿到指定的li 设置样式
	$(".nav li[id='${currentNav}']").addClass("active");
</script>
</#if>

