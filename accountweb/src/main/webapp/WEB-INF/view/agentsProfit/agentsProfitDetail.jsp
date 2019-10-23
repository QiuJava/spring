
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
	<link href="${ctx}/css/plugins/bootstrap-datepicker/bootstrap-datepicker3.min.css" rel="stylesheet"></head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">代理商分润管理</div>
            <em class=""></em>
            <div class="pull-left active">代理商分润明细</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="agentsProfitDetailForm">
					 
					 
                          <div class="form-group" >
                          	  <label class="col-sm-2 control-label">交易日期:</label>
                           		<div class="col-sm-4">
		                            <div class="input-daterange input-group" id="datepicker">
									    <input type="text" class="input-sm form-control" name="transTime1" value="${date1}" />
									    <span class="input-group-addon">~</span>
									    <input type="text" class="input-sm form-control" name="transTime2" value="${date2}"/>
									</div>   
								</div>
                          	  <label class="col-sm-2 control-label">汇总日期:</label>
                           		<div class="col-sm-4">
		                            <div class="input-daterange input-group" id="datepicker">
									    <input type="text" class="input-sm form-control" name="collectionTime1" />
									    <span class="input-group-addon">~</span>
									    <input type="text" class="input-sm form-control" name="collectionTime2"/>
									</div>   
								</div>
							</div> 
							<div class="form-group" > 
							    
								<label class="col-sm-2 control-label">代理商名称:</label>
		                           <div class="col-sm-3">
                                   <select id="agentNo" class="form-control" name="agentNo">
									</select>
								</div>
								<label class="col-sm-2 control-label">商户名称/编号：</label>
                                <div class="col-sm-2"><input type="text" class="form-control" name="merchantName" id="merchantName"  ></div>
							</div>	
								
							<div class="form-group" > 
                                <label class="col-sm-2 control-label">汇总批次：</label>
                                <div class="col-sm-2"><input type="text" class="form-control" name="collectionBatchNo" id="collectionBatchNo" value="${params.collectionBatchNo}"></div>
							</div>	
                            <div class="clearfix lastbottom"></div>
                            <div class="form-group" style="margin-bottom:0">
                                    <label class="col-sm-2 control-label aaa"></label>
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                   	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                       	<button class="btn btn-success" type="submit"><span class="glyphicon gly-search"></span>查询</button>
                                       	<sec:authorize access="hasAuthority('agentsProfitDetail:tranDeatilExport')">                              
                                       	<button class="btn btn-danger col-sm-offset-14" type="button" onclick="exportExcel()"><span class="glyphicon gly-out"></span>交易明细导出</button>
                                       	</sec:authorize>
                                       	<button class="btn btn-default col-sm-offset-14" type="reset" id="reset"><span class="glyphicon gly-trash"></span>清空</button>
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
					<table id="table_list_agentsProfitDetail"></table>
					<div id="pager_list_agentsProfitDetail"></div>
                    <br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
					</div>
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
	<script type="text/javascript">

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
	/*表单提交时的处理*/
		function exportExcel() {  
			var data = $("#agentsProfitDetailForm").serialize();
			$.download('${ctx}/agentsProfitAction/tranDeatilExport.do',data,'post');
	    }
	
		$("#agentsProfitDetailForm").submit(function() {
			$("#table_list_agentsProfitDetail").setGridParam({
				datatype : 'json',
				page : 1
			//Replace the '1' here
			}).trigger("reloadGrid");
			return false;
		});
		
		function getParams(o) {
			var data = $("#agentsProfitDetailForm").serializeArray();
			$.each(data, function() {
				o[this.name] = this.value || '';
			});
			o.agentNo = $("#agentNo").select2("val");
			console.info($("#agentNo").select2("val"));
		}
		
		function customCheckAccountStatusFormatter(cellvalue, options, rowObject){  
			
			  if(cellvalue == 'SUCCESS'){
				  return '核对成功';
			  }
		 
			  if(cellvalue == 'FAILED'){
				  return '核对有误';
			  }
		 
			  if(cellvalue == 'ACQ_SINGLE'){
				  return '上游单边';
			  }
		 
			  if(cellvalue == 'PLATE_SINGLE'){
				  return '平台单边';
			  }
		 
			  if(cellvalue == 'AMOUNT_FAILED'){
				  return '金额不符';
			  }
		 
			  if(cellvalue == 'NO_CHECKED'){
				  return '未核对';
			  }
		 	
		 return "" ;
	}
		function customCardTypeFormatter(cellvalue, options, rowObject){  
			if(cellvalue == '0'){
				return  "不限";
			}else if(cellvalue =='1'){
				return "贷记卡";
			}else if(cellvalue == '2'){
				return  "借记卡";
			}
		 	
		 return "" ;
		}
		
		function customAgentProfitCollectionStatusFormatter(cellvalue, options, rowObject){  
			if(cellvalue == 'NOCOLLECT'){
				return  "未汇总";
			}else if(cellvalue =='COLLECTED'){
				return "已汇总";
			}
		 	return "" ;
		}
		
		$(document).ready(function() {
			$newOption = $("<option></option>").val("${params.agentNo}").text("${params.agentName}")
			$("#agentNo").append($newOption).trigger('change');
			
			var lastsel;
			//var data = $("#agentsProfitDetailForm").serialize();
			
			// 初始化表格
			$("#table_list_agentsProfitDetail")
					.jqGrid({url : "${ctx}/agentsProfitAction/findAgentsProfitDetailList.do",
								datatype : "local",
								mtype : "POST",
								height:"auto",
								autowidth: true,
								shrinkToFit: false,
								autoScroll: false,
								rowNum: 10,
								rowList: [ 10, 20 ],
								colNames : [ 
									'交易订单号',
									'订单时间',
									'硬件产品种类',
									'业务产品',
									'服务类型',
									'卡类型',
									'商户编号',
									'商户名称',
                                    '一级代理商编号',
                                    '一级代理商名称',
									'代理商编号',
									'代理商名称',
									'代理商级别',
									'交易金额',
									'交易商户扣率',
									'交易商户手续费',
									'抵扣交易商户手续费',
									'实际交易商户手续费',
									'自选商户手续费',
									'抵扣自选商户手续费',
									'实际自选商户手续费',
									'收单机构',
                                    '收单扣率',
									'收单服务成本',
									'交易代理商分润',
									'商户提现手续费',
									'抵扣商户提现手续费',
									'提现代理商分润',
									'出款通道',
									'代付成本',
									'垫资成本',
									'对账状态',
									'代理商分润汇总',
									'汇总批次',
									'汇总时间' ],
								colModel : [
										{
											name : 'plateOrderNo',
											index : 'plateOrderNo',
											width : 200,
											align : "right"
										},
										{
											name : 'transTime',
											index : 'transTime',
											width : 180,
											align : "right",
											formatter : function(val) {
												return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");
											 }
										},
										{
											name : 'hardwareProductName',
											index : 'hardwareProductName',
											width : 150,
											align : "right"
										},
										{
											name : 'businessProductName',
											index : 'businessProductName',
											width : 150,
											align : "right"
										},
										{
											name : 'serviceName',
											index : 'serviceName',
											width : 150,
											align : "right"
										},
										{
											name : 'cardType',
											index : 'cardType',
											width : 100,
											align : "right",
											formatter:customCardTypeFormatter
										},
										{
											name : 'merchantNo',
											index : 'merchantNo',
											width : 200,
											align : "right"
										},
										{
											name : 'merchantName',
											index : 'merchantName',
											width : 180,
											align : "right"
										},
										{
											name : 'oneAgentNo',
											index : 'oneAgentNo',
											width : 100,
											align : "right"
										},
										{
											name : 'oneAgentName',
											index : 'oneAgentName',
											width : 200,
											align : "right"
										},
										{
											name : 'agentNo',
											index : 'agentNo',
											width : 100,
											align : "right"
										},
										{
											name : 'agentName',
											index : 'agentName',
											width : 200,
											align : "right"
										},
										{
											name : 'agentLevel',
											index : 'agentLevel',
											width : 200,
											align : "right"
										},
										{
											name : 'transAmount',
											index : 'transAmount',
											width : 150,
											align : "right",
											formatter : 'number'
										},
										{
											name : 'merchantRate',
											index : 'merchantRate',
											width : 150,
											align : "right"
										},
										{
											name : 'merchantFee',
											index : 'merchantFee',
											width : 200,
											align : "right",
											formatter : 'number'
										},
										{
											name : 'transDeductionFee',
											index : 'transDeductionFee',
											width : 200,
											align : "right",
											formatter : 'number'
										},
										{
											name : 'actualFee',
											index : 'actualFee',
											width : 200,
											align : "right",
											formatter : 'number'
										},
										{
											name : 'merchantPrice',
											index : 'merchantPrice',
											width : 200,
											align : "right",
											formatter : 'number'
										},
										{
											name : 'deductionMerFee',
											index : 'deductionMerFee',
											width : 200,
											align : "right",
											formatter : 'number'
										},
										{
											name : 'actualOptionalFee',
											index : 'actualOptionalFee',
											width : 200,
											align : "right",
											formatter : 'number'
										},
										{
											name : 'acqEnname',
											index : 'acqEnname',
											width : 100,
											align : "right"
										},
										{
											name : 'plateAcqMerchantRate',
											index : 'plateAcqMerchantRate',
											width : 100,
											align : "right"
										},
										{
											name : 'acqOutCost',
											index : 'acqOutCost',
											width : 100,
											align : "right",
											formatter : 'number'
										},
										{
											name : 'agentShareAmount',
											index : 'agentShareAmount',
											width : 150,
											align : "right",
											formatter : 'number'
										},
										{
											name : 'merCashFee',
											index : 'merCashFee',
											width : 150,
											align : "right",
											formatter : 'number'
										},
										{
											name : 'deductionFee',
											index : 'deductionFee',
											width : 150,
											align : "right",
											formatter : 'number'
										},
										{
											name : 'cashAgentShareAmount',
											index : 'cashAgentShareAmount',
											width : 150,
											align : "right",
											formatter : 'number'
										},
										{
											name : 'acqEnname',
											index : 'acqEnname',
											width : 150,
											align : "right"
										},
										{
											name : 'daiCost',
											index : 'daiCost',
											width : 150,
											align : "right",
											formatter : 'number'
										},
										{
											name : 'dianCost',
											index : 'dianCost',
											width : 150,
											align : "right",
											formatter : 'number'
										},
										{
											name : 'checkAccountStatus',
											index : 'checkAccountStatus',
											width : 100,
											align : "right",
											formatter:customCheckAccountStatusFormatter
										},
										{
											name : 'agentProfitCollectionStatus',
											index : 'agentProfitCollectionStatus',
											width : 200,
											align : "right",
											formatter:customAgentProfitCollectionStatusFormatter
										},
										{
											name : 'collectionBatchNo',
											index : 'collectionBatchNo',
											width : 200,
											align : "right"
										},
										{
											name : 'agentProfitGroupTime',
											index : 'agentProfitGroupTime',
											width : 200,
											align : "right",
											formatter : function(val) {
												return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");
											 }
										}
										 ],
								multiselect : false,//支持多项选择
								multiselectWidth : 80,
								multiboxonly: false,
								pager : "#pager_list_agentsProfitDetail",
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
									//hideCheckBox();
								},
							});
					jQuery("#table_list_agentsProfitDetail").jqGrid('setFrozenColumns');
					$(window).bind('resize',function() {
						var width = $('.jqGrid_wrapper').width();
						$('#table_list_agentsProfitDetail').setGridWidth(width);
					});
            
            $('.input-daterange').datepicker({
                format: "yyyy-mm-dd",
                language: "zh-CN",
                todayHighlight: true,
                autoclose: true,
                clearBtn: true
            });
		});
		 $("#reset").on("click", function () { 
	            //$exampleMulti.val(null).trigger("change");
	            $("#collectionBatchNo").val("");
	            $("#collectionBatchNo").attr("value","");
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
		    		  minimumInputLength: 0,
		    		  language: "zh-CN",
		    		  templateResult: formatRepo, // omitted for brevity, see the source of this page
		    		  templateSelection: formatRepoSelection // omitted for brevity, see the source of this page
		    		}
	    		  );	

	</script>
	 </title>
</html>  
      