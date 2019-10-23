
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
            <div class="pull-left active" id="title3">记账流水查询</div>
		</div>
	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="recordFlowForm">
					 <div class="form-group">
						<input type="hidden" class="form-control" name="importId" value="${importId}">
												
					 	<label class="col-sm-2 control-label">交易日期：</label>
								 <div class="col-sm-4">
		                            <div class="input-daterange input-group" id="datepicker">
									    <input type="text" class="input-sm form-control" name="beginDate" />
									    <span class="input-group-addon">~</span>
									    <input type="text" class="input-sm form-control" name="endDate" />
									</div>   
								  </div>

						
					 </div>
					 <div class="form-group">
					 	<label class="col-sm-2 control-label">记账流水号：</label>
						<div class="col-sm-2"><input type="text" class="form-control" name="serialNo"></div>
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
					 </div>
					 
					 <div class="form-group">
					 	
						<label class="col-sm-2 control-label">外部用户类型：</label>
						<div class="col-sm-2">
							<select class="form-control" name="accountType"> 
	 							<option value="ALL" selected="selected">全部</option>
									<c:forEach var="item" items="${accountTypeList}">
										<option value="${item.sysValue}">
											${item.sysName}</option>
									</c:forEach>
							</select>
						</div>
						<label class="col-sm-2 control-label">外部用户编号：</label>
						<div class="col-sm-2"><input type="text" class="form-control" name="userId"></div>
						<label class="col-sm-2 control-label">账号：</label>
                                   <div class="col-sm-2"><input type="text" class="form-control" name="accountNo"></div>
					</div>
					 <div class="form-group">
					 		
							
                             <label class="col-sm-2 control-label">支付机构号：</label>
                             <div class="col-sm-2"><input type="text" class="form-control" name="orgNo"></div>
                             <label class="col-sm-2 control-label">内／外标志：</label>
								<div class="col-sm-2">
									<select class="form-control" name="accountFlag"> 
	 									<option value="ALL" selected="selected">全部</option>
								         <c:forEach var="accountFlag" items="${accountFlagList}">
											<option value="${accountFlag.sysValue}">
											${accountFlag.sysName}</option>
										</c:forEach>
									</select>
								</div>
							<label class="col-sm-2 control-label">冲销标志：</label>
								<div class="col-sm-2">
									<select class="form-control" name="reverseFlag"> 
	 									<option value="ALL" selected="selected">全部</option>
	 									<option value="NORMAL">正常交易</option>
	 									<option value="REVERSED">冲销交易</option>
									</select>
								</div>
					 </div>
					 
                     <div class="form-group">
                     		<label class="col-sm-2 control-label">科目内部编号：</label>
                                   <div class="col-sm-2"><input type="text" class="form-control" name="subjectNo"></div>
                             <label class="col-sm-2 control-label">借贷方向：</label>
								<div class="col-sm-2">
									<select class="form-control" name="debitCreditSide"> 
	 									<option value="ALL" selected="selected">全部</option>
								        <option value="debit">借方</option>
								        <option value="credit">贷方</option>
									</select>
								</div>
                             <label class="col-sm-2 control-label">交易类型：</label>
								<div class="col-sm-2">
									<select class="form-control" name="transType"> 
	 									<option value="ALL" selected="selected">全部</option>
			                               <c:forEach var="transType" items="${transTypeList}">
			                                  <option value="${transType.transTypeCode}">
			                                  ${transType.transTypeName}-${transType.fromSystem}</option>
			                              </c:forEach>
									</select>
								</div>
                             
					  </div>
					 
                                <div class="clearfix lastbottom"></div>
                                
                                   <div class="form-group">
                                   		<label class="col-sm-2 control-label aaa"></label>
                                   <!-- <div class="col-sm-11 col-sm-offset-13  "> -->
                                   	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                       <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                       <button id="submitBut" type="submit" class=" btn btn-success " value="" ><span class="glyphicon gly-search"></span>查询</button>
                                       <button class="btn btn-default col-sm-offset-14" type="reset"><span class="glyphicon gly-trash"></span>清空</button>
                                       <sec:authorize access="hasAuthority('recordAccountFlow:export')">
                                       		<button id="export" type="button" class=" btn btn-info col-sm-offset-14" value=""  onclick="exportExcel()"><span class="glyphicon gly-out"></span>导出</button>
                                       </sec:authorize>
                                       <button id="returnUp" type="button" class=" btn btn-default col-sm-offset-14" onclick="window.location.href='${ctx}/reportAction/toTransFlow.do?queryParams=${params.queryParams}'" value="" style="display: none;" /><span class="glyphicon gly-return"></span>返回</button>
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
	   		$("#recordFlowForm").submit(function(){
				$("#table_list_transFlow").setGridParam({
				       datatype : 'json',
				       page : 1            //Replace the '1' here
				    }).trigger("reloadGrid");
				return false;
			});
	   		/*表单提交时的处理*/
	   		function exportExcel() {  
	   			var data = $("#recordFlowForm").serialize();
	   			//console.info(data);
	   			$.download('${ctx}/reportAction/exportRecordAccountFlow.do',data,'post');
	   	    }
	   		 	
			
			function getParams(o){
				var data=$("#recordFlowForm").serializeArray();
				
			     $.each(data, function() {   
			             o[this.name] = this.value || '';    
			     });  
			     //o['subjectNo'] = $("#subjectNo").select2("val");

			}
				 
			$(document).ready(function() {
				var importId = '${importId}' ;
				//importId = '${importId}' ;alert(importId) ;
				if(importId != "" ){
					$("#returnUp").get(0).style.display = "inline" ;
					$("#title3").html("交易流水查询明细") ;
				}
				
				var lastsel;
			            // 初始化表格
			            $("#table_list_transFlow").jqGrid({
			            	url:"${ctx}/reportAction/recordAccountFlow.do",
			                datatype: "local",
			                mtype: "POST",
			                height:"auto",
			                autowidth: true,
			                shrinkToFit: false,
			                autoScroll: false,
			                rowNum: 10,
			                rowList: [10, 20],
 			                colNames:['交易日期','记账流水号','交易类型','外部用户类型','分录号','记账子流水号','交易金额','借贷方向','内/外标志','外部用户编号','冲销标志','账号','科目内部编号','币种号','支付机构号','摘要'],		               		
 			                colModel: [
			                   {name: 'transDate', index: 'transDate', width: 230 ,align: "center",
			                	   formatter : function(val) {
										return myFormatDate(val,"yyyy-MM-dd");
									}},
			                   {name: 'serialNo', index: 'serialNo',  width: 200,align: "left", sortable:false},
			                   {name: 'transType', index: 'transType',width: 220, align: "center", sortable:false,formatter:  customTransTypeFmatter},
			                   {name: 'accountType', index: 'accountType',  width: 200,align: "center", sortable:false, formatter:  customAccountTypeFmatter},
			                   {name: 'journalNo', index: 'journalNo',width: 200, align: "left", sortable:false},
			                   {name: 'childSerialNo', index: 'childSerialNo',  width: 200,align: "left", sortable:false},
			                   {name: 'transAmount', index: 'transAmount',width: 140, align: "right",formatter: 'number'},
			                   {name: 'debitCreditSide', index: 'debitCreditSide',width: 80, align: "center", sortable:false,formatter:  customDebitCreditSideFmatter},
			                   {name: 'accountFlag', index: 'accountFlag',width: 100, align: "center", sortable:false,formatter:  customAccountFlagFmatter},
			                   {name: 'userId', index: 'userId',width: 200, align: "center", sortable:false},
			                   {name: 'reverseFlag', index: 'reverseFlag',width: 120, align: "center", sortable:false,formatter:  customReverseFlagFmatter},
			                   {name: 'accountNo', index: 'accountNo',width: 250, align: "left", sortable:false},
			                   {name: 'subjectNo', index: 'subjectNo',width: 200, align: "left", sortable:false},
			                   {name: 'currencyNo', index: 'currencyNo',width: 100, align: "center", sortable:false,formatter:  customCurrencyFmatter},
			                   {name: 'orgNo', index: 'orgNo',width: 200, align: "left", sortable:false},
			                   {name: 'summaryInfo', index: 'summaryInfo',width: 300, align: "center", sortable:false},
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
			            });
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
			function customCurrencyFmatter(cellvalue, options, rowObject){  
				<c:forEach var="currency" items="${currencyList}">
					  if(cellvalue == '${currency.currencyNo}'){
						  return '${currency.currencyName}';
					  }
				 </c:forEach>	
				 return "" ;
			}
			
			function customAccountTypeFmatter(cellvalue, options, rowObject){  
				<c:forEach var="item" items="${accountTypeList}">
					  if(cellvalue == '${item.sysValue}'){
						  return '${item.sysName}';
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
			
			function customTransTypeFmatter(cellvalue, options, rowObject){  
				<c:forEach var="transType" items="${transTypeList}">
				  if(cellvalue == '${transType.transTypeCode}'){
					  return '${transType.transTypeName}';
				  }
			 	</c:forEach>	
				return "" ;
			}

			function customDebitCreditSideFmatter(cellvalue, options, rowObject){  
				return cellvalue == 'debit' ?"借方":cellvalue == 'credit'?"贷方":"" ;
			}
			
			function customReverseFlagFmatter(cellvalue, options, rowObject){  
				return cellvalue == 'NORMAL' ?"正常交易":cellvalue == 'REVERSED'?"冲销交易":"" ;
			}
		</script>
	</title>
	
   </body>