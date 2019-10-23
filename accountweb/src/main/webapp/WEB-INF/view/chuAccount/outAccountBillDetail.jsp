
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<!-- jqGrid plugin -->
	<link href="${ctx}/css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/jqGrid/ui.jqgrid.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/bootstrapTour/bootstrap-tour.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/bootstrap-datepicker/bootstrap-datepicker3.min.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">出账管理</div>
            <em class=""></em>
            <div class="pull-left active">出账单详情</div>
		</div>
	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
			<!-- 出账单基本信息 -->
			<div class="col-lg-12">
				<div class="ibox-content">
					<!--边框表格布局: table table-bordered \悬停表格 : table table-hover-->					 
					<table class="table table-bordered" style="width:100;max-width:100%">
					   <thead>
					      <tr style="text-align: center;">
					         <td width="200">出账单 ID</td>
					         <td width="200">系统计算时间</td>
					         <td width="200">出账任务金额</td>
					         <td width="200">计算出账金额</td>
					         <td width="200">有余额上游数量</td>
					         <td width="200">有余额商户数量</td>
					         <td width="200">确认出账状态</td>
					      </tr>
					   </thead>
					   <tbody>
					      <tr>
					         <td>${outBill.id }</td>
					         <td>
					         	<fmt:formatDate value="${outBill.sysTime }" pattern="yyyy-MM-dd HH:mm:ss"/> 
					         </td>
					         <td style="text-align: right;">${outBill.outAccountTaskAmount }</td>
					         <td style="text-align: right;">${outBill.calcOutAmount }</td>
					         <td>${outBill.balanceUpCount }</td>
					         <td>${outBill.balanceMerchantCount }</td>
					         <td>${outBill.outBillStatus }</td>
					      </tr>
					   </tbody>
					</table>
					
				</div>
				
			</div>
			
			<!-- 收单机构出账明细 -->
			
			<div class="col-lg-12">
				<div class="ibox ">
					<div class="ibox-content">
						<div class="jqGrid_wrapper">
						<table id="table_list_acqOutBill"></table>
						<div id="pager_list_acqOutBill"></div>
						</div>
					</div>
				</div>
			</div>
			
			<!-- 商户出账明细 -->
			
			<div class="col-lg-12">
				<div class="ibox ">
					<div class="ibox-content">
						
						<form class="form-horizontal" id="outAccountBillDetailForm">
	                          
	                          <div class="form-group">
	                          	    <label class="col-sm-2 control-label">商户编号:</label>
	    							<div class="col-sm-2">
	    								<input  type="text" class="form-control"  name="merchantNo">
	    							</div>
	                                <label class="col-sm-2 control-label">出款通道:</label>
	    							<div class="col-sm-2">
										<select class="form-control" name="acqOrg" id="acqOrg"> 
									         <option value="ALL" selected="selected">全部</option>
									         <c:forEach var="acqOrg" items="${acqOrgList}">
												<option value="${acqOrg.sysValue}"
													<c:if test="${acqOrg.sysName == params.sysName}">selected="selected"</c:if>>
													${acqOrg.sysName}
												</option>
											</c:forEach>
										</select>      
									</div>	
									
									<label class="col-sm-2 control-label">订单参考号:</label>
	    							<div class="col-sm-2">
	    								<input  type="text" class="form-control" name="orderReferenceNo">
	    							</div> 
                                    
	                           </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">有无财务调整备注:</label>
                                    <div class="col-sm-2">
                                        <select class="form-control" name="isChangeRemark" id="isChangeRemark"> 
                                             <option value="" selected="selected">全部</option>
                                             <option value="1" >有</option>
                                             <option value="0" >无</option>
                                        </select>      
                                    </div>
                                    <label class="col-sm-2 control-label">校验通过:</label>
                                    <div class="col-sm-2">
                                        <select class="form-control" name="verifyFlag" id="verifyFlag"> 
                                             <option value="-1" selected="selected">全部</option>
                                             <c:forEach var="vf" items="${verifyFlagList}">
                                                <option value="${vf.sysValue}">
                                                    ${vf.sysName}
                                                </option>
                                             </c:forEach>
                                        </select>      
                                    </div>
                                    <label class="col-sm-2 control-label">出款状态:</label>
                                    <div class="col-sm-2">
                                        <select class="form-control" name="outBillStatus" id="outBillStatus"> 
                                             <option value="-1" selected="selected">全部</option>
                                             <c:forEach var="vf" items="${billStatusList}">
                                                <option value="${vf.sysValue}">
                                                    ${vf.sysName}
                                                </option>
                                             </c:forEach>
                                        </select>      
                                    </div>
                                </div>

	                          <div class="form-group">
								  <label class="col-sm-2 control-label">商户进件编号:</label>
								  <div class="col-sm-2">
									  <input  type="text" class="form-control"  name="plateMerchantEntryNo">
								  </div>
								  <label class="col-sm-2 control-label">银联报备商户号:</label>
								  <div class="col-sm-2">
									  <input  type="text" class="form-control" name="acqMerchantNo">
								  </div>
								  <label class="col-sm-2 control-label">记账状态:</label>
								  <div class="col-sm-2">
									  <select class="form-control" name="recordStatus" id="recordStatus">
										  <option value="-1" selected="selected">全部</option>
										  <c:forEach var="vf" items="${recordStatusList}">
											  <option value="${vf.sysValue}">
													  ${vf.sysName}
											  </option>
										  </c:forEach>
									  </select>
								  </div>
	                           </div>
							<div class="form-group">
								<label class="col-sm-2 control-label">商户余额:</label>
								<div class="col-sm-2">
									<input  type="text" class="form-control" name="merchantBalance1">
								</div>
								<label class="pull-left control-label" style="width:0">~</label>
								<div class="col-sm-2">
									<input  type="text" class="form-control" name="merchantBalance2">
								</div>
								<label class="col-sm-2 control-label">商户出账金额:</label>
								<div class="col-sm-2">
									<input  type="text" class="form-control" name="outAccountTaskAmount1">
								</div>
								<label class="pull-left control-label" style="width:0">~</label>
								<div class="col-sm-2">
									<input  type="text" class="form-control" name="outAccountTaskAmount2">
								</div>


							</div>
                               <div class="form-group">


                                    <label class="col-sm-2 control-label">交易时间:</label>
	                           		<div class="col-sm-4">
			                            <div class="input-daterange input-group" id="datepicker">
										    <input type="text" class="input-sm form-control" name="start" />
										    <span class="input-group-addon">~</span>
										    <input type="text" class="input-sm form-control" name="end" />
										</div>
									</div>

                               </div>
	                           <div class="clearfix lastbottom"></div>
	                            <div class="form-group">
	                            
	                                    <input type="hidden" name="outBillDetailId" value="${outBillDetailId}" />
                                        <label class="col-sm-2 control-label aaa"></label>
	                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
	                                   	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	                                   	   <input type="hidden" name="outBillId" value="${outBill.id}" />
	                                       	<button class="btn btn-success" type="submit"><span class="glyphicon gly-search"></span>查询</button>
	                                       	<button class="btn btn-default col-sm-offset-14 " type="reset"><span class="glyphicon gly-trash"></span>清空</button>
	                                       	<button class="btn btn-danger col-sm-offset-14" type="button" onclick="exportExcel()"><span class="glyphicon gly-out"></span>导出</button>
	                                   <!-- </div> -->
	                             </div>
	                           <br/><br/>
	                    </form>
						<span >汇总：
						未出款：<span id="outBillNo">0</span> 笔,&nbsp;&nbsp;金额：<span  id="outBillNoAmount">0.00</span>,&nbsp;&nbsp;  
						出款成功：<span id="outBillSuccess" style="color:blue">0</span> 笔,金额：<span  id="outBillSuccessAmount" style="color:blue">0.00</span>,&nbsp;&nbsp;  
						出款中：<span  id="outBillIng">0</span>笔, 金额：<span  id="outBillIngAmount">0.00</span>,  &nbsp;&nbsp;
						出款失败：<a href='javascript:void(0);' onclick="toErrorResult()"><span  id="outBillFail" style="color:red">0</span></a> 笔,金额：<span  id="outBillFailAmount" style="color:red">0.00</span>
						</span>
						<br/>
						<br/>
						<div class="jqGrid_wrapper">
							<table id="table_list_outAccountBillDetail"></table>
							<div id="pager_list_outAccountBillDetail"></div>
                            <div class="col-sm-0" style="margin-top:30px">
                                 <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                 <%--<button id="returnUp" type="button" class=" btn btn-default" onclick="javascript:history.back(-1);" value="" /><span class="glyphicon gly-return"></span>返回</button>--%>
                                 <button id="returnUp" type="button" class=" btn btn-default" onclick="winClose()" value="" />关闭</button>
                            </div>
						</div>
					</div>
				</div>
			</div>
			<br/>
            <div class="col-lg-12">
	            
            </div>
		</div>
			
	</div>
	
   </body>
    
	<title>
	<script src="${ctx}/js/plugins/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
	<script src="${ctx}/js/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js"></script>
	<script type="text/javascript">
	    var data = "" ;//出账失败的记录
        // 去除空格啊
        $('input').blur(function(){
            replaceSpace(this);
        })
        function replaceSpace(obj){
            obj.value = obj.value.replace(/\s/gi,'')
        }
		function getParams(o){
		     o["${_csrf.parameterName}"] = "${_csrf.token}";    
		     o["outBillId"] = "${params.outBillId}";
		}
		
		function customStatusFormatter(cellvalue, options, rowObject) {
			// alert(cellvalue);
			if (cellvalue == 2) {
				return "成功";
			} else if (cellvalue == 3) {
				return "失败";
			} else {
				return "";
			}
		}
		
		function getParams2(o){
			 var data=$("#outAccountBillDetailForm").serializeArray();
		     $.each(data, function() {    
		             o[this.name] = this.value || '';    
		     });   
		     o["outBillId"] = "${params.outBillId}";
		}
		$("#outAccountBillDetailForm").submit(function(){
			
			$.ajax({
                cache: false,
                type: "POST",
                url:"${ctx}/chuAccountAction/collectSubOutBillDetailAmount.do",
                data:$('#outAccountBillDetailForm').serialize(),// formid
                async: false,
                success: function(data) {
                	 $("#outBillSuccess").html(data.outBillSuccess);
                     $("#outBillSuccessAmount").html(data.outBillSuccessAmount.toFixed(2));
                     
                     $("#outBillFail").html(data.outBillFail);
                     $("#outBillFailAmount").html(data.outBillFailAmount.toFixed(2));
                     
                     $("#outBillIng").html(data.outBillIng);
                     $("#outBillIngAmount").html(data.outBillIngAmount.toFixed(2));
                     
                     $("#outBillNo").html(data.outBillNo);
                     $("#outBillNoAmount").html(data.outBillNoAmount.toFixed(2));
                }
            });
			
			$("#table_list_outAccountBillDetail").setGridParam({
			       datatype : 'json',
			       page : 1            //Replace the '1' here
			    }).trigger("reloadGrid");
			return false;
		});
		
		$(document).ready(function() {
			
			$.ajax({
                cache: false,
                type: "POST",
                url:"${ctx}/chuAccountAction/collectSubOutBillDetailAmount.do",
                data:$('#outAccountBillDetailForm').serialize(),// formid
                async: false,
                success: function(data) {
                	 $("#outBillSuccess").html(data.outBillSuccess);
                     $("#outBillSuccessAmount").html(data.outBillSuccessAmount.toFixed(2));
                     
                     $("#outBillFail").html(data.outBillFail);
                     $("#outBillFailAmount").html(data.outBillFailAmount.toFixed(2));
                     
                     $("#outBillIng").html(data.outBillIng);
                     $("#outBillIngAmount").html(data.outBillIngAmount.toFixed(2));
                     
                     $("#outBillNo").html(data.outBillNo);
                     $("#outBillNoAmount").html(data.outBillNoAmount.toFixed(2));
                }
            });
			
			
			/* 收单机构出账明细      ==开始  ==       */
			var lastsel;
			//var sequenceNo = 0 ;
			//var data=$("#bankAccountForm").serialize();
			//alert(data);alert('${list[0].getUserType}');
            // 初始化表格
            $("#table_list_acqOutBill").jqGrid({
            	url:"${ctx}/chuAccountAction/findAcqOutBillList.do",
                datatype: "json",
                mtype: "POST",
                height:"auto",
                autowidth: true,
                shrinkToFit: true,
                rowNum: 10,
                rowList: [10, 20],
                colNames:['上游', '上游结算中余额','出账单金额', '出账任务金额','实际出账金额'],
                colModel: [
					{name: 'acqOrgNo', index: 'acqOrgNo', width: 50, align: "left"},
					{name: 'upBalance', index: 'upBalance',width: 50, align: "right",formatter: 'number'},
					{name: 'outAccountTaskAmount', index: 'outAccountTaskAmount',width: 50, align: "right",formatter: 'number'},
					{name: 'calcOutAmount', index: 'calcOutAmount',width: 50, align: "right",formatter: 'number'},
					{name: 'outAmount', index: 'outAmount',width: 50, align: "right",formatter: 'number'},
                    
                ],
                onSelectRow: function(id){
            		if(id && id!==lastsel){
            			jQuery('#table_list_acqOutBill').jqGrid('restoreRow',lastsel);
            			jQuery('#table_list_acqOutBill').jqGrid('editRow',id,true);
            			lastsel=id;
            		}
            	},
                multiselect: false,//支持多项选择
                pager: "#pager_list_acqOutBill",
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
                    var ids=$("#table_list_acqOutBill").jqGrid('getDataIDs');
                    for(var i=0; i<ids.length; i++){
                        var id=ids[i];   
                        var detail = "<a href='${ctx}/chuAccountAction/toSettleTransferDetail.do?id='"+id+"' class='default-details' title='详情'>详情</a>";
                        jQuery("#table_list_acqOutBill").jqGrid('setRowData', ids[i], { Detail: detail });
                    }
                }
            });
            /* 收单机构出账明细       ==结束 ==  */
            
            /* 商户出账明细       == 开始  ==   */
            
            $("#table_list_outAccountBillDetail").jqGrid({
            	url:"${ctx}/chuAccountAction/findSubOutBillDetailList.do",
                datatype: "json",	
                mtype: "POST",
                height:"auto",
                autowidth: true,
                shrinkToFit:false,
                autoScroll: false,
                rowNum: 10,
                rowList: [10, 20],
                colNames:['子出账单明细','出账单明细',  '商户编号','商户进件编号','银联报备商户编号','商户结算中金额','出账任务金额','出款通道','商户出账金额','订单参考号','交易金额','交易时间','校验通过','校验错误信息','财务调整备注','记账状态','出账结果'],
                colModel: [
                	{name: 'id', index: 'id',width: 150, align: "left"},
					{name: 'outBillDetailId', index: 'outBillDetailId', width: 100, align: "left"},
					{name: 'merchantNo', index: 'merchantNo',width: 150, align: "left"},
					{name: 'plateMerchantEntryNo', index: 'plateMerchantEntryNo',width: 150, align: "left"},
					{name: 'acqMerchantNo', index: 'acqMerchantNo',width: 150, align: "left"},
					{name: 'merchantBalance', index: 'merchantBalance',width: 130, align: "right",formatter: 'number'},
					{name: 'outAccountTaskAmount', index: 'outAccountTaskAmount',width: 130, align: "right",formatter: 'number'},
					{name: 'acqOrgNo', index: 'acqOrgNo',width: 130, align: "center"},
					{name: 'outAccountTaskAmount', index: 'outAccountTaskAmount',width: 130, align: "center",formatter: 'number'},
					{name: 'orderReferenceNo', index: 'orderReferenceNo',width: 180, align: "center"},
					{name: 'transAmount', index: 'transAmount',width: 130, align: "center",formatter: 'number'},
					{name: 'transTime', index: 'transTime',width: 150, align: "center",
						formatter : function(val) {return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
					
					{name: 'verifyFlag', index: 'verifyFlag',width: 120, align: "center",formatter:customVerifyFlagFormatter},
					{name: 'verifyMsg', index: 'verifyMsg',width: 120, align: "center"},
					{name: 'changeRemark', index: 'changeRemark',width: 120, align: "center"},
					{name: 'recordStatus', index: 'recordStatus',width: 150, align: "center",formatter:customRecordStatusFormatter},
					{name: 'outBillStatus', index: 'outBillStatus',width: 120, align: "center",formatter:customOutBillStatusFormatter},
                ],
                onSelectRow: function(id){
            		if(id && id!==lastsel){
            			jQuery('#table_list_outAccountBillDetail').jqGrid('restoreRow',lastsel);
            			jQuery('#table_list_outAccountBillDetail').jqGrid('editRow',id,true);
            			lastsel=id;
            		}
            	},
                multiselect: false,//支持多项选择
                pager: "#pager_list_outAccountBillDetail",
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
        			getParams2(postData);
                    return postData;
                },
                gridComplete:function(){  //在此事件中循环为每一行添加修改和删除链接
                    var ids=$("#table_list_outAccountBillDetail").jqGrid('getDataIDs');
                    for(var i=0; i<ids.length; i++){
                        var id=ids[i];   
                        var getRow = $('#table_list_outAccountBillDetail').getRowData(id);
                        if("出款失败" == getRow.outBillStatus){//出款失败id
                           data += id+"," 
                        }
                        var detail = "<a href='${ctx}/chuAccountAction/toSettleTransferDetail.do?id='"+id+"' class='default-details' title='详情'>详情</a>";
                        jQuery("#table_list_outAccountBillDetail").jqGrid('setRowData', ids[i], { Detail: detail });
                    }
                }
            });
            
            /* 商户出账明细      ==  结束  ==  */
            
            // Add responsive to jqGrid
            $(window).bind('resize', function () {
                var width = $('.jqGrid_wrapper').width();
                $('#table_list_acqOutBill').setGridWidth(width);
            });
            
            $('.input-daterange').datepicker({
                format: "yyyy-mm-dd",
                language: "zh-CN",
                todayHighlight: true,
                autoclose: true,
                clearBtn: true
            });
            
		});
		
		function customVerifyFlagFormatter(cellvalue, options, rowObject){  
			<c:forEach var="vf" items="${verifyFlagList}">
			  	if(cellvalue == '${vf.sysValue}'){
				  	return '${vf.sysName}';
			  	}
		 	</c:forEach>	
			return "" ;
		}
		function customRecordStatusFormatter(cellvalue, options, rowObject){  
			<c:forEach var="vf" items="${recordStatusList}">
			  	if(cellvalue == '${vf.sysValue}'){
				  	return '${vf.sysName}';
			  	}
		 	</c:forEach>	
			return "" ;
		}
		function customOutBillStatusFormatter(cellvalue, options, rowObject){
			<c:forEach var="vf" items="${billStatusList}">
		  	if(cellvalue == '${vf.sysValue}'){
			  	return '${vf.sysName}';
		  	}
	 	</c:forEach>	
		return "" ;
		}
		
		/*导出*/
   		function exportExcel() {  
   			var data=$("#outAccountBillDetailForm").serializeArray();
   			$.download('${ctx}/chuAccountAction/exportOutBillDetail.do',data,'post');
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
		function goBack(){
			window.location.href='${ctx}/chuAccountAction/toChuAccountBillManage.do';
		}
		
		function toErrorResult(){
			window.location.href='${ctx}/chuAccountAction/toMerchantChuAccountResult.do?outBillId='+${params.outBillId}+'&selectIds='+data;
		}
		function winClose() {
			window.top.opener=null;
			window.close();
		}

	</script>
	 </title>
</html>  
      