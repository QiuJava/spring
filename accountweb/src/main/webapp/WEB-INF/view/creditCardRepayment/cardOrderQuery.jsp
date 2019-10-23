
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
            <div class="pull-left">综合服务对账</div>
            <em class=""></em>
            <div class="pull-left active">信用卡还款订单查询</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="agentsProfitDetailForm">

						 <div class="form-group" >
							 <label class="col-sm-2 control-label">订单ID：</label>
							 <div class="col-sm-2"><input type="text" class="form-control" name="batchNo" id="orderNo"></div>

							 <label class="col-sm-1 control-label">还款人ID：</label>
							 <div class="col-sm-2"><input type="text" class="form-control" name="merchantNo" id="repaymentPersonId"></div>

							 <label class="col-sm-1 control-label">手机号：</label>
							 <div class="col-sm-2"><input type="text" class="form-control" name="mobileNo" id="phoneNo"></div>

						 </div>

						 <div class="form-group" >
							 <label class="col-sm-2 control-label">任务金额:</label>
							 <div class="col-sm-4 form-inline">
								 <input type="text" class="input-sm form-control" name="repayAmount1" />-
								 <input type="text" class="input-sm form-control" name="repayAmount2"/>
							 </div>

							 <label class="col-sm-2 control-label">保证金:</label>
							 <div class="col-sm-4 form-inline">
								 <input type="text" class="input-sm form-control" name="ensureAmount1" />-
								 <input type="text" class="input-sm form-control" name="ensureAmount2"/>
							 </div>


						 </div>
						 <div class="form-group" >
							 <label class="col-sm-2 control-label">服务费:</label>
							 <div class="col-sm-4 form-inline">
								 <input type="text" class="input-sm form-control" name="repayFee1" />-
								 <input type="text" class="input-sm form-control" name="repayFee2"/>
							 </div>

							 <label class="col-sm-2 control-label">入账状态:</label>
							 <div class="col-sm-2">
								 <select class="form-control" name="ruStatus" id="shareStatus">
									 <option value="-1" selected="selected">全部</option>
									 <c:forEach var="vf" items="${cardRepaymentStatusList}">
									 <option value="${vf.sysValue}">
									 ${vf.sysName}
									 </option>
									 </c:forEach>
								 </select>
							 </div>
						 </div>

						 <div class="form-group" >
							 <label class="col-sm-2 control-label">入账时间:</label>
							 <div class="col-sm-4">
								 <div class="input-daterange input-group" id="datepicker">
									 <input type="text" class="input-sm form-control" name="tallyTime1" />
									 <span class="input-group-addon">~</span>
									 <input type="text" class="input-sm form-control" name="tallyTime2"/>
								 </div>
							 </div>
						 </div>

						<div class="clearfix lastbottom"></div>
						<div class="form-group" style="margin-bottom:0">
								<label class="col-sm-2 control-label aaa"></label>
							   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
								   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
									<button class="btn btn-success" type="submit"><span class="glyphicon gly-search"></span>查询</button>
									<sec:authorize access="hasAuthority('cardOrderList:export')">
									<button class="btn btn-danger col-sm-offset-14" type="button" onclick="exportExcel()"><span class="glyphicon gly-out"></span>导出</button>
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
					<sec:authorize access="hasAuthority('cardOrderCollectionDataCount:count')">
						<span>保证金入账：<span id="ruAccountSuccess">0.00</span> 元,&nbsp;&nbsp;保证金未入账：<span id="ruAccountWait">0.00</span>元</span>
						<br/>
						<br/>
					</sec:authorize>
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
			$.download('${ctx}/serviceDuiAccount/exportCardOrderList.do',data,'post');
	    }
	
		$("#agentsProfitDetailForm").submit(function() {
			$("#table_list_agentsProfitDetail").setGridParam({
				datatype : 'json',
				page : 1
			//Replace the '1' here
			}).trigger("reloadGrid");
			//汇总数据
			collection();
			return false;
		});
		
		function getParams(o) {
			var data = $("#agentsProfitDetailForm").serializeArray();
			$.each(data, function() {
				o[this.name] = this.value || '';
			});
		}
		
		function ruStatus(cellvalue, options, rowObject){
			if(cellvalue==0){
				return "未记账";
			}else if(cellvalue==1){
				return "发起记账失败";
			}else if(cellvalue==2){
				return "记账成功";
            }else if(cellvalue==3){
				return "记账失败";
            }
            return "";
		}
		
		
		function collection(){
			<sec:authorize access="hasAuthority('cardOrderCollectionDataCount:count')">
				$.ajax({
	                cache: false,
	                type: "POST",
	                url:"${ctx}/serviceDuiAccount/cardOrderCollectionDataCount.do",
	                data:$('#agentsProfitDetailForm').serialize(),// formid
	                async: false,
	                success: function(data) {
                        $("#ruAccountSuccess").html(data.mapSuccess);
                        $("#ruAccountWait").html(data.mapWait);
	                }
	            });
			</sec:authorize>		
		}
		
		$(document).ready(function() {
			var lastsel;
			//var data = $("#agentsProfitDetailForm").serialize();
			
			// 初始化表格
			$("#table_list_agentsProfitDetail")
					.jqGrid({url : "${ctx}/serviceDuiAccount/findCardOrderList.do",
								datatype : "json",
								mtype : "POST",
								height:"auto",
								autowidth: true,
								shrinkToFit: false,
								autoScroll: false,
								rowNum: 10,
								rowList: [ 10, 20 ],
								//'对账状态',
								colNames : [ 
									'订单ID','还款人ID','姓名','手机号','任务金额','保证金',
									'服务费','汇总时间','汇总批次','入账状态','入账时间'],
								colModel : [
										{name : 'batchNo',index : 'batchNo',width : 160,align : "right"},
										{name : 'merchantNo',index : 'merchantNo',width : 120,align : "right"},
										{name : 'userName',index : 'userName',width : 120,align : "right"},
										{name : 'mobileNo',index : 'mobileNo',width : 120,align : "right"},
										{name : 'repayAmount',index : 'repayAmount',width : 150,align : "right",formatter: 'number'},
										{name : 'ensureAmount',index : 'ensureAmount',width : 150,align : "right",formatter: 'number'},
										{name : 'repayFee',index : 'repayFee',width : 120,align : "right",formatter: 'number'},
										{name : 'countTime',index : 'countTime',width : 180,align : "right",formatter : function(val) {
                                            return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
										{name : 'serviceOrderNo',index : 'serviceOrderNo',width : 150,align : "right"},
										{name : 'ruStatus',index : 'ruStatus',width : 80,align : "right", formatter:  ruStatus},
										{name : 'tallyTime',index : 'tallyTime',width : 160,align : "right",formatter : function(val) {
											return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
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
            
            collection();
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

	</script>
	 </title>
</html>  
      