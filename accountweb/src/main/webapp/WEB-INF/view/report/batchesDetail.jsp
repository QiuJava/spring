<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<!-- jqGrid plugin -->
	<link href="${ctx}/css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/jqGrid/ui.jqgrid.css" rel="stylesheet">
	
	<link href="${ctx}/css/plugins/bootstrap-datepicker/bootstrap-datepicker3.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
	<style>
		.table td {
			text-align:center;
		}
	</style>
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10" >
			<div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">报表管理</div>
            <em class=""></em>
            <div class="pull-left active">批处理详情</div>
		</div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox-content">
				<br/>
				<label class="col-sm-0 control-label">批处理详情</label>
				<table class="table table-bordered" style="width:100;max-width:100%">
					<thead>
						<tr>
					    	<td width="100">子任务 ID</td>
					    	<td width="100">子任务序号</td>
					        <td width="200">子任务名称</td>
					        <td width="150">运行结果</td>
					        <td width="150">操作时间</td>
					        <td width="200">日志</td>
					        <td width="200">操作</td>
						</tr>
					</thead>
					<tbody id="tabody">
						<c:forEach items="${batchesDetailList }" var="item">
						<tr>
					    	<td>${item.id }</td>
					        <td>${item.stepId }</td>
					        <td>${item.stepName }</td>
					        <td>
					        	<c:forEach var="va" items="${statusDicts}">
					        		<c:if test="${va.sysValue==item.status}">
					        			${va.sysName}
					        		</c:if>
					        	</c:forEach>
					        </td>
					        <td><fmt:formatDate value="${item.executeTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					        <td>
					        	<c:if test="${item.status != 0}">
					        		<a href="javascript:void(0)" class="default-details" style="width:100px;" onclick="viewLog(${item.id})"  data-toggle='modal' data-target='#myModalViewLog' >点击查看</a>
					        	</c:if>
					        </td>
					        <td>
					        	<c:if test="${item.status==0 || item.status==2}">
					        		<a href="javascript:void(0)" class="default-details" style="width:100px;" onclick="manualSetSuccess(${item.id })">人工处理成功</a>
					        		<a href="javascript:void(0)" class="default-details" style="width:100px;" onclick="manualExec(${item.id },${item.stepId})">运行</a>
					        	</c:if>
					        </td>
                            
						</tr>
						</c:forEach>
                        <tr>
                            <td style="border: 1px solid #fff;">
                                <div class="col-sm-12" style="margin-top: 10px;">
                                <button id="returnUp" type="button" class=" btn btn-default" onclick="goBack();" value="" /><span class="glyphicon gly-return"></span>返回</button>
                            </div>
                        </td>
                        </tr>
                        
					</tbody>
				</table>
				
				<div class="clearfix lastbottom"></div>
                                
        		
			</div>
		</div>
	</div>
	
	<div class="modal inmodal" id="myModalViewLog" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
        	<div class="modal-content">
            	<div class="modal-header">
                	<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                	<h5 class="modal-title">日志查看</h5>
                </div>
                <div class="modal-body">
					<div class="form-group">
						<pre id="execLog"></pre>
					</div>
         		</div>
				<div class="modal-footer">
            		<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
         		</div>
  			</div>
		</div>
	</div>
	
	<title>
	<script src="${ctx}/js/plugins/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
	<script src="${ctx}/js/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js"></script>
	<script src="${ctx}/js/plugins/sweetalert/sweetalert.min.js"></script>
	
	<script>
		function viewLog(id) {
			$.ajax({
				type: "post",
				url: "${ctx}/reportAction/getBatchesDetailLog.do",
				data: {id:id,'${_csrf.parameterName}':'${_csrf.token}'},
				dataType: "json",
				success: function(data) {
					if (data.state) {
						$("#execLog").html(data.msg);
					} else {
						toastr.error(data.msg, "错误");
					}
				}
			});
		}
		
		function manualSetSuccess(id) {
			swal({  
			 		title: "确认人工处理成功，将子任务状态置为成功？", 
			   		text: "",   
			   		type: "warning",  
			    	showCancelButton: true,   
			    	cancelButtonText: "取消",  
			   		confirmButtonColor: "#DD6B55",  
			    	confirmButtonText: "确定",  
			    	closeOnConfirm: false 
			    }, 
			    function(){   
			    	$.post('${ctx}/reportAction/manualSetSuccess.do', 
						{ '${_csrf.parameterName}':'${_csrf.token}', id:id },
						function(msg) {
							if(!msg.state){
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
								location.reload();
							}
							swal.close();
						});
			    });
		}
		
		function manualExec(id, stepId) {
		
		}
		
		$(function(){
			//reflushTable();
		});
		
		function reflushTable() {
			$.ajax({
				type: "post",
				url: "${ctx}/reportAction/findBatchesDetailList.do",
				data: {'${_csrf.parameterName}':'${_csrf.token}', batchesId:'${batchesId}'},
				dataType: "html",
				function: function(data) {
					$("#tabody").html(data.data);
				}
			});
		}
		
		function goBack() {
			location.href = "${ctx}/reportAction/toBatches.do?queryParams=${params.queryParams}";
		}
		
		/**
		$(document).ready(function() {
			var lastsel;
            // 初始化表格
            $("#table_list_batches_detail").jqGrid({
            	url:"${ctx}/reportAction/findBatchesDetailList.do",
                datatype: "json",	
                mtype: "POST",
                height: 'auto',
                autowidth: true,
                shrinkToFit:false,
                autoScroll: false,
                rowNum: 10,
                rowList: [10, 20],
                colNames:['子任务 ID','子任务序号','子任务名称','运行结果','日志', '操作' ],
                colModel: [
                    {name: 'id', index: 'id', width: 120, align: "center"},
                    {name: 'stepId', index: 'stepId', width: 120, align: "center"},
                    {name: 'name', index: 'name', width: 120, align: "center"},
                    {name: 'status', index: 'status',width: 150, align: "center", formatter: customStatusFormatter},
                    {name: '', index: 'status',width: 110, align: "center", formatter: customStatusFormatter},
                    {name:'Detail',index:'id',width:210,align:"center",sortable:false,frozen:true},
                ],
                multiselect: false,//支持多项选择
                viewrecords: true,
                hidegrid: false,
                gridComplete:function(){  //在此事件中循环为每一行添加修改和删除链接
                    var ids=$("#table_list_batches_detail").jqGrid('getDataIDs');
                    for(var i=0; i<ids.length; i++){
                        var id=ids[i];
                        var getRow = $('#table_list_batches_detail').getRowData(id);
                        var detail = "<a href='${ctx}/reportAction/toBatchesJob.do?batchesId="+ id +"' class='default-details' title='详情'>详情</a>" ;
                        jQuery("#table_list_batches_detail").jqGrid('setRowData', ids[i], { Detail: detail });
                    }
                }
            });
            jQuery("#table_list_batches").jqGrid('setFrozenColumns');
            // Add responsive to jqGrid
            $(window).bind('resize', function () {
                var width = $('.jqGrid_wrapper').width();
                $('#table_list_batches_detail').setGridWidth(width);
            });
		});
		
		*/
	</script>
	</title>
</body>
</html>