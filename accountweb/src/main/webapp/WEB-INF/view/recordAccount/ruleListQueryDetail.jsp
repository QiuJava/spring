
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
		<div class="col-lg-10" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">记账处理</div>
            <em class=""></em>
            <div class="pull-left active">查看记账规则</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
                             <div class="row">
					            <div class="col-lg-6"  style="margin-left: 4%;margin-right: 4%">
					                <div class="ibox float-e-margins">
					                    <div class="ibox-content">
					                            <table class="col-sm-5">
						                            <tr >
						                                <td width="50"><label>记账规则名称：</label></td>
						                                <td width="30%" style="line-height:23px">${rule.ruleName }</td>
						                            </tr>
						                            <tr> 
						                            	<td width="50"><label>记账程序：</label></td>
						                                <td style="line-height:23px">${rule.program }</td>
						                                </tr>
						                            <tr>
						                                <td width="50"><label>说&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;明：</label></td>
						                                <td colspan="2" style="line-height:23px">${rule.remark }</td>
						                            </tr>
						                          </table>
					                    </div>
					                     <div class="ibox-content">
			                                     <ul class="unstyled">
						                            <li>A 为发生金额（交易金额，提现金额等） </li>
						                            <li>B 为商户交易服务手续费</li>
						                            <li>C 为代理商交易服务分润金额</li>
						                            <li>D 为收单机构收单服务费率</li>
						                            <li>E 为收单机构出款服务费率1（服务本身的费用）</li>
						                            <li>F 为收单机构出款服务费率2（代付垫资资金成本）</li>
						                            <li>G 为商户提现服务手续费</li>
						                            <li>H 为代理商提现服务分润金额</li>
						                            <li>I 为代理商提现服务手续费</li>
						                            <li>J 为一级商户超级推分润</li>
						                            <li>K 为二级商户超级推分润</li>
						                            <li>L 为三级商户超级推分润</li>
						                            <li>M 为本级商户超级推分润</li>
						                        </ul>
					                     </div>
					                </div>
					            </div>
					            </div>
                            
                            <div class="" style="margin-left: 5%;margin-right: 5%;">
					                                 <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					                                 <button id="returnUp" type="button" class=" btn btn-default " onclick="window.location.href='${ctx}/recordAccountAction/toRecordAccountRuleListQuery.do?queryParams=${params.queryParams}'" value="" /><span class="glyphicon gly-return"></span>返回</button>
					         </div>
                            
                </div>
			</div>
		</div>
		
		<div class="col-lg-12">
			<div class="ibox ">
				<div class="ibox-content" style="padding-top:0">
					<div class="jqGrid_wrapper" style="margin-left: 5%;margin-right: 5%">
					<table id="table_list_recordAccount"></table>
					<div id="pager_list_recordAccount"></div>
					</div>
				</div>
			</div>
		</div>


        
        
                     
		
	</div>
	<!-- 填充内容结束 -->
		
</body>
    
	<title>
	<script src="${ctx}/js/plugins/select2/select2.full.min.js"></script>
	<script src="${ctx}/js/plugins/select2/i18n/zh-CN.js"></script>
	<script type="text/javascript">
		
		function customDebitCreditSideFmatter(cellvalue, options, rowObject){  
			<c:forEach var="balanceFrom" items="${balanceFromList}">
				  if(cellvalue == '${balanceFrom.sysValue}'){
					  return '${balanceFrom.sysName}';
				  }
			 </c:forEach>
			 return "" ;
		}  
		
		function customAccountFlagFmatter(cellvalue, options, rowObject){  
			<c:forEach var="accountFlag" items="${accountFlagList}">
				  if(cellvalue == '${accountFlag.sysValue}'){
					  return '${accountFlag.sysName}';
				  }
			 </c:forEach>
			 return "" ;
		}  
		
		function customSubjectNameFmatter(cellvalue, options, rowObject){  
			<c:forEach var="subject" items="${subjectList}">
				  if(cellvalue == '${subject.subjectNo}'){
					  return '${subject.subjectName}';
				  }
			 </c:forEach>
			 return "" ;
		}  
		
		function customDebitCreditFlagFmatter(cellvalue, options, rowObject){  
			switch(cellvalue){
				case "yes":return "是" ;break;
				case "no":return "否" ;break ;
				default :return "" ; 
			}
		}  

		function customAccountTypeFmatter(cellvalue, options, rowObject){  
			<c:forEach var="accountType" items="${accountTypeList}">
				  if(cellvalue == '${accountType.sysValue}'){
					  return '${accountType.sysName}';
				  }
			 </c:forEach>
			 return "" ;
		}  

		function customCurrencyNameFmatter(cellvalue, options, rowObject){  
			<c:forEach var="currency" items="${currencyList}">
				  if(cellvalue == '${currency.currencyNo}'){
					  return '${currency.currencyName}';
				  }
			 </c:forEach>
			 return "" ;
		}  
		
		//加载数据要用到的参数
		var postData={ruleNo:"${rule.ruleNo}","${_csrf.parameterName}":"${_csrf.token}"};
		
		$(document).ready(function() {
			var lastsel;
            // 初始化表格
            $("#table_list_recordAccount").jqGrid({
            	url:"${ctx}/recordAccountAction/findRuleConfigList.do",
                datatype: "json",
                mtype: "POST",
                postData: postData ,
                height: 'auto',
                autowidth: true,
                shrinkToFit:false,
                autoScroll: false,
                rowNum: 10,
                rowList: [10, 20],
                colNames:['分录号','子交易标识号',  '借贷标识','内/外部账','外部账用户类型','科目编号','科目名称','币种号','检查借贷平衡','金额','说明'],
                colModel: [
                    {name: 'journalNo', index: 'journalNo', width: 90, align: "left", sortable:false},
                    {name: 'childTransNo', index: 'childTransNo', width: 120, align: "left", sortable:false},
                    {name: 'debitCreditSide', index: 'debitCreditSide',width: 90, align: "center", sortable:false, formatter:  customDebitCreditSideFmatter},
                    {name: 'accountFlag', index: 'accountFlag',width: 90, align: "center", sortable:false, formatter:  customAccountFlagFmatter},
                    {name: 'accountType', index: 'accountType',width: 150, align: "left", sortable:false, formatter:  customAccountTypeFmatter},
                    {name: 'subjectNo', index: 'subjectNo',width: 200, align: "left", sortable:false},
                    {name: 'subjectNo', index: 'subjectName',width: 200, align: "left", sortable:false, formatter:  customSubjectNameFmatter},
                    {name: 'currencyNo', index: 'currencyNo',width: 70, align: "center", sortable:false, formatter:  customCurrencyNameFmatter},
                    {name: 'debitCreditFlag', index: 'debitCreditFlag',width: 100, align: "center", sortable:false, formatter:  customDebitCreditFlagFmatter},
                    {name: 'amount', index: 'amount',width: 100, align: "center", sortable:false},
                    {name: 'remark', index: 'remark',width: 300, align: "center", sortable:false},
                    
                ],
                onSelectRow: function(id){
            		if(id && id!==lastsel){
            			jQuery('#table_list_user').jqGrid('restoreRow',lastsel);
            			jQuery('#table_list_user').jqGrid('editRow',id,true);
            			lastsel=id;
            		}
            	},
                multiselect: false,//支持多项选择
                pager: "#pager_list_recordAccount",
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
        		}
            });
            // Add responsive to jqGrid
            $(window).bind('resize', function () {
                var width = $('.jqGrid_wrapper').width();
                $('#table_list_recordAccount').setGridWidth(width);
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
		
	</script>
	 </title>
</html>  
      