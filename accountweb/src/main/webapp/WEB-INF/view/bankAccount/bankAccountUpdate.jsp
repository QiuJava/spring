<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<link href="${ctx}/css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
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
            <div class="pull-left active">维护银行账户</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
				<div class="ibox-content">
					 <form class="form-horizontal" id="bankAccountForm">
                          <div class="form-group">
                          	  <label class="col-sm-2 control-label">开户银行名称：</label>
                              <div class="col-sm-2"><input type="text" class="form-control" name="bankName" id="bankName" value="${bankAccount.bankName }" required></div>
                             
                              <label class="col-sm-2 control-label">开户名：</label>
                              <div class="col-sm-2"><input type="text" class="form-control" name="accountName" id="accountName" value="${bankAccount.accountName }" required> </div>
                              
                            <label class="col-sm-2 control-label">开户账号：</label>
                            <div class="col-sm-2"><input type="text" class="form-control" name="accountNo" id="accountNo" value="${bankAccount.accountNo }" required></div>
                             
                              
                           </div> 
                           <div class="form-group">
                                <label class="col-sm-2 control-label" >支付机构：</label>
                                <div class="col-sm-2">
                                    <select class="form-control" name="orgNo"> 
                                             <c:forEach var="orgInfo" items="${orgInfos}">
                                                <option value="${orgInfo.orgNo}"
                                                <c:if test="${orgInfo.orgName == bankAccount.orgNo}">selected="selected"</c:if>>
                                                ${orgInfo.orgName}</option>
                                            </c:forEach>
                                        
                                        </select>  
                                 </div>							
                                 <label class="col-sm-2 control-label">币种号：</label>
                                 <div class="col-sm-2">
   									<select class="form-control" name="currencyNo"> 
									         <c:forEach var="currencyInfo" items="${currencyInfos}">
												<option value="${currencyInfo.currencyNo}"
												<c:if test="${currencyInfo.currencyName == bankAccount.currencyNo}">selected="selected"</c:if>>
												${currencyInfo.currencyName}</option>
											</c:forEach>
										
										</select>  
								 </div>
 								 <label class="col-sm-2 control-label">账户类别：</label>
                                 <div class="col-sm-2">
   									<select class="form-control" name="accountType"> 
										<c:forEach var="accountTypeInfo" items="${accountTypeInfos}">
											<option value="${accountTypeInfo.sysValue}"
											<c:if test="${accountTypeInfo.sysValue == bankAccount.accountType}">selected="selected"</c:if>>
											${accountTypeInfo.sysName}</option>
										</c:forEach>  
									</select>  
								 </div>
                                 
                            </div>
                           <div class="form-group">
                                <label class="col-sm-2 control-label">科目号：</label>
                                <div class="col-sm-2">
                                    <%-- <input type="text" class="form-control" name="subjectNo" id="subjectNo" value="${bankAccount.subjectNo }" required> --%>
                                    <select id="subjectNo" autocomplete="off" class="form-control" name="subjectNo">
                                        <option value="${bankAccount.subjectNo }" selected="selected">
                                            ${bankAccount.subjectNo }
                                        </option>
                                   </select>
                                </div>
                             <label class="col-sm-2 control-label">状态：</label>
                                 <div class="col-sm-2">
                                    <select class="form-control" name="accountStatus"> 
                                             <c:forEach var="accountStatus" items="${accountStatusList}">
                                                <option value="${accountStatus.sysValue}"
                                                <c:if test="${accountStatus.sysValue == bankAccount.insAccount.accountStatus}">selected="selected"</c:if>>
                                                ${accountStatus.sysName}</option>
                                            </c:forEach>
                                        
                                        </select>  
                                 </div>
                                
                            </div>
                            <div class="form-group ">
                                <label class="col-sm-2 control-label">联行行号：</label>
                                <div class="col-sm-2"><input type="text" class="form-control" name="cnapsNo" id="cnapsNo" value="${bankAccount.cnapsNo }" required></div>
                                <div class="col-sm-1"><a href="http://www.posp.cn/" target="_black" style='color:#f60' title='联行行号查询'>联行行号查询</a></div>
                            </div>
                            <div class="clearfix lastbottom"></div>
                                 <div class="form-group">
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                        <label class="col-sm-2 control-label aaa"></label>
                                   	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                   	   <input type="hidden" name="id" value="${id}" />
                                   	   
                                   	   <sec:authorize access="hasAuthority('bankAccountManage:update')">
                                       		<button class="btn btn-success" type="submit"><span class="glyphicon gly-ok"></span>提交</button>
                                       </sec:authorize>
                                       <button id="returnUp" type="button" class=" btn btn-default col-sm-offset-14" onclick="window.location.href='${ctx}/bankAccountAction/bankAccountManage.do'"><span class="glyphicon gly-return"></span>返回</button>
                                   <!-- </div> -->
                              	   </div>
                                   <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
                    </form>
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
			var data=$("#bankAccountForm").serialize();
			$.ajax({
				url:"${ctx}/bankAccountAction/bankAccountUpdate.do",
				type:"POST",
				data:data,
				success :function(msg){
					if(!msg.state){
			                // Display a success toast, with a title
			            toastr.error(msg.msg,'错误');
					}else{
						toastr.success(msg.msg,'提示');
						setTimeout("javascript:location.href='${ctx}/bankAccountAction/bankAccountManage.do'", 500);
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

