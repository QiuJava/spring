<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<script language="javascript" type="text/javascript"  src="${ctx}/js/My97DatePicker/WdatePicker.js"></script>
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
            <div class="pull-left">内部账号管理</div>
            <em class=""></em>
            <div class="pull-left active">内部账户明细查询</div>
		</div>
	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="insideTransInfoForm" method="post">
                          <div class="form-group">
                          		   <label class="col-sm-2 control-label">账号:</label>
                                   <div class="col-sm-2"><input type="text" class="form-control" name="accountNo" value="${accountNo}"></div>
                                  
                                   <label class="col-sm-2 control-label">借贷方向:</label>
									<div class="col-sm-2">
   										<select class="form-control" name="debitCreditSide"> 
   											 <option value="ALL" selected="selected">全部</option>
   											 <c:forEach var="balanceFrom" items="${balanceFromList}">
												<option value="${balanceFrom.sysValue}"
												<c:if test="${balanceFrom.sysValue == params.debitCreditSide}">selected="selected"</c:if>>
												${balanceFrom.sysName}</option>
											</c:forEach> 
										</select>                          
									  </div>									
						</div>
						
						<div class="form-group">
								 	<label class="col-sm-2 control-label">记账日期:</label>
									 
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
                            <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                    <label class="col-sm-2 control-label aaa"></label>
                            	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            	   <button id="submitBut" type="submit" class=" btn btn-success" value=""><span class="glyphicon gly-search"></span>查询</button>
                                <button type="reset" class=" btn btn-default col-sm-offset-14" value=""><span class="glyphicon gly-trash"></span>清空</button>
                                <sec:authorize access="hasAuthority('insAccountDetailQuery:export')">  
                                	<button id="export" type="button" class=" btn btn-info col-sm-offset-14" value="" onclick="exportExcel()"><span class="glyphicon gly-out"></span>导出</button>
                                </sec:authorize>
                                <c:if test="${!empty forwardTo && forwardTo==1}">
                                	<button id="returnUp" type="button" class=" btn btn-default col-sm-offset-14" onclick="window.location.href='${ctx}/insAccountAction/insAccountListInfo.do?queryParams=${params.queryParams}'" value="" /><span class="glyphicon gly-return"></span>返回</button>
                                </c:if>
                                <c:if test="${!empty forwardTo && forwardTo==2}">
                                	<button id="returnUp" type="button" class=" btn btn-default col-sm-offset-14" onclick="window.location.href='${ctx}/bankAccountAction/bankAccountManage.do'" value="" /><span class="glyphicon gly-return"></span>返回</button>
                                </c:if>
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
								<table id="table_list_insAccountDetail"></table>
								<div id="pager_list_insAccountDetail"></div>
								<br /><br /><br /><br /><br />
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
   		$("#insideTransInfoForm").submit(function(){
			$("#table_list_insAccountDetail").setGridParam({
			       datatype : 'json',
			       page : 1            //Replace the '1' here
			    }).trigger("reloadGrid");
			return false;
		});
   		/*表单提交时的处理*/
   		function exportExcel() {  
   			var data = $("#insideTransInfoForm").serialize();
   			//console.info(data);
   			$.download('${ctx}/insAccountAction/exportInsAccountTrans.do',data,'post');
   	    }
		function getParams(o){
			var data=$("#insideTransInfoForm").serializeArray();
		     $.each(data, function() {   
		             o[this.name] = this.value || '';    
		     });  
		     
		}
		function customBalanceFromFmatter(cellvalue, options, rowObject){  
			<c:forEach var="balanceFrom" items="${balanceFromList}">
				  if(cellvalue == '${balanceFrom.sysValue}'){
					  return '${balanceFrom.sysName}';
				  }
			 </c:forEach>
			 return "" ;
		}   
 			
 		$(document).ready(function() {
				var lastsel;
	            // 初始化表格
	            $("#table_list_insAccountDetail").jqGrid({
	            	url:"${ctx}/insAccountAction/insideTransInfo.do",
	                datatype: "local",
	                mtype: "POST",
	                height:"auto",
	                autowidth: true,
	                shrinkToFit:false,
	                autoScroll: false,
	                rowNum: 10,
	                rowList: [10, 20],
	                colNames:['账号','记账日期','记账时间','记账流水号','记账子交易流水号','借贷方向','记账金额','余额','可用余额','摘要'],
	                colModel: [
	   	                {name: 'accountNo', index: 'accountNo', width: 280,align: "left", sortable:false},
	                    {name: 'recordDate', index: 'recordDate', sorttype: "date",width: 150,align: "left"},
	                    {name: 'recordTime', index: 'recordTime',  width: 150,align: "left"},
	                    {name: 'serialNo', index: 'serialNo',width: 240, align: "left", sortable:false},
	                    {name: 'childSerialNo', index: 'childSerialNo',width: 240, align: "left", sortable:false},
	                    {name: 'debitCreditSide', index: 'debitCreditSide',width: 120, align: "center", sortable:false,formatter: customBalanceFromFmatter},
	                    {name: 'recordAmount', index: 'recordAmount',width: 120, align: "right",formatter: 'number'},
	                    {name: 'balance', index: 'balance',width: 120, align: "right",formatter: 'number'},
	                    {name: 'avaliBalance', index: 'avaliBalance',width: 120, align: "right",formatter: 'number'},
	                    {name: 'summaryInfo', index: 'summaryInfo', width: 200, sorttype: "int",align: "left", sortable:false},
	                ],
	                onSelectRow: function(id){
	            		if(id && id!==lastsel){
	            			jQuery('#table_list_insAccountDetail').jqGrid('restoreRow',lastsel);
	            			jQuery('#table_list_insAccountDetail').jqGrid('editRow',id,true);
	            			lastsel=id;
	            		}
	            	},
	                //multiselect: true,//支持多项选择
	                pager: "#pager_list_insAccountDetail",
	                viewrecords: true,
	                
	                // caption: "科目信息多条列表",
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
	            });
	            // Add responsive to jqGrid
	            $(window).bind('resize', function () {
	                var width = $('.jqGrid_wrapper').width();
	                $('#table_list_insAccountDetail').setGridWidth(width);
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