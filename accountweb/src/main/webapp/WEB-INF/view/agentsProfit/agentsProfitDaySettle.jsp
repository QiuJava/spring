
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<!-- jqGrid plugin -->
	<link href="${ctx}/css/plugins/select2/select2.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/select2/select2-skins.min.css" rel="stylesheet">
	<link rel="stylesheet" href="${ctx}/css/icheck-css/custom.css" />
	<link rel="stylesheet" href="${ctx}/css/skins/all.css" />
	 <!-- Sweet Alert -->
    <link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/jqGrid/ui.jqgrid.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/bootstrapTour/bootstrap-tour.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/webuploader/webuploader.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/bootstrap-datepicker/bootstrap-datepicker3.min.css" rel="stylesheet">
	<link href="${ctx}/css/showLoading.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">代理商分润管理</div>
            <em class=""></em>
            <div class="pull-left active">代理商分润日结报表</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="agentsProfitDayForm">
                          <div class="form-group" >
                          	  <label class="col-sm-2 control-label">交易日期:</label>
                           		<div class="col-sm-3">
		                            <div class="input-daterange input-group" id="datepicker">
									    <input type="text" class="input-sm form-control" name="transDate1" value="${date1}"/>
									    <span class="input-group-addon">~</span>
									    <input type="text" class="input-sm form-control" name="transDate2" value="${date2}"/>
									</div>   
								</div>
								<label class="col-sm-2 control-label">汇总时间:</label>
                           		<div class="col-sm-3">
		                            <div class="input-daterange input-group" id="datepicker">
									    <input type="text" class="input-sm form-control" name="groupTime1" />
									    <span class="input-group-addon">~</span>
									    <input type="text" class="input-sm form-control" name="groupTime2"/>
									</div>   
								</div>
							</div> 
							<div class="form-group" > 
								<label class="col-sm-2 control-label">代理商名称:</label>
		                           <div class="col-sm-3">
                                   <select id="agentNo" class="form-control" name="agentNo">
									</select>
								</div>
								
								<label class="col-sm-2 control-label">入账状态:</label>
    							<div class="col-sm-2">
                                        <select class="form-control" name="enterAccountStatus" id="enterAccountStatus"> 
                                             <option value="ALL" selected="selected">全部</option>
                                             <c:forEach var="vf" items="${enterAccountStatusList}">
                                                <option value="${vf.sysValue}">
                                                    ${vf.sysName}
                                                </option>
                                             </c:forEach>
                                        </select>      
                                    </div>	
							</div> 
							
							<div class="form-group" > 
								<label class="col-sm-2 control-label">汇总批次号:</label>
		                           <div class="col-sm-3">
		                           <input type="text" class="input-sm form-control" name="collectionBatchNo"/>
								</div>
								
								<label class="col-sm-2 control-label">代理商级别:</label>
    							<div class="col-sm-2">
                                        <select class="form-control" name="agentLevel" id="agentLevel">
                                             <option value="ALL" selected="selected">全部</option>
                                                <option value="1">1级</option>
                                                <option value="2">2级</option>
                                                <option value="3">3级</option>
                                                <option value="4">4级</option>
                                                <option value="5">5级</option>
                                                <option value="6">6级</option>
                                                <option value="7">7级</option>
                                                <option value="8">8级</option>
                                                <option value="9">9级</option>
                                                <option value="10">10级</option>
                                                <option value="11">11级</option>
                                                <option value="12">12级</option>
                                                <option value="13">13级</option>
                                                <option value="14">14级</option>
                                                <option value="15">15级</option>
                                                <option value="16">16级</option>
                                                <option value="17">17级</option>
                                                <option value="18">18级</option>
                                                <option value="19">19级</option>
                                                <option value="20">20级</option>
                                        </select>
                                    </div>
							</div> 
							
                            <div class="clearfix lastbottom"></div>
                            
                            <div class="form-group" style="margin-bottom:0">
                                    <label class="col-sm-2 control-label aaa"></label>
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                   	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                       	<button class="btn btn-success" type="submit"><span class="glyphicon gly-search"></span>查询</button>
                                       	<button class="btn btn-default col-sm-offset-14" type="reset" id="reset"><span class="glyphicon gly-trash"></span>清空</button>
                                   <!-- </div> -->
                             </div>
                             <br />
                            <div class="form-group" style="margin-bottom:0">
                                    <label class="col-sm-2 control-label aaa"></label>
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                        <sec:authorize access="hasAuthority('agentsProfitDaySettle:collection')">
                                   	   	<button class="btn btn-success" type="button" id="collection"><span class="glyphicon"></span>汇总</button>
                                   	   	</sec:authorize>
                                       	<sec:authorize access="hasAuthority('agentsProfitDaySettle:tryCalculation')">
                                       	<button class="btn btn-success" type="button" id="tryCalculation" style="margin-left:10px;"><span class="glyphicon"></span>试算</button>
                                       	</sec:authorize>
                                       	<sec:authorize access="hasAuthority('agentsProfitDaySettle:enterAccount')">
                                       	<button class="btn btn-success" type="button" id="enterAccount" style="margin-left:10px;"><span class="glyphicon"></span>批量入账</button> 
                                       	</sec:authorize>
                                       	<sec:authorize access="hasAuthority('agentsProfitDaySettle:export')">                    	
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
					<span>分润入账比例:${params.enterScale}%</span>
					<sec:authorize access="hasAuthority('agentsProfitDaySettle:updateEnterScale')">  
						<button class="btn btn-success btn-sm col-sm-offset-14" type="button"  id="updateEnterScale" ><span class="glyphicon"></span>修改</button>
					</sec:authorize>

					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>当前分润提现状态：</span>
					<c:if test="${params.accountantShareAccounting =='0'}" >
					<span id="accountingSpan" class="label label-info">已打开可提现</span>
					</c:if>
					<c:if test="${params.accountantShareAccounting =='1'}" >
						<span id="accountingSpan" class="label label-danger">财务正在进行分润入账，请稍后再来提现。</span>
					</c:if>

					<br/>
					<br/>
					<sec:authorize access="hasAuthority('agentsProfitDaySettle:collectionData')">  
						<span>汇总：交易总金额：<span id="allTransTotalAmount">0.00</span> ,&nbsp;&nbsp;提现总笔数：<span  id="allCashTotalNum">0</span>,&nbsp;&nbsp;原交易分润：<span  id="allPreTransShareAmount">0.00</span>,&nbsp;&nbsp;
							原提现分润：<span  id="allPreTransCashAmount">0.00</span>,&nbsp;&nbsp;调账金额：<span  id="allAdjustAmount">0.00</span>,&nbsp;&nbsp;  调整后总分润：<span id="allAdjustTotalShareAmount">0.00</span> </span>
						<br/>
						<br/>
					</sec:authorize>		
					<div class="jqGrid_wrapper">
					<table id="table_list_agentsProfitDaySettle"></table>
					<div id="pager_list_agentsProfitDaySettle"></div>
                    <br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
					</div>
				</div>
			</div>
		</div>
		
	</div>	
	
	<div class="modal inmodal" id="myModalCollection" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
        	<div class="modal-content">
            	<div class="modal-header">
                	<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                	<h5 class="modal-title">代理商分润汇总</h5>
                </div>
                <div class="modal-body"  >
                	<form method="post" class="form-horizontal" id="collectionForm">
                	
					<div class="form-group">
					<label class="col-sm-2 control-label">交易日期:</label>
                           		<div class="col-sm-4">
		                            <div class="input-daterange input-group" id="datepicker">
									    <input type="text" class="input-sm form-control" name="collectionDate" id="collectionDate"/>
									</div>   
								</div>
					</div>
					</form>
         		</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" onclick="saveAgentProfitCollection()">确认</button>
            		<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
         		</div>
  			</div>
		</div>
	</div>
		<div class="modal inmodal" id="myModalTryCalculation" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
        	<div class="modal-content">
            	<div class="modal-header">
                	<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                	<h5 class="modal-title">代理商分润试算</h5>
                </div>
                <div class="modal-body"  >
                	<form method="post" class="form-horizontal" id="tryCalculationForm">
					<div class="form-group">
					<label class="col-sm-2 control-label">交易日期:</label>
                           		<div class="col-sm-4">
		                            <div class="input-daterange input-group" id="datepicker">
									    <input type="text" class="input-sm form-control" name="tryCalculationDate" id="tryCalculationDate"/>
									</div>   
								</div>
					</div>
					</form>
         		</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" onclick="saveTryCalculation()">确认</button>
            		<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
         		</div>
  			</div>
		</div>
	</div>
		<div class="modal inmodal" id="myModalEnterAccount" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
        	<div class="modal-content">
            	<div class="modal-header">
                	<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                	<h5 class="modal-title">代理商分润入账</h5>
                </div>
                <div class="modal-body"  >
                	<form method="post" class="form-horizontal" id="enterAccountForm">
					<div class="form-group">
					<label class="col-sm-2 control-label">交易日期:</label>
                           		<div class="col-sm-4">
		                            <div class="input-daterange input-group" id="datepicker">
									    <input type="text" class="input-sm form-control" name="enterAccountDate" id="enterAccountDate"/>
									</div>   
								</div>
					</div>
					</form>
         		</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" onclick="saveEnterAccount()">确认</button>
            		<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
         		</div>
  			</div>
		</div>
	</div>
	
	<div class="modal inmodal" id="myModalUpdateEnterScale" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
        	<div class="modal-content">
            	<div class="modal-header">
                	<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                	<h5 class="modal-title">分润入账比例</h5>
                </div>
                <div class="modal-body"  >
                	<form method="post" class="form-horizontal" id="updateEnterScaleForm">
					<div class="form-group">
					<label class="col-sm-3 control-label">分润入账比例%:</label>
                           		<div class="col-sm-4">
									    <input type="number" class="input-sm form-control" name="enterScale" id="enterScale" value="${params.enterScale}"/>
								</div>
					</div>
					</form>
         		</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" onclick="updateEnterScale()">确认</button>
            		<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
         		</div>
  			</div>
		</div>
	</div>		
	
	<!-- 填充内容结束 -->
		
</body>
    
<title>
<script src="${ctx}/js/icheck.min.js"></script>
<script src="${ctx}/js/custom.min.js"></script>
<script src="${ctx}/js/plugins/select2/select2.full.min.js"></script>
<script src="${ctx}/js/plugins/select2/i18n/zh-CN.js"></script>
<!-- Sweet alert -->
<script src="${ctx}/js/plugins/sweetalert/sweetalert.min.js"></script>
	<script src="${ctx}/js/plugins/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
	<script src="${ctx}/js/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js"></script>
	
	<script src="${ctx}/js/jquery.showLoading.js"></script>
	<script src="${ctx}/js/jquery.showLoading.min.js"></script>
	
	<script type="text/javascript">
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
			var data = $("#agentsProfitDayForm").serialize();
			$.download('${ctx}/agentsProfitAction/exportAgentsProfitDaySettleList.do',data,'post');
	    }
	
		$("#agentsProfitDayForm").submit(function() {
			$("#table_list_agentsProfitDaySettle").setGridParam({
				datatype : 'json',
				page : 1
			//Replace the '1' here
			}).trigger("reloadGrid");
            //汇总
            collection();
			return false;
		});
		
		function getParams(o) {
			var data = $("#agentsProfitDayForm").serializeArray();
			$.each(data, function() {
				o[this.name] = this.value || '';
			});
			o.agentNo = $("#agentNo").select2("val");
			//console.info($("#agentNo").select2("val"));
		}
		
		function collection(){
			<sec:authorize access="hasAuthority('agentsProfitDaySettle:collectionData')"> 
				$.ajax({
	                cache: false,
	                type: "POST",
	                url:"${ctx}/agentsProfitAction/findAgentsProfitDaySettleListCollection.do",
	                data:$('#agentsProfitDayForm').serialize(),// formid
	                async: false,
	                success: function(data) {
	                     $("#allTransTotalAmount").html(data.allTransTotalAmount);
	                     $("#allCashTotalNum").html(data.allCashTotalNum);
	                     $("#allAdjustTotalShareAmount").html(data.allAdjustTotalShareAmount);
	                     $("#allPreTransShareAmount").html(data.allPreTransShareAmount);
	                     $("#allPreTransCashAmount").html(data.allPreTransCashAmount);
	                     $("#allAdjustAmount").html(data.allAdjustAmount);
	                }
	            });
			</sec:authorize>		
		}
		
		$(document).ready(function() {
			var lastsel;
			//var data = $("#agentsProfitDayForm").serialize();
			// 初始化表格
			$("#table_list_agentsProfitDaySettle")
					.jqGrid({url : "${ctx}/agentsProfitAction/findAgentsProfitDaySettleList.do",
								datatype : "local",
								mtype : "POST",
								height:"auto",
								autowidth: true,
								shrinkToFit: false,
								autoScroll: false,
								rowNum: 10,
								rowList: [ 10, 20 ],
								colNames : [ '汇总批次号','汇总时间', '交易日期', '代理商编号', '代理商名称','代理商级别','上级代理商编号','上级代理商名称', '所属销售', '交易总金额', '交易总笔数', '对账成功交易总笔数',
									'对账成功交易总金额', '提现总笔数', '商户交易手续费','抵扣交易商户手续费','实际交易商户手续费','自选商户手续费','抵扣自选商户手续费','实际自选商户手续费', '商户提现手续费', '抵扣商户提现手续费','收单成本', '收单收益', '代付成本', '垫资成本',
									'原交易分润 ', '原提现分润 ', '开通返现 ', '费率差异 ', '超级推成本 ', '风控扣款 ','商户管理费 ', '保证金扣除 ',  '其他 ', '调整后交易分润 ',
									'调整后提现分润 ', '调整后总分润 ', '实际到账分润 ', '机具款冻结 ', '其他冻结 ', '入账状态', '入账时间', '入账信息', '操作人', '操作' ],
								colModel : [
										{ name : 'collectionBatchNo', index : 'collectionBatchNo', width : 180, align : "center" },
										{ name : 'groupTime', index : 'groupTime', width : 180, align : "center" ,formatter : function(val) {return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
										{ name : 'transDate', index : 'transDate', width : 150, align : "center" ,formatter : function(val) {return myFormatDate(val,"yyyy-MM-dd");}},
										{ name : 'agentNo', index : 'agentNo', width : 120, align : "center" },
										{ name : 'agentName', index : 'agentName', width : 130, align : "center" },
										{ name : 'agentLevel', index : 'agentLevel', width : 100, align : "center" },
										{ name : 'parentAgentNo', index : 'parentAgentNo', width : 120, align : "center" },
										{ name : 'parentAgentName', index : 'parentAgentName', width : 130, align : "center" },
										{ name : 'saleName', index : 'saleName', width : 200, align : "center" },
										{ name : 'transTotalAmount', index : 'transTotalAmount', width : 130, align : "right" ,formatter : 'number'},
										{ name : 'transTotalNum', index : 'transTotalNum', width : 150, align : "right" },
										{ name : 'duiSuccTransTotalNum', index : 'duiSuccTransTotalNum', width : 200, align : "right" },
										{ name : 'duiSuccTransTotalAmount', index : 'duiSuccTransTotalAmount', width : 200, align : "right",formatter : 'number' },
										{ name : 'cashTotalNum', index : 'cashTotalNum', width : 200, align : "right" },
										{ name : 'merFee', index : 'merFee', width : 130, align : "right" ,formatter : 'number'},
										
										{ name : 'transDeductionFee', index : 'transDeductionFee', width : 135, align : "right" ,formatter : 'number'},
										{ name : 'actualFee', index : 'actualFee', width : 135, align : "right" ,formatter : 'number'},
										{ name : 'merchantPrice', index : 'merchantPrice', width : 135, align : "right" ,formatter : 'number'},
										{ name : 'deductionMerFee', index : 'deductionMerFee', width : 135, align : "right" ,formatter : 'number'},
										{ name : 'actualOptionalFee', index : 'actualOptionalFee', width : 135, align : "right" ,formatter : 'number'},
										
										{ name : 'merCashFee', index : 'merCashFee', width : 130, align : "right" ,formatter : 'number'},
										{ name : 'deductionFee', index : 'deductionFee', width : 160, align : "right" ,formatter : 'number'},
										{ name : 'acqOutCost', index : 'acqOutCost', width : 130, align : "right" ,formatter : 'number'},
										{ name : 'acqOutProfit', index : 'acqOutProfit', width : 130, align : "right" ,formatter : 'number'},
										{ name : 'daiCost', index : 'daiCost', width : 130, align : "right" ,formatter : 'number'},
										{ name : 'dianCost', index : 'dianCost', width : 130, align : "right" ,formatter : 'number'},
										{ name : 'preTransShareAmount', index : 'preTransShareAmount', width : 130, align : "right" ,formatter : 'number'},
										{ name : 'preTransCashAmount', index : 'preTransCashAmount', width : 130, align : "right" ,formatter : 'number'},
										{ name : 'openBackAmount', index : 'openBackAmount', width : 130, align : "right" ,formatter : 'number'},
										{ name : 'rateDiffAmount', index : 'rateDiffAmount', width : 130, align : "right" ,formatter : 'number'},
										{ name : 'tuiCostAmount', index : 'tuiCostAmount', width : 130, align : "right" ,formatter : 'number'},
										{ name : 'riskSubAmount', index : 'riskSubAmount', width : 130, align : "right" ,formatter : 'number'},
										{ name : 'merMgAmount', index : 'merMgAmount', width : 130, align : "right" ,formatter : 'number'},
										{ name : 'bailSubAmount', index : 'bailSubAmount', width : 130, align : "right" ,formatter : 'number'},
										{ name : 'otherAmount', index : 'otherAmount', width : 130, align : "right" ,formatter : 'number'},
										{ name : 'adjustTransShareAmount', index : 'adjustTransShareAmount', width : 130, align : "right" ,formatter : 'number'},
										{ name : 'adjustTransCashAmount', index : 'adjustTransCashAmount', width : 130, align : "right" ,formatter : 'number'},
										{ name : 'adjustTotalShareAmount', index : 'adjustTotalShareAmount', width : 130, align : "right" ,formatter : 'number'},
										{ name : 'realEnterShareAmount', index : 'realEnterShareAmount', width : 130, align : "right" ,formatter : 'number'},
										{ name : 'terminalFreezeAmount', index : 'terminalFreezeAmount', width : 130, align : "right" ,formatter : 'number'},
										{ name : 'otherFreezeAmount', index : 'otherFreezeAmount', width : 130, align : "right" ,formatter : 'number'},
										{ name : 'enterAccountStatus', index : 'enterAccountStatus', width : 130, align : "right" , formatter:  customEnterAccountStatus},
                                    	{ name : 'enterAccountTime',index : 'enterAccountTime',width : 200,align : "right",formatter : function(val) {return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
										{ name : 'enterAccountMessage', index : 'enterAccountMessage', width : 300, align : "right" },
                                    	{name : 'operator', index : 'operator ', width : 130, align : "center" },
										{ name:'Detail',index:'id',width:200,align:"center",sortable:false, },
										 ],
								multiselect : false,//支持多项选择
								multiselectWidth : 80,
								multiboxonly: false,
								pager : "#pager_list_agentsProfitDaySettle",
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
									var ids=$("#table_list_agentsProfitDaySettle").jqGrid('getDataIDs');
				                    for(var i=0; i<ids.length; i++){
				                        var id=ids[i];   
				                        var detail = "" ;
				                        var model = $("#table_list_agentsProfitDaySettle").jqGrid('getRowData', id);
				       				    var enterAccountStatus = model.enterAccountStatus;
				                        <sec:authorize access="hasAuthority('agentsProfitDaySettle:transDetail')">
				                        detail += "<a href='javascript:void(0);'  title='交易明细' onclick='TransDetail(" + id + ")' class='default-details'>交易明细</a>";
				                        </sec:authorize>
				                        <sec:authorize access="hasAuthority('agentsProfitDaySettle:enterAccount')">                               	   
				                        if (enterAccountStatus =="已入账") {
				    						detail += "&nbsp;&nbsp;<a href='javascripgetRowgetRowt:void(0);'  title='入账' class='default-undetails' style='cursor:not-allowed' disabled='disabled' '>入账</a>";
				    						} else {
				    						detail += "&nbsp;&nbsp;<a href='javascript:void(0);'  title='入账' class='default-maintenance' onclick='singleEnterAccount(" + id + ")'>入账</a>";
				    						}	
				                   		</sec:authorize> 
				                        jQuery("#table_list_agentsProfitDaySettle").jqGrid('setRowData', ids[i], { Detail: detail });
				                    }
								},
							});
					jQuery("#table_list_agentsProfitDaySettle").jqGrid('setFrozenColumns');
					$(window).bind('resize',function() {
						var width = $('.jqGrid_wrapper').width();
						$('#table_list_agentsProfitDaySettle').setGridWidth(width);
					});

			$('.input-daterange').datepicker({
				format : "yyyy-mm-dd",
				language : "zh-CN",
				todayHighlight : true,
				autoclose : true,
				clearBtn : true
			});
            //汇总
			collection();
		});
		//点击汇总
		$("#collection").click(function() {
			$("#myModalCollection").modal("show");
		});
		
		//点击试算
		$("#tryCalculation").click(function() {
			$("#myModalTryCalculation").modal("show");
		});
		
		//点击入账
		$("#enterAccount").click(function() {
			$("#myModalEnterAccount").modal("show");
		});
		//修改分润比例
		$("#updateEnterScale").click(function() {
			$("#myModalUpdateEnterScale").modal("show");
		});
		
		
		function TransDetail(id) {   //单击修改链接的操作     
			//console.info(id);
	        var model = $("#table_list_agentsProfitDaySettle").jqGrid('getRowData', id);
	        var pageNo = $("#table_list_agentsProfitDaySettle").jqGrid('getGridParam','page');
	        var pageSize = $("#table_list_agentsProfitDaySettle").jqGrid('getGridParam','rowNum');
	        var sortname = $("#table_list_agentsProfitDaySettle").jqGrid('getGridParam','sortname');
	        var sortorder = $("#table_list_agentsProfitDaySettle").jqGrid('getGridParam','sortorder');
			var collectionBatchNo = model.collectionBatchNo;
			var agentNo = model.agentNo;
	        console.info(model);
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
			location.href='${ctx}/agentsProfitAction/toAgentsProfitDetail.do?collectionBatchNo='+collectionBatchNo+"&agentNo="+agentNo+"&queryParams="+encodeQueryParams;
	        
	    }
		 $("#reset").on("click", function () { 
	            //$exampleMulti.val(null).trigger("change");
	            //$('#agentNo').select2("val", "");
	            $("#agentNo").empty().trigger("change");
	        });
		function formatRepo (repo) {
	        if (repo.loading) return repo.text;
			//console.info(repo.id);
			return repo.id+'('+repo.text+')';  
	      }

	      function formatRepoSelection (repo) {
	    	  //console.info("formatRepoSelection:"+ repo.text);
	    	  return repo.id+'('+repo.text+')';
	        //return repo.full_name || repo.text;
	      }
	      
	      function parseSelectParams(params){
	    	  if(params && params.term){
	    		  return encodeURI(params.term);
	    	  }
	    	  else
	    		  return null;
	      }
	
	   // turn the element to select2 select style
	      $('#agentNo').select2({
		    	  		ajax: {
			    		  	url: "${ctx}/agentsProfitAction/queryAgentName.do",
			    		    dataType: 'json',
			    		    delay: 250,
			    		    data: function (params) {
			    		      return {
			    		        q: parseSelectParams(params), // search term
			    		        page: params.page
			    		      };
			    		    },
			    		    processResults: function (data, params) {
			    		      // parse the results into the format expected by Select2
			    		      // since we are using custom formatting functions we do not need to
			    		      // alter the remote JSON data, except to indicate that infinite
			    		      // scrolling can be used
			    		      params.page = params.page || 1;

			    		      return {
			    		        results: data,
			    		        pagination: {
			    		          more: (params.page * 30) < data.total_count
			    		        }
			    		      };
			    		    },
			    		    cache: true
		    		  },
		    		  placeholder: "选择代理商名称",  
		    		  allowClear: true,
		    		  width: '100%',
		    		  // containerCssClass: 'tpx-select2-container',
		    		  // dropdownCssClass: 'tpx-select2-drop',
		    		  escapeMarkup: function (markup) { return markup; }, // let our custom formatter work
		    		  minimumInputLength: 2,
		    		  language: "zh-CN",
		    		  templateResult: formatRepo, // omitted for brevity, see the source of this page
		    		  templateSelection: formatRepoSelection // omitted for brevity, see the source of this page
		    		}
	    		  );		
		
		
	      function customEnterAccountStatus(cellvalue, options, rowObject){  
				<c:forEach var="enterAccountStatus" items="${enterAccountStatusList}">
					  if(cellvalue == '${enterAccountStatus.sysValue}'){
						  return '${enterAccountStatus.sysName}';
					  }
				 </c:forEach>	
				 return "" ;
			}
	        var validateMsg = {};
	      	function validateAgentProfitCollection(_collectionDate){
	      		$.ajax({
	                cache: false,
	                type: "POST",
	                url:"${ctx}/agentsProfitAction/validateAgentProfitCollection.do",
	                data:{ transDate:_collectionDate,'${_csrf.parameterName}':'${_csrf.token}' },// formid
	                async: false,
	                success: function(data) {
	                	validateMsg = data;
	                }
	            });
	      	}
	      	function goAgentProfitCollection(_collectionDate){
	      		 $.post('${ctx}/agentsProfitAction/agentProfitCollection.do', 
						{ transDate:_collectionDate,'${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) { 
							$("#wrapper").hideLoading();
							if(!msg.status){
								//alert(msg.msg);
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
								setTimeout(function() {
									location.href='${ctx}/agentsProfitAction/toAgentsProfitDaySettle.do';
								}, 1000);
							}
						});  
	      		
			/* $.ajax({
	                cache: false,
	                timeout:480000, //10分钟
	                type: "POST",
	                url:"${ctx}/agentsProfitAction/agentProfitCollection.do",
	                data:{ transDate:_collectionDate,'${_csrf.parameterName}':'${_csrf.token}' },// formid
	                async: false,
	                error:function(jqXHR, textStatus, errorThrown){ 
	                	setTimeout(function() {
							location.href='${ctx}/agentsProfitAction/toAgentsProfitDaySettle.do';
						}, 1000);
	                }, 
	                success: function(msg) {
	                	$("#wrapper").hideLoading();
						if(!msg.status){
							//alert(msg.msg);
				            toastr.error(msg.msg,'错误');
						}else{
							toastr.success(msg.msg,'提示');
							setTimeout(function() {
								location.href='${ctx}/agentsProfitAction/toAgentsProfitDaySettle.do';
							}, 1000);
						}
	                }
	            }); */
	            
	      	}
	       /*汇总*/			
			function saveAgentProfitCollection(){
				$("#wrapper").showLoading();
				var _collectionDate = $("#collectionDate").val();
				validateAgentProfitCollection(_collectionDate);
				console.info(validateMsg);
				if(!validateMsg.status){
					if(confirm(validateMsg.msg)){
						goAgentProfitCollection(_collectionDate);
					}
					else{
						$("#wrapper").hideLoading();
					}
				}
				else{
					goAgentProfitCollection(_collectionDate);
				}
			}
			/*试算*/			
			function saveTryCalculation(){
				$("#wrapper").showLoading();
				var _collectionDate = $("#tryCalculationDate").val();

				//alert(_collectionDate);
				$.post('${ctx}/agentsProfitAction/agentProfitTryCalculation.do', 
						{ transDate:_collectionDate,'${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) { 
							$("#wrapper").hideLoading();
							if(!msg.status){
								//alert(msg.msg);
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
								setTimeout(function() {
									location.href='${ctx}/agentsProfitAction/toAgentsProfitDaySettle.do';
								}, 1000);
							}
						}); 
			}
			
			/*入账*/			
			function saveEnterAccount(){
				$("#wrapper").showLoading();
				var _collectionDate = $("#enterAccountDate").val();
                $("#accountingSpan").html("财务正在进行分润入账，请稍后再来提现。");
				//alert(_collectionDate);
				$.post('${ctx}/agentsProfitAction/agentProfitEnterAccount.do', 
						{ transDate:_collectionDate,'${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) { 
							$("#wrapper").hideLoading();
							if(!msg.status){
								//alert(msg.msg);
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
								setTimeout(function() {
									location.href='${ctx}/agentsProfitAction/toAgentsProfitDaySettle.do';
								}, 1000);
							}
						}); 
			}
			/*单个入账*/
			function  singleEnterAccount(id){
				$("#wrapper").showLoading();
				 var model = $("#table_list_agentsProfitDaySettle").jqGrid('getRowData', id);
				 var agentNo = model.agentNo;
				$.post('${ctx}/agentsProfitAction/agentProfitSingleEnterAccount.do', 
						{ id:id,agentNo:agentNo,'${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) { 
							$("#wrapper").hideLoading();
							if(!msg.status){
								//alert(msg.msg);
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
								setTimeout(function() {
									location.href='${ctx}/agentsProfitAction/toAgentsProfitDaySettle.do';
								}, 1000);
							}
						}); 
			}
			
			function updateEnterScale(){
				<sec:authorize access="hasAuthority('agentsProfitDaySettle:updateEnterScale')"> 
					var _enterScale = $("#enterScale").val();
					//alert(_enterScale);
					$.post('${ctx}/agentsProfitAction/updateAgentsProfitEnterScale.do', 
							{ 'enterScale':_enterScale,'${_csrf.parameterName}':'${_csrf.token}' },
							function(msg) { 
								if(!msg.status){
						            toastr.error(msg.msg,'错误');
								}else{
									toastr.success(msg.msg,'提示');
									setTimeout(function() {
										location.href='${ctx}/agentsProfitAction/toAgentsProfitDaySettle.do';
									}, 1000);
								}
							}); 
					
				</sec:authorize>		
			}
			
	</script>
	 </title>
</html>  
      