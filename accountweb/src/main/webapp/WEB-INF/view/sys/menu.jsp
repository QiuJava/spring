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
	<link rel="stylesheet" type="text/css" href="css/plugins/easyui/themes/bootstrap/easyui.css">
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
            <div class="pull-left active">系统菜单</div>
        </div>
	</div>


	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
			<div class="col-lg-4">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<div class="ibox-tools">
							<a class="collapse-link"> <i class="fa fa-chevron-up"></i></a> 
						</div>
					</div>
					<div class="ibox-content">
						<div id="jstree1"></div>
					</div>
				</div>
			</div>
			<div class="col-lg-8">
				<div class="ibox float-e-margins ">
					<div class="ibox-title">
						<div class="ibox-tools">
							<a class="collapse-link"> <i class="fa fa-chevron-up"></i></a> 
						</div>
					</div>
					<div class="ibox-content">
						<form method="post" class="form-horizontal" id="updateSysMenuForm">
						<div class="form-group">
							<label class="col-sm-2 control-label">菜单名称：</label>
							<div class="col-sm-3">
								<input type="text" class="form-control" name="menuName1" id="menuName1">
							</div>
							<label class="col-sm-2 control-label">菜单编码：</label>
							<div class="col-sm-3">
								<input type="text" class="form-control" name="menuCode1" id="menuCode1">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">父级菜单：</label>
							<div class="col-sm-3">
								<!-- <input type="text" class="form-control" name="parentId1" id="parentId1"> -->
								<input class="easyui-combotree form-control" data-options="url:'${ctx}/menuAction/menuComboTree.do',method:'get',required:true" style="height:34px"  panelHeight="auto" name="parentId1" id="parentId1">
							</div>
							<label class="col-sm-2 control-label">菜单路径：</label>
							<div class="col-sm-3">
								<input type="text" class="form-control" name="menuUrl1" id="menuUrl1">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">排序：</label>
							<div class="col-sm-3">
								<input type="text" class="form-control" name="orderNo1" id="orderNo1">
							</div>
						</div>
						<div class="form-group">
						<div class="col-sm-11   col-sm-offset-1">
								<sec:authorize access="hasAuthority('menu:insert')">
									<button type="button" class="btn btn-success "  onclick="toAddMenu()">
		                                			新增菜单
		                            </button>
	                            </sec:authorize>
								<sec:authorize access="hasAuthority('menu:update')">
									<button type="button" class=" btn btn-danger col-sm-offset-14" style="background:#ff4f4f;border:0" onclick="updateMenu()">修改</button>
								</sec:authorize>
								<sec:authorize access="hasAuthority('menu:delete')">
									<button type="button" class=" btn btn-primary col-sm-offset-14" value="" style="background:#ff4f4f;border:0" onclick="deleteMenu()" >删除该菜单</button>
								</sec:authorize>
							</div>
						</div>
					</form>
					
						
                            
                            <div class="modal inmodal" id="myModalUpdateFunction" tabindex="-1" role="dialog" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                            <h5 class="modal-title">修改功能</h5>
                                        </div>
                                        <div class="modal-body">
                                           	<form method="post" class="form-horizontal" id="updatePrivilegeForm">
												<div class="form-group">
													<label class="col-sm-2 control-label">功能名称</label>
													<div class="col-sm-4">
														<input type="text" class="form-control" name="subMenuName1" id="subMenuName1">
													</div>
													<label class="col-sm-2 control-label">功能编号</label>
													<div class="col-sm-4">
														<input type="text" class="form-control" name="subMenuCode1" id="subMenuCode1">
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-2 control-label">菜单路径</label>
													<div class="col-sm-4">
														<input type="text" class="form-control" name="subMenuUrl1" id="subMenuUrl1">
													</div>
													<label class="col-sm-2 control-label">排序号</label>
													<div class="col-sm-4">
														<input type="text" class="form-control" name="subOrderNo1" id="subOrderNo1">
													</div>
												</div>
											</form>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                                             <sec:authorize access="hasAuthority('menu.function:update')">
                                            	<button type="button" class="btn btn-success" onclick="updateFunction()">保存</button>
                                             </sec:authorize>
                                            <!-- <button type="button" class="btn btn-primary" onclick='$("#myModal2").modal("hide")'>myclose</button> -->
                                        </div>
                                    </div>
                                </div>
                            </div>
					
							<div class="modal inmodal" id="myModalAddFunction" tabindex="-1" role="dialog" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                            <h5 class="modal-title">新增功能</h5>
                                        </div>
                                        <div class="modal-body">
                                           	<form method="post" class="form-horizontal" id="updatePrivilegeForm2">
												<div class="form-group">
													<label class="col-sm-2 control-label">功能名称</label>
													<div class="col-sm-4">
														<input type="text" class="form-control" name="subMenuName2" id="subMenuName2" required="">
													</div>
													<label class="col-sm-2 control-label">功能编号</label>
													<div class="col-sm-4">
														<input type="text" class="form-control" name="subMenuCode2" id="subMenuCode2" required="">
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-2 control-label">菜单路径</label>
													<div class="col-sm-4">
														<input type="text" class="form-control" name="subMenuUrl2" id="subMenuUrl2">
													</div>
													<label class="col-sm-2 control-label">排序号</label>
													<div class="col-sm-4">
														<input type="text" class="form-control" name="subOrderNo2" id="subOrderNo2" required="">
													</div>
												</div>
											</form>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                                            <sec:authorize access="hasAuthority('menu.function:insert')">
                                            	<button type="button" class="btn btn-success" onclick="addFunction()">保存</button>
                                            </sec:authorize>
                                            <!-- <button type="button" class="btn btn-primary" onclick='$("#myModal2").modal("hide")'>myclose</button> -->
                                        </div>
                                    </div>
                                </div>
                            </div>
					</div>
				</div>
			
			
				<div class="ibox ">
							<div class="ibox-content">
								<div class="jqGrid_wrapper">
								<sec:authorize access="hasAuthority('menu.function:insert')">
								<div class="form-group">
									<input type="button" class=" btn btn-success btn-sm " value="新增功能" data-toggle='modal' data-target='#myModalAddFunction'>
									</div>
								</sec:authorize>
								<table id="table_list_menu"></table>
								<div id="pager_list_menu"></div>
								</div>
							</div>
						</div>
			</div>
	</div>
	<!-- 填充内容结束 -->
</body>


<title>
<script src="${ctx}/js/plugins/jsTree/jstree.min.js"></script>
   <script type="text/javascript" src="${ctx}/js/jquery.easyui.min.js"></script>
   	<!-- Sweet alert -->
    <script src="js/plugins/sweetalert/sweetalert.min.js"></script>
	<script  type="text/javascript">
    // 去除空格啊
        $('input').blur(function(){
            replaceSpace(this);
        })
        function replaceSpace(obj){
            obj.value = obj.value.replace(/\s/gi,'')
        }
	var _menuId = '-1';
	$(function () { 
		$('#jstree1').jstree({ 
		'core': {
			'data': {
            	'url': '${ctx}/sysAction/menuTree.do',  //异步加载jstree html格式的数据地址
                'data':function(data){  
                	var zNodes2 = eval(data);
                	return zNodes2;
                } 
            }
        } 
        });
		
		$('#jstree1').on("changed.jstree", function (e, data) {
			  onClick1(data.selected);
		});
        var tureWidth = $(".col-sm-3").width();
        $(".combo").width(tureWidth)
	 });
	
	function onClick1(treeId) {
		_menuId = treeId;
		var timestamp = new Date().getTime();
		$.getJSON('${ctx}/sysAction/findMenuById.do?menuId='+treeId+'&timestamp='+timestamp, function(data) {
				$("#menuName1").val(data.menuName);
				$("#menuCode1").val(data.menuCode);
				$("#menuUrl1").val(data.menuUrl);
				$("#menuType1").val(data.menuType);
				//$("#parentId1").val(data.parentId);
				$('#parentId1').combotree('setValue',data.parentId);
				$("#orderNo1").val(data.orderNo);
		});
		//jqgrid reload
		$("#table_list_menu").setGridParam({
		       datatype : 'json',
		       page : 1            //Replace the '1' here
		    }).trigger("reloadGrid");
		
	}	
	
	function updateMenu(){
		var _menu_name = $("#menuName1").val();
		var _menu_code = $("#menuCode1").val();
		var _menu_parent_id = $('#parentId1').combotree('getValue');
		var _menu_url = $("#menuUrl1").val();
		var _order_no = $("#orderNo1").val();
		$.post('${ctx}/menuAction/updateMenu.do', 
				{ menuId: _menuId.toString(),menuName:_menu_name,menuCode:_menu_code,parentId:_menu_parent_id,menuUrl:_menu_url,orderNo:_order_no,'${_csrf.parameterName}':'${_csrf.token}' },
				function(msg) {
					if(!msg.state){
			            toastr.error(msg.msg,'错误');
					}else{
						$('#jstree1').data('jstree', false).empty();
						$('#jstree1').jstree({ 
							'core': {
							'data': {
            					'url': '${ctx}/sysAction/menuTree.do?menuId='+_menuId,  //异步加载jstree html格式的数据地址
                				'data':function(data){  
                					var zNodes2 = eval(data);
                					return zNodes2;
                				} 
            				}
        				} 
        				});
						toastr.success(msg.msg,'提示');
					}
				});
	}

	var _sub_menu_id = null;
	function modifyFunction(id) {   //单击修改链接的操作         
		_sub_menu_id = id;
        var model = $("#table_list_menu").jqGrid('getRowData', id);
		$("#subMenuName1").val(model.menuName);
		$("#subMenuCode1").val(model.menuCode);
		$("#subOrderNo1").val(model.orderNo);
	}
	
	function updateFunction(){
		var _sub_menu_name = $("#subMenuName1").val();
		var _sub_menu_code = $("#subMenuCode1").val();
		var _sub_menu_url = $("#subMenuUrl1").val();
		var _sub_order_no = $("#subOrderNo1").val();
		$.post('${ctx}/sysAction/updateFunction.do', 
				{ id:_sub_menu_id,menuName:_sub_menu_name,menuCode:_sub_menu_code,menuUrl:_sub_menu_url,orderNo:_sub_order_no,'${_csrf.parameterName}':'${_csrf.token}' },
				function(msg) {
					if(!msg.state){
						//alert(msg.msg);
			            toastr.error(msg.msg,'错误');
					}else{
						toastr.success(msg.msg,'提示');
						$("#myModalUpdateFunction").modal("hide");
					}
				});
	}
	
	function addFunction(){
		var _sub_menu_name = $("#subMenuName2").val();
		var _sub_menu_code = $("#subMenuCode2").val();
		var _sub_menu_url = $("#subMenuUrl2").val();
		var _sub_order_no = $("#subOrderNo2").val();
		$.post('${ctx}/sysAction/addFunction.do', 
				{ menuId:_menuId.toString(),menuName:_sub_menu_name,menuCode:_sub_menu_code,menuUrl:_sub_menu_url,orderNo:_sub_order_no,'${_csrf.parameterName}':'${_csrf.token}' },
				function(msg) {
					if(!msg.status){
			            toastr.error(msg.msg,'错误');
					}else{
						toastr.success(msg.msg,'提示');
						$("#updatePrivilegeForm2")[0].reset();
						$("#myModalAddFunction").modal("hide");
						//jqgrid reload
						$("#table_list_menu").setGridParam({
		       				datatype : 'json',
		       				page : 1            //Replace the '1' here
		    			}).trigger("reloadGrid");
					}
				});
	}
	
	function deleteFunction(id){
		thenswal(id);
	}
	function deleteMenu(){
		swal({  
		 	title: "是否继续删除菜单?", 
		   	text: "将删除本菜单及所有子菜单,操作将不能撤销",   
		   	type: "warning",  
		    showCancelButton: true,   
		    cancelButtonText: "取消",  
		   	confirmButtonColor: "#ff3737",  
		    confirmButtonText: "确认删除",  
		    closeOnConfirm: true 
		    }, 
		    function(){   
		    	// ajax post handler
		    	$.post('${ctx}/menuAction/deleteMenu.do', 
						{ 'menuId':_menuId.toString(),'${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) {
							if(!msg.status){
								toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
								$('#jstree1').data('jstree', false).empty();
								$('#jstree1').jstree({ 
									'core': {
									'data': {
            						'url': '${ctx}/sysAction/menuTree.do',  //异步加载jstree html格式的数据地址
                					'data':function(data){  
                						var zNodes2 = eval(data);
                						return zNodes2;
                					} 
            					}
        					} 
        					});
							}
						});
		    });
	}
	
	function getParams(o){
	      o.menuId = _menuId.toString() || '' ; 
	      o['${_csrf.parameterName}'] = '${_csrf.token}';    
	}
	
	$(document).ready(function() {
		var lastsel;
		//var data=$("#subjectForm").serialize();
		//alert(data);
        // 初始化表格
        $("#table_list_menu").jqGrid({
        	url:"sysAction/findMenuFunctionList.do",
            datatype: "json",
            mtype: "POST",
            height:"auto",
            autowidth: true,
            shrinkToFit:false,
            autoScroll: false,
            rowNum: 10,
            rowList: [10, 20],
            colNames:['功能名称', '功能编号', '功能路径', '排序号','操作'],
            colModel: [
                {name: 'menuName', index: 'menuName', width: 120, align: "center"},
                {name: 'menuCode', index: 'menuCode', width: 120, align: "center"},
                {name: 'menuUrl', index: 'menuUrl', width: 250, align: "center"},
                {name: 'orderNo', index: 'orderNo',width: 100, align: "center"},
                {name:'Detail',index:'Id',width:150,align:"center",sortable:false},
                
            ],
            onSelectRow: function(id){
        		if(id && id!==lastsel){
        			jQuery('#table_list_user').jqGrid('restoreRow',lastsel);
        			jQuery('#table_list_user').jqGrid('editRow',id,true);
        			lastsel=id;
        		}
        	},
            multiselect: false,//支持多项选择
            //pager: "#pager_list_menu",
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
                var ids=$("#table_list_menu").jqGrid('getDataIDs');
                for(var i=0; i<ids.length; i++){
                    var id=ids[i];   
                    var detail="";
                    /* <sec:authorize access="hasAuthority('menu.function:update')">
                    detail += "<a href='#' class='default-delete' data-toggle='modal' data-target='#myModalUpdateFunction' title='修改' onclick='modifyFunction(" + id + ")'>修改</a>";
                    </sec:authorize> */
                    <sec:authorize access="hasAuthority('menu.function:delete')">
                    detail += "&nbsp;&nbsp;<a href='javascript:void(0);' class='default-delete' title='删除' onclick='deleteFunction(" + id + ")'>删除</a>";
                    </sec:authorize>
                    jQuery("#table_list_menu").jqGrid('setRowData', ids[i], { Detail: detail });
                }
            }
        });
        // Add responsive to jqGrid
        $(window).bind('resize', function () {
            var width = $('.jqGrid_wrapper').width();
            $('#table_list_menu').setGridWidth(width);
        });
	});
	
	function toAddMenu(){
		location.href='${ctx}/menuAction/toAddMenu.do';
	}
	
	
	function thenswal(_menuId){
		//console.info("_id" + _id);
		swal({  
		 	title: "是否继续删除?", 
		   	text: "删除后将不能撤销",   
		   	type: "warning",  
		    showCancelButton: true,   
		    cancelButtonText: "取消",  
		   	confirmButtonColor: "#ff3737",  
		    confirmButtonText: "确认删除",  
		    closeOnConfirm: true 
		    }, 
		    function(){   
		    	// ajax post handler
		    	$.post('${ctx}/sysAction/deleteFunction.do', 
						{ 'menuId':_menuId.toString(),'${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) {
							if(!msg.status){
								toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
								//jqgrid reload
								$("#table_list_menu").setGridParam({
		       						datatype : 'json',
		       						page : 1            //Replace the '1' here
		    					}).trigger("reloadGrid");
							}
						});
		    });
	}
	
	</script>
</title>
</html>

