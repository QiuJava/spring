
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
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">记账处理</div>
            <em class=""></em>
            <div class="pull-left active">记账规则列表查询</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="recordAccountForm">
                          <div class="form-group lastbottom">
                          	  <label class="col-sm-2 control-label">记账规则名称：</label>
                              <div class="col-sm-2"><input type="text" class="form-control" name="ruleName" id="ruleName" value="${params.ruleName}"></div>
                             
                              <label class="col-sm-2 control-label">记账程序：</label>
                              <div class="col-sm-2"><input type="text" class="form-control" name="program" id="program" value="${params.program}"> </div>
                           </div>
                           <div class="clearfix lastbottom"></div>
	                        <div class="form-group">
	                          <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
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
					<table id="table_list_recordAccount"></table>
					<div id="pager_list_recordAccount"></div>
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
	<script type="text/javascript">
		// 去除空格啊
        $('input').blur(function(){
            replaceSpace(this);
        })
        function replaceSpace(obj){
            obj.value = obj.value.replace(/\s/gi,'')
        }
		$("#recordAccountForm").submit(function(){
			$("#table_list_recordAccount").setGridParam({
			       datatype : 'json',
			       page : 1            //Replace the '1' here
			    }).trigger("reloadGrid");
			return false;
		});
		function getParams(o){
			var data=$("#recordAccountForm").serializeArray();
		     $.each(data, function() {    
		             o[this.name] = this.value || '';    
		     });   
		     //o.subjectNo = $("#subjectNo").select2("val");
		}
		
		function toRuleListQueryDetail(id) {   //单击修改链接的操作         
	        var model = $("#table_list_recordAccount").jqGrid('getRowData', id);
	        var pageNo = $("#table_list_recordAccount").jqGrid('getGridParam','page');
	        var pageSize = $("#table_list_recordAccount").jqGrid('getGridParam','rowNum');
	        var sortname = $("#table_list_recordAccount").jqGrid('getGridParam','sortname');
	        var sortorder = $("#table_list_recordAccount").jqGrid('getGridParam','sortorder');
	        var ruleNoq = $("#table_list_recordAccount").jqGrid('getCell',id,'ruleNo') ;
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
	        location.href='${ctx}/recordAccountAction/toRuleListQueryDetail.do?ruleNo='+ruleNoq+"&queryParams="+encodeQueryParams;
	    }
	    function toRuleListQueryUpdate(id) {   //单击修改链接的操作         
	        var model = $("#table_list_recordAccount").jqGrid('getRowData', id);
	        var pageNo = $("#table_list_recordAccount").jqGrid('getGridParam','page');
	        var pageSize = $("#table_list_recordAccount").jqGrid('getGridParam','rowNum');
	        var sortname = $("#table_list_recordAccount").jqGrid('getGridParam','sortname');
	        var sortorder = $("#table_list_recordAccount").jqGrid('getGridParam','sortorder');
	        var ruleNoq = $("#table_list_recordAccount").jqGrid('getCell',id,'ruleNo') ;
			var queryParamsObject = {}; 
			getParams(queryParamsObject);
			queryParamsObject.pageNo = pageNo;
			queryParamsObject.pageSize = pageSize;
			queryParamsObject.sortname = sortname;
			queryParamsObject.sortorder = sortorder;
			//console.info(queryParamsObject);
			var queryParams = $.param(queryParamsObject);
			queryParams = decodeURIComponent(queryParams);
			queryParams = decodeURIComponent(queryParams);
			var encodeQueryParams = $.base64.encode(queryParams);
			//console.info($.base64.decode(encodeQueryParams));
	        location.href='${ctx}/recordAccountAction/toRuleListQueryUpdate.do?ruleNo='+ruleNoq+"&queryParams="+encodeQueryParams;
	    }
		
		$(document).ready(function() {
			var lastsel;
			//var sequenceNo = 0 ;
			var data=$("#recordAccountForm").serialize();
			//alert(data);alert('${list[0].getUserType}');
            // 初始化表格
            $("#table_list_recordAccount").jqGrid({
            	url:"${ctx}/recordAccountAction/findRecordAccountRuleList.do",
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
                colNames:['操作','记账规则编号',  '记账规则名称','记账程序','创建人',],
                colModel: [
                    {name:'Detail',index:'Id',width:140,align:"left",sortable:false,frozen:true},
                    {name: 'ruleNo', index: 'ruleNo', width: 300, align: "left", sortable:false},
                    {name: 'ruleName', index: 'ruleName',width: 400, align: "left", sortable:false},
                    {name: 'program', index: 'program',width: 400, align: "left", sortable:false},
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
                pager: "#pager_list_recordAccount",
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
                    var ids=$("#table_list_recordAccount").jqGrid('getDataIDs');
                    for(var i=0; i<ids.length; i++){
                        var id=ids[i];   
                       	var ruleNoq = $("#table_list_recordAccount").jqGrid('getCell',id,'ruleNo') ;
                        var detail = "" ;
                        <sec:authorize access="hasAuthority('recordAccountRuleListQuery:detail')">
                        detail += "<a href='javascript:void(0);' class='default-details' title='详情' onclick='toRuleListQueryDetail(" + id + ")'>详情</a>";
                        </sec:authorize>
                        <sec:authorize access="hasAuthority('recordAccountRuleListQuery:update')">  
                        	detail += "&nbsp;&nbsp;<a href='javascript:void(0);' class='default-delete'  title='编辑' onclick='toRuleListQueryUpdate(" + id + ")'>编辑</a>";
                        </sec:authorize>
                        jQuery("#table_list_recordAccount").jqGrid('setRowData', ids[i], { Detail: detail });
                    }
                }
            });
            jQuery("#table_list_recordAccount").jqGrid('setFrozenColumns');
            // Add responsive to jqGrid
            $(window).bind('resize', function () {
                var width = $('.jqGrid_wrapper').width();
                $('#table_list_recordAccount').setGridWidth(width);
            });
		});
	</script>
	 </title>
</html>  
      