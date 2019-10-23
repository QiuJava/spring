<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
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
            <div class="pull-left active">新建记账规则</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
				<div class="ibox-content">
					 <form:form class="form-horizontal" id="recordAccountForm" modelAttribute="recordAccountRule,recordAccountRuleConfig">
					 <%-- <form class="form-horizontal" id="recordAccountForm"> --%>
                            <div class="form-group">
                          	    <label class="col-sm-2 control-label">记账规则名称：</label>
                                <div class="col-sm-2"><input type="text" class="form-control" name="ruleName" id="ruleName" required></div>
                             
                                <label class="col-sm-2 control-label">记账程序：</label>  
                                <div class="col-sm-2"><input type="text" class="form-control" name="program" id="program"> </div>
                            </div>
                            <div class="form-group"> 
                              <label class="col-sm-2 control-label">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;说明：</label>
                              <div class="col-sm-2"><textarea rows="3" cols="30" class="form-control" name="remark" id="remark" ></textarea></div>
                            </div>
                            <div class="form-group lastbottom" style="overflow-x:scroll">
	                           	<table class="table " style="" id="recordAccountTable">
								   <thead>
								      <tr>
                                         <th width="400" style="text-align: center;">操作</th>
								         <th width="200" style="text-align: center;">分录号</th>
								         <th width="200" style="text-align: center;">子交易标识号</th>
								         <th width="270" style="text-align: center;">借贷标识</th>
								         <th width="400" style="text-align: center;">内/外部账</th>
								         <th width="400" style="text-align: center;">外部账用户类型</th>
								         <th width="500" style="text-align: center;">科目编号</th>
								         <th width="300" style="text-align: center;">币种号</th>
								         <th width="200" style="text-align: center;">检查借贷平衡</th>
								         <th width="250" style="text-align: center;">金额</th>
								         <th width="250" style="text-align: center;">说明</th>
								         
								      </tr>
								   </thead>
								   <tbody>
								      <tr>
                                         <td style="width:242px">
                                            <div align=center>
                                            <%int i = 100 ; i++ ;String strA = "other" ;String idA = strA+i ; %>
                                            <a id=<%=idA%> href='javascript:void(0);' class='default-details'  title='增加' onclick='Add(this)'>增加</a>
                                            <a href='javascript:void(0);' class='default-delete'  title='删除' onclick='DeleteRow(this)'>删除</a>
                                            </div>
                                         </td>
								         <td>
								         	<div align=center>
								         	<input type="text" class="form-control" name="recordAccountRuleConfig[0].journalNo"  required>
								         	</div>
								         </td>
								         <td>
								         	<div align=center>
								         	<input type="text" class="form-control" name="recordAccountRuleConfig[0].childTransNo"  required>
								         	</div>
								         </td>
								         <td>
								         	<div align=center>
								         	<select class="form-control" style="" name="recordAccountRuleConfig[0].debitCreditSide"> 
												<!--<option value="debit" selected="selected">借</option>
										        <option value="credit" >贷</option>
										        <option value="frozen" >冻结</option>
										        <option value="thaw" >解冻</option>-->
										        <c:forEach var="balanceFrom" items="${balanceFromList}">
													<option value="${balanceFrom.sysValue}">
													${balanceFrom.sysName}</option>
												</c:forEach>
											</select>   
											</div> 
								         </td>
								         <td>
								         	<div align=center>
								         	<select class="form-control" style="" name="recordAccountRuleConfig[0].accountFlag" onchange="disbaled($(this))"> 
												<c:forEach var="accountFlag" items="${accountFlagList}">
													<option value="${accountFlag.sysValue}"
													<c:if test="${accountFlag.sysValue == 0}"> selected="selected"</c:if>>
													${accountFlag.sysName}</option>
												</c:forEach>
											</select>
											</div>   
								         </td>
								         <td>
								         	<div align=center>
								         	<select class="form-control" style="" name="recordAccountRuleConfig[0].accountType"> 
												<c:forEach var="accountType" items="${accountTypeList}">
													<option value="${accountType.sysValue}"
													<c:if test="${accountType.sysValue == 'M'}"> selected="selected"</c:if>>
													${accountType.sysName}</option>
												</c:forEach>
											</select>
											</div>   
								         </td>
								         <td>
								         	   <div align=center>
								         	   <select id="subjectNo" autocomplete="off" class="form-control" name="recordAccountRuleConfig[0].subjectNo"></select>
								         	   </div>
								         </td>
								         <td>
								         	<div align=center>
								         	<select class="form-control" style="" name="recordAccountRuleConfig[0].currencyNo"> 
												<c:forEach var="currency" items="${currencyList}">
													<option value="${currency.currencyNo}">
													${currency.currencyName}</option>
												</c:forEach>
											</select>
											</div>   
								         </td>
								         <td>
								         	<div align=center>
								         	<select class="form-control" style="" name="recordAccountRuleConfig[0].debitCreditFlag"> 
												<option value="yes" selected="selected">是</option>
										        <option value="no" >否</option>
											</select> 
											</div>  
								         </td>
								         <td>
								         	<div align=center>
								         	<input type="text" class="form-control" name="recordAccountRuleConfig[0].amount"  required>
								         	</div>
								         </td>
								         <td>
								         	<div align=center>
								         		<textarea name="recordAccountRuleConfig[0].remark" rows="1" cols="15" style="height: 34px;resize:none;"></textarea> 
								         	</div>
								         </td>
								         
								      </tr>
								   </tbody>
								</table>
						 	</div>
                            <div class="clearfix lastbottom"></div>
                            <div class="form-group">
                              <!-- <div class="col-sm-9 col-sm-offset-13  "> -->
                                    <label class="col-sm-2 control-label aaa"></label>
                                   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                   <sec:authorize access="hasAuthority('recordAccountRule:insert')">                                   
                                        <button class="btn btn-success" type="submit"><span class="glyphicon gly-ok"></span>提交</button>
                                  </sec:authorize>
                                  
                              <!-- </div> -->
                              <div class="col-sm-2 pull-right">
                              	<p>&nbsp;</p>
                                    <p>I 为代理商提现服务手续费</p>
                                    <p>J 为一级商户超级推分润</p>
                                    <p>K 为二级商户超级推分润</p>
                                    <p>L 为三级商户超级推分润</p>
                                    <p>M 为本级商户超级推分润</p>
                                </div>
                                <div class="col-sm-3 pull-right">
                                    <p>备注：</p>
                                    <p>A 为发生金额（交易金额，提现金额等）</p>
                                    <p>B 为商户交易服务手续费</p>
                                    <p>C 为代理商交易服务分润金额</p>
                                    <p>D 为收单机构收单服务费率</p>
                                    <p>E 为收单机构出款服务费率1（服务本身的费用）</p>
                                    <p>F 为收单机构出款服务费率2（代付垫资资金成本）</p>
                                    <p>G 为商户提现服务手续费；</p>
                                    <p>H 为代理商提现服务分润金额；</p>
                                </div>
                                
                           </div>
                       	   
                       	   
                           <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br/>
                    <%-- </form> --%>
                    </form:form>
				</div>
			</div>
		</div>
		<!-- 
		<div class="col-lg-12">
						<div class="ibox ">
							<div class="ibox-content">
								<div class="jqGrid_wrapper">
								<table id="table_list_insAccountList"></table>
								<div id="pager_list_insAccountList"></div>
								</div>
							</div>
						</div>
				</div>
		 -->
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
	//控制  当   内/外标识   选中  内部账户  时，外部账用户类型  不可用
	function disbaled(a){
	    if (a.children('option:selected').val()==1) {
	        a.parents('td').next().find('select').prop("disabled","disabled");
	        a.parents('td').next().find('select').val("") ;
	    } else {
	        a.parents('td').next().find('select').prop("disabled","");
	        a.parents('td').next().find('select').find('option:first').prop("selected","selected");
	        //a.parents('td').next().find('select').find('option')[0].selected = true ;
	    }
	}


	//用来对subjectNo的id进行递增，以免重复
	var n = 0 ;
	var h = 0 ;		//用于新增行的  name值的下标控制
	
	/* 添加一行 */
	  function Add(obj){
		
		h = $("#recordAccountTable tr").length -1;			//包括标题行
		//再添加第一行时，h的值应该为0 ；但是h的值是在没有删除前获取的，所以h第一轮的值为1，第二轮删除后值还是为1，第三轮值为2 。
	    if(obj.attributes["id"].value == "first"){
	  		h = h-1 ;		//h = 0 ;
	  	}
		
	    var index=obj.parentNode.parentNode.parentNode.rowIndex+1;			//不包括标题行
    	var x=document.getElementById('recordAccountTable').insertRow(index);
  		var h1=x.insertCell(0);
  		var h2=x.insertCell(1);
  		var h3=x.insertCell(2);
  		var h4=x.insertCell(3);
  		var h5=x.insertCell(4);
  		var h6=x.insertCell(5);
  		var h7=x.insertCell(6);
  		var h8=x.insertCell(7);
  		var h9=x.insertCell(8);
  		var h10=x.insertCell(9);
  		var h11=x.insertCell(10);
        h1.innerHTML="<div align=center ><a id='other"+n+"' href='javascript:void(0);' class='default-details' title='增加' onclick='Add(this)'>增加</a>"
                    +"&nbsp;<a href='javascript:void(0);' class='default-delete' title='删除' onclick='DeleteRow(this)' id='del'>删除</a></div>"
  		h2.innerHTML="<div align=center><input type='text' class='form-control' name='recordAccountRuleConfig["+h+"].journalNo' required></div>";
  		h3.innerHTML="<div align=center><input type='text' class='form-control' name='recordAccountRuleConfig["+h+"].childTransNo' required></div>";
  		h4.innerHTML="<div align=center><select class='form-control' style='' name='recordAccountRuleConfig["+h+"].debitCreditSide'>"
  										+"<c:forEach var='balanceFrom' items='${balanceFromList}'>"
													+"<option value='${balanceFrom.sysValue}'>"
													+"${balanceFrom.sysName}</option>"
										+"</c:forEach></select></div>";
  		h5.innerHTML="<div align=center><select class='form-control' style='' name='recordAccountRuleConfig["+h+"].accountFlag' onchange='disbaled($(this))'>"
  										+"<c:forEach var='accountFlag' items='${accountFlagList}'>"
										+"<option value='${accountFlag.sysValue}' <c:if test='${accountFlag.sysValue == 0}'> selected='selected'</c:if>>"
										+"${accountFlag.sysName}</option></c:forEach>" ;
		h6.innerHTML="<div align=center><select class='form-control' style='' name='recordAccountRuleConfig["+h+"].accountType'>"
  										+"<c:forEach var='accountType' items='${accountTypeList}'>"
										+"<option value='${accountType.sysValue}' <c:if test='${accountType.sysValue == \"M\"}'> selected='selected'</c:if>>"
										+"${accountType.sysName}</option></c:forEach>" ;
  		h7.innerHTML="<div align=center><select id='subjectNo"+n+"' autocomplete='off' class='form-control' name='recordAccountRuleConfig["+h+"].subjectNo'></select></div>";
  		h8.innerHTML="<div align=center><select class='form-control' style='' name='recordAccountRuleConfig["+h+"].currencyNo'>"
										+"<c:forEach var='currency' items='${currencyList}'>"
										+"<option value='${currency.currencyNo}'>${currency.currencyName}</option></c:forEach>" ;
  		h9.innerHTML="<div align=center><select class='form-control' style='' name='recordAccountRuleConfig["+h+"].debitCreditFlag'>" 
  										+"<option value='yes' selected='selected'>是</option>"
  								        +"<option value='no' >否</option></select></div>";
  		h10.innerHTML="<div align=center><input type='text' class='form-control' name='recordAccountRuleConfig["+h+"].amount' required></div>";
  		h11.innerHTML="<div align=center><textarea name='recordAccountRuleConfig["+h+"].remark' rows='1' cols='15' style='height: 34px;resize:none;'></textarea></div>";
  		
          $('#subjectNo'+n).select2({
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

        //当所有行删除完，再次添加新的一行时的处理
          if(obj.attributes["id"].value == "first"){
        		document.getElementById('recordAccountTable').deleteRow(1) ;
        	}
          n=n+1 ;
  	}
  	
	//删除行
	function DeleteRow(obj){
		var index=obj.parentNode.parentNode.parentNode.rowIndex;
  	    var table = document.getElementById("recordAccountTable");

    	//删除行
	    	table.deleteRow(index);
  	    //处理当删除只剩一行时
  	    if($("#recordAccountTable tr").length==1){
  	    	//添加一个新增按钮
  	    	var x=document.getElementById('recordAccountTable').insertRow(1);
  	  		var h1=x.insertCell(0);
  	  		var h2=x.insertCell(1);
  	  		var h3=x.insertCell(2);
  	  		var h4=x.insertCell(3);
  	  		var h5=x.insertCell(4);
  	  		var h6=x.insertCell(5);
  	  		var h7=x.insertCell(6);
  	  		var h8=x.insertCell(7);
  	  		var h9=x.insertCell(8);
  	  		var h10=x.insertCell(9);
  	  		var h11=x.insertCell(10);
  	  		h6.innerHTML="<div align=center><a id='first' href='javascript:void(0);' class='inputAccount-detail' style='color:#f60' title='增加' onclick='Add(this)'>增加</a></div>";
  	  		
  	    }
	}
	
</script>

<script type="text/javascript">
	
		$("#recordAccountForm").submit(function(){
			var data=$("#recordAccountForm").serialize();
			//alert(data) ;
			$.ajax({
				url:"${ctx}/recordAccountAction/recordAccountRuleAdd.do",
				type:"POST",
				data:data,
				success :function(msg){
					if(!msg.state){
						//alert(msg.msg);
			                // Display a success toast, with a title
			            toastr.error(msg.msg,'错误');
					}else{
						toastr.success(msg.msg,'提示');
                        $('textarea').val('');
                        $('input').val('');
                        $("input[name='${_csrf.parameterName}']").val('${_csrf.token}');
					}
				}
			});
			return false;
		});

		/* 科目查询开始 */

	 	  function formatRepo (repo) {
	        if (repo.loading) return repo.text;
			//console.info(repo.id);
			return repo.id+'('+repo.text+')';  
	      }

	      function formatRepoSelection (repo) {
	    	  //console.info("formatRepoSelection:"+ repo.text);
	    	 // return repo.text;  
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

