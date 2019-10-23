
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">系统管理</div>
            <em class=""></em>
            <div class="pull-left  active">新增数据字典</div>
        </div>
	</div>
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="sysDictForm">
					 
                          <div class="form-group">
                          	   <label class="pull-left control-label">字典组：</label>
                               <div class="col-sm-3"><input type="text" class="form-control" name="sysKey" required="required"></div>
                               <label class="pull-left control-label">字典名称：</label>
                               <div class="col-sm-3"><input type="text" class="form-control" name="sysName" required="required"></div>
                                <label class="pull-left control-label">字典值：</label>
                               <div class="col-sm-3"><input type="text" class="form-control" name="sysValue" required="required"></div>
						</div>
						<div class="form-group">
                               
                               <label class="pull-left control-label" >&emsp;状态：</label>
                               <div class="col-sm-3">
                                    <select class="form-control" name="status"> 
                                         <option value="0" >无效</option>
                                         <option value="1" selected="selected" >有效</option>
                                    </select>  
                                </div> 
                               <label class="pull-left control-label">排序号：</label>
                               <div class="col-sm-3"><input type="text" class="form-control" name="orderNo"></div>
						</div>
						<div class="form-group">
							<label class="pull-left control-label">HTML名称：</label>
                               <div class="col-sm-3"><input type="text" class="form-control" name="htmlName"></div>
                          	   <label class="pull-left control-label" >&emsp;备注：</label>
                               <div class="col-sm-3"><input type="text" class="form-control" name="remark"></div>
						</div>
                                 
                          
                                <div class="clearfix lastbottom"></div>
                                   <div class="form-group">
                                   <div class="col-sm-11 col-sm-offset-13  ">
                                  
                                   		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                       <button class="btn btn-success" type="submit" ><span class="glyphicon gly-ok"></span>提交</button>
                                       <button class="btn btn-default col-sm-offset-14" type="button" onclick="goBack()"><span class="glyphicon gly-return"></span>返回</button>
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
		$("#sysDictForm").submit(function(){
			var data=$("#sysDictForm").serialize();
			$.ajax({
				url:"${ctx}/sysAction/addSysDict.do",
				type:"POST",
				data:data,
				success :function(msg){
					if(!msg.state){
			                // Display a success toast, with a title
			            toastr.error(msg.msg,'错误');
					}else{
						toastr.success(msg.msg,'提示');
						setTimeout(function() {
			            	location.href='${ctx}/sysAction/toSysDict.do';
			            }, 1000);
					}
				}
			});
			return false;
		});
		function goBack(){
			location.href='${ctx}/sysAction/toSysDict.do';
		}
			
	</script>
</title>

</html>

