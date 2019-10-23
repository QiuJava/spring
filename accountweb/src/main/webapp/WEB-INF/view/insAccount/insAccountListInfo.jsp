
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
	<link href="${ctx}/css/plugins/select2/select2.min.css" rel="stylesheet">
	
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10" >
			<div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">内部账号管理</div>
            <em class=""></em>
            <div class="pull-left active">内部账户多条件查询</div>
		</div>
	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<!-- <div class="ibox-title">
					<div class="ibox-tools">
						<a class="collapse-link"> <i class="fa fa-chevron-up"></i></a> 
					</div>
				</div> -->
				<div class="ibox-content">
					 <form class="form-horizontal" id="insAccountForm">
					 <div class="form-group">
					 		<label class="col-sm-2 control-label">科目编号:</label>
									<div class="col-sm-2">
                                   <select id="subjectNo" autocomplete="off" class="form-control" name="subjectNo">
                                   			<option value="ALL" selected="selected">全部</option>
                                     		<c:forEach var="subject" items="${subjectList}">
                                                <option value="${subject.subjectNo}"
                                                <c:if test="${subject.subjectNo == params.subjectNo}">selected="selected"</c:if>>
                                                ${subject.subjectNo}(${subject.subjectName})</option>
                                            </c:forEach>
									</select>
								
								</div>
								<label class="col-sm-2 control-label">账号:</label>
                                   <div class="col-sm-2"><input type="text" class="form-control" name="accountNo" value="${params.accountNo}"></div>
                                <label class="col-sm-2 control-label">机构号:</label>
                                   <div class="col-sm-2">
                                   	 <select class="form-control" name="orgNo"  > 
											<option value="ALL" selected="selected">全部</option>
	   										<c:forEach var="orgInfo" items="${orgInfoList}">
												<option value="${orgInfo.orgNo}"
												<c:if test="${orgInfo.orgNo == params.orgNo}">selected="selected"</c:if>>
												${orgInfo.orgName}  ${orgInfo.orgNo}</option>
											</c:forEach>
									</select>
                                   </div>
					 </div>
					 
                            <div class="form-group">
                                   	<label class="col-sm-2 control-label" >币种号:</label>
                                   <div class="col-sm-2">
											<select class="form-control" name="currencyNo"> 
												<option value="ALL" selected="selected">全部</option>
										         <c:forEach var="currency" items="${currencyList}">
													<option value="${currency.currencyNo}"
													<c:if test="${currency.currencyNo == params.currencyNo}">selected="selected"</c:if>>
													${currency.currencyName}</option>
												</c:forEach>
											</select>                                     
                                   </div>
                                   <label class="col-sm-2 control-label">账户名称:</label>
                                   <div class="col-sm-2"><input type="text" class="form-control"  name="accountName" value="${params.accountName}"></div>
                                   <label class="col-sm-2 control-label" >账户状态:</label>
									<div class="col-sm-2">
   										<select class="form-control"  name="accountStatus" > 
   											 <option value="ALL" selected="selected">全部</option>
   											 <c:forEach var="accountStatus" items="${accountStatusList}">
												<option value="${accountStatus.sysValue}"
												<c:if test="${accountStatus.sysValue == params.accountStatus}">selected="selected"</c:if>>
												${accountStatus.sysName}</option>
											</c:forEach> 
									       
										</select>                       
									</div>					
						</div>
						
                          
                                <div class="clearfix lastbottom"></div>
                                
                                   <div class="form-group">
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                   		<label class="col-sm-2 control-label aaa"></label>
                                   	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                       <button id="submitBut" type="submit" class=" btn btn-success " value="" ><span class="glyphicon gly-search"></span>查询</button>
                                       <button class="btn btn-default  col-sm-offset-14" type="reset" id="reset"><span class="glyphicon gly-trash"></span>清空</button>
                                		<sec:authorize access="hasAuthority('insAccountListInfo:export')">       
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
								<table id="table_list_insAccountList"></table>
								<div id="pager_list_insAccountList"></div>
								</div>
							</div>
						</div>
				</div>
			
	</div>
	
	<title>
	<script src="${ctx}/js/plugins/select2/select2.full.min.js"></script>
	<script src="${ctx}/js/plugins/select2/i18n/zh-CN.js"></script>
	<script type="text/javascript">
		//var $exampleMulti = $("#subjectNo").select2();
        $("#reset").on("click", function () { 
            //$exampleMulti.val(null).trigger("change");
            $('#subjectNo').select2("val", "ALL");
            //$("#subjectNo").empty();
        });
        // 去除空格啊
        $('input').blur(function(){
            replaceSpace(this);
        })
        function replaceSpace(obj){
            obj.value = obj.value.replace(/\s/gi,'')
        }
	function formatRepo (repo) {
        if (repo.loading) return repo.text;
		//console.info(repo.id);
		return repo.id+'('+repo.text+')';  
      }

      function formatRepoSelection (repo) {
    	  //console.info("formatRepoSelection:"+ repo.text);
    	  return repo.text;  
        //return repo.full_name || repo.text;
      }
      
      function parseSelectParams(params){
    	  if(params && params.term){
    		  return encodeURI(params.term);
    	  }
    	  else
    		  return null;
      }
	
      function matchStart (term, text, option) {
    	  //console.info(option.id)
    	  if (text.toUpperCase().indexOf(term.toUpperCase()) >= 0) {
    	    	return true;
    	  }
    	  if (option.id.toUpperCase().indexOf(term.toUpperCase())>=0) {
	    	    return true;
	      }
    	  return false;
    	}

    	$.fn.select2.amd.require(['select2/compat/matcher'], function (oldMatcher) {
    	  $('#subjectNo').select2({
    	    matcher: oldMatcher(matchStart)
    	  })
    	  
    	  //$('#parentSubjectNo').val("${params.parentSubjectNo}");
    	  var len = $('#subjectNo option:selected').length;
          if (len == 0) {
            $('#select2-parentSubjectNo-container').text('')
          };
    	});
	  </script>
			<script type="text/javascript">

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
			        /* jQuery('<form action="'+ url +'" method="'+ (method||'post') +'">'+inputs+'</form>')
			        .appendTo('body').submit().remove(); */
			        jQuery('<form action="'+ url +'" method="'+ (method||'post') +'">'+inputs+'</form>')
			        .appendTo('body').submit().remove();
			    };
			};	
	   		$("#insAccountForm").submit(function(){
				$("#table_list_insAccountList").setGridParam({
				       datatype : 'json',
				       page : 1            //Replace the '1' here
				    }).trigger("reloadGrid");
				return false;
			});
	   		/*表单提交时的处理*/
	   		function exportExcel() {  
	   			var data = $("#insAccountForm").serialize();
	   			//data = decodeURIComponent(data,true);
	   			//console.info(data);
	   			$.download('${ctx}/insAccountAction/exportInsAccount.do',data,'post');
	   			//$.download('#',data,'post');
	   	    }
	   		
			
	   		
			function customBalanceFromFmatter(cellvalue, options, rowObject){  
				<c:forEach var="balanceFrom" items="${balanceFromList}">
					  if(cellvalue == '${balanceFrom.sysValue}'){
						  return '${balanceFrom.sysName}';
					  }
				 </c:forEach>
				 return "" ;
			}   	
			
			
			function getParams(o){
				var data=$("#insAccountForm").serializeArray();
				
			     $.each(data, function() {   
			             o[this.name] = this.value || '';    
			     });  
			     //o['subjectNo'] = $("#subjectNo").select2("val");

			}
			
			function customAccountStatusFmatter(cellvalue, options, rowObject){  
				<c:forEach var="accountStatus" items="${accountStatusList}">
					  if(cellvalue == '${accountStatus.sysValue}'){
						  return '${accountStatus.sysName}';
					  }
				 </c:forEach>
				 return "" ;
			}
				 
			$(document).ready(function() {
						var lastsel;
			            // 初始化表格
			            $("#table_list_insAccountList").jqGrid({
			            	url:"${ctx}/insAccountAction/findInsAccountListInfo.do",
			                datatype: "json",
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
 			                colNames:['操作','账号','科目编号','科目名称','币种号','账户名称','余额','可用余额','余额借贷方向','账户状态','开户时间','创建人'],		               		
 			                colModel: [
 			                	{name:'Detail',index:'Id',width:70,align:"center",sortable:false, frozen : true},
			                   {name: 'accountNo', index: 'accountNo', width: 280 ,align: "left", sortable:false},
			                   {name: 'subjectNo', index: 'subjectNo',  width: 150,align: "left", sortable:false},
			                   {name: 'subject.subjectName', index: 'subject.subjectName',width: 200, align: "left", sortable:false},
			                   {name: 'currencyNo', index: 'currencyNo',  width: 120,align: "center", sortable:false,formatter:  customCurrencyFmatter},
			                   {name: 'accountName', index: 'accountName',width: 200, align: "left", sortable:false},
			                   {name: 'currBalance', index: 'currBalance',width: 120, align: "right",formatter: 'number'},
			                   {name: 'availBalance', index: 'availBalance',width: 120, align: "right",formatter: 'number'},
			                   {name: 'balanceFrom', index: 'balanceFrom',width: 150, align: "center", sortable:false,formatter: customBalanceFromFmatter},
			                   {name: 'accountStatus', index: 'accountStatus',width: 120, align: "center", sortable:false,formatter:  customAccountStatusFmatter},
			                   {name: 'createTime', index: 'createTime',width: 170, align: "center", formatter : function(val) {return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
			                   {name: 'creator', index: 'creator',width: 100, align: "left"},
			                ],
			                onSelectRow: function(id){
			            		if(id && id!==lastsel){
			            			jQuery('#table_list_insAccountList').jqGrid('restoreRow',lastsel);
			            			jQuery('#table_list_insAccountList').jqGrid('editRow',id,true);
			            			lastsel=id;
			            		}
			            	},
			            	
			               // multiselect: true,//支持多项选择
			                pager: "#pager_list_insAccountList",
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
			                gridComplete:function(){  //在此事件中循环为每一行添加修改和删除链接
			                    var ids=$("#table_list_insAccountList").jqGrid('getDataIDs');
			                    for(var i=0; i<ids.length; i++){
			                        var id=ids[i];   
			                        var detail = "" ;
			                        <sec:authorize access="hasAuthority('insAccountListInfo:detail')"> 
			                        detail += "<a href='javascript:void(0);' class='default-details'  title='交易明细' onclick='Detail(" + id + ")'>交易明细</a>";
			                        </sec:authorize>
			                        jQuery("#table_list_insAccountList").jqGrid('setRowData', ids[i], { Detail: detail });
			                    }
			                },
			            });
						jQuery("#table_list_insAccountList").jqGrid('setFrozenColumns');
			            // Add responsive to jqGrid
			            $(window).bind('resize', function () {
			                var width = $('.jqGrid_wrapper').width();
			                $('#table_list_insAccountList').setGridWidth(width);
			            });
			});
			function Detail(id) {   //单击修改链接的操作         
		        var model = $("#table_list_insAccountList").jqGrid('getRowData', id);
		        var pageNo = $("#table_list_insAccountList").jqGrid('getGridParam','page');
		        var pageSize = $("#table_list_insAccountList").jqGrid('getGridParam','rowNum');
		        var sortname = $("#table_list_insAccountList").jqGrid('getGridParam','sortname');
		        var sortorder = $("#table_list_insAccountList").jqGrid('getGridParam','sortorder');
				var accountNo = model.accountNo;
				
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
		        location.href='${ctx}/insAccountAction/insAccountDetailQuery.do?forwardTo=1&accountNo='+accountNo+"&queryParams="+encodeQueryParams;
			}
			function customCurrencyFmatter(cellvalue, options, rowObject){  
				<c:forEach var="currency" items="${currencyList}">
					  if(cellvalue == '${currency.currencyNo}'){
						  return '${currency.currencyName}';
					  }
				 </c:forEach>	
				 return "" ;
			}
			
		</script>
	</title>
	
   </body>