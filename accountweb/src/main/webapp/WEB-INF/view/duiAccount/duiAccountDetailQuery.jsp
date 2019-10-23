
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
	 <link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
	 <link href="${ctx}/css/showLoading.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
				<div class="pull-left">当前位置</div>
        <em class=""></em>
        <div class="pull-left">对账管理</div>
        <em class=""></em>
        <div class="pull-left active">对账信息详情查询</div>
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
                                   
                                   <label class="col-sm-2 control-label">创建时间：</label>
                                    <!-- <div class="col-sm-2">
                                     <input  type="text" class="form-control"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" name="createTime">
                                    </div>   -->
                                    <div class="col-sm-4">
                                        <div class="input-daterange input-group" id="datepicker">
                                            <input type="text" class="input-sm form-control" name="createTimeStart"    />
                                            <span class="input-group-addon">~</span>
                                            <input type="text" class="input-sm form-control" name="createTimeEnd"    />
                                        </div>   
                                    </div>
                        </div>
                        <div class="form-group">
                                  <label class="col-sm-2 control-label">收单机构商户名称：</label>
                                  <div class="col-sm-2"><input type="text" class="form-control" name="acqMerchantName" id="acqMerchantName"    value="${params.acqMerchantName}" ></div>
                                  <label class="col-sm-2 control-label">收单机构商户号：</label>
                                  <div class="col-sm-2"><input type="text" class="form-control" name="acqMerchantNo" id="acqMerchantNo"    value="${params.acqMerchantNo}" ></div>
                                  <label class="col-sm-2 control-label">收单机构终端号：</label>
                                  <div class="col-sm-2"><input type="text" class="form-control" name="acqTerminalNo" id="acqTerminalNo"    value="${params.acqTerminalNo}" ></div> 
                        </div>
                        <div class="form-group">
                                  <label class="col-sm-2 control-label">收单机构卡号：</label>
                                  <div class="col-sm-2"><input type="text" class="form-control" name="acqAccountNo" id="acqAccountNo"    value="${params.acqAccountNo}" ></div>
                                   <label class="col-sm-2 control-label">收单机构交易流水号：</label>
                                   <div class="col-sm-2"><input type="text" class="form-control" name="acqTransSerialNo" id="acqTransSerialNo"   value="${params.acqTransSerialNo}" ></div>
                                   
                                   <label class="col-sm-2 control-label">收单机构参考号：</label>
                                   <div class="col-sm-2"><input type="text" class="form-control" name="acqReferenceNo" id="acqReferenceNo"   value="${params.acqReferenceNo}" ></div>                                                                                   
                        </div>
                        <div class="form-group">
                                   <label class="col-sm-2 control-label">平台收单机构参考号：</label>
                                   <div class="col-sm-2"><input type="text" class="form-control" name="plateAcqReferenceNo" id="plateAcqReferenceNo"   value="${params.plateAcqReferenceNo}" ></div>
                                   
                                   <label class="col-sm-2 control-label">平台商户号：</label>
                                   <div class="col-sm-2"><input type="text" class="form-control" name="plateMerchantNo" id="plateMerchantNo"   value="${params.plateMerchantNo}" ></div>
                                   <label class="col-sm-2 control-label">平台终端号：</label>
                                   <div class="col-sm-2"><input type="text" class="form-control" name="plateTerminalNo" id="plateTerminalNo"  value="${params.plateTerminalNo}" ></div> 
                                                                                  
                         </div>
                         <div class="form-group">
                                     <label class="col-sm-2 control-label">平台交易类型：</label>
                                     <div class="col-sm-2">
                                         <select class="form-control" name="plateTransType"> 
                                                <option value="ALL" selected="selected">全部</option>
                                                <c:forEach var="plateTransType" items="${plateTransTypeList}">
                                                <option value="${plateTransType.sysValue}"
                                                <c:if test="${plateTransType.sysValue == params.plateTransType}">selected="selected"</c:if>>
                                                ${plateTransType.sysName}</option>
                                                </c:forEach>
                                         </select>   
                                     </div>
                                   <label class="col-sm-2 control-label">平台交易状态：</label>
                                   <div class="col-sm-2">
                                        <select class="form-control" name="plateTransStatus"> 
                                            <option value="ALL" selected="selected">全部</option>
                                            <c:forEach var="plateTransStatus" items="${plateTransStatusList}">
                                            <option value="${plateTransStatus.sysValue}"
                                            <c:if test="${plateTransStatus.sysValue == params.plateTransStatus}">selected="selected"</c:if>>
                                            ${plateTransStatus.sysName}</option>
                                            </c:forEach>  
                                        </select>   
                                   </div>
                                   
                                   <label class="col-sm-2 control-label">对账状态：</label>
                                   <div class="col-sm-2">
                                        <select class="form-control" name="checkAccountStatus"> 
                                            <option value="ALL" selected="selected">全部</option>
                                            <c:forEach var="checkAccountStatus" items="${checkAccountStatusList}">
                                            <option value="${checkAccountStatus.sysValue}"
                                            <c:if test="${checkAccountStatus.sysValue == params.checkAccountStatus}">selected="selected"</c:if>>
                                            ${checkAccountStatus.sysName}</option>
                                            </c:forEach>
                                        </select> 
                                   </div>
                        </div>
                        
                        <div class="form-group">
                                     <label class="col-sm-2 control-label">结算状态：</label>
                                     <div class="col-sm-2">
                                         <select class="form-control" name="settleStatus"> 
                                                <option value="-1" selected="selected">全部</option>
                                                <c:forEach var="settleStatus" items="${settleStatusList}">
                                                <option value="${settleStatus.sysValue}"
                                                <c:if test="${settleStatus.sysValue == params.settleStatus}">selected="selected"</c:if>>
                                                ${settleStatus.sysName}</option>
                                                </c:forEach>
                                         </select>   
                                     </div>
                                   <label class="col-sm-2 control-label">交易记账：</label>
                                   <div class="col-sm-2">
                                        <select class="form-control" name="account"> 
                                            <option value="-1" selected="selected">全部</option>
                                            <c:forEach var="account" items="${npospAccountList}">
                                            <option value="${account.sysValue}"
                                            <c:if test="${account.sysValue == params.account}">selected="selected"</c:if>>
                                            ${account.sysName}</option>
                                            </c:forEach>  
                                        </select>   
                                   </div>
                                   
                                   <label class="col-sm-2 control-label">记账状态：</label>
                                   <div class="col-sm-2">
                                        <select class="form-control" name="recordStatus"> 
                                            <option value="-1" selected="selected">全部</option>
                                            <c:forEach var="recordStatus" items="${recordStatusList}">
                                            <option value="${recordStatus.sysValue}"
                                            <c:if test="${recordStatus.sysValue == params.recordStatus}">selected="selected"</c:if>>
                                            ${recordStatus.sysName}</option>
                                            </c:forEach>
                                        </select> 
                                   </div>
                        </div>
                    	<div class="form-group">
                                     <label class="col-sm-2 control-label">收单机构：</label>
                                     <div class="col-sm-2">
                                         <select class="form-control" name="acqEnname"> 
                                               <option value="ALL" selected="selected">全部</option>
									         <c:forEach var="acqOrg" items="${acqOrgList}">
												<option value="${acqOrg.sysValue}"
												<c:if test="${acqOrg.sysValue == params.acqEnname}">selected="selected"</c:if>>
													${acqOrg.sysName}
												</option>
											</c:forEach>
                                         </select>   
                                     </div>
                                     <label class="col-sm-2 control-label">订单参考号：</label>
                                   <div class="col-sm-2"><input type="text" class="form-control" name="orderReferenceNo" id="orderReferenceNo"  ></div>
                                    <label class="col-sm-2 control-label">是否加入出账单：</label>
                                     <div class="col-sm-2">
                                         <select class="form-control" name="isAddBill"> 
                                               <option value="ALL" selected="selected">全部</option>
                                               <option value="0" <c:if test="${params.isAddBill == '0'}">selected="selected"</c:if>>否</option>
                                               <option value="1" <c:if test="${params.isAddBill == '0'}">selected="selected"</c:if>>是</option>
                                         </select>   
                                     </div> 
                        </div>
                        <div class="form-group">
                                    <label class="col-sm-2 control-label">冻结：</label>
                                     <div class="col-sm-2">
                                         <select class="form-control" name="freezeStatus"> 
                                               <option value="ALL" selected="selected">全部</option>
                                               <option value="0" <c:if test="${params.freezeStatus == '0'}">selected="selected"</c:if>>未冻结</option>
                                               <option value="1" <c:if test="${params.freezeStatus == '1'}">selected="selected"</c:if>>风控冻结</option>
                                               <option value="2" <c:if test="${params.freezeStatus == '2'}">selected="selected"</c:if>>活动冻结</option>
                                               <option value="3" <c:if test="${params.freezeStatus == '3'}">selected="selected"</c:if>>财务冻结</option>
                                         </select>   
                                     </div> 
                                     <label class="col-sm-2 control-label">平台订单号：</label>
                                   <div class="col-sm-2"><input type="text" class="form-control" name="plateOrderNo" id="plateOrderNo"   value="${params.plateOrderNo}" ></div>
                            <label class="col-sm-2 control-label">单边日切标识：</label>
                            <div class="col-sm-2">
                                <select class="form-control" name="dbCutFlag">
                                    <option value="ALL" selected="selected">全部</option>
                                    <option value="None" <c:if test="${params.dbCutFlag == 'None'}">selected="selected"</c:if>>无</option>
                                    <option value="ShortCashAuto" <c:if test="${params.dbCutFlag == 'ShortCashAuto'}">selected="selected"</c:if>>短款自动匹配</option>
                                    <option value="MoreCashAuto" <c:if test="${params.dbCutFlag == 'MoreCashAuto'}">selected="selected"</c:if>>长款自动匹配</option>
                                </select>
                            </div>
                        </div>
                         <div class="form-group">
                             <label class="col-sm-2 control-label">平台交易方式：</label>
                             <div class="col-sm-2">
                                 <select class="form-control" name="payMethod">
                                     <option value="ALL" selected="selected">全部</option>
                                     <option value="1" <c:if test="${params.payMethod == '1'}">selected="selected"</c:if>>POS</option>
                                     <option value="2" <c:if test="${params.payMethod == '2'}">selected="selected"</c:if>>支付宝</option>
                                     <option value="3" <c:if test="${params.payMethod == '3'}">selected="selected"</c:if>>微信</option>
                                     <option value="4" <c:if test="${params.payMethod == '4'}">selected="selected"</c:if>>快捷</option>
                                     <option value="5" <c:if test="${params.payMethod == '5'}">selected="selected"</c:if>>银联二维码</option>
                                 </select>
                             </div>
                         </div>

                         <div class="clearfix lastbottom"></div>
                                
                                   <div class="form-group">
                                        <label class="col-sm-2 control-label aaa"></label>
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                       <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                       <input type="hidden" name="pageNo1" value="${params.pageNo1}" />
                                       <button id="submitBut" type="submit" class=" btn btn-success" value=""><span class="glyphicon gly-search"></span>查询</button>
                                       <button type="reset" class=" btn btn-default col-sm-offset-14" value=""><span class="glyphicon gly-trash"></span>清空</button>
                                       <sec:authorize access="hasAuthority('duiAccountMsgDetail:export')">
                                            <button id="export" type="button" class=" btn btn-info col-sm-offset-14" value="" onclick="exportExcel()"><span class="glyphicon gly-out"></span>导出</button>
                                       </sec:authorize>
                                       <sec:authorize access="hasAuthority('duiAccountMsgDetail:sysSettleStatus')">
                                        <button type="button" class=" btn btn-info col-sm-offset-14" value="" onclick="sysSettleStatus()">同步结算状态</button>
                                       </sec:authorize>
                                   <%--    <c:if test="${!empty forwardTo}">
                                         <button id="returnUp" type="button" class=" btn btn-default" onclick="window.location.href='${ctx}/duiAccountAction/toDuiAccountQuery.do?queryParams=${params.queryParams}'" value="" /><span class="glyphicon gly-return"></span>返回</button>
                                       </c:if>--%>
                                       <button id="returnUp" type="button" class=" btn btn-default" onclick="winClose()" value="" />关闭</button>
                                       <%-- <c:if test="${!empty forwardTo}">
                                           <button id="returnUp" type="button" class=" btn btn-default col-sm-offset-14" onclick="window.location.href='${ctx}/duiAccountAction/toDuiAccountQuery.do'" value="" /><span class="glyphicon gly-return"></span>返回</button>
                                       </c:if> --%>
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
	<script src="${ctx}/js/plugins/sweetalert/sweetalert.min.js"></script>
	
	<script src="${ctx}/js/jquery.showLoading.js"></script>
	<script src="${ctx}/js/jquery.showLoading.min.js"></script>
	
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
			$.download('${ctx}/duiAccountAction/exportDuiAccountDetail.do',data,'post');
	}
	/*同步结算状态*/
	function sysSettleStatus() { 
		$("#wrapper").showLoading();
		var data = $("#duiAccountForm").serialize();
		$.ajax({
				url:"${ctx}/duiAccountAction/sysSettleStatus.do",
				type:"POST",
				data:data,
				success :function(msg){
					//console.log(msg.state);
					//console.log(msg.msg);
					$("#wrapper").hideLoading();
					if(msg.state){
						$("#wrapper").hideLoading();
						swal({title:"提示" ,text:msg.msg ,animation:"slide-from-top"}, function() {
							//重新加载
							$("#table_list_dui_account").setGridParam({
								datatype : 'json',
								page : 1
							//Replace the '1' here
							}).trigger("reloadGrid");
							});
					}else{
						$("#wrapper").hideLoading();
						swal({title:"提示" ,text:msg.msg ,animation:"slide-from-top"});
					}
				}
			});
	}	 	
	
	function Download(id) {   //单击修改链接的操作         
	        var model = $("#table_list_dui_account").jqGrid('getRowData', id);
			var _fileName = model.name;
			var _acqOrg = $('#acqOrg').val();
	        //alert(model.subjectNo);
	        location.href='${ctx}/duiAccountAction/duiAccountFileDown.do?fileName='+_fileName+'&acqOrg='+_acqOrg;
	        
	    }
	function viewDuiAccountDetail(id) {   //单击修改链接的操作         
        var model = $("#table_list_dui_account").jqGrid('getRowData', id);
        var pageNo = $("#table_list_dui_account").jqGrid('getGridParam','page');
        var pageSize = $("#table_list_dui_account").jqGrid('getGridParam','rowNum');
        var sortname = $("#table_list_dui_account").jqGrid('getGridParam','sortname');
        var sortorder = $("#table_list_dui_account").jqGrid('getGridParam','sortorder');
		var _fileName = model.name;
        
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
		
        location.href='${ctx}/duiAccountAction/viewDuiAccountDetail.do?id='+id+"&queryParams="+encodeQueryParams;
        
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
	function customOutAccountBillMethodFormatter(cellvalue, options, rowObject){  
		if(cellvalue == '4'){
			  return 'T1线上单笔代付';
		  }else if(cellvalue == '5'){
			  return 'T1线下批量代付';
		  }
	 return "" ;
}
	function customIsAddBillFormatter(cellvalue, options, rowObject){  
		if(cellvalue == '1'){
			  return '是';
		  }else if(cellvalue == '0'){
			  return '否';
		  }
	 return "" ;
}
	
	function customFreezeStatusFormatter(cellvalue, options, rowObject){
        var black = rowObject.merchantBlack == null ? "" : rowObject.merchantBlack;
		if(cellvalue == '0'){
			  return '未冻结'+black;
		  }else if(cellvalue == '1'){
			  return '风控冻结'+black;
		  }else if(cellvalue == '2'){
			  return '活动冻结'+black;
		  }else if(cellvalue == '3'){
			  return '财务冻结'+black;
		  }
	 return ""+black ;
}
        function customPayMethodFormatter(cellvalue, options, rowObject){
            if(cellvalue == '1'){
                return 'POS';
            }else if(cellvalue == '2'){
                return '支付宝';
            }else if(cellvalue == '3'){
                return '微信';
            }else if(cellvalue == '4'){
                return '快捷';
            }else if(cellvalue == '5'){
                return '银联二维码';
            }
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
			 var checkBatchNo = $("#checkBatchNo").val();
			 var loadType = "local";
			 if(checkBatchNo){
                 loadType = "json";
             }
            // 初始化表格
            $("#table_list_dui_account").jqGrid({
            	url:"${ctx}/duiAccountAction/findDuiAccountDetailList.do",
                datatype: loadType,
                mtype: "POST",
                height:"auto",
                autowidth: true,
                shrinkToFit: false,
                autoScroll: false, 
                page: ${params.pageNo},
                rowNum: ${params.pageSize},
                sortname :'${params.sortname}',
                sortorder:'${params.sortorder}',
                rowList: [10, 20],
                colNames:['ID','对账批次号','上游名称','订单参考号','收单机构参考号','平台订单号','对账状态','存疑记账状态','是否加入出账单','冻结状态','单边日切标识','平台商户号','商户扣率','收单机构商户号','平台交易卡号','平台交易金额','平台上游手续费',
                	      '平台商户手续费','抵扣交易手续费','实际交易手续费','抵扣券编号','平台交易状态','平台交易方式','平台交易时间','出账任务金额','结算周期','结算状态','出款方式','交易记账','收单机构卡号','收单机构交易金额',
                	      '收单机构退货金额','收单机构交易时间','云闪付手续费', '活动费率','原始交易手续费' ,"自选商户手续费","抵扣自选商户手续费","实际自选商户手续费",'创建时间','操作'],
                          colModel: [
                          			 {name: 'id', index: 'id', width: 100, align: "left"},
                                     {name: 'checkBatchNo', index: 'checkBatchNo', width: 180, align: "left", sortable:false},
                                     {name: 'acqEnname', index: 'acqEnname', width: 130, align: "center", sortable:false},
                                     {name: 'orderReferenceNo', index: 'orderReferenceNo', width: 180, align: "left", sortable:false},
                                     {name: 'acqReferenceNo', index: 'acqReferenceNo', width: 150, align: "left", sortable:false},
                                     {name: 'plateOrderNo', index: 'plateOrderNo', width: 180, align: "left", sortable:false},
                                     {name: 'checkAccountStatus', index: 'checkAccountStatus', width: 150, align: "center",formatter:customCheckAccountStatusFormatter},
                                     {name: 'recordStatus', index: 'recordStatus', width: 150, align: "center",formatter:  customRecordStatusFormatter},
                                     {name: 'isAddBill', index: 'isAddBill', width: 120, align: "center",formatter:  customIsAddBillFormatter},
                                     {name: 'freezeStatus', index: 'freezeStatus', width: 100, align: "center",formatter:  customFreezeStatusFormatter},
                                     {name: 'dbCutFlag', index: 'dbCutFlag', width: 100, align: "center",formatter:  function(e){
                                         if(e=='None'){
                                             return '无';
                                         }else if(e == 'ShortCashAuto'){
                                            return '短款自动匹配';
                                         }else{
                                             return '长款自动匹配';
                                         }
                                     }},
                                     {name: 'plateMerchantNo',index:'plateMerchantNo',width:200,align:"left"},
                                    {name: 'plateMerchantRate',index:'plateMerchantRate',width:100,align:"left"},
                                     {name: 'acqMerchantNo',index:'acqMerchantNo',width:200,align:"left"},
                                     {name: 'plateCardNo', index: 'plateCardNo', width: 150, align: "center"},
                                     {name: 'plateTransAmount', index: 'plateTransAmount',width: 150, align: "left",formatter:'number'},
                                     {name: 'plateAcqMerchantFee', index: 'plateAcqMerchantFee',width: 150, align: "left",formatter:'number'},
                                     {name: 'merFee2', index: 'merFee2', width: 150, align: "left", sortable:false,formatter:'number'},
                                     {name: 'deductionFee', index: 'deductionFee',width: 150, align: "left",formatter:'number'},
                                     {name: 'plateMerchantFee', index: 'plateMerchantFee',width: 150, align: "left",formatter:'number'},    //实际交易手续费
                                     {name: 'couponNos', index: 'couponNos', width: 210, align: "left", sortable:false},
                                     {name: 'plateTransStatus', index: 'plateTransStatus', width: 120, align: "center",formatter:customPlateTransStatusFormatter},
                                     {name: 'payMethod', index: 'payMethod', width: 120, align: "center",formatter:customPayMethodFormatter},
                                     {name: 'plateAcqTransTime', index: 'plateAcqTransTime', width: 200, align: "left", sortable:false,formatter:function(val){return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
                                     {name: 'taskAmount', index: 'taskAmount', width: 120, align: "left", sortable:false,formatter:'number'},
                                     {name: 'settlementMethod', index: 'settlementMethod', width: 120, align: "center", sortable:false,formatter:customSettlementMethodFormatter},
                                     {name: 'settleStatus', index: 'settleStatus', width: 120, align: "center", sortable:false,formatter:customSettleStatusFormatter},
                                     {name: 'outAccountBillMethod', index: 'outAccountBillMethod', width: 120, align: "left", sortable:false,formatter:customOutAccountBillMethodFormatter},
                                     {name: 'account', index: 'account', width: 120, align: "center", sortable:false,formatter:customNpospAccountFormatter},
                                     {name: 'acqAccountNo', index: 'acqAccountNo', width: 120, align: "left", sortable:false},
                                     {name: 'acqTransAmount', index: 'acqTransAmount', width: 150, align: "left", sortable:false,formatter: 'number'},
                                     {name: 'acqRefundAmount', index: 'acqRefundAmount', width: 150, align: "left" , sortable:false,formatter: 'number'},
                                     {name: 'acqTransTime', index: 'acqTransTime', width: 200, align: "left",formatter:function(val){return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
                                     {name: 'quickFee', index: 'quickFee', width: 150, align: "left", sortable:false,formatter: 'number'},
                                     {name: 'quickRate', index: 'quickRate', width: 150, align: "left", sortable:false,formatter: 'number'},
                                     {name: 'actualFee', index: 'actualFee', width: 150, align: "left", sortable:false,formatter: 'number'},

                                     {name: 'merchantPrice', index: 'merchantPrice', width: 150, align: "left", sortable:false,formatter: 'number'},
                                     {name: 'deductionMerFee', index: 'deductionMerFee', width: 150, align: "left", sortable:false,formatter: 'number'},
                                     {name: 'actualMerFee', index: 'actualMerFee', width: 150, align: "left", sortable:false,formatter: 'number'},

                                    {name: 'createTime', index: 'createTime', width: 200, align: "left",formatter:function(val){return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
                                     {name: 'Detail',index:'id',width:100,align:"center",sortable:false,frozen:true}
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
                        var detail= "&nbsp;<a href='javascript:void(0)' class='default-details'  onclick='viewDuiAccountDetail(" + id + ")' title='对账明细' >对账明细</a>";
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
        function winClose() {
            window.top.opener=null;
            window.close();
        }
		
	</script>
	 </title>
</html>  
      