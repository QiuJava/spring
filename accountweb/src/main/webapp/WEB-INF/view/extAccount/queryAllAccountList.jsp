<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<!-- jqGrid plugin -->
	<link href="${ctx}/css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/jqGrid/ui.jqgrid.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/select2/select2.min.css" rel="stylesheet">
    <style type="text/css">
        .ui-jqgrid tr.jqgrow td   
        {  
            /* jqGrid cell content wrap  */  
            white-space: normal !important;  
                height: 50px;  
        }  
          
        th.ui-th-column div  
        {  
            /* jqGrid columns name wrap  */  
            white-space:normal !important;  
            height:auto !important;  
            /*padding:0px;  */
        } 
        #jqgh_table_list_extAccountList_Detail {
            height: 100px !important;
            line-height: 100px;
        }
    </style>
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">外部账号管理</div>
            <em class=""></em>
            <div class="pull-left active">查询用户所有账户列表</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content bbb" >
					 <form class="form-horizontal" id="extAccountForm">
						<div class="form-group">
							<label class="col-sm-2 control-label">科目编号：</label>
							<!-- <div class="pull-left control-label labeldiv"></div> -->
							<div class="col-sm-2">
								<select id="subjectNo" autocomplete="off" class="form-control"
									name="subjectNo">
									<option value="ALL" selected="selected">全部</option>
                                     		<c:forEach var="subject" items="${subjectList}">
                                                <option value="${subject.subjectNo}"
                                                <c:if test="${subject.subjectNo == params.subjectNo}">selected="selected"</c:if>>
                                                ${subject.subjectNo}(${subject.subjectName})</option>
                                            </c:forEach>
									</select>
							</div>
							<label class="col-sm-2 control-label">用户类别：</label>
							<!-- <div class="pull-left control-label labeldiv" style="width:62px"></div> -->
							<div class="col-sm-2">
								<select class="form-control" name="extAccountInfo.accountType">
									<option value="ALL" selected="selected">全部</option>
									<c:forEach var="accountType" items="${accountTypeList}">
										<option value="${accountType.sysValue}"
											<c:if test="${accountType.sysValue == params['extAccountInfo.accountType']}">selected="selected"</c:if>>
											${accountType.sysName}</option>
									</c:forEach>
								</select>
							</div>
							<label class="col-sm-2 control-label">商户/代理商等编号:</label>
							<!-- <div class="pull-left control-label labeldiv"></div> -->
							<div class="col-sm-2">
								<input type="text" class="form-control"
									name="extAccountInfo.userId" value="${params['extAccountInfo.userId']}">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">商户/代理商等名称：</label>
							<div class="col-sm-2">
								<input type="text" class="form-control"
									name="extAccountInfo.userName" value="${params['extAccountInfo.userName']}">
							</div>
							<label class="col-sm-2 control-label">手机号：</label>
							<div class="col-sm-2">
								<input type="text" class="form-control"
									name="extAccountInfo.mobilephone"
									onkeyup="this.value=this.value.replace(/\D/g,'')"
									onafterpaste="this.value=this.value.replace(/\D/g,'')"  value="${params['extAccountInfo.mobilephone']}">
							</div>
							<label class="col-sm-2 control-label">账号：</label>
							<div class="col-sm-2">
								<input type="text" class="form-control" name="accountNo"  value="${params['accountNo']}">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">账户状态：</label>
							<div class="col-sm-2">
								<select class="form-control" name="accountStatus">
									<option value="ALL" selected="selected">全部</option>
									<c:forEach var="accountStatus" items="${accountStatusList}">
										<option value="${accountStatus.sysValue}"
											<c:if test="${accountStatus.sysValue == params.accountStatus}">selected="selected"</c:if>>
											${accountStatus.sysName}</option>
									</c:forEach>
								</select>
							</div>
							<label class="col-sm-2 control-label">币种号：</label>
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
							<label class="col-sm-2 control-label">账号归属：</label>
							<div class="col-sm-2">
								<select class="form-control" name="orgNo">
									<option value="ALL" selected="selected">全部</option>
									<c:forEach var="orgInfo" items="${orgInfoList}">
										<option value="${orgInfo.orgNo}"
										<c:if test="${orgInfo.orgNo == params.orgNo}">selected="selected"</c:if>>
											${orgInfo.orgName}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">冻结金额：</label>
							<div class="col-sm-2">
								<div class=" input-group" id="freezeAmount">
									<input type="text" class="form-control"
										name="controlBeginAmount" id="controlBeginAmount" onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" 
										value="${params['controlBeginAmount']}"/> <span
										class="input-group-addon">~</span> <input type="text"
										class="form-control" name="controlEndAmount"
										id="controlEndAmount" onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" 
										value="${params['controlEndAmount']}"/>
								</div>
							</div>
							<label class="col-sm-2 control-label">余额：</label>
							<div class="col-sm-2">
								<div class=" input-group" id="currBalanceAmount">
									<input type="text" class="form-control"
										name="currBalanceBeginAmount" id="currBalanceBeginAmount" onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" 
										value="${params['currBalanceBeginAmount']}"/> <span
										class="input-group-addon">~</span> <input type="text"
										class="form-control" name="currBalanceEndAmount"
										id="currBalanceEndAmount" onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')" 
										value="${params['currBalanceEndAmount']}"/>
								</div>
							</div>
						</div>


						<div class="clearfix lastbottom"></div>

						<div class="form-group">
							<!-- <div class="col-sm-12 col-sm-offset-13  "> -->
							<label class="col-sm-2 control-label aaa"></label> <input
								type="hidden" name="${_csrf.parameterName}"
								value="${_csrf.token}" />
							<button class="btn btn-success" type="submit">
								<span class="glyphicon gly-search"></span>查询
							</button>
							<button class="btn btn-default col-sm-offset-14" type="reset"
								id="reset">
								<span class="glyphicon gly-trash"></span>清空
							</button>
							<!-- </div> -->
						</div>
					</form>
                    <%-- </form:form> --%>
                    
                    
                            
                            <div class="modal inmodal" id="myModalUpdateStatus" tabindex="-1" role="dialog" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                            <h5 class="modal-title">修改状态</h5>
                                        </div>
                                        <div class="modal-body">
                                           	<form method="post" class="form-horizontal" id="updateStatusForm">
												<div class="form-group">
													<label class="col-sm-3 control-label">账号：</label>
													<div class="col-sm-8">
														<p id="accountNo" style="padding-top:7px"></p>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-3 control-label">账户状态：</label>
													<div class="col-sm-8">
														<select class="form-control"  name="accountStatus" id = "accountStatus" > 
															<c:forEach var="accountStatus" items="${accountStatusList}">
																<option value="${accountStatus.sysValue}">
																${accountStatus.sysName}
																</option>
															</c:forEach>
														</select> 
													</div>
												</div>
												<!-- <div class="form-group">
													<label class="col-sm-3 control-label">结算保留金额：</label>
													<div class="col-sm-8">
														<input type="number" class="form-control"  name="settlingHoldAmount"  id="settlingHoldAmount">
													</div>
												</div> -->
											</form>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                                            <sec:authorize access="hasAuthority('queryAllAccountList:update')">  
                                            	<button type="button" class="btn btn-success" onclick="saveStatus()">保存</button>
                                            </sec:authorize>
                                            <!-- <button type="button" class="btn btn-primary" onclick='$("#myModal2").modal("hide")'>myclose</button> -->
                                        </div>
                                    </div>
                                </div>
                            </div>
					
                    <div class="modal inmodal" id="myModalUpdateSettlingHoldAmount" tabindex="-1" role="dialog" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                                            <h5 class="modal-title">修改结算保留金额</h5>
                                        </div>
                                        <div class="modal-body">
                                           	<form method="post" class="form-horizontal" id="updateSettlingHoldAmountForm">
												<div class="form-group">
													<label class="col-sm-3 control-label">账号：</label>
													<div class="col-sm-8">
														<p style="padding-top:7px"></p>
													</div>
												</div>
												<div class="form-group">
													<label class="col-sm-3 control-label">结算保留金额：</label>
													<div class="col-sm-8">
														<input type="number" class="form-control"  name="settlingHoldAmount"  id="settlingHoldAmount">
													</div>
												</div>
											</form>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                                            <sec:authorize access="hasAuthority('queryAllAccountList:updateSettlingHoldAmount')">  
                                            	<button type="button" class="btn btn-success" onclick="saveSettlingHoldAmount()">保存</button>
                                            </sec:authorize>
                                            <!-- <button type="button" class="btn btn-primary" onclick='$("#myModal2").modal("hide")'>myclose</button> -->
                                        </div>
                                    </div>
                                </div>
                            </div>
				</div>
			</div>
		</div>
				<div class="col-lg-12">
						<div class="ibox ">
							<div class="ibox-content">
								<div class="jqGrid_wrapper">
								<table id="table_list_extAccountList"></table>
								<div id="pager_list_extAccountList"></div>
								</div>
							</div>
						</div>
				</div>
			
	</div>
	
	
	
   </body>
   <title>
   <script src="${ctx}/js/plugins/select2/select2.full.min.js"></script>
	<script src="${ctx}/js/plugins/select2/i18n/zh-CN.js"></script>
	<script type="text/javascript">
    // 去除空格啊
        $('input').blur(function(){
            replaceSpace(this);
        })
        function replaceSpace(obj){
            obj.value = obj.value.replace(/\s/gi,'')
        }
		//var $exampleMulti = $("#subjectNo").select2();
        $("#reset").on("click", function () { 
            //$exampleMulti.val(null).trigger("change");
            $('#subjectNo').select2("val", "ALL");
            //$("#subjectNo").empty();
        });
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
			function Detail(id) {   //单击修改链接的操作         
		        var model = $("#table_list_extAccountList").jqGrid('getRowData', id);
		        var pageNo = $("#table_list_extAccountList").jqGrid('getGridParam','page');
		        var pageSize = $("#table_list_extAccountList").jqGrid('getGridParam','rowNum');
		        var sortname = $("#table_list_extAccountList").jqGrid('getGridParam','sortname');
		        var sortorder = $("#table_list_extAccountList").jqGrid('getGridParam','sortorder');
		        
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
		        location.href='${ctx}/extAccountAction/extAccountDetailQuery.do?forwardTo=1&accountNo='+accountNo+"&queryParams="+encodeQueryParams;
		    }
			var currentAccountNo = "";
			function ModifyStatus(id) {   //单击修改状态链接的操作         
		        var model = $("#table_list_extAccountList").jqGrid('getRowData', id);
		        currentAccountNo = model.accountNo;
		        $("#updateStatusForm > .form-group p").text(model.accountNo);
				$("#accountNo").text(model.accountNo);
				$("#accountStatus").val(model.accountStatus); 
			}
			
			
			function ModifySettlingHoldAmount(id) {   //单击修改状态链接的操作         
		        var model = $("#table_list_extAccountList").jqGrid('getRowData', id);
		        currentAccountNo = model.accountNo;
				$("#updateSettlingHoldAmountForm > .form-group p").text(model.accountNo);
				$("#settlingHoldAmount").val(model.settlingHoldAmount);
			}
			
			
			
			function saveStatus(id) {    
				var _accountNo = currentAccountNo;
				var _accountStatus = $("#accountStatus").val();
				//var _settlingHoldAmount = $("#settlingHoldAmount").val();
				//console.info(_settlingHoldAmount);
				$.post('${ctx}/extAccountAction/extAccountStatusUpdate.do', 
						{ accountNo:_accountNo,accountStatus:_accountStatus,'${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) {
							if(!msg.status){
								//alert(msg.msg);
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
								$("#myModalUpdateStatus").modal("hide");
								
								$("#table_list_extAccountList").jqGrid('setGridParam',{
							           url : "${ctx}/extAccountAction/findAllAccountInfo.do",
							           datatype : 'json',
							        }).trigger('reloadGrid');//重新载入
							}
						});
			}
			
			function saveSettlingHoldAmount(id) {    
				var _accountNo = currentAccountNo;
				var _settlingHoldAmount = $("#settlingHoldAmount").val();
				//console.info(_settlingHoldAmount);
				$.post('${ctx}/extAccountAction/extAccountSettlingHoldAmountUpdate.do', 
						{ accountNo:_accountNo,settlingHoldAmount:_settlingHoldAmount,'${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) {
							if(!msg.status){
								//alert(msg.msg);
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
								$("#myModalUpdateSettlingHoldAmount").modal("hide");
								
								$("#table_list_extAccountList").jqGrid('setGridParam',{
							           url : "${ctx}/extAccountAction/findAllAccountInfo.do",
							           datatype : 'json',
							        }).trigger('reloadGrid');//重新载入
							}
						});
			} 
			
			$("#extAccountForm").submit(function(){
				$("#table_list_extAccountList").setGridParam({
				       datatype : 'json',
				       page : 1            //Replace the '1' here
				    }).trigger("reloadGrid");
				return false;
			});
			
			function getParams(o){
				//console.info(o);
				var data=$("#extAccountForm").serializeArray();
			     $.each(data, function() {    
			             o[this.name] = this.value || '';
			     });   
	             //o.subjectNo = $("#subjectNo").select2("val");
				 //console.info(o);
			}
			function customAccountTypeFmatter(cellvalue, options, rowObject){  
				<c:forEach var="accountType" items="${accountTypeList}">
					  if(cellvalue == '${accountType.sysValue}'){
						  return '${accountType.sysName}';
					  }
				 </c:forEach>	
				 return "" ;
			}
			function customCurrencyFmatter(cellvalue, options, rowObject){  
				<c:forEach var="currency" items="${currencyList}">
					  if(cellvalue == '${currency.currencyNo}'){
						  return '${currency.currencyName}';
					  }
				 </c:forEach>	
				 return "" ;
			}
			function customOrgFmatter(cellvalue, options, rowObject){  
				<c:forEach var="orgInfo" items="${orgInfoList}">
					  if(cellvalue == '${orgInfo.orgNo}'){
						  return '${orgInfo.orgName}';
					  }
				 </c:forEach>	
				 return "" ;
			}
			/* function customAccountStatusFmatter(cellvalue, options, rowObject){  
				<c:forEach var="accountStatus" items="${accountStatusList}">
					  if(cellvalue == '${accountStatus.sysValue}'){
						  return '${accountStatus.sysName}';
					  }
				 </c:forEach>	
			} */
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
			            $("#table_list_extAccountList").jqGrid({
			            	url:"${ctx}/extAccountAction/findAllAccountInfo.do",
			                datatype: "local",
			                mtype: "POST",
			                height:"auto",
			                autowidth: true,
			                shrinkToFit:false,
			                autoScroll: false,
			                page: ${params.pageNo},
			                rowNum: ${params.pageSize},
			                sortname :'${params.sortname}',
			                sortorder:'${params.sortorder}',
			                rowList: [10, 20],
			                colNames:['操作','账号','账号名称','状态','用户类别','商户/代理商等编号','商户/代理商等名称','手机号','科目编号','科目名称','币种号','账户归属','余额A<br>(B+C+D)','可用余额B<br>(A-C-D)','结算中金额C','冻结金额D','预冻结金额','结算保留金额','开户时间','创建人'],
			                colModel: [
					                	{name:'Detail',index:'Id',width:200,align:"center",sortable:false, frozen : true},
					            {name: 'accountNo', index: 'accountNo',width: 260, align: "left", sortable:false},
					            {name: 'accountName', index: 'accountName',width: 200, align: "left", sortable:false},
			                    {name: 'accountStatus', index: 'accountStatus',width: 120, align: "center", sortable:false,  formatter: "select", edittype:"select",editoptions: { value: getAccountStatusGroups() },editable: false},
			                    {name: 'accountType', index: 'accountType', width: 100 , align: "center", sortable:false, formatter:  customAccountTypeFmatter},
			                    {name: 'userId', index: 'userId',  width: 220, align: "left", sortable:false},
			                    {name: 'userName', index: 'userName',  width: 180, align: "left", sortable:false},
			                    {name: 'mobilephone', index: 'mobilephone',  width: 120, align: "left", sortable:false},
			                    {name: 'subjectNo', index: 'subjectNo',width: 120, align: "left", sortable:false},
			                    {name: 'subjectName', index: 'subjectName',width: 200, align: "left", sortable:false},
			                    {name: 'currencyNo', index: 'currencyNo',width: 80, align: "center", sortable:false, formatter:  customCurrencyFmatter},
			                    {name: 'orgNo', index: 'orgNo',width: 120, align: "center", sortable:false, formatter:  customOrgFmatter},
			                    {name: 'currBalance', index: 'currBalance', width: 100, align: "right", sorttype: "int",formatter: 'number'},
			                    {name: 'availBalance', index: 'availBalance',width: 100, align: "right",formatter: 'number'},
			                    {name: 'settlingAmount', index: 'settlingAmount',width: 100, align: "right",formatter: 'number'},
			                    {name: 'controlAmount', index: 'controlAmount',width: 100, align: "right",formatter: 'number'},
			                    {name: 'preFreezeAmount', index: 'preFreezeAmount',width: 100, align: "right",formatter: 'number'},
			                    {name: 'settlingHoldAmount', index: 'settlingHoldAmount',width: 100, align: "right",formatter: 'number'},
			                    {name: 'createTime', index: 'createTime',width: 150, align: "center",
									formatter : function(val) {return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
								{name: 'creator', index: 'creator',width: 120, align: "left"},
			                ],
			                /* onSelectRow: function(id){
			            		if(id && id!==lastsel){
			            			jQuery('#table_list_extAccountList').jqGrid('restoreRow',lastsel);
			            			jQuery('#table_list_extAccountList').jqGrid('editRow',id,true);
			            			lastsel=id;
			            			$("#m"+id).hide();
			        				$("#s"+id+",#c"+id+"").show();
			            		}
			            	}, */
			                multiselect: false,//支持多项选择
			                pager: "#pager_list_extAccountList",
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
			                    var ids=$("#table_list_extAccountList").jqGrid('getDataIDs');
			                    for(var i=0; i<ids.length; i++){
			                        var id=ids[i];  
			                        var getRow = $('#table_list_extAccountList').getRowData(id);//获取当前的数据行
			                        var accountType = getRow.accountType;
			                        var detail = "" ;
			                        detail += "<a href='javascript:void(0);' class='default-details' title='账号明细' onclick='Detail(" + id + ")'>账号明细</a>";
			                        //20161220需求隐藏改元素
			                        /* <sec:authorize access="hasAuthority('queryAllAccountList:update')">  
			                            detail += "&nbsp;<a href='#' class='default-delete' data-toggle='modal' data-target='#myModalUpdateStatus' title='修改状态'  style='background: #9dcf9d;cursor:not-allowed;' onclick='ModifyStatus(" + id + ")' id='m"+id+"'>修改状态</a>";
			                        </sec:authorize>
			                        */
		                       	    <sec:authorize access="hasAuthority('queryAllAccountList:updateSettlingHoldAmount')">
		                       		if("收单机构" == getRow.accountType) 
		                        		detail += "&nbsp;<a href='#' style='width:110px' class='default-delete' data-toggle='modal' data-target='#myModalUpdateSettlingHoldAmount' title='修改结算保留金额' onclick='ModifySettlingHoldAmount(" + id + ")' id='h"+id+"'>修改结算保留金额</a>";
		                       		</sec:authorize>
		                       		
			                        jQuery("#table_list_extAccountList").jqGrid('setRowData', ids[i], { Detail: detail });
			                    }
			                }
			            });
						jQuery("#table_list_extAccountList").jqGrid('setFrozenColumns');
			            // Add responsive to jqGrid
			            $(window).bind('resize', function () {
			                var width = $('.jqGrid_wrapper').width();
			                $('#table_list_extAccountList').setGridWidth(width);
			            });
			});
			</script>
		
	</title>
	</html>