<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<link href="${ctx}/css/plugins/select2/select2.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/select2/select2-skins.min.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10" >
			<div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">科目管理</div>
            <em class=""></em>
            <div class="pull-left active">新增用户类型科目关联</div>
		</div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="userTypeForm">
					 	<div class="form-group">
							<label class="col-sm-2 control-label">科目编号：</label>
                                <div class="col-sm-2">
                                <select id="selectSubjectNo" class="form-control" name="subjectNo" required="required">
								</select>
								</div>	
							 <label class="col-sm-2 control-label" >用户类别：</label>
                                <div class="col-sm-2">
   									<select class="form-control" name="userType"> 
									         <c:forEach var="userType" items="${userTypes}">
												<option value="${userType.sysValue}"
												<c:if test="${userType.sysValue == params.userType}">selected="selected"</c:if>>
												${userType.sysName}</option>
											</c:forEach>
										
										</select>  
								 </div>
						</div>
						<div class="clearfix lastbottom"></div>
							<div class="form-group">
                               <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                    <label class="col-sm-2 control-label aaa"></label>
                               	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                               	   
                                    <sec:authorize access="hasAuthority('userTypeSubjectAdd:insert')">                               	   
                                   		<button class="btn btn-success" type="submit"><span class="glyphicon gly-ok"></span>提交</button>
                                   </sec:authorize>
                               <!-- </div> -->
                          	</div>
                          	<br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
                          	
                 </div>
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
		$("#userTypeForm").submit(function(){
			var data=$("#userTypeForm").serialize();
			$.ajax({
				url:"subjectAction/saveUserTypeSubject.do",
				type:"POST",
				data:data,
				success :function(msg){
					if(!msg.state){
						//alert(msg.msg);
						//showError(msg.msg);
						toastr.error(msg.msg,'错误');
					}else{
						//alert(msg.msg);	
						toastr.success(msg.msg,'提示');
						//清空表单值
				        $("#selectSubjectNo").empty();
				        $("#select2-selectSubjectNo-container").html("选择科目");
						$('#userTypeForm').get(0).reset() ;
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
	      $('#selectSubjectNo').select2({
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
		/* 科目查询结束 */
		
	  </script>
</title>

</html>

