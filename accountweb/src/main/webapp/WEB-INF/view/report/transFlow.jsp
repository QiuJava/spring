
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
            <div class="pull-left active">交易流水查询</div>
		</div>
	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="transFlowForm">
					 <div class="form-group">
					 		<label class="col-sm-2 control-label">来源系统：</label>
                           <div class="col-sm-2">
                          		<select class="form-control" name="fromSystem"> 
	                          		<option value="ALL" selected="selected">全部</option>
	                               <c:forEach var="fromSystem" items="${fromSystemList}">
	                                  <option value="${fromSystem.sysValue}"
	                                  <c:if test="${fromSystem.sysValue == params.fromSystem}">selected="selected"</c:if>>
	                                  ${fromSystem.sysName}</option>
	                              </c:forEach>
                          		</select> 
                         	</div>
                         	<label class="col-sm-2 control-label">冲销标志：</label>
								<div class="col-sm-2">
									<select class="form-control" name="reverseFlag"> 
	 									<option value="ALL" selected="selected">全部</option>
	 									<!-- <option value="0">正常交易</option>
	 									<option value="1">冲销交易</option> -->
	 									<c:forEach var="reverseFlag" items="${reverseFlagList}">
			                                  <option value="${reverseFlag.sysValue}"
			                                   <c:if test="${reverseFlag.sysValue == params.reverseFlag}">selected="selected"</c:if>>
			                                  ${reverseFlag.sysName}</option>
			                            </c:forEach>
									</select>
								</div>
							<label class="col-sm-2 control-label">冲销状态：</label>
								<div class="col-sm-2">
									<select class="form-control" name="reverseStatus"> 
	 									<option value="ALL" selected="selected">全部</option>
	 									<!-- <option value="0">正常</option>
	 									<option value="1">已冲销</option> -->
	 									<c:forEach var="reverseStatus" items="${reverseStatusList}">
			                                  <option value="${reverseStatus.sysValue}"
			                                  <c:if test="${reverseStatus.sysValue == params.reverseStatus}">selected="selected"</c:if>>
			                                  ${reverseStatus.sysName}</option>
			                            </c:forEach>
									</select>
								</div>

					 </div>
					 
					 <div class="form-group">
					 		
							<label class="col-sm-2 control-label">记账标志：</label>
								<div class="col-sm-2">
									<select class="form-control" name="recordStatus"> 
	 									<option value="ALL" selected="selected">全部</option>
	 									<!-- <option value="1">已记账</option>
	 									<option value="0">未记账</option> -->
	 									<c:forEach var="recordStatus" items="${recordStatusList}">
			                                  <option value="${recordStatus.sysValue}"
			                                  <c:if test="${recordStatus.sysValue == params.recordStatus}">selected="selected"</c:if>>
													${recordStatus.sysName}>
			                                  ${recordStatus.sysName}</option>
			                            </c:forEach>
									</select>
								</div>
								<label class="col-sm-2 control-label">交易类型：</label>
								<div class="col-sm-2">
									<select class="form-control" name="transType"> 
	 									<option value="ALL" selected="selected">全部</option>
			                               <c:forEach var="transType" items="${transTypeList}">
			                                  <option value="${transType.transTypeCode}"
			                                  <c:if test="${transType.transTypeCode == params.transType}">selected="selected"</c:if>>
			                                  ${transType.transTypeName}-${transType.fromSystem}</option>
			                              </c:forEach>
									</select>
								</div>
							<label class="col-sm-2 control-label">交易卡号：</label>
							<div class="col-sm-2"><input type="text" class="form-control" name="cardNo"  value="${params.cardNo}"></div>
					 </div>
					 
					 <div class="form-group">
					 	
						<label class="col-sm-2 control-label">交易金额：</label>
						 <div class="col-sm-2">
                            <div class=" input-group" id="transAmount">
							    <input type="text" class="form-control" name="transBeginAmount" id="transBeginAmount" value="${params.transBeginAmount}"> 
							    <span class="input-group-addon">~</span>
                            	<input type="text" class="form-control" name="transEndAmount" id="transEndAmount" value="${params.transEndAmount}"> 
							</div>   
						  </div>
						  <label class="col-sm-2 control-label">商户编号：</label>
							<div class="col-sm-2"><input type="text" class="form-control" name="merchantNo" value="${params.merchantNo}"></div>
							<label class="col-sm-2 control-label">代理商编号：</label>
							<div class="col-sm-2"><input type="text" class="form-control" name="directAgentNo" value="${params.directAgentNo}"></div>
					 </div>
					 
					 <div class="form-group">
					 	
							<label class="col-sm-2 control-label">交易订单号：</label>
							<div class="col-sm-2"><input type="text" class="form-control" name="transOrderNo" value="${params.transOrderNo}"></div>
							<label class="col-sm-2 control-label">记账流水号：</label>
							<div class="col-sm-2"><input type="text" class="form-control" name="recordSerialNo" value="${params.recordSerialNo}"></div>
							<label class="col-sm-2 control-label">来源系统流水号：</label>
							<div class="col-sm-2"><input type="text" class="form-control" name="fromSerialNo" value="${params.fromSerialNo}"></div>
					 </div>
					<div class="form-group">
					 	<label class="col-sm-2 control-label" >收单机构：(交易通道)</label>
								<div class="col-sm-2">
									<select class="form-control" name="acqEnname"> 
	 									<option value="ALL" selected="selected">全部</option>
			                               <c:forEach var="acqOrg" items="${acqOrgList}">
			                                  <option value="${acqOrg.acqEnname}"
			                                  <c:if test="${acqOrg.acqEnname == params.acqEnname}">selected="selected"</c:if>>
													${acqOrg.acqEnname}>
			                                  ${acqOrg.acqEnname}</option>
			                              </c:forEach>
									</select>
								</div>
						<label class="col-sm-2 control-label" >收单机构：(出款通道)</label>
								<div class="col-sm-2">
									<select class="form-control" name="outAcqEnname"> 
	 									<option value="ALL" selected="selected">全部</option>
			                               <c:forEach var="acqOrg" items="${acqOrgList}">
			                                  <option value="${acqOrg.acqEnname}"
			                                  <c:if test="${acqOrg.acqEnname == params.acqEnname}">selected="selected"</c:if>>
													${acqOrg.acqEnname}>
			                                  ${acqOrg.acqEnname}</option>
			                              </c:forEach>
									</select>
								</div>
					 </div>
					 <div class="form-group">
					 	
					 	
						 <label class="col-sm-2 control-label">交易时间：</label>
						 <div class="col-sm-4">
                            <div class="input-daterange input-group" id="datepicker2">
							    <input type="text" class="input-sm form-control" name="transBeginDate"  value="${params.transBeginDate}"/>
							    <span class="input-group-addon">~</span>
							    <input type="text" class="input-sm form-control" name="transEndDate"  value="${params.transEndDate}"/>
							</div>   
						  </div>
					 	
					 </div>
					 <div class="form-group">
					 	<label class="col-sm-2 control-label">记账时间：</label>
						 <div class="col-sm-4">
                            <div class="input-daterange input-group" id="datepicker">
							    <input type="text" class="input-sm form-control" name="recordBeginDate"  value="${params.recordBeginDate}"/>
							    <span class="input-group-addon">~</span>
							    <input type="text" class="input-sm form-control" name="recordEndDate"  value="${params.recordEndDate}"/>
							</div>   
						  </div>
					 </div>
					 
					 
                                <div class="clearfix lastbottom"></div>
                                
                                   <div class="form-group">
                                   		<label class="col-sm-2 control-label aaa"></label>
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                   	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                       <button id="submitBut" type="submit" class=" btn btn-success " value="" ><span class="glyphicon gly-search"></span>查询</button>
                                       <button class="btn btn-default col-sm-offset-14" type="reset"><span class="glyphicon gly-trash"></span>清空</button>
                                        <sec:authorize access="hasAuthority('transFlow:export')">
                                        	<button id="export" type="button" class=" btn btn-info col-sm-offset-14" value=""  onclick="exportExcel()"><span class="glyphicon gly-out"></span>导出</button>
                                   		</sec:authorize>
                                   <!-- </div>                               -->
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
	
	var matched;  
	  
	jQuery.uaMatch = function( ua ) {  
	    ua = ua.toLowerCase();  
	  
	    var match = /(chrome)[ \/]([\w.]+)/.exec( ua ) ||  
	        /(webkit)[ \/]([\w.]+)/.exec( ua ) ||  
	        /(opera)(?:.*version|)[ \/]([\w.]+)/.exec( ua ) ||  
	        /(msie) ([\w.]+)/.exec( ua ) ||  
	        ua.indexOf("compatible") < 0 && /(mozilla)(?:.*? rv:([\w.]+)|)/.exec( ua ) ||  
	        [];  
	  
	    return {  
	        browser: match[ 1 ] || "",  
	        version: match[ 2 ] || "0"  
	    };  
	};  
	  
	matched = jQuery.uaMatch( navigator.userAgent );  
			var browser = {
					isIE: function() {
						var _uaMatch = $.uaMatch(navigator.userAgent);
						var _browser = _uaMatch.browser;
						if (_browser == 'msie') {
							return true;
						} else {
							return false;
						}
					},
					isChrome: function() {
						var _uaMatch = $.uaMatch(navigator.userAgent);
						var _browser = _uaMatch.browser;
						if (_browser == 'chrome') {
							return true;
						} else {
							return false;
						}
					},
					isMozila: function() {
						var _uaMatch = $.uaMatch(navigator.userAgent);
						var _browser = _uaMatch.browser;
						if (_browser == 'mozilla') {
							return true;
						} else {
							return false;
						}
					},
					isOpera: function() {
						var _uaMatch = $.uaMatch(navigator.userAgent);
						var _browser = _uaMatch.browser;
						if (_browser == 'webkit') {
							return true;
						} else {
							return false;
						}
					}
				};
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
	   		$("#transFlowForm").submit(function(){
				$("#table_list_transFlow").setGridParam({
				       datatype : 'json',
				       page : 1            //Replace the '1' here
				    }).trigger("reloadGrid");
				return false;
			});
	   		/*表单提交时的处理*/
	   		function exportExcel() {  
	   			var data = $("#transFlowForm").serialize();
	   			//console.info(data);
	   			$.download('${ctx}/reportAction/exportTransFlow.do',data,'post');
	   	    }
	   	    
	   	    function viewDetail(id) {
	   	    	location.href = "${ctx}/reportAction/toTransFlowDetail.do?id="+id;
	   	    }
	   		 	
			
			function getParams(o){
				var data=$("#transFlowForm").serializeArray();
				
			     $.each(data, function() {   
			             o[this.name] = this.value || '';    
			     });  
			     //o['subjectNo'] = $("#subjectNo").select2("val");

			}
			function hackHeight() {
				var listId = "#table_list_transFlow";
			 	$(listId + '_frozen tr').slice(1).each(function() {
			 		var rowId = $(this).attr('id');
			 		var frozenTdHeight = parseFloat($('td:first', this).height());
			 		var normalHeight = parseFloat($(listId + ' #' + $(this).attr('id')).find('td:first').height());
			 		if (frozenTdHeight < normalHeight) {
			 			$('td', this).each(function() {
			 				var space = 0; 
			 				if (browser.isChrome()) {
			 					space = 6.5;
			 				} else if (browser.isIE()) {
			 					space = 6.8;
			 				} else if (browser.isMozila()) {
			 					space = 7.05;
			 				}
			 				if (!$(this).attr('style') || $(this).attr('style').indexOf('height:') == -1) {
			 					$(this).attr('style', $(this).attr('style') + ";height:" + (normalHeight + space) + "px !important");
			 				}
			 			});
			 		}
			 	});
			 }
			
			function Detail(id) {   //单击修改链接的操作         
		        var model = $("#table_list_transFlow").jqGrid('getRowData', id);
		        var pageNo = $("#table_list_transFlow").jqGrid('getGridParam','page');
		        var pageSize = $("#table_list_transFlow").jqGrid('getGridParam','rowNum');
		        var sortname = $("#table_list_transFlow").jqGrid('getGridParam','sortname');
		        var sortorder = $("#table_list_transFlow").jqGrid('getGridParam','sortorder');
		        
				var queryParamsObject = {}; 
				getParams(queryParamsObject);
				queryParamsObject.pageNo = pageNo;
				queryParamsObject.pageSize = pageSize;
				queryParamsObject.sortname = sortname;
				queryParamsObject.sortorder = sortorder;
				//console.info(queryParamsObject);
				var queryParams = $.param(queryParamsObject);
				var encodeQueryParams = $.base64.encode(queryParams);
				//console.info($.base64.decode(encodeQueryParams));
		        <%--location.href='${ctx}/reportAction/toTransFlowDetail.do?id='+id+"&queryParams="+encodeQueryParams;--%>
		        <%--location.href='${ctx}/reportAction/toTransFlowDetail.do?id='+id+"&queryParams="+encodeQueryParams;--%>
				openNewWindow('${ctx}/reportAction/toTransFlowDetail.do',{"id":id})
		    }
	function openNewWindow(url,param){
		var $form = $('<form method="get" action="'+url+'" target="_blank"></form>');
		if(typeof(param) == "object"){
			var tmp = [];
			for(var key in param){
				tmp.push('<textarea name="'+key+'">'+param[key]+'</textarea>');
			}
			$form.append(tmp.join(" "));
		}
		$form.appendTo('body');
		setTimeout(function(){
			$form.submit();
		},10);

	}
			function toRecordAccountFlow(id){
				var model = $("#table_list_transFlow").jqGrid('getRowData', id);
		        var pageNo = $("#table_list_transFlow").jqGrid('getGridParam','page');
		        var pageSize = $("#table_list_transFlow").jqGrid('getGridParam','rowNum');
		        var sortname = $("#table_list_transFlow").jqGrid('getGridParam','sortname');
		        var sortorder = $("#table_list_transFlow").jqGrid('getGridParam','sortorder');
		        
				var queryParamsObject = {}; 
				getParams(queryParamsObject);
				queryParamsObject.pageNo = pageNo;
				queryParamsObject.pageSize = pageSize;
				queryParamsObject.sortname = sortname;
				queryParamsObject.sortorder = sortorder;
				//console.info(queryParamsObject);
				var queryParams = $.param(queryParamsObject);
				queryParams = decodeURIComponent(queryParams);
				var encodeQueryParams = $.base64.encode(queryParams);
				//console.info($.base64.decode(encodeQueryParams));
		        location.href='${ctx}/reportAction/toRecordAccountFlow.do?id='+id+"&queryParams="+encodeQueryParams;
			}
			
			$(document).ready(function() {
						var lastsel;
			            // 初始化表格
			            $("#table_list_transFlow").jqGrid({
			            	url:"${ctx}/reportAction/transFlow.do",
			                datatype: "local",
			                mtype: "POST",
			                height:"auto",
			                autowidth: true,
			                shrinkToFit: false,
			                autoScroll: false,
			                page: ${params.pageNo},
			                rowNum: ${params.pageSize},
			                sortname :'${params.sortname}',
			                sortorder:'${params.sortorder}',
			                rowList: [10, 20],
 			                colNames:['操作','记账流水号','记账标志','记账时间','交易类型','来源系统','来源系统流水号','交易订单号','交易日期','交易金额','商户编号','直属代理商编号','交易卡号','收单机构（交易通道）','收单机构（出款通道）','冲销标志','冲销状态','记账结果'],		               		
 			                colModel: [
 			                   {name:'Detail',index:'Id',width:150,align:"center",sortable:false,frozen:true},
			                   {name: 'recordSerialNo', index: 'recordSerialNo',  width: 200,align: "left", sortable:false},
			                   {name: 'recordStatus', index: 'recordStatus',width: 100, align: "left", sortable:false,formatter:  customRecordStatusFmatter},
			                   {name: 'recordDate', index: 'recordDate', width: 210 ,align: "left",
			                	   formatter : function(val) {
										return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");
									}},
					           {name: 'transType', index: 'transType',width: 180, align: "left", sortable:false,formatter:  customTransTypeFmatter},
			                   {name: 'fromSystem', index: 'fromSystem',width: 200, align: "center", sortable:false,formatter:  customFromSystemFmatter},
			                   {name: 'fromSerialNo', index: 'fromSerialNo',width: 200, align: "left", sortable:false},
			                   {name: 'transOrderNo', index: 'transOrderNo',width: 200, align: "left", sortable:false},
			                   {name: 'transDate', index: 'transDate',  width: 200,align: "center",
			                	   formatter : function(val) {
										return myFormatDate(val,"yyyy-MM-dd");
									}},
			                   {name: 'transAmount', index: 'transAmount',width: 140, align: "right",formatter: 'number'},
			                   {name: 'merchantNo', index: 'merchantNo',width: 180, align: "center", sortable:false},
			                   {name: 'directAgentNo', index: 'directAgentNo',width: 200, align: "center", sortable:false},
			                   {name: 'cardNo', index: 'cardNo',width: 200, align: "left", sortable:false},
			                   {name: 'acqEnname', index: 'acqEnname',width: 200, align: "left", sortable:false},
			                   {name: 'outAcqEnname', index: 'outAcqEnname',width: 200, align: "left", sortable:false},
			                   {name: 'reverseFlag', index: 'reverseFlag',width: 100, align: "center", sortable:false,formatter:  customReverseFlagFmatter},
			                   {name: 'reverseStatus', index: 'reverseStatus',width: 100, align: "center", sortable:false, formatter:customReverseStatusFmatter},
			                   {name: 'recordResult', index: 'recordResult',width: 400, align: "center", sortable:false},
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
                        			var detail = "<a href='javascript:void(0);' onclick='Detail(" + id + ")' class='default-details' title='详情' data-toggle='tooltip' data-placement='right' >详情</a>";
                        			detail += "&nbsp;&nbsp;<a href='javascript:void(0);' onclick='toRecordAccountFlow(" + id + ")' class='default-details' title='记账分录' data-toggle='tooltip' data-placement='right'>记账分录</a>";
                        			
                        			//var recordStatusText = "<button type='button' class='btn btn-default' data-toggle='tooltip' data-placement='right' title='Tooltip on top'>Tooltip on top</button>";
                        			jQuery("#table_list_transFlow").jqGrid('setRowData', ids[i], { Detail: detail});
                    				//var curRowData = $("#table_list_transFlow").jqGrid('getRowData', ids[i]);  
                    				//console.info(curRowData);
                    			}
                    			$('[data-toggle="tooltip"]').tooltip();
                    			setTimeout("hackHeight()", 150 );//设置不动列的宽度适应
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
					  var colltext = '${recordStatus.htmlName}';
					  return colltext;
				  }
			 	</c:forEach>	
			 	
				return "" ;
			}
			
			function customReverseFlagFmatter(cellvalue, options, rowObject){  
				<c:forEach var="reverseFlag" items="${reverseFlagList}">
				  if(cellvalue == '${reverseFlag.sysValue}'){
					  return '${reverseFlag.sysName}';
				  }
			 	</c:forEach>	
				return "" ;
			}
			
			function customReverseStatusFmatter(cellvalue, options, rowObject){  
				<c:forEach var="reverseStatus" items="${reverseStatusList}">
				  if(cellvalue == '${reverseStatus.sysValue}'){
					  return '${reverseStatus.sysName}';
				  }
			 	</c:forEach>	
				return "" ;
			}
			
			function customFromSystemFmatter(cellvalue, options, rowObject){  
				<c:forEach var="fromSystem" items="${fromSystemList}">
				  if(cellvalue == '${fromSystem.sysValue}'){
					  return '${fromSystem.sysName}';
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
		</script>
	</title>
	
   </body>