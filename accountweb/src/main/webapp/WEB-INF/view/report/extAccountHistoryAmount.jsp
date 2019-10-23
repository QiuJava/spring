
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
            <div class="pull-left active">外部账户历史余额查询</div>
		</div>
	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="extAccountHistoryAmountForm">
					 	<div class="form-group">
					 		<label class="col-sm-2 control-label">账号：</label>
                            <div class="col-sm-2"><input type="text" class="form-control" id="accountNo" name="accountNo"></div>
                             <label class="col-sm-2 control-label">记账日期：</label>
								 <div class="col-sm-4">
		                            <div class="input-daterange input-group" id="datepicker">
									    <input type="text" class="input-sm form-control" name="beginDate" value="${params.beginDate}"/>
									    <span class="input-group-addon">~</span>
									    <input type="text" class="input-sm form-control" name="endDate" value="${params.endDate}" />
									</div>   
								  </div>
                                   		
							</div>
						 <div class="form-group">
						 		<label class="col-sm-2 control-label">用户编号：</label>
									<div class="col-sm-2"><input type="text" class="form-control" name="extAccountInfo.userId"></div>
								<label class="col-sm-2 control-label">币种号：</label>
	                                   <div class="col-sm-2">
	   										<select class="form-control" name="currencyNo"> 
												<option value="ALL" selected="selected">全部</option>
										         <c:forEach var="currency" items="${currencyList}">
													<option value="${currency.currencyNo}"
													<c:if test="${currency.currencyNo == params.subjectType}">selected="selected"</c:if>>
													${currency.currencyName}</option>
												</c:forEach>
											</select>                       
										</div>
										<label class="col-sm-2 control-label">科目编号：</label>
	                                   <div class="col-sm-2"><input type="text" class="form-control" name="subjectNo"></div>
	                             
						 </div>
						 		<div class="form-group">
						 			
						 		<label class="col-sm-2 control-label">手机号：</label>
							<div class="col-sm-2">
								<input type="text" class="form-control"
									name="extAccountInfo.mobilephone"
									onkeyup="this.value=this.value.replace(/\D/g,'')"
									onafterpaste="this.value=this.value.replace(/\D/g,'')">
							</div>
						 		<label class="col-sm-2 control-label">用户类别：</label>
							<!-- <div class="pull-left control-label labeldiv" style="width:62px"></div> -->
							<div class="col-sm-2">
								<select class="form-control" name="extAccountInfo.accountType">
									<option value="ALL" selected="selected">全部</option>
									<c:forEach var="accountType" items="${accountTypeList}">
										<option value="${accountType.sysValue}"
											<c:if test="${accountType.sysValue == params.userType}">selected="selected"</c:if>>
											${accountType.sysName}</option>
									</c:forEach>
								</select>
							</div>
						 		</div>
						 
						 		
						 		
                                <div class="clearfix lastbottom"></div>
                                
                                   <div class="form-group">
                                   		<label class="col-sm-2 control-label aaa"></label>
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                   	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                       <button id="submitBut" type="submit" class=" btn btn-success " value="" ><span class="glyphicon gly-search"></span>查询</button>
                                       <button class="btn btn-default col-sm-offset-14" type="reset"><span class="glyphicon gly-trash"></span>清空</button>
                                       <sec:authorize access="hasAuthority('extAccountHistoryAmount:export')">
                                       		<button id="export" type="button" class=" btn btn-info col-sm-offset-14" value=""  onclick="exportExcel()"><span class="glyphicon gly-out"></span>导出</button>
                                   	   </sec:authorize>
                                   <!-- </div>                              -->
                              	 </div>
                    </form>
				</div>
			</div>
		</div>
				<div class="col-lg-12">
						<div class="ibox ">
							<div class="ibox-content">
								<div class="jqGrid_wrapper">
								<table id="table_list_extAccountHistoryAmount"></table>
								<div id="pager_list_extAccountHistoryAmount"></div>
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
	   		$("#extAccountHistoryAmountForm").submit(function(){
				/*if(!$("#accountNo").val()){
					toastr.error("请输入账号后点击查询！");
					return false;
				}
*/
				$("#table_list_extAccountHistoryAmount").setGridParam({
                       url:"${ctx}/reportAction/extAccountHistoryAmount.do",
				       datatype : 'json',
				       page : 1            //Replace the '1' here
				    }).trigger("reloadGrid");
				return false;
			});
	   		/*表单提交时的处理*/
	   		function exportExcel() {
	   			var data = $("#extAccountHistoryAmountForm").serialize();
	   			//console.info(data);
	   			$.download('${ctx}/reportAction/exportExtAccountHistoryAmount.do',data,'post');
	   	    }
	   		 	

			function getParams(o){
				var data=$("#extAccountHistoryAmountForm").serializeArray();

			     $.each(data, function() {   
			             o[this.name] = this.value || '';    
			     });
			     //o['subjectNo'] = $("#subjectNo").select2("val");

			}

			$(document).ready(function() {
						var lastsel;
			            // 初始化表格
			            $("#table_list_extAccountHistoryAmount").jqGrid({
			                datatype: "json",
			                mtype: "POST",
			                height:"auto",
			                autowidth: true,
			                shrinkToFit: false,
			                autoScroll: false,
			                rowNum: 10,
			                rowList: [10, 20],
 			                colNames:['用户编号','商户/代理商等名称','记账日期','账号','用户类别','手机号','机构号','机构名称','币种号','科目编号','当前余额','冻结金额','上一个记账日期余额','账户状态'],		               		
 			                colModel: [
			                   {name: 'userId', index: 'userId', width: 230 ,align: "left", sortable:false},
			                   {name: 'userName', index: 'userName',  width: 150,align: "left", sortable:false},
			                   {name: 'billDate', index: 'billDate',width: 180, align: "center"},
			                   {name: 'accountNo', index: 'accountNo',  width: 250,align: "left", sortable:false},
			                   
			                   {name: 'accountType', index: 'accountType', width: 100 , align: "center", sortable:false, formatter:  customAccountTypeFmatter},
			                   {name: 'mobilephone', index: 'mobilephone',  width: 100, align: "left", sortable:false},
			                   
			                   {name: 'orgNo', index: 'orgNo',width: 200, align: "center", sortable:false},
			                   {name: 'orgName', index: 'orgName',width: 150, align: "center", sortable:false},
			                   {name: 'currencyNo', index: 'currencyNo',width: 100, align: "center", sortable:false,formatter:  customCurrencyFmatter},
			                   {name: 'subjectNo', index: 'subjectNo',width: 200, align: "left", sortable:false},
			                   {name: 'currBalance', index: 'currBalance',width: 150, align: "right",formatter: 'number'},
			                   {name: 'controlAmount', index: 'controlAmount',width: 150, align: "right",formatter: 'number'},
			                   {name: 'parentTransBalance', index: 'parentTransBalance',width: 150, align: "right",formatter: 'number'},
			                   {name: 'accountStatus', index: 'accountStatus',width: 100, align: "center", sortable:false,formatter:  customAccountStatusFmatter},
			                ],
			                onSelectRow: function(id){
			            		if(id && id!==lastsel){
			            			jQuery('#table_list_extAccountHistoryAmount').jqGrid('restoreRow',lastsel);
			            			jQuery('#table_list_extAccountHistoryAmount').jqGrid('editRow',id,true);
			            			lastsel=id;
			            		}
			            	},
			            	
			               // multiselect: true,//支持多项选择
			                pager: "#pager_list_extAccountHistoryAmount",
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
			            });
			            // Add responsive to jqGrid
			            $(window).bind('resize', function () {
			                var width = $('.jqGrid_wrapper').width();
			                $('#table_list_extAccountHistoryAmount').setGridWidth(width);
			            });

			            $('.input-daterange').datepicker({
			                format: "yyyy-mm-dd",
			                language: "zh-CN",
			                todayHighlight: true,
			                autoclose: true,
			                clearBtn: true
			            });
			            
			});
			function customCurrencyFmatter(cellvalue, options, rowObject){  
				<c:forEach var="currency" items="${currencyList}">
					  if(cellvalue == '${currency.currencyNo}'){
						  return '${currency.currencyName}';
					  }
				 </c:forEach>	
				 return "" ;
			}
			function customAccountTypeFmatter(cellvalue, options, rowObject){  
				<c:forEach var="accountType" items="${accountTypeList}">
					  if(cellvalue == '${accountType.sysValue}'){
						  return '${accountType.sysName}';
					  }
				 </c:forEach>	
				 return "" ;
			}
			function customAccountStatusFmatter(cellvalue, options, rowObject){  
				<c:forEach var="accountStatus" items="${accountStatusList}">
					  if(cellvalue == '${accountStatus.sysValue}'){
						  return '${accountStatus.sysName}';
					  }
				 </c:forEach>	
				 return "" ;
			}
			
		</script>
	</title>
	
   </body>