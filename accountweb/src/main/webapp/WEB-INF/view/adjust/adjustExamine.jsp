<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
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
	
	<link href="${ctx}/css/plugins/bootstrap-datepicker/bootstrap-datepicker3.min.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">调账管理</div>
            <em class=""></em>
            <div class="pull-left active">调账记录审核</div>
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
                                   <div class="col-sm-2 " ><input type="text" class="form-control" name="id"></div>
                                    <label class="col-sm-2 control-label">提交人:</label>
                                   <div class="col-sm-2"><input type="text" class="form-control" name="applicant"></div>
                                   
                                   
                           </div>
                           <div class="form-group">
                               <label class="col-sm-2 control-label">提交时间:</label>
                                    <div class="col-sm-4">
                                        <div class="input-daterange input-group" id="datepicker">
                                            <input type="text" class="input-sm form-control" name="beginDate" />
                                            <span class="input-group-addon">~</span>
                                            <input type="text" class="input-sm form-control" name="endDate" />
                                        </div>   
                                      </div>
                           </div>
                                <div class="clearfix lastbottom"></div>
                                
                                   <div class="form-group">
                                        <label class="col-sm-2 control-label aaa"></label>
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                   		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                       <button class="btn btn-success" type="submit"><span class="glyphicon gly-search"></span>查询</button>
                                       <button class="btn btn-default col-sm-offset-14" type="reset"><span class="glyphicon gly-trash"></span>清空</button>
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
                                <!-- <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /> -->
								</div>
							</div>
						</div>
				</div>
			
	</div>
	
   </body>
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
				var data=$("#adjustExamineForm").serializeArray();
				
			     $.each(data, function() {   
			             o[this.name] = this.value || '';    
			     });  
			     
			}
			function customAccountStatusFmatter(cellvalue, options, rowObject){  
					<c:forEach var="status" items="${adjustAccountStatusList}">
						  if(cellvalue == '${status.sysValue}'){
							  return '${status.sysName}';
						  }
					 </c:forEach>	
			}
			
			$("#adjustExamineForm").submit(function(){
				$("#table_list_adjustExamine").setGridParam({
			       datatype : 'json',
			       page : 1            //Replace the '1' here
			    }).trigger("reloadGrid");
				return false;
			});   	
				
   			
   					$(document).ready(function() {
						var lastsel;
			            // 初始化表格
			            $("#table_list_adjustExamine").jqGrid({
			            	url:"${ctx}/adjustAction/findAdjustAccount.do",
			                datatype: "json",
			                mtype: "POST",
			                height:"auto",
			                autowidth: true,
			                shrinkToFit: true,
			                rowNum: 10,
			                rowList: [10, 20],
			                colNames:['操作','调账ID','状态','提交人','提交时间','审核人','审核时间','status'],
			                colModel: [
                                {name:'Detail',index:'Id',width:45,align:"center",sortable:false,frozen:true},
			                    {name: 'id', index: 'id', width: 60, sorttype: "int",align:"left"},
			                    {name: 'status', index: 'status', width: 90,align:"left", sortable:false,formatter:customAccountStatusFmatter},
			                    {name: 'applicant', index: 'applicant',  width: 100,align:"left", sortable:false},
			                    {name: 'applicantTime', index: 'applicantTime',width: 80, align:"left",formatter:function(val){return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
			                    {name: 'approver', index: 'approver',width: 80, align:"left", sortable:false},
			                    {name: 'approveTime', index: 'approveTime',width: 80, align:"left" ,formatter:function(val){return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
			                    {name: 'status', index: 'status', width: 90,align:"left", sortable:false,hidden:'true'},
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
			                        var detail = "" ;
			                        var getRow = $('#table_list_adjustExamine').getRowData(id);
			                        <sec:authorize access="hasAuthority('adjustExamine:examine')">
			                        	if (getRow.status != '2' && getRow.status != '0') {
			                        		detail += "<a href='javascript:void(0);' title='审核' class='default-details' onclick='Detail(" + id + ")'>审核</a>";
			                        	} else {
			                        		detail += "<a href='javascript:void(0);' title='审核' style='cursor:not-allowed;' class='default-undetails' >审核</a>";
			                        	}
			                        </sec:authorize>
			                        jQuery("#table_list_adjustExamine").jqGrid('setRowData', ids[i], { Detail: detail });
			                    }
			                },
			            
			            });
                        jQuery("#table_list_adjustExamine").jqGrid('setFrozenColumns');
			            // Add responsive to jqGrid
			            $(window).bind('resize', function () {
			                var width = $('.jqGrid_wrapper').width();
			                $('#table_list_adjustExamine').setGridWidth(width);
			            });

			            $('.input-daterange').datepicker({
			                format: "yyyy-mm-dd",
			                language: "zh-CN",
			                todayHighlight: true,
			                autoclose: true,
			                clearBtn: true
			            });
			            
			});
			
		
   					
   					function Detail(id) {   //单击修改链接的操作         
   				        var model = $("#table_list_adjustExamine").jqGrid('getRowData', id);
   						var id = model.id;
   				        location.href = '${ctx}/adjustAction/examineList.do?adjustId='+id;
   					}
   					
   					
		</script>
   	
   	
   	</title>