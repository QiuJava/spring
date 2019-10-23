<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
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
            <div class="pull-left">系统管理</div>
            <em class=""></em>
            <div class="pull-left active">新增系统用户</div>
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
					 <form class="form-horizontal" id="shiroUserForm">
                          <div class="form-group ">
                          	 	 <label class="col-sm-2 control-label">登录名:</label>
                              	<div class="col-sm-3">
                              		<input type="text" class="form-control" name="userName" id="userName" required>
                                </div>
                             	<label class="col-sm-2 control-label">用户名：</label>
                             	<div class="col-sm-3">
									<input type="text" class="form-control" name="realName" id="realName" required>   
								</div>
							</div>
						
                          <div class="form-group">
                           		<label class="col-sm-2 control-label">邮箱：</label>
                                <div class="col-sm-3">
                                	<input type="text" class="form-control"  name="email"  id="email">
                                </div>
                                <label class="col-sm-2 control-label" >电话：</label>
                                <div class="col-sm-3">
										<input type="text" class="form-control"  name="telNo"  id="telNo">       
								 </div>	
                                 
                            </div>
                            
                            <div class="form-group">
                           		<label class="col-sm-2 control-label">部门：</label>
                                <div class="col-sm-3">
                                	<select class="form-control" name="dept"  id="dept" required> 
                                	<option value="" selected="selected">请选择</option>
										         <c:forEach var="dept" items="${deptList}">
													<option value="${dept.id}">
													${dept.deptName}</option>
												</c:forEach>
											</select>  
                                </div>
                                <label class="col-sm-2 control-label">状态：</label>
                                <div class="col-sm-3">
									<select class="form-control" name="state"  id="state"> 
										<c:forEach var="state" items="${userStateList}">
											<option value="${state.sysValue}">${state.sysName}</option>
										</c:forEach>
									</select>  
								</div>
                                 
                            </div>
                            	
                                 <div class="form-group">
                                  
                                   <div class="col-sm-10 col-sm-offset-2 ">
                                  
                                   		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                       <button class="btn btn-success" type="submit"><span class="glyphicon gly-ok"></span>提交</button>
                                       <button class="btn btn-default col-sm-offset-14" type="button" onclick="goBack()"><span class="glyphicon gly-return"></span>返回</button>
                                   </div>
                              	</div>
                              	<p class="bg-info">默认密码 88888888</p>
                    </form>
				</div>
			</div>
		</div>
		
		<div class="col-lg-4" >
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<h5>
						角色列表
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
	<script type="text/javascript">
        // 去除空格啊
        $('input').blur(function(){
            replaceSpace(this);
        })
        function replaceSpace(obj){
            obj.value = obj.value.replace(/\s/gi,'')
        }
		function goBack(){
			location.href='${ctx}/sysAction/toUser.do';
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
			"plugins" : [ "checkbox" ]
			});
			
		 });			
		
		$("#shiroUserForm").submit(function() {
			var nodes = $("#jstree1").jstree("get_checked");
				//console.info(nodes);
		        var ns = $.grep(nodes, function(n,i){
				  return n != 'root';
				});
				var _arr_role_id = ns.join(",");
				//console.info(_arr_role_id);
				var _deptId = $("#dept").val();
				var _user_name = $("#userName").val();
				var _real_name = $("#realName").val();
				var _email = $("#email").val();
				var _tel_no = $("#telNo").val();
				var _state = $("#state").val();
				$.post('${ctx}/sysAction/addUser.do', 
						{ deptId: _deptId,userName:_user_name,realName:_real_name,email:_email,telNo:_tel_no,roleIds: _arr_role_id,'${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) {
							if(!msg.status){
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
			return false;
		});
	</script>
</title>

</html>

