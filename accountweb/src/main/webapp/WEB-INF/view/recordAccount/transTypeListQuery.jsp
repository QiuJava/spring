
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
	<%-- <link href="${ctx}/css/plugins/select2/select2.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/select2/select2-skins.min.css" rel="stylesheet"> --%>
	 <!-- Sweet Alert -->
    <link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">记账处理</div>
            <em class=""></em>
            <div class="pull-left active">交易类型列表查询</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="transTypeForm">
                          <div class="form-group ">
                          	  <label class="col-sm-2 control-label">交易类型编号：</label>
                              <div class="col-sm-2"><input type="text" class="form-control" name="transTypeCode" id="transTypeCode" value="${params.transTypeCode}"></div>
                          	  <label class="col-sm-2 control-label">交易类型名称：</label>
                              <div class="col-sm-2"><input type="text" class="form-control" name="transTypeName" id="transTypeName" value="${params.transTypeName}"></div>
                             
                              
                           </div>
                           <div class="form-group">
                               <label class="col-sm-2 control-label">来源系统：</label>
                                <div class="col-sm-2">
                                  <select class="form-control" name="fromSystem"> 
                                       <option value="ALL" selected="selected">全部</option>
                                       <c:forEach var="fromSystem" items="${fromSystemList}">
                                          <option value="${fromSystem.sysValue}"
                                          <c:if test="${fromSystem.sysValue == params.fromSystem}">selected="selected"</c:if>>
                                          ${fromSystem.sysName}</option>
                                      </c:forEach>
                                  </select> 
                                 </div>
                           </div>
                           <div class="clearfix lastbottom"></div>
	                        <div class="form-group">
	                          <!-- <div class="col-sm-8 col-sm-offset-13  "> -->
                                    <label class="col-sm-2 control-label aaa"></label>
	                          	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	                          	   
	                               <button class="btn btn-success" type="submit"><span class="glyphicon gly-search"></span>查询</button>
	                               <button class="btn btn-default col-sm-offset-14" type="reset"><span class="glyphicon gly-trash"></span>清空</button>
	                          <!-- </div> -->
	                     	</div>
                    </form>
				</div>
			</div>
		</div>
		
		<div class="col-lg-12">
			<div class="ibox ">
				<div class="ibox-content">
					<div class="jqGrid_wrapper" style="">
					<table id="table_list_transType"></table>
					<div id="pager_list_transType"></div>
                    <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
					</div>
				</div>
			</div>
		</div>
		
	</div>
	<!-- 填充内容结束 -->
		
</body>
    
	<title>
	<script src="js/plugins/select2/select2.full.min.js"></script>
	<script src="js/plugins/select2/i18n/zh-CN.js"></script>
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
		$("#transTypeForm").submit(function(){
			$("#table_list_transType").setGridParam({
			       datatype : 'json',
			       page : 1            //Replace the '1' here
			    }).trigger("reloadGrid");
			return false;
		});
		
		function getParams(o){
			var data=$("#transTypeForm").serializeArray();
		     $.each(data, function() {    
		             o[this.name] = this.value || '';    
		     });   
		     //o.subjectNo = $("#subjectNo").select2("val");
		}

		function customFromSystemFmatter(cellvalue, options, rowObject){  
			<c:forEach var="fromSystem" items="${fromSystemList}">
				  if(cellvalue == '${fromSystem.sysValue}'){
					  return '${fromSystem.sysName}';
				  }
			 </c:forEach>	
			 return "" ;
		}
		
		function toTransTypeDetail(id) {   //单击修改链接的操作         
	        var model = $("#table_list_transType").jqGrid('getRowData', id);
	        var pageNo = $("#table_list_transType").jqGrid('getGridParam','page');
	        var pageSize = $("#table_list_transType").jqGrid('getGridParam','rowNum');
	        var sortname = $("#table_list_transType").jqGrid('getGridParam','sortname');
	        var sortorder = $("#table_list_transType").jqGrid('getGridParam','sortorder');
	        
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
	        location.href='${ctx}/recordAccountAction/toTransTypeDetail.do?id='+id+"&queryParams="+encodeQueryParams;
	    }
		
		function toTransTypeUpdate(id) {   //单击修改链接的操作         
	        var model = $("#table_list_transType").jqGrid('getRowData', id);
	        var pageNo = $("#table_list_transType").jqGrid('getGridParam','page');
	        var pageSize = $("#table_list_transType").jqGrid('getGridParam','rowNum');
	        var sortname = $("#table_list_transType").jqGrid('getGridParam','sortname');
	        var sortorder = $("#table_list_transType").jqGrid('getGridParam','sortorder');
	        
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
	        location.href='${ctx}/recordAccountAction/toTransTypeUpdate.do?id='+id+"&queryParams="+encodeQueryParams;
	    }
		$(document).ready(function() {
			var lastsel;
			//var sequenceNo = 0 ;
			var data=$("#transTypeForm").serialize();
			//alert(data);alert('${list[0].getUserType}');
            // 初始化表格
            $("#table_list_transType").jqGrid({
            	url:"${ctx}/recordAccountAction/findTransTypeList.do",
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
                colNames:['操作','交易类型与记账规则定义','交易类型编号','交易类型名称','交易分组','记账规则编号','记账规则名称','来源系统','创建人',],
                colModel: [
                    {name:'Detail',index:'Id',width:210,align:"center",sortable:false,frozen:true},
                    {name: 'id', index: 'id', width: 300, align: "left", sortable:false},
                    {name: 'transTypeCode', index: 'transTypeCode',width: 200, align: "left", sortable:false},
                    {name: 'transTypeName', index: 'transTypeName',width: 200, align: "left", sortable:false},
                    {name: 'transGroup', index: 'transGroup',width: 300, align: "left", sortable:false},
                    {name: 'recordAccountRule.ruleNo', index: 'recordAccountRule.ruleNo',width: 200, align: "left", sortable:false},
                    {name: 'recordAccountRule.ruleName', index: 'recordAccountRule.ruleName',width: 200, align: "left", sortable:false},
                    {name: 'fromSystem', index: 'fromSystem',width: 200, align: "center", sortable:false,formatter:  customFromSystemFmatter},
                    {name: 'creator', index: 'creator',width: 120, align: "left", sortable:false},
                    
                ],
                onSelectRow: function(id){
            		if(id && id!==lastsel){
            			jQuery('#table_list_user').jqGrid('restoreRow',lastsel);
            			jQuery('#table_list_user').jqGrid('editRow',id,true);
            			lastsel=id;
            		}
            	},
                multiselect: false,//支持多项选择
                pager: "#pager_list_transType",
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
                    var ids=$("#table_list_transType").jqGrid('getDataIDs');
                    for(var i=0; i<ids.length; i++){
                        var id=ids[i];   
                        var detail = "" ;
                        detail += "<a href='javascript:void(0);' class='default-details' title='查看' onclick='toTransTypeDetail(" + id + ")'>查看</a>";
                        <sec:authorize access="hasAuthority('transTypeListQuery:update')">  
                        	detail += "&nbsp;&nbsp;<a href='javascript:void(0);' class='default-delete'  title='修改' onclick='toTransTypeUpdate(" + id + ")'>修改</a>";
                        </sec:authorize>
                        <sec:authorize access="hasAuthority('transTypeListQuery:delete')">  
                        	detail += "&nbsp;&nbsp;<a href='javascript:void(0);' class='default-delete'  title='删除' onclick='Delete(" + id + ")'>删除</a>";
                        </sec:authorize>
                        jQuery("#table_list_transType").jqGrid('setRowData', ids[i], { Detail: detail });
                    }
                }
            });

            jQuery("#table_list_transType").jqGrid('setFrozenColumns');
            // Add responsive to jqGrid
            $(window).bind('resize', function () {
                var width = $('.jqGrid_wrapper').width();
                $('#table_list_transType').setGridWidth(width);
            });
		});
		
		
		function Delete(id) {   //单击删除链接的操作         
			thenswal(id);
	    }

		function thenswal(_id){
			//console.info("_id" + _id);
			swal({  
			 	title: "是否继续删除?", 
			   	type: "warning",  
			    showCancelButton: true,   
			    cancelButtonText: "取消",  
			   	confirmButtonColor: "#DD6B55",  
			    confirmButtonText: "继续删除",  
			    closeOnConfirm: false 
			    }, 
			    function(){   
			    	//$.ajax({url:'${ctx}/subjectAction/deleteUserTypeSubject.do?id='+id});
			    	// ajax post handler
			    	$.post('${ctx}/recordAccountAction/transTypeDelete.do', 
						{ 'id' : _id , '${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) {
							if(!msg.state){
								//alert(msg.msg);
					                // Display a success toast, with a title
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
								$("#transTypeForm").submit() ;
							}
							swal.close();
						});
			    });
		}
	
	</script>
	 </title>
</html>  
      