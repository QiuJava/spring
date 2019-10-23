
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
	<link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">出账管理</div>
            <em class=""></em>
            <div class="pull-left active">商户出账结果查询</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="bankAccountForm">
                          <div class="form-group">
                          	  <label class="col-sm-2 control-label">商户编号：</label>
							  <div class="col-sm-2"><input type="text" class="form-control" name="merchantNo" id="merchantNo"><font color="red">(支持逗号分隔,批量查询)</font></div>
                             
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
                                 <label class="col-sm-2 control-label">出款状态：</label>
                                 <div class="col-sm-2">
   									<select class="form-control" name="outBillStatus"> 
										<option value="-1" selected="selected">全部</option>
										<c:forEach var="item" items="${outBillStatusList}">
                                        	<option value="${item.sysValue}">${item.sysName}</option>
                                        </c:forEach>
									</select>  
								 </div>
								 
								 <label class="col-sm-2 control-label">订单参考号:</label>
	    							<div class="col-sm-2">
	    								<input  type="text" class="form-control" name="orderReferenceNo"  id="orderReferenceNo">
										<font color="red">(支持逗号分隔,批量查询)</font>
	    							</div>
                            </div>
                            <div class="form-group ">
                                <label class="col-sm-2 control-label">结算日期：</label>
                                 <div class="col-sm-4">
									 <div class="input-daterange input-group" id="datepicker">
										 <input type="text" class="input-sm form-control" name="startTime" value="${date1}"/>
										 <span class="input-group-addon">~</span>
										 <input type="text" class="input-sm form-control" name="endTime" value="${date2}"/>
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
			                            <div class="input-daterange input-group" id="datepicker1">
										    <input type="text" class="input-sm form-control" name="transTimeStart" />
										    <span class="input-group-addon">~</span>
										    <input type="text" class="input-sm form-control" name="transTimesEnd" />
										</div>   
									</div>
                                 <label class="col-sm-2 control-label">出账单ID:</label>
	    							<div class="col-sm-2">
	    								<input  type="text" class="form-control" name="outBillId">
	    							</div> 
                            </div>
                            
                             <div class="form-group ">
                                 
                                 <label class="col-sm-2 control-label">子出账单明细:</label>
	    							<div class="col-sm-2">
	    								<input  type="text" class="form-control" name="id">
	    							</div> 
	    							
	    						 <label class="col-sm-2 control-label">加入出账单:</label>
                                    <div class="col-sm-2">
                                        <select class="form-control" name="isAddBill" id="isAddBill"> 
                                             <option value="-1" selected="selected">全部</option>
                                             <c:forEach var="vf" items="${isAddBillList}">
                                                <option value="${vf.sysValue}">
                                                    ${vf.sysName}
                                                </option>
                                             </c:forEach>
                                        </select>      
                                    </div>
							 </div>
						 <div class="form-group">
							 <label class="col-sm-2 control-label" >商户进件编号：</label>
							 <div class="col-sm-2">
								 <input  type="text" class="form-control" name="plateMerchantEntryNo"  id="plateMerchantEntryNo">
							 </div>
							 <label class="col-sm-2 control-label">银联报备编号:</label>
							 <div class="col-sm-2">
								 <input  type="text" class="form-control" name="acqMerchantNo"  id="acqMerchantNo">
								 <font color="red">(支持逗号分隔,批量查询)</font>
							 </div>



						 </div>
                            <div class="clearfix lastbottom"></div>
                                 <div class="form-group">
                                        <label class="col-sm-2 control-label aaa"></label>
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                       <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                       <input type="hidden" name="selectIds" value="${selectIds}" />
                                       
                                        <button class="btn btn-success" type="submit"><span class="glyphicon gly-search"></span>查询</button>
                                        <button class="btn btn-default col-sm-offset-14" type="reset"><span class="glyphicon gly-trash"></span>清空</button>
                                        <sec:authorize access="hasAuthority('merchantChuAccountResult:addOutBillToAlready')">
                                        <button class="btn btn-info2 col-sm-offset-14" type="button" id="isAddOutBill"><span class="glyphicon gly-protect"></span>加入出账单</button>
                                       </sec:authorize>
                                       <sec:authorize access="hasAuthority('merchantChuAccountResult:export')">
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

	<!-- 加入出账单按钮-->
	<div class="modal inmodal" id="isAddOutBillModal" tabindex="-1"
		role="dialog" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h5 class="modal-title">加入出账单</h5>
				</div>
				<div class="modal-body">
					<form method="post" class="form-horizontal"
						id="isAddOutBillForm">
						<div class="form-group">
							<label class="col-sm-3 control-label">出账单ID：</label>
							<div class="col-sm-6">
								<select class="form-control" name="outBillId" id="outBillId">
									<option value="-1" selected="selected">请选择</option>
									<c:forEach var="vf" items="${allNoOutBillIdList}">
										<option value="${vf.id}">${vf.acqEnname}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<input type="hidden" id="selectSubOutDetailId" value =""/>
					<input type="hidden" id="subAcqOrg" value =""/>
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-success" onclick="addOutBill()">添加</button>
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
	<script type="text/javascript">
	    var num = 0;//判断多选时的变量
		// 去除空格啊
		$('input').blur(function() {
			replaceSpace(this);
		})
		function replaceSpace(obj) {
			obj.value = obj.value.replace(/\s/gi, '')
		}
		function Detail(id) { //单击详情链接的操作         
			$.ajax({
				url : '${ctx}/bankAccountAction/findBankAccountList.do?id='+ id
			});
			location.reload();

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
		/*表单提交时的处理*/
   		function exportExcel() {  
   			var data = $("#bankAccountForm").serialize();
   			$.download('${ctx}/chuAccountAction/exportMerchantChuAccountResult.do',data,'post');
   	    }

		function Modify(id) { //单击修改链接的操作         
			var model = $("#table_list_inputAccountList").jqGrid('getRowData',id);
			var accountNo = model.accountNo;
			location.href = base+ 'bankAccountAction/findBankAccountList.do?accountNo='+ accountNo;
		}
		function TransactionDetail(id) { //单击交易明细链接的操作         
			var model = $("#table_list_inputAccountList").jqGrid('getRowData',id);
			var accountNo = model.accountNo;
			location.href = base+ 'bankAccountAction/findBankAccountList.do?accountNo='+ accountNo;
		}

		$("#bankAccountForm").submit(function() {
			$("#table_list_bankAccount").setGridParam({
				datatype : 'json',
				page : 1
			//Replace the '1' here
			}).trigger("reloadGrid");
			return false;
		});

		function getParams(o) {
			var data = $("#bankAccountForm").serializeArray();
			$.each(data, function() {
				o[this.name] = this.value || '';
			});
		}

		function customStatusFormatter(cellvalue, options, rowObject) {
			if (cellvalue == 2) {
				return "成功";
			} else if (cellvalue == 3) {
				return "失败";
			} else {
				return "";
			}
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
												url : "${ctx}/chuAccountAction/findMerChuAccountList.do",
												datatype : "json",
												mtype : "POST",
												height:"auto",
												autowidth: true,
												shrinkToFit: false,
												autoScroll: false,
												rowNum: 10,
												rowList: [ 10, 20, 50, 100, 200 ],
												colNames : [ '子出账单明细', '出账单ID','出账单明细', '商户编号','平台商户进件编号','银联报备商户编号','加入出账单','结算卡号', '商户出账金额',
														'出款状态', '出账通道', '结算时间','订单参考号', '交易金额',
														'交易时间', '交易通道', '商户名称','商户手机号', '银行户名', '出账备注' ],
												colModel : [
														{
															name : 'id',
															index : 'id',
															width : 100,
															align : "right"
														},
														{
															name : 'outBillId',
															index : 'outBillId',
															width : 100,
															align : "right"
														},
														{
															name : 'outBillDetailId',
															index : 'outBillDetailId',
															width : 100,
															align : "right"
														},
														{
															name : 'merchantNo',
															index : 'merchantNo',
															width : 150,
															align : "right"
														},
														{
															name : 'plateMerchantEntryNo',
															index : 'plateMerchantEntryNo',
															width : 150,
															align : "right"
														},
														{
															name : 'acqMerchantNo',
															index : 'acqMerchantNo',
															width : 150,
															align : "right"
														},
														{
															name : 'isAddBill',
															index : 'isAddBill',
															width : 100,
															align : "center",
															formatter : customIsAddBillStatusFormatter
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
															width : 130,
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
												multiselect : true,//支持多项选择
												multiselectWidth : 80,
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
													hideCheckBox();
												},
		                    					onSelectAll:function(rowid, status) { //点击全选时触发事件
		                    						 var rowIds = jQuery("#table_list_bankAccount").jqGrid('getDataIDs');//获取jqgrid中所有数据行的id
		                    						 for(var k=0; k<rowIds.length; k++) {
		                    						   var curRowData = jQuery("#table_list_bankAccount").jqGrid('getRowData', rowIds[k]);//获取指定id所在行的所有数据.
		                    						   if("是" == curRowData.isAddBill)
		                    						      $("#table_list_bankAccount").jqGrid("setSelection", rowIds[k],false);//设置改行不能被选中。
		                    						 }
		                    						 if(!chkNum(num)){
		                    							  $("#table_list_bankAccount").resetSelection();
		                    						 }
		                    						 num ++;
		                    					} ,
		                    					onSelectRow:function(id){//选择某行时触发事件
		                    					      var curRowData = jQuery("#table_list_bankAccount").jqGrid('getRowData', id);
		                    					      if("是" == curRowData.isAddBill)
		                    					        $("#table_list_bankAccount").jqGrid("setSelection", id,false);
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
		function customRecordStatusFormatter(cellvalue, options, rowObject){  
			
		  	if(cellvalue == 'NORECORD'){
			  	return '未记账';
		  	}
	 	
		  	if(cellvalue == 'FAILURE'){
			  	return '记账失败';
		  	}
	 	
		  	if(cellvalue == 'SUCCESS'){
			  	return '记账成功';
		  	}
	 	
		  	if(cellvalue == 'CHONGZHENGED'){
			  	return '已冲正';
		  	}
	 		
		return "" ;
	}

		function customIsAddBillStatusFormatter(cellvalue, options, rowObject) {
			<c:forEach var="isAddOutBill" items="${isAddBillList}">
			if (cellvalue == '${isAddOutBill.sysValue}') {
				return '${isAddOutBill.sysName}';
			}
			</c:forEach>
			return "";
		}

		function chkNum(num){
       	 return (num%2 ==0) ?true:false;  //判断是否能整除2
        }
       function hideCheckBox(){
       	 var rowIds = jQuery("#table_list_bankAccount").jqGrid('getDataIDs');//获取jqgrid中所有数据行的id
			 for(var k=0; k<rowIds.length; k++) {
			  var curRowData = jQuery("#table_list_bankAccount").jqGrid('getRowData', rowIds[k]);//获取指定id所在行的所有数据.
			  if("是" == curRowData.isAddBill){
				  var checkboxFailID = "jqg_table_list_bankAccount_"+rowIds[k];
				    $("#"+checkboxFailID).hide(); 
			  }
			 }
       	
       }
		//点击是否加入出账单按钮
		$("#isAddOutBill").click(function() {
			//获取到所有选中的chekbox的值，即选中行的id值.	
			var s = jQuery("#table_list_bankAccount").jqGrid('getGridParam', 'selarrrow');
			if(s==''){
				toastr.warning('你还没有选择任何内容！', '警告');  
			}else{
				$("#isAddOutBillModal").modal("show");
				var data = "" ;
				var ss = s.toString().split(',');
				var arr = new Array(); 
				for (var i in ss) {
					var curRowData = jQuery("#table_list_bankAccount").jqGrid('getRowData', ss[i]);//获取指定id所在行的所有数据.
					if(arr.indexOf(curRowData.acqOrgNo) == -1){
					     arr.push(curRowData.acqOrgNo);
					}
				}
				if(arr.length > 1){
					toastr.warning('你选择了不同出款通道，请谨慎操作！', '警告'); 
				}

				for (var i in ss) {
					data += "selectId["+i+"]="+ss[i]+"&" ;
				}
				$("#selectSubOutDetailId").val(data);
				$("#subAcqOrg").val(arr);
			}
			
		});
		
		function addOutBill(id) {    
			var data = $("#selectSubOutDetailId").val();
			var arr = $("#subAcqOrg").val();
			data += "outBillId="+$("#outBillId").val()+"&";
			data += "subAcqOrg="+$("#subAcqOrg").val()+"&";
			data += "${_csrf.parameterName}=${_csrf.token}" ;
			var outBillId = $("#outBillId").val();

			if(outBillId !="-1"){
			$.ajax({
				url : "${ctx}/chuAccountAction/addOutBillToAlready.do" ,
				data : data ,
				type : "POST" ,  
				success :function(msg){
					if(!msg.status){
			            toastr.error(msg.msg,'错误');
			            $("#isAddOutBillModal").modal("hide");
					}else{
						toastr.success(msg.msg,'提示');
						$("#isAddOutBillModal").modal("hide");
						//重新加载
						$("#table_list_bankAccount").setGridParam({
							datatype : 'json',
							page : 1
						//Replace the '1' here
						}).trigger("reloadGrid");
					}
				}
			}) ;
			}else{
				swal({title:"提示" ,text: '请选择出账单！' ,animation:"slide-from-top"});
			}
		}
		
		/* 科目查询开始 */
		function formatRepo(repo) {
			if (repo.loading)
				return repo.text;
			return repo.id + '(' + repo.text + ')';
		}

		function formatRepoSelection(repo) {
			return repo.text;
		}

		function parseSelectParams(params) {
			if (params && params.term) {
				return encodeURI(params.term);
			} else
				return null;
		}

	</script>
	 </title>
</html>  
      