
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<!-- jqGrid plugin -->
	<link href="css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
	<link href="css/plugins/jqGrid/ui.jqgrid.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">系统管理</div>
            <em class=""></em>
            <div class="pull-left active">数据字典</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<div class="ibox-tools">
						<a class="collapse-link"> <i class="fa fa-chevron-up"></i></a> 
					</div>
				</div>
				<div class="ibox-content">
					 <form class="form-horizontal" id="sysDictForm">
					 
                          <div class="form-group">
                      		   <label class="col-sm-1 control-label">字典组：</label>
                               <div class="col-sm-2"><input type="text" class="form-control" name="sysKey"></div>
                               <label class="col-sm-1 control-label">字典名称:</label>
                               <div class="col-sm-2"><input type="text" class="form-control" name="sysName"></div>
                               <label class="col-sm-1 control-label" >状态：</label>
                               <div class="col-sm-2">
  									<select class="form-control" name="status"> 
								         <option value="ALL" selected="selected">全部</option>
								         <option value="0" >无效</option>
										 <option value="1" >有效</option>
									</select>  
							 	</div>
							</div>
                                <div class="clearfix lastbottom"></div>
                                   <div class="form-group">
                                   		<label class="col-sm-1 control-label aaa"></label>

	                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
	                                   		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	                                       <button class="btn btn-success" type="submit"><span class="glyphicon gly-search"></span>查询</button>
	                                       <button class="btn btn-default col-sm-offset-14" type="reset"><span class="glyphicon gly-trash"></span>清空</button>
	                                        
	                                        <sec:authorize access="hasAuthority('sysDict:insert')">
												<button type="button" class="btn btn-danger  col-sm-offset-14" onclick="toAddSysDict()">新增
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
	<script type="text/javascript">
			//去除空格啊
	        $('input').blur(function(){
	            replaceSpace(this);
	        })
	        function replaceSpace(obj){
	            obj.value = obj.value.replace(/\s/gi,'')
	        }
			function statusFmatter(cellvalue, options, rowObject){  
				if("0"==cellvalue){
					return "无效" ;
				}else{
					if("1"==cellvalue){
						return "有效" ;
					}else{
						return "" ;
					}
				}
			}
			
			$("#sysDictForm").submit(function(){
				$("#table_list_userList").setGridParam({
				       datatype : 'json',
				       page : 1            //Replace the '1' here
				    }).trigger("reloadGrid");
				return false;
			});
			function getParams(o){
				var data=$("#sysDictForm").serializeArray();
				
			     $.each(data, function() {   
			             o[this.name] = this.value || '';    
			     });  
			}
			
				 
			$(document).ready(function() {
						var lastsel;
			            // 初始化表格
			            $("#table_list_userList").jqGrid({
			            	url:"${ctx}/sysAction/findSysDictList.do",
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
 			                colNames:['操作','字典组','字典名称','HTML名称','字典值','状态','排序',],		               		
 			                colModel: [
 			                	{name:'Detail',index:'Id',width:200,align:"center",sortable:false,frozen:true},
			                   {name: 'sysKey', index: 'sysKey', width: 300 ,align: "center"},
			                   {name: 'sysName', index: 'sysName',  width: 280,align: "center"},
			                   {name: 'htmlName', index: 'htmlName',  width: 280,align: "center"},
			                   {name: 'sysValue', index: 'sysValue',  width: 200,align: "center"},
			                   {name: 'status', index: 'status',width: 200, align: "center",formatter:  statusFmatter},
			                   {name: 'orderNo', index: 'orderNo',width: 180, align: "center"},
			                   
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
			                        var status = $("#table_list_userList").jqGrid('getRowData', id).status;
			                        var detail = "";
			                        <sec:authorize access="hasAuthority('sysDict:update')">
			                        detail += "<a href='javascript:void(0);' class='default-delete' title='编辑' onclick='Modify(" + id + ")'>编辑</a>";
			                        </sec:authorize>
			                        
			                        // detail += "&nbsp;&nbsp;<a href='javascript:void(0);' class='menu-userAuthorize'  title='失效' onclick='Invalid(" + id + ",this)'>状态</a>";
			                       
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
		        location.href='${ctx}/sysAction/toUpdateSysDict.do?id='+id+"&queryParams="+encodeQueryParams;
			}
			function Invalid(id,obj) {   //单击失效链接的操作         
		        var model = $("#table_list_userList").jqGrid('getRowData', id);
				var status = model.status ;
				var data = {status:'0',id:id} ;

				if(status=="失效"){
					data = {status:'1',id:id} ;
					$.ajax({
						//url:"${ctx}/sysAction/updateStatus.do?status= 1&id='"+id+"'",
						url:"${ctx}/sysAction/updateStatus.do" ,
						dataType: 'json',
						data: data ,
						success :function(msg){
							if(!msg.state){
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
								$("#sysDictForm").submit();
								/* alert( $(obj).parent().html()) ;
								alert( $(obj).parent().firstChild) ;
								alert($(obj).html()) ; */
								$(obj).html("恢复");
								//$(obj).attr("title","恢复") ;
							}
						}
					}); 
				}
				if(status=="有效"){
					$.ajax({
						//url:"${ctx}/sysAction/updateStatus.do?status= 1&id='"+id+"'",
						url:"${ctx}/sysAction/updateStatus.do" ,
						dataType: 'json',
						data: data ,
						success :function(msg){
							if(!msg.state){
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
								$("#sysDictForm").submit();
								/* alert( $(obj).parent().html()) ;
								alert( $(obj).parent().firstChild) ; */
								$(obj).html("失效"); 
							}
						}
					}); 
				}
				
				//parent.location.reload() ; 
				//window.location.reload();
		        //location.href='${ctx}/sysAction/toSysDict.do';
				/* $("#table_list_userList").setGridParam({
					url:"${ctx}/sysAction/findSysDictList.do",
					datatype : 'json',
				    page : 1            //Replace the '1' here
					}).trigger("reloadGrid"); */
			}
			
			
			function toAddSysDict(){
				location.href='${ctx}/sysAction/toAddSysDict.do';
			}
			
			
		</script>
			
	
	</title>
	
   </body>