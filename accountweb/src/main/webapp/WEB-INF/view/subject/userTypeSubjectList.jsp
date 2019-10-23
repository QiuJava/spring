
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="sequenceNo" value="0" />
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
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">科目管理</div>
            <em class=""></em>
            <div class="pull-left active">用户类型科目关联查询</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="userTypeForm">
                           <div class="form-group ">		
                                 <label class="col-sm-2 control-label" >用户类别：</label>
                                <div class="col-sm-2">
   									<select class="form-control" name="userType"> 
									         <option value="ALL" selected="selected">全部</option>
									         <c:forEach var="userType" items="${userTypes}">
												<option value="${userType.sysValue}">
												${userType.sysName}</option>
											</c:forEach>
										</select>  
								 </div>
 								<label class="col-sm-2 control-label">科目编号：</label>
                                <div class="col-sm-2">
                                	<select id="subjectNo" autocomplete="off" class="form-control" name="subjectNo">
									</select>
								</div>
							</div>	
                            <div class="clearfix lastbottom"></div>
							<div class="form-group" style="margin-bottom:80px">
                               <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                    <label class="col-sm-2 control-label aaa"></label>
                               	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                   		<button class="btn btn-success " type="submit"><span class="glyphicon gly-search"></span>查询</button>
                                   		<button class="btn btn-default  col-sm-offset-14" type="reset" id="reset"><span class="glyphicon gly-trash"></span>清空</button>
                               <!-- </div> -->
                          	</div>
                     </form>
                
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
        var $exampleMulti = $("#subjectNo").select2();
        $("#reset").on("click", function () { 
            $exampleMulti.val(null).trigger("change");
            $("#subjectNo").empty();
        });

	
	function Delete(id) {   //单击删除链接的操作         
			thenswal(id);
			/* $.ajax({url:'${ctx}/subjectAction/deleteUserTypeSubject.do?id='+id});
	        location.reload() ; */
	        
	    }
		
		$("#userTypeForm").submit(function(){
			$("#table_list_subject").setGridParam({
			       datatype : 'json',
			       page : 1            //Replace the '1' here
			    }).trigger("reloadGrid");
			return false;
		});
		
		function getParams(o){
			var data=$("#userTypeForm").serializeArray();
		     $.each(data, function() {    
		             o[this.name] = this.value || '';    
		     });   
		     o.subjectNo = $("#subjectNo").select2("val");
		}
		
		function customUserTypeFmatter(cellvalue, options, rowObject){  
			<c:forEach var="userType" items="${userTypes}">
				  if(cellvalue == '${userType.sysValue}'){
					  return '${userType.sysName}';
				  }
			 </c:forEach>	
			 return "" ;
		}
		
		
		$(document).ready(function() {
			var lastsel;
			sequenceNo = 0 ;
			var data=$("#userTypeForm").serialize();
			//alert(data);alert('${list[0].getUserType}');
            // 初始化表格
            $("#table_list_subject").jqGrid({
            	url:"${ctx}/subjectAction/findUserTypeSubjectList.do",
                datatype: "json",	
                mtype: "POST",
                height:"auto",
                autowidth: true,
                shrinkToFit: true,
                rowNum: 10,
                rowList: [10, 20],
                colNames:['操作', '序号','用户类别','科目编号','科目名称','科目别名','创建人'],
                colModel: [
                    {name:'Detail',index:'Id',width:45,align:"center",sortable:false,frozen:true},
                    {name: 'id', index: 'id',width: 100, align: "left"},
                    {name: 'userType', index: 'userType',width: 100, align: "left", sortable:false ,formatter:  customUserTypeFmatter},
                    {name: 'subjectNo', index: 'subjectNo',width: 80, align: "left"},
                    {name: 'subject.subjectName', index: 'subjectName',width: 140, align: "left", sortable:false},
                    {name: 'subject.subjectAlias', index: 'subjectAlias',width: 140, align: "left", sortable:false},
                    {name: 'creator', index: 'creator',width: 80, align: "left", sortable:false}
                ],
                onSelectRow: function(id){
            		if(id && id!==lastsel){
            			jQuery('#table_list_user').jqGrid('restoreRow',lastsel);
            			jQuery('#table_list_user').jqGrid('editRow',id,true);
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
                        <sec:authorize access="hasAuthority('userTypeSubjectList:delete')">                               	   
							detail += "<a href='javascript:void(0);' class='default-delete' title='删除' onclick='Delete(" + id + ")'>删除</a>";
			            </sec:authorize>
                        //detail += "&nbsp;&nbsp;<a href='javascript:void(0);' style='color:#f60'  title='维护' onclick='Modify(" + id + ")'>维护</a>";
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
		
		

		/* 科目查询开始 */

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
		
		// turn the element to select2 select style
	      $('#subjectNo').select2({
		    	  		ajax: {
			    		  	url: "${ctx}/subjectAction/queryParentSubjectName.do",
			    		    dataType: 'json',
			    		    delay: 250,
			    		    data: function (params) {
			    		      return {
			    		        q: parseSelectParams(params), // search term
			    		        page: params.page
			    		      };
			    		    },
			    		    processResults: function (data, params) {
			    		      // parse the results into the format expected by Select2
			    		      // since we are using custom formatting functions we do not need to
			    		      // alter the remote JSON data, except to indicate that infinite
			    		      // scrolling can be used
			    		      params.page = params.page || 1;
	
			    		      return {
			    		        results: data,
			    		        pagination: {
			    		          more: (params.page * 30) < data.total_count
			    		        }
			    		      };
			    		    },
			    		    cache: true
		    		  },
		    		  placeholder: "选择科目",  
		    		  allowClear: true,
		    		  width: '100%',
		    		  // containerCssClass: 'tpx-select2-container',
		    		  // dropdownCssClass: 'tpx-select2-drop',
		    		  escapeMarkup: function (markup) { return markup; }, // let our custom formatter work
		    		  minimumInputLength: 0,
		    		  language: "zh-CN",
		    		  templateResult: formatRepo, // omitted for brevity, see the source of this page
		    		  templateSelection: formatRepoSelection // omitted for brevity, see the source of this page
		    		}
	    		  );
		

			function thenswal(_id){
				//console.info("_id" + _id);
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
				    	//$.ajax({url:'${ctx}/subjectAction/deleteUserTypeSubject.do?id='+id});
				    	// ajax post handler
				    	$.post('${ctx}/subjectAction/deleteUserTypeSubject.do', 
							{ 'id' : _id , '${_csrf.parameterName}':'${_csrf.token}' },
							function(msg) {
								if(!msg.state){
									//alert(msg.msg);
						                // Display a success toast, with a title
						            toastr.error(msg.msg,'错误');
								}else{
									toastr.success(msg.msg,'提示');
									$("#userTypeForm").submit();
								}
								swal.close();
							});
				    });
			}
		
	</script>
	 </title>
</html>  
      