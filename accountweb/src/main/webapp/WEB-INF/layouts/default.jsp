<%@page language="java" contentType="text/html; charset=UTF-8" isErrorPage="true"  pageEncoding="UTF-8" %>
<!DOCTYPE html>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>    
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>

<head>
<base href="${ctx}/" />
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge">

<title>移联支付账务系统</title>
<script type="text/javascript">var base="${ctx}/"</script>
<script language="javascript" type="text/javascript"  src="${ctx}/js/My97DatePicker/WdatePicker.js"></script>
<link href="${ctx}/favicon.ico" rel="shortcut icon" type="image/x-icon">

<link href="${ctx}/css/bootstrap.min.css" rel="stylesheet">
<link href="${ctx}/font-awesome/css/font-awesome.css" rel="stylesheet">
<!-- 消息提示 样式-->
<link href="${ctx}/css/plugins/toastr/toastr.min.css" rel="stylesheet">
<!-- 提示后逐渐消失 -->
<link href="${ctx}/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
<sitemesh:head/>
<link href="${ctx}/css/animate.css" rel="stylesheet">
<link href="${ctx}/css/style.css?version=20160070801" rel="stylesheet">
<script type="text/javascript">
var _userId = '<sec:authentication property="principal.userId"></sec:authentication>';
var _contextPath = '${ctx}';
var _expiredUrl ='${ctx}/login.do?expired';
</script>

</head>

<body class="no-skin-config">
	<div id="wrapper">
		<!-- 导航开始-->
		<nav class="navbar-default navbar-static-side" role="navigation">
			<ul class="nav metismenu" id="side-menu">
				<!-- 导航头开始 -->
				<li class="nav-header">
					<div class="dropdown profile-element">
						<div class="user-pic"> 
							<img alt="image" class="img-rounded" src="${ctx}/img/profile_small.jpg" />
						</div>
                        <p class="username"><sec:authentication property="principal.realName"></sec:authentication></p>
					</div>
				</li>
				<!-- 导航头结束 -->
				
				<!-- 导航列表开始 -->
				<jsp:include page="/menuAction/leftMenu.do"></jsp:include>
				
			</ul>
		</nav>
		<!-- 导航结束 -->
		
		<!-- 主要内容开始 -->
		<div id="page-wrapper" class="gray-bg dashbard-1">
			<div class="row border-bottom">
				<nav class="navbar navbar-static-top" role="navigation"
					style="margin-bottom: 0">
					<div class="navbar-header" style="padding:0px 0;">
						<a class="navbar-minimalize minimalize-styl-2 btn btn-success " style="padding:6px 12px" href="javascript:doResize();"><i class="fa fa-bars"></i> </a>
						<form role="search" class="navbar-form-custom"
							action="search_results.html">
							<div class="form-group">
								<p id="top-search">账户及清结算系统 <span>V2.2.001</span></p>
								<!-- <input type="text" placeholder="移付宝账户及清结算系统" 
									class="form-control" name="top-search" id="top-search" readonly="readonly"> -->
							</div>
						</form>
					</div>
					
					
					<ul class="nav navbar-top-links navbar-right">
						<!-- <li>
							<span class="m-r-sm text-muted welcome-message">欢迎 <sec:authentication property="principal.realName"></sec:authentication></span>
						</li> -->
						<li class="dropdown default-data">
							<a class="dropdown-toggle count-info" data-toggle="dropdown" href="#"> 
								<i class="fa fa-calendar"></i>
								<span class="label " id="default-data"><jsp:include page="/currentDate.do"></jsp:include></span>
							</a>
						</li>
						
						<li class="default-out">
								<c:url value="/logout.do" var="logoutUrl" />
								<form action="${logoutUrl}" method="post" id="form_logout">
									<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
									<a href="javascript:$('#form_logout').submit()"><i class="fa fa-sign-out"></i>退出</a>
								</form>
						</li>
						<!-- <li>
							<a class="right-sidebar-toggle">
								<i class="fa fa-tasks"></i>
							</a>
						</li> -->
					</ul>
				</nav>
			</div>
		
			 <sitemesh:body />
			 
			<!--  <div class="footer fixed">
	            <div class="pull-right">
	                	深圳移付宝科技有限公司
	            </div>
	            <div>
	                <strong>Copyright</strong> Eeepay Company &copy; 2015-2016
	            </div>
        	</div> -->
		</div>
		<!-- 主要内容结束 -->
	</div>

	<!-- Mainly scripts -->
	<script src="${ctx}/js/jquery-2.1.1.js"></script>
	<!-- jQuery UI -->
	<script src="${ctx}/js/plugins/jquery-ui/jquery-ui.min.js"></script>
	<script src="${ctx}/js/bootstrap.min.js"></script>
	<script src="${ctx}/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="${ctx}/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<!-- jQuery base64 -->
	<script src="${ctx}/js/plugins/jquery-base64/jquery.base64.js"></script>
	<script type="text/javascript">
	//$(document).ready(
	//	function() {
				//if (localStorageSupport){
			        //var current_href = localStorage.getItem('current_href:<sec:authentication property="principal.userId"></sec:authentication>');
			        //console.debug('a:'+current_href);
			        //var nav_node = $('.nav-second-level > li').has("a[href='${ctx}"+current_href+"']");
			        //console.debug(nav_node);
			        /* if(nav_node.length > 0){
			        	nav_node.parent().parent().addClass("active");
			        	nav_node.addClass("active");
			        } */
			    //}
				
				/* var currentDate = '<jsp:include page="/currentDate.do"></jsp:include>';
		        $("#default-data").html(currentDate); */
	// });	
	</script>
	<!-- Flot -->
	<script src="${ctx}/js/plugins/flot/jquery.flot.js"></script>
	<script src="${ctx}/js/plugins/flot/jquery.flot.tooltip.min.js"></script>
	<script src="${ctx}/js/plugins/flot/jquery.flot.spline.js"></script>
	<script src="${ctx}/js/plugins/flot/jquery.flot.resize.js"></script>
	<script src="${ctx}/js/plugins/flot/jquery.flot.pie.js"></script>

	<!-- Peity -->
	<script src="${ctx}/js/plugins/peity/jquery.peity.min.js"></script>
	<script src="${ctx}/js/demo/peity-demo.js"></script>

	<!-- Custom and plugin javascript -->
	<script src="${ctx}/js/inspinia.js?version=20160091301"></script>
	<script src="${ctx}/js/plugins/pace/pace.min.js"></script>
	
	<script src="${ctx}/js/plugins/jqGrid/i18n/grid.locale-cn.js"></script>
    <script src="${ctx}/js/plugins/jqGrid/jquery.jqGrid.min.js"></script>
    

	<!-- GITTER -->
	<script src="${ctx}/js/plugins/gritter/jquery.gritter.min.js"></script>

	<!-- Sparkline -->
	<script src="${ctx}/js/plugins/sparkline/jquery.sparkline.min.js"></script>

	<!-- Sparkline demo data  -->
	<script src="${ctx}/js/demo/sparkline-demo.js"></script>

	<!-- ChartJS-->
	<script src="${ctx}/js/plugins/chartJs/Chart.min.js"></script>

	<!-- Toastr -->
	<script src="${ctx}/js/plugins/toastr/toastr.min.js"></script>
	
	
	<script type="text/javascript">
		
		$(document).ready(
				function() {
					//$.jgrid.ajaxOptions.type = 'post';
					$.base64.utf8encode = true;  
					$.base64.utf8decode = true;
				
					setTimeout(function() {
						toastr.options = {
							closeButton : true,
							progressBar : true,
							showMethod : 'slideDown',
							timeOut : 4000
						};
						//toastr.success('欢迎登陆账务系统');
					}, 300);

					 /* $('.nav-second-level > li > a').click(function (){
					  		var current_href = $(this).attr('href');
					  		current_href = current_href.replace(_contextPath,'');
				            if (localStorageSupport){
				                localStorage.setItem('current_href:<sec:authentication property="principal.userId"></sec:authentication>',current_href);
				            }
				  	 }); */
				});
		

		
				Date.prototype.format = function(format){ 
					var o = { 
						"M+" : this.getMonth()+1, //month 
						"d+" : this.getDate(), //day 
						"h+" : this.getHours(), //hour 
						"m+" : this.getMinutes(), //minute 
						"s+" : this.getSeconds(), //second 
						"q+" : Math.floor((this.getMonth()+3)/3), //quarter 
						"S" : this.getMilliseconds() //millisecond 
					} 

					if(/(y+)/.test(format)) { 
						format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
					} 
		
					for(var k in o) { 
						if(new RegExp("("+ k +")").test(format)) { 
							format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
						} 
					} 
					return format; 
				}
				
				function myFormatDate(ts,type){
					var toString="";
					if(ts==null||ts=="") return toString;
					if(typeof(ts)=="string")
						ts=parseInt(ts);
					return new Date(ts).format(type); 
				}
				function doResize(){
					setTimeout(function() {$(window).resize();}, 300);
				}
				$(document).ajaxError(function(event, xhr, settings, error) {
					var optStatus = xhr.getResponseHeader('optStatus');
					//console.info(optStatus);
                    if(optStatus == 'expired') {  
                        location.href = _expiredUrl;              
                    }
                    if(optStatus == 'noRigth') {  
                        toastr.error("你没有权限操作.",'错误');
                    }
				});
				
	</script>
	<sitemesh:title/>
	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
	<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
	<!--[if lt IE 9]>
	  <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
	  <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
	<![endif]-->
</body>
</html>
