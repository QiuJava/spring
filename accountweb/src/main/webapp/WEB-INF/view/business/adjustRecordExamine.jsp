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
                                 <sec:authorize access="hasAuthority('businessRecordListQuery:submitExamine')">
                                    <button class="btn btn-success" type="button" id="approveBt"><span class="glyphicon gly-ok"></span>提交</button>
                                 </sec:authorize>
                                 <button id="returnUp" type="button" class=" btn btn-default col-sm-offset-14" onclick="window.location.href='${ctx}/business/businessRecordListQuery.do'" value="" /><span class="glyphicon gly-return"></span>返回</button>
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
   					url:"${ctx}/business/updateRecordExamine.do",
   					type:"POST",
   					data:data,
   					success :function(msg){
   						if(msg.state){
   							swal({title:"提示" ,text:msg.msg ,animation:"slide-from-top"}, function() {window.location.href='${ctx}/business/businessRecordListQuery.do';});
   						}else{
   							swal({title:"提示" ,text:msg.msg ,animation:"slide-from-top"});
   						}
   					}
   				});
   			});
			function getParams(o){
				var data=$("#adjustExamineForm").serializeArray();
			     $.each(data, function() {    
			             o[this.name] = this.value || '';    
			     });   
			     o.businessId = ${adjustAccount.id};
			}
   			
   					$(document).ready(function() {
						var lastsel;
			            // 初始化表格
			            $("#table_list_adjustExamine").jqGrid({
			            	url:"${ctx}/business/findBusinessDetail.do",
			                datatype: "json",
			                mtype: "POST",
			                height:"auto",
			                autowidth: true,
			                shrinkToFit:false,
			                autoScroll: false,
			                rowNum: 10,
			                rowList: [10, 20],
			                colNames:['交易序号','调出外部用户（商户、代理商、收单机构）编号', '调入外部用户（商户、代理商、收单机构）编号', '金额','调账类型','调账原因'],
			                colModel: [
			                    {name: 'transNo', index: 'transNo', width: 100, sorttype: "int", align: "center"},
		                   		{name: 'outUserNo', index: 'outUserNo', width: 250, align: "center"},
		                    	{name: 'inUserNo', index: 'inUserNo', width: 250, align: "center"},
		                    	{name: 'amount', index: 'amount',width: 200, align: "right"},
		                    	{name: 'accountType', index: 'accountType',width: 200, align: "center"},
		                    	{name: 'reason', index: 'reason',width: 300, align: "center"},
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