
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<!-- jqGrid plugin -->
	<link href="${ctx}/css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/jqGrid/ui.jqgrid.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">调账管理</div>
            <em class=""></em>
            <div class="pull-left active">调账记录查看</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				
				<div class="ibox-content">
					 <form class="form-horizontal" id="form1">
                          	<div class="form-group">
                          		   <label class="col-sm-2 control-label">调账ID:</label>
                                   <div class="col-sm-2 control-label" style="text-align:left;">${adjustAccount.id}</div>
                          		   <label class="col-sm-2 control-label">调账类型:</label>
									<div class="col-sm-2 control-label" style="text-align:left;">
									         <c:forEach var="adjustAccountType" items="${adjustAccountTypeList}">
												<c:if test="${adjustAccountType.sysValue == adjustAccount.accountType}">${adjustAccountType.sysName}</c:if>
											</c:forEach>
									  </div>
                                   <label class="col-sm-2 control-label">状态:</label>
									<div class="col-sm-2 control-label" style="text-align:left;">
									         <c:forEach var="adjustAccountStatus" items="${adjustAccountStatusList}">
												<c:if test="${adjustAccountStatus.sysValue == adjustAccount.status}">${adjustAccountStatus.sysName}</c:if>
											</c:forEach>
									  </div>									  														  

							</div>
							<div class="form-group"> 
									<label class="col-sm-2 control-label">提交人:</label>
                                   <div class="col-sm-2 control-label" style="text-align:left;">${adjustAccount.applicant}</div>
                                   <label class="col-sm-2 control-label" >提交时间:</label>
									<div class="col-sm-2 control-label" style="text-align:left;">
									<fmt:formatDate value="${adjustAccount.applicantTime}" pattern="yyyy-MM-dd HH:mm:ss" type="both"/>
									 </div> 
							</div>
							
							<div class="form-group">    
									                   	
                                    <label class="col-sm-2 control-label">审核人:</label>
                                   <div class="col-sm-2 control-label" style="text-align:left;">${adjustAccount.approver}</div>
									 <label class="col-sm-2 control-label">审核时间:</label>
									<div class="col-sm-2 control-label" style="text-align:left;">
									 <fmt:formatDate value="${adjustAccount.approveTime}" pattern="yyyy-MM-dd HH:mm:ss" type="both"/>
									 </div>
							</div>
						 	<div class="form-group">
	                          		   <label class="col-sm-2 control-label">备注:</label>
	                                   <div class="col-sm-2 control-label" style="text-align:left;">${adjustAccount.remark}</div>
	                                   
							</div>
							 <div class="form-group">
	                          		   <label class="col-sm-2 control-label">记账失败原因:</label>
	                                   <div class="col-sm-2 control-label" style="text-align:left;">${adjustAccount.recordFailRemark}</div>
	                                   
							</div>
							<div class="clearfix lastbottom"></div>
							<div class="form-group">
								<label class="col-sm-2 control-label aaa"></label>
                         		<button id="returnUp" type="button" class=" btn btn-default" onclick="window.location.href='${ctx}/adjustAction/adjustRecordListQuery.do'" value="" /><span class="glyphicon gly-return"></span>返回</button>
                         	</div>
                          
                                <!-- <div class="clearfix"></div> -->
                                
                    </form>
				</div>
			</div>
		</div>
		<div class="col-lg-12">
						<div class="ibox ">
							<div class="ibox-content">
								<div class="jqGrid_wrapper">
								<table id="table_list_adjust_detail"></table>
								<div id="pager_list_adjust_detail"></div>
								</div>
							</div>
						</div>
				</div>
			
	</div>
	
   </body>
    
	<title>
	<script type="text/javascript">
		function Detail(id) {   //单击修改链接的操作         
	        var model = $("#table_list_subject").jqGrid('getRowData', id);
			var subjectNo = model.subjectNo;
	        //alert(model.subjectNo);
	        location.href='${ctx}/subjectAction/subjectDetail.do?subjectNo='+subjectNo;
	        
	    }
		function customBalanceFromFmatter(cellvalue, options, rowObject){  
			<c:forEach var="balanceFrom" items="${balanceFromList}">
				  if(cellvalue == '${balanceFrom.sysValue}'){
					  return '${balanceFrom.sysName}';
				  }
			 </c:forEach>	
			 return "" ;
		} 
		function customAccountFlagFmatter(cellvalue, options, rowObject){  
			<c:forEach var="accountFlag" items="${accountFlagList}">
				  if(cellvalue == '${accountFlag.sysValue}'){
					  return '${accountFlag.sysName}';
				  }
			 </c:forEach>	
			 return "" ;
		} 
		function customAccountTypeFmatter(cellvalue, options, rowObject) {
			<c:forEach var="accountType" items="${accountTypeList}">
				  if(cellvalue == '${accountType.sysValue}'){
					  return '${accountType.sysName}';
				  }
			 </c:forEach>	
			 return "" ;
		}
		function customOrgInfoFmatter(cellvalue, options, rowObject) {
			<c:forEach var="orgInfo" items="${orgInfoList}">
				  if(cellvalue == '${orgInfo.orgNo}'){
					  return '${orgInfo.orgName}';
				  }
			 </c:forEach>	
			 return "" ;
		}
		function customCurrencyFmatter(cellvalue, options, rowObject) {
			<c:forEach var="currency" items="${currencyList}">
				  if(cellvalue == '${currency.currencyNo}'){
					  return '${currency.currencyName}';
				  }
			 </c:forEach>	
			 return "" ;
		}
		
		function getParams(o){
			var data=$("#form1").serializeArray();
		     $.each(data, function() {    
		             o[this.name] = this.value || '';    
		     });   
		     o.adjustId = ${adjustAccount.id};
		}
		$("#form1").submit(function(){
			$("#table_list_adjust_detail").trigger("reloadGrid");
			return false;
		});
		$(document).ready(function() {
					var lastsel;
		            // 初始化表格
		            $("#table_list_adjust_detail").jqGrid({
		            	url:"${ctx}/adjustAction/findAdjustDetail.do",
		                datatype: "json",
		                mtype: "POST",
		                height:"auto",
		                autowidth: true,
		                shrinkToFit:false,
		                autoScroll: false,
		                rowNum: 14,
		                rowList: [10, 20],
		                colNames:['序号','分录号','子交易流水号','内部账/外部帐','账号','外部用户类型', '外部用户（商户、代理商、收单机构）编号', '账户归属', '科目编号', '币种号','卡号（预付卡号，若没有，则为空）', '借贷标识', '金额','备注'],
		                colModel: [
		                    {name: 'transNo', index: 'id', width: 100, sorttype: "int", align: "center"},
		                    {name: 'journalNo', index: 'journalNo', width: 100, align: "center", sortable:false},
		                    {name: 'childTransNo', index: 'childTransNo', width: 100, align: "center", sortable:false},
		                    {name: 'accountFlag', index: 'approve_time',width: 120, align: "center", sortable:false, formatter:  customAccountFlagFmatter},
		                    {name: 'account', index: 'applicant', width: 250, align: "center", sortable:false},
		                    
		                    {name: 'accountType', index: 'applicant', width: 100, align: "center", sortable:false, formatter: customAccountTypeFmatter},
		                    {name: 'userId', index: 'applicant', width: 350, align: "center", sortable:false},
		                    {name: 'accountOwner', index: 'applicant', width: 90, align: "center", sortable:false, formatter: customOrgInfoFmatter},
		                    {name: 'subjectNo', index: 'applicant', width: 250, align: "center", sortable:false},
		                    {name: 'currencyNo', index: 'applicant', width: 90, align: "center", sortable:false, formatter: customCurrencyFmatter},
		                    {name: 'cardNo', index: 'applicant', width: 250, align: "center", sortable:false},
		                    
		                    {name: 'amountFrom', index: 'applicant_time', editable:true, width: 100, align: "center", sortable:false, formatter:  customBalanceFromFmatter},
		                    {name: 'amount', index: 'approver',width: 100, align: "center",formatter: 'number'},
		                    {name: 'remark', index: 'account_type',width: 300, align: "center", sortable:false},
		                ],
		                onSelectRow: function(id){
		            		if(id && id!==lastsel){
		            			jQuery('#table_list_user').jqGrid('restoreRow',lastsel);
		            			jQuery('#table_list_user').jqGrid('editRow',id,true);
		            			lastsel=id;
		            		}
		            	},
		                multiselect: false,//支持多项选择
		                pager: "#pager_list_adjust_detail",
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
		                }
		            });
		            // Add responsive to jqGrid
		            $(window).bind('resize', function () {
		                var width = $('.jqGrid_wrapper').width();
		                $('#table_list_adjust_detail').setGridWidth(width);
		            });
		});
		

	</script>
	
	 </title>
</html>  
      