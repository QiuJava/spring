
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%
   request.setCharacterEncoding("utf-8");
   response.setCharacterEncoding("utf-8");
%>  
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
            <div class="pull-left active">超级盟主分润明细报表</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="peragentForm" >
                          <div class="form-group">
							<label class="col-sm-2 control-label">入账时间：</label>
                           		<div class="col-sm-4">
		                            <div class="input-daterange input-group" id="accTime1">
									    <input type="text" class="input-sm form-control" name="accTime1"/>
									    <span class="input-group-addon">~</span>
									    <input type="text" class="input-sm form-control" name="accTime2"/>
									</div>   
								</div>
								<label class="col-sm-2 control-label">分润创建时间：</label>
                           		<div class="col-sm-4">
		                            <div class="input-daterange input-group" id="createTime1">
									    <input type="text" class="input-sm form-control" name="createTime1" />
									    <span class="input-group-addon">~</span>
									    <input type="text" class="input-sm form-control" name="createTime2"/>
									</div>   
								</div>
							</div>
                          
                          <div class="form-group" >
                          
                              <label class="col-sm-2 control-label">用户名称：</label>
                                <div class="col-sm-2"><input type="text" class="form-control" name="realName" id="realName"  ></div>  
                               <label class="col-sm-2 control-label">用户类别：</label>
    							   <div class="col-sm-2">
                                         <select class="form-control" name="userType" id="userType"> 
                                             <option value="ALL" selected="selected">全部</option>
                                             <c:forEach var="vf" items="${userTypeList}">
                                                <option value="${vf.sysValue}">
                                                    ${vf.sysName}
                                                </option>
                                             </c:forEach>
                                        </select>           
                                 </div>
                          	   <label class="col-sm-2 control-label">用户编号：</label>
                                <div class="col-sm-2"><input type="text" class="form-control" name="userCode" id="userCode"  ></div>  
							</div> 
							<div class="form-group" > 
								
								 <label class="col-sm-2 control-label">分润类别：</label>
    							   <div class="col-sm-2">
                                        <select class="form-control" name="shareType" id="shareType"> 
                                             <option value="ALL" selected="selected">全部</option>
                                             <c:forEach var="vf" items="${shareTypeList}">
                                                <option value="${vf.sysValue}">
                                                    ${vf.sysName}
                                                </option>
                                             </c:forEach>
                                        </select>         
                                 </div>
								<label class="col-sm-2 control-label">入账状态：</label>
    							<div class="col-sm-2">
                                        <select class="form-control" name="accStatus" id="accStatus"> 
                                             <option value="ALL" selected="selected">全部</option>
                                             <c:forEach var="vf" items="${enterAccountStatusList}">
                                                <option value="${vf.sysValue}">
                                                    ${vf.sysName}
                                                </option>
                                             </c:forEach>
                                        </select>      
                                    </div>	
                                    
                                  <label class="col-sm-2 control-label">所属品牌：</label>
    							   <div class="col-sm-2">
                                         <select class="form-control" name="brandCode" id="brandCode"> 
                                             <option value="ALL" selected="selected">全部</option>
                                             <c:forEach var="vf" items="${brandTypeList}">
                                                <option value="${vf.brandCode}">
                                                    ${vf.brandName}
                                                </option>
                                             </c:forEach>
                                        </select>          
                                 </div> 
							</div> 
							
							<div class="form-group" > 
								
								<label class="col-sm-2 control-label">所属大盟主：</label>
                                <div class="col-sm-2"><input type="text" class="form-control" name="twoUserCode" id="twoUserCode"  ></div>  
								<label class="col-sm-2 control-label">所属机构：</label>
                                <div class="col-sm-2"><input type="text" class="form-control" name="oneUserCode" id="oneUserCode"  ></div>  
							</div> 
							
							
							
							
                            <div class="clearfix lastbottom"></div>
                            
                            <div class="form-group" style="margin-bottom:0">
                                    <label class="col-sm-2 control-label aaa"></label>
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                   	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                       	<button class="btn btn-success" type="submit"><span class="glyphicon gly-search"></span>查询</button>
                                       	<button class="btn btn-default col-sm-offset-14" type="reset" id="reset"><span class="glyphicon gly-trash"></span>清空</button>
                                       	<sec:authorize access="hasAuthority('peragentAtion:batchEnterAccount')">
                                       	<button class="btn btn-success" type="button" id="batchEnterAccount" style="margin-left:10px;"><span class="glyphicon"></span>月结奖金入账</button> 
                                       	</sec:authorize>
                                       	<sec:authorize access="hasAuthority('peragentAtion:export')">                    	
                                       	<button class="btn btn-danger col-sm-offset-14" type="button" onclick="exportExcel()"><span class="glyphicon gly-out"></span>导出</button>
                                       	</sec:authorize>
                             </div>
                           
                    </form>
				</div>
			</div>
		</div>
		
		<div class="col-lg-12">
			<div class="ibox ">
				<div class="ibox-content">
					<sec:authorize access="hasAuthority('peragentAtion:collectionData')">  
						<span>总计：总分润总金额：<span id="allShareTotalAmount">0.00</span> 元,&nbsp;&nbsp;已入账：<span id="allAccountedShareTotalAmount">0.00</span>元,&nbsp;&nbsp; 未入账：<span  id="allNoEnterShareTotalAmount">0</span> 元</span>
						<br/>
						<br/>
					</sec:authorize>		
					<div class="jqGrid_wrapper">
					<table id="table_list_peragent"></table>
					<div id="pager_list_peragent"></div>
                    <br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
					</div>
				</div>
			</div>
		</div>
		
	</div>	
	
	<div class="modal inmodal" id="myModalBatchEnter" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
        	<div class="modal-content">
            	<div class="modal-header">
                	<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                	<h5 class="modal-title">批量入账</h5>
                </div>
                <div class="modal-body"  >
                	<form method="post" class="form-horizontal" id="collectionForm">
                	
					<div class="form-group">
					<label class="col-sm-2 control-label">入账月份:</label>
                           		<div class="col-sm-4">
		                            <div class="input-daterange input-group" id="bacthDatepicker">
									    <input type="text" class="input-sm form-control" name="accountDate" id="accountDate"/>
									</div>   
								</div>
					</div>
					</form>
         		</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" onclick="comfirmBacthAccount()">确认</button>
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
                	<h5 class="modal-title">超级盟主分润入账</h5>
                </div>
                <div class="modal-body"  >
                	<form method="post" class="form-horizontal" id="enterAccountForm">
					<div class="form-group">
                           		<div class="col-sm-7">
									本次需要入账金额为<span id="enterAmount"></span>元
									<br/>
									入账之前请先确定金额无误，你确定要入账吗？
									<input type="hidden" id="accountMonth" name="accountMonth">
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
			var data = $("#peragentForm").serialize();
			$.download('${ctx}/peragentAtion/peragentExport.do',data,'post');
	    }
	
		$("#peragentForm").submit(function() {
			$("#table_list_peragent").setGridParam({
				datatype : 'json',
				page : 1
			//Replace the '1' here
			}).trigger("reloadGrid");
			//汇总
			collection();
			return false;
		});
		
		function getParams(o) {
			var data = $("#peragentForm").serializeArray();
			$.each(data, function() {
				o[this.name] = this.value || '';
			});
		}
		
		function collection(){
			<sec:authorize access="hasAuthority('peragentAtion:collectionData')"> 
				$.ajax({
	                cache: false,
	                type: "POST",
	                url:"${ctx}/peragentAtion/findPeragentListCollection.do",
	                data:$('#peragentForm').serialize(),// formid
	                async: false,
	                success: function(data) {
	                     $("#allShareTotalAmount").html(data.allShareTotalAmount);
	                     $("#allNoEnterShareTotalAmount").html(data.allNoEnterShareTotalAmount);
	                     $("#allAccountedShareTotalAmount").html(data.allAccountedShareTotalAmount);
	                }
	            });
			</sec:authorize>		
		}

		
		$(document).ready(function() {
			var lastsel;
			// 初始化表格
			$("#table_list_peragent")
					.jqGrid({url : "${ctx}/peragentAtion/findPeragentList.do",
								datatype : "json",
								mtype : "POST",
								height:"auto",
								autowidth: true,
								shrinkToFit: false,
								autoScroll: false,
								rowNum: 10,
								rowList: [ 10, 20 ],
								colNames : ['序号', '分润金额','交易/结算金额', '交易订单号','结算订单号','分润类别', '当月团队总流水（元）', '当月直营商户总流水（元）', '用户类别','用户名称','用户编号','交易分润等级','交易分润比例','荣耀奖金分润比例','所属品牌','所属大盟主编号','所属机构编号','分润创建时间', '入账状态', '入账时间', '入账信息', '操作人', '操作' ],
								colModel : [
										{ name : 'id', index : 'id', width : 100, align : "center",hidden: false },
										{ name : 'shareAmount', index : 'shareAmount', width : 150, align : "center" ,formatter: 'number' },
										{ name : 'transAmountStr', index : 'transAmountStr', width : 150, align : "center"  },
										{ name : 'transNo', index : 'transNo', width : 210, align : "center" , },
										{ name : 'setterNo', index : 'setterNo', width : 230, align : "center" , },
										{ name : 'shareType', index : 'shareType', width : 120, align : "center" , formatter:  customShareType},
										{ name : 'teamTotalAmount', index : 'teamTotalAmount', width : 200, align : "center" ,formatter: 'number'},
										{ name : 'totalAmount', index : 'totalAmount', width : 200, align : "center" ,formatter: 'number'},
										{ name : 'userType', index : 'userType', width : 110, align : "center" , formatter:  customUserType},
										{ name : 'realName', index : 'realName', width : 140, align : "center" },
										{ name : 'userCode', index : 'userCode', width : 140, align : "center" },
										{ name : 'shareLevelStr', index : 'shareLevelStr', width : 140, align : "center" },
										{ name : 'shareRatioStr', index : 'shareRatioStr', width : 140, align : "center" },
										{ name : 'honourShareRatioStr', index : 'honourShareRatioStr', width : 140, align : "center" },
										{ name : 'brandCode', index : 'brandCode', width : 120, align : "center" , formatter:  customBrandCode},
										{ name : 'twoUserCode', index : 'twoUserCode', width : 140, align : "center" },
										{ name : 'oneUserCode', index : 'oneUserCode', width : 140, align : "center" },
										{ name : 'createTime', index : 'createTime', width : 200, align : "center",formatter : function(val) {
											return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");} },
										{ name : 'accStatus', index : 'accStatus', width : 120, align : "center" , formatter:  customEnterAccountStatus},
										{ name : 'accTime', index : 'accTime', width : 200, align : "center" ,formatter : function(val) {
											return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
										{ name : 'accMessage', index : 'accMessage', width : 300, align : "left" },
										{ name : 'accOperator', index : 'accOperator', width : 120, align : "center" },
										{ name:  'Detail',index:'id',width:120,align:"center",sortable:false, },
										 ],
								multiselect : false,//支持多项选择
								multiboxonly: false,
								pager : "#pager_list_peragent",
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
									var ids=$("#table_list_peragent").jqGrid('getDataIDs');
				                    for(var i=0; i<ids.length; i++){
				                        var id=ids[i];   
				                        var detail = "" ;
				                        var model = $("#table_list_peragent").jqGrid('getRowData', id);
				       				    var accStatus = model.accStatus;
				       				    var shareType = model.shareType;
				                        <sec:authorize access="hasAuthority('peragentAtion:singleEnterAccount')">                               	   
				                        if (accStatus == "已入账" || shareType == "固定收益" || shareType == "交易分润" || shareType == "机具分润" ) {
				    						detail += "&nbsp;&nbsp;<a href='javascripgetRowgetRowt:void(0);'  title='入账' class='default-undetails' style='cursor:not-allowed' disabled='disabled' '>入账</a>";
				    						} else {
				    						detail += "&nbsp;&nbsp;<a href='javascript:void(0);'  title='入账' class='default-maintenance' onclick='singleEnterAccount(" + id + ")'>入账</a>";
				    						}	
				                   		</sec:authorize> 
				                        jQuery("#table_list_peragent").jqGrid('setRowData', ids[i], { Detail: detail });
				                    }
								}
							});
			
					jQuery("#table_list_peragent").jqGrid('setFrozenColumns');
					$(window).bind('resize',function() {
						var width = $('.jqGrid_wrapper').width();
						$('#table_list_peragent').setGridWidth(width);
					});

			$('#createTime1').datepicker({
				format : "yyyy-mm-dd",
				language : "zh-CN",
				todayHighlight : true,
				autoclose : true,
				clearBtn : true
			});
			$('#accTime1').datepicker({
				format : "yyyy-mm-dd",
				language : "zh-CN",
				todayHighlight : true,
				autoclose : true,
				clearBtn : true
			});
			
			$('#bacthDatepicker').datepicker({
				language: "zh-CN",
		          todayHighlight: true,
		          format: 'yyyy-mm',
		          autoclose: true,
		          startView: 'months',
		          maxViewMode:'years',
		          minViewMode:'months'
			});
			
            //汇总
			collection();
		});
		//点击批量入账
		$("#batchEnterAccount").click(function() {
			$("#myModalBatchEnter").modal("show");
		});

	    /*入账前校验*/			
		function comfirmBacthAccount(){
			var _collectionDate = $("#accountDate").val();
			$("#myModalEnterAccount").modal("show");
			$("#accountMonth").val(_collectionDate);
			comfirmBacthAccountAjax(_collectionDate);
		}
		function comfirmBacthAccountAjax(_collectionDate){
			$.post('${ctx}/peragentAtion/comfirmBacthAccount.do', 
					{accountMonth:_collectionDate,'${_csrf.parameterName}':'${_csrf.token}' },
					function(msg) { 
						$("#enterAmount").html(msg.enterAmount);
					}); 
		}
		//批量入账
		function saveBatchEnterAccount(){
			var accountMonth = $("#accountMonth").val();
			$("#wrapper").showLoading();
			$.post('${ctx}/peragentAtion/bacthAccount.do', 
					{accountMonth:accountMonth,'${_csrf.parameterName}':'${_csrf.token}' },
					function(msg) { 
						$("#myModalEnterAccount").modal("hide");
			            $("#myModalBatchEnter").modal("hide");
						$("#wrapper").hideLoading();
						if(!msg.status){
				            toastr.error(msg.msg,'错误');
						}else{
							toastr.success(msg.msg,'提示');
							setTimeout(function() {
								location.href='${ctx}/peragentAtion/toPeragent.do';
							}, 1000);
						}
					}); 
		}

		 $("#reset").on("click", function () { 
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
	

	      function customShareType(cellvalue, options, rowObject){  
				<c:forEach var="shareType" items="${shareTypeList}">
					  if(cellvalue == '${shareType.sysValue}'){
						  return '${shareType.sysName}';
					  }
				 </c:forEach>	
				 return "" ;
			}
	      function customUserType(cellvalue, options, rowObject){  
				<c:forEach var="userType" items="${userTypeList}">
					  if(cellvalue == '${userType.sysValue}'){
						  return '${userType.sysName}';
					  }
				 </c:forEach>	
				 return "" ;
			}
	      function customBrandCode(cellvalue, options, rowObject){  
				<c:forEach var="paBrand" items="${brandTypeList}">
					  if(cellvalue == '${paBrand.brandCode}'){
						  return '${paBrand.brandName}';
					  }
				 </c:forEach>	
				 return "" ;
			}
	      function customEnterAccountStatus(cellvalue, options, rowObject){  
				<c:forEach var="accStatus" items="${enterAccountStatusList}">
					  if(cellvalue == '${accStatus.sysValue}'){
						  return '${accStatus.sysName}';
					  }
				 </c:forEach>	
				 return "" ;
			}

			/*单个入账*/
			function  singleEnterAccount(id){
				 var model = $("#table_list_peragent").jqGrid('getRowData', id);
				 //$("#wrapper").showLoading();
				 swal({  
					 	title: "确认是否入账?", 
					   	type: "warning",  
					    showCancelButton: true,   
					    cancelButtonText: "取消",  
					   	confirmButtonColor: "#DD6B55",  
					    confirmButtonText: "继续入账",  
					    closeOnConfirm: false 
					    }, 
					    function(){
					    	$.post('${ctx}/peragentAtion/singleEnterAccount.do', 
						    { id:id,'${_csrf.parameterName}':'${_csrf.token}' },
						    function(msg) { 
							//$("#wrapper").hideLoading();
							if(!msg.status){
								//alert(msg.msg);
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
								setTimeout(function() {
									location.href='${ctx}/peragentAtion/toPeragent.do';
								}, 1000);
							}
							swal.close();
						 }); 
					});
			}
				
	</script>
	 </title>
</html>  
      