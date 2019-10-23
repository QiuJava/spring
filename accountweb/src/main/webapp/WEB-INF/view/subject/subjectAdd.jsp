<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<link href="${ctx}/css/plugins/select2/select2.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/select2/select2-skins.min.css" rel="stylesheet">
	<link rel="stylesheet" href="${ctx}/css/icheck-css/custom.css" />
	<link rel="stylesheet" href="${ctx}/css/skins/all.css" />
	 <!-- Sweet Alert -->
    <link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
                <div class="pull-left">当前位置</div>
                <em class=""></em>
                <div class="pull-left">科目管理</div>
                <em class=""></em>
                <div class="pull-left active">新建科目信息</div>
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
					 <form class="form-horizontal" id="subjectForm">
					  		<div class="form-group">
                             <label class="col-sm-2 control-label">上级科目:</label>
                                <div class="col-sm-2">
                                <select id="parentSubjectNo" class="form-control" name="parentSubjectNo">
								</select>
								</div>
                            <label class="col-sm-2 control-label">科目编号:</label>
                              <div class="col-sm-2"><input type="text"  onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" class="form-control" name="subjectNo" id="subjectNo" required></div>
                             
                             <label class="col-sm-2 control-label" >科目类别:</label>
                                <div class="col-sm-2">
   									<select class="form-control" name="subjectType"> 
									         <c:forEach var="subjectType" items="${subjectTypes}">
												<option value="${subjectType.sysValue}">
												${subjectType.sysName}</option>
											</c:forEach>
										
										</select>  
								 </div>
                           </div>
                                 
                           <div class="form-group">
                                <label class="col-sm-2 control-label ">科目名称:</label>
                                <div class="col-sm-2"><input type="text" class="form-control" name="subjectName" id="subjectName" required> </div>	
                                <label class="col-sm-2 control-label ">科目别名:</label>
                                <div class="col-sm-2"><input type="text" class="form-control" name="subjectAlias" id="subjectAlias"  required> </div>		
                                 <label class="col-sm-2 control-label">余额方向:</label>
                                 <div class="col-sm-2">
 										<select class="form-control" name="balanceFrom" required> 
   											 <c:forEach var="balanceFrom" items="${balanceFromList}">
												<option value="${balanceFrom.sysValue}">
												${balanceFrom.sysName}</option>
											</c:forEach>
   											 <!-- <option value="credit">贷</option> 
									         <option value="debit">借</option>  -->
										</select>
 								 </div>		
                            </div>
                            <div class="form-group">		
                                 <label class="col-sm-2 control-label">修改余额标志:</label>
                                 <div class="col-sm-2">
 										<select class="form-control" name="innerDayBalFlag"> 
   											 <!-- 日终修改余额标志：0-日间，1-日终 -->
   											 <c:forEach var="innerDayBalFlag" items="${innerDayBalFlagList}">
												<option value="${innerDayBalFlag.sysValue}">
												${innerDayBalFlag.sysName}</option>
											</c:forEach>
										</select>
 								 </div>		
 								 <label class="col-sm-2 control-label">明细处理方式:</label>
                                 <div class="col-sm-2">
 										<select class="form-control" name="innerSumFlag"> 
 											<!-- 明细处理方式：0-日间单笔，1-日终单笔，2-日终汇总 -->
   											 <c:forEach var="innerSumFlag" items="${innerSumFlagList}">
												<option value="${innerSumFlag.sysValue}">
												${innerSumFlag.sysName}</option>
											</c:forEach>
										</select>
 								 </div>		
                            </div>
                            
                            <div class="form-group ">
                                <label class="col-sm-2 control-label " >参加借贷平衡:</label>
                                <div class="col-sm-2" id="toloan">
                                	<label class="radio-inline">
										<input type="radio" name="debitCreditFlag" id="inlineRadio1" value="1" checked="checked"> 是
									</label>
									<label class="radio-inline">
										<input type="radio" name="debitCreditFlag" id="inlineRadio2" value="0"> 否
									</label>
								</div>
                                <label class="col-sm-2 control-label " >开立内部账户:</label>
                                <div class="col-sm-2" id="inputaccount">
                                	<label class="radio-inline">
										<input type="radio" name="isInnerAccount"  value="1" checked="checked"> 是
									</label>
									<label class="radio-inline">
										<input type="radio" name="isInnerAccount" value="0"> 否
									</label>
								</div>
                             
                                </div>
                                <div class="clearfix lastbottom"></div>
                                 <div class="form-group">
                                        <label class="col-sm-2 control-label aaa"></label>

                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                   	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                   	   
                                        <sec:authorize access="hasAuthority('subjectAdd:insert')">                               	   
                                       		<button class="btn btn-success" type="submit"><span class="glyphicon gly-ok"></span>提交</button>
                                       </sec:authorize>
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
	<script type="text/javascript">
		$(document).ready(function(){
              $('#toloan input').iCheck({
                // checkboxClass: 'icheckbox_square-green',
                radioClass: 'iradio_square-orange',
                increaseArea: '20%'
              });
              $('#inputaccount input').iCheck({
                radioClass: 'iradio_square-orange',
                increaseArea: '20%'
              });
         });
	</script>
	<script type="text/javascript">
        // 去除空格啊
        $('input').blur(function(){
            replaceSpace(this);
        })
        function replaceSpace(obj){
            obj.value = obj.value.replace(/\s/gi,'')
        }
		$("#subjectForm").submit(function(){
			var data=$("#subjectForm").serialize();
            console.log(data);
			$.ajax({
				url:"${ctx}/subjectAction/saveSubjectInfo.do",
				type:"POST",
				data:data,
				success :function(msg){
					if(typeof(msg.state) != "undefined"){
						if(msg.state){
							toastr.success(msg.msg,'提示');
							//清空表单值
					        $("#parentSubjectNo").empty();
					        $("#select2-parentSubjectNo-container").html("选择上级科目");
							$('#subjectForm').get(0).reset() ;
							/* $(':input','#subjectForm').not(':button, :submit, :reset, :hidden').val('').removeAttr('checked').removeAttr('selected'); */
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
		/* var checkSubject={
				check:function(subjectNo,type){
					$.ajax({
						url:"${ctx}/subjectAction/exsitsSubject.do",
						type:"POST",
						data:{subjectNo:subjectNo,subjectType:type},
						success :function(msg){
							if(!msg.state){
								//alert(msg.msg);
					                // Display a success toast, with a title
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
							}
							
						}
					});
				}	
			};
			//检查科目是否存在
			$("#subjectNo").change(function(){
				var no=$(this).val().trim();
				if(no!="")
					checkSubject.check(no,"inner");
			}); */
			//检查科目是否存在
			/* $("#subjectLegalNo").change(function(){
				var no=$(this).val().trim();
				if(no!="")
					checkSubject.check(no,"legal");
			}) */

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
	      $('#parentSubjectNo').select2({
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
		    		  placeholder: "选择上级科目",  
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

