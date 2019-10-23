
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
            <div class="pull-left active">系统用户</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="userForm">
					 
                          <div class="form-group">
                          		   <label class="col-sm-2 control-label">登录账号：</label>
                                   <div class="col-sm-2"><input type="text" class="form-control" name="userName" value="${params.userName}"></div>
                                   <label class="col-sm-2 control-label">用户名：</label>
                                   <div class="col-sm-2"><input type="text" class="form-control" name="realName" value="${params.realName}"></div>
                                  	
                                   		
						</div>
						<div class="form-group">
									<label class="col-sm-2 control-label" >邮箱：</label>
                                   <div class="col-sm-2"><input type="email" class="form-control" name="email" value="${params.email}"></div>
									<label class="col-sm-2 control-label">手机号码：</label>
                                   <div class="col-sm-2"><input type="text" class="form-control" pattern="[0-9]{11}"  name="telNo" value="${params.telNo}"></div>	
						</div>
                                 
                          
                                <div class="clearfix lastbottom"></div>
                                
                                   <div class="form-group">
                                   <!-- <div class="col-sm-10 col-sm-offset-13  "> -->
                                   		<label class="col-sm-2 control-label aaa"></label>

                                   		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                        <button class="btn btn-success" type="submit"><span class="glyphicon gly-search"></span>查询</button>
                                        <button class="btn btn-default col-sm-offset-14" type="reset"><span class="glyphicon gly-trash"></span>清空</button> 
                                        <sec:authorize access="hasAuthority('user:insert')">
											<button type="button" class="btn btn-danger col-sm-offset-14" onclick="toAddUser()">
				                                			新增用户
				                            </button>
			                            </sec:authorize>    
                                   <!-- </div> -->
                              	 </div>
                    </form>
				</div>
			</div>
		</div>
				<div class="col-lg-12">
						<div class="ibox ">
							<div class="ibox-content">
								<div class="jqGrid_wrapper">
								<table id="table_list_userList"></table>
								<div id="pager_list_userList"></div>
								</div>
							</div>
						</div>
				</div>
			
	</div>
	
	<title>
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
			function stateFmatter(cellvalue, options, rowObject){  
				<c:forEach var="state" items="${userStateList}">
					  if(cellvalue == '${state.sysValue}'){
						  return '${state.sysName}';
					  }
				 </c:forEach>	
			}
			
			$("#userForm").submit(function(){
				$("#table_list_userList").setGridParam({
				       datatype : 'json',
				       page : 1            //Replace the '1' here
				    }).trigger("reloadGrid");
				return false;
			});
			function getParams(o){
				var data=$("#userForm").serializeArray();
				
			     $.each(data, function() {   
			             o[this.name] = this.value || '';    
			     });  
			}
			
				 
			$(document).ready(function() {
						var lastsel;
			            // 初始化表格
			            $("#table_list_userList").jqGrid({
			            	url:"${ctx}/sysAction/findUsers.do",
			                datatype: "json",
			                mtype: "POST",
			                height:"auto",
			                autowidth: true,
			                shrinkToFit:false,
			                autoScroll: false,
			                page: ${params.pageNo},
			                rowNum: ${params.pageSize},
			                sortname :'${params.sortname}',
			                sortorder:'${params.sortorder}',
			                rowList: [10, 20],
 			                colNames:['操作','编号','登录账号','用户名','邮箱','手机号码','状态'],		               		
 			                colModel: [
 			                	{name:'Detail',index:'Id',width:250,align:"center",sortable:false,frozen:true},
			                   {name: 'id', index: 'id', width: 100 ,align: "center"},
			                   {name: 'userName', index: 'userName',  width: 170,align: "center"},
			                   {name: 'realName', index: 'realName',  width: 170,align: "center"},
			                   {name: 'email', index: 'email',width: 170, align: "center"},
			                   {name: 'telNo', index: 'telNo',width: 170, align: "center"},
			                   {name: 'state', index: 'state',width: 120, align: "center",formatter:  stateFmatter},
			                   
			                ],
			                onSelectRow: function(id){
			            		if(id && id!==lastsel){
			            			jQuery('#table_list_userList').jqGrid('restoreRow',lastsel);
			            			jQuery('#table_list_userList').jqGrid('editRow',id,true);
			            			lastsel=id;
			            		}
			            	},
			            	
			               // multiselect: true,//支持多项选择
			                pager: "#pager_list_userList",
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
			                    var ids=$("#table_list_userList").jqGrid('getDataIDs');
			                    for(var i=0; i<ids.length; i++){
			                        var id=ids[i];   
			                        var detail = "";
			                        <sec:authorize access="hasAuthority('user:update')">
			                        detail += "<a href='javascript:void(0);' class='default-delete' title='编辑' onclick='Modify(" + id + ")'>编辑</a>";
			                        </sec:authorize>
			                        <sec:authorize access="hasAuthority('user:delete')">
			                        detail += "&nbsp;&nbsp;<a href='javascript:void(0);' class='default-delete'  title='删除' onclick='Delete(" + id + ")'>删除</a>";
			                        </sec:authorize>
			                        <sec:authorize access="hasAuthority('user:findUserRigth')">
			                        detail += "&nbsp;&nbsp;<a href='javascript:void(0);' class='default-maintenance'  title='授权' onclick='Privilege(" + id + ")'>授权</a>";
			                        </sec:authorize>
			                        jQuery("#table_list_userList").jqGrid('setRowData', ids[i], { Detail: detail });
			                    }
			                },
			            });
						jQuery("#table_list_userList").jqGrid('setFrozenColumns');
			            // Add responsive to jqGrid
			            $(window).bind('resize', function () {
			                var width = $('.jqGrid_wrapper').width();
			                $('#table_list_userList').setGridWidth(width);
			            });
			});
			function Modify(id) {   //单击修改链接的操作         
		        var model = $("#table_list_userList").jqGrid('getRowData', id);
		        var pageNo = $("#table_list_userList").jqGrid('getGridParam','page');
		        var pageSize = $("#table_list_userList").jqGrid('getGridParam','rowNum');
		        var sortname = $("#table_list_userList").jqGrid('getGridParam','sortname');
		        var sortorder = $("#table_list_userList").jqGrid('getGridParam','sortorder');
				var id = model.id;
				var queryParamsObject = {}; 
				getParams(queryParamsObject);
				queryParamsObject.pageNo = pageNo;
				queryParamsObject.pageSize = pageSize;
				queryParamsObject.sortname = sortname;
				queryParamsObject.sortorder = sortorder;
				//console.info(queryParamsObject);
				var queryParams = $.param(queryParamsObject);
				var encodeQueryParams = $.base64.encode(queryParams);
				//console.info($.base64.decode(encodeQueryParams));
		        location.href='${ctx}/sysAction/toUpdateUser.do?id='+id+"&queryParams="+encodeQueryParams;
			}
			function Privilege(id) {   //单击修改链接的操作         
		        var model = $("#table_list_userList").jqGrid('getRowData', id);
		        var pageNo = $("#table_list_userList").jqGrid('getGridParam','page');
		        var pageSize = $("#table_list_userList").jqGrid('getGridParam','rowNum');
		        var sortname = $("#table_list_userList").jqGrid('getGridParam','sortname');
		        var sortorder = $("#table_list_userList").jqGrid('getGridParam','sortorder');
				var id = model.id;
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
		        location.href='${ctx}/sysAction/toUserRigth.do?userId='+id+"&queryParams="+encodeQueryParams;
			}
			
			function Delete(userId){
				swal({  
				 	title: "是否继续删除用户?", 
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
				    	var model = $("#table_list_userList").jqGrid('getRowData', userId);
						var id = model.id;
						$.post('${ctx}/sysAction/deleteUser.do', 
								{ 'userId': id,'${_csrf.parameterName}':'${_csrf.token}' },
								function(msg) {
									if(!msg.status){
										toastr.error(msg.msg,'错误');
									}else{
										toastr.success(msg.msg,'提示');
										$("#userForm").submit();
									}
								});
				    });
			}
			
			function addUser(){
				var _user_name = $("#userName2").val();
				var _real_name = $("#realName2").val();
				var _email = $("#email2").val();
				var _tel_no = $("#telNo2").val();
				$.post('${ctx}/sysAction/addUser.do', 
						{ userName:_user_name,realName:_real_name,email:_email,telNo:_tel_no,'${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) {
							if(!msg.status){
								//alert(msg.msg);
					                // Display a success toast, with a title
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
								$("#myModalAdd").modal("hide");
								$("#userForm").submit();
							}
						});
			}
			
			
			function toAddUser(){
				location.href='${ctx}/sysAction/toAddUser.do';
			}
			
			
		</script>
			
	
	</title>
	
   </body>