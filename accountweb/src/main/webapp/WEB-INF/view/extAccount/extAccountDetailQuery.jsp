
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
		<div class="col-lg-10" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">外部账号管理</div>
            <em class=""></em>
            <div class="pull-left  active">客户账户明细查询</div>
        </div>

	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
			<div class="ibox-title">
					<div class="ibox-tools">
						<a class="collapse-link"> <i class="fa fa-chevron-up"></i></a> 
					</div>
				</div>
				<div class="ibox-content">
					 <form class="form-horizontal" id="extTransInfoForm">
						 <div class="form-group">
                      		    <label class="col-sm-2 control-label">账号：</label>
                                <div class="col-sm-2"><input type="text" class="form-control" name="accountNo" value="${params.accountNo}"></div>  
                                <label class="col-sm-2 control-label">记账日期:</label>
								<div class="col-sm-4">
		                            <div class="input-daterange input-group" id="datepicker">
								    	<input type="text" class="input-sm form-control" name="recordDate1" value="${params.recordDate1}" />
								    	<span class="input-group-addon">~</span>
								    	<input type="text" class="input-sm form-control" name="recordDate2" value="${params.recordDate2}" />
									</div>   
								</div>                    
						</div> 
						<div class="clearfix lastbottom"></div>
                        <div class="form-group">
                           <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                           <label class="col-sm-2 control-label aaa"></label>
                           <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                               <button class="btn btn-success" type="submit"><span class="glyphicon gly-search"></span>查询</button>
                               <button class="btn btn-default col-sm-offset-14" type="reset"><span class="glyphicon gly-trash"></span>清空</button>
                               <c:if test="${!empty forwardTo}">
                               	   <button id="returnUp" type="button" class=" btn btn-default col-sm-offset-14" onclick="window.location.href='${ctx}/extAccountAction/queryAllAccountList.do?queryParams=${params.queryParams}'" value="" /><span class="glyphicon gly-return"></span>返回</button>
                               </c:if>
                                <button class="btn btn-danger col-sm-offset-14" type="button" onclick="exportExcel()"><span class="glyphicon gly-out"></span>导出</button>
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
								<table id="table_list_extAccountDetail"></table>
								<div id="pager_list_extAccountDetail"></div>
								</div>
							</div>
						</div>
				</div>
			
	</div>
	
   </body>
   
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
			   /*表单提交时的处理*/
	   		function exportExcel() {  
	   			var data = $("#extTransInfoForm").serialize();
	   			$.download('${ctx}/extAccountAction/exportAllExtTransDetailInfo.do',data,'post');
	   	    }
			
			
			
			$("#extTransInfoForm").submit(function(){
				$("#table_list_extAccountDetail").setGridParam({
				       datatype : 'json',
				       page : 1            //Replace the '1' here
				    }).trigger("reloadGrid");
				return false;
			});
			function customBalanceFromFmatter(cellvalue, options, rowObject){  
				<c:forEach var="balanceFrom" items="${balanceFromList}">
					  if(cellvalue == '${balanceFrom.sysValue}'){
						  return '${balanceFrom.sysName}';
					  }
				 </c:forEach> 
				 return "" ;
			}   
			/* function customDebitCreditSideFmatter(cellvalue, options, rowObject){  
				return "debit" == cellbalue?"借方":"credit" == cellbalue?"贷方":"" ;
			}
			 */
			 function customTransTypeFmatter(cellvalue, options, rowObject){  
					<c:forEach var="transType" items="${transTypeList}">
					  if(cellvalue == '${transType.transTypeCode}'){
						  return '${transType.transTypeName}';
					  }
				 	</c:forEach>	
					return "" ;
				}
			function getParams(o){
				var data=$("#extTransInfoForm").serializeArray();
			     $.each(data, function() {    
			             o[this.name] = this.value || '';    
			     });   
			}
			$(document).ready(function() {
						var lastsel;
			            // 初始化表格
			            $("#table_list_extAccountDetail").jqGrid({
			            	url:"${ctx}/extAccountAction/findAllExtTransDetailInfo.do",
			                datatype: "json",
			                mtype: "POST",
			                height:"auto",
			                autowidth: true,
			                shrinkToFit:false,
			                autoScroll: false,
			                rowNum: 10,
			                rowList: [10, 20],
			                colNames:['账号','记账流水号','子记账流水号','交易类型','记账日期','记账时间','借贷方向','记账金额','余额','可用余额','控制金额','结算中金额','预冻结金额','摘要'],
			                colModel: [
					            {name: 'accountNo', index: 'accountNo', width: 230, sorttype: "date", align: "left", sortable:false},
					            {name: 'serialNo', index: 'serialNo', width: 180, sorttype: "date", align: "left"},
					            {name: 'childSerialNo', index: 'childSerialNo', width: 100, sorttype: "date", align: "left"},
					            {name: 'transType', index: 'transType',width: 120, align: "left", sortable:false,formatter:  customTransTypeFmatter},
			                    {name: 'recordDate', index: 'recordDate', width: 100, sorttype: "date", align: "left"},
			                    {name: 'recordTime', index: 'recordTime', width: 100, align: "left"},
			                    {name: 'debitCreditSide', index: 'debitCreditSide',width: 80, align: "center", sortable:false},
			                    {name: 'recordAmount', index: 'recordAmount',width: 90, align: "right",formatter: 'number'},
			                    {name: 'balance', index: 'balance',width: 90, align: "right",formatter: 'number'},
			                    {name: 'avaliBalance', index: 'avaliBalance',width: 90, align: "right",formatter: 'number'},
			                    {name: 'controlAmount', index: 'controlAmount',width: 90, align: "right",formatter: 'number'},
			                    {name: 'settlingAmount', index: 'settlingAmount',width: 90, align: "right",formatter: 'number'},
			                    {name: 'preFreezeAmount', index: 'preFreezeAmount',width: 90, align: "right",formatter: 'number'},
			                    {name: 'summaryInfo', index: 'summaryInfo',width: 300, align: "left", sortable:false},
	              
			                ],
			                onSelectRow: function(id){
			            		if(id && id!==lastsel){
			            			jQuery('#table_list_extAccountDetail').jqGrid('restoreRow',lastsel);
			            			jQuery('#table_list_extAccountDetail').jqGrid('editRow',id,true);
			            			lastsel=id;
			            		}
			            	},
			                multiselect: false,//支持多项选择
			                pager: "#pager_list_extAccountDetail",
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
			                }
			            });
						jQuery("#table_list_extAccountDetail").jqGrid('setFrozenColumns');
			            // Add responsive to jqGrid
			            $(window).bind('resize', function () {
			                var width = $('.jqGrid_wrapper').width();
			                $('#table_list_extAccountDetail').setGridWidth(width);
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