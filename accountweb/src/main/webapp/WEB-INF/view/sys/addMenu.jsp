<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="${ctx}/css/plugins/easyui/themes/bootstrap/easyui.css">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">系统管理</div>
            <em class=""></em>
            <div class="pull-left active">新增菜单</div>
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
					 <form method="post" class="form-horizontal" id="addSysMenuForm">
							<div class="form-group">
								<label class="pull-left control-label">菜单名称：</label>
								<div class="col-sm-2">
									<input type="text" class="form-control" name="menuName2" id="menuName2">
								</div>
								<label class="pull-left control-label">菜单编码：</label>
								<div class="col-sm-2">
									<input type="text" class="form-control" name="menuCode2" id="menuCode2">
								</div>
								<label class="pull-left control-label">父级菜单：</label>
								<div class="col-sm-2">
									<!-- <input type="text" class="form-control" name="parentId2" id="parentId2"> -->
									<input class="easyui-combotree form-control" data-options="url:'${ctx}/menuAction/menuComboTree.do',method:'get',required:true" style="width:260px;height:28px"  panelHeight="auto" name="parentId2" id="parentId2">
								</div>
                            
								
							</div>
							<div class="form-group lastbottom">
								<label class="pull-left control-label">菜单路径：</label>
								<div class="col-sm-2">
									<input type="text" class="form-control" name="menuUrl2" id="menuUrl2">
								</div>
								<label class="pull-left control-label"> &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;排序：</label>
								<div class="col-sm-2">
									<input type="text" class="form-control" name="orderNo2" id="orderNo2">
								</div>
							</div>
							<div class="form-group">
              
                               <div class="col-sm-12 col-sm-offset-13  ">
                              
                               		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                               	   <sec:authorize access="hasAuthority('menu:insert')">
                                     <button class="btn btn-success  " type="button" onclick="addMenu()"><span class="glyphicon gly-ok"></span>提交</button>
                                   </sec:authorize>
                                   <button class="btn btn-default  col-sm-offset-14" type="button" onclick="goBack()"><span class="glyphicon gly-return"></span>返回</button>
                               </div>
                          	</div>
					</form>
				</div>
			</div>
		</div>
		
	</div>
	<!-- 填充内容结束 -->
</body>

<title>
   <script type="text/javascript" src="${ctx}/js/jquery.easyui.min.js"></script>
	<script type="text/javascript">
		// 去除空格啊
        $('input').blur(function(){
            replaceSpace(this);
        })
        function replaceSpace(obj){
            obj.value = obj.value.replace(/\s/gi,'')
        }
		$("#sebjectForm").submit(function(){
			var data=$("#sebjectForm").serialize();
			$.ajax({
				url:"${ctx}/subjectAction/subjectUpdate.do",
				type:"POST",
				data:data,
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
			return false;
		});
		function goBack(){
			location.href='${ctx}/sysAction/menu.do';
		}
			
		function addMenu(){
			var _menu_name = $("#menuName2").val();
			var _menu_code = $("#menuCode2").val();
			var _menu_parent_id = $('#parentId2').combotree('getValue');
			var _menu_url = $("#menuUrl2").val();
			var _order_no = $("#orderNo2").val();
			
			$.post('${ctx}/menuAction/addMenu.do', 
					{ menuName:_menu_name,menuCode:_menu_code,parentId:_menu_parent_id,menuUrl:_menu_url,orderNo:_order_no,'${_csrf.parameterName}':'${_csrf.token}' },
					function(msg) { 
						if(!msg.state){
							//alert(msg.msg);
				            toastr.error(msg.msg,'错误');
						}else{
							toastr.success(msg.msg,'提示');
							setTimeout(function() {
								location.href='${ctx}/sysAction/menu.do';
							}, 1000);
						}
					}); 
		}
	</script>
</title>

</html>

