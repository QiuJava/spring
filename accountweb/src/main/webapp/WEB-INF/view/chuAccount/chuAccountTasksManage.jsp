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
	<link href="${ctx}/css/plugins/ladda/ladda-themeless.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">出账管理</div>
            <em class=""></em>
            <div class="pull-left active">出账任务管理</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="outAccountForm">
                          <div class="form-group">
                          	    <label class="col-sm-2 control-label">交易日期:</label>
    							<div class="col-sm-4">
		                            <div class="input-daterange input-group" id="datepicker">
									    <input type="text" class="input-sm form-control" name="start" value="${params.start}"/>
									    <span class="input-group-addon">~</span>
									    <input type="text" class="input-sm form-control" name="end"  value="${params.end}"/>
									</div>   
								</div>
                                <label class="col-sm-2 control-label">出账任务ID：</label>
                                <div class="col-sm-2"><input type="text" class="form-control" name="id" id="id"   value="${params.id}"></div>
                                
                           </div>
                           <div class="form-group">
                                <label class="col-sm-2 control-label">出账单ID：</label>
                                <div class="col-sm-2"><input type="text" class="form-control" name="outAccountId" id="outAccountId"  value="${params.outAccountId}"></div>
                          	    
                          	    <label class="col-sm-2 control-label">上游:</label>
    							<div class="col-sm-2">
		                            <select class="form-control" name="acqEnname" id="acqEnname"> 
									         <option value="ALL" selected="selected">全部</option>
									         <c:forEach var="acqOrg" items="${acqOrgList}">
												<option value="${acqOrg.sysValue}"
												<c:if test="${acqOrg.sysValue == params.acqEnname}">selected="selected"</c:if>>
													${acqOrg.sysName}
												</option>
											</c:forEach>
										</select>       
								</div>
                                <label class="col-sm-2 control-label">出账单状态:</label>
    							<div class="col-sm-2">
		                            <select class="form-control" name="billStatus" id="billStatus"> 
		                            	<option value="-2" selected="selected">全部</option>
									         <c:forEach var="billStatus" items="${billStatusList}">
												<option value="${billStatus.sysValue}"
												<c:if test="${billStatus.sysValue == params.billStatus}">selected="selected"</c:if>>
													${billStatus.sysName}
												</option>
											</c:forEach>
										</select>     
								</div>
                           </div>
                            <div class="form-group">
                                
                                <label class="col-sm-2 control-label">出账范围:</label>
    							<div class="col-sm-2">
		                            <select class="form-control" name="outBillRange" id="outBillRange"> 
		                            	<option value="ALL" selected="selected">全部</option>
									         <c:forEach var="outBillRange" items="${outBillRangeList}">
												<option value="${outBillRange.sysValue}"
												<c:if test="${outBillRange.sysValue == params.outBillRange}">selected="selected"</c:if>>
													${outBillRange.sysName}
												</option>
											</c:forEach>
										</select>     
								</div>
                           </div>
                           
                             <div class="clearfix lastbottom"></div>   
                            <div class="form-group">
                                    <label class="col-sm-2 control-label aaa"></label>
                                   <!-- <div class="col-sm-12 col-sm-offset-13 "> -->
                                   	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                       	<button class="btn btn-success" type="submit"><span class="glyphicon gly-search"></span>查询</button>
                                       	<sec:authorize access="hasAuthority('chuAccountTasksManage:createTask')">
                                       	<button class="btn btn-success" type="button" id="createTask" style="margin-left:20px;"><span class="glyphicon"></span>新增出账任务</button>
                                       	</sec:authorize>
                                       	<button class="btn btn-default col-sm-offset-14  " type="reset"><span class="glyphicon gly-trash"></span>清空</button>
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
					<table id="table_list_chuAccountTasksManage"></table>
					<div id="pager_list_chuAccountTasksManage"></div>
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
	<script src="${ctx}/js/plugins/ladda/spin.min.js"></script>
	<script src="${ctx}/js/plugins/ladda/ladda.min.js"></script>
	<script src="js/plugins/sweetalert/sweetalert.min.js"></script>
	<script type="text/javascript">

	    $("#createTask").click(function() {
	    	location.href='${ctx}/chuAccountAction/toOutAccountTaskAdd.do';
	    });
		function Modify(id) {   			//单击修改链接的操作         
	        var model = $("#table_list_chuAccountTasksManage").jqGrid('getRowData', id);
			var accountNo = model.accountNo;
	        location.href='${ctx}/bankAccountAction/findBankAccountList.do?accountNo='+accountNo;
		}
		
		function CreateOutBillBegin(id, outBillId, _this) {
			
			var model = $("#table_list_chuAccountTasksManage").jqGrid('getRowData', id);
			var id = model.id;
			var l = Ladda.create(_this);
			
			l.start();
			$.post('${ctx}/chuAccountAction/judgeCreateOutBill.do', 
					{ id:id,'${_csrf.parameterName}':'${_csrf.token}' },
					function(msg) {
						if(!msg.status){
							swal({  
							 	title: "存在未对账的交易，是否确认生成出账单?", 
							   	type: "warning",  
							    showCancelButton: true,   
							    cancelButtonText: "取消",  
							   	confirmButtonColor: "#DD6B55",  
							    confirmButtonText: "继续生成",  
							    closeOnConfirm: false 
							    }, 
							    function(){   
									swal.close();
									CreateOutBill(id, outBillId, _this);
							    });
						}else{
							CreateOutBill(id, outBillId, _this);
						}
					}).always(function() { 
						l.stop();
					});
		}

		function CreateOutBill(id, outBillId, _this) {   		//单击交易明细链接的操作     
			//var model = $("#table_list_chuAccountTasksManage").jqGrid('getRowData', id);
			//var id = model.id;
			if (outBillId != -1) {
				swal({  
			 		title: "是否覆盖旧的出账单?", 
			   		type: "warning",  
			    	showCancelButton: true,   
			    	cancelButtonText: "取消",  
			   		confirmButtonColor: "#DD6B55",  
			    	confirmButtonText: "确认覆盖",  
			    	closeOnConfirm: false 
			    	}, 
			    function(){   
			    	var l = Ladda.create(_this);
 					l.start();
					$.post('${ctx}/chuAccountAction/createOutBill.do', 
						{ 'id': id,'${_csrf.parameterName}':'${_csrf.token}' },
							function(msg) {
								if(!msg.status){
									toastr.error(msg.msg,'错误');
								}else{
									toastr.success(msg.msg,'提示');
								}
					}).always(function() { l.stop(); swal.close();});
			    });
			} else {
				var l = Ladda.create(_this);
 				l.start();
				$.post('${ctx}/chuAccountAction/createOutBill.do', 
						{ 'id': id,'${_csrf.parameterName}':'${_csrf.token}' },
							function(msg) {
								if(!msg.status){
									toastr.error(msg.msg,'错误');
								}else{
									toastr.success(msg.msg,'提示');
								}
					}).always(function() { 
						l.stop(); swal.close();
						$("#table_list_chuAccountTasksManage").setGridParam({
			       			datatype : 'json',
			       			page : 1            //Replace the '1' here
			    		}).trigger("reloadGrid");
					});
			}  
		}
		
		$("#outAccountForm").submit(function(){
			$("#table_list_chuAccountTasksManage").setGridParam({
			       datatype : 'json',
			       page : 1            //Replace the '1' here
			    }).trigger("reloadGrid");
			return false;
		});
		
		function getParams(o){
			var data=$("#outAccountForm").serializeArray();
		     $.each(data, function() {    
		             o[this.name] = this.value || '';    
		     });   
		     //o.subjectNo = $("#subjectNo").select2("val");
		}
		
		function customBillStatusFormatter(cellvalue, options, rowObject){  
			<c:forEach var="checkAccountStatus" items="${billStatusList}">
				  if(cellvalue == '${checkAccountStatus.sysValue}'){
					  return '${checkAccountStatus.sysName}';
				  }
			 </c:forEach>	
			 return "" ;
		}
		
		function customOutBillRangeFormatter(cellvalue, options, rowObject){  
			<c:forEach var="outBillRange" items="${outBillRangeList}">
			  if(cellvalue == '${outBillRange.sysValue}'){
				  return '${outBillRange.sysName}';
			  }
		 </c:forEach>	
		 return "" ;
	}
		
		function Detail(id) {   //单击修改链接的操作         
	        var model = $("#table_list_chuAccountTasksManage").jqGrid('getRowData', id);
	        var pageNo = $("#table_list_chuAccountTasksManage").jqGrid('getGridParam','page');
	        var pageSize = $("#table_list_chuAccountTasksManage").jqGrid('getGridParam','rowNum');
	        var sortname = $("#table_list_chuAccountTasksManage").jqGrid('getGridParam','sortname');
	        var sortorder = $("#table_list_chuAccountTasksManage").jqGrid('getGridParam','sortorder');
	        
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
	        location.href='${ctx}/chuAccountAction/toOutAccountTaskDetail.do?id='+id+"&queryParams="+encodeQueryParams;
	    }
		
		function Edit(id) {   //单击修改链接的操作         
	        var model = $("#table_list_chuAccountTasksManage").jqGrid('getRowData', id);
	        var pageNo = $("#table_list_chuAccountTasksManage").jqGrid('getGridParam','page');
	        var pageSize = $("#table_list_chuAccountTasksManage").jqGrid('getGridParam','rowNum');
	        var sortname = $("#table_list_chuAccountTasksManage").jqGrid('getGridParam','sortname');
	        var sortorder = $("#table_list_chuAccountTasksManage").jqGrid('getGridParam','sortorder');
	        
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
	        location.href='${ctx}/chuAccountAction/toOutAccountTaskUpdate.do?id='+id+"&queryParams="+encodeQueryParams;
	    }
		
		$(document).ready(function() {
			var lastsel;
			//var sequenceNo = 0 ;
			var data=$("#outAccountForm").serialize();
			//alert(data);alert('${list[0].getUserType}');
            // 初始化表格
            $("#table_list_chuAccountTasksManage").jqGrid({
            	url:"${ctx}/chuAccountAction/findOutAccountTaskList.do",
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
                colNames:['操作','出账任务ID', '交易日期','上游','出账单状态','上游结算中金额','出账任务金额','出账范围','上游个数','出账单ID','系统计算时间','创建人','billStatus'],
                colModel: [
                     {name:'Detail',index:'id',width:250,align:"center",sortable:false,frozen:true},
                    {name: 'id', index: 'id', width: 120, align: "left"},
                    {name: 'dayPhase', index: 'transTime',width: 220, align: "left"},
                    {name: 'acqEnname', index: 'acqEnname',width: 110, align: "left"},
                    {name: 'billStatus', index: 'billStatus',width: 110, align: "left",formatter: customBillStatusFormatter},
                    {name: 'upBalance', index: 'upBalance',width: 110, align: "right",formatter: 'number'},
                    {name: 'outAccountTaskAmount', index: 'outAccountTaskAmount',width: 120, align: "right",formatter: 'number'},
                    {name: 'outBillRange', index: 'outBillRange',width: 110, align: "left",formatter: customOutBillRangeFormatter},
                    {name: 'upCompanyCount', index: 'upCompanyCount',width: 110, align: "left"},
                    {name: 'outAccountId', index: 'outAccountId',width: 110, align: "left"},
                    {name: 'sysTime', index: 'sysTime',width: 220, align: "left",formatter:function(val){return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
                    {name: 'creator', index: 'creator',width: 100, align: "left"},
                    {name: 'billStatus', index: 'billStatus', width:110, align:"left", hidden:"true"},
                ],
                onSelectRow: function(id){
            		if(id && id!==lastsel){
            			jQuery('#table_list_user').jqGrid('restoreRow',lastsel);
            			jQuery('#table_list_user').jqGrid('editRow',id,true);
            			lastsel=id;
            		}
            	},
                multiselect: false,//支持多项选择
                pager: "#pager_list_chuAccountTasksManage",
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
                    var ids=$("#table_list_chuAccountTasksManage").jqGrid('getDataIDs');
                    for(var i=0; i<ids.length; i++){
                        var id=ids[i];
                        var outBillId = -1;
                        var getRow = $('#table_list_chuAccountTasksManage').getRowData(id);
                        if (getRow.outAccountId != null && getRow.outAccountId != '') {
                        	outBillId = getRow.outAccountId;
                        } else {
                        	outBillId = -1;
                        }
                        var detail = "<a href='javascript:void(0);'  onclick='Detail(" + id + ")' class='default-details' title='详情'>详情</a>" ;
                        <sec:authorize access="hasAuthority('chuAccountTasksManage:update')">
                        if (getRow.billStatus!='0') {
                        	detail += "&nbsp;&nbsp;<a href='javascript:void(0);' class='default-undetails' style='cursor:not-allowed;'  title='编辑'>编辑</a>";
                        } else {
                        	detail += "&nbsp;&nbsp;<a href='javascript:void(0);'  onclick='Edit(" + id + ")' class='default-delete'  title='编辑'>编辑</a>";
                        }
                        </sec:authorize>
                        <sec:authorize access="hasAuthority('chuAccountTasksManage:createOutBill')">
                        if (getRow.billStatus!='0') {
                        	detail += "&nbsp;&nbsp;<a href='javascript:void(0);' title='生成出账单' style='display: inline-block;width: 80px;height: 24px;margin: 0 auto;line-height: 24px;text-align: center;color: #fff !important;border-radius: 2px;background: #7aca7c;padding:0;border:0;cursor:not-allowed;opacity:.5'>生成出账单</a>";
                        } else {
                        	detail += "&nbsp;&nbsp;<a href='javascript:void(0);' class='btn btn-primary ladda-button' data-style='zoom-in' data-size='l' style='display:inline-block;width: 80px;height: 24px;margin: 0 auto;font-size:12px;line-height: 24px;text-align: center;color: #fff !important;border-radius: 2px;background: #7aca7c;padding:0;border:0;' onclick='CreateOutBillBegin(" + id + ","+outBillId+", this)'><span class=''>生成出账单</span></a>";
                        }
                        </sec:authorize>
                        jQuery("#table_list_chuAccountTasksManage").jqGrid('setRowData', ids[i], { Detail: detail });
                    }
                }
            });
            jQuery("#table_list_chuAccountTasksManage").jqGrid('setFrozenColumns');
            // Add responsive to jqGrid
            $(window).bind('resize', function () {
                var width = $('.jqGrid_wrapper').width();
                $('#table_list_chuAccountTasksManage').setGridWidth(width);
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
      