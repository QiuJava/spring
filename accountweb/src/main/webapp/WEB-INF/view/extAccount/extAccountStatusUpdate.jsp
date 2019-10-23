<%@page pageEncoding="utf8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<!-- jqGrid plugin -->
	<link href="${ctx}/css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/jqGrid/ui.jqgrid.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/select2/select2.min.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">外部账号管理</div>
            <em class=""></em>
            <div class="pull-left active">客户账户状态修改</div>
        </div>
	</div>
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
					 <form class="form-horizontal" id="accountStatusUpdateForm">
                          <div class="form-group">
                          	  	<label class="col-sm-2 control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;账号：</label>
                              	<div class="col-sm-2"><input type="number"   min="0" class="form-control" name="subjectNo"></div>
                              	<label class="col-sm-2 control-label">用户编号：</label>
                              	<div class="col-sm-2"><input type="text" class="form-control" name="extAccountInfo.userId"></div>
                              	<label class="col-sm-2 control-label">账户归属：</label>
                              	<div class="col-sm-2">
		              				<select class="form-control" name="orgNo"> 
												 <c:forEach var="orgInfo" items="${orgInfoList}">
												<option value="${orgInfo.orgNo}">${orgInfo.orgName}  ${orgInfo.orgNo}
												</option>
											</c:forEach>	
									</select>
							 	</div>
                           </div>
                                 
                           <div class="form-group">
                           		<label class="col-sm-2 control-label">内部科目编号：</label>
                                <div class="col-sm-2">
                                	<select id="subjectNo" class="form-control" name="subjectNo">
									</select>
								</div>
                                <label class="col-sm-2 control-label" >&nbsp;&nbsp;&nbsp;&nbsp;币种号：</label>
                                <div class="col-sm-2">
                             		<select class="form-control" name="currencyNo"> 
								         <c:forEach var="currency" items="${currencyList}">
											<option value="${currency.currencyNo}"
											<c:if test="${currency.currencyNo == params.subjectType}">selected="selected"</c:if>>
											${currency.currencyName}</option>
										</c:forEach>
									</select>
								</div>
                                <label class="col-sm-2 control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;卡号：</label>
                                <div class="col-sm-2"><input type="text" class="form-control" name="cardNo"></div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;账户状态：</label>
                                 <div class="col-sm-2">
 										<select class="form-control" name="accountStatus"> 
     												 <c:forEach var="accountStatus" items="${accountStatusList}">
												<option value="${accountStatus.sysValue}">${accountStatus.sysName}
												</option>
											</c:forEach>	
											</select>
 								 </div>		
								</div>
								<div class="form-group">
                                <!-- <div class="col-sm-4 col-sm-offset-13  "> -->
                                	<label class="col-sm-2 control-label aaa"></label>
                                 	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                      <button class="btn btn-success col-sm-offset-1" type="submit">&nbsp;&nbsp;&nbsp;查询&nbsp;&nbsp;&nbsp;</button>
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
								<table id="table_list_AccountStatusUpdate"></table>
								<div id="pager_list_AccountStatusUpdate"></div>
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
		
	      // turn the element to select2 select style
	      $('#subjectNo').select2({
		    	  		ajax: {
			    		  	url: "${ctx}/subjectAction/queryParentSubjectName.do",
			    		    dataType: 'json',
			    		    delay: 250,
			    		    data: function (params) {
			    		      return {
			    		        q: params.term, // search term
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
		    		  escapeMarkup: function (markup) { return markup; }, // let our custom formatter work
		    		  minimumInputLength: 1,
		    		  language: "zh-CN",
		    		  templateResult: formatRepo, // omitted for brevity, see the source of this page
		    		  templateSelection: formatRepoSelection // omitted for brevity, see the source of this page
		    		}
	    		  );
	      
			function Modify(id) {   //单击修改链接的操作         
				$("#table_list_AccountStatusUpdate").jqGrid('editRow',id);
			}
			function Save(id) {    
				var model = $("#table_list_AccountStatusUpdate").jqGrid('getRowData', id);
				$("#table_list_AccountStatusUpdate").jqGrid('saveRow',id,{  
		            keys : true,        //这里按[enter]保存  
		            url: "${ctx}/extAccountAction/saveExtAccountStatusUpdate.do",  
		            mtype : "POST",  
		            restoreAfterError: true,  
		            extraparam: {  
		                "accountNo": model.accountNo,
		                "${_csrf.parameterName}":"${_csrf.token}"
		            },  
		            oneditfunc: function(rowid){  
		                console.log(rowid);  
		            },  
		            succesfunc: function(response){  
		                alert("save success");  
		                return true;  
		            },  
		            errorfunc: function(rowid, res){  
		                console.log(rowid);  
		                console.log(res);  
		            }  
		        });
			}
			$("#accountStatusUpdateForm").submit(function(){
				$("#table_list_AccountStatusUpdate").trigger("reloadGrid");
				return false;
			});
			function getParams(o){
				var data=$("#accountStatusUpdateForm").serializeArray();
			     $.each(data, function() {    
			             o[this.name] = this.value || '';    
			     });
			     o.subjectNo = $("#subjectNo").select2("val");
			}
			function customCurrencyFmatter(cellvalue, options, rowObject){  
				<c:forEach var="currency" items="${currencyList}">
					  if(cellvalue == '${currency.currencyNo}'){
						  return '${currency.currencyName}';
					  }
				 </c:forEach>	
			}
			function getAccountStatusGroups(id) {  
			    var arr = []
			    <c:forEach var="accountStatus" items="${accountStatusList}">
			    arr.push('${accountStatus.sysValue}:${accountStatus.sysName}');
				 </c:forEach>	
			    return arr.join(';');  
			}
			$(document).ready(function() {
						var lastsel;
			            // 初始化表格
			            $("#table_list_AccountStatusUpdate").jqGrid({
			            	url:"${ctx}/extAccountAction/findAllAccountStatusUpdateInfo.do",
			                datatype: "json",
			                mtype: "POST",
			                height:"auto",
			                autowidth: true,
			                shrinkToFit: true,
			                rowNum: 10,
			                rowList: [10, 20],
			                colNames:['账号','用户编号','账户归属','内部科目编号','科目名称','币种号','卡号','账户状态','修改账户状态'],
			                colModel: [
			                    {name: 'accountNo', index: 'account_no', width: 190, sorttype: "int", align: "center"},
			                    {name: 'extAccountInfo.userId', index: 'user_id', width: 90, align: "center"},
			                    {name: 'orgNo', index: 'org_no', width: 100, align: "center"},
			                    {name: 'subjectNo', index: 'subject_no',width: 100, align: "center"},
			                    {name: 'subject.subjectName', index: 'subjectName',width: 80, align: "center"},
			                    {name: 'currencyNo', index: 'currency_no',width: 80, align: "center", formatter:  customCurrencyFmatter},
			                    {name: 'extAccountInfo.cardNo', index: 'card_no',width: 80, align: "center"},
			                    {name: 'accountStatus', index: 'account_status',width: 80,   formatter: "select", edittype:"select",editoptions: { value: getAccountStatusGroups() },editable: true},
			                    {name:'Detail',index:'detail',width:80,align:"center",sortable:false},
	              
			                ],
			                onSelectRow: function(id){
			            		if(id && id!==lastsel){
			            			jQuery('#table_list_AccountStatusUpdate').jqGrid('restoreRow',lastsel);
			            			jQuery('#table_list_AccountStatusUpdate').jqGrid('editRow',id,true);
			            			lastsel=id;
			            		}
			            	},
			                multiselect: false,//支持多项选择
			                pager: "#pager_list_AccountStatusUpdate",
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
			                    var ids=$("#table_list_AccountStatusUpdate").jqGrid('getDataIDs');
			                    for(var i=0; i<ids.length; i++){
			                        var id=ids[i];   
			                        var detail = "<a href='javascript:void(0);' class='default-details' title='操作' onclick='Modify(" + id + ")'>操作</a>";
			                        detail += "&nbsp;<a href='javascript:void(0);' class='default-maintenance' title='保存'  onclick='Save(" + id + ")'>保存</a>";
			                        jQuery("#table_list_AccountStatusUpdate").jqGrid('setRowData', ids[i], { Detail: detail });
			                    }
			                }
			            });
			            // Add responsive to jqGrid
			            $(window).bind('resize', function () {
			                var width = $('.jqGrid_wrapper').width();
			                $('#table_list_AccountStatusUpdate').setGridWidth(width);
			            });
			});
			</script>
		
	</title>

</html>

