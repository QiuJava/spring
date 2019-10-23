<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
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
            <div class="pull-left active">修改系统用户</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
	<div class="row">
		<div class="col-lg-8" style="padding-left:25px">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<div class="ibox-tools">
						<a class="collapse-link"> <i class="fa fa-chevron-up"></i></a> 
					</div>
				</div>
				<div class="ibox-content">
					 <form class="form-horizontal" id="sebjectForm">
                          <div class="form-group ">
                          	 	 <label class="col-sm-2 control-label">登录名：</label>
                              	<div class="col-sm-4">
                              		<input type="text"   class="form-control"  name="userName"  id="userName" value="${user.userName}">
                                </div>
                             	<label class="col-sm-2 control-label">用户名：</label>
                             	<div class="col-sm-4">
									<input type="text"   class="form-control"  name="realName"  id="realName" value="${user.realName}">   
								</div>
							</div>
						
                          <div class="form-group">
                           		<label class="col-sm-2 control-label">邮箱：</label>
                                <div class="col-sm-4">
                                	<input type="text"   class="form-control"  name="email"  id="email" value="${user.email}">
                                </div>
                                <label class="col-sm-2 control-label" >电话：</label>
                                <div class="col-sm-4">
										<input type="text"   class="form-control"  name="telNo"  id="telNo"  value="${user.telNo}">       
								 </div>	
                                 
                            </div>
                            <div class="form-group">
                            	<label class="col-sm-2 control-label">部门：</label>
                                <div class="col-sm-4">
                                	<select class="form-control" name="dept"  id="dept"> 
                                	<option value="">请选择</option>
										         <c:forEach var="dept" items="${deptList}">
													<option value="${dept.id}"
													<c:if test="${dept.id == user.deptId}">selected="selected"</c:if>>
													${dept.deptName}</option>
												</c:forEach>
											</select>  
                                </div>
								 <label class="col-sm-2 control-label">状态：</label>
                                <div class="col-sm-4">
											<select class="form-control" name="state"  id="state"> 
										         <c:forEach var="state" items="${userStateList}">
													<option value="${state.sysValue}"
													<c:if test="${state.sysValue == user.state}">selected="selected"</c:if>>
													${state.sysName}</option>
												</c:forEach>
											</select>  
								</div>						
                                 
                            </div>
                            
                                 <div class="form-group">
                                   <div class="col-sm-11 col-sm-offset-1 ">
                                   		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                   		<sec:authorize access="hasAuthority('user:update')">
                                       <button class="btn btn-success" type="button" onclick="updateUser()">保存</button>
                                       </sec:authorize>
                                       <sec:authorize access="hasAuthority('user:resetUserPwd')">
                                       <button class="btn btn-success col-sm-offset-14" type="button" onclick="resetUserPwd()">重置密码</button>
                                       </sec:authorize>
                                       <button class="btn btn-default col-sm-offset-14" type="button" onclick="goBack()"><span class="glyphicon gly-return"></span>返回</button>
                                   </div>
                              	</div>
                    </form>
				</div>
			</div>
		</div>
		
		<div class="col-lg-4" style="padding-left:0px">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<h5>
						修改角色
					</h5>
					<div class="ibox-tools">
						<a class="collapse-link"> <i class="fa fa-chevron-up"></i></a> 
					</div>
				</div>
				<div class="ibox-content">
					<div id="jstree1"></div>
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
	<script type="text/javascript">
        // 去除空格啊
        $('input').blur(function(){
            replaceSpace(this);
        })
        function replaceSpace(obj){
            obj.value = obj.value.replace(/\s/gi,'')
        }
		function goBack(){
			window.location.href='${ctx}/sysAction/toUser.do?queryParams=${params.queryParams}';
		}
		$(function () { 
			$('#jstree1').jstree({ 'core' : {
			    'data' : [
			       <c:out value="${userRoleTree}" escapeXml="false"/>
			    ]
			},
			"checkbox" : {
			    "keep_selected_style" : false
			  },
			"plugins" : [ "wholerow", "checkbox" ]
			});
			
		 });
/* 			function saveUserRole(){
				var nodes = zTreeObj1.getCheckedNodes(true);
		        var ns = $.grep(nodes, function(n,i){
				  return n.id != 'root';
				});
		        var arr = [];
		        $.each(ns,function(key,val){
		        	arr.push(val.id);
		        });
				var _arr_role_id = arr.join(",");
				console.info(_arr_role_id);
				$.post('${ctx}/sysAction/saveUserRole.do', 
						{ userId: '${user.id}',roleId: _arr_role_id,'${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) {
						   if(!msg.state){
								//alert(msg.msg);
					                // Display a success toast, with a title
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
							}
						});
			} */
			
			function updateUser(){
				var nodes = $("#jstree1").jstree("get_checked");
				//console.info(nodes);
		        var ns = $.grep(nodes, function(n,i){
				  return n != 'root';
				});
				var _arr_role_id = ns.join(",");
				//console.info(_arr_role_id);
				var _user_name = $("#userName").val();
				var _real_name = $("#realName").val();
				var _email = $("#email").val();
				var _tel_no = $("#telNo").val();
				var _deptId = $("#dept").val();
				var _state = $("#state").val();
				$.post('${ctx}/sysAction/updateUser.do', 
						{ deptId:_deptId,userId:'${user.id}',userName:_user_name,realName:_real_name,email:_email,telNo:_tel_no,state:_state,roleIds: _arr_role_id,'${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) {
							if(!msg.state){
								//alert(msg.msg);
					                // Display a success toast, with a title
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
								setTimeout(function() {
									location.href="${ctx}/sysAction/toUser.do";
								}, 1000);
							}
						});
			}
			
			
			function resetUserPwd(){
				swal({  
				 	title: "是否继续重置密码?", 
				   	text: "密码将重置为88888888",   
				   	type: "warning",  
				    showCancelButton: true,   
				    cancelButtonText: "取消",  
				   	confirmButtonColor: "#ff3737",  
				    confirmButtonText: "确认重置密码",  
				    closeOnConfirm: true 
				    }, 
				    function(){   
				    	// ajax post handler
				    	$.post('${ctx}/sysAction/resetUserPwd.do', 
								{ 'userId':'${user.id}','${_csrf.parameterName}':'${_csrf.token}' },
								function(msg) {
									if(!msg.state){
										//alert(msg.msg);
							                // Display a success toast, with a title
							            toastr.error(msg.msg,'错误');
									}else{
										toastr.success(msg.msg,'提示');
									}
								});
				    });
			}
	</script>
</title>

</html>

