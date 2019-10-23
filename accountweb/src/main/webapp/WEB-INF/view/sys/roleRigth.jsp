<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<link href="${ctx}/css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">
	<!-- jqGrid plugin -->
	<link href="${ctx}/css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/jqGrid/ui.jqgrid.css" rel="stylesheet">
	<link rel="stylesheet" href="${ctx}/css/plugins/jsTree/style.min.css" />
	<!-- Sweet Alert -->
    <link href="css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">系统管理</div>
            <em class=""></em>
            <div class="pull-left active">系统角色权限</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
	<div class="row">
		<div class="col-lg-6" style="padding-left:25px;padding-right:0px">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					<div style="overflow-y:scroll; height:600px; padding:10px 10px 10px 100px; border:1px solid #eee;">
					<div id="jstree2"></div>
					</div>
				</div>
				
			</div>
		</div>
		<div class="" style="float:left;width:500px;padding-left:25px">
			<div class="ibox  float-e-margins">
				<div class="ibox-content">
					<form method="post" class="form-horizontal" id="roleRigthForm"  role="form">
						<div class="form-group">
							<label class="col-sm-3">角色名称:</label>
							<div class="col-sm-3">
										${role.roleName }
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3">权限列表：</label>
						</div>
						<div class="form-group">
							<div class='checkbox checkbox-inline' style="padding-left:70px">
								<input id="checkboxAll" type="checkbox" value="" onclick="checkBoxAll(this)"/>
								<label for='checkboxAll'>全选</label>
							</div>
						</div>
						<div class="form-group">
							<sec:authorize access="hasAuthority('role:saveRoleRigth')">
							<br/><br/><button type='button' class='btn btn-success' value='' style='margin-right:20px;' onclick='saveRoleRigth()' >保存</button>
							<button id='returnUp' type='button' class=' btn btn-default' onclick='goBack()' value=''><span class='glyphicon gly-return'></span>返回</button>
							</sec:authorize>
						</div>		
					</form>
				</div>
			</div>
		</div>
		
		
	</div>
</div>
	<!-- 填充内容结束 -->
</body>
<title>
   <script src="${ctx}/js/plugins/jsTree/jstree.min.js"></script>
      	<!-- Sweet alert -->
    <script src="js/plugins/sweetalert/sweetalert.min.js"></script>
	<script  type="text/javascript">
	var _roleId = '${role.id}';
	var _menuId = null;
	function onClick2(treeId) {
		var checkboxAllSelected = true;
		_menuId = treeId;
		var timestamp = new Date().getTime();
		if ($("#menuId"+treeId).length > 0) {
				$("#roleRigthForm .menuTree").css("display", "none");
				$("#roleRigthForm #menuId"+treeId).css("display", "block");
				$("#roleRigthForm #menuId"+treeId+" input[type=checkbox]").each(function () {
					if(!($(this).prop('checked'))){
						checkboxAllSelected = false;
					}
				});
				$("#roleRigthForm  #checkboxAll").prop("checked", checkboxAllSelected);
		} else {
			$.getJSON('${ctx}/roleAction/findRolePrivilege.do?menuId=' + treeId + '&roleId=' + _roleId + "&timestamp=" + timestamp, function(data) {
				$("#roleRigthForm .menuTree").css("display", "none");
				var html = "";
				html += "<div class='form-group menuTree' id='menuId" +treeId+ "' data-id='" +treeId+ "' style='padding-left:40px'>";
				$.each(data, function(i, item) {
					var _checked = "";
					if(item.state.selected == true){ 
						_checked="checked";
					}
					else {
						checkboxAllSelected = false;
					}
	            	html += "<div class='checkbox checkbox-inline' >"+
	            		 "<input id='checkbox"+item.id+"' type='checkbox' value='"+item.rigthCode+"' "+_checked+" />" + 
	            		 "<label for='checkbox"+item.id+"'> " + item.text + " </label>"+
	            		 "</div>";
	            	
	        	});
			
				html += "</div>";
				$("#roleRigthForm .form-group:last-child").before(html);
				$("#roleRigthForm .form-group:last-child").prev().children().first().css('padding-left', '30px');
				var al = $("#roleRigthForm .form-group:last-child").prev();
				// console.log(al.childNodes)
				// .checkbox-inline:eq(1)
				// $("#roleRigthForm .form-group:last-child").prev()
				// .css('padding-left', '30px');
				$("#roleRigthForm  #checkboxAll").prop("checked", checkboxAllSelected);
			});
		}
		
	}
	
	$(function () { 
		$('#jstree2').jstree({ 'core' : {
		    'data' : [
		       <c:out value="${menuTree}" escapeXml="false"/>
		    ]
		},
		"plugins" : [ "changed" ]
		});
		
		$('#jstree2').on("changed.jstree", function (e, data) {
			  //console.log(data.selected);
			  if(data.changed.selected.length >0)
	    	  {
			  	onClick2(data.selected);
	    	  }
		});
        var tureWidth = $(".col-sm-3").width();
        $(".combo").width(tureWidth)
		
	 });
	
	function saveRoleRigth(){
		var dataArr = [];
		var data = {};
		var arr = [];
		$('#roleRigthForm .menuTree').each(function () {
			data = {roleId:_roleId.toString(), menuId:"", rightCode:""};
			data.menuId = $(this).data("id").toString();
			$(this).find("input[type=checkbox]").each(function() {
				if($(this).prop('checked')){
					arr.push($(this).val());
				}
			});
			data.rightCode = arr.join(",");
			dataArr.push(data);
		});
		//console.info(dataArr);
		var _csrf_param_name = '${_csrf.parameterName}';
		var _csrf_token = '${_csrf.token}';
		$.post('${ctx}/roleAction/saveRoleRigth.do', 
				{ dataArr: JSON.stringify(dataArr),'${_csrf.parameterName}':'${_csrf.token}' },
				function(msg) {
					if(!msg.status){
						toastr.error(msg.msg,'错误');
					}else{
						toastr.success(msg.msg,'提示');
					}
				});
	}
	
	function checkBoxAll(_this){
		$('#roleRigthForm .menuTree input[type=checkbox]').each(function() {
			if ($(this).is(":hidden")) {
			} else {
				$(this).prop("checked", _this.checked);
			}
		});
	}
	
	function goBack(){
		location.href='${ctx}/roleAction/role.do?queryParams=${params.queryParams}';
	}
	</script>
</title>
</html>

