
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
            <div class="pull-left">微创业分润管理</div>
            <em class=""></em>
            <div class="pull-left active">微创业分润日结报表</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="superPushShareDayForm">
                          <div class="form-group" >
                          	  <label class="col-sm-2 control-label">分润创建日期:</label>
                           		<div class="col-sm-4">
		                            <div class="input-daterange input-group" id="createTime1">
									    <input type="text" class="input-sm form-control" name="createTime1"/>
									    <span class="input-group-addon">~</span>
									    <input type="text" class="input-sm form-control" name="createTime2"/>
									</div>   
								</div>
								<label class="col-sm-2 control-label">汇总时间:</label>
                           		<div class="col-sm-4">
		                            <div class="input-daterange input-group" id="groupTime1">
									    <input type="text" class="input-sm form-control" name="groupTime1" />
									    <span class="input-group-addon">~</span>
									    <input type="text" class="input-sm form-control" name="groupTime2"/>
									</div>   
								</div>
							</div> 
							<div class="form-group" > 
								
								
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
                                    
                                 <label class="col-sm-2 control-label">商户/代理商编号：</label>
                                <div class="col-sm-2"><input type="text" class="form-control" name="shareNo" id="shareNo"  ></div>   
							</div> 
							
							<div class="form-group" > 
								<label class="col-sm-2 control-label">汇总批次号:</label>
		                           <div class="col-sm-3">
		                           <input type="text" class="input-sm form-control" name="collectionBatchNo"/>
								</div>
								<label class="col-sm-2 control-label">用户类别:</label>
    							   <div class="col-sm-2">
                                        <select class="form-control" name="shareType" id="shareType"> 
                                             <option value="ALL" selected="selected">全部</option>
                                             <option value="0">代理商</option>
                                             <option value="1">商户</option>
                                        </select>      
                                 </div>
							</div> 
							
							<div class="form-group">
							<label class="col-sm-2 control-label">分润总金额：</label>
							<div class="col-sm-3">
								<div class=" input-group" id="shareTotalAmount">
									<input type="text" class="form-control"
										name="shareTotalAmount1" id="shareTotalAmount1" onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" 
										value=""/> <span
										class="input-group-addon">~</span> <input type="text"
										class="form-control" name="shareTotalAmount2"
										id="shareTotalAmount2" onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" 
										value=""/>
								</div>
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
                                        <sec:authorize access="hasAuthority('superPushShareDaySettle:collection')">
                                   	   	<button class="btn btn-success" type="button" id="collection"><span class="glyphicon"></span>汇总</button>
                                   	   	</sec:authorize>
                                       	<sec:authorize access="hasAuthority('superPushShareDaySettle:batchEnterAccount')">
                                       	<button class="btn btn-success" type="button" id="batchEnterAccount" style="margin-left:10px;"><span class="glyphicon"></span>批量入账</button> 
                                       	</sec:authorize>
                                       	<sec:authorize access="hasAuthority('superPushShareDaySettle:todayEnterAccount')">
                                       	<button class="btn btn-success" type="button" id="enterAccount" style="margin-left:10px;"><span class="glyphicon"></span>今日汇总全部入账</button> 
                                       	</sec:authorize>
                                       	<sec:authorize access="hasAuthority('superPushShareDaySettle:export')">                    	
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
					<sec:authorize access="hasAuthority('superPushShareDaySettle:collectionData')">  
						<span>汇总：累计分润总金额：<span id="allShareTotalAmount">0.00</span> 元,&nbsp;&nbsp;已入账：<span id="allAccountedShareTotalAmount">0.00</span>元,&nbsp;&nbsp; 未入账：<span  id="allNoEnterShareTotalAmount">0</span> 元</span>
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
                	<h5 class="modal-title">微创业分润汇总</h5>
                </div>
                <div class="modal-body"  >
                	<form method="post" class="form-horizontal" id="collectionForm">
                	
					<div class="form-group">
					<label class="col-sm-2 control-label">交易日期:</label>
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
		<div class="modal inmodal" id="myModalEnterAccount" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
        	<div class="modal-content">
            	<div class="modal-header">
                	<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                	<h5 class="modal-title">微创业分润入账</h5>
                </div>
                <div class="modal-body"  >
                	<form method="post" class="form-horizontal" id="enterAccountForm">
					<div class="form-group">
                           		<div class="col-sm-7">
									本次需要入账金额为<span id="selectEnterAmount"></span>元，共<span id="selectEnterNum"></span>笔 
									<br/>
									入账之前请先确定金额无误，你确定要入账吗？
									<input type="hidden" id="selectEnterId" name="selectEnterId">
								</div>
					</div>
					</form>
         		</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" onclick="saveBatchEnterAccount()">确认</button>
            		<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
         		</div>
  			</div>
		</div>
	</div>
	
	
	<div class="modal inmodal" id="myModalAllEnterAccount" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
        	<div class="modal-content">
            	<div class="modal-header">
                	<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                	<h5 class="modal-title">今日汇总全部入账</h5>
                </div>
                <div class="modal-body"  >
                	<form method="post" class="form-horizontal" id="enterAccountForm">
					<div class="form-group">
                           		<div class="col-sm-7">
									本次需要入账金额为<span id="allEnterAmount"></span>元，共<span id="allEnterNum"></span>笔
									<br/>
									入账之前请先确定金额无误，你确定要入账吗？
								</div>
					</div>
					</form>
         		</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" onclick="saveAllEnterAccount()">确认</button>
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
			var data = $("#superPushShareDayForm").serialize();
			$.download('${ctx}/superPushAction/exportSuperPushShareDaySettleList.do',data,'post');
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
			<sec:authorize access="hasAuthority('superPushShareDaySettle:collectionData')"> 
				$.ajax({
	                cache: false,
	                type: "POST",
	                url:"${ctx}/superPushAction/findSuperPushShareDaySettleListCollection.do",
	                data:$('#superPushShareDayForm').serialize(),// formid
	                async: false,
	                success: function(data) {
	                     $("#allShareTotalAmount").html(data.allShareTotalAmount);
	                     $("#allNoEnterShareTotalAmount").html(data.allNoEnterShareTotalAmount);
	                     $("#allAccountedShareTotalAmount").html(data.allAccountedShareTotalAmount);
	                }
	            });
			</sec:authorize>		
		}
		
		var num = 0;
		function chkNum(num){
	       	 return (num%2 ==0) ?true:false;  //判断是否能整除2
	    }
	    function hideCheckBox(){
	       	 var rowIds = jQuery("#table_list_superPushShareDaySettle").jqGrid('getDataIDs');//获取jqgrid中所有数据行的id
				 for(var k=0; k<rowIds.length; k++) {
				  var curRowData = jQuery("#table_list_superPushShareDaySettle").jqGrid('getRowData', rowIds[k]);//获取指定id所在行的所有数据.
				  if("已入账" == curRowData.enterAccountStatus){
					  var checkboxFailID = "jqg_table_list_superPushShareDaySettle_"+rowIds[k];
					    $("#"+checkboxFailID).hide(); 
				  }
				 }
	       	
	     }
		
		$(document).ready(function() {
			var lastsel;
			//var data = $("#superPushShareDayForm").serialize();
			// 初始化表格
			$("#table_list_superPushShareDaySettle")
					.jqGrid({url : "${ctx}/superPushAction/findSuperPushDaySettleList.do",
								datatype : "json",
								mtype : "POST",
								height:"auto",
								autowidth: true,
								shrinkToFit: false,
								autoScroll: false,
								rowNum: 10,
								rowList: [ 10, 20 ],
								// '入账信息',
								colNames : ['序号', '汇总批次号','汇总时间', '分润创建日期', '用户类别', '商户/代理商名称','商户/代理商编号','分润总金额','分润笔数', '入账状态', '入账时间', '操作' ],
								colModel : [
										{ name : 'id', index : 'id', width : 80, align : "center",hidden: false },
										{ name : 'collectionBatchNo', index : 'collectionBatchNo', width : 150, align : "center" },
										{ name : 'groupTime', index : 'groupTime', width : 180, align : "center" ,formatter : function(val) {return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
										{ name : 'createTime', index : 'createTime', width : 150, align : "center" ,formatter : function(val) {return myFormatDate(val,"yyyy-MM-dd");}},
										{ name : 'shareType', index : 'shareType', width : 120, align : "center" , formatter:customSuperPushShareType},
										{ name : 'shareName', index : 'shareName', width : 130, align : "center" },
										{ name : 'shareNo', index : 'shareNo', width : 140, align : "center" },
										{ name : 'shareTotalAmount', index : 'shareTotalAmount', width : 120, align : "center" ,formatter: 'number'},
										{ name : 'shareTotalNum', index : 'shareTotalNum', width : 130, align : "center" },
										{ name : 'enterAccountStatus', index : 'enterAccountStatus', width : 200, align : "center" , formatter:  customEnterAccountStatus},
										{ name : 'enterAccountTime', index : 'enterAccountTime', width : 150, align : "right" ,formatter : function(val) {
											return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
										//{ name : 'enterAccountMessage', index : 'enterAccountMessage', width : 300, align : "left" },
										{ name:'Detail',index:'id',width:200,align:"center",sortable:false, },
										 ],
								multiselect : true,//支持多项选择
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
								},
								gridComplete : function() { //在此事件中循环为每一行添加修改和删除链接
									var ids=$("#table_list_superPushShareDaySettle").jqGrid('getDataIDs');
				                    for(var i=0; i<ids.length; i++){
				                        var id=ids[i];   
				                        var detail = "" ;
				                        var model = $("#table_list_superPushShareDaySettle").jqGrid('getRowData', id);
				       				    var enterAccountStatus = model.enterAccountStatus;
				                        <sec:authorize access="hasAuthority('superPushShareDaySettle:singleEnterAccount')">                               	   
				                        if (enterAccountStatus =="已入账") {
				    						detail += "&nbsp;&nbsp;<a href='javascripgetRowgetRowt:void(0);'  title='入账' class='default-undetails' style='cursor:not-allowed' disabled='disabled' '>入账</a>";
				    						} else {
				    						detail += "&nbsp;&nbsp;<a href='javascript:void(0);'  title='入账' class='default-maintenance' onclick='singleEnterAccount(" + id + ")'>入账</a>";
				    						}	
				                   		</sec:authorize> 
				                        jQuery("#table_list_superPushShareDaySettle").jqGrid('setRowData', ids[i], { Detail: detail });
				                    }
				                    hideCheckBox();
								},
            					onSelectAll:function(rowid, status) { //点击全选时触发事件
           						 var rowIds = jQuery("#table_list_superPushShareDaySettle").jqGrid('getDataIDs');//获取jqgrid中所有数据行的id
           						 for(var k=0; k<rowIds.length; k++) {
           						   var curRowData = jQuery("#table_list_superPushShareDaySettle").jqGrid('getRowData', rowIds[k]);//获取指定id所在行的所有数据.
           						   if("已入账" == curRowData.enterAccountStatus)
           						      $("#table_list_superPushShareDaySettle").jqGrid("setSelection", rowIds[k],false);//设置改行不能被选中。
           						 }
           						 if(!chkNum(num)){
           							  $("#table_list_superPushShareDaySettle").resetSelection();
           						 }
           						 num ++;
           					   } ,
           					   onSelectRow:function(id){//选择某行时触发事件
           					      var curRowData = jQuery("#table_list_superPushShareDaySettle").jqGrid('getRowData', id);
           					      if("已入账" == curRowData.enterAccountStatus)
           					        $("#table_list_superPushShareDaySettle").jqGrid("setSelection", id,false);
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
		
		//点击批量入账
		$("#batchEnterAccount").click(function() {
			//获取到所有选中的chekbox的值，即选中行的id值.	
			var s = jQuery("#table_list_superPushShareDaySettle").jqGrid('getGridParam', 'selarrrow');
			//alert(s);
			if(s==''){
				toastr.warning('你还没有选择任何内容', '警告');  
			}else{
				$("#myModalEnterAccount").modal("show");
				var ss = s.toString().split(',');
				var arr = new Array();
				var totalAmount = 0;
				for (var i in ss) {
					var curRowData = jQuery("#table_list_superPushShareDaySettle").jqGrid('getRowData', ss[i]);//获取指定id所在行的所有数据.
					totalAmount = totalAmount + parseFloat(curRowData.shareTotalAmount);
				}
				$("#selectEnterAmount").text(totalAmount.toFixed(2));
				$("#selectEnterNum").text(ss.length);
				$("#selectEnterId").val(ss);
				
			}

		});
		
		//今日汇总全部入账判断
		$("#enterAccount").click(function() {
			$.ajax({
                cache: false,
                type: "POST",
                url:"${ctx}/superPushAction/judgeSuperPushShareEnterTodayAccount.do",
                data:$('#superPushShareDayForm').serialize(),// formid
                async: false,
                success: function(data) {
                	$("#myModalAllEnterAccount").modal("show");
    				$("#allEnterAmount").html(data.allEnterAmount);
    				$("#allEnterNum").html(data.allEnterNum);
                }
            });
		});
		//全部入账
		function saveAllEnterAccount(){
			$("#wrapper").showLoading();
			$.post('${ctx}/superPushAction/superPushShareEnterTodayAccount.do', 
					{'${_csrf.parameterName}':'${_csrf.token}' },
					function(msg) { 
						$("#wrapper").hideLoading();
						if(!msg.status){
							//alert(msg.msg);
				            toastr.error(msg.msg,'错误');
				            $("#myModalAllEnterAccount").modal("hide");
						}else{
							toastr.success(msg.msg,'提示');
							setTimeout(function() {
								location.href='${ctx}/superPushAction/toSuperPushShareDaySettle.do';
							}, 1000);
						}
					}); 
		}

		function TransDetail(id) {   //单击修改链接的操作     
			//console.info(id);
	        var model = $("#table_list_superPushShareDaySettle").jqGrid('getRowData', id);
	        var pageNo = $("#table_list_superPushShareDaySettle").jqGrid('getGridParam','page');
	        var pageSize = $("#table_list_superPushShareDaySettle").jqGrid('getGridParam','rowNum');
	        var sortname = $("#table_list_superPushShareDaySettle").jqGrid('getGridParam','sortname');
	        var sortorder = $("#table_list_superPushShareDaySettle").jqGrid('getGridParam','sortorder');
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
	

	      function customSuperPushShareType(cellvalue, options, rowObject){  
				<c:forEach var="superDaySettleType" items="${superDaySettleTypeList}">
					  if(cellvalue == '${superDaySettleType.sysValue}'){
						  return '${superDaySettleType.sysName}';
					  }
				 </c:forEach>	
				 return "" ;
			}
	      function customEnterAccountStatus(cellvalue, options, rowObject){  
				<c:forEach var="enterAccountStatus" items="${enterAccountStatusList}">
					  if(cellvalue == '${enterAccountStatus.sysValue}'){
						  return '${enterAccountStatus.sysName}';
					  }
				 </c:forEach>	
				 return "" ;
			}
	      	function goAgentProfitCollection(_collectionDate){
	      		 $.post('${ctx}/superPushAction/superPushShareCollection.do', 
						{ createDate:_collectionDate,'${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) { 
							$("#wrapper").hideLoading();
							if(!msg.status){
								//alert(msg.msg);
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
								setTimeout(function() {
									location.href='${ctx}/superPushAction/toSuperPushShareDaySettle.do';
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
			
			/*批量入账*/			
			function saveBatchEnterAccount(){
				$("#wrapper").showLoading();
				var _selectEnterId = $("#selectEnterId").val();
				$.post('${ctx}/superPushAction/superPushShareEnterAccount.do', 
						{ 'selectEnterId':_selectEnterId,'${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) { 
							$("#wrapper").hideLoading();
							if(!msg.status){
								//alert(msg.msg);
					            toastr.error(msg.msg,'错误');
					            $("#myModalEnterAccount").modal("hide");
							}else{
								toastr.success(msg.msg,'提示');
								setTimeout(function() {
									location.href='${ctx}/superPushAction/toSuperPushShareDaySettle.do';
								}, 1000);
							}
						}); 
			}
			/*单个入账*/
			function  singleEnterAccount(id){
				$("#wrapper").showLoading();
				 var model = $("#table_list_superPushShareDaySettle").jqGrid('getRowData', id);
				$.post('${ctx}/superPushAction/superPushSingleEnterAccount.do', 
						{ id:id,'${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) { 
							$("#wrapper").hideLoading();
							if(!msg.status){
								//alert(msg.msg);
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
								setTimeout(function() {
									location.href='${ctx}/superPushAction/toSuperPushShareDaySettle.do';
								}, 1000);
							}
						}); 
			}
				
	</script>
	 </title>
</html>  
      