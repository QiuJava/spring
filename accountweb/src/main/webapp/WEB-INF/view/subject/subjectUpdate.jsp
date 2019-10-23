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
    <link rel="stylesheet" href="${ctx}/css/icheck-css/custom.css" />
    <link rel="stylesheet" href="${ctx}/css/skins/all.css" />
	 <!-- Sweet Alert -->
    <link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">科目管理</div>
            <em class=""></em>
            <div class="pull-left active">维护科目信息</div>
		</div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="sebjectForm">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">上级科目:</label>
                                <div class="col-sm-2">
                                 <select id="parentSubjectNo" class="form-control" name="parentSubjectNo">
                                        <c:forEach var="subject" items="${subjectList}">
                                                <option value="${subject.subjectNo}"
                                                <c:if test="${subject.subjectNo == params.parentSubjectNo}">selected="selected"</c:if>>
                                                ${subject.subjectNo}(${subject.subjectName})</option>
                                            </c:forEach>
                                </select>
                                </div>  
                             <label class="col-sm-2 control-label">科目编号:</label>
                                <div class="col-sm-2">
                                <input type="text"   readonly="readonly"  class="form-control"  name="subjectNo"  value="${params.subjectNo}">
                              </div>
                                <label class="col-sm-2 control-label ">科目级别:</label>
                                <div class="col-sm-2">
                                        <select class="form-control" name="subjectLevel"> 
                                             <c:forEach var="subjectLevel" items="${subjectLevels}">
                                                <option value="${subjectLevel.sysValue}"
                                                <c:if test="${subjectLevel.sysValue == params.subjectLevel}">selected="selected"</c:if>>
                                                ${subjectLevel.sysName}</option>
                                            </c:forEach>
                                        </select>    
                                </div>        
                        </div>
						
                          <div class="form-group">
                           		<label class="col-sm-2 control-label">科目名称:</label>
                                <div class="col-sm-2">
                                <input type="text" class="form-control" name="subjectName" value="${params.subjectName}" required>
                                </div>
                                <label class="col-sm-2 control-label">科目别名:</label>
                                <div class="col-sm-2">
                                <input type="text" class="form-control" name="subjectAlias" value="${params.subjectAlias}" required>
                                </div>
                                <label class="col-sm-2 control-label" >科目类别:</label>
                                <div class="col-sm-2">
										<select class="form-control" name="subjectType"> 
									         <c:forEach var="subjectType" items="${subjectTypes}">
												<option value="${subjectType.sysValue}"
												<c:if test="${subjectType.sysValue == params.subjectType}">selected="selected"</c:if>>
												${subjectType.sysName}</option>
											</c:forEach>
										
										</select>         
								 </div>	
                            </div>
                            <div class="form-group">
                                 <label class="col-sm-2 control-label subbalance">余额方向:</label>
                                 <div class="col-sm-2">
                                        <select class="form-control" name="balanceFrom" required value="${params.balanceFrom}"> 
                                        
                                        <c:forEach var="balanceFromList" items="${balanceFromList}">
                                                <option value="${balanceFromList.sysValue}"
                                                <c:if test="${balanceFromList.sysValue == params.balanceFrom}">selected="selected"</c:if>>
                                                ${balanceFromList.sysName}</option>
                                            </c:forEach> 
                                             <%-- <option value="${params.balanceFrom}"
                                                <c:if test="${params.balanceFrom} == 'debit' ">selected="selected"</c:if>>
                                                借方</option>
                                             <option value="${params.balanceFrom}"
                                                <c:if test="${params.balanceFrom} == 'credit' ">selected="selected"</c:if>>
                                                贷方</option> --%>
                                        </select>
                                 </div>
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
                            <div class="form-group">	
                                <label class="col-sm-2 control-label " >参加借贷平衡:</label> 
                                <div class="col-sm-2" id="toloan">
                                	<label class="radio-inline">
										<input type="radio" name="debitCreditFlag" id="inlineRadio1" value="1" <c:if test="${params.debitCreditFlag == '1'}">checked="checked"</c:if>> 是
									</label>
									<label class="radio-inline">
										<input type="radio" name="debitCreditFlag" id="inlineRadio2" value="0" <c:if test="${params.debitCreditFlag == '0'}">checked="checked"</c:if>> 否
									</label>
								</div>
                                <label class="col-sm-2 control-label " >开立内部账户:</label>
                                <div class="col-sm-2" id="inputaccount">
                                	<label class="radio-inline">
										<input type="radio" name="isInnerAccount"  value="1" <c:if test="${params.isInnerAccount == '1'}">checked="checked"</c:if>> 是
									</label>
									<label class="radio-inline">
										<input type="radio" name="isInnerAccount" value="0" <c:if test="${params.isInnerAccount == '0'}">checked="checked"</c:if>> 否
									</label>
								</div>
                             
                                </div>
                                <div class="clearfix lastbottom"></div>

                                 <div class="form-group">
                                   <!-- <div class="col-sm-5 col-sm-offset-13 "> -->
                                        <label class="col-sm-2 control-label aaa"></label>

                                   		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                       <sec:authorize access="hasAuthority('subjectListInfo:update')"> 
                                       		<button class="btn btn-success" type="submit"><span class="glyphicon gly-ok"></span>提交</button>    	   
                                       </sec:authorize>
                                       <button class="btn btn-default col-sm-offset-14" type="button" onclick="goBack()"><span class="glyphicon gly-return"></span>返回</button>
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
		/* $(document).ready(function() {
			$('#parentSubjectNo').val("${params.parentSubjectNo}");
		}); */
		//用于判断是否从详情页面跳转过来
    // 去除空格啊
    $('input').blur(function(){
            replaceSpace(this);
        })
    function replaceSpace(obj){
            obj.value = obj.value.replace(/\s/gi,'')
        }

		var flag = '${params.flag}' ;
		$("#sebjectForm").submit(function(){
			var data=$("#sebjectForm").serialize();
			$.ajax({
				url:"${ctx}/subjectAction/subjectUpdate.do",
				type:"POST",
				data:data,
				success :function(msg){
					if(typeof(msg.state) != "undefined"){
						if(msg.state){
							toastr.success(msg.msg,'提示');
							if(flag == "detail"){
								setTimeout("javascript:location.href='${ctx}/subjectAction/toSubjectDetail.do?subjectNo=${params.subjectNo}'", 500);
							}else{
								setTimeout("javascript:location.href='${ctx}/subjectAction/toSubjectListInfo.do'", 500);
							}
							//window.location.href='${ctx}/subjectAction/toSubjectListInfo.do';
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
		function goBack(){
			location.href='${ctx}/subjectAction/toSubjectListInfo.do?queryParams=${params.queryParams}';
		}
		
       function matchStart (term, text, option) {
    	  //console.info(option.id)
    	  if (text.toUpperCase().indexOf(term.toUpperCase()) >= 0) {
    	    	return true;
    	  }
    	  if (option.id.toUpperCase().indexOf(term.toUpperCase())>=0) {
	    	    return true;
	      }
    	  return false;
    	}

    	$.fn.select2.amd.require(['select2/compat/matcher'], function (oldMatcher) {
    	  $('#parentSubjectNo').select2({
    	    matcher: oldMatcher(matchStart)
    	  })
    	  
    	  $('#parentSubjectNo').val("${params.parentSubjectNo}");
    	  var len = $('#parentSubjectNo option:selected').length;
          if (len == 0) {
            $('#select2-parentSubjectNo-container').text('')
          };
    	});

	</script>
</title>

</html>

