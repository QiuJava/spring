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
	<link href="${ctx}/css/plugins/select2/bootstrap-select.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/select2/bootstrap-select.min.csss" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
        <div class="col-lg-10" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">记账处理</div>
            <em class=""></em>
            <div class="pull-left active">修改交易类型与记账规则定义</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
				<div class="ibox-content">
					 <form class="form-horizontal" id="transTypeForm">
                          <div class="form-group">
                          	  <label class="col-sm-2 control-label">交易类型编号：</label>
                              <div class="col-sm-2"><input type="text" class="form-control" name="transTypeCode" id="transTypeCode" value="${transType.transTypeCode }" required></div>
                             
                          	  <label class="col-sm-2 control-label">交易类型名称：</label>
                              <div class="col-sm-2"><input type="text" class="form-control" name="transTypeName" id="transTypeName" value="${transType.transTypeName }" required></div>
                             
                           </div>
                           
                           <div class="form-group">
                              <label class="col-sm-2 control-label">记账规则：</label>
                                <div class="col-sm-2">
	                                <select id="ruleNo" autocomplete="off" class="form-control" name="recordAccountRule.ruleNo">
	                                	<option value="${transType.recordAccountRule.ruleNo }" selected="selected">
	                                		${transType.recordAccountRule.ruleName }
	                                	</option>
	                                </select>
								</div>
                              
                              <label class="col-sm-2 control-label">来源系统：</label>
                                <div class="col-sm-2">
	                              <select class="form-control" name="fromSystem"> 
	                                   <c:forEach var="fromSystem" items="${fromSystemList}">
	                                      <option value="${fromSystem.sysValue}"
	                                      <c:if test="${fromSystem.sysValue == transType.fromSystem}">selected="selected"</c:if>>
	                                      ${fromSystem.sysName} [ ${fromSystem.sysValue } ]</option>
	                                  </c:forEach>
	                              </select> 
	                             </div>
                           </div>
                           <div class="form-group">
                            <label class="col-sm-2 control-label">交易分组：</label>
                                <div class="col-sm-6">
	                               <select id = "transGroup" name = "transGroup" class="form-control selectpicker" multiple data-hide-disabled="true" data-size="15" style="width:300px; height:200px;"> 
	                                   <c:forEach var="transGroup" items="${transGroupList}">
	                                      <option value="${transGroup.sysKey}">${transGroup.remark}</option>
	                                  </c:forEach>
	                              </select> 
								</div>
                           </div>
                           <div class="form-group lastbottom">
                              <label class="col-sm-2 control-label">说明：</label>
                              <div class="col-sm-2"><textarea rows="3" cols="30" class="form-control" name="remark" id="remark" required >${transType.remark }</textarea></div>
                             
                            </div>
                           
                                 <div class="form-group">
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                        <label class="col-sm-2 control-label aaa"></label>
                                   	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                   	   <input type="hidden" name="id" value="${transType.id}" />
                                   	   <sec:authorize access="hasAuthority('transTypeListQuery:update')">  
                                       		<button class="btn btn-success " type="submit"><span class="glyphicon gly-ok"></span>提交</button>
                                       </sec:authorize>
                                       <button id="returnUp" type="button" class=" btn btn-default col-sm-offset-14" onclick="window.location.href='${ctx}/recordAccountAction/toTransTypeListQuery.do?queryParams=${params.queryParams}'" value="" /><span class="glyphicon gly-return"></span>返回</button>
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
<script src="${ctx}/js/plugins/select2/bootstrap-select.js"></script>
<script src="${ctx}/js/plugins/select2/bootstrap-select.min.js"></script>
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
		$("#transTypeForm").submit(function(){
			var data=$("#transTypeForm").serialize();
			//alert(data) ;
			$.ajax({
				url:"${ctx}/recordAccountAction/transTypeUpdate.do",
				type:"POST",
				data:data,
				success :function(msg){
					if(!msg.state){
			                // Display a success toast, with a title
			            toastr.error(msg.msg,'错误');
					}else{
						toastr.success(msg.msg,'提示');
						setTimeout("javascript:location.href='${ctx}/recordAccountAction/toTransTypeListQuery.do'", 500);
					}
				}
			});
			return false;
		});

		
		/* 记账规则查询开始 */

	 	  function formatRepo (repo) {
	        if (repo.loading) return repo.text;
			//console.info(repo.id);
			return repo.id+'('+repo.text+')';  
	      }

	      function formatRepoSelection (repo) {
	    	  //console.info("formatRepoSelection:"+ repo.text);
	    	  return '['+repo.id+']'+repo.text;  
	        //return repo.full_name || repo.text;
	      }
	      
	      function parseSelectParams(params){
	    	  if(params && params.term){
	    		  return encodeURI(params.term);
	    	  }
	    	  else
	    		  return null;
	      }
	      $(".selectpicker").selectpicker({  
	            noneSelectedText : '此选项可以多选，请选择'  
	        }); 
	      //对多选下拉框赋值
	      var transGroupList; 
	      transGroupList = '${transType.transGroup}';
	      var transGroupList = transGroupList.split(',');
	      $('.selectpicker').selectpicker('val', transGroupList).trigger("change");
		// turn the element to select2 select style
	      $('#ruleNo').select2({
		    	  		ajax: {
			    		  	url: "${ctx}/recordAccountAction/querySelectRule.do",
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
		    		  placeholder: "选择记账规则",  
		    		  allowClear: true,
		    		  width: '100%',
		    		  // containerCssClass: 'tpx-select2-container',
		    		  // dropdownCssClass: 'tpx-select2-drop',
		    		  escapeMarkup: function (markup) { return markup; }, // let our custom formatter work
		    		  minimumInputLength: 0,
		    		  language: "zh-CN",
		    		  templateResult: formatRepo, // omitted for brevity, see the source of this page
		    		  templateSelection: formatRepoSelection // omitted for brevity, see the source of this pag
		    	      
		    		}
	    		  );
		
		
		
	  </script>
</title>

</html>

