
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
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">出账管理</div>
            <em class=""></em>
            <div class="pull-left active">出账单管理</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="chuAccountBillManageForm">
                          <div class="form-group" >
                          	  <label class="col-sm-2 control-label">交易日期:</label>
                           		<div class="col-sm-3">
		                            <div class="input-daterange input-group" id="datepicker">
									    <input type="text" class="input-sm form-control" name="start"  value="${params.start}"/>
									    <span class="input-group-addon">~</span>
									    <input type="text" class="input-sm form-control" name="end"  value="${params.end}"/>
									</div>   
								</div>
								
								<label class="col-sm-2 control-label">出账单文件名:</label>
    							<div class="col-sm-2">
		                            <input type="text" class="form-control" name="fileName" id="fileName"  value="${params.fileName}"/>     
								</div>
							</div> 
							<div class="form-group" > 
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
								
								<label class="col-sm-3 control-label">确认出账状态:</label>
    							<div class="col-sm-2">
		                            <select class="form-control" name="outBillStatus" id="outBillStatus"> 
		                            		<option value="-2" selected="selected">全部</option>
									         <option value="0" <c:if test="${params.outBillStatus == '0'}">selected="selected"</c:if>>否</option>
									         <option value="1" <c:if test="${params.outBillStatus == '1'}">selected="selected"</c:if>>是</option>
									         <option value="2" <c:if test="${params.outBillStatus == '2'}">selected="selected"</c:if>>已关闭</option>
										</select>     
								</div>
							</div> 
							
							<div class="form-group" > 
								<label class="col-sm-2 control-label">出账单ID:</label>
    							 <div class="col-sm-2"><input type="text" class="form-control" name="id" id="id"   value="${params.id}" ></div>
								
								<%-- <label class="col-sm-3 control-label">出账方式:</label>
    							<div class="col-sm-2">
		                            <select class="form-control" name="outMethod" id="outMethod"> 
		                            		<option value="-2" selected="selected">全部</option>
									         <option value="0" <c:if test="${params.outMethod == '0'}">selected="selected"</c:if>>T1线上</option>
									         <option value="1" <c:if test="${params.outMethod == '1'}">selected="selected"</c:if>>T1线下</option>
										</select>     
								</div> --%>
                                <label class="col-sm-3 control-label">出账范围:</label>
                                <div class="col-sm-2">
                                    <select class="form-control" name="outBillRange" id="outBillRange">
                                        <option value="ALL" selected="selected">全部</option>
                                        <c:forEach var="outBill" items="${outBillList}">
                                            <option value="${outBill.sysValue}"
                                                    <c:if test="${outBill.sysValue == params.acqEnname}">selected="selected"</c:if>>
                                                    ${outBill.sysName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                            </div>
							
							
                            <div class="clearfix lastbottom"></div>
                            <div class="form-group" style="margin-bottom:0">
                                    <label class="col-sm-2 control-label aaa"></label>
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                   	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                       	<button class="btn btn-success" type="submit"><span class="glyphicon gly-search"></span>查询</button>
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
					<table id="table_list_chuAccountBillManage"></table>
					<div id="pager_list_chuAccountBillManage"></div>
                    <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
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
		function Detail(id) {   //单击详情链接的操作         
			var model = $("#table_list_chuAccountBillManage").jqGrid('getRowData', id);
	        var pageNo = $("#table_list_chuAccountBillManage").jqGrid('getGridParam','page');
	        var pageSize = $("#table_list_chuAccountBillManage").jqGrid('getGridParam','rowNum');
	        var sortname = $("#table_list_chuAccountBillManage").jqGrid('getGridParam','sortname');
	        var sortorder = $("#table_list_chuAccountBillManage").jqGrid('getGridParam','sortorder');
			var out_bill_id = model.id;
	        
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
			
	        <%--location.href='${ctx}/chuAccountAction/toOutAccountBillDetail.do?outBillId='+out_bill_id+"&queryParams="+encodeQueryParams;--%>
			openNewWindow('${ctx}/chuAccountAction/toOutAccountBillDetail.do',{"outBillId":out_bill_id});
	    }
		/* function Modify(id) {   //单击修改链接的操作         
	        var model = $("#table_list_chuAccountBillManage").jqGrid('getRowData', id);
	        var out_bill_id = model.id;
	        location.href='${ctx}/chuAccountAction/toOutAccountBillUpdate.do?outBillId='+out_bill_id;
		} */
		function openNewWindow(url,param){
			var $form = $('<form method="get" action="'+url+'" target="_blank"></form>');
			if(typeof(param) == "object"){
				var tmp = [];
				for(var key in param){
					tmp.push('<textarea name="'+key+'">'+param[key]+'</textarea>');
				}
				$form.append(tmp.join(" "));
			}
			$form.appendTo('body');
			setTimeout(function(){
				$form.submit();
			},10);

		}
		
		function viewDetail(id) {   //单击查看出账       
	        var model = $("#table_list_chuAccountBillManage").jqGrid('getRowData', id);
	        var out_bill_id = model.id;
	        location.href='${ctx}/chuAccountAction/toOutAccountBillView.do?outBillId='+out_bill_id;
		}
		
		function ConfirmDetail(id) {   //单击交易明细链接的操作         
	        var model = $("#table_list_chuAccountBillManage").jqGrid('getRowData', id);
	        var pageNo = $("#table_list_chuAccountBillManage").jqGrid('getGridParam','page');
	        var pageSize = $("#table_list_chuAccountBillManage").jqGrid('getGridParam','rowNum');
	        var sortname = $("#table_list_chuAccountBillManage").jqGrid('getGridParam','sortname');
	        var sortorder = $("#table_list_chuAccountBillManage").jqGrid('getGridParam','sortorder');
			var out_bill_id = model.id;
	        
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
			
	        location.href='${ctx}/chuAccountAction/toOutAccountBillConfirm.do?outBillId='+out_bill_id+"&queryParams="+encodeQueryParams;
		}
		function BillExport(id) {   //单击交易明细链接的操作        
			var model = $("#table_list_chuAccountBillManage").jqGrid('getRowData', id);
			var out_bill_id = model.id;
			location.href = "${ctx}/chuAccountAction/toOutAccountBillExport.do?outBillId="+out_bill_id;
			/** 
	        var model = $("#table_list_chuAccountBillManage").jqGrid('getRowData', id);
	        var out_bill_id = model.id;
	        var data = {'outBillId':out_bill_id,'${_csrf.parameterName}':'${_csrf.token}'};
   			//console.info(data);
   			$.download('${ctx}/chuAccountAction/billExport.do',data,'post');
   			*/
		}
		function TransactionDetail(id) {
			var model = $("#table_list_chuAccountBillManage").jqGrid('getRowData', id);
			var out_bill_id = model.id;
			location.href = "${ctx}/chuAccountAction/toTransactionImport.do?outBillId="+out_bill_id;
		}
		
		$("#chuAccountBillManageForm").submit(function(){
			$("#table_list_chuAccountBillManage").setGridParam({
			       datatype : 'json',
			       page : 1            //Replace the '1' here
			    }).trigger("reloadGrid");
			return false;
		});
		
		function customOutAccountBillMethodFormatter(cellvalue, options, rowObject){  
			<c:forEach var="outAccountService" items="${outAccountServiceList}">
			  if(cellvalue == '${outAccountService.id}'){
				  return '${outAccountService.serviceName}';
			  }
		   </c:forEach>	
		   return "";
	}

        function outBillRangeMethodFormatter(cellvalue, options, rowObject){
            <c:forEach var="outBillRange" items="${outBillList}">
            if(cellvalue == '${outBillRange.sysValue}'){
                return '${outBillRange.sysName}';
            }
            </c:forEach>
            return "";
        }
		
		function getParams(o){
			var data=$("#chuAccountBillManageForm").serializeArray();
		     $.each(data, function() {    
		             o[this.name] = this.value || '';    
		     });   
		     //o.subjectNo = $("#subjectNo").select2("val");
		}
		
		$(document).ready(function() {
			var lastsel;
			//var sequenceNo = 0 ;
			var data=$("#chuAccountBillManageForm").serialize();
			//alert(data);alert('${list[0].getUserType}');
            // 初始化表格
            $("#table_list_chuAccountBillManage").jqGrid({
            	url:"${ctx}/chuAccountAction/findOutBillList.do",
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
                colNames:['操作','出账单 ID', '确认出账状态', '交易日期','创建时间','出账任务金额','实际出账金额','计算出账金额','上游','有余额上游数量','有余额商户数量','创建人','出账方式','出账范围','回盘人','出账任务ID','出账单文件名','出账状态','hasService','tranImport'],
                colModel: [
                    {name:'Detail',index:'Id',width:300,align:"center",sortable:false,frozen:true},
                    {name: 'id', index: 'id', width: 80, align: "left"},
                    {name: 'outBillStatus', index: 'outBillStatus',width: 150, align: "center", formatter:function(val){if(val=='0'){return "否";}else if(val=='2'){return "已关闭";}else{return "是";}}},
                    {name: 'dayPhase', index: 'createTime',width: 200, align: "left"},
                    {name: 'createTime',index : 'createTime',width : 200,align : "left",formatter : function(val) {return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
                    {name: 'outAccountTaskAmount', index: 'outAccountTaskAmount',width: 150, align: "right",formatter: 'number'},
                    {name: 'outAmount', index: 'outAmount',width: 150, align: "right",formatter: 'number'},
                    {name: 'calcOutAmount', index: 'calcOutAmount',width: 150, align: "right",formatter: 'number'},
                    {name: 'acqEnname', index: 'acqEnname',width: 100, align: "right"},
                    {name: 'balanceUpCount', index: 'balanceUpCount',width: 150, align: "center"},
                    {name: 'balanceMerchantCount', index: 'balanceMerchantCount',width: 150, align: "center"},
                    {name: 'creator', index: 'creator',width: 100, align: "left"},
                    {name: 'outAccountBillMethod', index: 'outAccountBillMethod', width: 150, align: "left", sortable:false,formatter:customOutAccountBillMethodFormatter},
                    {name: 'outBillRange', index: 'outBillRange',width: 120, align: "left", sortable:false,formatter:outBillRangeMethodFormatter},
                    {name: 'backOperator', index: 'backOperator',width: 120, align: "left"},
                    {name: 'outAccountTaskId', index: 'outAccountTaskId',width: 120, align: "left"},
                    {name: 'fileName', index: 'fileName', width:400, align:"center"},
                    {name: 'outBillStatus', index: 'outBillStatus', width:110, align:"center", hidden:"true"},
                    {name: 'hasService', index: 'hasService', width:110, align:"center", hidden:"true"},
                    {name: 'tranImport', index: 'tranImport', width:110, align:"center", hidden:"true"},
                ],
                onSelectRow: function(id){
            		if(id && id!==lastsel){
            			jQuery('#table_list_chuAccountBillManage').jqGrid('restoreRow',lastsel);
            			jQuery('#table_list_chuAccountBillManage').jqGrid('editRow',id,true);
            			lastsel=id;
            		}
            	},
                multiselect: false,//支持多项选择
                pager: "#pager_list_chuAccountBillManage",
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
                    var ids=$("#table_list_chuAccountBillManage").jqGrid('getDataIDs');
                    for(var i=0; i<ids.length; i++){
                        var id=ids[i];   
                        var getRow = $('#table_list_chuAccountBillManage').getRowData(id);
                        var detail = "<a href='javascript:void(0);' class='default-details' title='详情' onclick='Detail(" + id + ")'>详情</a>";
                        /* <sec:authorize access="hasAuthority('chuAccountBillManage:update')">
                        if (getRow.outBillStatus!='2') {
                        	detail += "&nbsp;&nbsp;&nbsp;<a href='javascript:void(0);' class='default-delete'  title='编辑' onclick='Modify(" + id + ")'>编辑</a>";
                        } else {
                        	detail += "&nbsp;&nbsp;&nbsp;<a href='javascript:void(0);' class='default-undetails'  title='编辑' style='cursor:not-allowed'>编辑</a>";
                        }
                        </sec:authorize> */
                        
                        if (getRow.outBillStatus=='0') {
                        	<sec:authorize access="hasAuthority('chuAccountBillManage:confirmOut')">
                        	detail += "&nbsp;&nbsp;&nbsp;<a href='javascript:void(0);' class='default-details'  title='确认出账' onclick='ConfirmDetail(" + id + ")'>确认出账</a>";
                        	 </sec:authorize>
                        } else {
                       		<sec:authorize access="hasAuthority('chuAccountBillManage:viewOut')">
                       		detail += "&nbsp;&nbsp;&nbsp;<a href='javascript:void(0);' class='default-maintenance2'  title='查看出账' onclick='viewDetail(" + id + ")'>查看出账</a>";
                       		</sec:authorize>
                       	}
                       
                        if (getRow.tranImport == '0') {
                        <sec:authorize access="hasAuthority('chuAccountBillManage:fileExport')">
                        if (getRow.outBillStatus!='1' || getRow.hasService == '0') {
                        	detail += "&nbsp;&nbsp;&nbsp;<a href='javascript:void(0);' class='default-undetails' style='cursor:not-allowed' title='出账单导出'>出账单导出</a>";
                        } else {
                        	detail += "&nbsp;&nbsp;&nbsp;<a href='javascript:void(0);' class='default-details'  title='出账单导出' onclick='BillExport(" + id + ")'>出账单导出</a>";
                        }
                        </sec:authorize>
                        <sec:authorize access="hasAuthority('chuAccountBillManage:fileImport')">
                        if (getRow.outBillStatus!='1' || getRow.hasService == '0') {
                       		detail += "&nbsp;&nbsp;&nbsp;<a href='javascript:void(0);' class='default-undetails' style='cursor:not-allowed' title='回盘导入'>回盘导入</a>";
                        } else {
                        	detail += "&nbsp;&nbsp;&nbsp;<a href='javascript:void(0);' class='default-maintenance'  title='回盘导入' onclick='TransactionDetail(" + id + ")'>回盘导入</a>";
                        }
                        </sec:authorize>
                        } else {
                        <sec:authorize access="hasAuthority('chuAccountBillManage:fileExport')">
                        	detail += "&nbsp;&nbsp;&nbsp;<a href='javascript:void(0);' class='default-undetails' style='cursor:not-allowed' title='出账单导出'>出账单导出</a>";
                        </sec:authorize>
                        <sec:authorize access="hasAuthority('chuAccountBillManage:fileImport')">
                        	detail += "&nbsp;&nbsp;&nbsp;<a href='javascript:void(0);' class='default-undetails' style='cursor:not-allowed' title='回盘导入'>回盘导入</a>";
                        </sec:authorize>
                        }
                        jQuery("#table_list_chuAccountBillManage").jqGrid('setRowData', ids[i], { Detail: detail });
                    }
                }
            });
            jQuery("#table_list_chuAccountBillManage").jqGrid('setFrozenColumns');
            // Add responsive to jqGrid
            $(window).bind('resize', function () {
                var width = $('.jqGrid_wrapper').width();
                $('#table_list_chuAccountBillManage').setGridWidth(width);
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
      