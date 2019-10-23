
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<!-- jqGrid plugin -->
	<link href="${ctx}/css/plugins/select2/select2.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/select2/select2-skins.min.css" rel="stylesheet">
	<link rel="stylesheet" href="${ctx}/css/icheck-css/custom.css" />
	<link rel="stylesheet" href="${ctx}/css/skins/all.css" />
	 <!-- Sweet Alert -->
    <link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/jqGrid/ui.jqgrid.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/bootstrapTour/bootstrap-tour.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/webuploader/webuploader.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/bootstrap-datepicker/bootstrap-datepicker3.min.css" rel="stylesheet"></head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">代理商分润管理</div>
            <em class=""></em>
            <div class="pull-left active">代理商分润提现</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="agentsProfitSettleOrderForm">
					 
					 
                          <div class="form-group" >
                          	  <label class="col-sm-2 control-label">订单创建时间:</label>
                           		<div class="col-sm-3">
		                            <div class="input-daterange input-group" id="datepicker">
									    <input type="text" class="input-sm form-control" name="startTime" value="${params.startTime}"  />
									    <span class="input-group-addon">~</span>
									    <input type="text" class="input-sm form-control" name="endTime" value="${params.endTime}" />
									</div>   
								</div>
                          	  <label class="col-sm-2 control-label">银行账号:</label>
							  <div class="col-sm-2"><input type="text" class="form-control" name="inAccNo" id="inAccNo"  ></div>
							</div> 
							<div class="form-group" > 
							    
								<label class="col-sm-2 control-label">代理商名称:</label>
		                           <div class="col-sm-3">
                                   <select id="settleUserNo" class="form-control" name="settleUserNo">
									</select>
								</div>
								<label class="col-sm-2 control-label">结算状态：</label>
								<div class="col-sm-2">
									<select class="form-control" name="settleStatus" id="settleStatus">
										<option value="" selected="selected">全部</option>
										<option value="0">未结算</option>
										<option value="1">已结算</option>
										<option value="2">结算中</option>
										<option value="3">结算失败</option>
										<option value="4">转T1结算</option>
										<option value="5">不结算</option>
										<option value="6">已返鼓励金</option>
									</select>
								</div>
							</div>
						 <div class="form-group" >
							 <label class="col-sm-2 control-label">出款状态：</label>
							 <div class="col-sm-2">
								 <select class="form-control" name="status" id="status">
									 <option value="" selected="selected">全部</option>
									 <option value="0">未提交</option>
									 <option value="1">已提交</option>
									 <option value="2">提交失败</option>
									 <option value="3">超时</option>
									 <option value="4">交易成功</option>
									 <option value="5">交易失败</option>
									 <option value="6">未知</option>
								 </select>
							 </div>
						 </div>
                            <div class="clearfix lastbottom"></div>
                            <div class="form-group" style="margin-bottom:0">
                                    <label class="col-sm-2 control-label aaa"></label>
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                   	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                       	<button class="btn btn-success" type="submit"><span class="glyphicon gly-search"></span>查询</button>
                                       	<sec:authorize access="hasAuthority('agentsProfitToCash:export')">
                                       	<button class="btn btn-danger col-sm-offset-14" type="button" onclick="exportExcel()"><span class="glyphicon gly-out"></span>导出</button>
                                       	</sec:authorize>
                                       	<button class="btn btn-default col-sm-offset-14" type="reset" id="reset"><span class="glyphicon gly-trash"></span>清空</button>
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
					<table id="table_list_agentsProfitToCash"></table>
					<div id="pager_list_agentsProfitToCash"></div>
                    <br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
					</div>
				</div>
			</div>
		</div>
		
	</div>
	<!-- 填充内容结束 -->
		
</body>
    
<title>
	<script src="${ctx}/js/icheck.min.js"></script>
<script src="${ctx}/js/custom.min.js"></script>
<script src="${ctx}/js/plugins/select2/select2.full.min.js"></script>
<script src="${ctx}/js/plugins/select2/i18n/zh-CN.js"></script>
<!-- Sweet alert -->
<script src="${ctx}/js/plugins/sweetalert/sweetalert.min.js"></script>
<script src="${ctx}/js/plugins/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
<script src="${ctx}/js/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js"></script>
	<script type="text/javascript">

	// 去除空格啊
	$('input').blur(function() {
		replaceSpace(this);
	})
	function replaceSpace(obj) {
		obj.value = obj.value.replace(/\s/gi, '')
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
			var data = $("#agentsProfitSettleOrderForm").serialize();
			$.download('${ctx}/agentsProfitAction/exportAgentsProfitToCashList.do',data,'post');
	    }
	
		$("#agentsProfitSettleOrderForm").submit(function() {
			$("#table_list_agentsProfitToCash").setGridParam({
				datatype : 'json',
				page : 1
			//Replace the '1' here
			}).trigger("reloadGrid");
			return false;
		});
		
		function getParams(o) {
			var data = $("#agentsProfitSettleOrderForm").serializeArray();
			$.each(data, function() {
				o[this.name] = this.value || '';
			});
			o.settleUserNo = $("#settleUserNo").select2("val");
			console.info($("#settleUserNo").select2("val"));
		}
		


		function customStatusFormatter(cellvalue, options, rowObject){
			if(cellvalue == '0'){
				return  "未提交";
			}else if(cellvalue =='1'){
				return "已提交";
			}else if(cellvalue == '2'){
				return  "提交失败";
			}else if(cellvalue == '3'){
                return  "超时";
            }else if(cellvalue == '4'){
                return  "交易成功";
            }else if(cellvalue == '5'){
                return  "交易失败";
            }else if(cellvalue == '6'){
                return  "未知";
            }
		 	
		 return "" ;
		}

    function customSettleStatusFormatter(cellvalue, options, rowObject){
        if(cellvalue == '0'){
            return  "未结算";
        }else if(cellvalue =='1'){
            return "已结算";
        }else if(cellvalue == '2'){
            return  "结算中";
        }else if(cellvalue == '3'){
            return  "结算失败";
        }else if(cellvalue == '4'){
            return  "转T1结算";
        }else if(cellvalue == '5'){
            return  "不结算";
        }else if(cellvalue == '6'){
            return  "已返鼓励金";
        }else {
            return  "未知";
        }
        return "" ;
    }
		function addCellAttr(rowId, val, rawObject, cm, rdata) {
			if ("结算失败"==val||"交易失败"==val) {
				return "style='color:red'";
			}
		}


    function customSubType(cellvalue, options, rowObject){
        if(cellvalue == '4'){
            return  "活动补贴出款";
        }else if(cellvalue =='5'){
            return "代理商分润";
        }else{
            return cellvalue ;
		}

    }
		
		$(document).ready(function() {
			$newOption = $("<option></option>").val("${params.settleUserNo}").text("${params.agentName}")
			$("#settleUserNo").append($newOption).trigger('change');

			var lastsel;
			//var data = $("#agentsProfitSettleOrderForm").serialize();
			
			// 初始化表格
			$("#table_list_agentsProfitToCash")
					.jqGrid({url : "${ctx}/agentsProfitAction/findAgentsProfitToCashList.do",
								datatype : "local",
								mtype : "POST",
								height:"auto",
								autowidth: true,
								shrinkToFit: false,
								autoScroll: false,
								rowNum: 10,
								rowList: [ 10, 20 ],
								colNames : [
									'创建时间',
									'代理商编号',
									'代理商名称',
									'结算金额',
									'手续费',
									'出款子类型',
                                    '结算状态',
									'出款状态',
									'开户名',
									'银行账号',
									'备注' ],
								colModel : [
										{
											name : 'createTime',
											index : 'createTime',
											width : 180,
											align : "right",
											formatter : function(val) {
												return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");
											 }
										},
										{
											name : 'settleUserNo',
											index : 'settleUserNo',
											width : 150,
											align : "right"
										},
										{
											name : 'agentName',
											index : 'agentName',
											width : 150,
											align : "right"
										},
										{
											name : 'settleAmount',
											index : 'settleAmount',
											width : 100,
											align : "right"
										},
										{
											name : 'feeAmount',
											index : 'feeAmount',
											width : 100,
											align : "right"

										},
										{
											name : 'subType',
											index : 'subType',
											width : 100,
											align : "right",
                                            formatter:customSubType,

										},
										{
											name : 'settleStatus',
											index : 'settleStatus',
											width : 100,
											align : "right",
											formatter:customSettleStatusFormatter,
                                            cellattr: addCellAttr
										},
										{
											name : 'status',
											index : 'status',
											width : 100,
											align : "right",
											formatter:customStatusFormatter,
                                            cellattr: addCellAttr
										},
										{
											name : 'inAccName',
											index : 'inAccName',
											width : 100,
											align : "right"
										},
										{
											name : 'inAccNo',
											index : 'inAccNo',
											width : 180,
											align : "right"
										},
										{
											name : 'settleMsg',
											index : 'settleMsg',
											width : 100,
											align : "right"
										}
										 ],
								multiselect : false,//支持多项选择
								multiselectWidth : 80,
								multiboxonly: false,
								pager : "#pager_list_agentsProfitToCash",
								viewrecords : true,
								hidegrid : false,
								reloadAfterSubmit: true,
								jsonReader : {
									root : "result",
									total : "totalPages",
									page : "pageNo",
									pageSize : "pageSize",
									records : "totalCount",
									repeatitems : false
								},
								prmNames : {
									page : "pageNo",
									rows : "pageSize"
								},
								serializeGridData : function(postData) {
									getParams(postData);
									return postData;
								},
								gridComplete : function() { //在此事件中循环为每一行添加修改和删除链接
									//hideCheckBox();
								},
							});
					jQuery("#table_list_agentsProfitToCash").jqGrid('setFrozenColumns');
					$(window).bind('resize',function() {
						var width = $('.jqGrid_wrapper').width();
						$('#table_list_agentsProfitToCash').setGridWidth(width);
					});
            
            $('.input-daterange').datepicker({
                format: "yyyy-mm-dd",
                language: "zh-CN",
                todayHighlight: true,
                autoclose: true,
                clearBtn: true
            });
		});
		 $("#reset").on("click", function () { 
	            //$exampleMulti.val(null).trigger("change");
	            $("#collectionBatchNo").val("");
	            $("#collectionBatchNo").attr("value","");
	            $("#settleUserNo").empty().trigger("change");
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
	
	   // turn the element to select2 select style
	      $('#settleUserNo').select2({
		    	  		ajax: {
			    		  	url: "${ctx}/agentsProfitAction/queryAgentName.do",
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
		    		  placeholder: "选择代理商名称",  
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

	</script>
	 </title>
</html>  
      