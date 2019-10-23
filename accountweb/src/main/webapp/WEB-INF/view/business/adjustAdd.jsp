<%@ page pageEncoding="utf-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='sec' uri='http://www.springframework.org/security/tags'%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<link href="${ctx}/css/plugins/select2/select2.min.css" rel="stylesheet">
	<link href="${ctx}/css/plugins/select2/select2-skins.min.css" rel="stylesheet">
	 <!-- Sweet Alert -->
    <link href="${ctx}/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
</head>
<body>
	<div class="row wrapper border-bottom white-bg page-heading">
		<div class="col-lg-10 location-nav" >
            <div class="pull-left">当前位置</div>
            <em class=""></em>
            <div class="pull-left">业务调账</div>
            <em class=""></em>
            <div class="pull-left active">新增调账申请</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">

				<div class="ibox-content">
					 <form class="form-horizontal" id="form1" >
                           <div class="form-group">
								 <label class="col-sm-2 control-label">审核人：</label>
								 <div class="col-sm-2">
                                	<select id="approver" class="form-control" name="approver"></select>
								 </div>
                            </div>
                            <div class="form-group lastbottom">                          		 
                           		<label class="col-sm-2 control-label">备注：</label>
                                <div class="col-sm-2">
                                	<textarea class="form-control" rows="3" style="" name="remark"></textarea>                        		
                                </div>   
                            </div>
                            
                            <div class="form-group">
                                   		<label class="col-sm-2 control-label aaa"></label>
                                   		 <div class="row-fluid" style="float: left; color: blue;">
                                   		 <nobr>
                                   		 <sec:authorize access="hasAuthority('businessAdd:downloadTpl')">
                                   		  <button class="btn btn-primary btn-xs " type="button" id="btnDownload"><span class="glyphicon glyphicon-send" aria-hidden="true"></span>&nbsp;下载模板</button>
                                   		 </sec:authorize>
                                   		 <sec:authorize access="hasAuthority('businessAdd:insert')">
                                   		 	<input id="fileupload" name="fileupload" type="file"  data-url="${ctx}/business/insertAdjust.do?${_csrf.parameterName}=${_csrf.token}" data-icon="false">
                                   		 </sec:authorize>
                                   		 </nobr>
                                   		 </div>
                                   		 <sec:authorize access="hasAuthority('businessAdd:insert')">
                                   		  	<button class="btn btn-success col-sm-offset-14" type="button"  id="btnSubmit"><span class="glyphicon gly-ok" aria-hidden="true"></span>提交</button>
                                   		 </sec:authorize>
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
<script src="js/plugins/filestyle/bootstrap-filestyle.js"></script>
<script src="js/plugins/fileupload/jquery.fileupload.js"></script>
<script src="js/plugins/fileupload/jquery.ui.widget.js"></script>  
<script src="js/plugins/fileupload/jquery.iframe-transport.js"></script>  
<script src="${ctx}/js/plugins/select2/select2.full.min.js"></script>
<script src="${ctx}/js/plugins/select2/i18n/zh-CN.js"></script>
<!-- Sweet alert -->
<script src="${ctx}/js/plugins/sweetalert/sweetalert.min.js"></script>
	<script type="text/javascript">
		$("#btnSubmit").on('click', function () {
			var approver = $.trim($("#approver").val()) ;
			if(approver == ""){
				swal({title:"提示" ,text:"请选择审核人" ,animation:"slide-from-top"});
			}else{
				swal({title:"提示" ,text:"请选择上传的文件" ,animation:"slide-from-top"});
			}
	    });	
	
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
		        inputs+='<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />'; 
		        // request发送请求
		        jQuery('<form action="'+ url +'" method="'+ (method||'post') +'">'+inputs+'</form>')
		        .appendTo('body').submit().remove();
		    };
		};

		function theSubmit(filename){		
			var data=$("#form1").serialize();
			data += '&filename=' + filename;
			//console.info(data);
			$.ajax({
				url:"${ctx}/business/saveAdjustInfo.do",
				type:"POST",
				data:data,
				success :function(msg){
					if(!msg.state){
						alert(msg.msg);
						//showError(msg.msg);
					}else{
						alert(msg.msg);	
					}
				}
			});
			return false;
		}
		
		$('#btnDownload').bind('click', function() {
			 $.download('${ctx}/business/downloadAdjustAccTemplate.do','find=commoncode','post');
		});
		function thenfileupload(){
			$('#fileupload').fileupload({
		        dataType: 'json',
		        //formData:{"${_csrf.parameterName}":"${_csrf.token}"},
		        add: function (e, data) {
		            data.context = $("#btnSubmit").off('click').on('click', function () {
		                data.submit();
		            });
		        },
		        done: function (e, data) {
		        	if(data.result.error){
		        		swal({title:"错误" ,text:data.result.error ,animation:"slide-from-top"});
		        	}else{
		        		if(data.result.statu){
		        			$("#approver").empty();
		        			$("#select2-approver-container").html("审核人") ;
		        			$(".badge").remove() ;
		        			$('#form1').get(0).reset() ;
		        			toastr.success(data.result.msg,'提示');
		        		}else{
		        			swal({title:"提示" ,text:data.result.msg ,animation:"slide-from-top"});
		        		}
		        	}
		        }
		    });
		}
		$(function () {
			$('#fileupload').filestyle({
				input : false,
				buttonText : '上传文件',
				size : 'xs',
				buttonName : 'btn-primary'
			});
			//setInterval("thenfileupload()",3000);
			thenfileupload();		    
		});
		
		
		
		function formatRepo (repo) {
	        if (repo.loading) return repo.text;
			//console.info(repo.id);
			return repo.id+'('+repo.text+')';  
	      }

	      function formatRepoSelection (repo) {
	    	  //console.info("formatRepoSelection:"+ repo.text);
	    	  //return repo.id+'('+repo.text+')';
	    	  return repo.text ;
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
	      $('#approver').select2({
		    	  		ajax: {
			    		  	url: "${ctx}/shiroUserAction/queryShiroUserName.do",
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
		    		  //placeholder: "审核人",  
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

