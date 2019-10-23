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
	 <!-- Sweet Alert -->
    <link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
        <div class="col-lg-10" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">外部账号管理</div>
            <em class=""></em>
            <div class="pull-left active">外部账户开立</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="extAccountForm">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">科目编号:</label>
                                <div class="col-sm-2">
                                    <select id="subjectNo" autocomplete="off" class="form-control" name="subjectNo"></select>
                                </div>
                                <label class="col-sm-2 control-label">用户类别:</label>
                                <div class="col-sm-2">
                                    <select class="form-control" name="accountType"> 
                                                 <c:forEach var="accountType" items="${accountTypes}">
                                        <option value="${accountType.sysValue}">
                                          ${accountType.sysName}
                                        </option>
                                    </c:forEach>    
                                    </select>
                                </div>
                                <label class="col-sm-2 control-label ">商户/代理商/收单机构编号:</label>
                                <div class="col-sm-2">
                                    <input type="text" class="form-control"  name="userId" required />
                                </div>
                            </div>
                          <div class="form-group">
                                 <label class="col-sm-2 control-label">账号归属:</label>
                                 <div class="col-sm-2">
									 <select class="form-control" name="orgNo"> 
								 		<c:forEach var="orgInfo" items="${orgInfoList}">
											<option value="${orgInfo.orgNo}">${orgInfo.orgName}  ${orgInfo.orgNo}
											</option>
										</c:forEach>	
									</select>
								</div>
                                <label class="col-sm-2 control-label">币种号:</label>
								<div class="col-sm-2">
									<select class="form-control" name="currencyNo"> 
  									<c:forEach var="currency" items="${currencyList}">
										<option value="${currency.currencyNo}">
										  ${currency.currencyName}
										</option>
										</c:forEach>	
									</select>
								 </div>	
                                <label class="col-sm-2 control-label ">卡号:</label>
                                <div class="col-sm-2">
                                    <input type="text"  placeholder="预付卡号,若没有则为空" class="form-control" name="cardNo" />
                                </div>
                           </div>
                            
                            <div class="clearfix lastbottom"></div>
                            	 <div class="form-group">
                                 <label class="col-sm-2 control-label aaa"></label>
                               <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                               	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                <sec:authorize access="hasAuthority('createExtAccount:insert')">  
                                   <button class="ladda-button btn btn-success" type="button" data-style="expand-left"><span class="glyphicon gly-open"></span>开立账户</button>
                                </sec:authorize>
                               <!-- </div> -->
                           </div>
                           <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
                    </form>
				</div>
			</div>
		</div>
			
	</div>
	
   </body>

<title>
	<script src="${ctx}/js/plugins/select2/select2.full.min.js"></script>
	<script src="${ctx}/js/plugins/select2/i18n/zh-CN.js"></script>
	    <!-- Ladda style -->
    <link href="${ctx}/css/plugins/ladda/ladda-themeless.min.css" rel="stylesheet">
	
	    <!-- Ladda -->
    <script src="${ctx}/js/plugins/ladda/spin.min.js"></script>
    <script src="${ctx}/js/plugins/ladda/ladda.min.js"></script>
    <script src="${ctx}/js/plugins/ladda/ladda.jquery.min.js"></script>
<!-- Sweet alert -->
<script src="${ctx}/js/plugins/sweetalert/sweetalert.min.js"></script>
    
	<script type="text/javascript">
	
		$(document).ready(function() {
			var l = $( '.ladda-button' ).ladda();
	
	        l.click(function(){
	            // Start loading
	            l.ladda( 'start' );
	            // Timeout example
	            $("#extAccountForm").submit();
	            // Do something in backend and then stop ladda
	            setTimeout(function(){
	                l.ladda('stop');
	            },100)
	        });
		});
	
		$("#extAccountForm").submit(function(){
			//console.info(extAccountForm);
			var data=$("#extAccountForm").serialize();
			$.ajax({
				url:"${ctx}/extAccountAction/saveExtAccountInfo.do",
				type:"POST",
				data:data,
				success :function(msg){
					//console.info(msg);
					if(typeof(msg.state) != "undefined"){
						if(msg.state){
							toastr.success(msg.msg,'提示');
							//清空表单值
					        $("#subjectNo").empty();
					        $("#select2-subjectNo-container").html("选择科目");
							$('#extAccountForm').get(0).reset() ;
						}else{
							toastr.error(msg.msg,'错误');
						}
					}else{
						swal({title:"提示" ,text:msg.msg ,animation:"slide-from-top"});
					}
				}
			});
			return false;
		});


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
