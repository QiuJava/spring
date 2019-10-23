
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
	
	<link href="${ctx}/css/plugins/bootstrap-datepicker/bootstrap-datepicker3.min.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10" >
			<div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">报表管理</div>
            <em class=""></em>
            <div class="pull-left active">批处理管理</div>
		</div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					<form class="form-horizontal" id="batchesForm">
						<div class="form-group">
							<label class="col-sm-2 control-label">当前系统状态：</label>
							<div class="col-sm-2">
								<label class="pull-left control-label">${systemStatus}</label>
							</div>
                            <label class="col-sm-2 control-label">日期：</label>
                            <div class="col-sm-4">
                                <div class="input-daterange input-group" id="datepicker">
                                    <input type="text" class="input-sm form-control" name="start"  value="${params.start}"/>
                                    <span class="input-group-addon">~</span>
                                    <input type="text" class="input-sm form-control" name="end" value="${params.end}"/>
                                </div>   
                            </div>
						</div>
						
						<div class="clearfix lastbottom"></div>
                        <div class="form-group">
                                <label class="col-sm-2 control-label aaa"></label>

                        	<!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                            	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                <button id="submitBut" type="submit" class=" btn btn-success " value="" ><span class="glyphicon gly-search"></span>查询</button>
                                <button class="btn btn-default col-sm-offset-14" type="reset"><span class="glyphicon gly-trash"></span>清空</button>
                            <!-- </div>                               -->
                        </div>
					</form>
				</div>
			</div>
		</div>
		
		<div class="col-lg-12">
			<div class="ibox ">
				<div class="ibox-content">
					<div class="jqGrid_wrapper">
						<table id="table_list_batches"></table>
						<div id="pager_list_batches"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	
	<title>
	<script src="${ctx}/js/plugins/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
	<script src="${ctx}/js/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js"></script>
	
	<script>
		$("#batchesForm").submit(function(){
			$("#table_list_batches").setGridParam({
				datatype : 'json',
				page : 1            //Replace the '1' here
			}).trigger("reloadGrid");
			return false;
		});
		
		function customStatusFormatter(cellvalue, options, rowObject) {
			if (cellvalue == 1) {
				return "成功";
			} else {
				return "失败";
			}
		}
		function getParams(o){
			var data=$("#batchesForm").serializeArray();
		     $.each(data, function() {    
		             o[this.name] = this.value || '';    
		     });   
		}
		
		function Detail(id) {   //单击修改链接的操作         
	        var model = $("#table_list_batches").jqGrid('getRowData', id);
	        var pageNo = $("#table_list_batches").jqGrid('getGridParam','page');
	        var pageSize = $("#table_list_batches").jqGrid('getGridParam','rowNum');
	        var sortname = $("#table_list_batches").jqGrid('getGridParam','sortname');
	        var sortorder = $("#table_list_batches").jqGrid('getGridParam','sortorder');
	        
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
	        location.href='${ctx}/reportAction/toBatchesDetail.do?batchesId='+id+"&queryParams="+encodeQueryParams;
	    }
		$(document).ready(function() {
			var lastsel;
			var data=$("#batchesForm").serialize();
            // 初始化表格
            $("#table_list_batches").jqGrid({
            	url:"${ctx}/reportAction/findBatchesList.do",
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
                colNames:['操作','跑批名称','执行时间','交易日','执行结果' ],
                colModel: [
                    {name:'Detail',index:'id',width:100,align:"center",sortable:false,frozen:true},
                    {name: 'name', index: 'name', width: 180, align: "center"},
                    {name: 'createTime', index: 'createTime',width: 220, align: "center",formatter:function(val){return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
                    {name: 'currentDate', index: 'currentDate',width: 150, align: "center",formatter:function(val){return myFormatDate(val,"yyyy-MM-dd");}},
                    {name: 'status', index: 'status',width: 180, align: "center", formatter: customStatusFormatter},
                    
                ],
                onSelectRow: function(id){
            		if(id && id!==lastsel){
			        	jQuery('#table_list_batches').jqGrid('restoreRow',lastsel);
			        	jQuery('#table_list_batches').jqGrid('editRow',id,true);
			        	lastsel=id;
			        }
            	},
                multiselect: false,//支持多项选择
                pager: "#pager_list_batches",
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
                    var ids=$("#table_list_batches").jqGrid('getDataIDs');
                    for(var i=0; i<ids.length; i++){
                        var id=ids[i];
                        var getRow = $('#table_list_batches').getRowData(id);
                        var detail = "<a href='javascript:void(0);' onclick='Detail(" + id + ")' class='default-details' title='详情'>详情</a>" ;
                        jQuery("#table_list_batches").jqGrid('setRowData', ids[i], { Detail: detail });
                    }
                }
            });
            jQuery("#table_list_batches").jqGrid('setFrozenColumns');
            // Add responsive to jqGrid
            $(window).bind('resize', function () {
                var width = $('.jqGrid_wrapper').width();
                $('#table_list_batches').setGridWidth(width);
            });
            
            $('.input-daterange').datepicker({
                format: "yyyy-mm-dd",
                language: "zh-CN",
                todayHighlight: true,
                autoclose: true,
                clearBtn: true
            });
		});
	</script>
	
	</title>
	
</body>
</html>