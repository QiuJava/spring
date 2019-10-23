<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
 
<head>
	<link href="css/plugins/awesome-bootstrap-checkbox/awesome-bootstrap-checkbox.css" rel="stylesheet">
	<!-- jqGrid plugin -->
	<link href="css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
	<link href="css/plugins/jqGrid/ui.jqgrid.css" rel="stylesheet">
	<link rel="stylesheet" href="${ctx}/css/plugins/jsTree/style.min.css" />
	
</head>

<body>

	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left ">系统管理</div>
            <em class=""></em>
            <div class="pull-left ">系统用户</div>
            <em class=""></em>
            <div class="pull-left active">用户授权</div>
        </div>
	</div>


	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-11" style="background:#fff;margin: 0 15px;">
			<div class="ibox float-e-margins">
				<!-- <div class="ibox-title">
						<div class="ibox-tools">
							<a class="collapse-link"> <i class="fa fa-chevron-up"></i></a> 
						</div>
					</div> -->
				<div class="ibox-content pull-left col-lg-5" style="clear:none;">
					<div style="overflow-y:scroll; height:600px; padding:10px 0px 10px 0px; border:1px solid #eee;"><div id="jstree1" class=""></div></div>
				</div>
				<div class="ibox-content pull-left col-lg-6" style="clear:none">
						<form method="post" class="form-horizontal" id="userRigthForm"  role="form">
						<div class="form-group">
							<label class="col-sm-3">用户名:</label>
							<div class="col-sm-3">
										${shiroUser.realName }
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3">权限列表：</label>
						</div>
							<div class="form-group">
									<div class='checkbox checkbox-inline' style="padding-left:60px">
										<input id="checkboxAll" type="checkbox" value="" onclick="checkBoxAll(this)"/>
										<label for='checkboxAll'>全选</label>
									</div>
								</div>
							<div class="form-group">
								<sec:authorize access="hasAuthority('user:saveUserRigth')">
									&nbsp;&nbsp;<button type='button' class='btn btn-success' value='' onclick='saveUserRigth()'>保存</button>
									&nbsp;&nbsp;<button type='button' class='btn btn-default col-sm-offset-14' value='' onclick='goBack()'><span class='glyphicon gly-return'></span>返回</button>
								</sec:authorize>
							
							</div>
						</form>
					</div>
			</div>
		</div>
	</div>
	<!-- 填充内容结束 -->
</body>

<title>
   <script src="${ctx}/js/plugins/jsTree/jstree.min.js"></script>
	<script  type="text/javascript">
	var _userId = null;
	var _data2 = [];
	
	var _parentId = null;
	function onClick1(treeId) {
		//console.info(treeId);
		var checkboxAllSelected = true;
		_parentId = treeId;
		var timestamp = new Date().getTime();
		if ($("#menuId"+treeId).length > 0) {
				$("#userRigthForm .menuTree").css("display", "none");
				$("#userRigthForm #menuId"+treeId).css("display", "block");
				$("#userRigthForm #menuId"+treeId+" input[type=checkbox]").each(function () {
					if(!($(this).prop('checked'))){
						checkboxAllSelected = false;
					}
				});
				$("#userRigthForm  #checkboxAll").prop("checked", checkboxAllSelected);
		} else {
		$.getJSON('${ctx}/sysAction/findUserRigth.do?parentId='+treeId+"&userId=${params.userId}&timestamp="+timestamp, function(data) {
			_data2 = data;
			$("#userRigthForm .menuTree").css("display", "none");
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
	            html += "<div class='checkbox checkbox-inline '>"+
	            		 "<input id='checkbox"+item.id+"' type='checkbox' value='"+item.rigthCode+"' "+_checked+">" + 
	            		 "<label for='checkbox"+item.id+"'> " + item.text + " </label>"+
	            		 "</div>";
	            $(".checkbox-inline:eq(1)").css('padding-left', '30px');
	        });
			html += "<div/>";
			//console.info(html);
			$("#userRigthForm .form-group:last-child").before(html);
			$("#userRigthForm #checkboxAll").prop("checked", checkboxAllSelected);
		});
		}
	}
	
	$(function () { 
		$('#jstree1').jstree({ 'core' : {
		    'data' : [
		       <c:out value="${userRigthTree}" escapeXml="false"/>
		    ]
		},
		"plugins" : [ "changed" ]
		});
		$('#jstree1').on("changed.jstree", function (e, data) {
		      if(data.changed.selected.length >0)
	    	  {
			  	onClick1(data.selected);
	    	  }
		});
	 });
	
	function saveUserRigth(){
		var dataArr = [];
		$('#userRigthForm .menuTree').each(function () {
			var data = {};
			var arr = [];
			data = {userId:'${params.userId}', parentId:"", rightCode:""};
			data.parentId = $(this).data("id").toString();
			$(this).find("input[type=checkbox]").each(function() {
				if($(this).prop('checked')){
					arr.push($(this).val());
				}
			});
			data.rightCode = arr.join(",");
			dataArr.push(data);
		});
		//console.info(dataArr);
		$.post('${ctx}/sysAction/saveUserRigth.do',
				{ dataArr: JSON.stringify(dataArr),'${_csrf.parameterName}':'${_csrf.token}' },
				function(msg) {
					if(!msg.status){
						toastr.error(msg.msg,'错误');
					}else{
						toastr.success(msg.msg,'提示');
					}
				});
		
	}
	function goBack(){
		location.href='${ctx}/sysAction/toUser.do?queryParams=${params.queryParams}';
	}
	function checkBoxAll(_this){
		$('#userRigthForm .menuTree input[type=checkbox]').each(function() {
			if ($(this).is(":hidden")) {
			} else {
				$(this).prop("checked", _this.checked);
			}
		});
	}
	</script>
</title>
</html>

