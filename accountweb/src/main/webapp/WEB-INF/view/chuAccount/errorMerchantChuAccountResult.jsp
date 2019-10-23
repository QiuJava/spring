
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
            <div class="pull-left">出账管理</div>
            <em class=""></em>
            <div class="pull-left active">出款失败记录查询</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="errorMerchantForm">
                          <div class="form-group">
                          	  <label class="col-sm-2 control-label">商户编号：</label>
                              <div class="col-sm-2"><input type="text" class="form-control" name="merchantNo" id="merchantNo"></div>
                             
                              <label class="col-sm-2 control-label">商户名称：</label>
                              <div class="col-sm-2"><input type="text" class="form-control" name="merchantName" id="merchantName"> </div>
                              <label class="col-sm-2 control-label">商户手机号：</label>
                                <div class="col-sm-2"><input type="text" class="form-control" name="mobile" id="mobile"> </div>
                                
                              
                           </div> 
                           <div class="form-group">
                                <label class="col-sm-2 control-label" >出款通道：</label>
                                <div class="col-sm-2">
                                    <select class="form-control" name="acqOrgNo"> 
                                    	<option value="" selected="selected">全部</option>
                                    	<c:forEach var="acqOrg" items="${acqOrgs}">
                                        	<option value="${acqOrg.sysValue}">${acqOrg.sysName}</option>
                                        </c:forEach>
                                    </select>  
                                 </div>							
								 <label class="col-sm-2 control-label">订单参考号:</label>
	    							<div class="col-sm-2">
	    								<input  type="text" class="form-control" name="orderReferenceNo"  id="orderReferenceNo">
	    							</div> 
	    							
	    							<label class="col-sm-2 control-label">出账单ID:</label>
	    							<div class="col-sm-2">
	    								<input  type="text" class="form-control" name="outBillId">
	    							</div> 
 								 
                            </div>
                            <div class="form-group ">
                                <label class="col-sm-2 control-label">结算日期：</label>
                                 <div class="col-sm-4">
                                    <div class="input-daterange input-group" id="datepicker">
                                        <input type="text" class="input-sm form-control" name="startTime" />
                                        <span class="input-group-addon">~</span>
                                        <input type="text" class="input-sm form-control" name="endTime" />
                                    </div>
                                 </div>
                                 
                                  <label class="col-sm-2 control-label">交易通道:</label>
                                    <div class="col-sm-2">
                                        <select class="form-control" name="acqEnname" id="acqEnname"> 
                                             <option value="" selected="selected">全部</option>
                                             <c:forEach var="vf" items="${acqOrgs}">
                                                <option value="${vf.sysValue}">
                                                    ${vf.sysName}
                                                </option>
                                             </c:forEach>
                                        </select>      
                                    </div>
 
                            </div>
                            <div class="form-group ">
                                 <label class="col-sm-2 control-label">商户出账金额：</label>
                                 <div class="col-sm-2"><input type="text" class="form-control" name="outAmount1" id="outAmount1"> </div>
                                 <label class="pull-left control-label" style="width:0">~</label>
                                 <div class="col-sm-2"><input type="text" class="form-control" name="outAmount2" id="outAmount2"> </div>
                                 <label class="col-sm-2 control-label">出账单明细:</label>
	    							<div class="col-sm-2">
	    								<input  type="text" class="form-control" name="outBillDetailId">
	    							</div> 
                            </div>
                            
                             <div class="form-group ">
                                  <label class="col-sm-2 control-label">交易时间:</label>
	                           		<div class="col-sm-4">
			                            <div class="input-daterange input-group" id="datepicker">
										    <input type="text" class="input-sm form-control" name="transTimeStart" />
										    <span class="input-group-addon">~</span>
										    <input type="text" class="input-sm form-control" name="transTimesEnd" />
										</div>   
									</div>
                               <label class="col-sm-2 control-label">子出账单明细:</label>
	    							<div class="col-sm-2">
	    								<input  type="text" class="form-control" name="id">
	    							</div>   
                            </div>

                            <div class="clearfix lastbottom"></div>
                                 <div class="form-group">
                                        <label class="col-sm-2 control-label aaa"></label>
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                       <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                       
                                        <button class="btn btn-success" type="submit"><span class="glyphicon gly-search"></span>查询</button>
                                        <button class="btn btn-default col-sm-offset-14" type="reset"><span class="glyphicon gly-trash"></span>清空</button>
                                        <sec:authorize access="hasAuthority('errorMerchantChuAccountResult:export')">
                                        <button class="btn btn-danger col-sm-offset-14" type="button" onclick="exportExcel()"><span class="glyphicon gly-out"></span>导出</button>
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
					<table id="table_list_bankAccount"></table>
					<div id="pager_list_bankAccount"></div>
                    <br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
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
	<script src="${ctx}/js/plugins/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
	<script src="${ctx}/js/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js"></script>
	<script type="text/javascript">
	    var num = 0;//判断多选时的变量
		// 去除空格啊
		$('input').blur(function() {
			replaceSpace(this);
		})
		function replaceSpace(obj) {
			obj.value = obj.value.replace(/\s/gi, '')
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
	    
		$("#errorMerchantForm").submit(function() {
			$("#table_list_bankAccount").setGridParam({
				datatype : 'json',
				page : 1
			//Replace the '1' here
			}).trigger("reloadGrid");
			return false;
		});

		function getParams(o) {
			var data = $("#errorMerchantForm").serializeArray();
			$.each(data, function() {
				o[this.name] = this.value || '';
			});
		}

		
		/*表单提交时的处理*/
   		function exportExcel() {  
   			var data = $("#errorMerchantForm").serialize();
   			$.download('${ctx}/chuAccountAction/exportErrorMerchantChuAccountResult.do',data,'post');
   	    }
		

		$(document)
				.ready(
						function() {
							var lastsel;
							var data = $("#bankAccountForm").serialize();
							// 初始化表格
							$("#table_list_bankAccount")
									.jqGrid(
											{
												url : "${ctx}/chuAccountAction/findErrorSubMerChuAccountList.do",
												datatype : "json",
												mtype : "POST",
												height:"auto",
												autowidth: true,
												shrinkToFit: false,
												autoScroll: false,
												rowNum: 10,
												rowList: [ 10, 20 ],
												colNames : [ '子出账单明细', '出账单ID','出账单明细', '商户编号','结算卡号', '商户出账金额',
														'出款状态', '出账通道', '结算时间','订单参考号', '交易金额',
														'交易时间', '交易通道', '商户名称','商户手机号', '银行户名', '出账备注' ],
												colModel : [
														{
															name : 'subOutBillDetailId',
															index : 'subOutBillDetailId',
															width : 100,
															align : "left"
														},
														{
															name : 'outBillId',
															index : 'outBillId',
															width : 100,
															align : "left"
														},
														{
															name : 'outBillDetailId',
															index : 'outBillDetailId',
															width : 100,
															align : "left"
														},
														{
															name : 'merchantNo',
															index : 'merchantNo',
															width : 200,
															align : "left"
														},
														{
															name : 'cardNo',
															index : 'cardNo',
															width : 200,
															align : "left"
														},
														{
															name : 'outAccountTaskAmount',
															index : 'outAccountTaskAmount',
															width : 150,
															align : "right",
															formatter : 'number'
														},
														{
															name : 'outBillStatus',
															index : 'outBillStatus',
															width : 150,
															align : "center",
															formatter : customOutBillStatusFormatter
														},
														{
															name : 'acqOrgNo',
															index : 'acqOrgNo',
															width : 100,
															align : "left"
														},
														{
															name : 'settleTime',
															index : 'settleTime',
															width : 200,
															align : "left",
															formatter : function(
																	val) {
																return myFormatDate(
																		val,
																		"yyyy-MM-dd hh:mm:ss");
															}
														},
														{
															name : 'orderReferenceNo',
															index : 'orderReferenceNo',
															width : 200,
															align : "left"
														},
														{
															name : 'transAmount',
															index : 'transAmount',
															width : 150,
															align : "right",
															formatter : 'number'
														},
														{
															name : 'transTime',
															index : 'transTime',
															width : 200,
															align : "left",
															formatter : function(val) {
																return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");
															}
														},
														{
															name : 'acqOrgNo',
															index : 'acqOrgNo',
															width : 100,
															align : "left"
														},
														{
															name : 'merchantName',
															index : 'merchantName',
															width : 200,
															align : "left"
														},
														{
															name : 'mobile',
															index : 'mobile',
															width : 150,
															align : "left"
														},
														{
															name : 'cardName',
															index : 'cardName',
															width : 150,
															align : "left"
														},
														{
															name : 'outAccountNote',
															index : 'outAccountNote',
															width : 100,
															align : "left"
														}, ],
												onSelectRow : function(id) {
													if (id && id !== lastsel) {
														jQuery('#table_list_user').jqGrid('restoreRow',lastsel);
														jQuery('#table_list_user').jqGrid('editRow',id,true);
														lastsel = id;
													}
												},
												multiselect : false,//支持多项选择
												multiboxonly: false,
												pager : "#pager_list_bankAccount",
												viewrecords : true,
												hidegrid : false,
												reloadAfterSubmit: true,
												jsonReader : {
													root : "result",
													total : "totalPages",
													page : "pageNo",
													pageSize : "pageSize",
													records : "totalCount",
													repeatitems : false
												},
												prmNames : {
													page : "pageNo",
													rows : "pageSize"
												},
												serializeGridData : function(postData) {
													getParams(postData);
													return postData;
												},
												gridComplete : function() { //在此事件中循环为每一行添加修改和删除链接
													
												}
											});
							jQuery("#table_list_bankAccount").jqGrid('setFrozenColumns');
							$(window).bind('resize',
									function() {
										var width = $('.jqGrid_wrapper').width();
										$('#table_list_bankAccount').setGridWidth(width);
									});

							$('.input-daterange').datepicker({
								format : "yyyy-mm-dd",
								language : "zh-CN",
								todayHighlight : true,
								autoclose : true,
								clearBtn : true
							});
						});

		function customOutBillStatusFormatter(cellvalue, options, rowObject) {
			<c:forEach var="checkAccountStatus" items="${outBillStatusList}">
			if (cellvalue == '${checkAccountStatus.sysValue}') {
				return '${checkAccountStatus.sysName}';
			}
			</c:forEach>
			return "";
		}
		
		function customIsAddBillStatusFormatter(cellvalue, options, rowObject) {
			<c:forEach var="isAddOutBill" items="${isAddBillList}">
			if (cellvalue == '${isAddOutBill.sysValue}') {
				return '${isAddOutBill.sysName}';
			}
			</c:forEach>
			return "";
		}

	</script>
	 </title>
</html>  
      