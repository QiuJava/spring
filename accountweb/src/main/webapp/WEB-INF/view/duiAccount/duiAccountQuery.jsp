
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
	<link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">对账管理</div>
            <em class=""></em>
            <div class="pull-left active">对账信息查询</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="subjectForm">
					 
                          <div class="form-group">
                          		   <label class="col-sm-2 control-label">对账批次号：</label>
                            	   <div class="col-sm-3"><input type="text" class="form-control" name="checkBatchNo" id="checkBatchNo" value="${params.checkBatchNo}"></div>
                          		   
                          		   <label class="col-sm-2 control-label">收单机构：</label>
                                   <div class="col-sm-2">
										<select class="form-control" name="acqEnname" id="acqEnname"> 
									         <option value="ALL" selected="selected">全部</option>
											<c:forEach var="acqOrg" items="${acqOrgList}">
												<option value="${acqOrg.sysValue}"
													<c:if test="${acqOrg.sysValue == params.acqEnname}">selected="selected"</c:if>>
													${acqOrg.sysName}
												</option>
											</c:forEach>
										</select>      
									</div>		  														  
								 	
                        </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">创建时间:</label>
                                    <div class="col-sm-3">
                                     <!-- <input  type="text" class="form-control"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})" name="createTime"> -->
                                         <div class="input-group date">
                                            <span class="input-group-addon"><i class="fa fa-calendar"></i></span><input type="text" class="form-control"  name="createTime"  value="${params.createTime}">
                                        </div>
                                    
                                    </div>
                                    
                                    <label class="col-sm-2 control-label">对账结果：</label>
                                   <div class="col-sm-2">
										<select class="form-control" name="checkResult" id="checkResult"> 
									         <option value="ALL" selected="selected">全部</option>
											<c:forEach var="checkResult" items="${checkResultList}">
												<option value="${checkResult.sysValue}"
													<c:if test="${checkResult.sysValue == params.checkResult}">selected="selected"</c:if>>
													${checkResult.sysName}
												</option>
											</c:forEach>
										</select>      
									</div>	
                            </div>
                        
                          
                             <div class="clearfix lastbottom"></div>
                                
                                   <div class="form-group">
                                        <label class="col-sm-2 control-label aaa"></label>
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
									   <%-- <input type="hidden" name="pageNo1" value="${params.PageNo1}" /> --%>
                                    <button type="submit" class=" btn btn-success" value=""><span class="glyphicon gly-search"></span>查询</button>
                                    <button type="button" class=" btn btn-default col-sm-offset-14" value="" id="clear"><span class="glyphicon gly-trash"></span>清空</button>

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
								<table id="table_list_dui_account"></table>
								<div id="pager_list_dui_account"></div>
                                <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
								</div>
							</div>
						</div>
				</div>
			
	</div>
	
   </body>
    
	<title>
	<script src="${ctx}/js/plugins/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
	<script src="${ctx}/js/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js"></script>
	<script src="js/plugins/sweetalert/sweetalert.min.js"></script>
	<script type="text/javascript">
        // 去除空格啊
        $('input').blur(function(){
            replaceSpace(this);
        })

		$(document).ready(function() {
			$("#clear").click(function() {
				var objId = document.getElementById("subjectForm");
				for (var i=0;i<objId.elements.length;i++){
					if(objId.elements[i].type=="text"){
						objId.elements[i].value="";
					}
					if(objId.elements[i].type=="text"){
						objId.elements[i].value="";
					}else if(objId.elements[i].type=="select-one"){
						objId.elements[i].options[0].selected="false";
					}
				}
			});
		});



        function replaceSpace(obj){
            obj.value = obj.value.replace(/\s/gi,'')
        }
		//function Detail(checkBatchNo) {   //单击详情链接的操作         
	        //var model = $("#table_list_dui_account").jqGrid('getRowData', id);
			//var _fileName = model.name;
			//var _acqOrg = $('#acqEnname').val();
	      //  alert(checkBatchNo);
	       // location.href='${ctx}/duiAccountAction/toDuiAccountDetail2.do?checkBatchNo='+checkBatchNo;
	        
	    //}
		
		function Detail1(id) {   //单击修改链接的操作         
		        var model = $("#table_list_dui_account").jqGrid('getRowData', id);
		        var pageNo = $("#table_list_dui_account").jqGrid('getGridParam','page');
		        var pageSize = $("#table_list_dui_account").jqGrid('getGridParam','rowNum');
		        var sortname = $("#table_list_dui_account").jqGrid('getGridParam','sortname');
		        var sortorder = $("#table_list_dui_account").jqGrid('getGridParam','sortorder');
		        
		        var checkBatchNo = model.checkBatchNo;
		        
				var queryParamsObject = {}; 
				getParams(queryParamsObject);
				queryParamsObject.pageNo = pageNo;
				queryParamsObject.pageSize = pageSize;
				queryParamsObject.sortname = sortname;
				queryParamsObject.sortorder = sortorder;
				//queryParamsObject.pageNo1 = pageNo;
				var queryParams = $.param(queryParamsObject);
				var encodeQueryParams = $.base64.encode(queryParams);
		        <%--location.href='${ctx}/duiAccountAction/toDuiAccountDetailQuery.do?checkBatchNo='+checkBatchNo+"&forwardTo=1&queryParams="+encodeQueryParams;--%>
			openNewWindow('${ctx}/duiAccountAction/toDuiAccountDetailQuery.do',{"checkBatchNo":checkBatchNo})
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
		
		function Detail2(id) {   //单击修改链接的操作         
	        var model = $("#table_list_dui_account").jqGrid('getRowData', id);
	        var pageNo = $("#table_list_dui_account").jqGrid('getGridParam','page');
	        var pageSize = $("#table_list_dui_account").jqGrid('getGridParam','rowNum');
	        var sortname = $("#table_list_dui_account").jqGrid('getGridParam','sortname');
	        var sortorder = $("#table_list_dui_account").jqGrid('getGridParam','sortorder');
	        
	        var checkBatchNo = model.checkBatchNo;
	        var acqEnname = model.acqEnname;
	        
			var queryParamsObject = {}; 
			getParams(queryParamsObject);
			queryParamsObject.pageNo = pageNo;
			queryParamsObject.pageSize = pageSize;
			queryParamsObject.sortname = sortname;
			queryParamsObject.sortorder = sortorder;
			var queryParams = $.param(queryParamsObject);
			var encodeQueryParams = $.base64.encode(queryParams);
	        <%--location.href='${ctx}/duiAccountAction/toDuiAccountDetail2.do?checkBatchNo='+checkBatchNo+"&acqEnname="+acqEnname+"&queryParams="+encodeQueryParams;--%>
			openNewWindow('${ctx}/duiAccountAction/toDuiAccountDetail2.do',{"checkBatchNo":checkBatchNo,"acqEnname":acqEnname})
		}
		
		
		$("#subjectForm").submit(function(){
			$("#table_list_dui_account").setGridParam({
			       datatype : 'json',
			       page : '${params.pageNo}'          //Replace the '1' here
			    }).trigger("reloadGrid");
			return false;
		});
		
		function getParams(o){
			var data=$("#subjectForm").serializeArray();
		     $.each(data, function() {    
		             o[this.name] = this.value || '';    
		     });   
		}
		
		function customCheckResultFormatter(cellvalue, options, rowObject){  
			   if('FAILED'==cellvalue){
				   return "<span style=\"color:red\">失败</span>" ;
			   }else{
				   if('SUCCESS'==cellvalue){
					   return "成功" ;
				   }else{
					   return "空" ;
				   }
			   }
		}  
		
		function customRecordStatusFormatter(cellvalue, options, rowObject){  
			<c:forEach var="checkAccountStatus" items="${recordStatusList}">
				  if(cellvalue == '${checkAccountStatus.sysValue}'){
					  return '${checkAccountStatus.sysName}';
				  }
			 </c:forEach>	
			 return "" ;
		}
		
		function deleteRecord(_id) {
			swal({  
			 	title: "是否继续删除?", 
			   	type: "warning",  
			    showCancelButton: true,   
			    cancelButtonText: "取消",  
			   	confirmButtonColor: "#DD6B55",  
			    confirmButtonText: "继续删除",  
			    closeOnConfirm: false 
			    }, 
			    function(){   
			    	$.post('${ctx}/duiAccountAction/deleteDuiAccount.do', 
						{ 'id' : _id , '${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) {
							if(!msg.state){
								//alert(msg.msg);
					                // Display a success toast, with a title
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
								$("#subjectForm").submit() ;
							}
							swal.close();
						});
			    });
		}
		
		function confirmAcc(checkBatchNo,acqEnname) {
			swal({  
			 	title: "是否确认记账?", 
			   	type: "info",  
			    showCancelButton: true,   
			    cancelButtonText: "否",  
			   	confirmButtonColor: "#DD6B55",  
			    confirmButtonText: "是",  
			    closeOnConfirm: false 
			    }, 
			    function(){   
			    	 $.post('${ctx}/duiAccountAction/confirmAccount.do',
						{ 'checkBatchNo' : checkBatchNo,'acqEnname':acqEnname,'${_csrf.parameterName}':'${_csrf.token}'},
						function(msg) {
							if(!msg.state){
								//alert(msg.msg);
					                // Display a success toast, with a title
					            $("#table_list_dui_account").jqGrid('setGridParam',{
									url : "${ctx}/duiAccountAction/queryDuiAccountList.do",
									datatype : 'json',
								}).trigger('reloadGrid');//重新载入
					            toastr.error(msg.msg,'错误');
					           
							}else{
								$("#table_list_dui_account").jqGrid('setGridParam',{
									url : "${ctx}/duiAccountAction/queryDuiAccountList.do",
									datatype : 'json',
								}).trigger('reloadGrid');//重新载入
								toastr.success(msg.msg,'提示');
								$("#subjectForm").submit() ;
							}
							swal.close();
						}); 
			    });
		}
		
		
		$(document).ready(function() {
			var lastsel;
			//var data=$("#subjectForm").serialize();
			//alert(data);
			//$("#table_list_dui_account").closest(".ui-jqgrid-bdiv").css({ 'overflow-x' : 'scroll' });
            // 初始化表格
            $("#table_list_dui_account").jqGrid({
            	url:"${ctx}/duiAccountAction/queryDuiAccountList.do",
                datatype: "json",	
                mtype: "POST",
                height:"auto",
                autowidth: true,
                shrinkToFit:false,
                autoScroll: false,
                rowNum: 10,
                rowList: [10, 20],
                autoScroll: true, 
                colNames:['操作','对账批次号','收单机构标识','收单机构交易总金额',  '收单机构对账文件总笔数',  '收单机构对账文件成功笔数',  '平台交易总金额',  '平台交易总笔数',  '平台对账成功总笔数',  '平台对账失败总笔数',  '对账结果','记账结果',  '对账文件日期',  '对账时间',  '对账文件名称',  '操作员',  '创建时间','记账状态'],
                colModel: [
                    {name:'Detail',index:'id',width:300,align:"center",sortable:false,frozen:true},
                    {name: 'checkBatchNo', index: 'checkBatchNo', width: 210, align: "left"},
                    {name: 'acqEnname', index: 'acqEnname',width: 150, align: "left", sortable:false},
                    {name: 'acqTotalAmount', index: 'acqTotalAmount',width: 200, align: "right",formatter: 'number'},
                    {name: 'acqTotalItems', index: 'acqTotalItems', width: 160, align: "center"},
                    {name: 'acqTotalSuccessItems', index: 'acqTotalSuccessItems', width: 250, align: "center"},
                    {name: 'totalAmount', index: 'totalAmount', width: 140, align: "right",formatter: 'number'},
                    {name: 'totalItems', index: 'totalItems', width: 140, align: "center"},
                    {name: 'totalSuccessItems', index: 'totalSuccessItems', width: 150, align: "center"},
                    {name: 'totalFailedItems', index: 'totalFailedItems', width: 150, align: "center"},
                    {name: 'checkResult', index: 'checkResult', width: 130, align: "center" , sortable:false,formatter:  customCheckResultFormatter},
                    {name: 'recordStatus', index: 'recordStatus', width: 130, align: "center" , sortable:false,formatter:  customRecordStatusFormatter},
                    {name: 'checkFileDate', index: 'checkFileDate', width: 250, align: "left",formatter:function(val){return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
                    {name: 'checkTime', index: 'checkTime', width: 250, align: "left",formatter:function(val){return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
                    {name: 'checkFileName', index: 'checkFileName', width: 300, align: "left", sortable:false},
                    {name: 'operator', index: 'operator', width: 150, align: "left", sortable:false},
                    {name: 'createTime', index: 'createTime', width: 200, align: "left",formatter:function(val){return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
                    
                    {name: 'recordStatus', index: 'recordStatus', width: 130, align: "left" , sortable:false,hidden:"true"},
                ],
                onSelectRow: function(id){
            		if(id && id!==lastsel){
            			jQuery('#table_list_user').jqGrid('restoreRow',lastsel);
            			jQuery('#table_list_user').jqGrid('editRow',id,true);
            			lastsel=id;
            		}
            	},
                multiselect: false,//支持多项选择
                pager: "#pager_list_dui_account",
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
                    var ids=$("#table_list_dui_account").jqGrid('getDataIDs');
                    for(var i=0; i<ids.length; i++){
                        var id=ids[i];  
                        var getRow = $('#table_list_dui_account').getRowData(id);//获取当前的数据行
                        var checkBatchNo = getRow.checkBatchNo ;
                        var acqEnname = getRow.acqEnname;
                        var detail = "" ;
                        <sec:authorize access="hasAuthority('duiAccountMsgDetail:query')">
                        	detail += "<a href='javascript:void(0);' onclick='Detail1(" + id + ")'  class='default-details' title='详情'>详情</a>&nbsp;";
                        </sec:authorize>
                        detail += "&nbsp;<a  href='javascript:void(0);' onclick='Detail2(" + id + ")'  class='default-details' title='详情2'>详情2</a>";
                        <sec:authorize access="hasAuthority('duiAccountMsg:confirmAcc')">
                        	if (getRow.recordStatus =='1') {
                        		detail += "&nbsp;<a href='javascript:void(0)' class='default-details' title='确认记账' style='background: #99D1FD;cursor:not-allowed;'>确认记账</a>";
                        	} else {
                        		detail += "&nbsp;<a href='javascript:void(0)' class='default-details'  onclick=confirmAcc('"+checkBatchNo+"','"+acqEnname+"') title='确认记账' >确认记账</a>";
                        	}
                        </sec:authorize>
                        <sec:authorize access="hasAuthority('duiAccountMsg:delete')">
                        	if (getRow.recordStatus!='2') {
                        		detail += "&nbsp;<a href='javascript:void(0)' class='default-delete' title='删除' style='background: #9dcf9d;cursor:not-allowed;'>删除</a>";
                        	} else {
                        		detail += "&nbsp;<a href='javascript:void(0)' class='default-delete' title='删除' onclick='deleteRecord("+id+")'>删除</a>";
                        	}
                        </sec:authorize>
                        jQuery("#table_list_dui_account").jqGrid('setRowData', ids[i], { Detail: detail });
                    }
                }
            });
            jQuery("#table_list_dui_account").jqGrid('setFrozenColumns');
            // Add responsive to jqGrid
            $(window).bind('resize', function () {
                var width = $('.jqGrid_wrapper').width();
                $('#table_list_dui_account').setGridWidth(width);
            });
            
            $('.input-group.date').datepicker({
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
      