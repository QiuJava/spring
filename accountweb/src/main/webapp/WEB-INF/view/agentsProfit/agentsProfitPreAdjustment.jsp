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
                <div class="pull-left active">代理商分润预调账</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<!-- <div class="ibox-title">
					<div class="ibox-tools">
						<a class="collapse-link"> <i class="fa fa-chevron-up"></i></a> 
					</div>
				</div> -->
				<div class="ibox-content">
					 <form class="form-horizontal" id="adjustForm">
					  		<div class="form-group">
                            <label class="col-sm-2 control-label">代理商名称:</label>
		                           <div class="col-sm-3">
                                    <select id="agentNo" class="form-control" name="agentNo"  required>
								    </select>
								</div>
                           </div>     
                           <div class="form-group">
                                <label class="col-sm-2 control-label ">调账金额:</label>
                                <div class="col-sm-2"><input type="text" class="form-control" name="adjustAmount" id="adjustAmount" required> </div>元		
                            </div>
                            <div class="form-group">		
                                 <label class="col-sm-2 control-label">调账原因:</label>
                                 <div class="col-sm-2">
 										<select class="form-control" name="adjustReason"> 
   											 <c:forEach var="vf" items="${ajustmentReasonList}">
												<option value="${vf.sysValue}">
												${vf.sysName}</option>
											</c:forEach>
										</select>
 								 </div>		
                            </div>
                            <div class="form-group">
                                <label class="col-sm-2 control-label ">备注:</label>
                                <div class="col-sm-4"><input type="text" class="form-control" name="remark" id="remark" required> </div>	
                            </div>
                           
                                <div class="clearfix lastbottom"></div>
                                 <div class="form-group">
                                        <label class="col-sm-2 control-label aaa"></label>

                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                   	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                   	   
                                        <sec:authorize access="hasAuthority('subjectAdd:insert')">                               	   
                                       		<button class="btn btn-success" type="submit" id="savePreAdjust"><span class="glyphicon gly-ok"></span>提交</button>
                                       </sec:authorize>
                                       <button id="returnUp" type="button" class=" btn btn-default  col-sm-offset-14" onclick="javascript:history.back(-1);" value="" /><span class="glyphicon gly-return"></span>返回</button>
                                       
                                   <!-- </div> -->
                              	   </div>
                                   <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
                    </form>
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
    $('input').blur(function(){
        replaceSpace(this);
    })
    function replaceSpace(obj){
        obj.value = obj.value.replace(/\s/gi,'')
    }

		
	$("#adjustForm").submit(function(){
		var data=$("#adjustForm").serialize();
		$("#savePreAdjust").attr("disabled","disabled");
		$.ajax({
			url:"${ctx}/agentsProfitAction/saveAdjustInfo.do",
			type:"POST",
			data:data,
			success :function(msg){
				if(!msg.status){
					//alert(msg.msg);
					$("#savePreAdjust").removeAttr("disabled");
		                // Display a success toast, with a title
		            toastr.error(msg.msg,'错误');
				}else{
					toastr.success(msg.msg,'提示');
					setTimeout(function() {
		            	location.href='${ctx}/agentsProfitAction/agentsProfitPreAdjustmentQuery.do';
		            }, 1000);
				}
			}
		});
		return false;
	});

	
		$(document).ready(function() {
			
            
            $('.input-daterange').datepicker({
                format: "yyyy-mm-dd",
                language: "zh-CN",
                todayHighlight: true,
                autoclose: true,
                clearBtn: true
            });
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

	</script>
	 </title>

</html>

