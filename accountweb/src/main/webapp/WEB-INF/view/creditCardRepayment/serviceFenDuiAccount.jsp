
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
            <div class="pull-left">综合服务对账</div>
            <em class=""></em>
            <div class="pull-left active">服务商查询</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="superPushShareDayForm">
                          <div class="form-group" >
							  <label class="col-sm-2 control-label">服务商名称：</label>
							  <div class="col-sm-2"><input type="text" class="form-control" name="agentName" ></div>

							  <label class="col-sm-3 control-label">服务商编号：</label>
							  <div class="col-sm-2"><input type="text" class="form-control" name="profitMerNo" ></div>

							</div>

						 <div class="form-group" >
							 <label class="col-sm-2 control-label">分润金额:</label>
							 <div class="col-sm-4 form-inline">
								 <input type="text" class="form-control" name="fenMoney1" onkeyup="amount(this)"  >-
								 <input type="text" class="form-control" name="fenMoney2" onkeyup="amount(this)"  >
							 </div>
							 <label class="col-sm-1 control-label">还款订单：</label>
							 <div class="col-sm-2"><input type="text" class="form-control" name="orderNo"  ></div>
						 </div>

						 <div class="form-group" >
							 <label class="col-sm-2 control-label">用户名称：</label>
							 <div class="col-sm-2"><input type="text" class="form-control" name="userName" ></div>

							 <label class="col-sm-3 control-label">用户编号：</label>
							 <div class="col-sm-2"><input type="text" class="form-control" name="merchantNo" ></div>

						 </div>

						 <div class="form-group" >
							 <label class="col-sm-2 control-label">订单类型:</label>
							 <div class="col-sm-2">
								 <select class="form-control" name="profitType" id="profitType">
									 <option value="ALL" selected="selected">全部</option>
									 <option value="1">分期还款分润</option>
									 <option value="2">全额还款分润</option>
									 <option value="3">保证金分润</option>
									 <option value="4">完美还款分润</option>
								 </select>
							 </div>

							 <label class="col-sm-3 control-label">代理商级别:</label>
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

						 <div class="form-group" >
							 <label class="col-sm-2 control-label">订单时间:</label>
							 <div class="col-sm-4">
								 <div class="input-daterange input-group" id="createTime1">
									 <input type="text" class="input-sm form-control" name="orderTime1"/>
									 <span class="input-group-addon">~</span>
									 <input type="text" class="input-sm form-control" name="orderTime2"/>
								 </div>
							 </div>

						 </div>

						 <div class="form-group" style="margin-bottom:0">
                                    <label class="col-sm-2 control-label aaa"></label>
                                    <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
							 		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
							 		<button class="btn btn-success" type="submit"><span class="glyphicon gly-search"></span>查询</button>
							 		<button class="btn btn-default col-sm-offset-14" type="reset" id="reset"><span class="glyphicon gly-trash"></span>清空</button>
									 <sec:authorize access="hasAuthority('serviceShareDaySettle:collection')">
										 <button class="btn btn-success" type="button" id="collection"><span class="glyphicon"></span>汇总</button>
									 </sec:authorize>
									 <sec:authorize access="hasAuthority('serviceShareList:export')">
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
					<sec:authorize access="hasAuthority('serviceCollectionDataCount:count')">
						<span>分润总额：<span id="allShareTotalAmount">0.00</span> 元</span>
						<br/>
						<br/>
					</sec:authorize>		
					<div class="jqGrid_wrapper">
					<table id="table_list_superPushShareDaySettle"></table>
					<div id="pager_list_superPushShareDaySettle"></div>
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
							<label class="col-sm-3 control-label">交易日期:</label>
							<div class="col-sm-4">
								<div class="input-daterange input-group" id="collectionDatepicker">
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

        function amount(th){
            var regStrs = [
                ['^0(\\d+)$', '$1'], //禁止录入整数部分两位以上，但首位为0
                ['[^\\d\\.]+$', ''], //禁止录入任何非数字和点
                ['\\.(\\d?)\\.+', '.$1'], //禁止录入两个以上的点
                ['^(\\d+\\.\\d{2}).+', '$1'] //禁止录入小数点后两位以上
            ];
            for(var i=0; i<regStrs.length; i++){
                var reg = new RegExp(regStrs[i][0]);
                th.value = th.value.replace(reg, regStrs[i][1]);
            }
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
			var data = $("#superPushShareDayForm").serialize();
			$.download('${ctx}/serviceDuiAccount/exportServiceShareList.do',data,'post');
	    }
	
		$("#superPushShareDayForm").submit(function() {
			$("#table_list_superPushShareDaySettle").setGridParam({
				datatype : 'json',
				page : 1
			//Replace the '1' here
			}).trigger("reloadGrid");
			//汇总
			collection();
			return false;
		});
		
		function getParams(o) {
			var data = $("#superPushShareDayForm").serializeArray();
			$.each(data, function() {
				o[this.name] = this.value || '';
			});
		}
		
		function collection(){
			<sec:authorize access="hasAuthority('serviceCollectionDataCount:count')">
				$.ajax({
	                cache: false,
	                type: "POST",
	                url:"${ctx}/serviceDuiAccount/serviceCollectionDataCount.do",
	                data:$('#superPushShareDayForm').serialize(),// formid
	                async: false,
	                success: function(data) {
	                     $("#allShareTotalAmount").html(data.mapSuccess);
	                }
	            });
			</sec:authorize>		
		}
		
		var num = 0;
		function chkNum(num){
	       	 return (num%2 ==0) ?true:false;  //判断是否能整除2
	    }

        function formatDjho(cellvalue, options, rowObject){
            if(rowObject["profitType"] == "3" || rowObject["profitType"] == "4"){
                return "";
            }
            return Number(cellvalue);
        }

		$(document).ready(function() {
			var lastsel;
			//var data = $("#superPushShareDayForm").serialize();
			// 初始化表格
			$("#table_list_superPushShareDaySettle")
					.jqGrid({url : "${ctx}/serviceDuiAccount/serviceFenDuiAccountList.do",
								datatype : "json",
								mtype : "POST",
								height:"auto",
								autowidth: true,
								shrinkToFit: false,
								autoScroll: false,
								rowNum: 10,
								rowList: [ 10, 20 ],
								// '入账信息',
								colNames : ['序号', '关联还款订单','汇总批次号','订单时间', '服务商编号', '服务商名称','代理商级别','一级代理商名称','一级代理商编号','订单类型','已消费总金额','已还款金额','收单机构','收单扣率','收单手续费', '分润','产生分润金额','任务金额','保证金','服务费', '实际交易手续费', '实际代付手续费', '实际费率', '用户编号', '用户名称' ],
								colModel : [
										{ name : 'id', index : 'id', width : 80, align : "center",hidden: true },
										{ name : 'orderNo', index : 'orderNo', width : 150, align : "center" },
										{ name : 'collectionBatchNo', index : 'collectionBatchNo', width : 150, align : "center" },
										{ name : 'transTime', index : 'transTime', width : 180, align : "center" ,formatter : function(val) {return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
                                    	{ name : 'profitMerNo', index : 'profitMerNo', width : 150, align : "center" },
										{ name : 'agentName', index : 'agentName', width : 120, align : "center" },
										{ name : 'agentLevel', index : 'agentLevel', width : 120, align : "center" },

										{ name : 'oneAgentName', index : 'oneAgentName', width : 120, align : "center" },
										{ name : 'oneLevelId', index : 'oneLevelId', width : 120, align : "center" },

										{ name : 'profitType', index : 'profitType', width : 120, align : "center" ,formatter : function(val) {
										    if(val=="1"){
                                                return "分期还款分润";
											}else if(val=="2"){
                                                return "全额还款分润";
											}else if(val == "3"){
                                                return "保证金分润";
											}else if(val == "4"){
                                                return "完美还款分润";
                                            }
											return "";
										}},

                                    	{ name : 'successPayAmount', index : 'successPayAmount', width : 130, align : "center" ,formatter: formatDjho},
                                    	{ name : 'successRepayAmount', index : 'successRepayAmount', width : 130, align : "center" ,formatter: formatDjho},
                                    	{ name : 'acqCode', index : 'acqCode', width : 120, align : "center" },
										{ name : 'fee', index : 'fee', width : 130, align : "center" ,formatter: 'number', formatoptions: {defaulValue:"0",decimalPlaces:4}},
                                    	{ name : 'payFl', index : 'payFl', width : 130, align : "center" ,formatter: 'number', formatoptions: {defaulValue:"0",decimalPlaces:4}},

										{ name : 'shareAmount', index : 'shareAmount', width : 130, align : "center" ,formatter: 'number'},

										{ name : 'toProfitAmount', index : 'toProfitAmount', width : 130, align : "center" ,formatter: 'number'},

										{ name : 'repayAmount', index : 'repayAmount', width : 130, align : "center" ,formatter: formatDjho},
										{ name : 'ensureAmount', index : 'ensureAmount', width : 130, align : "center" ,formatter: formatDjho},
										{ name : 'repayFee', index : 'repayFee', width : 130, align : "center" ,formatter: formatDjho},
										{ name : 'actualPayFee', index : 'actualPayFee', width : 130, align : "center" ,formatter: formatDjho},
										{ name : 'actualWithdrawFee', index : 'actualWithdrawFee', width : 130, align : "center" ,formatter: formatDjho},
										{ name : 'repayFeeRate', index : 'repayFeeRate', width : 120, align : "center" },
										{ name : 'merchantNo', index : 'merchantNo', width : 120, align : "center" },
										{ name : 'userName', index : 'userName', width : 120, align : "center" }
										 ],
								//multiselect : true,//支持多项选择
								multiselectWidth : 30,
								multiboxonly: false,
								pager : "#pager_list_superPushShareDaySettle",
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
								}
							});
			
					jQuery("#table_list_superPushShareDaySettle").jqGrid('setFrozenColumns');
					$(window).bind('resize',function() {
						var width = $('.jqGrid_wrapper').width();
						$('#table_list_superPushShareDaySettle').setGridWidth(width);
					});

			$('#createTime1').datepicker({
				format : "yyyy-mm-dd",
				language : "zh-CN",
				todayHighlight : true,
				autoclose : true,
				clearBtn : true
			});
			$('#groupTime1').datepicker({
				format : "yyyy-mm-dd",
				language : "zh-CN",
				todayHighlight : true,
				autoclose : true,
				clearBtn : true
			});
			
			$('#collectionDatepicker').datepicker({
				format : "yyyy-mm-dd",
				language : "zh-CN",
				todayHighlight : true,
				autoclose : true,
				clearBtn : true,
			    endDate : new Date(new Date()-24*60*60*1000)  
			});
			
            //汇总
			collection();
		});


    //点击汇总
    $("#collection").click(function() {
        $("#myModalCollection").modal("show");
    });

		 $("#reset").on("click", function () { 
	            //$exampleMulti.val(null).trigger("change");
	            //$('#agentNo').select2("val", "");
	            $("#agentNo").empty().trigger("change");
	        });


	      	function goAgentProfitCollection(_collectionDate){
	      		 $.post('${ctx}/serviceDuiAccount/serviceShareCollection.do',
						{ createDate:_collectionDate,'${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) { 
							$("#wrapper").hideLoading();
							if(!msg.status){
								//alert(msg.msg);
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
								setTimeout(function() {
									location.href='${ctx}/serviceDuiAccount/toServiceFenDuiAccountList.do';
								}, 1000);
							}
						});  
	            
	      	}

    /*汇总*/
    function saveAgentProfitCollection(){
        $("#wrapper").showLoading();
        var _collectionDate = $("#collectionDate").val();
        goAgentProfitCollection(_collectionDate);
    }

	</script>
	 </title>
</html>  
      