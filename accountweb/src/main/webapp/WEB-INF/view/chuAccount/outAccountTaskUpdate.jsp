
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<!-- jqGrid plugin -->
	<link href="${ctx}/css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/jqGrid/ui.jqgrid.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/bootstrapTour/bootstrap-tour.min.css" rel="stylesheet">
	<link href="${ctx}/css/outAccountTaskUpdate.css" rel="stylesheet">
	
	<link href="${ctx}/css/plugins/select2/select2.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/select2/select2-skins.min.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">出账管理</div>
            <em class=""></em>
            <div class="pull-left active">出账任务修改</div>
		</div>
	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">

			<div class="col-lg-12">
				<div class="ibox float-e-margins">
					<form class="form-horizontal" id="outAccountTaskUpdateForm">
							<div class="ibox-content">
								<!-- 出账任务 -->
								<!--边框表格布局: table table-bordered \悬停表格 : table table-hover-->					 
								<table class="table table-bordered" id="taskTab" style="width:100;max-width:100%">
								   <thead>
								      <tr style="text-align: center;">
								         <td width="200">出账任务 ID</td>
								         <td width="200">交易日期</td>
								         <td width="200">总交易额（万）</td>
								         <td width="200">上游结算中金额</td>
								         <td width="200">出账任务金额</td>
								         <td width="200">上游个数</td>
								         <td width="200">出账单ID</td>
								         <td width="200">系统计算时间</td>
								      </tr>
								   </thead>
								   <tbody>
								      <tr>
								         <td>${outAccountTask.id }</td>
								         <td><fmt:formatDate value='${outAccountTask.transTime }' pattern='yyyy-MM-dd HH:mm:ss'/></td>
								         <td style="text-align: right;">${outAccountTask.transAmount }</td>
								         <td style="text-align: right;">${outAccountTask.upBalance }</td>
								         <td style="text-align: right;" id="newAmount">${outAccountTask.outAccountTaskAmount }</td>
								         <td>${outAccountTask.upCompanyCount }</td>
								         <td>${outAccountTask.outAccountId }</td>
								         <td><fmt:formatDate value='${outAccountTask.sysTime }' pattern='yyyy-MM-dd HH:mm:ss'/></td>
								      </tr>
								   </tbody>
								</table>
							</div>
							<input type="hidden" name="outAccountTaskId" value="${outAccountTask.id}"/>
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
						</form> 
				</div>
				

				</div>
				<!-- 出账任务详细 -->
					<div class="col-lg-12">
						<div class="ibox ">
							<div class="ibox-content">
								<div class="jqGrid_wrapper">
									<table id="table_list_outAccountTaskUpdate"></table>
									<div id="pager_list_outAccountTaskUpdate"></div>
									<div class="col-sm-0" style="margin-top:30px">
										<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                        <button id="returnUp" type="button" class=" btn btn-default" onclick="window.location.href='${ctx}/chuAccountAction/toChuAccountTasksManage.do?queryParams=${params.queryParams}'" value="" /><span class="glyphicon gly-return"></span>返回</button>
	            					</div>
								</div>
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
	<script type="text/javascript">

		function Modify(id) {   //单击修改链接的操作         
			$("#table_list_outAccountTaskUpdate").jqGrid('editRow',id);
			$("#m"+id).hide();
			$("#s"+id+",#c"+id+"").show();
			//$("#s"+id+",#c"+id+"").attr("disabled",true);
		}
		function Cancel(id) {   //单击修改链接的操作         
			$("#table_list_outAccountTaskUpdate").jqGrid('restoreRow',id);
			$("#m"+id).show();
			$("#s"+id+",#c"+id+"").hide();
		}
		function Save(id) {    
			var model = $("#table_list_outAccountTaskUpdate").jqGrid('getRowData', id);
			$("#m"+id).show();
			$("#s"+id+",#c"+id+"").hide();
			$("#table_list_outAccountTaskUpdate").jqGrid('saveRow',id,{  
	            keys : true,        //这里按[enter]保存  
	            url: "${ctx}/chuAccountAction/saveOutAccountTaskUpdate.do",  
	            mtype : "POST",  
	            restoreAfterError: true,  
	            extraparam: {  
	                "id": model.id,
	                "${_csrf.parameterName}":"${_csrf.token}"
	            },  
	            oneditfunc: function(rowid){  
	                console.log(rowid);  
	            },  
	            succesfunc: function(response){  
	                alert("save success");  
	                return true;  
	            },
	            aftersavefunc: function (id, response, options) {
	            	var msg = response.responseJSON;
	            	if(!msg.status){
						toastr.error(msg.msg,'错误');
					}else{
						toastr.success(msg.msg,'提示');
						$("#newAmount").text(msg.newAmount);
					}
	            },
	            errorfunc: function(rowid, res){  
	                console.log(rowid);  
	                console.log(res);  
	            }  
	        });
		}
		function customAcqOrgFmatter(cellvalue, options, rowObject){  
			<c:forEach var="acqOrg" items="${acqOrgList}">
				  if(cellvalue == '${acqOrg.sysValue}'){
					  return '${acqOrg.sysName}';
				  }
			 </c:forEach>	
		} 
		
		function getParams(o){
			var data=$("#outAccountTaskUpdateForm").serializeArray();
		     $.each(data, function() {    
		             o[this.name] = this.value || '';    
		     });
		}
		function getAccountStatusGroups(id) {  
		    var arr = []
		    <c:forEach var="accountStatus" items="${accountStatusList}">
		    arr.push('${accountStatus.sysValue}:${accountStatus.sysName}');
			 </c:forEach>	
		    return arr.join(';');  
		}
		$(document).ready(function() {
			var lastsel;
            // 初始化表格
            $("#table_list_outAccountTaskUpdate").jqGrid({
            	url:"${ctx}/chuAccountAction/findOutAccountTaskUpdateList.do",
                datatype: "json",
                mtype: "POST",
                height:"auto",
                autowidth: true,
                shrinkToFit:false,
                autoScroll: false,
                rowNum: 14,
                rowList: [10, 20],
                colNames:['出账任务明细ID','出账任务ID','上游','剩余上游结算中金额','出账金额','操作'],
                colModel: [
                    {name: 'id', index: 'id', width: 220, sorttype: "int", align: "left"},
                    {name: 'outAccountTaskId', index: 'outAccountTaskId', width: 200, align: "left"},
                    {name: 'acqOrgNo', index: 'acqOrgNo', width: 200, align: "left"},
                    {name: 'upBalance', index: 'upBalance',width: 200, align: "right",formatter: 'number'},
                    {name: 'outAccountAmount', index: 'outAccountAmount',width: 280, align: "right",editable: true,formatter: 'number'},
                    {name:'Detail',index:'detail',width:280,align:"center",sortable:false},
      
                ],
                onSelectRow: function(id){
            		if(id && id!==lastsel){
            			jQuery('#table_list_outAccountTaskUpdate').jqGrid('restoreRow',lastsel);
            			jQuery('#table_list_outAccountTaskUpdate').jqGrid('editRow',id,true);
            			lastsel=id;
            			$("#m"+id).hide();
        				$("#s"+id+",#c"+id+"").show();
            		}
            	},
                multiselect: false,//支持多项选择
                pager: "#pager_list_outAccountTaskUpdate",
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
                    var ids=$("#table_list_outAccountTaskUpdate").jqGrid('getDataIDs');
                    for(var i=0; i<ids.length; i++){
                        var id=ids[i];   
                        var detail = "<a href='javascript:void(0);' class='default-details' title='编辑' onclick='Modify(" + id + ")' id='m"+id+"'>编辑</a>";
                        detail += "&nbsp;<a href='javascript:void(0);' class='default-maintenance'  style='display:none' title='保存'  onclick='Save(" + id + ")' id='s"+id+"'>保存</a>";
                        detail += "&nbsp;<a href='javascript:void(0);' class='default-delete'  style='display:none' title='取消'  onclick='Cancel(" + id + ")'  id='c"+id+"'>取消</a>";
                        jQuery("#table_list_outAccountTaskUpdate").jqGrid('setRowData', ids[i], { Detail: detail });
                    }
                }
            });
            // Add responsive to jqGrid
            $(window).bind('resize', function () {
                var width = $('.jqGrid_wrapper').width();
                $('#table_list_outAccountTaskUpdate').setGridWidth(width);
            });
		});

	</script>
	 </title>
</html>  
      