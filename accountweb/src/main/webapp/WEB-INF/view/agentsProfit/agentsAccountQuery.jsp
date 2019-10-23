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
	<link href="${ctx}/css/plugins/bootstrap-datepicker/bootstrap-datepicker3.min.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
                <div class="pull-left">当前位置</div>
                <em class=""></em>
                <div class="pull-left">代理商分润管理</div>
                <em class=""></em>
                <div class="pull-left active">代理商分润账户查询</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="agentsProfitAccountForm">
                          <div class="form-group" >
                          	 
								<label class="col-sm-2 control-label">代理商名称:</label>
		                           <div class="col-sm-3">
                                   <select id="agentNo" class="form-control" name="agentNo">
									</select>
								</div>

							  <label class="col-sm-2 control-label">代理商级别:</label>
							  <div class="col-sm-2">
								  <select class="form-control" name="agentLevel" id="agentLevel">
									  <option value="ALL" selected="selected">全部</option>
									  <option value="1">1级</option>
									  <option value="2">2级</option>
									  <option value="3">3级</option>
									  <option value="4">4级</option>
									  <option value="5">5级</option>
									  <option value="6">6级</option>
									  <option value="7">7级</option>
									  <option value="8">8级</option>
									  <option value="9">9级</option>
									  <option value="10">10级</option>
									  <option value="11">11级</option>
									  <option value="12">12级</option>
									  <option value="13">13级</option>
									  <option value="14">14级</option>
									  <option value="15">15级</option>
									  <option value="16">16级</option>
									  <option value="17">17级</option>
									  <option value="18">18级</option>
									  <option value="19">19级</option>
									  <option value="20">20级</option>
								  </select>
							  </div>

						  </div>

                            <div class="clearfix lastbottom"></div>
                            <div class="form-group" style="margin-bottom:0">
                                    <label class="col-sm-2 control-label aaa"></label>
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                   	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                       	<button class="btn btn-success" type="submit"><span class="glyphicon gly-search"></span>查询</button> 
                                       	<button class="btn btn-default col-sm-offset-14" type="reset" id="reset"><span class="glyphicon gly-trash"></span>清空</button>
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


						<span>汇总：开通返现：<span id="allOpenBackAmount">0.00</span> ,&nbsp;&nbsp;费率差异：<span  id="allRateDiffAmount">0</span>,&nbsp;&nbsp;超级推成本：<span  id="allTuiCostAmount">0.00</span>,&nbsp;&nbsp;
							风控扣款预扣款：<span  id="allRiskSubAmount">0.00</span>,&nbsp;&nbsp;商户管理费预扣款：<span  id="allMerMgAmount">0.00</span>,&nbsp;&nbsp;  保证金预扣款：<span id="allBailSubAmount">0.00</span> </span>
							&nbsp;&nbsp; <br/> 其他预扣款：<span id="allOtherAmount">0.00</span> </span>,&nbsp;&nbsp;  机具款预冻结：<span id="allTerminalFreezeAmount">0.00</span> </span>
							,&nbsp;&nbsp;  其他预冻结：<span id="allOtherFreezeAmount">0.00</span> </span>,&nbsp;&nbsp;  已冻结金额：<span id="allControlAmount">0.00</span> </span>
							,&nbsp;&nbsp;  分润账户余额：<span id="allCurrBalance">0.00</span> </span>,&nbsp;&nbsp;  分润账户可用余额：<span id="allAvailBalance">0.00</span> </span>
					,&nbsp;&nbsp;  活动补贴账户已冻结金额：<span id="allControlAmount2">0.00</span> </span>
					,&nbsp;&nbsp;  活动补贴账户余额：<span id="allCurrBalance2">0.00</span> </span>,&nbsp;&nbsp;  活动补贴账户可用余额：<span id="allAvailBalance2">0.00</span> </span>
					<br/>
					<br/>

					<div class="jqGrid_wrapper">
					<table id="table_list_agentsProfitAccount"></table>
					<div id="pager_list_agentsProfitAccount"></div>
                    <br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
					</div>
				</div>
			</div>
		</div>
		
	</div>
	
	
	<div class="modal inmodal" id="myModalUnfreeze" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
        	<div class="modal-content">
            	<div class="modal-header">
                	<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                	<h5 class="modal-title">解冻</h5>
                </div>
                <div class="modal-body"  >
                	<form method="post" class="form-horizontal" id="unfreezeForm">
                		<div class="form-group">
							<label class="col-sm-4 control-label">代理商编号:</label>
	                        <div class="col-sm-4">
								<input type="text" class="input-sm form-control" name="unfreezeAgentNo" id="unfreezeAgentNo" readonly="readonly"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label">代理商名称:</label>
	                        <div class="col-sm-4">
								<input type="text" class="input-sm form-control" name="unfreezeAgentName" id="unfreezeAgentName" readonly="readonly"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label">机具冻结款:</label>
	                        <div class="col-sm-4">
								<input type="number" class="input-sm form-control" name="terminalFreezeAmount" id="terminalFreezeAmount" readonly="readonly"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label">其他冻结款:</label>
	                        <div class="col-sm-4">
								<input type="number" class="input-sm form-control" name="otherFreezeAmount" id="otherFreezeAmount" readonly="readonly"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label">分润账户已冻结金额:</label>
	                        <div class="col-sm-4">
								<input type="number" class="input-sm form-control" name="controlAmount" id="controlAmount" readonly="readonly"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label">活动补贴账户已冻结金额:</label>
							<div class="col-sm-4">
								<input type="number" class="input-sm form-control" name="activityControlAmount" id="activityControlAmount" readonly="readonly"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label">总冻结金额:</label>
	                        <div class="col-sm-4">
								<input type="number" class="input-sm form-control" name="sumControlAmount" id="sumControlAmount" readonly="readonly"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label">解冻金额:</label>
	                        <div class="col-sm-4">
								<input type="number" class="input-sm form-control" name="amount" id="amount" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4 control-label">备注:</label>
	                        <div class="col-sm-4">
								<input type="text" class="input-sm form-control" name="remark" id="remark" />
							</div>
						</div>
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					</form>
         		</div>
				<div class="modal-footer">
					<sec:authorize access="hasAuthority('agentsAccountQuery:unfreeze')">
					<button type="button" class="btn btn-success" onclick="saveAgentsProfitUnfreeze()">确认</button>
					</sec:authorize>
            		<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
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
			var data = $("#agentsProfitAccountForm").serialize();
			$.download('${ctx}/agentsProfitAction/agentsAccountExport.do',data,'post');
	    }
	
		$("#agentsProfitAccountForm").submit(function() {
			$("#table_list_agentsProfitAccount").setGridParam({
				datatype : 'json',
				page : 1
			//Replace the '1' here
			}).trigger("reloadGrid");
            //汇总
            collection();
            return false;
		});
		
		function getParams(o) {
			var data = $("#agentsProfitAccountForm").serializeArray();
			$.each(data, function() {
				o[this.name] = this.value || '';
			});
			o.agentNo = $("#agentNo").select2("val");
			console.info($("#agentNo").select2("val"));
		}
		
		//解冻
		function unfreeze(id) {
			$("#myModalUnfreeze").modal("show");
            $("#unfreezeAgentNo").val("");
            $("#unfreezeAgentName").val("");
            $("#terminalFreezeAmount").val("");
            $("#otherFreezeAmount").val("");
            $("#controlAmount").val("");
            $("#activityControlAmount").val("");
            $("#sumControlAmount").val("");

			var model = $("#table_list_agentsProfitAccount").jqGrid('getRowData', id);
			
			var _agentNo = model.agentNo;
			var _agentName = model.agentName;
			var terminalFreezeAmount = Number(model.terminalFreezeAmount);
			var otherFreezeAmount = Number(model.otherFreezeAmount);
			var controlAmount = Number(model.controlAmount);		//分润账户
			var activityControlAmount = Number(model.activitySubsidyFreeze);	//活动补贴
			var sumControlAmount = (Number(terminalFreezeAmount) + Number(otherFreezeAmount) + Number(controlAmount) + Number(activityControlAmount)).toFixed(2);
			
			//alert(agentNo);
			$("#unfreezeAgentNo").val(_agentNo);
			$("#unfreezeAgentName").val(_agentName);
			$("#terminalFreezeAmount").val(terminalFreezeAmount);
			$("#otherFreezeAmount").val(otherFreezeAmount);
			$("#controlAmount").val(controlAmount);
			$("#activityControlAmount").val(activityControlAmount);
			$("#sumControlAmount").val(sumControlAmount);
		};

		//汇总
		function collection(){

			$.ajax({
				cache: false,
				type: "POST",
				url:"${ctx}/agentsProfitAction/findAgentsAccountCollection.do",
				data:$('#agentsProfitAccountForm').serialize(),// formid
				async: false,
				success: function(data) {
					$("#allOpenBackAmount").html(data.allOpenBackAmount);
					$("#allRateDiffAmount").html(data.allRateDiffAmount);
					$("#allTuiCostAmount").html(data.allTuiCostAmount);
					$("#allRiskSubAmount").html(data.allRiskSubAmount);
					$("#allMerMgAmount").html(data.allMerMgAmount);
					$("#allBailSubAmount").html(data.allBailSubAmount);
					$("#allOtherAmount").html(data.allOtherAmount);
					$("#allTerminalFreezeAmount").html(data.allTerminalFreezeAmount);
					$("#allOtherFreezeAmount").html(data.allOtherFreezeAmount);
					$("#allControlAmount").html(data.allControlAmount);
					$("#allCurrBalance").html(data.allCurrBalance);
					$("#allAvailBalance").html(data.allAvailBalance);
                    $("#allControlAmount2").html(data.allControlAmount2);
                    $("#allCurrBalance2").html(data.allCurrBalance2);
                    $("#allAvailBalance2").html(data.allAvailBalance2);
				}
			});

		}

		$(document).ready(function() {
			var lastsel;
			var data = $("#agentsProfitAccountForm").serialize();
			// 初始化表格
			$("#table_list_agentsProfitAccount")
					.jqGrid({url : "${ctx}/agentsProfitAction/findAgentsProfitAccountList.do",
								datatype : "local",
								mtype : "POST",
								height:"auto",
								autowidth: true,
								shrinkToFit: false,
								autoScroll: false,
								rowNum: 10,
								rowList: [ 10, 20 ],
								colNames : ['代理商名称','代理商编号','代理商级别','开通返现 ','费率差异 ','超级推成本 ','风控扣款预扣款 ','商户管理费预扣款 ','保证金预扣款 ','其他预扣款 ',
									'机具款预冻结 ','其他预冻结 ','分润账户已冻结金额 ','分润账户余额','分润账户可用余额','活动补贴账户已冻结金额','活动补贴账户余额','活动补贴账户可用余额', '操作'],
								colModel : [
										{name : 'agentName',index : 'agentName',width : 200,align : "right"}, 
										{name : 'agentNo',index : 'agentNo',width : 150,align : "right"},
                                    	{name : 'agentLevel',index : 'agentLevel',width : 100,align : "right"},
										{name : 'openBackAmount',index : 'openBackAmount',width : 180,align : "right",formatter : 'number'},
										{name : 'rateDiffAmount',index : 'rateDiffAmount',width : 180,align : "right",formatter : 'number'},
										{name : 'tuiCostAmount',index : 'tuiCostAmount',width : 180,align : "right",formatter : 'number'},
										{name : 'riskSubAmount',index : 'riskSubAmount',width : 180,align : "right",formatter : 'number'},
										{name : 'merMgAmount',index : 'merMgAmount',width : 180,align : "right",formatter : 'number'},
										{name : 'bailSubAmount',index : 'bailSubAmount',width : 180,align : "right",formatter : 'number'},
										{name : 'otherAmount',index : 'otherAmount',width : 180,align : "right",formatter : 'number'},
										{name : 'terminalFreezeAmount',index : 'terminalFreezeAmount',width : 180,align : "right",formatter : 'number'},
										{name : 'otherFreezeAmount',index : 'otherFreezeAmount',width : 180,align : "right",formatter : 'number'},
										{name : 'controlAmount',index : 'controlAmount',width : 180,align : "right",formatter : 'number'},
										{name : 'currBalance',index : 'currBalance',width : 180,align : "right",formatter : 'number'},
										{name : 'availBalance',index : 'availBalance',width : 180,align : "right",formatter : 'number'},
										{name : 'activitySubsidyFreeze',index : 'activitySubsidyFreeze',width : 180,align : "right",formatter : 'number'},
										{name : 'activitySubsidyBalance',index : 'activitySubsidyBalance',width : 180,align : "right",formatter : 'number'},
										{name : 'activitySubsidyAvailableBalance',index : 'activitySubsidyAvailableBalance',width : 180,align : "right",formatter : 'number'},
										{name:'Detail',index:'id',width:100,align:"center",sortable:false, },
										 ],
								multiselect : false,//支持多项选择
								multiselectWidth : 80,
								multiboxonly: false,
								pager : "#pager_list_agentsProfitAccount",
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
									var ids=$("#table_list_agentsProfitAccount").jqGrid('getDataIDs');
				                    for(var i=0; i<ids.length; i++){
				                        var id=ids[i];   
				                        var detail = "" ;
				                        <sec:authorize access="hasAuthority('agentsAccountQuery:unfreeze')">
				                        detail += "<a href='javascript:void(0);'  title='解冻' onclick='unfreeze(" + id + ")' class='default-details'>解冻</a>";
				                        </sec:authorize>
				                        jQuery("#table_list_agentsProfitAccount").jqGrid('setRowData', ids[i], { Detail: detail });
				                    }
								},
							});
			jQuery("#table_list_agentsProfitAccount").jqGrid('setFrozenColumns');
			$(window).bind('resize',
					function() {
						var width = $('.jqGrid_wrapper').width();
						$('#table_list_agentsProfitAccount').setGridWidth(width);
					});
			
            $('.input-daterange').datepicker({
                format: "yyyy-mm-dd",
                language: "zh-CN",
                todayHighlight: true,
                autoclose: true,
                clearBtn: true
            });
            //汇总
            collection();
		});

		//点击汇总
		$("#collection").click(function() {
			$("#myModalCollection").modal("show");
		});

		 $("#reset").on("click", function () {
	            //$exampleMulti.val(null).trigger("change");
	            //$('#agentNo').select2("val", "ALL");
	            $("#agentNo").empty().trigger("change");
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
	      $('#agentNo').select2({
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
		    		  minimumInputLength: 2,
		    		  language: "zh-CN",
		    		  templateResult: formatRepo, // omitted for brevity, see the source of this page
		    		  templateSelection: formatRepoSelection // omitted for brevity, see the source of this page
		    		}
	    		  );	
									
	      
			function saveAgentsProfitUnfreeze(){
				<sec:authorize access="hasAuthority('agentsAccountQuery:unfreeze')"> 
				//alert($('#unfreezeForm').serialize());
					$.ajax({
		                cache: false,
		                type: "POST",
		                url:"${ctx}/agentsProfitAction/saveAgentsProfitUnfreeze.do",
		                data:$('#unfreezeForm').serialize(),// formid
		                async: false,
		                success: function(msg) {
		                	if(!msg.status){
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
								setTimeout(function() {
									location.href='${ctx}/agentsProfitAction/agentsAccountQuery.do';
								}, 1000);
							}
		                }
		            });
					
				</sec:authorize>		
			}
	</script>
	 </title>
</html>  
      