
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true" language="java" contentType="text/html; charset=utf-8"%>
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
	<link href="${ctx}/css/plugins/bootstrap-datepicker/bootstrap-datepicker3.min.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
				<div class="pull-left">当前位置</div>
        <em class=""></em>
        <div class="pull-left">对账管理</div>
        <em class=""></em>
        <div class="pull-left active">快捷对账查询详情</div>
		</div>
	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="duiAccountForm" method="post" accept-charset="utf-8">
					 
                        <div class="form-group">
                                   <label class="col-sm-2 control-label">对账批次号：</label>
                                   <div class="col-sm-2"><input type="text" class="form-control" name="checkBatchNo" id="checkBatchNo" value='${params.checkBatchNo}'></div>
                                   <label class="col-sm-2 control-label">平台订单号：</label>
                                   <div class="col-sm-2"><input type="text" class="form-control" name="acqOrderNo" id="acqOrderNo"></div>
                                   <label class="col-sm-2 control-label">收单机构交易订单号：</label>
                                   <div class="col-sm-2"><input type="text" class="form-control" name="acqTransOrderNo" id="acqTransOrderNo"></div>
                        </div>
                        
                         <div class="form-group">
                            <label class="col-sm-2 control-label">平台商户号：</label>
                            <div class="col-sm-2"><input type="text" class="form-control" name="plateMerchantNo" id="plateMerchantNo"  ></div>
                                     <label class="col-sm-2 control-label">平台交易类型：</label>
                                     <div class="col-sm-2">
                                         <select class="form-control" name="plateTransType"> 
                                                <option value="ALL" selected="selected">全部</option>
                                                <c:forEach var="plateTransType" items="${plateTransTypeList}">
                                                <option value="${plateTransType.sysValue}">${plateTransType.sysName}</option>
                                                </c:forEach>
                                         </select>   
                                     </div>
                                   <label class="col-sm-2 control-label">平台交易状态：</label>
                                   <div class="col-sm-2">
                                        <select class="form-control" name="plateTransStatus"> 
                                            <option value="ALL" selected="selected">全部</option>
                                            <c:forEach var="plateTransStatus" items="${plateTransStatusList}">
                                            <option value="${plateTransStatus.sysValue}">${plateTransStatus.sysName}</option>
                                            </c:forEach>  
                                        </select>   
                                   </div>
                                   
                                  
                        </div>
                        <div class="form-group">
                             <label class="col-sm-2 control-label">对账状态：</label>
                                   <div class="col-sm-2">
                                        <select class="form-control" name="checkAccountStatus"> 
                                            <option value="ALL" selected="selected">全部</option>
                                            <c:forEach var="checkAccountStatus" items="${checkAccountStatusList}">
                                            <option value="${checkAccountStatus.sysValue}">${checkAccountStatus.sysName}</option>
                                            </c:forEach>
                                        </select> 
                                   </div>
                                   
                                   <label class="col-sm-2 control-label">记账状态：</label>
                                   <div class="col-sm-2">
                                        <select class="form-control" name="recordStatus"> 
                                            <option value="-1" selected="selected">全部</option>
                                            <c:forEach var="checkAccountStatus" items="${recordStatusList}">
                                            <option value="${checkAccountStatus.sysValue}">${checkAccountStatus.sysName}</option>
                                            </c:forEach>
                                        </select> 
                                   </div>
                        			
                        		<label class="col-sm-2 control-label">结算状态：</label>
                                   <div class="col-sm-2">
                                        <select class="form-control" name="settleStatus"> 
                                            <option value="-1" selected="selected">全部</option>
                                            <c:forEach var="settleStatus" items="${settleStatusList}">
                                            <option value="${settleStatus.sysValue}">${settleStatus.sysName}</option>
                                            </c:forEach>
                                        </select> 
                                   </div>
                                   
                         </div>
                            <div class="form-group">
                            	<label class="col-sm-2 control-label">交易记账：</label>
                                   <div class="col-sm-2">
                                        <select class="form-control" name="account"> 
                                            <option value="-1" selected="selected">全部</option>
                                            <c:forEach var="account" items="${npospAccountList}">
                                            <option value="${account.sysValue}">${account.sysName}</option>
                                            </c:forEach>
                                        </select> 
                                   </div>
                            
                                <label class="col-sm-2 control-label">创建时间：</label>
                                    <!-- <div class="col-sm-2">
                                     <input  type="text" class="form-control"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" name="createTime">
                                    </div>   -->
                                    <div class="col-sm-4">
                                        <div class="input-daterange input-group" id="datepicker">
                                            <input type="text" class="input-sm form-control" name="createTimeStart" />
                                            <span class="input-group-addon">~</span>
                                            <input type="text" class="input-sm form-control" name="createTimeEnd" />
                                        </div>   
                                    </div>
                            </div>
                          
                             <div class="clearfix lastbottom"></div>
                                
                                   <div class="form-group">
                                        <label class="col-sm-2 control-label aaa"></label>
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                       <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                       <button id="submitBut" type="submit" class=" btn btn-success" value=""><span class="glyphicon gly-search"></span>查询</button>
                                       <button type="reset" class=" btn btn-default col-sm-offset-14" value=""><span class="glyphicon gly-trash"></span>清空</button>
                                       <sec:authorize access="hasAuthority('fastDuiAccountDetail:export')">
                                            <button id="export" type="button" class=" btn btn-info col-sm-offset-14" value="" onclick="exportExcel()"><span class="glyphicon gly-out"></span>导出</button>
                                       </sec:authorize>
                                       <c:if test="${!empty forwardTo}">
                                       	<button id="returnUp" type="button" class=" btn btn-default col-sm-offset-14" onclick="window.location.href='${ctx}/fastDuiAccount/toDuiAccountQuery.do'" value="" /><span class="glyphicon gly-return"></span>返回</button>
                                       </c:if>
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
								<table id="table_list_dui_account"></table>
								<div id="pager_list_dui_account"></div>
								</div>
							</div>
						</div>
				</div>
			
	</div>
	
   </body>
    
	<title>
	<script src="${ctx}/js/plugins/select2/select2.full.min.js"></script>
    <script src="${ctx}/js/plugins/select2/i18n/zh-CN.js"></script>
	<script src="${ctx}/js/plugins/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
	<script src="${ctx}/js/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js"></script>
	<script type="text/javascript">
        // 去除空格啊
        $('input').blur(function(){
            replaceSpace(this);
        })
        function replaceSpace(obj){
            obj.value = obj.value.replace(/\s/gi,'')
        }
		// Ajax 文件下载
	jQuery.download = function(url, data, method){
	    // 获得url和data
	    if( url && data ){ 
	        // data 是 string 或者 array/object
	        data = typeof data == 'string' ? data : jQuery.param(data);
	        // 把参数组装成 form的  input
	        var inputs = '';
	        jQuery.each(data.split('&'), function(){ 
	            var pair = this.split('=');
	            inputs+='<input type="hidden" name="'+ pair[0] +'" value="'+ pair[1] +'" />';
	        });
	        // request发送请求
	        jQuery('<form action="'+ url +'" method="'+ (method||'post') +'">'+inputs+'</form>')
	        .appendTo('body').submit().remove();
	    };
	};	
	$("#duiAccountForm").submit(function() {
		$("#table_list_dui_account").setGridParam({
			datatype : 'json',
			page : 1
		//Replace the '1' here
		}).trigger("reloadGrid");
		return false;
	});
		/*表单提交时的处理*/
		function exportExcel() {  
			var data = $("#duiAccountForm").serialize();
			//console.info(data);
			$.download('${ctx}/fastDuiAccount/exportDuiAccountDetail.do',data,'post');
	    }
		 	
	
	function Download(id) {   //单击修改链接的操作         
	        var model = $("#table_list_dui_account").jqGrid('getRowData', id);
			var _fileName = model.name;
			var _acqOrg = $('#acqOrg').val();
	        //alert(model.subjectNo);
	        location.href='${ctx}/fastDuiAccount/duiAccountFileDown.do?fileName='+_fileName+'&acqOrg='+_acqOrg;
	        
	    }
		
		
	function customCheckAccountStatusFormatter(cellvalue, options, rowObject){  
		<c:forEach var="checkAccountStatus" items="${checkAccountStatusList}">
			  if(cellvalue == '${checkAccountStatus.sysValue}'){
				  return '${checkAccountStatus.sysName}';
			  }
		 </c:forEach>	
		 return "" ;
	}
	function customRecordStatusFormatter(cellvalue, options, rowObject){  
			<c:forEach var="checkAccountStatus" items="${recordStatusList}">
				  if(cellvalue == '${checkAccountStatus.sysValue}'){
					  return '${checkAccountStatus.sysName}';
				  }
			 </c:forEach>	
			 return "" ;
		}
	
	function customPlateTransStatusFormatter(cellvalue, options, rowObject){  
		<c:forEach var="plateTransStatus" items="${plateTransStatusList}">
			  if(cellvalue == '${plateTransStatus.sysValue}'){
				  return '${plateTransStatus.sysName}';
			  }
		 </c:forEach>	
		 return "" ;
	}
	
	function customPlateTransTypeFormatter(cellvalue, options, rowObject){  
		<c:forEach var="plateTransType" items="${plateTransTypeList}">
			  if(cellvalue == '${plateTransType.sysValue}'){
				  return '${plateTransType.sysName}';
			  }
		 </c:forEach>	
		 return "" ;
	}
	
	function customSettlementMethodFormatter(cellvalue, options, rowObject){  
		<c:forEach var="plateTransType" items="${settlementMethodList}">
			  if(cellvalue == '${plateTransType.sysValue}'){
				  return '${plateTransType.sysName}';
			  }
		 </c:forEach>	
		 return "" ;
	}
	
	function customSettleStatusFormatter(cellvalue, options, rowObject){  
		<c:forEach var="plateTransType" items="${settleStatusList}">
			  if(cellvalue == '${plateTransType.sysValue}'){
				  return '${plateTransType.sysName}';
			  }
		 </c:forEach>	
		 return "" ;
	}
	
	function customNpospAccountFormatter(cellvalue, options, rowObject){  
		<c:forEach var="plateTransType" items="${npospAccountList}">
			  if(cellvalue == '${plateTransType.sysValue}'){
				  return '${plateTransType.sysName}';
			  }
		 </c:forEach>	
		 return "" ;
	}
	
		function getParams(o){
			var data=$("#duiAccountForm").serializeArray();
		     $.each(data, function() {    
		             o[this.name] = this.value || '';    
		     });
		     
		}
		
		$(document).ready(function() {
			var lastsel;
			 /* var data=$("#duiAccountForm").serialize();
			alert(data);  */
            // 初始化表格
            $("#table_list_dui_account").jqGrid({
            	url:"${ctx}/fastDuiAccount/findDuiAccountDetailList.do",
                datatype: "json",	
                mtype: "POST",
                height:"auto",
                autowidth: true,
                shrinkToFit: false,
                autoScroll: false, 
                rowNum: 10,
                rowList: [10, 20],
                colNames:['ID','对账批次号', '收单机构订单号', '收单机构交易订单号', 
                          '收单机构交易金额',  '收单机构退货金额',  '收单机构对账日期',  '收单机构英文名称', '平台订单号', '平台收单机构商户号',  
                          '平台商户号', '平台交易金额',  '平台收单机构商户手续费',  
                          '平台商户手续费', '平台交易类型',  '平台交易状态',
                          '出账任务金额','结算周期','结算状态','交易记账',
                          '对账状态','记账状态', 
                          '创建时间'],
                          colModel: [
                                     {name: 'id', index: 'id', width: 120, align: "center"},
                                     {name: 'checkBatchNo', index: 'checkBatchNo', width: 190, align: "center"},
                                     {name: 'acqOrderNo', index: 'checkBatchNo', width: 190, align: "center"},
                                     {name: 'acqTransOrderNo', index: 'checkBatchNo', width: 200, align: "center"},
                                     {name: 'acqTransAmount',index:'acqTransAmount',width:150,align:"right",formatter: 'number'},
                                     {name: 'acqRefundAmount', index: 'acqRefundAmount', width: 160, align: "right",formatter: 'number'},
                                     {name: 'acqCheckDate', index: 'acqCheckDate',width: 200, align: "center",formatter:function(val){return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
                                     {name: 'acqEnname', index: 'acqEnname', width: 180, align: "center", sortable:false},
                                     {name: 'plateOrderNo', index: 'checkBatchNo', width: 190, align: "center"},
                                     {name: 'plateAcqMerchantNo', index: 'plateAcqMerchantNo', width: 150, align: "center", sortable:false},
                                     {name: 'plateMerchantNo', index: 'plateMerchantNo', width: 150, align: "center", sortable:false},
                                     {name: 'plateTransAmount', index: 'plateTransAmount', width: 150, align: "right",formatter: 'number'},
                                     {name: 'plateAcqMerchantFee', index: 'plateAcqMerchantFee', width: 180, align: "right",formatter: 'number'},
                                     {name: 'plateMerchantFee',index:'plateMerchantFee',width:120,align:"right",formatter: 'number'},
                                     {name: 'plateTransType', index: 'plateTransType', width: 120, align: "center", sortable:false,formatter:customPlateTransTypeFormatter},
                                     {name: 'plateTransStatus', index: 'plateTransStatus', width: 120, align: "center", sortable:false,formatter:customPlateTransStatusFormatter},
                                     {name: 'taskAmount', index: 'taskAmount', width: 120, align: "right", sortable:false,formatter: 'number'},
                                     {name: 'settlementMethod', index: 'settlementMethod', width: 120, align: "left", sortable:false,formatter:customSettlementMethodFormatter},
                                     {name: 'settleStatus', index: 'settleStatus', width: 120, align: "left", sortable:false,formatter:customSettleStatusFormatter},
                                     {name: 'account', index: 'account', width: 120, align: "left", sortable:false,formatter:customNpospAccountFormatter},
                                     {name: 'checkAccountStatus', index: 'checkAccountStatus', width: 120, align: "center", sortable:false,formatter:customCheckAccountStatusFormatter},
                                     {name: 'recordStatus', index: 'recordStatus', width: 130, align: "center" , sortable:false,formatter:  customRecordStatusFormatter},
                                     {name: 'createTime', index: 'createTime', width: 200, align: "center",formatter:function(val){return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}}
                                     
                                 ],
                onSelectRow: function(id){
            		if(id && id!==lastsel){
            			jQuery('#table_list_user').jqGrid('restoreRow',lastsel);
            			jQuery('#table_list_user').jqGrid('editRow',id,true);
            			lastsel=id;
            		}
            	},
                multiselect: false,//支持多项选择
                pager: "#pager_list_dui_account",
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
                    var ids=$("#table_list_dui_account").jqGrid('getDataIDs');
                    for(var i=0; i<ids.length; i++){
                        var id=ids[i];   
                        var detail = "<a href='javascript:void(0);' class='duidownload' title='下载' onclick='Download(" + id + ")'>下载</a>";
                        jQuery("#table_list_dui_account").jqGrid('setRowData', ids[i], { Detail: detail });
                    }
                }
            });
            // Add responsive to jqGrid
            $(window).bind('resize', function () {
                var width = $('.jqGrid_wrapper').width();
                $('#table_list_dui_account').setGridWidth(width);
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
      