
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
            <div class="pull-left active">服务商分润汇总</div>
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

							  <label class="col-sm-2 control-label">服务商编号：</label>
							  <div class="col-sm-2"><input type="text" class="form-control" name="merNo" ></div>

							</div>

						 <div class="form-group" >
							 <label class="col-sm-2 control-label">入账状态:</label>
							 <div class="col-sm-2">
								 <select class="form-control" name="incomeStatus" id="enterAccountStatus">
									 <option value="-1" selected="selected">全部</option>
									 <c:forEach var="vf" items="${cardRepaymentStatusList}">
									 <option value="${vf.sysValue}">
									 ${vf.sysName}
									 </option>
									 </c:forEach>
								 </select>
							 </div>

							 <label class="col-sm-2 control-label">分润金额:</label>
							 <div class="col-sm-4 form-inline">
								 <input type="text" class="form-control" name="fenMoney1" onkeyup= "amount(this)"  >-
								 <input type="text" class="form-control" name="fenMoney2" onkeyup= "amount(this)"  >
							 </div>
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

						 <div class="form-group" >
							 <label class="col-sm-2 control-label">交易时间:</label>
							 <div class="col-sm-4">
								 <div class="input-daterange input-group" id="createTime1">
									 <input type="text" class="input-sm form-control" name="TallyTime1"/>
									 <span class="input-group-addon">~</span>
									 <input type="text" class="input-sm form-control" name="TallyTime2"/>
								 </div>
							 </div>

							 <label class="col-sm-2 control-label">是否入账:</label>
							 <div class="col-sm-2">
								 <select class="form-control" name="allowIncome" id="allowIncome">
									 <option value="ALL" selected="selected">全部</option>
									 <option value="1">需要</option>
									 <option value="0">不需要</option>
								 </select>
							 </div>
						 </div>

						 <div class="form-group" style="margin-bottom:0">
                                    <label class="col-sm-2 control-label aaa"></label>
                                    <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
							 		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
							 		<button class="btn btn-success" type="submit"><span class="glyphicon gly-search"></span>查询</button>
							 		<button class="btn btn-default col-sm-offset-14" type="reset" id="reset"><span class="glyphicon gly-trash"></span>清空</button>
									 <sec:authorize access="hasAuthority('serviceShareInAccount:batchEnterAccount')">
										 <button class="btn btn-success" type="button" id="batchEnterAccount" style="margin-left:10px;"><span class="glyphicon"></span>批量入账</button>
									 </sec:authorize>
									 <sec:authorize access="hasAuthority('serviceInAccountList:export')">
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
					<sec:authorize access="hasAuthority('serviceInAccountCollectionDataCount:collectionData')">
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
	
	<div class="modal inmodal" id="myModalEnterAccount" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
        	<div class="modal-content">
            	<div class="modal-header">
                	<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                	<h5 class="modal-title">服务商分润入账</h5>
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
					<button type="button" class="btn btn-success" onclick="saveBatchEnterAccount()">确认</button>
            		<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
         		</div>
  			</div>
		</div>
	</div>
	<!-- 填充内容结束 -->

	<div class="modal inmodal" id="inAccountWarning" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					<h5 class="modal-title">部分数据未能成功入账</h5>
				</div>
				<div class="modal-body"  >
					<div id="warningList"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" onclick="closeWarning()">确认</button>
				</div>
			</div>
		</div>
	</div>

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
			$.download('${ctx}/serviceDuiAccount/exportServiceInAccountList.do',data,'post');
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
			<sec:authorize access="hasAuthority('serviceInAccountCollectionDataCount:collectionData')">
				$.ajax({
	                cache: false,
	                type: "POST",
	                url:"${ctx}/serviceDuiAccount/serviceInAccountCollectionDataCount.do",
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
					.jqGrid({url : "${ctx}/serviceDuiAccount/serviceInAccountList.do",
								datatype : "json",
								mtype : "POST",
								height:"auto",
								autowidth: true,
								shrinkToFit: false,
								autoScroll: false,
								rowNum: 10,
								rowList: [ 10, 20 ],
								// '入账信息',
								colNames : ['序号','汇总编号','汇总批次', '服务商编号','服务商名称', '代理商级别','分润类型','分润', '实际费率','交易时间','创建时间','入账状态','是否需要入账', '入账时间', '操作' ],
								colModel : [
										{ name : 'id', index : 'id', width : 80, align : "center",hidden: true },
										{ name : 'collectionNo', index : 'collectionNo', width : 80, align : "center",hidden: true },
										{ name : 'collectionBatchNo', index : 'collectionBatchNo', width : 150, align : "center" },
										{ name : 'merNo', index : 'merNo', width : 180, align : "center" },
										{ name : 'agentName', index : 'agentName', width : 150, align : "center"},
										{ name : 'agentLevel', index : 'agentLevel', width : 150, align : "center"},

										{ name : 'profitType', index : 'profitType', width : 120, align : "center" ,formatter : function(val) {
											if(val=="1"){
												return "分期还款分润";
											}else if(val=="2"){
												return "全额还款分润";
											}else if(val=="3"){
                                                return "保证金分润";
                                            }else if(val=="4"){
                                                return "完美还款分润";
                                            }
											return "";
										}},

										{ name : 'profitAmount', index : 'profitAmount', width : 120, align : "center" ,formatter: 'number'},
										{ name : 'serviceCostRate', index : 'serviceCostRate', width : 130, align : "center" },
										{ name : 'collectionTime', index : 'collectionTime', width : 140, align : "center" ,formatter : function(val) {return myFormatDate(val,"yyyy-MM-dd");}},
										{ name : 'createTime', index : 'createTime', width : 140, align : "center" ,formatter : function(val) {return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
										{ name : 'incomeStatus', index : 'incomeStatus', width : 120, align : "center",formatter:function(val){
										    if(val==0){
										        return "未入账";
											}else if(val==1){
                                                return "已入账";
											}
										}},
										{ name : 'allowIncome', index : 'allowIncome', width : 120, align : "center",formatter:function(val){
											if(val==0){
												return "不需要";
											}else if(val==1){
												return "需要";
											}
										}},
										{ name : 'incomeTime', index : 'incomeTime', width : 130, align : "center" ,formatter : function(val) {return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
										{ name:'Detail',index:'id',width:200,align:"center",sortable:false }
										 ],
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
				       				    var enterAccountStatus = model.incomeStatus;
				       				    var allowIncome = model.allowIncome;
				       				    var collectionNo = model.collectionNo;
				                        <sec:authorize access="hasAuthority('serviceShareInAccount:batchEnterAccount')">
				                        if (enterAccountStatus =="已入账" || allowIncome == "不需要") {
				    						detail += "&nbsp;&nbsp;<a href='javascripgetRowgetRowt:void(0);'  title='入账' class='default-undetails' style='cursor:not-allowed' disabled='disabled' '>入账</a>";
										} else {
											detail += "&nbsp;&nbsp;<a href='javascript:void(0);'  title='入账' class='default-maintenance' onclick='singleEnterAccount(&apos;" +collectionNo + "&apos;)'>入账</a>";
										}
				                   		</sec:authorize> 
				                        jQuery("#table_list_superPushShareDaySettle").jqGrid('setRowData', ids[i], { Detail: detail });
				                    }
				                    hideCheckBox();
								},
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

		
		//点击批量入账
		$("#batchEnterAccount").click(function() {
            $("#myModalEnterAccount").modal("show");
		});

		 $("#reset").on("click", function () { 
	            //$exampleMulti.val(null).trigger("change");
	            //$('#agentNo').select2("val", "");
	            $("#agentNo").empty().trigger("change");
	        });

		//关闭警告
        function closeWarning(){
            $("#warningList").text('');
			setTimeout(function() {
				location.href='${ctx}/serviceDuiAccount/toServiceInAccountList.do';
			}, 1000);
        }

			/*批量入账*/			
			function saveBatchEnterAccount(){
				$("#wrapper").showLoading();
                var _collectionDate = $("#collectionDate").val();
				$.post('${ctx}/serviceDuiAccount/serviceShareInAccount.do',
						{ 'createDate':_collectionDate,type:1,'${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) { 
							$("#wrapper").hideLoading();
							if(!msg.status){
								//alert(msg.msg);
					            toastr.error(msg.msg,'错误');
					            $("#myModalEnterAccount").modal("hide");
							}else{
								if(msg.data){
                                    toastr.warning('入账成功，但有部分数据未能入账','警告');
                                    $("#warningList").text(msg.data);
                                    $("#inAccountWarning").modal("show");
								}else{
                                    toastr.success(msg.msg,'提示');
                                    setTimeout(function() {
                                        location.href='${ctx}/serviceDuiAccount/toServiceInAccountList.do';
                                    }, 1000);
								}
							}
						}); 
			}
			/*单个入账*/
			function  singleEnterAccount(collectionNo){
				$("#wrapper").showLoading();
                $.post('${ctx}/serviceDuiAccount/serviceShareInAccount.do',
						{ batchNo:collectionNo,type:2,'${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) { 
							$("#wrapper").hideLoading();
							if(!msg.status){
								//alert(msg.msg);
					            toastr.error(msg.msg,'错误');
							}else{
                                if(msg.data){
                                    toastr.warning('入账成功，但有部分数据未能入账','警告');
                                    $("#warningList").text(msg.data);
                                    $("#inAccountWarning").modal("show");
                                }else{
                                    toastr.success(msg.msg,'提示');
                                    setTimeout(function() {
                                        location.href='${ctx}/serviceDuiAccount/toServiceInAccountList.do';
                                    }, 1000);
                                }
							}
						}); 
			}
				
	</script>
	 </title>
</html>  
      