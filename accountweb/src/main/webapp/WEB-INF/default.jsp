<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>  

<html>
 
<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<!-- <title>移联支付账务系统</title> -->
<title>移联支付账务系统<sitemesh:title /></title>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.css" rel="stylesheet">
<!-- 消息提示 样式-->
<link href="css/plugins/toastr/toastr.min.css" rel="stylesheet">
<!-- 提示后逐渐消失 -->
<link href="js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<link href="css/animate.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
</head>

<body>

	<div id="wrapper">
		<!-- 导航开始-->
		<nav class="navbar-default navbar-static-side" role="navigation">
			
				
							<%@ include file="header.jsp"%>		
							
							<ul class="nav metismenu" id="side-menu">
				
					<!-- 导航头开始 -->
					<!-- <li class="nav-header">
						<div class="dropdown profile-element">
							<span> 
								<img alt="image" class="img-circle" src="img/profile_small.jpg" />
							</span>
							<a data-toggle="dropdown" class="dropdown-toggle" href="#">
								<span class="clear"> 
									<span class="block m-t-xs">
										<strong class="font-bold">David Williams</strong>
									</span> 
									<span class="text-muted text-xs block">
										Art Director <b class="caret"></b>
									</span>
								</span>
							</a>
							<ul class="dropdown-menu animated fadeInRight m-t-xs">
								<li><a href="profile.html">Profile</a></li>
								<li><a href="contacts.html">Contacts</a></li>
								<li><a href="mailbox.html">Mailbox</a></li>
								<li class="divider"></li>分割线
								<li><a href="login.html">Logout</a></li>
							</ul>
						</div>
						<div class="logo-element">IN+</div>
					</li> -->
					<!-- 导航头结束 -->
					
					<!-- 导航列表开始 -->
					<li class="active"><!-- 活动列表并展开 -->
						<a href="index.html">
							<i class="fa fa-th-large"></i> 
								<a href="${ctx}/UserAction/login.do"><span class="nav-label">科目管理</span>
							<span class="fa arrow"></span>
						</a>
						<ul class="nav nav-second-level">
							<li><a href="${ctx}/UserAction/login.do">新建科目信息</a></li>
							<li><a href="dashboard_3.html">科目详细列表信息</a></li>
							
						</ul>
					</li>
						<li><!-- 活动列表并展开 -->
						<a href="index.html">
							<i class="fa fa-th-large"></i> 
							<span class="nav-label">内部账号管理</span>
							<span class="fa arrow"></span>
						</a>
						<ul class="nav nav-second-level">
							<li><a href="dashboard_2.html">开立内部账户</a></li>
							<li><a href="dashboard_3.html">内部账户多条列表查询</a></li>
							<li><a href="dashboard_3.html">内部账户明细查询</a></li>																						
						</ul>
					</li>
						<li><!-- 活动列表并展开 -->
						<a href="index.html">
							<i class="fa fa-th-large"></i> 
							<span class="nav-label">外部账号管理</span>
							<span class="fa arrow"></span>
						</a>
						<ul class="nav nav-second-level">
							<li><a href="dashboard_2.html">外部账户开立</a></li>
							<li><a href="dashboard_3.html">查询所有账户列表</a></li>
							<li><a href="dashboard_3.html">客户账户明细查询</a></li>
							<li><a href="dashboard_3.html">账户状态修改</a></li>									
						</ul>
					</li>
						<li><!-- 活动列表并展开 -->
						<a href="index.html">
							<i class="fa fa-th-large"></i> 
							<span class="nav-label">调账管理</span>
							<span class="fa arrow"></span>
						</a>
						<ul class="nav nav-second-level">
							<li><a href="dashboard_2.html">新增调账申请</a></li>
							<li><a href="dashboard_3.html">调账记录多条列表查询</a></li>
							<li><a href="dashboard_3.html">调账记录审批</a></li>								
						</ul>
					</li>
					
					
					</li>
						<li><!-- 活动列表并展开 -->
						<a href="index.html">
							<i class="fa fa-th-large"></i> 
							<span class="nav-label">报表管理</span>
							<span class="fa arrow"></span>
						</a>
						<ul class="nav nav-second-level">
							<li><a href="dashboard_2.html">查询科目动态信息</a></li>
							<li><a href="dashboard_3.html">内部账户历史余额查询</a></li>
							<li><a href="dashboard_3.html">外部账户历史余额查询</a></li>
							<li><a href="dashboard_3.html">交易流水查询</a></li>								
						</ul>
					</li>
					
					</li>
						<li><!-- 活动列表并展开 -->
						<a href="index.html">
							<i class="fa fa-th-large"></i> 
							<span class="nav-label">系统管理</span>
							<span class="fa arrow"></span>
						</a>
						<ul class="nav nav-second-level">
							<li><a href="dashboard_2.html">新增用户</a></li>
							<li><a href="dashboard_3.html">个人中心</a></li>
													
						</ul>
					</li>
					
					
					
					
				</ul>
			</div>
		</nav>
		<!-- 导航结束 -->
		
		<!-- 主要内容开始 -->
		<!--
		
		
		
		  -->
		  <div id="page-wrapper" class="gray-bg dashbard-1">
			<div class="row border-bottom">
				<nav class="navbar navbar-static-top" role="navigation"
					style="margin-bottom: 0">
					<div class="navbar-header">
						<a class="navbar-minimalize minimalize-styl-2 btn btn-primary "
							href="#"><i class="fa fa-bars"></i> </a>
						<form role="search" class="navbar-form-custom"
							action="search_results.html">
							<div class="form-group">
								<input type="text" placeholder="移动宝BOSS-V2系统" 
									class="form-control" name="top-search" id="top-search">
							</div>
						</form>
					</div>
					
					<ul class="nav navbar-top-links navbar-right">
						<li>
							<span class="m-r-sm text-muted welcome-message">Welcome to INSPINIA+ Admin Theme.</span>
						</li>
						<li class="dropdown">
							<a class="dropdown-toggle count-info" data-toggle="dropdown" href="#"> 
								<i class="fa fa-envelope"></i>
								<span class="label label-warning">16</span>
							</a>
							<ul class="dropdown-menu dropdown-messages">
								<li>
									<div class="dropdown-messages-box">
										<a href="profile.html" class="pull-left"> <img alt="image"
											class="img-circle" src="img/a7.jpg">
										</a>
										<div class="media-body">
											<small class="pull-right">46h ago</small> <strong>Mike
												Loreipsum</strong> started following <strong>Monica Smith</strong>. <br>
											<small class="text-muted">3 days ago at 7:58 pm -
												10.06.2014</small>
										</div>
									</div>
								</li>
								<li class="divider"></li>
								<li>
									<div class="dropdown-messages-box">
										<a href="profile.html" class="pull-left"> <img alt="image"
											class="img-circle" src="img/a4.jpg">
										</a>
										<div class="media-body ">
											<small class="pull-right text-navy">5h ago</small> <strong>Chris
												Johnatan Overtunk</strong> started following <strong>Monica
												Smith</strong>. <br> <small class="text-muted">Yesterday
												1:21 pm - 11.06.2014</small>
										</div>
									</div>
								</li>
								<li class="divider"></li>
								<li>
									<div class="dropdown-messages-box">
										<a href="profile.html" class="pull-left"> <img alt="image"
											class="img-circle" src="img/profile.jpg">
										</a>
										<div class="media-body ">
											<small class="pull-right">23h ago</small> <strong>Monica
												Smith</strong> love <strong>Kim Smith</strong>. <br> <small
												class="text-muted">2 days ago at 2:30 am -
												11.06.2014</small>
										</div>
									</div>
								</li>
								<li class="divider"></li>
								<li>
									<div class="text-center link-block">
										<a href="mailbox.html"> <i class="fa fa-envelope"></i> <strong>Read
												All Messages</strong>
										</a>
									</div>
								</li>
							</ul>
						</li>
						<li class="dropdown">
							<a class="dropdown-toggle count-info" data-toggle="dropdown" href="#"> 
								<i class="fa fa-bell"></i>
								<span class="label label-primary">8</span>
							</a>
							<ul class="dropdown-menu dropdown-alerts">
								<li>
									<a href="mailbox.html">
										<div>
											<i class="fa fa-envelope fa-fw"></i> You have 16 messages <span
												class="pull-right text-muted small">4 minutes ago</span>
										</div>
									</a>
								</li>
								<li class="divider"></li>
								<li><a href="profile.html">
										<div>
											<i class="fa fa-twitter fa-fw"></i> 3 New Followers <span
												class="pull-right text-muted small">12 minutes ago</span>
										</div>
								</a></li>
								<li class="divider"></li>
								<li>
									<a href="grid_options.html">
										<div>
											<i class="fa fa-upload fa-fw"></i> Server Rebooted <span
												class="pull-right text-muted small">4 minutes ago</span>
										</div>
									</a>
								</li>
								<li class="divider"></li>
								<li>
									<div class="text-center link-block">
										<a href="notifications.html"> <strong>See All
												Alerts</strong> <i class="fa fa-angle-right"></i>
										</a>
									</div>
								</li>
							</ul>
						</li>
						<li>
							<a href="login.html"> 
								<i class="fa fa-sign-out"></i>退出
							</a>
						</li>
						<li>
							<a class="right-sidebar-toggle">
								<i class="fa fa-tasks"></i>
							</a>
						</li>
					</ul>
				</nav>
			</div>
		</div>
		<!-- 主要内容结束 -->
		
		<!-- 右则功能设置开始 -->		
		<!-- <div id="right-sidebar">
			<div class="sidebar-container">
				<ul class="nav nav-tabs navs-3">
					<li class="active">
						<a data-toggle="tab" href="#tab-1">Notes</a>
					</li>
					<li class="">
						<a data-toggle="tab" href="#tab-2"> <i class="fa fa-gear"></i></a>
					</li>
				</ul>

				<div class="tab-content">
					<div id="tab-1" class="tab-pane active">
						<div class="sidebar-title">
							<h3>
								<i class="fa fa-comments-o"></i> Latest Notes
							</h3>
							<small><i class="fa fa-tim"></i> You have 10 new message.</small>
						</div>
						<div>
							<div class="sidebar-message">
								<a href="#">
									<div class="pull-left text-center">
										<img alt="image" class="img-circle message-avatar"
											src="img/a1.jpg">

										<div class="m-t-xs">
											<i class="fa fa-star text-warning"></i> <i
												class="fa fa-star text-warning"></i>
										</div>
									</div>
									<div class="media-body">

										There are many variations of passages of Lorem Ipsum
										available. <br> <small class="text-muted">Today
											4:21 pm</small>
									</div>
								</a>
							</div>
							<div class="sidebar-message">
								<a href="#">
									<div class="pull-left text-center">
										<img alt="image" class="img-circle message-avatar"
											src="img/a2.jpg">
									</div>
									<div class="media-body">
										The point of using Lorem Ipsum is that it has a more-or-less
										normal. <br> <small class="text-muted">Yesterday
											2:45 pm</small>
									</div>
								</a>
							</div>
						</div>
					</div>
					<div id="tab-2" class="tab-pane">

						<div class="sidebar-title">
							<h3>
								<i class="fa fa-gears"></i> Settings
							</h3>
							<small><i class="fa fa-tim"></i> You have 14 projects. 10
								not completed.</small>
						</div>

						<div class="setings-item">
							<span> Show notifications </span>
							<div class="switch">
								<div class="onoffswitch">
									<input type="checkbox" name="collapsemenu"
										class="onoffswitch-checkbox" id="example"> <label
										class="onoffswitch-label" for="example"> <span
										class="onoffswitch-inner"></span> <span
										class="onoffswitch-switch"></span>
									</label>
								</div>
							</div>
						</div>
						<div class="setings-item">
							<span> Disable Chat </span>
							<div class="switch">
								<div class="onoffswitch">
									<input type="checkbox" name="collapsemenu" checked
										class="onoffswitch-checkbox" id="example2"> <label
										class="onoffswitch-label" for="example2"> <span
										class="onoffswitch-inner"></span> <span
										class="onoffswitch-switch"></span>
									</label>
								</div>
							</div>
						</div>
						<div class="setings-item">
							<span> Enable history </span>
							<div class="switch">
								<div class="onoffswitch">
									<input type="checkbox" name="collapsemenu"
										class="onoffswitch-checkbox" id="example3"> <label
										class="onoffswitch-label" for="example3"> <span
										class="onoffswitch-inner"></span> <span
										class="onoffswitch-switch"></span>
									</label>
								</div>
							</div>
						</div>
						<div class="setings-item">
							<span> Show charts </span>
							<div class="switch">
								<div class="onoffswitch">
									<input type="checkbox" name="collapsemenu"
										class="onoffswitch-checkbox" id="example4"> <label
										class="onoffswitch-label" for="example4"> <span
										class="onoffswitch-inner"></span> <span
										class="onoffswitch-switch"></span>
									</label>
								</div>
							</div>
						</div>
						<div class="setings-item">
							<span> Offline users </span>
							<div class="switch">
								<div class="onoffswitch">
									<input type="checkbox" checked name="collapsemenu"
										class="onoffswitch-checkbox" id="example5"> <label
										class="onoffswitch-label" for="example5"> <span
										class="onoffswitch-inner"></span> <span
										class="onoffswitch-switch"></span>
									</label>
								</div>
							</div>
						</div>
						<div class="setings-item">
							<span> Global search </span>
							<div class="switch">
								<div class="onoffswitch">
									<input type="checkbox" checked name="collapsemenu"
										class="onoffswitch-checkbox" id="example6"> <label
										class="onoffswitch-label" for="example6"> <span
										class="onoffswitch-inner"></span> <span
										class="onoffswitch-switch"></span>
									</label>
								</div>
							</div>
						</div>
						<div class="setings-item">
							<span> Update everyday </span>
							<div class="switch">
								<div class="onoffswitch">
									<input type="checkbox" name="collapsemenu"
										class="onoffswitch-checkbox" id="example7"> <label
										class="onoffswitch-label" for="example7"> <span
										class="onoffswitch-inner"></span> <span
										class="onoffswitch-switch"></span>
									</label>
								</div>
							</div>
						</div>
					</div>
				</div>

			</div>
		</div> -->
		 <sitemesh:body />
		<!-- 右则功能设置结束 -->		
		<!-- 消息小窗口开始 -->
		<div class="small-chat-box fadeInRight animated">
			<!-- 消息标题开始 -->
			<div class="heading" draggable="true">
				<small class="chat-date pull-right"> 02.19.2015 </small> Small chat
			</div>
			<!-- 消息标题结束 -->
			<!-- 消息内容开始 -->
			<div class="content">
				<div class="left">
					<div class="author-name">
						用户1 <small class="chat-date"> 10:02 am </small>
					</div>
					<div class="chat-message active">你好！</div>
				</div>
				<div class="right">
					<div class="author-name active">
						用户2 <small class="chat-date"> 11:24 am </small>
					</div>
					<div class="chat-message">大家好！</div>
				</div>
			</div>
			<!-- 消息标题结束 -->
			<!-- 发送消息开始 -->
			<div class="form-chat">
				<div class="input-group input-group-sm">
					<input type="text" class="form-control"> <span
						class="input-group-btn">
						<button class="btn btn-primary" type="button">Send</button>
					</span>
				</div>
			</div>
			<!-- 发送消息结束 -->
		</div>
		<!-- 消息小窗口结束 -->
		
		<div id="small-chat">
			<span class="badge badge-warning pull-right">5</span> 
			<a class="open-small-chat"> <i class="fa fa-comments"></i></a>
		</div>
	</div>

	<!-- Mainly scripts -->
	<script src="js/jquery-2.1.1.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

	<!-- Flot -->
	<script src="js/plugins/flot/jquery.flot.js"></script>
	<script src="js/plugins/flot/jquery.flot.tooltip.min.js"></script>
	<script src="js/plugins/flot/jquery.flot.spline.js"></script>
	<script src="js/plugins/flot/jquery.flot.resize.js"></script>
	<script src="js/plugins/flot/jquery.flot.pie.js"></script>

	<!-- Peity -->
	<script src="js/plugins/peity/jquery.peity.min.js"></script>
	<script src="js/demo/peity-demo.js"></script>

	<!-- Custom and plugin javascript -->
	<script src="js/inspinia.js"></script>
	<script src="js/plugins/pace/pace.min.js"></script>

	<!-- jQuery UI -->
	<script src="js/plugins/jquery-ui/jquery-ui.min.js"></script>

	<!-- GITTER -->
	<script src="js/plugins/gritter/jquery.gritter.min.js"></script>

	<!-- Sparkline -->
	<script src="js/plugins/sparkline/jquery.sparkline.min.js"></script>

	<!-- Sparkline demo data  -->
	<script src="js/demo/sparkline-demo.js"></script>

	<!-- ChartJS-->
	<script src="js/plugins/chartJs/Chart.min.js"></script>

	<!-- Toastr -->
	<script src="js/plugins/toastr/toastr.min.js"></script>


	<script>
		$(document).ready(
				function() {
					setTimeout(function() {
						toastr.options = {
							closeButton : true,
							progressBar : true,
							showMethod : 'slideDown',
							timeOut : 4000
						};
						toastr.success('Responsive Admin Theme','Welcome to INSPINIA');
					}, 1300);

					var data1 = [ [ 0, 4 ], [ 1, 8 ], [ 2, 5 ], [ 3, 10 ],
							[ 4, 4 ], [ 5, 16 ], [ 6, 5 ], [ 7, 11 ], [ 8, 6 ],
							[ 9, 11 ], [ 10, 30 ], [ 11, 10 ], [ 12, 13 ],
							[ 13, 4 ], [ 14, 3 ], [ 15, 3 ], [ 16, 6 ] ];
					var data2 = [ [ 0, 1 ], [ 1, 0 ], [ 2, 2 ], [ 3, 0 ],
							[ 4, 1 ], [ 5, 3 ], [ 6, 1 ], [ 7, 5 ], [ 8, 2 ],
							[ 9, 3 ], [ 10, 2 ], [ 11, 1 ], [ 12, 0 ],
							[ 13, 2 ], [ 14, 8 ], [ 15, 0 ], [ 16, 0 ] ];
					$("#flot-dashboard-chart").length
							&& $.plot($("#flot-dashboard-chart"), [ data1,
									data2 ], {
								series : {
									lines : {
										show : false,
										fill : true
									},
									splines : {
										show : true,
										tension : 0.4,
										lineWidth : 1,
										fill : 0.4
									},
									points : {
										radius : 0,
										show : true
									},
									shadowSize : 2
								},
								grid : {
									hoverable : true,
									clickable : true,
									tickColor : "#d5d5d5",
									borderWidth : 1,
									color : '#d5d5d5'
								},
								colors : [ "#1ab394", "#1C84C6" ],
								xaxis : {},
								yaxis : {
									ticks : 4
								},
								tooltip : false
							});

					var doughnutData = [ {
						value : 300,
						color : "#a3e1d4",
						highlight : "#1ab394",
						label : "App"
					}, {
						value : 50,
						color : "#dedede",
						highlight : "#1ab394",
						label : "Software"
					}, {
						value : 100,
						color : "#A4CEE8",
						highlight : "#1ab394",
						label : "Laptop"
					} ];

					var doughnutOptions = {
						segmentShowStroke : true,
						segmentStrokeColor : "#fff",
						segmentStrokeWidth : 2,
						percentageInnerCutout : 45, // This is 0 for Pie charts
						animationSteps : 100,
						animationEasing : "easeOutBounce",
						animateRotate : true,
						animateScale : false
					};

/* 					var ctx = document.getElementById("doughnutChart")
							.getContext("2d");
					var DoughnutChart = new Chart(ctx).Doughnut(doughnutData,
							doughnutOptions); */

					var polarData = [ {
						value : 300,
						color : "#a3e1d4",
						highlight : "#1ab394",
						label : "App"
					}, {
						value : 140,
						color : "#dedede",
						highlight : "#1ab394",
						label : "Software"
					}, {
						value : 200,
						color : "#A4CEE8",
						highlight : "#1ab394",
						label : "Laptop"
					} ];

					var polarOptions = {
						scaleShowLabelBackdrop : true,
						scaleBackdropColor : "rgba(255,255,255,0.75)",
						scaleBeginAtZero : true,
						scaleBackdropPaddingY : 1,
						scaleBackdropPaddingX : 1,
						scaleShowLine : true,
						segmentShowStroke : true,
						segmentStrokeColor : "#fff",
						segmentStrokeWidth : 2,
						animationSteps : 100,
						animationEasing : "easeOutBounce",
						animateRotate : true,
						animateScale : false
					};
				/* 	var ctx = document.getElementById("polarChart").getContext("2d");
					var Polarchart = new Chart(ctx).PolarArea(polarData,polarOptions); */

				});
	</script>
</body>
</html>
