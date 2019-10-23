
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
	<link href="${ctx}/css/plugins/select2/select2.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/select2/select2-skins.min.css" rel="stylesheet">
	<!-- Sweet Alert -->
    <link href="css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">系统管理</div>
            <em class=""></em>
            <div class="pull-left active">系统角色</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="shiroRoleForm">
                          <div class="form-group lastbottom">
                          	  <label class="col-sm-2 control-label">角色编码：</label>
                              <div class="col-sm-2"><input type="text" class="form-control" name="roleCode" id="roleCode"></div>
                             
                              <label class="col-sm-2 control-label">角色名称：</label>
                              <div class="col-sm-2"><input type="text" class="form-control" name="roleName" id="roleName"> </div>
                           
                            </div>
                                 <div class="form-group">
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                        <label class="col-sm-2 control-label aaa"></label>

                                   	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                   	   
                                       	<button class="btn btn-success" type="submit"><span class="glyphicon gly-search"></span>查询</button>
                                       	<button class="btn btn-default col-sm-offset-14" type="reset"><span class="glyphicon gly-trash"></span>清空</button>
                                       	<sec:authorize access="hasAuthority('role:insert')">
											<button type="button" class="btn btn-danger col-sm-offset-14" data-toggle="modal" data-target="#myModalAdd" style="">新增</button>
										</sec:authorize>
										
                                   <!-- </div> -->
                              	  </div>
                    </form>
                    
                    
                            <div class="modal inmodal" id="myModalAdd" tabindex="-1" role="dialog" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                            <h5 class="modal-title">新增角色</h5>
                                        </div>
                                        <div class="modal-body">
                                           <form method="post" class="form-horizontal" id="addRoleForm">
												<div class="form-group">
													<label class="col-sm-2 control-label">角色编码</label>
													<div class="col-sm-5">
														<input type="text" class="form-control" name="roleCode2" id="roleCode2">
													</div>
													角色编码用字母大写,必须以ROLE_开头 
												</div>
												<div class="form-group">
													<label class="col-sm-2 control-label">角色名称</label>
													<div class="col-sm-5">
														<input type="text" class="form-control" name="roleName2" id="roleName2">
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-2 control-label">备注</label>
													<div class="col-sm-5">
														<input type="text" class="form-control" name="roleRemake2" id="roleRemake2">
													</div>
												</div>
												</form>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                                            <sec:authorize access="hasAuthority('role:insert')">
                                            	<button type="button" class="btn btn-success" onclick="addRole()">保存</button>
                                            </sec:authorize>
                                            <!-- <button type="button" class="btn btn-primary" onclick='$("#myModal2").modal("hide")'>myclose</button> -->
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="modal inmodal" id="myModalUpdate" tabindex="-1" role="dialog" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                            <h5 class="modal-title">修改角色</h5>
                                        </div>
                                        <div class="modal-body">
                                           <form method="post" class="form-horizontal" id="updateRoleForm">
												<div class="form-group">
													<label class="col-sm-2 control-label">角色编码</label>
													<div class="col-sm-5">
														<input type="text" class="form-control" name="roleCode3" id="roleCode3">
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-2 control-label">角色名称</label>
													<div class="col-sm-5">
														<input type="text" class="form-control" name="roleName3" id="roleName3">
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-2 control-label">备注</label>
													<div class="col-sm-5">
														<input type="text" class="form-control" name="roleRemake3" id="roleRemake3">
													</div>
												</div>
												<input type="hidden" class="form-control" name="roleId3" id="roleId3">
												</form>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                                            <sec:authorize access="hasAuthority('role:update')"> 
                                            	<button type="button" class="btn btn-success" onclick="updateRole()">保存</button>
                                            </sec:authorize> 
                                            <!-- <button type="button" class="btn btn-primary" onclick='$("#myModal2").modal("hide")'>myclose</button> -->
                                        </div>
                                    </div>
                                </div>
                            </div>
                    
                    
				</div>
			</div>
		</div>
		
		<div class="col-lg-12">
			<div class="ibox ">
				<div class="ibox-content">
					<div class="jqGrid_wrapper">
					<table id="table_list_shiroRole"></table>
					<div id="pager_list_shiroRole"></div>
                    <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
					</div>
				</div>
			</div>
		</div>
		
	</div>
	<!-- 填充内容结束 -->
		
</body>
    
	<title>
	<script src="${ctx}/js/plugins/select2/select2.full.min.js"></script>
	<script src="${ctx}/js/plugins/select2/i18n/zh-CN.js"></script>
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
		$("#shiroRoleForm").submit(function(){
			$("#table_list_shiroRole").setGridParam({
			       datatype : 'json',
			       page : 1            //Replace the '1' here
			    }).trigger("reloadGrid");
			return false;
		});
		
		function getParams(o){
			var data=$("#shiroRoleForm").serializeArray();
		     $.each(data, function() {    
		             o[this.name] = this.value || '';    
		     });   
		     //o.subjectNo = $("#subjectNo").select2("val");
		}
		
		
		$(document).ready(function() {
			var lastsel;
			//var sequenceNo = 0 ;
			var data=$("#shiroRoleForm").serialize();
			//alert(data);alert('${list[0].getUserType}');
            // 初始化表格
            $("#table_list_shiroRole").jqGrid({
            	url:"${ctx}/roleAction/findAllShiroRoleList.do",
                datatype: "json",	
                mtype: "POST",
                height: 'auto',
                autowidth: true,
                shrinkToFit:false,
                autoScroll: false,
                page: ${params.pageNo},
                rowNum: ${params.pageSize},
                sortname :'${params.sortname}',
                sortorder:'${params.sortorder}',
                rowList: [10, 20],
                colNames:['操作','角色编码',  '角色名称','备注'],
                colModel: [
                    {name:'Detail',index:'Id',width:210,align:"center",sortable:false,frozen:true},
                    {name: 'roleCode', index: 'roleCode', width: 150, align: "center"},
                    {name: 'roleName', index: 'roleName',width: 150, align: "center"},
                    {name: 'roleRemake', index: 'roleRemake',width: 300, align: "center", sortable:false},
                ],
                onSelectRow: function(id){
            		if(id && id!==lastsel){
            			jQuery('#table_list_user').jqGrid('restoreRow',lastsel);
            			jQuery('#table_list_user').jqGrid('editRow',id,true);
            			lastsel=id;
            		}
            	},
                multiselect: false,//支持多项选择
                pager: "#pager_list_shiroRole",
                viewrecords: true,
                hidegrid: false,
                jsonReader : {
        			root : "result",
        			total : "totalPages",
        			page : "pageNo",
        			pageSize : "pageSize",
        			records : "totalCount",
        			repeatitems : false
        		},
        		prmNames : { 
        		    page:"pageNo",
        		    rows:"pageSize"
        		},
        		serializeGridData:function (postData) {
        			getParams(postData);
                    return postData;
                },
                gridComplete:function(){  //在此事件中循环为每一行添加修改和删除链接
                    var ids=$("#table_list_shiroRole").jqGrid('getDataIDs');
                    for(var i=0; i<ids.length; i++){
                        var id=ids[i];   
                       	var roleCodeq = $("#table_list_shiroRole").jqGrid('getCell',id,'roleCode') ;
                        var detail = "" ;
                        <sec:authorize access="hasAuthority('role:update')"> 
                        	detail += "<a href='#' class='default-delete' data-toggle='modal' data-target='#myModalUpdate' onclick='updateRoleModel(" + id + ")' title='编辑'>编辑</a>";
                        </sec:authorize> 
                        <sec:authorize access="hasAuthority('role:delete')">
                        	detail += "&nbsp;&nbsp;<a href='javascript:void(0);' class='default-delete' onclick='deleteRole(" + id + ")' title='删除'>删除</a>";
                        </sec:authorize> 
                        <sec:authorize access="hasAuthority('role:saveRoleRigth')">
                        	detail += "&nbsp;&nbsp;<a href='javascript:void(0);' onclick='toRoleRigth(" + id + ")' class='default-maintenance' title='权限'>权限</a>";
                        </sec:authorize>
                        jQuery("#table_list_shiroRole").jqGrid('setRowData', ids[i], { Detail: detail });
                    }
                }
            });
            jQuery("#table_list_shiroRole").jqGrid('setFrozenColumns');
            // Add responsive to jqGrid
            $(window).bind('resize', function () {
                var width = $('.jqGrid_wrapper').width();
                $('#table_list_shiroRole').setGridWidth(width);
            });
		});
		
		function toRoleRigth(id) {   //单击修改链接的操作         
	        var model = $("#table_list_shiroRole").jqGrid('getRowData', id);
	        var pageNo = $("#table_list_shiroRole").jqGrid('getGridParam','page');
	        var pageSize = $("#table_list_shiroRole").jqGrid('getGridParam','rowNum');
	        var sortname = $("#table_list_shiroRole").jqGrid('getGridParam','sortname');
	        var sortorder = $("#table_list_shiroRole").jqGrid('getGridParam','sortorder');
	        
			var queryParamsObject = {}; 
			getParams(queryParamsObject);
			queryParamsObject.pageNo = pageNo;
			queryParamsObject.pageSize = pageSize;
			queryParamsObject.sortname = sortname;
			queryParamsObject.sortorder = sortorder;
			//console.info(queryParamsObject);
			var queryParams = $.param(queryParamsObject);
			queryParams = decodeURIComponent(queryParams);
			var encodeQueryParams = $.base64.encode(queryParams);
			//console.info($.base64.decode(encodeQueryParams));
	        location.href='${ctx}/roleAction/toRoleRigth.do?id='+id+"&queryParams="+encodeQueryParams;
	    }

		function addRole(){
			var _role_code = $("#roleCode2").val();
			var _role_name = $("#roleName2").val();
			var _role_remake = $("#roleRemake2").val();
			$.post('${ctx}/roleAction/addRole.do', 
					{ roleCode:_role_code,roleName:_role_name,roleRemake:_role_remake,'${_csrf.parameterName}':'${_csrf.token}' },
					function(msg) {
						if(!msg.status){
							toastr.error(msg.msg,'错误');
						}else{
							toastr.success(msg.msg,'提示');
							$("#myModalAdd").modal("hide");
							setTimeout(function() {
								location.href="${ctx}/roleAction/role.do";
							}, 1000);
							
						}
					});
		}

		function updateRoleModel(id) {   //单击修改链接的操作         
	        var model = $("#table_list_shiroRole").jqGrid('getRowData', id);
			$("#roleCode3").val(model.roleCode);
			$("#roleName3").val(model.roleName); 
			$("#roleRemake3").val(model.roleRemake);
			$("#roleId3").val(id);
		}
		
		function updateRole(){
			var _role_code = $("#roleCode3").val();
			var _role_name = $("#roleName3").val();
			var _role_remake = $("#roleRemake3").val();
			var _role_id = $("#roleId3").val();
			$.post('${ctx}/roleAction/updateShiroRoleById.do', 
					{ roleId: _role_id,roleCode:_role_code,roleName:_role_name,roleRemake:_role_remake,'${_csrf.parameterName}':'${_csrf.token}' },
					function(msg) {
						if(!msg.status){
							toastr.error(msg.msg,'错误');
						}else{
							toastr.success(msg.msg,'提示');
							setTimeout(function() {
								location.href="${ctx}/roleAction/role.do";
							}, 1000);
						}
					});
		}
		

		function deleteRole(id){
			//console.info("_id" + _id);
			swal({  
			 	title: "是否继续删除?", 
			   	text: "删除后将不能撤销",   
			   	type: "warning",  
			    showCancelButton: true,   
			    cancelButtonText: "取消",  
			   	confirmButtonColor: "#ff3737",  
			    confirmButtonText: "确认删除",  
			    closeOnConfirm: false 
			    }, 
			    function(){   
			    	// ajax post handler
			    	$.post('${ctx}/roleAction/deleteRoleById.do', 
							{ roleId:id,'${_csrf.parameterName}':'${_csrf.token}' },
							function(msg) {
								if(!msg.status){
									toastr.error(msg.msg,'错误');
								}else{
									toastr.success(msg.msg,'提示');
									setTimeout(function() {
										location.href="${ctx}/roleAction/role.do";
									}, 1000);
								}
								swal.close();
							});
			    });
		}

		
	</script>
	 </title>
</html>  
      