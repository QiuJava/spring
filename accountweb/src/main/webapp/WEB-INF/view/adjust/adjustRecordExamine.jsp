<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<script src="${ctx}/js/My97DatePicker/WdatePicker.js"></script>
	<!-- jqGrid plugin -->
	<link href="${ctx}/css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/jqGrid/ui.jqgrid.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading " >
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">调账管理</div>
            <em class=""></em>
            <div class="pull-left active">提交审核</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="adjustExamineForm">
                          <div class="form-group">
                          	 <label class="col-sm-2 control-label">调账ID：</label>
                          	 <input  type="hidden" value="${adjustAccount.id}" name="id">
                             <div class="col-sm-2 form-control-static"  name="id" value="${adjustAccount.id}">${adjustAccount.id}</div>      			
							<label class="col-sm-2 control-label">调账类型：</label>
                            <div class="col-sm-2 form-control-static">
								<c:forEach var="adjustAccountType" items="${adjustAccountTypeList}">
									<c:if test="${adjustAccountType.sysValue == adjustAccount.accountType}">${adjustAccountType.sysName}</c:if>
								</c:forEach>
							</div>
                            <div class="col-sm-0">
                            <input  type="hidden" value="${adjustAccount.status}" name="status">
                             
										</div>
                            <label class="col-sm-2 control-label">状态：</label>
                            <div class="col-sm-2 form-control-static">
                            	<c:forEach var="adjustAccountStatus" items="${adjustAccountStatusList}">
									<c:if test="${adjustAccountStatus.sysValue == adjustAccount.status}">${adjustAccountStatus.sysName}</c:if>
								</c:forEach>
							</div>
                  		   
                         </div>
                         <div class="form-group">
                             <label class="col-sm-2 control-label">提交人：</label>
                           <div class="col-sm-2 form-control-static">${adjustAccount.applicant}</div>
                           <label class="col-sm-2 control-label">申请时间：</label>
                           <div class="col-sm-2 form-control-static">
                           <fmt:formatDate value="${adjustAccount.applicantTime}" pattern="yyyy-MM-dd HH:mm:ss" type="both"/>
                           </div> 
                         </div>
                          <div class="form-group">
                                   
                                                              		   
						     

                  		   <label class="col-sm-2 control-label">审核人：</label>
                           <div class="col-sm-2 form-control-static">${adjustAccount.approver}</div>
								 
                        </div>
                        
                         <div class="form-group">
                         <label class="col-sm-2 control-label">备注：</label>
                            <div class="col-sm-2 form-control-static">${adjustAccount.remark}</div>
						</div>
                                    
						 <div class="form-group">     		  
                                   <label class="col-sm-2 control-label">审核意见：</label>
									<div class="col-sm-2 form-control-static">${adjustAccount.approveRemark}</div>	  
						</div>
						 <div class="form-group " style="margin-top: 20px;">
                             <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                <label class="col-sm-2 control-label aaa"></label>
                             	 <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                             	 <sec:authorize access="hasAuthority('adjustRecordListQuery:submitExamine')">
                                 	<button class="btn btn-success" type="button" id="approveBt"><span class="glyphicon gly-ok"></span>提交</button>
                                 </sec:authorize>
                                 <button id="returnUp" type="button" class=" btn btn-default col-sm-offset-14" onclick="window.location.href='${ctx}/adjustAction/adjustRecordListQuery.do'" value="" /><span class="glyphicon gly-return"></span>返回</button>
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
								<table id="table_list_adjustExamine"></table>
								<div id="pager_list_adjustExamine"></div>
								</div>
							</div>
						</div>
				</div>
				
	</div>
	
   </body>
   	<title>
   			<script src="${ctx}/js/plugins/sweetalert/sweetalert.min.js"></script>
   			<script type="text/javascript">
   			$("#approveBt").click(function(){
   				var data=$("#adjustExamineForm").serialize();
   				$.ajax({
   					url:"${ctx}/adjustAction/updateRecordExamine.do",
   					type:"POST",
   					data:data,
   					success :function(msg){
   						if(msg.state){
   							swal({title:"提示" ,text:msg.msg ,animation:"slide-from-top"}, function() {window.location.href='${ctx}/adjustAction/adjustRecordListQuery.do';});
   						}else{
   							swal({title:"提示" ,text:msg.msg ,animation:"slide-from-top"});
   						}
   					}
   				});
   			});
   			
   			
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
			/* function getParams(o){
				var data=$("#adjustExamineForm").serializeArray();
				
			     $.each(data, function() {   
			             o[this.name] = this.value || '';    
			     });  
			     
			} */
			function getParams(o){
				var data=$("#adjustExamineForm").serializeArray();
			     $.each(data, function() {    
			             o[this.name] = this.value || '';    
			     });   
			     o.adjustId = ${adjustAccount.id};
			}
   			
   					$(document).ready(function() {
						var lastsel;
			            // 初始化表格
			            $("#table_list_adjustExamine").jqGrid({
			            	url:"${ctx}/adjustAction/findAdjustDetail.do",
			                datatype: "json",
			                mtype: "POST",
			                height:"auto",
			                autowidth: true,
			                shrinkToFit:false,
			                autoScroll: false,
			                rowNum: 10,
			                rowList: [10, 20],
			                colNames:['序号','分录号','子交易流水号','内部账/外部帐','账号','外部用户类型', '外部用户（商户、代理商、收单机构）编号', '账户归属', '科目编号', '币种号','卡号（预付卡号，若没有，则为空）', '借贷方向', '金额','备注'],
			                colModel: [
			                    {name: 'transNo', index: 'transNo', width: 100, sorttype: "int", align: "center"},
		                        {name: 'journalNo', index: 'journalNo', width: 100, align: "center", sortable:false},
		                        {name: 'childTransNo', index: 'childTransNo', width: 100, align: "center", sortable:false},
			                    {name: 'accountFlag', index: 'accountFlag',width: 120, align: "center", sortable:false, formatter:  customAccountFlagFmatter},
			                    {name: 'account', index: 'account', width: 250, align: "center", sortable:false},
			                    
			                    {name: 'accountType', index: 'accountType', width: 100, align: "center", sortable:false, formatter: customAccountTypeFmatter},
			                    {name: 'userId', index: 'userId', width: 350, align: "center", sortable:false},
			                    {name: 'accountOwner', index: 'accountOwner', width: 90, align: "center", sortable:false, formatter:  customOrgInfoFmatter},
			                    {name: 'subjectNo', index: 'subjectNo', width: 250, align: "center", sortable:false},
			                    {name: 'currencyNo', index: 'currentyNo', width: 90, align: "center", sortable:false, formatter:  customCurrencyFmatter},
			                    {name: 'cardNo', index: 'cardNo', width: 250, align: "center", sortable:false},
			                    
			                    {name: 'amountFrom', index: 'amountFrom', editable:true, width: 100, align: "center", sortable:false, formatter:  customBalanceFromFmatter},
			                    {name: 'amount', index: 'amount',width: 100, align: "center"},
			                    {name: 'remark', index: 'remark',width: 300, align: "center", sortable:false},
			                ],
			                onSelectRow: function(id){
			            		if(id && id!==lastsel){
			            			jQuery('#table_list_adjustExamine').jqGrid('restoreRow',lastsel);
			            			jQuery('#table_list_adjustExamine').jqGrid('editRow',id,true);
			            			lastsel=id;
			            		}
			            	},
			               
			                pager: "#pager_list_adjustExamine",
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
			                    var ids=$("#table_list_adjustExamine").jqGrid('getDataIDs');
			                    for(var i=0; i<ids.length; i++){
			                        var id=ids[i];   
			                        var detail = "<a href='javascript:void(0);' class='default-details' title='审核' onclick='Detail(" + id + ")'>审核</a>";
			                        jQuery("#table_list_adjustExamine").jqGrid('setRowData', ids[i], { Detail: detail });
			                    }
			                },
			            
			            });
			            // Add responsive to jqGrid
			            $(window).bind('resize', function () {
			                var width = $('.jqGrid_wrapper').width();
			                $('#table_list_adjustExamine').setGridWidth(width);
			            });
			});
			

		</script>
   	
   	
   	</title>