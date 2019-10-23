
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
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">对账管理</div>
            <em class=""></em>
            <div class="pull-left active">对账文件下载</div>
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
                          		   <label class="pull-left control-label">收单机构：</label>
                                   <div class="col-sm-3">
										<select class="form-control" name="acqOrg" id="acqOrg"> 
									         <c:forEach var="acqOrg" items="${acqOrgList}">
												<option value="${acqOrg.sysValue}">
													${acqOrg.sysName}
												</option>
											</c:forEach>
										</select>      
									</div>
                                  
                                    <label class="pull-left control-label">文件名：</label>
                                   <div class="col-sm-3"><input type="text" class="form-control" name="fileName"></div>									  														  
						</div>
                             <div class="clearfix lastbottom"></div>
                                
                                   <div class="form-group">
                                   <div class="col-sm-12 col-sm-offset-13  ">
                                   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                       <button type="submit" class=" btn btn-success" value=""><span class="glyphicon gly-search"></span>查询</button>
                                       <button class="btn btn-default col-sm-offset-14" type="reset"><span class="glyphicon gly-trash"></span>清空</button>
                                   </div>
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
		function Download(id) {   //单击修改链接的操作         
	        var model = $("#table_list_dui_account").jqGrid('getRowData', id);
			var _fileName = model.name;
			var _acqOrg = $('#acqOrg').val();
	        //alert(model.subjectNo);
	        location.href='${ctx}/duiAccountAction/duiAccountFileDown.do?fileName='+_fileName+'&acqOrg='+_acqOrg;
	        
	    }
		$("#subjectForm").submit(function(){
			$("#table_list_dui_account").setGridParam({
			       datatype : 'json',
			       page : 1            //Replace the '1' here
			    }).trigger("reloadGrid");
			return false;
		});
		
		function getParams(o){
			var data=$("#subjectForm").serializeArray();
		     $.each(data, function() {    
		             o[this.name] = this.value || '';    
		     });   
		}
		
		function customBytesToSizeFormatter(cellvalue, options, rowObject){  
			   var bytes = cellvalue;			
			   var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
			   if (bytes == 0) return '0 Byte';
			   var i = parseInt(Math.floor(Math.log(bytes) / Math.log(1024)));
			   return Math.round(bytes / Math.pow(1024, i), 2) + ' ' + sizes[i];
		}  
		
		
		$(document).ready(function() {
			var lastsel;
			//var data=$("#subjectForm").serialize();
			//alert(data);
            // 初始化表格
            $("#table_list_dui_account").jqGrid({
            	url:"${ctx}/duiAccountAction/findDuiAccountFileDownList.do",
                datatype: "json",	
                mtype: "POST",
                height:"auto",
                autowidth: true,
                shrinkToFit: true,
                autoScroll: true,
                rowNum: 10,
                rowList: [10, 20],
                colNames:['操作','文件名',  '文件大小','上传时间',],
                colModel: [
                    {name:'Detail',index:'Id',width:25,align:"center",sortable:false,frozen:true},
                    {name: 'name', index: 'name', width: 90, align: "center",sortable:false},
                    {name: 'size', index: 'size',width: 120, align: "center",sortable:false, formatter:  customBytesToSizeFormatter},
                    {name: 'createDate', index: 'createDate',width: 80, align: "center",sortable:false},
                    
                    
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
                        var detail = "" ;
                        <sec:authorize access="hasAuthority('duiAccountFileDown:download')">
                        	detail += "<a href='javascript:void(0);' title='下载' onclick='Download(" + id + ")' class='default-maintenance'>下载</a>";
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
		});
		
		
	</script>
	 </title>
</html>  
      