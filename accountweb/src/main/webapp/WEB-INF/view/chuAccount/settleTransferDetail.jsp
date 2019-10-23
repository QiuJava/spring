
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<!-- jqGrid plugin -->
	<link href="${ctx}/css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/jqGrid/ui.jqgrid.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/bootstrapTour/bootstrap-tour.min.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
        <div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">出账管理</div>
            <em class=""></em>
            <div class="pull-left active">结算转账文件详情</div>
        </div>
	</div> 
	
   <!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
				<div class="ibox-content">
					<!--边框表格布局: table table-bordered \悬停表格 : table table-hover-->					 
					<table class="table table-hover" style="width:100;max-width:100%">
					   <thead>
					      <tr>
					         <td width="100">出款通道：</td>
					         <td width="400">${settleTransferFile.settleBank }</td>
					         <td width="100">&emsp;&emsp;状态：</td>
					         <td width="400">
					         	<c:if test="${settleTransferFile.status =='0' }">未提交</c:if>
					         	<c:if test="${settleTransferFile.status =='1' }">已提交</c:if>
					         	<c:if test="${settleTransferFile.status =='2' }">提交失败</c:if>
					         	<c:if test="${settleTransferFile.status =='3' }">超时</c:if>
					         </td>
					      </tr>
					   </thead>
					   <tbody>
					      <tr>
					         <td>&emsp;总笔数：</td>
					         <td>${settleTransferFile.totalNum }&nbsp;&nbsp;条</td>
					         <td>&emsp;总金额：</td>
					         <td>${settleTransferFile.totalAmount }&nbsp;&nbsp;元</td>
					      </tr>
					      <tr>
					         <td>上传时间：</td>
					         <td>
					         	<fmt:formatDate value="${settleTransferFile.createTime }" pattern="yyyy - MM - dd  HH:mm:ss"/>
					         </td>
					         <td>提交时间：</td>
					         <td>
					         	<fmt:formatDate value="${settleTransferFile.transferTime }" pattern="yyyy - MM - dd  HH:mm:ss"/>
					         </td>
					      </tr>
					      <tr>
					         <td>&emsp;&emsp;摘要：</td>
					         <td>${settleTransferFile.summary }</td>
					         <td></td>
					         <td></td>
					      </tr>
					   </tbody>
					</table>
					 
				</div>
				
				 
			</div>
			<div>&nbsp;</div>
		<!-- 商户出账明细    查询列表 开始-->
            <div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<h5>
						商户出账明细
					</h5>
					<div class="ibox-tools">
						<a class="collapse-link"> <i class="fa fa-chevron-up"></i></a> 
					</div>
				</div>
				
				<div class="col-lg-13">
					<div class="ibox ">
						<div class="ibox-content">
							<div class="jqGrid_wrapper">
							<table id="table_list_settleTransfer"></table>
							<div id="pager_list_settleTransfer"></div>
                            <button id="returnUp" type="button" class=" btn btn-default" onclick="window.location.href='${ctx}/chuAccountAction/toSettleTransferQuery.do'" value="" style="margin-top:30px"/><span class="glyphicon gly-return"></span>返回</button>
							</div>
						</div>
					</div>
				</div>
			</div>
            </div>
		<!-- 商户出账明细    查询列表 结束-->
			
		</div>
			
	</div>
	<!-- 填充内容结束 -->
	
   </body>
    
	<title>
	<script type="text/javascript">
		
		function getParams(o){
		     o["${_csrf.parameterName}"] = "${_csrf.token}";    
		     o["fileId"] = "${fileId}";
		}
		
		function customStatusFmatter(cellvalue, options, rowObject){  
			<c:forEach var="settleTransferStatus" items="${settleTransferStatusList}">
			  if(cellvalue == '${settleTransferStatus.sysValue}'){
				  return '${settleTransferStatus.sysName}';
			  }
		 	</c:forEach>	
		}
		
		$(document).ready(function() {
			var lastsel;
			//var sequenceNo = 0 ;
			//var data=$("#bankAccountForm").serialize();
			//alert(data);alert('${list[0].getUserType}');
            // 初始化表格
            $("#table_list_settleTransfer").jqGrid({
            	url:"${ctx}/chuAccountAction/findSettleTransferList.do",
                datatype: "json",	
                mtype: "POST",
                height:"auto",
                autowidth: true,
                shrinkToFit: true,
                rowNum: 10,
                rowList: [10, 20],
                colNames:['编号',  '收款账号','收款人户名','金额','收款开户行','状态'],
                colModel: [
					{name: 'id', index: 'id', width: 50, align: "center"},
					{name: 'inAccNo', index: 'inAccNo',width: 50, align: "center"},
					{name: 'inAccName', index: 'inAccName',width: 50, align: "center"},
					{name: 'amount', index: 'amount',width: 50, align: "center"},
					{name: 'inSettleBankNo', index: 'inSettleBankNo',width: 50, align: "center"},
					{name: 'status', index: 'status',width: 50, align: "center", formatter:  customStatusFmatter},
                    
                ],
                onSelectRow: function(id){
            		if(id && id!==lastsel){
            			jQuery('#table_list_settleTransfer').jqGrid('restoreRow',lastsel);
            			jQuery('#table_list_settleTransfer').jqGrid('editRow',id,true);
            			lastsel=id;
            		}
            	},
                multiselect: false,//支持多项选择
                pager: "#pager_list_settleTransfer",
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
                    var ids=$("#table_list_settleTransfer").jqGrid('getDataIDs');
                    for(var i=0; i<ids.length; i++){
                        var id=ids[i];   
                        var detail = "<a href='${ctx}/chuAccountAction/toSettleTransferDetail.do?id='"+id+"' class='default-details' title='详情'>详情</a>";
                        jQuery("#table_list_settleTransfer").jqGrid('setRowData', ids[i], { Detail: detail });
                    }
                }
            });
            // Add responsive to jqGrid
            $(window).bind('resize', function () {
                var width = $('.jqGrid_wrapper').width();
                $('#table_list_settleTransfer').setGridWidth(width);
            });
		});
		
		
	</script>
	 </title>
</html>  
      