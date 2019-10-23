
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
	<!-- datepicker plugin -->
    <link href="${ctx}/css/plugins/bootstrap-datepicker/bootstrap-datepicker3.min.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">操作日志管理</div>
            <em class=""></em>
            <div class="pull-left active">对账记账日志查询</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="duiLogForm">
                          <div class="form-group ">
                          	  <label class="col-sm-2 control-label">操作人：</label>
                              <div class="col-sm-2"><input type="text" class="form-control" name="operator" id="operator"></div>
                          	  <label class="col-sm-2 control-label">对账批次：</label>
                              <div class="col-sm-2"><input type="text" class="form-control" name="checkBatchNo" id="checkBatchNo"></div>
                             
                           </div>
						<div class="form-group ">
							<label class="col-sm-2 control-label">操作时间：</label>
							<div class="col-sm-4">
								<div class="input-daterange input-group" id="datepicker">
									<input type="text" class="input-sm form-control"
										name="beginDate" /> <span class="input-group-addon">~</span>
									<input type="text" class="input-sm form-control" name="endDate" />
								</div>
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
					<table id="table_list_dui_logType"></table>
					<div id="pager_list_dui_logType"></div>
                    <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
					</div>
				</div>
			</div>
		</div>
		
	</div>
	<!-- 填充内容结束 -->
		
</body>
    
	<title>
	 <script src="${ctx}/js/plugins/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
     <script src="${ctx}/js/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js"></script>
	<script type="text/javascript">
	
	    var orderNum = 0;
		// 去除空格啊
        $('input').blur(function(){
            replaceSpace(this);
        })
        function replaceSpace(obj){
            obj.value = obj.value.replace(/\s/gi,'')
        }
		$("#duiLogForm").submit(function(){
			$("#table_list_dui_logType").setGridParam({
			       datatype : 'json',
			       page : 1            //Replace the '1' here
			    }).trigger("reloadGrid");
			return false;
		});
		
		function getParams(o){
			var data=$("#duiLogForm").serializeArray();
		     $.each(data, function() {    
		             o[this.name] = this.value || '';    
		     });   
		}
		$(document).ready(function() {
			var lastsel;
			var data=$("#duiLogForm").serialize();
            // 初始化表格
            $("#table_list_dui_logType").jqGrid({
            	url:"${ctx}/optLogsAction/findDuiAccountOptLogsList.do",
                datatype: "json",	
                mtype: "POST",
                height: 'auto',
                autowidth: true,
                shrinkToFit:false,
                autoScroll: false,
                rowNum: 10,
                rowList: [10, 20],
                colNames:['序号','对账批次号','操作内容','操作人','操作时间'],
                colModel: [
                    {name: 'id', index: 'id', width: 100, align: "center", sortable:false},
                    {name: 'checkBatchNo', index: 'checkBatchNo',width: 200, align: "center", sortable:false},
                    {name: 'operateContent', index: 'operateContent',width: 300, align: "center", sortable:false},
                    {name: 'operator', index: 'operator',width: 200, align: "center", sortable:false},
                    {name: 'operateTime', index: 'operateTime',width: 200, align: "center",
						formatter : function(val) {return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
                ],
                onSelectRow: function(id){
            	},
                multiselect: false,//支持多项选择
                pager: "#pager_list_dui_logType",
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
                   //不添加任何事件
                }
            });

            jQuery("#table_list_dui_logType").jqGrid('setFrozenColumns');
            // Add responsive to jqGrid
            $(window).bind('resize', function () {
                var width = $('.jqGrid_wrapper').width();
                $('#table_list_dui_logType').setGridWidth(width);
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
</html>  
      