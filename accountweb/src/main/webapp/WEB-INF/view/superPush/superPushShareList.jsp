
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
            <div class="pull-left">微创业分润管理</div>
            <em class=""></em>
            <div class="pull-left active">微创业收益明细</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="agentsProfitDetailForm">
					 
					 
                          <div class="form-group" >
                          	  <label class="col-sm-2 control-label">分润创建时间:</label>
                           		<div class="col-sm-4">
		                            <div class="input-daterange input-group" id="datepicker">
									    <input type="text" class="input-sm form-control" name="createTime1" />
									    <span class="input-group-addon">~</span>
									    <input type="text" class="input-sm form-control" name="createTime2"/>
									</div>   
								</div>
                          	  <label class="col-sm-2 control-label">分润入账时间:</label>
                           		<div class="col-sm-4">
		                            <div class="input-daterange input-group" id="datepicker">
									    <input type="text" class="input-sm form-control" name="shareTime1" />
									    <span class="input-group-addon">~</span>
									    <input type="text" class="input-sm form-control" name="shareTime2"/>
									</div>   
								</div>
							</div> 
							<div class="form-group" > 
							    
								<label class="col-sm-2 control-label">分润级别:</label>
    							   <div class="col-sm-2">
                                        <select class="form-control" name="shareType" id="shareType"> 
                                             <option value="ALL" selected="selected">全部</option>
                                             <option value="0">一级代理商分润</option>
                                             <option value="1">直属代理商分润</option>
                                             <option value="2">上一级商户</option>
                                             <option value="3">上二级商户</option>
                                             <option value="4">上三级商户</option>
                                        </select>      
                                 </div>
								<label class="col-sm-2 control-label">商户/代理商编号：</label>
                                <div class="col-sm-2"><input type="text" class="form-control" name="shareNo" id="shareNo"  ></div>
							</div>	
								
							<div class="form-group" > 
                                <label class="col-sm-2 control-label">汇总状态：</label>
                                 <div class="col-sm-2">
                                        <select class="form-control" name="collectionStatus" id="collectionStatus"> 
                                             <option value="ALL" selected="selected">全部</option>
                                                <option value="NOCOLLECTION">未汇总</option>
                                                <option value="COLLECTIONED">已汇总</option>
                                        </select>      
                                    </div>	
                                
                                <label class="col-sm-2 control-label">交易商户编号：</label>
                                <div class="col-sm-2"><input type="text" class="form-control" name="merchantNo" id="merchantNo" value="${params.merchantNo}"></div>
                                
							</div>	
							
							<div class="form-group" > 
                                
                                 <label class="col-sm-2 control-label">交易订单号：</label>
                                <div class="col-sm-3"><input type="text" class="form-control" name="orderNo" id="orderNo" value="${params.orderNo}"></div>
							
							<label class="col-sm-2 control-label">入账状态:</label>
    							   <div class="col-sm-2">
                                        <select class="form-control" name="shareStatus" id="shareStatus"> 
                                             <option value="ALL" selected="selected">全部</option>
                                             <c:forEach var="vf" items="${superPushShareStatusList}">
                                                <option value="${vf.sysValue}">
                                                    ${vf.sysName}
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
                                       	<sec:authorize access="hasAuthority('superPushShareList:export')">                              
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
				<sec:authorize access="hasAuthority('superPushShareList:collectionData')">  
						<span>统计：累计分润金额：<span id="allShareTotalAmount">0.00</span> 元,&nbsp;&nbsp;已入账：<span id="allAccountedShareTotalAmount">0.00</span>元,&nbsp;&nbsp; 未入账：<span  id="allNoEnterShareTotalAmount">0</span>元,&nbsp;&nbsp; 累计交易金额：<span  id="allTransTotalAmount">0</span>&nbsp;元,&nbsp;&nbsp; 交易笔数：<span  id="allTransTotalNum">0</span>&nbsp;笔 </span>
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
			$.download('${ctx}/superPushAction/exportSuperPushShareList.do',data,'post');
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
		function customShareTypeFormatter(cellvalue, options, rowObject){  
			if(cellvalue == '0'){
				return  "一级代理商分润";
			}else if(cellvalue =='1'){
				return "直属代理商分润";
			}else if(cellvalue == '2'){
				return  "上一级商户";
			}else if(cellvalue == '3'){
				return  "上二级商户";
			}else if(cellvalue == '4'){
				return  "上三级商户";
			}
		 	
		 return "" ;
		}
		
		
		
		
		function customSuperPushCollectionStatusFormatter(cellvalue, options, rowObject){  
			if(cellvalue == 'NOCOLLECTION'){
				return  "未汇总";
			}else if(cellvalue =='COLLECTIONED'){
				return "已汇总";
			}
		 	return "" ;
		}
		
		
		function customSuperPushShareStatus(cellvalue, options, rowObject){  
			<c:forEach var="superPushShareStatus" items="${superPushShareStatusList}">
				  if(cellvalue == '${superPushShareStatus.sysValue}'){
					  return '${superPushShareStatus.sysName}';
				  }
			 </c:forEach>	
			 return "" ;
		}
		
		
		function collection(){
			<sec:authorize access="hasAuthority('superPushShareList:collectionData')"> 
				$.ajax({
	                cache: false,
	                type: "POST",
	                url:"${ctx}/superPushAction/findSuperPushShareListCollection.do",
	                data:$('#agentsProfitDetailForm').serialize(),// formid
	                async: false,
	                success: function(data) {
	                     $("#allShareTotalAmount").html(data.allShareTotalAmount);
	                     $("#allNoEnterShareTotalAmount").html(data.allNoEnterShareTotalAmount);
	                     $("#allAccountedShareTotalAmount").html(data.allAccountedShareTotalAmount);
	                     $("#allTransTotalAmount").html(data.allTransTotalAmount);
	                     $("#allTransTotalNum").html(data.allTransTotalNum);
	                }
	            });
			</sec:authorize>		
		}
		
		$(document).ready(function() {
			var lastsel;
			//var data = $("#agentsProfitDetailForm").serialize();
			
			// 初始化表格
			$("#table_list_agentsProfitDetail")
					.jqGrid({url : "${ctx}/superPushAction/findSuperPushShareList.do",
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
									'分润创建时间','分润金额','分润级别','分润百分比','商户/代理商名称','商户/代理商编号','交易金额',
									'交易订单号','交易商户编号','汇总状态','汇总批次号','入账状态','入账时间' ],
								colModel : [
										{name : 'createTime',index : 'createTime',width : 160,align : "right",formatter : function(val) {
											return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
										{name : 'shareAmount',index : 'shareAmount',width : 120,align : "right",formatter: 'number'},
										{name : 'shareType',index : 'shareType',width : 120,align : "right", formatter:  customShareTypeFormatter},
										{name : 'shareRateStr',index : 'shareRateStr',width : 120,align : "right"},
										{name : 'shareName',index : 'shareName',width : 150,align : "right"},
										{name : 'shareNo',index : 'shareNo',width : 150,align : "right"},
										{name : 'transAmount',index : 'transAmount',width : 120,align : "right",formatter: 'number'},
										{name : 'orderNo',index : 'orderNo',width : 180,align : "right"},
										{name : 'merchantNo',index : 'merchantNo',width : 140,align : "right"},
										//{name : 'checkAccountStatus',index : 'checkAccountStatus',width : 200,align : "right", formatter:  customCheckAccountStatusFormatter},
										{name : 'collectionStatus',index : 'collectionStatus',width : 150,align : "right", formatter:  customSuperPushCollectionStatusFormatter},
										{name : 'collectionBatchNo',index : 'collectionBatchNo',width : 150,align : "right"},
										{name : 'shareStatus',index : 'shareStatus',width : 80,align : "right", formatter:  customSuperPushShareStatus},
										{name : 'shareTime',index : 'shareTime',width : 160,align : "right",formatter : function(val) {
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
      