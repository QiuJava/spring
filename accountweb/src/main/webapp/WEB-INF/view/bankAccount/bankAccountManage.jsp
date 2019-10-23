
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
		<div class="col-lg-10" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">银行账户管理</div>
            <em class=""></em>
            <div class="pull-left active">银行账户查询</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="bankAccountForm">
                          <div class="form-group">
                          	  <label class="col-sm-2 control-label">开户银行名称：</label>
                              <div class="col-sm-2"><input type="text" class="form-control" name="bankName" id="bankName"></div>
                             
                              <label class="col-sm-2 control-label">开户名：</label>
                              <div class="col-sm-2"><input type="text" class="form-control" name="accountName" id="accountName"> </div>
                            <label class="col-sm-2 control-label">开户账号：</label>
                            <div class="col-sm-2"><input type="text" class="form-control" name="accountNo" id="accountNo"> </div>
                            
                            </div>
                           <div class="form-group">
                                <label class="col-sm-2 control-label" >支付机构：</label>
                                <div class="col-sm-2">
                                    <select class="form-control" name="orgNo"> 
                                             <option value="ALL" selected="selected">全部</option>
                                             <c:forEach var="orgInfo" items="${orgInfos}">
                                                <option value="${orgInfo.orgNo}"
                                                <c:if test="${orgInfo.orgName == params.orgNo}">selected="selected"</c:if>>
                                                ${orgInfo.orgName}</option>
                                            </c:forEach>
                                        
                                        </select>  
                                 </div>  							
                                 <label class="col-sm-2 control-label">币种号：</label>
                                 <div class="col-sm-2">
   									<select class="form-control" name="currencyNo"> 
									         <option value="ALL" selected="selected">全部</option>
									         <c:forEach var="currencyInfo" items="${currencyInfos}">
												<option value="${currencyInfo.currencyNo}"
												<c:if test="${currencyInfo.currencyName == params.currencyNo}">selected="selected"</c:if>>
												${currencyInfo.currencyName}</option>
											</c:forEach>
										
										</select>  
								 </div>
 								 <label class="col-sm-2 control-label">账户类别：</label>
                                 <div class="col-sm-2">
   									<select class="form-control" name="accountType"> 
										<option value="ALL" selected="selected">全部</option>
										<c:forEach var="accountType" items="${accountTypeInfos}">
												<option value="${accountType.sysValue}"
												<c:if test="${accountType.sysValue == params.accountType}">selected="selected"</c:if>>
												${accountType.sysName}</option>
										</c:forEach>
									</select>  
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
		</div>
		
		<div class="col-lg-12">
			<div class="ibox ">
				<div class="ibox-content">
					<div class="jqGrid_wrapper">
					<table id="table_list_bankAccount"></table>
					<div id="pager_list_bankAccount"></div>
                    <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
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
		// 去除空格啊
        $('input').blur(function(){
            replaceSpace(this);
        })
        function replaceSpace(obj){
            obj.value = obj.value.replace(/\s/gi,'')
        }
		$("#bankAccountForm").submit(function(){
			$("#table_list_bankAccount").setGridParam({
			       datatype : 'json',
			       page : 1            //Replace the '1' here
			    }).trigger("reloadGrid");
			return false;
		});
		
		function getParams(o){
			var data=$("#bankAccountForm").serializeArray();
		     $.each(data, function() {    
		             o[this.name] = this.value || '';    
		     });   
		     //o.subjectNo = $("#subjectNo").select2("val");
		}
		
		
		function customOrgNoFmatter(cellvalue, options, rowObject){  
			<c:forEach var="orgNo" items="${orgInfos}">
				  if(cellvalue == '${orgNo.orgNo}'){
					  return '${orgNo.orgName}';
				  }
			 </c:forEach>	
			 return "" ;
		}  
		function customCurrencyNoFmatter(cellvalue, options, rowObject){  
			<c:forEach var="currencyNo" items="${currencyInfos}">
				  if(cellvalue == '${currencyNo.currencyNo}'){
					  return '${currencyNo.currencyName}';
				  }
			 </c:forEach>	
			 return "" ;
		}  
		function customAccountStatesFmatter(cellvalue, options, rowObject){  
			/* switch(cellvalue){
				case "1":return "正常" ;break;
				case "2":return "销户" ;break ;
				case "3":return "冻结" ;
				default :return "空" ;
			} */
			<c:forEach var="accountStatus" items="${accountStatusInfos}">
				  if(cellvalue == '${accountStatus.sysValue}'){
					  return '${accountStatus.sysName}';
				  }
			 </c:forEach>
			return "" ;
		}
		function customAccountTypeFmatter(cellvalue, options, rowObject){  
			<c:forEach var="accountType" items="${accountTypeInfos}">
				  if(cellvalue == '${accountType.sysValue}'){
					  return '${accountType.sysName}';
				  }
			 </c:forEach>	
			 return "" ;
		}  
		 
		
		
		
		$(document).ready(function() {
			var lastsel;
			//var sequenceNo = 0 ;
			var data=$("#bankAccountForm").serialize();
			//alert(data);alert('${list[0].getUserType}');
            // 初始化表格
            $("#table_list_bankAccount").jqGrid({
            	url:"${ctx}/bankAccountAction/findBankAccountList.do",
                datatype: "json",	
                mtype: "POST",
                height: 'auto',
                autowidth: true,
                shrinkToFit:false,
                autoScroll: false,
                rowNum: 10,
                rowList: [10, 20],
                colNames:['操作','支付机构',  '开户银行名称','开户名','开户账号','币种号','账户类别','状态','对应内部账号','余额','可用余额',],
                colModel: [
                    {name:'Detail',index:'Id',width:210,align:"center",sortable:false,frozen:true},
                    {name: 'orgNo', index: 'orgNo', width: 150, align: "left", sortable:false, formatter:  customOrgNoFmatter},
                    {name: 'bankName', index: 'bankName',width: 150, align: "left", sortable:false},
                    {name: 'accountName', index: 'accountName',width: 150, align: "left", sortable:false},
                    {name: 'accountNo', index: 'accountNo',width: 220, align: "left", sortable:false},
                    {name: 'currencyNo', index: 'currencyNo',width: 150, align: "center", sortable:false, formatter:  customCurrencyNoFmatter},
                    {name: 'accountType', index: 'accountType',width: 150, align: "left", sortable:false, formatter:  customAccountTypeFmatter},
                    {name: 'insAccount.accountStatus', index: 'accountStatus',width: 150, align: "center", sortable:false, formatter:  customAccountStatesFmatter},
                    {name: 'insAccountNo', index: 'insAccountNo',width: 300, align: "left", sortable:false},
                    {name: 'insAccount.currBalance', index: 'currBalance',width: 150, align: "right",formatter: 'number'},
                    {name: 'insAccount.availBalance', index: 'availBalance',width: 150, align: "right",formatter: 'number'},
                    
                    
                ],
                onSelectRow: function(id){
            		if(id && id!==lastsel){
            			jQuery('#table_list_user').jqGrid('restoreRow',lastsel);
            			jQuery('#table_list_user').jqGrid('editRow',id,true);
            			lastsel=id;
            		}
            	},
                multiselect: false,//支持多项选择
                pager: "#pager_list_bankAccount",
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
                    var ids=$("#table_list_bankAccount").jqGrid('getDataIDs');
                    for(var i=0; i<ids.length; i++){
                        var id=ids[i];   
                       	var insAccountNoq = $("#table_list_bankAccount").jqGrid('getCell',id,'insAccountNo') ;
                        var detail = "" ;
                        detail += "<a href='${ctx}/bankAccountAction/toFindBankAccountById.do?id="+ id +"' class='default-details' title='详情'>详情</a>";
                        <sec:authorize access="hasAuthority('bankAccountManage:update')">
                        	detail += "&nbsp;&nbsp;<a href='${ctx}/bankAccountAction/toBankAccountUpdatePage.do?id="+ id +"' class='default-delete'  title='修改'>修改</a>";
                        </sec:authorize>
                        detail += "&nbsp;&nbsp;<a href='${ctx}/insAccountAction/insAccountDetailQuery.do?forwardTo=2&accountNo="+ insAccountNoq +"' class='default-details'  title='交易明细'>交易明细</a>";
                        jQuery("#table_list_bankAccount").jqGrid('setRowData', ids[i], { Detail: detail });
                    }
                }
            });
            jQuery("#table_list_bankAccount").jqGrid('setFrozenColumns');
            // Add responsive to jqGrid
            $(window).bind('resize', function () {
                var width = $('.jqGrid_wrapper').width();
                $('#table_list_bankAccount').setGridWidth(width);
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
      