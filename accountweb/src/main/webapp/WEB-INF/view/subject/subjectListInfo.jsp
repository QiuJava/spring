
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
	<link href="${ctx}/css/plugins/select2/select2-skins.min.css" rel="stylesheet">
	 <!-- Sweet Alert -->
    <link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10" >
            <div class="pull-left ">当前位置</div>
            <em class=""></em>
            <div class="pull-left">科目管理</div>
            <em class=""></em>
            <div class="pull-left active">科目详细列表信息</div>
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
    					   <label class="col-sm-2 control-label">上级科目:</label>
                                       <div class="col-sm-2">
                                    <select id="parentSubjectNo" autocomplete="off" class="form-control" name="parentSubjectNo">
                                     		<option value="ALL" selected="selected">全部</option>
                                     		<c:forEach var="subject" items="${subjectList}">
                                                <option value="${subject.subjectNo}"
                                                <c:if test="${subject.subjectNo == params.parentSubjectNo}">selected="selected"</c:if>>
                                                ${subject.subjectNo}(${subject.subjectName})</option>
                                            </c:forEach>
    								</select>
    								</div>
                            <label class="col-sm-2 control-label">科目编号:</label>
                                   <div class="col-sm-2"><input type="text" class="form-control" name="subjectNo" value="${params.subjectNo}"/></div>
                                  
                                   <label class="col-sm-2 control-label">级别:</label>
                                    <div class="col-sm-2">
                                        <select class="form-control" name="subjectLevel"> 
                                            <option value="9999" selected="selected">全部</option>
                                             <c:forEach var="subjectLevel" items="${subjectLevelList}">
                                                <option value="${subjectLevel.sysValue}"
                                                <c:if test="${subjectLevel.sysValue == params.subjectLevel}">selected="selected"</c:if>>
                                                    ${subjectLevel.sysName}
                                                </option>
                                            </c:forEach>
                                        </select>                          
                                      </div>
    					 </div>
						  <div class="form-group lastbottom">
                          		   <label class="col-sm-2 control-label">科目名称:</label>
                                   <div class="col-sm-2"><input type="text" class="form-control" name="subjectName" value="${params.subjectName}"></div>
                                 
                                   <label class="col-sm-2 control-label">类别:</label>
									<div class="col-sm-2">
   										<select class="form-control" name="subjectType"> 
   											<option value="ALL" selected="selected">全部</option>
									         <c:forEach var="subjectType" items="${subjectTypeList}">
												<option value="${subjectType.sysValue}"
												<c:if test="${subjectType.sysValue == params.subjectType}">selected="selected"</c:if>>
												${subjectType.sysName}</option>
											</c:forEach>
										
										</select>                    
									  </div>
									 
						</div>
						
					
                          
                            <div class="clearfix lastbottom"></div>
                                
                                   <div class="form-group">
                                        <label class="col-sm-2 control-label aaa"></label>
                                   
                                       <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                           <button type="submit" class=" btn  btn-success " value=""><span class="glyphicon gly-search"></span>查询</button>
                                        <!--    
                                       </div>
                                       <div class="col-sm-4 col-sm-offset-1  "> -->
                                            <button class="btn  btn-default  col-sm-offset-14" type="reset" id="reset"><span class="glyphicon gly-trash"></span>清空</button>
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
										<table id="table_list_subject"></table>
										<div id="pager_list_subject"></div>
								</div>
							</div>
						</div>
				</div>
	</div>
	
   </body>
    
	<title>
	<script src="${ctx}/js/plugins/select2/select2.full.min.js"></script>
	<script src="${ctx}/js/plugins/select2/i18n/zh-CN.js"></script>
	<!-- Sweet alert -->
    <script src="${ctx}/js/plugins/sweetalert/sweetalert.min.js"></script>
    
	<script type="text/javascript">
        //var $exampleMulti = $("#parentSubjectNo").select2(); 
        $("#reset").on("click", function () { 
            //$exampleMulti.val("ALL").trigger("change");
            $('#parentSubjectNo').select2("val", "ALL");
            //$("#parentSubjectNo").empty();
        });
		function Detail(id) {   //单击修改链接的操作     
			//console.info(id);
	        var model = $("#table_list_subject").jqGrid('getRowData', id);
	        var pageNo = $("#table_list_subject").jqGrid('getGridParam','page');
	        var pageSize = $("#table_list_subject").jqGrid('getGridParam','rowNum');
	        var sortname = $("#table_list_subject").jqGrid('getGridParam','sortname');
	        var sortorder = $("#table_list_subject").jqGrid('getGridParam','sortorder');
			var subjectNo = model.subjectNo;
	        //alert(model.subjectNo);
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
	        location.href='${ctx}/subjectAction/toSubjectDetail.do?subjectNo='+subjectNo+"&queryParams="+encodeQueryParams;
	        
	    }
		function Modify(id) {   //单击修改链接的操作         
	        var model = $("#table_list_subject").jqGrid('getRowData', id);
	        var pageNo = $("#table_list_subject").jqGrid('getGridParam','page');
	        var pageSize = $("#table_list_subject").jqGrid('getGridParam','rowNum');
	        var sortname = $("#table_list_subject").jqGrid('getGridParam','sortname');
	        var sortorder = $("#table_list_subject").jqGrid('getGridParam','sortorder');
			var subjectNo = model.subjectNo;
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
	        location.href='${ctx}/subjectAction/toSubjectUpdate.do?subjectNo='+subjectNo+"&queryParams="+encodeQueryParams;
		}
		
		function Delete(id) {   //单击修改链接的操作         
	        var model = $("#table_list_subject").jqGrid('getRowData', id);
			var id = model.id;
			//console.info("model.id:"+id);
			thenswal(id);
		}
		
		$("#subjectForm").submit(function(){
			$("#table_list_subject").setGridParam({
			       datatype : 'json',
			       page : 1            //Replace the '1' here
			    }).trigger("reloadGrid");
			return false;
		});
		// 去除空格啊
        $('input').blur(function(){
            replaceSpace(this);
        })
        function replaceSpace(obj){
            obj.value = obj.value.replace(/\s/gi,'')
        }

		function getParams(o){
			var data=$("#subjectForm").serializeArray();
		     $.each(data, function() {    
		             o[this.name] = this.value || '';    
		     });   
		     //o.parentSubjectNo = $("#parentSubjectNo").select2("val");
		}
		
		function customSubjectTypeFmatter(cellvalue, options, rowObject){  
			<c:forEach var="subjectType" items="${subjectTypeList}">
				  if(cellvalue == '${subjectType.sysValue}'){
					  return '${subjectType.sysName}';
				  }
			 </c:forEach>	
		}  
		
		function customSubjectLevelFmatter(cellvalue, options, rowObject){  
			<c:forEach var="subjectLevel" items="${subjectLevelList}">
				  if(cellvalue == '${subjectLevel.sysValue}'){
					  return '${subjectLevel.sysName}';
				  }
			 </c:forEach>	
		}  
		
		function customBalanceFromFmatter(cellvalue, options, rowObject){  
			<c:forEach var="balanceFrom" items="${balanceFromList}">
				  if(cellvalue == '${balanceFrom.sysValue}'){
					  return '${balanceFrom.sysName}';
				  }
			 </c:forEach>	
		}   
		
		function customIsInnerAccountFmatter(cellvalue, options, rowObject){  
			<c:forEach var="isInnerAccount" items="${isInnerAccountList}">
				  if(cellvalue == '${isInnerAccount.sysValue}'){
					  return '${isInnerAccount.sysName}';
				  }
			 </c:forEach>	
		}   
		
		$(document).ready(function() {
			var lastsel;
            // 初始化表格
            $("#table_list_subject").jqGrid({
            	url:"${ctx}/subjectAction/findSubjectListInfo.do",
                datatype: "json",	
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
                altRows: true,
                colNames:['操作','id', '科目编号',  '科目名称','科目别名','级别','上级编号','上级名称','类别','余额方向','开立内部账户','创建人'],
                colModel: [
                    {name:'Detail',index:'Id',width:210,align:"center",sortable:false, frozen : true},
                    {name: 'id', index: 'id', width: 150, align: "center", hidden:true},
                    {name: 'subjectNo', index: 'subjectNo', width: 220, align: "left"},
                    {name: 'subjectName', index: 'subjectName',width: 200, align: "left"},
                    {name: 'subjectAlias', index: 'subjectAlias',width: 200, align: "center"},
                    {name: 'subjectLevel', index: 'subjectLevel',width: 180, align: "center", formatter:  customSubjectLevelFmatter},
                    {name: 'parentSubjectNo', index: 'parentSubjectNo',width: 140, align: "left"},
                    {name: 'parentSubject.subjectName', index: 'parentSubjectName',width: 180, align: "left"},
                    {name: 'subjectType', index: 'subjectType',width: 120, align: "left", formatter:  customSubjectTypeFmatter},
                    {name: 'balanceFrom', index: 'balanceFrom',width: 120, align: "center", formatter:  customBalanceFromFmatter},  
                    {name: 'isInnerAccount', index: 'isInnerAccount',width: 100, align: "center", formatter:  customIsInnerAccountFmatter}, 
                    {name: 'creator', index: 'creator',width: 100, align: "center"}, 
                ],
                onSelectRow: function(id){
            		if(id && id!==lastsel){
            			jQuery('#table_list_subject').jqGrid('restoreRow',lastsel);
            			jQuery('#table_list_subject').jqGrid('editRow',id,true);
            			lastsel=id;
            		}
            	},
                multiselect: false,//支持多项选择
                pager: "#pager_list_subject",
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
                    var ids=$("#table_list_subject").jqGrid('getDataIDs');
                    for(var i=0; i<ids.length; i++){
                        var id=ids[i];   
                        var detail = "" ;
                        <sec:authorize access="hasAuthority('subjectListInfo:detail')">
                        detail += "<a href='javascript:void(0);'  title='详情' onclick='Detail(" + id + ")' class='default-details'>详情</a>";
                        </sec:authorize>
                        <sec:authorize access="hasAuthority('subjectListInfo:update')">                               	   
                        	detail += "&nbsp;&nbsp;<a href='javascript:void(0);'   title='维护' class='default-maintenance' onclick='Modify(" + id + ")'>维护</a>";
                   		</sec:authorize>
                   		<sec:authorize access="hasAuthority('subjectListInfo:delete')">                               	   
                   			detail += "&nbsp;&nbsp;<a href='javascript:void(0);'   title='删除' class='default-delete' onclick='Delete(" + id + ")'>删除</a>";
	               		</sec:authorize>
                        jQuery("#table_list_subject").jqGrid('setRowData', ids[i], { Detail: detail });
                    }
                }
            });
            jQuery("#table_list_subject").jqGrid('setFrozenColumns');
            // Add responsive to jqGrid
            $(window).bind('resize', function () {
                var width = $('.jqGrid_wrapper').width();
                $('#table_list_subject').setGridWidth(width);
            });
		});
		
		
		function formatRepo (repo) {
	        if (repo.loading) return repo.text;
			//console.info(repo.id);
			return repo.id+'('+repo.text+')';  
	      }

	      function formatRepoSelection (repo) {
	    	  //console.info("formatRepoSelection:"+ repo.text);
	    	  return repo.id+'('+repo.text+')';
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
	    	  $('#parentSubjectNo').select2({
	    	    matcher: oldMatcher(matchStart)
	    	  })
	    	  
	    	  //$('#parentSubjectNo').val("${params.parentSubjectNo}");
	    	  var len = $('#parentSubjectNo option:selected').length;
	          if (len == 0) {
	            $('#select2-parentSubjectNo-container').text('')
	          };
	    	});
	      
			function thenswal(_id){
				//console.info("_id" + _id);
				swal({  
				 	title: "是否继续删除?", 
				   	text: "只能删除没有使用科目",   
				   	type: "warning",  
				    showCancelButton: true,   
				    cancelButtonText: "取消",  
				   	confirmButtonColor: "#ff3737",  
				    confirmButtonText: "继续删除",  
				    closeOnConfirm: false 
				    }, 
				    function(){   
				    	// ajax post handler
				    	$.post('${ctx}/subjectAction/deleteSubjectById.do', 
							{ 'id' : _id , '${_csrf.parameterName}':'${_csrf.token}' },
							function(msg) {
								if(!msg.state){
									//alert(msg.msg);
						                // Display a success toast, with a title
						            toastr.error(msg.msg,'错误');
								}else{
									toastr.success(msg.msg,'提示');
									$("#subjectForm").submit();
								}
								swal.close();
							});
				    });
			}
	</script>
	 </title>
</html>  
      