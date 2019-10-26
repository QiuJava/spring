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
    <link rel="stylesheet" href="${ctx}/css/icheck-css/custom.css" />
    <link rel="stylesheet" href="${ctx}/css/skins/all.css" />
	 <!-- Sweet Alert -->
    <link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">调账管理</div>
            <em class=""></em>
            <div class="pull-left active">审核</div>
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
                          		   <label class="col-sm-2 control-label">调账ID:</label>
                                   <div class="col-sm-2 " name="id" value="${adjustAccount.id}" style="font-size:12px;padding-top:7px;">${adjustAccount.id}</div>
                                   <label class="col-sm-2 control-label">状态:</label>
                                   <div class="col-sm-2" style="font-size:12px;padding-top:7px;">
                                   	<c:forEach var="adjustAccountStatus" items="${adjustAccountStatusList}">
										<c:if test="${adjustAccountStatus.sysValue == adjustAccount.status}">${adjustAccountStatus.sysName}</c:if>
									</c:forEach>
                                   </div>
                          		   
                        
                        </div>
                        <div class="form-group">
                                    <label class="col-sm-2 control-label">提交人:</label>
                                   <div class="col-sm-2" style="font-size:12px;padding-top:7px;">${adjustAccount.applicant}</div>
                                   <label class="col-sm-2 control-label">提交时间:</label>
                                   
                                   <div class="col-sm-2" style="font-size:12px;padding-top:7px;">                                              <fmt:formatDate value="${adjustAccount.applicantTime}" pattern="yyyy-MM-dd HH:mm:ss" type="both"/>
                                    </div>
                                                                                                                          
                                    </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">备注:</label>
                                    <div class="col-sm-2" style="font-size:12px;padding-top:7px;">${adjustAccount.remark}
                                                       
                                      </div>
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
				
				<div class="col-lg-12 ">
						<div class="ibox-content" style="">
						 <form class="form-horizontal" id="approveStatusFrom" >
						 
				 	<div class="form-group">
				 		<input name= "id"  type="hidden"  value="${adjustAccount.id}"/>
				 		<label class="col-sm-2 control-label">审核意见：</label>
                                <div class="col-sm-10" id="examine-agree">
                                	<label class="radio-inline">
										<input type="radio" name="status" id="inlineRadio1" value="2"> 同意
									</label>
									<label class="radio-inline" style="font-weight:bold;">
										<input type="radio" name="status" id="inlineRadio2" value="3" checked="checked"> 不同意
									</label>
								</div>
					</div>
				    <div class="clearfix"></div>
				
					<div class="form-group lastbottom">                          		 
                                  		<label class="col-sm-2 control-label"  for="approveRemark" style="">审核说明：</label>
                                  		<div class="col-sm-2">
                                  		<textarea class="form-control"  style="width: 300px"  name="approveRemark" id="approveRemark"></textarea>                        		
                                  		</div>   
                            </div>
                                  <div class="form-group " style="margin-top: 20px;">
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                        <label class="col-sm-2 control-label aaa"></label>

                                   		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                        <sec:authorize access="hasAuthority('adjustExamine:examine')">
                                        	<button class="btn btn-success" type="button" id="approveBt"><span class="glyphicon gly-ok"></span>提交</button>
                                 		</sec:authorize>
                                 		<button id="returnUp" type="button" class=" btn btn-default col-sm-offset-14" onclick="window.location.href='${ctx}/adjustAction/adjustExamine.do'" value="" /><span class="glyphicon gly-return"></span>返回</button>
                                   <!-- </div> -->
                              	   </div>
                                   <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
                             </form>
				</div>
			</div>
	</div>
	
   </body>
   	<title>
            <script src="${ctx}/js/icheck.min.js"></script>
            <script src="${ctx}/js/custom.min.js"></script>
			<!-- Sweet alert -->
			<script src="${ctx}/js/plugins/sweetalert/sweetalert.min.js"></script>
            <script type="text/javascript">
                $(document).ready(function(){
                      $('#examine-agree input').iCheck({
                        // checkboxClass: 'icheckbox_square-green',
                        radioClass: 'iradio_square-orange',
                        increaseArea: '20%'
                      });
                 });
            </script>
   			<script>
   			
   			
   			
   			$("#approveBt").click(function(){
   				var data=$("#approveStatusFrom").serialize();
   				$.ajax({
   					url:"${ctx}/adjustAction/updateAdjustExamine.do",
   					type:"POST",
   					data:data,
   					success :function(msg){
   						if(msg.state){
   							swal({title:"提示" ,text:msg.msg ,animation:"slide-from-top"}, function() {window.location.href='${ctx}/adjustAction/adjustExamine.do';});
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
			}   	
   			
   			
   			
   			function customAccountFlagFmatter(cellvalue, options, rowObject){  
				<c:forEach var="AccountFalg" items="${sysAccountFalgList}">
					  if(cellvalue == '${AccountFalg.sysValue}'){
						  return '${AccountFalg.sysName}';
					  }
				 </c:forEach>	
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
			                shrinkToFit: true,
			                rowNum: 10,
			                rowList: [10, 20],
			                colNames:['交易序号','分录号','子交易流水号','账号','借贷标识','金额','内部账外部账标志','备注'],
			                colModel: [
			                    {name: 'transNo', index: 'transNo', width: 100, sorttype: "int", align: "center"},
		                        {name: 'journalNo', index: 'journalNo', width: 100, align: "center", sortable:false},
		                        {name: 'childTransNo', index: 'childTransNo', width: 100, align: "center", sortable:false},
			                    {name: 'account', index: 'account', width: 90,align:"center", sortable:false},
			                    {name: 'amountFrom', index: 'amountFrom',  width: 100,align:"center", sortable:false,formatter: customBalanceFromFmatter},
			                    {name: 'amount', index: 'amount',width: 80, align:"center",formatter: 'number'},
			                    {name: 'accountFlag', index: 'accountFlag',width: 80, align:"center", sortable:false,formatter: customAccountFlagFmatter},
			                    {name: 'remark', index: 'remark',width: 80, align:"center", sortable:false },
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
			                // caption: "调账信息多条列表",
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