
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<!-- jqGrid plugin -->
	<link href="${ctx}/css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/jqGrid/ui.jqgrid.css" rel="stylesheet">
	
	<link href="${ctx}/css/plugins/bootstrap-datepicker/bootstrap-datepicker3.min.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10" >
			<div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">报表管理</div>
            <em class=""></em>
            <div class="pull-left active">出款通道阶梯费率返差</div>
		</div>
	</div>
	<!-- 填充内容开始 -->
    
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
				<div class="col-lg-12">
						<div class="ibox ">
							<div class="ibox-content">
								<div class="jqGrid_wrapper">
								<table id="table_list_transFlow"></table>
								<div id="pager_list_transFlow"></div>
								<!-- <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /> -->
								</div>
							</div>
						</div>
				</div>
			
	</div>
	
	<title>
   <script src="${ctx}/js/plugins/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
   <script src="${ctx}/js/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js"></script>
			<script type="text/javascript">
			// 去除空格啊
	        $('input').blur(function(){
	            replaceSpace(this);
	        })
	        function replaceSpace(obj){
	            obj.value = obj.value.replace(/\s/gi,'')
	        }
			function getParams(o){
				var data=$("#transFlowForm").serializeArray();
				
			     $.each(data, function() {   
			             o[this.name] = this.value || '';    
			     });  
			     //o['subjectNo'] = $("#subjectNo").select2("val");
				o['${_csrf.parameterName}'] = '${_csrf.token}';
			}
				 
			$(document).ready(function() {
						var lastsel;
			            // 初始化表格
			            $("#table_list_transFlow").jqGrid({
			            	url:"${ctx}/reportAction/findOutChannelRate.do",
			                datatype: "json",
			                mtype: "POST",
			                height:"auto",
			                autowidth: true,
			                shrinkToFit: false,
			                autoScroll: false,
			                rowNum: 10,
			                rowList: [10, 20],
 			                colNames:['出款通道','出款服务','类型','月份','月累计出款服务总额','该月日均累计出款服务总额','该月出款费用','实际返差金额','记账状态','返差记账'],		               		
 			                colModel: [
 			                   {name:'outAcqEnname',index:'outAcqEnname',width:100,align:"left",sortable:false},
			                   {name: 'outServiceId', index: 'outServiceId',  width: 150,align: "left", sortable:false},
			                   {name: 'reType', index: 'reType',width: 150, align: "left", sortable:false},
			                   {name: 'reMonth', index: 'reDate', width: 100 ,align: "left"},
					           {name: 'totalOutAmountMonth', index: 'totalOutAmountMonth',width: 150, align: "right", sortable:false},
			                   {name: 'totalAvgDayOutAmountMonth', index: 'totalAvgDayOutAmountMonth',width: 160, align: "right", sortable:false},
			                   {name: 'outAmountMonthFee', index: 'outAmountMonthFee',width: 150, align: "right", sortable:false},
			                   {name: 'realRebalance', index: 'realRebalance',width: 150, align: "center", sortable:false,editable: true},
			                   {name: 'recordStatus', index: 'recordStatus',  width: 100,align: "center", formatter: customRecordStatusFmatter},
			                   {name: 'Detail',index:'Id',width:250,align:"center",sortable:false,frozen:true},
			                ],
			                onSelectRow: function(id){
			            		if(id && id!==lastsel){
			            			jQuery('#table_list_transFlow').jqGrid('restoreRow',lastsel);
			            			jQuery('#table_list_transFlow').jqGrid('editRow',id,true);
			            			lastsel=id;
			            		}
			            	},
			            	
			               // multiselect: true,//支持多项选择
			                pager: "#pager_list_transFlow",
			                viewrecords: true,
			                // caption: "账户信息多条列表",
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
                			gridComplete:function(){  //在此事件中循环为每一行添加详情链接
                    			var ids=$("#table_list_transFlow").jqGrid('getDataIDs');
                    			for(var i=0; i<ids.length; i++){
                        			var id=ids[i]; 
                        			var recordStatus = -1;
                        			var getRow = $('#table_list_transFlow').getRowData(id);
                        			if (getRow.recordStatus != null && getRow.recordStatus != '') {
                        				recordStatus = getRow.recordStatus;
                        			} else {
                        				recordStatus = -1;
                        			}
                        			if (recordStatus != '1') {
                        			var detail = '';
                        				detail += "<a href='javascript:void(0);' class='default-details' title='编辑' onclick='Modify(" + id + ")' id='m"+id+"'>编辑</a>";
                        				detail += "&nbsp;<a href='javascript:void(0);' class='default-maintenance'  style='display:none' title='保存'  onclick='Save(" + id + ")' id='s"+id+"'>保存</a>";
                        				detail += "&nbsp;<a href='javascript:void(0);' class='default-delete'  style='display:none' title='取消'  onclick='Cancel(" + id + ")'  id='c"+id+"'>取消</a>&nbsp;";
                        				detail += "<a href='javascript:;' onclick='record("+id+")' class='default-details' title='记账' >记账</a>";
                        			}
                        			jQuery("#table_list_transFlow").jqGrid('setRowData', ids[i], { Detail: detail });
                    			}
                			}
			            });
						jQuery("#table_list_transFlow").jqGrid('setFrozenColumns');
			            // Add responsive to jqGrid
			            $(window).bind('resize', function () {
			                var width = $('.jqGrid_wrapper').width();
			                $('#table_list_transFlow').setGridWidth(width);
			            });

			            $('.input-daterange').datepicker({
			                format: "yyyy-mm-dd",
			                language: "zh-CN",
			                todayHighlight: true,
			                autoclose: true,
			                clearBtn: true
			            });
			            
			});
			
			function customRecordStatusFmatter(cellvalue, options, rowObject){  
				<c:forEach var="recordStatus" items="${recordStatusList}">
				  if(cellvalue == '${recordStatus.sysValue}'){
					  return '${recordStatus.sysName}';
				  }
			 	</c:forEach>	
				return "" ;
			}
			
			function record(id) {
				$.ajax({
					url: "${ctx}/reportAction/outChannelRateRecord.do",
					type: "post",
					dataType: "json",
					data: {"id": id, '${_csrf.parameterName}': '${_csrf.token}'},
					success: function(data) {
						if (data.success) {
							toastr.success(data.msg, "成功");
							$("#table_list_transFlow").setGridParam({
			       				datatype : 'json',
			      				page : 1            //Replace the '1' here
			    			}).trigger("reloadGrid");
						} else {
							toastr.error(data.msg, "错误");
						}
					}
				});
			}
			
			function Modify(id) {   //单击修改链接的操作         
				$("#table_list_transFlow").jqGrid('editRow',id);
				$("#m"+id).hide();
				$("#s"+id+",#c"+id+"").show();
			}
			function Cancel(id) {   //单击修改链接的操作         
				$("#table_list_transFlow").jqGrid('restoreRow',id);
				$("#m"+id).show();
				$("#s"+id+",#c"+id+"").hide();
			}
			function Save(id) {    
				var model = $("#table_list_transFlow").jqGrid('getRowData', id);
				$("#m"+id).show();
				$("#s"+id+",#c"+id+"").hide();
				$("#table_list_transFlow").jqGrid('saveRow',id,{  
	            	keys : true,        //这里按[enter]保存  
	           		url: "${ctx}/reportAction/saveRealRelalance.do",  
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
						}
	            	},
	            	errorfunc: function(rowid, res){  
	                	console.log(rowid);  
	                	console.log(res);  
	            	}  
	        	});
			}
			
		</script>
	</title>
	
   </body>