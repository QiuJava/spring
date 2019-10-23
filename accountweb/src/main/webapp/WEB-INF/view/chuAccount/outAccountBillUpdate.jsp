
<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<!-- jqGrid plugin -->
	<link href="${ctx}/css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/jqGrid/ui.jqgrid.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/bootstrapTour/bootstrap-tour.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/select2/select2.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/webuploader/webuploader.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">出账管理</div>
            <em class=""></em>
            <div class="pull-left active">出账单修改</div>
		</div>
	</div>
	<!-- 填充内容开始 -->
    
    	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
			<!-- 出账单基本信息 -->
			<div class="col-lg-12">
				<div class="ibox-content">
					<!--边框表格布局: table table-bordered \悬停表格 : table table-hover-->					 
					<table class="table table-bordered" style="width:100;max-width:100%">
					   <thead>
					      <tr style="text-align: center;">
					         <td width="200">出账单 ID</td>
					         <td width="200">系统计算时间</td>
					         <td width="200">出账任务金额</td>
					         <td width="200">计算出账金额</td>
					         <td width="200">有余额上游数量</td>
					         <td width="200">有余额商户数量</td>
					         <td width="200">确认出账状态</td>
					      </tr>
					   </thead>
					   <tbody>
					      <tr>
					         <td>${outBill.id }</td>
					         <td>
					         	<fmt:formatDate value="${outBill.sysTime }" pattern="yyyy-MM-dd HH:mm:ss"/> 
					         </td>
					         <td style="text-align: right;">${outBill.outAccountTaskAmount }</td>
					         <td style="text-align: right;" id="newAmount">${outBill.calcOutAmount }</td>
					         <td>${outBill.balanceUpCount }</td>
					         <td>${outBill.balanceMerchantCount }</td>
					         <td>${outBill.outBillStatus }</td>
					      </tr>
					   </tbody>
					</table>
					
				</div>
				
			</div>
			
			<!-- 收单机构出账明细 -->
			<div class="col-lg-12">
				<div class="ibox ">
					<div class="ibox-content">
						<div class="jqGrid_wrapper">
						<table id="table_list_acqOutBill"></table>
						<div id="pager_list_acqOutBill"></div>
						</div>
					</div>
				</div>
			</div>
			
			<!-- 商户出账明细 -->
			<div class="col-lg-12">
				<div class="ibox ">
					<div class="ibox-content">
						
						<form class="form-horizontal" id="outAccountBillDetailForm">
	                          
	                          <div class="form-group">
	                          	    <label class="col-sm-2 control-label">商户编号:</label>
	    							<div class="col-sm-2">
	    								<input  type="text" class="form-control"  name="merchantNo">
	    							</div>
	                                <label class="col-sm-1 control-label">出款通道:</label>
	    							<div class="col-sm-2">
										<select class="form-control" name="acqOrg" id="acqOrg"> 
									         <option value="ALL" selected="selected">全部</option>
									         <c:forEach var="acqOrg" items="${acqOrgList}">
												<option value="${acqOrg.sysValue}">
													${acqOrg.sysName}
												</option>
											</c:forEach>
										</select>      
									</div>	
									
									
	                           </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label">有无财务调整备注:</label>
                                    <div class="col-sm-2">
                                        <select class="form-control" name="isChangeRemark" id="isChangeRemark"> 
                                             <option value="ALL" selected="selected">全部</option>
                                             <option value="1" >有</option>
                                             <option value="0" >无</option>
                                        </select>      
                                    </div>
                                    <label class="col-sm-1 control-label">校验通过:</label>
                                    <div class="col-sm-2">
                                        <select class="form-control" name="verifyFlag" id="verifyFlag"> 
                                             <option value="-1" selected="selected">全部</option>
                                             <c:forEach var="vf" items="${verifyFlagList}">
                                                <option value="${vf.sysValue}">
                                                    ${vf.sysName}
                                                </option>
                                             </c:forEach>
                                        </select>      
                                    </div>
                                    <label class="col-sm-1 control-label">出账状态:</label>
                                    <div class="col-sm-2">
                                        <select class="form-control" name="outBillStatus" id="outBillStatus"> 
                                             <option value="-1" selected="selected">全部</option>
                                             <c:forEach var="vf" items="${billStatusList}">
                                                <option value="${vf.sysValue}">
                                                    ${vf.sysName}
                                                </option>
                                             </c:forEach>
                                        </select>      
                                    </div>
                                </div>   
	                          <div class="form-group">
	                          	    <label class="col-sm-2 control-label">商户余额:</label>
	    							<div class="col-sm-2">
	    								<input  type="text" class="form-control" name="merchantBalance1">
	    							</div>
	                                <label class="pull-left control-label" style="width:0">~</label>
	                                <div class="col-sm-2">
	                                    <input  type="text" class="form-control" name="merchantBalance2">
	                                </div>
	                                
	                           </div>
                               <div class="form-group">
                                   <label class="col-sm-2 control-label">商户出账金额:</label>
                                    <div class="col-sm-2">
                                        <input  type="text" class="form-control" name="outAccountTaskAmount1">
                                    </div>
                                    <label class="pull-left control-label" style="width:0">~</label>
                                    <div class="col-sm-2">
                                        <input  type="text" class="form-control" name="outAccountTaskAmount2">
                                    </div>
                                    
                                    
                               </div>
	                           <div class="clearfix lastbottom"></div>
	                            <div class="form-group">
                                        <label class="col-sm-2 control-label aaa"></label>
	                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
	                                   	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	                                   	   <input type="hidden" name="outBillId" value="${outBill.id}" />
	                                   
	                                       	<button class="btn btn-success" type="submit"><span class="glyphicon gly-search"></span>查询</button>
	                                       	<button class="btn btn-default col-sm-offset-14  " type="reset"><span class="glyphicon gly-trash"></span>清空</button>
	                                       	<button class="btn btn-danger col-sm-offset-14" type="button" onclick="exportExcel()"><span class="glyphicon gly-out"></span>导出</button>
	                                       	<c:if test="${outBill.outBillStatus=='0'}">
	                                       	<button class="btn btn-danger col-sm-offset-14" type="button" data-toggle='modal' data-target='#myModalImportExcel'><span class="glyphicon gly-out"></span>导入</button>
	                                       	</c:if>
	                                       	<c:if test="${outBill.outBillStatus=='1'}">
	                                       	<button class="btn btn-danger col-sm-offset-14" type="button" disabled><span class="glyphicon gly-out"></span>导入</button>
	                                       	</c:if>
	                                   <!-- </div> -->
	                             </div>
	                           <br/><br/>
	                    </form>
						
						<div class="jqGrid_wrapper">
							<table id="table_list_outAccountBillDetail"></table>
							<div id="pager_list_outAccountBillDetail"></div>
                             <div class="col-sm-0 col-sm-offset-0  ">
                                 <br /><br />
                                 <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                 <button id="returnUp" type="button" class=" btn btn-success" onclick="confirmOut($(this));" value="" />确认出账</button>
                                 <button id="returnUp" type="button" class=" btn btn-default col-sm-offset-14" onclick="goBack();" value="" /><span class="glyphicon gly-return"></span>返回</button>
                            </div>
						</div>
					</div>
				</div>
			</div>
		</div>
			
	</div>
	
	
	<div class="modal inmodal" id="myModalUpdateStatus" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
        	<div class="modal-content">
            	<div class="modal-header">
                	<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                	<h5 class="modal-title">修改</h5>
                </div>
                <div class="modal-body">
                	<form method="post" class="form-horizontal" id="updatePrivilegeForm">
					<div class="form-group">
						<label class="col-sm-3 control-label">商户出账金额：</label>
						<div class="col-sm-8">
							<input type="hidden" id="merBalance"/>
							<input type="text" class="form-control" name="outAccountTaskAmount2" id="outAccountTaskAmount2" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-3 control-label">财务调整备注：</label>
						<div class="col-sm-8">
							<input type="text" class="form-control" name="changeRemark2" id="changeRemark2" />
						</div>
					</div>
					</form>
         		</div>
				<div class="modal-footer">
					<input type="hidden" id="outBillId2"/>
            		<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            		<button type="button" class="btn btn-success" onclick="Save()">保存</button>
         		</div>
  			</div>
		</div>
	</div>
	
	<div class="modal inmodal" id="myModalImportExcel" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
        	<div class="modal-content">
            	<div class="modal-header">
                	<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                	<h5 class="modal-title">导入-选择文件</h5>
                </div>
                <div class="modal-body">
                	<form method="post" class="form-horizontal" id="updatePrivilegeForm">
					<div class="form-group">
						<label class="col-sm-3 control-label">文件名：</label>
						<div id="uploader" class="wu-example col-sm-8">
    						<!--用来存放文件信息-->
    						<div id="thelist" class="uploader-list"></div>
    						<div class="btns">
        						<div id="picker">选择文件</div>
    						</div>
    					</div>
					</div>
					</form>
         		</div>
				<div class="modal-footer">
					<input type="hidden" id="outBillId2"/>
            		<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            		<button type="button" class="btn btn-success" onclick="confirmImport()">确认导入</button>
         		</div>
  			</div>
		</div>
	</div>
	
   </body>
    
	<title>
	<script src="${ctx}/js/plugins/select2/select2.full.min.js"></script>
	<script src="${ctx}/js/plugins/select2/i18n/zh-CN.js"></script>
	<script src="${ctx}/js/plugins/webuploader/webuploader.js"></script>
	<script src="${ctx}/js/plugins/sweetalert/sweetalert.min.js"></script>
	<script type="text/javascript">
        // 去除空格啊
        $('input').blur(function(){
            replaceSpace(this);
        })
        function replaceSpace(obj){
            obj.value = obj.value.replace(/\s/gi,'')
        }
		$("#outAccountBillDetailForm").submit(function(){
			$("#table_list_outAccountBillDetail").setGridParam({
			       datatype : 'json',
			       page : 1            //Replace the '1' here
			    }).trigger("reloadGrid");
			return false;
		});
	
		function getParams(o){
		     o["${_csrf.parameterName}"] = "${_csrf.token}";    
		     o["outBillId"] = "${params.outBillId}";
		}
		
		function getAcqOrgGroups(id) {  
		    var arr = []
		    <c:forEach var="acqOrg" items="${acqOrgList}">
		    arr.push('${acqOrg.sysValue}:${acqOrg.sysName}');
			 </c:forEach>	
		    return arr.join(';');  
		}
		
		function getParams2(o){
			 var data=$("#outAccountBillDetailForm").serializeArray();
		     $.each(data, function() {    
		             o[this.name] = this.value || '';    
		     });   
		     o["outBillId"] = "${params.outBillId}";
		}
		
		
		$(document).ready(function() {
			/* 收单机构出账明细      ==开始  ==       */
			var lastsel;
			//var sequenceNo = 0 ;
			//var data=$("#bankAccountForm").serialize();
			//alert(data);alert('${list[0].getUserType}');
            // 初始化表格
            $("#table_list_acqOutBill").jqGrid({
            	url:"${ctx}/chuAccountAction/findAcqOutBillList.do",
                datatype: "json",	
                mtype: "POST",
                height:"auto",
                autowidth: true,
                shrinkToFit: true,
                rowNum: 10,
                rowList: [10, 20],
                colNames:['上游', '上游余额','出账单金额','出账任务金额','实际出账金额'],
                colModel: [
					{name: 'acqOrgNo', index: 'acqOrgNo', width: 50, align: "left"},
					{name: 'upBalance', index: 'up_balance',width: 50, align: "right"},
					{name: 'outAccountTaskAmount', index: 'outAccountTaskAmount',width: 50, align: "right",formatter: 'number'},
					{name: 'calcOutAmount', index: 'calcOutAmount',width: 50, align: "right",formatter: 'number'},
					{name: 'outAmount', index: 'outAmount',width: 50, align: "right",formatter: 'number'},
                    
                ],
                onSelectRow: function(id){
            		if(id && id!==lastsel){
            			jQuery('#table_list_acqOutBill').jqGrid('restoreRow',lastsel);
            			jQuery('#table_list_acqOutBill').jqGrid('editRow',id,true);
            			lastsel=id;
            		}
            	},
                multiselect: false,//支持多项选择
                pager: "#pager_list_acqOutBill",
                viewrecords: true,
                hidegrid: false,
                jsonReader : {
        			root : "result",
        			total : "totalPages",
        			page : "pageNo",
        			pageSize : "pageSize",
        			records : "totalCount",
        			repeatitems : false
        		},
        		prmNames : { 
        		    page:"pageNo",
        		    rows:"pageSize"
        		},
        		serializeGridData:function (postData) {
        			getParams(postData);
                    return postData;
                },
                gridComplete:function(){  //在此事件中循环为每一行添加修改和删除链接
                    var ids=$("#table_list_acqOutBill").jqGrid('getDataIDs');
                    for(var i=0; i<ids.length; i++){
                        var id=ids[i];   
                        var detail = "<a href='${ctx}/chuAccountAction/toSettleTransferDetail.do?id='"+id+"' class='default-details' title='详情'>详情</a>";
                        jQuery("#table_list_acqOutBill").jqGrid('setRowData', ids[i], { Detail: detail });
                    }
                }
            });
            /* 收单机构出账明细       ==结束 ==  */
            
            /* 商户出账明细       == 开始  ==   */
            
            $("#table_list_outAccountBillDetail").jqGrid({
            	url:"${ctx}/chuAccountAction/findOutBillDetailList.do",
                datatype: "json",	
                mtype: "POST",
                height: 'auto',
                autowidth: true,
                shrinkToFit:false,
                autoScroll: false,
                rowNum: 10,
                rowList: [10, 20],
                colNames:['操作','出账单明细',  '商户编号','商户结算中金额','商户出账金额','出款通道','校验通过','校验错误信息','财务调整备注','记账状态','出账结果','outBillStatus'],
                colModel: [
					{name:'Detail',index:'detail',width:100,align:"center",sortable:false,frozen:true},
					{name: 'id', index: 'id', width: 150, align: "left"},
					{name: 'merchantNo', index: 'merchantNo',width: 200, align: "left"},
					{name: 'merchantBalance', index: 'merchantBalance',width: 180, align: "right",formatter: 'number'},
					{name: 'outAccountTaskAmount', index: 'outAccountTaskAmount',width: 180, align: "right",formatter: 'number'},
					{name: 'acqOrgNo', index: 'acqOrgNo',width: 150, align: "center"},
					{name: 'verifyFlag', index: 'verifyFlag',width: 150, align: "center",formatter: customVerifyFlagFormatter},
					{name: 'verifyMsg', index: 'verifyMsg',width: 220, align: "center"},
					{name: 'changeRemark', index: 'changeRemark',width: 220, align: "center"},
					{name: 'recordStatus', index: 'outBillStatus',width: 220, align: "center",formatter:customRecordStatusFormatter},
					{name: 'outBillResult', index: 'outBillResult',width: 220, align: "center"},
					{name: 'outBillStatus', index: 'outBillStatus',width: 220, align: "center",hidden:"true"},
                ],
                multiselect: false,//支持多项选择
                pager: "#pager_list_outAccountBillDetail",
                viewrecords: true,
                hidegrid: false,
                jsonReader : {
        			root : "result",
        			total : "totalPages",
        			page : "pageNo",
        			pageSize : "pageSize",
        			records : "totalCount",
        			repeatitems : false
        		},
        		prmNames : { 
        		    page:"pageNo",
        		    rows:"pageSize"
        		},
        		serializeGridData:function (postData) {
        			getParams2(postData);
                    return postData;
                },
                gridComplete:function(){  //在此事件中循环为每一行添加修改和删除链接
                    var ids=$("#table_list_outAccountBillDetail").jqGrid('getDataIDs');
                    for(var i=0; i<ids.length; i++){
                        var id=ids[i];   
                        var getRow = $('#table_list_outAccountBillDetail').getRowData(id);
                        //var detail = "<a href='${ctx}/chuAccountAction/toSettleTransferDetail.do?id='"+id+"' class='default-details' title='详情'>详情</a>";
                        var detail = "";
                        if (${outBill.outBillStatus} == '0') {
                        	detail += "<a href='javascript:void(0);' class='default-details' data-toggle='modal' data-target='#myModalUpdateStatus' title='编辑' onclick='Modify(\"" + id + "\")' id='m"+id+"'>编辑</a>&nbsp;";
                        } else {
                        	detail += "<a href='javascript:void(0);' class='default-undetails' title='编辑' style='cursor:not-allowed;' id='m"+id+"'>编辑</a>&nbsp;";
                        }
                        /**
                        if (getRow.outBillStatus=='2') {
                        	detail += "<a href='javascript:void(0);' class='default-details' title='出款' onclick='chuAmount(\"" + id + "\")' id='m"+id+"'>出款</a>";
                        }
                        */
                        jQuery("#table_list_outAccountBillDetail").jqGrid('setRowData', ids[i], { Detail: detail });
                    }
                }
            });
            jQuery("#table_list_outAccountBillDetail").jqGrid('setFrozenColumns');
            /* 商户出账明细      ==  结束  ==  */
            
            // Add responsive to jqGrid
            $(window).bind('resize', function () {
                var width = $('.jqGrid_wrapper').width();
                $('#table_list_acqOutBill').setGridWidth(width);
                $('#table_list_outAccountBillDetail').setGridWidth(width);
            });
		});
		
		function customVerifyFlagFormatter(cellvalue, options, rowObject){  
			<c:forEach var="vf" items="${verifyFlagList}">
			  	if(cellvalue == '${vf.sysValue}'){
				  	return '${vf.sysName}';
			  	}
		 	</c:forEach>	
			return "" ;
		}
		
		function customRecordStatusFormatter(cellvalue, options, rowObject){  
			<c:forEach var="vf" items="${recordStatusList}">
			  	if(cellvalue == '${vf.sysValue}'){
				  	return '${vf.sysName}';
			  	}
		 	</c:forEach>	
			return "" ;
		}
		
		function chuAmount(id) {
			var model = $("#table_list_outAccountBillDetail").jqGrid('getRowData',id);
			var _id = model.id;
			swal({  
				 	title: "确认是否出款?", 
				   	type: "warning",  
				    showCancelButton: true,   
				    cancelButtonText: "取消",  
				   	confirmButtonColor: "#DD6B55",  
				    confirmButtonText: "继续出款",  
				    closeOnConfirm: false 
				    }, 
				    function(){
				    	$.post('${ctx}/chuAccountAction/outBillDetailChuAmount.do', 
							{ id:_id,'${_csrf.parameterName}':'${_csrf.token}' },
							function(msg) {
								if(!msg.status){
					            	toastr.error(msg.msg,'错误');
								}else{
									toastr.success(msg.msg,'提示');
									$("#table_list_outAccountBillDetail").jqGrid('setGridParam',{
							           url : "${ctx}/chuAccountAction/findOutBillDetailList.do",
							           datatype : 'json',
							        }).trigger('reloadGrid');//重新载入
								}
								swal.close();
						});
				    });
		}
		
		function Modify(id) {   //单击修改链接的操作         
			var model = $("#table_list_outAccountBillDetail").jqGrid('getRowData',id);
			
			<c:forEach var="acqOrg" items="${acqOrgList}">
				  if(model.acqOrgNo == '${acqOrg.sysName}'){
					  $("#acqOrgNo2").val('${acqOrg.sysValue}');
				  }
			</c:forEach>
			$("#merBalance").val(model.merchantBalance);
			$("#outAccountTaskAmount2").val(model.outAccountTaskAmount);
			$("#changeRemark2").val(model.changeRemark); 
			$("#outBillId2").val(model.id);
		}
		function Save(id) {    
			var merBalance = $("#merBalance").val();
			var outAccountTaskAmount2 = $("#outAccountTaskAmount2").val();
			var changeRemark2 = $("#changeRemark2").val();
			var _id =  $("#outBillId2").val();
			
			$.post('${ctx}/chuAccountAction/saveOutAccountBillUpdate.do', 
						{ id:_id,merchantBalance:merBalance,outAccountTaskAmount:outAccountTaskAmount2,changeRemark:changeRemark2,'${_csrf.parameterName}':'${_csrf.token}' },
						function(msg) {
							if(!msg.status){
					            toastr.error(msg.msg,'错误');
							}else{
								toastr.success(msg.msg,'提示');
								$("#myModalUpdateStatus").modal("hide");
								$("#newAmount").text(msg.newAmount);
								$("#table_list_acqOutBill").jqGrid('setGridParam',{
							           url : "${ctx}/chuAccountAction/findAcqOutBillList.do",
							           datatype : 'json',
							        }).trigger('reloadGrid');//重新载入
								
								$("#table_list_outAccountBillDetail").jqGrid('setGridParam',{
							           url : "${ctx}/chuAccountAction/findOutBillDetailList.do",
							           datatype : 'json',
							        }).trigger('reloadGrid');//重新载入
							}
						});
		}
		
		function goBack(){
			window.location.href='${ctx}/chuAccountAction/toChuAccountBillManage.do';
		}
		
		/*导出*/
   		function exportExcel() {  
   			var data=$("#outAccountBillDetailForm").serializeArray();
   			$.download('${ctx}/chuAccountAction/exportOutBillDetail.do',data,'post');
   	    }
   	    
   	    // Ajax 文件下载
		jQuery.download = function(url, data, method){
		    // 获得url和data
		    if( url && data ){ 
		        // data 是 string 或者 array/object
		        data = typeof data == 'string' ? data : jQuery.param(data);
		        // 把参数组装成 form的  input
		        var inputs = '';
		        jQuery.each(data.split('&'), function(){ 
		            var pair = this.split('=');
		            inputs+='<input type="hidden" name="'+ pair[0] +'" value="'+ pair[1] +'" />';
		        });
		        // request发送请求
		        jQuery('<form action="'+ url +'" method="'+ (method||'post') +'">'+inputs+'</form>')
		        .appendTo('body').submit().remove();
		    };
		};	
		
		var $list = $('#thelist');
		var uploader = WebUploader.create({
			auto:false,
    		// swf文件路径
    		swf: '/js/Uploader.swf',

    		// 文件接收服务端。
    		server: '${ctx}/chuAccountAction/importOutBillDetailFile.do',
			fileNumLimit:1,
    		// 选择文件的按钮。可选。
    		// 内部根据当前运行是创建，可能是input元素，也可能是flash.
    		pick: '#picker',
    		formData: {
    			'${_csrf.parameterName}':'${_csrf.token}',
    			'outBillId':'${outBill.id}'
    		},

    		// 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
   		 	resize: false
		});
		
		// 当有文件被添加进队列的时候
		uploader.on( 'fileQueued', function( file ) {
    		$list.append( '<div id="' + file.id + '" class="item">' +
        		'<h4 class="info">' + file.name + '</h4>' +
        		'<p class="state">等待上传...</p>' +
    		'</div>' );
		});
		
		// 文件上传过程中创建进度条实时显示。
		uploader.on( 'uploadProgress', function( file, percentage ) {
    		var $li = $( '#'+file.id ),
        		$percent = $li.find('.progress .progress-bar');

    		// 避免重复创建
    		if ( !$percent.length ) {
       			$percent = $('<div class="progress progress-striped active">' +
          		'<div class="progress-bar" role="progressbar" style="width: 0%">' +
          		'</div>' +
        		'</div>').appendTo( $li ).find('.progress-bar');
    		}

    		$li.find('p.state').text('上传中');

    		$percent.css( 'width', percentage * 100 + '%' );
		});
		
		uploader.on( 'uploadSuccess', function( file, data ) {
    		$( '#'+file.id ).find('p.state').text('已上传');
    		if (data.status) {
    			$list.html("");
    			toastr.success(data.msg,'提示');
    			$("#myModalImportExcel").modal("hide");
				$("#newAmount").text(data.newAmount);
				$("#table_list_acqOutBill").jqGrid('setGridParam',{
					url : "${ctx}/chuAccountAction/findAcqOutBillList.do",
					datatype : 'json',
				}).trigger('reloadGrid');//重新载入
								
				$("#table_list_outAccountBillDetail").jqGrid('setGridParam',{
					url : "${ctx}/chuAccountAction/findOutBillDetailList.do",
					datatype : 'json',
				}).trigger('reloadGrid');//重新载入
    		} else {
    			toastr.error(data.msg,'错误');
    		}
		});

		uploader.on( 'uploadError', function( file ) {
    		$( '#'+file.id ).find('p.state').text('上传出错');
		});

		uploader.on( 'uploadComplete', function( file ) {
    		$( '#'+file.id ).find('.progress').fadeOut();
		});
		
		uploader.on("beforeFileQueued",function(file){
			// 移除所有缩略图并将上传文件移出上传序列
        	for (var i = 0; i < uploader.getFiles().length; i++) {
           		// 将图片从上传序列移除
            	uploader.removeFile(uploader.getFiles()[i], true);
            	$list.html("");
        	}
		});
		
		function confirmImport() {
			uploader.upload();
		}
		
		function confirmOut(_this) {
			var data = {'${_csrf.parameterName}':'${_csrf.token}', 'id':'${outBill.id}'}
			$.ajax({
				url:"${ctx}/chuAccountAction/confirmOut.do",
				type:"POST",
				data:data,
				beforeSend:function(){
	        		_this.prop('disabled',true);
	        	},
				success :function(msg){
					//TODO:按钮恢复
					_this.prop('disabled',false);
					if(!msg.status){
						toastr.error(msg.msg,'提示','错误');
					}else{
						toastr.success(msg.msg,'提示');
						$("#table_list_outAccountBillDetail").setGridParam({
			       			datatype : 'json',
			       			page : 1            //Replace the '1' here
			    		}).trigger("reloadGrid");
						setTimeout(function() {
							location.href="${ctx}/chuAccountAction/toChuAccountBillManage.do";
						}, 1000);
					}
				},
				error: function() {
					//TODO:按钮恢复
                    _this.prop('disabled',false);
				}
			});
		}
	</script>
	 </title>
</html>  
      