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
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">科目管理</div>
            <em class=""></em>
            <div class="pull-left active">科目信息详情</div>
		</div>
	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="sebjectForm">
                          <div class="form-group">
                          		   <div class="pull-left control-label labeldiv2">科目编号：</div>
                                   <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${params.subjectNo}</div>
								   <div class="pull-left control-label labeldiv2">科目名称：</div>
                                   <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${params.subjectName}</div>
                                   <div class="pull-left control-label labeldiv2">科目别名：</div>
									<div class="col-sm-2 control-label" style="text-align:left;font-size:12px">
									         ${params.subjectAlias}
									  </div>									  										
                                    		

						</div>
						
						  <div class="form-group">
                                   <div class="pull-left control-label labeldiv2">科目类别：</div>
                                    <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">
                                         ${params.subjectTypeName}                  
                                      </div>
                                   <div class="pull-left control-label labeldiv2">科目级别：</div>
									<div class="col-sm-2 control-label" style="text-align:left;font-size:12px">
									         ${params.subjectLevelName}
									 </div>
                                   <div class="pull-left control-label labeldiv2">余额方向：</div>
                                    <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">
                                         ${params.balanceFrom}                  
                                      </div>
						</div>
						<div class="form-group">
                                   
                                   <div class="pull-left control-label labeldiv2">上级科目编号：</div>
                                   <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${params.parentSubjectNo}</div>
                                    <div class="pull-left control-label labeldiv2">上级科目名称：</div>
                                   <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${params.parentSubjectName}</div>
                                   
                                   <div class="pull-left control-label labeldiv2">参加借贷平衡检查：</div>
                                   <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${params.debitCreditFlag}</div>
                                   
						</div>
						<div class="form-group">
                                    <div class="pull-left control-label labeldiv2">开立内部账户：</div>
                                   <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${params.isInnerAccount}</div>
                                <div class="pull-left control-label labeldiv2">修改余额标志：</div>
	                             <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">
	                                   ${params.innerDayBalFlag}                  
	                             </div>
	                            <div class="pull-left control-label labeldiv2">明细处理方式：</div>
	                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${params.innerSumFlag}</div>
			
						</div>
						<br/>
			            <div class="form-group">
			              	<div class="pull-left control-label labeldiv2">创建人：</div>
                              <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">
                                   ${params.creator}                  
                              </div>
                            <div class="pull-left control-label labeldiv2">创建时间：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${params.createTime}</div>
			
			            </div>
			            <div class="form-group ">
			              	<div class="pull-left control-label labeldiv2">修改人：</div>
                              <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">
                                   ${params.updator}                  
                              </div>
                            <div class="pull-left control-label labeldiv2">修改时间：</div>
                            <div class="col-sm-2 control-label" style="text-align:left;font-size:12px">${params.updateTime}</div>
			
			            </div>
                        <div class="clearfix lastbottom"></div>
                          
                                <!-- <div class="clearfix"></div> -->
                        <div class="form-group " style="">
                           <div class="col-sm-12 col-sm-offset-13  ">
                           <sec:authorize access="hasAuthority('subjectAdd:update')">                               	   
                           		<button class="btn btn-primary" style="" type="button" onclick="toUpdateSubjectDetail()"><span class="glyphicon gly-protect"></span>维护</button>
                           </sec:authorize>
                           <button id="returnUp" type="button" class=" btn btn-default col-sm-offset-14" onclick="window.location.href='${ctx}/subjectAction/toSubjectListInfo.do?queryParams=${params.queryParams}'" value="" /><span class="glyphicon gly-return"></span>返回</button>
                           </div>
                        </div>  
                        <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />         
                    </form>
				</div>
			</div>
		</div>
			
	</div>
	
   </body>
	<title>
	<script type="text/javascript">
	function toUpdateSubjectDetail(){
		 location.href='${ctx}/subjectAction/toSubjectUpdate.do?subjectNo=${params.subjectNo}&flag=detail';
	}
	
	</script>
	
	
	
	 
	 </title>
</html>  
      