<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>  
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<link href="${ctx}/css/plugins/switchery/switchery.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
        <div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">系统管理</div>
            <em class=""></em>
            <div class="pull-left active">用户设置</div>
        </div>
	</div>
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
                         <form method="post" id="userSettingForm" class="form-horizontal">
							<div class="form-group">
                                <label class="pull-left control-label">回到上一次访问页面:</label>
								<div class="col-sm-2">
									<input type="checkbox" class="js-switch" id="inputBackLastPage" <c:if test="${userSetting.backLastPage == 1 || userSetting.backLastPage == null}">checked</c:if>/> 
								</div>
                                <label class="pull-left control-label">收缩菜单:</label>
                                <div class="col-sm-2">
                                    <input type="checkbox" class="js-switch2" id="collapseMenu" <c:if test="${userSetting.collapseMenu == 'on'}">checked</c:if>/> 
                                </div>
                             </div>
                             <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
						</form>
				</div>	
			</div>
		</div>
	</div>
	<!-- 填充内容结束 -->
	<title>
	<script src="${ctx}/js/plugins/switchery/switchery.js"></script>
		<script type="text/javascript">
		
		$(document).ready(function(){
			var elem = document.querySelector('.js-switch');
        	var switchery = new Switchery(elem, { color: '#1AB394' });
        	$('#inputBackLastPage').change(function () {
        	    var _checked = $(this).prop('checked');
        	    //console.info(_checked);
        	    saveBackLastPage(_checked);
        	 });
        	
        	var elem2 = document.querySelector('.js-switch2');
        	var switchery2 = new Switchery(elem2, { color: '#1AB394' });
        	$('#collapseMenu').change(function () {
        	    var _checked = $(this).prop('checked');
        	    //console.info(_checked);
        	    //save(_checked);
        	    //console.info('collapseMenu');
                if (_checked){
                    $("body").addClass('mini-navbar');
                    SmoothlyMenu();

                    if (localStorageSupport){
                        localStorage.setItem('collapse_menu:<sec:authentication property="principal.userId"></sec:authentication>','on');
                    }

                } else{
                    $("body").removeClass('mini-navbar');
                    SmoothlyMenu();

                    if (localStorageSupport){
                        localStorage.setItem('collapse_menu:<sec:authentication property="principal.userId"></sec:authentication>','off');
                    }
                }
                saveCollapseMenu(_checked);
        	 });
		});
		
		function saveBackLastPage(_checked){
			var _value = 0;
    	    if(_checked){
    	    	_value = 1;
    	    }
    	    $.post('${ctx}/sysAction/saveUserSettingWithBackLastPage.do', 
    				{ 'backLastPage': _value, '${_csrf.parameterName}':'${_csrf.token}' },
    				function(msg) {
    					if(!msg.status){
    						toastr.error(msg.msg,'错误');
    					}else{
    						toastr.success(msg.msg,'提示');
    					}
    				});
		}
		
		function saveCollapseMenu(_checked){
			var _value = 'off';
    	    if(_checked){
    	    	_value = 'on';
    	    }
    	    $.post('${ctx}/sysAction/saveUserSettingWithCollapseMenu.do', 
    				{ 'collapseMenu': _value, '${_csrf.parameterName}':'${_csrf.token}' },
    				function(msg) {
    					if(!msg.status){
    						toastr.error(msg.msg,'错误');
    					}else{
    						toastr.success(msg.msg,'提示');
    					}
    				});
		}
		
	</script>
		</title>
</body>


 