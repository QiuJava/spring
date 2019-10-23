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
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">业务管理</div>
            <em class=""></em>
            <div class="pull-left active">调账记录多条列表查询</div>
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
                  		    <label class="col-sm-2 control-label">调账ID：</label>
                            <div class="col-sm-2"><input type="text" class="form-control" name="id"></div>
                            
                            <label class="col-sm-2 control-label">提交人：</label>
                            <div class="col-sm-2"><input type="text" class="form-control" name="applicant"></div>
                                
						</div>
						 <div class="form-group">                         	
							 <label class="col-sm-2 control-label">提交时间：</label>
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
								<table id="table_list_adjust_record"></table>
								<div id="pager_list_adjust_record"></div>
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
		$(document).ready(function () {
			// console.log($("#table_list_adjust_record").html())
	        $(".ui-row-ltr td:last-child").each(function () {
	            var textValue = $(this).html();
	            console.log(textValue);

	            // if (textValue == "XX概况" || textValue == "服务导航") {
	            //     $(this).css("cursor", "default");
	            //     $(this).attr('href', '#');     //修改<a>的 href属性值为 #  这样状态栏不会显示链接地址  
	            //     $(this).click(function (event) {
	            //         event.preventDefault();   // 如果<a>定义了 target="_blank“ 需要这句来阻止打开新页面
	            //     });
	            // }
	        });
		});


		function Detail(id) {   //单击修改链接的操作         
	        var model = $("#table_list_adjust_record").jqGrid('getRowData', id);
			var id = model.id;
	        location.href='${ctx}/business/businessRecordDetail.do?id='+id;
	    }
		function Modify(id) {   //单击修改链接的操作         
	        var model = $("#table_list_adjust_record").jqGrid('getRowData', id);
			var id = model.id;
	        location.href='${ctx}/business/businessUpdate.do?id='+id;
		}
		
		function Shenhe(id) {   //单击修改链接的操作         
	        var model = $("#table_list_adjust_record").jqGrid('getRowData', id);
			var id = model.id;
	        location.href='${ctx}/business/businessRecordExamine.do?id='+id;
		}

		function customAccountTypeFmatter(cellvalue, options, rowObject){  
				<c:forEach var="accountType" items="${adjustAccountTypeList}">
					  if(cellvalue == '${accountType.sysValue}'){
						  return '${accountType.sysName}';
					  }
				 </c:forEach>
				 return "" ;
			}   	

		function customAccountStatusFmatter(cellvalue, options, rowObject){  
				<c:forEach var="status" items="${adjustAccountStatusList}">
					  if(cellvalue == '${status.sysValue}'){
						  return '${status.sysName}';
					  }
				 </c:forEach>	
				 return "" ;
			}   	
			

		function getParams(o){
			var data=$("#form1").serializeArray();
		     $.each(data, function() {    
		             o[this.name] = this.value || '';    
		     });   
		}
		$("#form1").submit(function(){
			$("#table_list_adjust_record").setGridParam({
			       datatype : 'json',
			       page : 1            //Replace the '1' here
			    }).trigger("reloadGrid");
			
			return false;
		});
		$(document).ready(function() {
					var lastsel;
		            // 初始化表格
		            $("#table_list_adjust_record").jqGrid({
		            	url:"${ctx}/business/findBusinessAccountRecord.do",
		                datatype: "json",
		                mtype: "POST",
		                height:"auto",
		                autowidth: true,
		                shrinkToFit: true,
		                rowNum: 10,
		                rowList: [10, 20],
		                colNames:['操作','调账ID','提交人', '提交时间', '审核人','审核时间','状态',],
		                colModel: [
		                	{name:'Detail',index:'detail',width:150,align:"center",sortable:false,frozen:true},
		                    {name: 'id', index: 'id', width: 60, sorttype: "int", align: "left"},
		                    {name: 'applicant', index: 'applicant', width: 90, align: "left", sortable:false},
		                    {name: 'applicantTime', index: 'applicantTime', editable:true, width: 200,align: "left",formatter:function(val){return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
		                    {name: 'approver', index: 'approver',width: 80, align: "left", sortable:false},
		                    {name: 'approveTime', index: 'approveTime',width: 200, align: "left",formatter:function(val){return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
		                    {name: 'status', index: 'status',width: 80, align: "center", sortable:false,formatter:customAccountStatusFmatter},
		                ],
		                onSelectRow: function(id){
		            		if(id && id!==lastsel){
		            			jQuery('#table_list_user').jqGrid('restoreRow',lastsel);
		            			jQuery('#table_list_user').jqGrid('editRow',id,true);
		            			lastsel=id;
		            		}
		            	},
		                multiselect: false,//支持多项选择
		                pager: "#pager_list_adjust_record",
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
		                    var ids=$("#table_list_adjust_record").jqGrid('getDataIDs');
		                    console.log(ids.length)
		                    for(var i=0; i<ids.length; i++){
		                        var id=ids[i];   
		                        var getRow = $('#table_list_adjust_record').getRowData(ids[i]);
		                        // console.log(getRow);
		                       	var status = getRow.status;
							    console.log(status);
							    var username = '${userName}';
							    var detail = "" ;
							    if(getRow.applicant != username){
							    	detail += "<a href='javascript:void(0);' class='default-details'  title='查看' onclick='Detail(" + id + ")'>查看</a>";
							    	<sec:authorize access="hasAuthority('businessRecordListQuery:update')">
							    		detail += "&nbsp;&nbsp;<a href='javascript:void(0);' class='default-undelete' title='编辑' >编辑</a>";
								    </sec:authorize>
								    <sec:authorize access="hasAuthority('businessRecordListQuery:submitExamine')">
							    		detail += "&nbsp;&nbsp;<a href='javascript:void(0);' class='default-undetails'  title='提交审核' >提交审核</a>";
							    	</sec:authorize>
							    	jQuery("#table_list_adjust_record").jqGrid('setRowData', ids[i], { Detail: detail });
							    }else{
							    	if(status == '审核通过' || status == '待审核'){
							    		detail += "<a href='javascript:void(0);' class='default-details'  title='查看' onclick='Detail(" + id + ")'>查看</a>";
							    		<sec:authorize access="hasAuthority('adjustRecordListQuery:update')">
								    		detail += "&nbsp;&nbsp;<a href='javascript:void(0);' class='default-delete' onclick='Modify(" + id + ")' title='编辑' >编辑</a>";
						                </sec:authorize>
									    <sec:authorize access="hasAuthority('adjustRecordListQuery:submitExamine')">
					                        detail += "&nbsp;&nbsp;<a href='javascript:void(0);' class='default-undetails'  title='提交审核' >提交审核</a>";
					                    </sec:authorize>
					                    jQuery("#table_list_adjust_record").jqGrid('setRowData', ids[i], { Detail: detail });
							    	}else{
							    		detail += "<a href='javascript:void(0);' class='default-details'  title='查看' onclick='Detail(" + id + ")'>查看</a>";
							    		<sec:authorize access="hasAuthority('adjustRecordListQuery:update')">
								    		detail += "&nbsp;&nbsp;<a href='javascript:void(0);' class='default-delete' title='编辑' onclick='Modify(" + id + ")'>编辑</a>";
						                </sec:authorize>
									    <sec:authorize access="hasAuthority('adjustRecordListQuery:submitExamine')">
					                        detail += "&nbsp;&nbsp;<a href='javascript:void(0);' class='default-details'  title='提交审核' onclick='Shenhe(" + id + ")'>提交审核</a>";
							            </sec:authorize>
				                        jQuery("#table_list_adjust_record").jqGrid('setRowData', ids[i], { Detail: detail });
							    	}
							    }


		                    }
		     //                var getRow = $('#table_list_adjust_record').getRowData();
		     //                // console.log(getRow)
		     //                // console.log(typeof(getRow));
		     //                $.each(getRow, function(idx, obj) {
		     //                	var status = obj.status;
							//     console.log(status);
							//     if (status == '审批通过' || status == '待审批') {
							//     	// console.log(this.Detail)
							//     	console.log($(this))
							//     	$(this).Detail = 100;
							//     	console.log($(this))
							//     };

							//     // console.log(obj.Detail)
							// });
		            		// console.log($("#table_list_adjust_record tr td").html())

		                }
		                
		            });
					jQuery("#table_list_adjust_record").jqGrid('setFrozenColumns');
		            // Add responsive to jqGrid
		            $(window).bind('resize', function () {
		                var width = $('.jqGrid_wrapper').width();
		                $('#table_list_adjust_record').setGridWidth(width);
		            });

		            $('.input-daterange').datepicker({
		                format: "yyyy-mm-dd",
		                language: "zh-CN",
		                todayHighlight: true,
		                autoclose: true,
		                clearBtn: true
		            });
		            
		});
		

	</script>
	
	 </title>
</html>  
      