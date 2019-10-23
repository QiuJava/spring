
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
            <div class="pull-left active">修改数据字典</div>
        </div>
	</div>
	<div class="row wrapper wrapper-content  animated fadeInRight">
		
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-title">
					<div class="ibox-tools">
						<a class="collapse-link"> <i class="fa fa-chevron-up"></i></a> 
					</div>
				</div>
				<div class="ibox-content">
					 <form class="form-horizontal" id="sysDictForm">
					 
                          <div class="form-group">
                          	   <label class="col-sm-2 control-label">字典键：</label>
                               <div class="col-sm-2"><input type="text" class="form-control" required name="sysKey" value="${sysDict.sysKey }"></div>
                               <label class="col-sm-2 control-label">字典名称：</label>
                               <div class="col-sm-2"><input type="text" class="form-control" required name="sysName" value="${sysDict.sysName }" ></div>
                                <label class="col-sm-2 control-label">字典值：</label>
                               <div class="col-sm-2"><input type="text" class="form-control" required name="sysValue" value="${sysDict.sysValue }" ></div>
                                				
						</div>
						<div class="form-group">
                               <label class="col-sm-2 control-label">HTML名称：</label>
                               <div class="col-sm-2"><input type="text" class="form-control" name="htmlName" value="<c:out value='${sysDict.htmlName }' escapeXml='true'/>" ></div>
                                				
                                <label class="col-sm-2 control-label" >状态：</label>
                               <div class="col-sm-2">
                                    <select class="form-control" name="status"> 
                                         <option value="0" 
                                         <c:if test="${sysDict.status=='0' }"> selected="selected"</c:if>>无效</option>
                                         <option value="1"  
                                         <c:if test="${sysDict.status=='1' }"> selected="selected"</c:if>>有效</option>
                                    </select>  
                                </div> 
                               <label class="col-sm-2 control-label">排序号：</label>
                               <div class="col-sm-2"><input type="text" class="form-control" name="orderNo" value="${sysDict.orderNo }"></div>
						</div>
						<div class="form-group">
                          	   <label class="col-sm-2 control-label" >备注：</label>
                               <div class="col-sm-2"><input type="text" class="form-control"  name="remark" value="${sysDict.remark }"></div>
						</div>
                                 
                          
                                <div class="clearfix lastbottom"></div>
                                
                                   <div class="form-group ">
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                        <label class="col-sm-2 control-label aaa"></label>

                                   		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                   		<input type="hidden" name="id" value="${sysDict.id}" />
                                       <button class="btn btn-success  " type="submit" >保存</button>
                                       <button class="btn btn-default  col-sm-offset-14" type="button" onclick="goBack()"><span class="glyphicon gly-return"></span>返回</button>
                                   <!-- </div> -->
                              	 </div>
                                 <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
                    </form>
				</div>
			</div>
		</div>
				
	</div>
</body>

<title>
   <script type="text/javascript" src="js/plugins/ztree/jquery.ztree.core.min.js"></script>
   <script type="text/javascript" src="js/plugins/ztree/jquery.ztree.excheck.min.js"></script>
	<script type="text/javascript">
        //去除空格啊
            /* $('input').blur(function(){
                replaceSpace(this);
            })
            function replaceSpace(obj){
                obj.value = obj.value.replace(/\s/gi,'')
            } */
		$("#sysDictForm").submit(function(){
			var data=$("#sysDictForm").serialize();
			//alert(data) ;
			$.ajax({
				url:"${ctx}/sysAction/updateSysDict.do",
				type:"POST",
				data:data,
				success :function(msg){
					if(!msg.state){
						//alert(msg.msg);
			                // Display a success toast, with a title
			            toastr.error(msg.msg,'错误');
					}else{
						toastr.success(msg.msg,'提示');
						setTimeout(function() {
			            	location.href='${ctx}/sysAction/toSysDict.do?queryParams=${params.queryParams}';
			            }, 1000);
					}
				}
			});
			return false;
		});
		function goBack(){
			location.href='${ctx}/sysAction/toSysDict.do?queryParams=${params.queryParams}';
		}
		var zTreeObj1, setting = {
				check: {
					enable: true,
					chkStyle: "checkbox",
					chkboxType: { "Y" : "ps", "N" : "ps" }
				},
				data: {
					simpleData: {
						enable: true
					}
				},
				callback: {
					beforeCheck: beforeCheck1,
					onClick: onClick1
				}
			};
			//
			var zNodes =[
				<c:out value="${userRoleTree}" escapeXml="false"/>
			];
			//var className1 = "dark";
			function beforeCheck1(treeId, treeNode) {
				//className1 = (className1 === "dark" ? "":"dark");
				return (treeNode.doCheck !== false);
			}
			var zTreeObj2=null;
			var _userId = null;
			function onClick1(e, treeId, treeNode) {
				
			}
			$(document).ready(function(){
				zTreeObj1 = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
			});
			
/* 			function saveUserRole(){
				var nodes = zTreeObj1.getCheckedNodes(true);
		        var ns = $.grep(nodes, function(n,i){
				  return n.id != 'root';
				});
		        var arr = [];
		        $.each(ns,function(key,val){
		        	arr.push(val.id);
		        });
				var _arr_role_id = arr.join(",");
				console.info(_arr_role_id);
				$.post('${ctx}/sysAction/saveUserRole.do', 
						{ userId: '${user.id}',roleId: _arr_role_id,'${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) {
						   if(!msg.state){
								//alert(msg.msg);
					                // Display a success toast, with a title
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
							}
						});
			} */
			
	</script>
</title>

</html>

