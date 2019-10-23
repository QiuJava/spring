<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<body>
<center><font size="7">欢迎登录账务系统</font></center>
</body>
<title>
<script type="text/javascript">
	$(document).ready(
		function() {
			collapseMenu();
	});	
	function collapseMenu(){
		 if (localStorageSupport) {
			var user_setting_collapse_menu = 'off';
				<c:if test="${userSetting.collapseMenu == 'on'}">
				user_setting_collapse_menu = 'on'; 
				</c:if>
		    var collapse = localStorage.getItem("collapse_menu:"+_userId);
		    //console.info(collapse);
		    if(collapse == null || collapse != user_setting_collapse_menu){
		    	if (user_setting_collapse_menu == 'off'){
		            $("body").removeClass('mini-navbar');
		            SmoothlyMenu();
		
		            if (localStorageSupport){
		                localStorage.setItem('collapse_menu:<sec:authentication property="principal.userId"></sec:authentication>','off');
		            }
		
		        } else{
		        	$("body").addClass('mini-navbar');
		            SmoothlyMenu();
		
		            if (localStorageSupport){
		                localStorage.setItem('collapse_menu:<sec:authentication property="principal.userId"></sec:authentication>','on');
		            }
		        }
		    }
	        
		 }
	}
</script>
</title>