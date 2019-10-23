
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
        <div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">出账管理</div>
            <em class=""></em>
            <div class="pull-left active">确认提交出款通道</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12"> 
				<div class="ibox-content">
					 <form class="form-horizontal" id="form1">
                           <div class="form-group ">
                                <label class="col-sm-2 control-label" >出款通道：</label>
                                <div class="col-sm-2">
   									<select class="form-control" name="settleBank" id="settleBank"> 
								         <option value="ALL" selected="selected">全部</option>
								         <c:forEach var="settleBank" items="${settleTransferBankList}">
											<option value="${settleBank.sysValue}">
												${settleBank.sysName}
											</option>
										</c:forEach>
									</select>      
								 </div>								
 								 <label class="col-sm-2 control-label">创建时间：</label>
                                 <div class="col-sm-4">
                                    <div class="input-daterange input-group" id="datepicker">
                                        <input type="text" class="input-sm form-control" name="createDate1" />
                                        <span class="input-group-addon">~</span>
                                        <input type="text" class="input-sm form-control" name="createDate2" />
                                    </div>   
                                </div>
                            </div>
                            <div class="clearfix lastbottom"></div>
                            <div class="form-group">
                                    <label class="col-sm-2 control-label aaa"></label>
                              <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                              	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                              	   
                                  	<button class="btn btn-success" type="submit"><span class="glyphicon gly-search"></span>查询</button>
                                  	<button class="btn btn-default col-sm-offset-14" type="reset"><span class="glyphicon gly-trash"></span>清空</button>
                              <!-- </div> -->
                         	</div>
                    </form>
				</div>
			</div>
            <div class="col-lg-12">
            <div class="ibox ">
                <div class="ibox-content">
                    <div class="jqGrid_wrapper">
                    <table id="table_list_confirmSubmit"></table>
                    <div id="pager_list_confirmSubmit"></div>
                    <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
                    </div>
                </div>
            </div>
        </div>
		</div>
		
		
		
	</div>
	<!-- 填充内容结束 -->
		
</body>
    
	<title>
	<script src="${ctx}/js/plugins/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
	<script src="${ctx}/js/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js"></script>
	
	<script type="text/javascript">
		function confirmSubmit(id) {   //单击详情链接的操作         
			var model = $("#table_list_confirmSubmit").jqGrid('getRowData', id);
			var id = model.id;
	        //location.href='${ctx}/chuAccountAction/toSettleTransferDetail.do?fileId='+id;
	        //TODO 执行 确认提交 到 通道 方法 
	        
	    }
	
		$("#form1").submit(function(){
			$("#table_list_confirmSubmit").setGridParam({
			       datatype : 'json',
			       page : 1            //Replace the '1' here
			    }).trigger("reloadGrid");
			return false;
		});
		
		function getParams(o){
			var data=$("#form1").serializeArray();
		     $.each(data, function() {    
		             o[this.name] = this.value || '';    
		     });   
		     //o.subjectNo = $("#subjectNo").select2("val");
		}
		
		
		function customStatesFmatter(cellvalue, options, rowObject){  
			switch(cellvalue){
			case '0':return "未提交" ;break ;
			case '1':return "已提交" ;break ;
			case '2':return "提交失败" ;break ;
			case '3':return "超时" ;
			default:return "未知" ;
			}	
		}
		
		
		$(document).ready(function() {
			var lastsel;
			//var sequenceNo = 0 ;
			var data=$("#form1").serialize();
			//alert(data);alert('${list[0].getUserType}');
            // 初始化表格
            $("#table_list_confirmSubmit").jqGrid({
            	url:"${ctx}/chuAccountAction/findSubmitChuKuanChannelList.do",
                datatype: "json",
                mtype: "POST",
                height:"auto",
                autowidth: true,
                shrinkToFit: false,
                autoScroll: false,
                rowNum: 10,
                rowList: [10, 20],
                colNames:['操作','id','文件名',  '操作员','出款通道','笔数/金额(元)','创建时间','状态','摘要',],
                colModel: [
                    {name:'Detail',index:'Id',width:80,align:"center",sortable:false,frozen:true},
					{name: 'id', index: 'id', width: 150, align: "center", hidden: true},
					{name: 'fileName', index: 'fileName', width: 220, align: "center"},
					{name: 'operatorName', index: 'operatorName',width: 100, align: "center"},
					{name: 'settleBank', index: 'settleBank',width: 150, align: "center"},
					{name: 'total', index: 'total',width: 150, align: "center"},
					{name: 'createTime', index: 'createTime',width: 150, align: "center",formatter:function(val){return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}},
					{name: 'status', index: 'status',width: 150, align: "center", formatter:  customStatesFmatter},
					{name: 'summary', index: 'summary',width: 150, align: "center"},
					
                    
                ], 
                onSelectRow: function(id){
            		if(id && id!==lastsel){
            			jQuery('#table_list_user').jqGrid('restoreRow',lastsel);
            			jQuery('#table_list_user').jqGrid('editRow',id,true);
            			lastsel=id;
            		}
            	},
                multiselect: false,//支持多项选择
                pager: "#pager_list_confirmSubmit",
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
                    var ids=$("#table_list_confirmSubmit").jqGrid('getDataIDs');
                    for(var i=0; i<ids.length; i++){
                        var id=ids[i];   
                        var detail = "<a href='javascript:void(0);' class='default-details'  onclick='confirmSubmit(" + id + ")' title='确认提交'>确认提交</a>";
                        jQuery("#table_list_confirmSubmit").jqGrid('setRowData', ids[i], { Detail: detail });
                    }
                }
            });
            jQuery("#table_list_confirmSubmit").jqGrid('setFrozenColumns');
            // Add responsive to jqGrid
            $(window).bind('resize', function () {
                var width = $('.jqGrid_wrapper').width();
                $('#table_list_confirmSubmit').setGridWidth(width);
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
</html>  
      