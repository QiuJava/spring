
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
            <div class="pull-left">活动账户调账管理</div>
            <em class=""></em>
            <div class="pull-left active">欢乐返不满扣明细</div>
        </div>
	</div>
	<!-- 填充内容开始 -->
	<div class="row wrapper wrapper-content  animated fadeInRight">
		<div class="col-lg-12">
			<div class="ibox float-e-margins">
				<div class="ibox-content">
					 <form class="form-horizontal" id="hlfAgentDebtRecordForm">
                          <div class="form-group" >
                               <label class="col-sm-2 control-label">代理商:</label>
		                           <div class="col-sm-2">
                                   <select id="agentNo" class="form-control" name="agentNo">
									</select>
								</div>
								<label class="col-sm-2 control-label">所属上级代理商:</label>
		                           <div class="col-sm-2">
                                   <select id="parentAgentNo" class="form-control" name="parentAgentNo">
									</select>
								</div>
								<label class="col-sm-2 control-label">所属一级代理商:</label>
		                           <div class="col-sm-2">
                                   <select id="oneAgentNo" class="form-control" name="oneAgentNo">
									</select>
								</div>
							</div>	
								
							<div class="form-group">
                          	  <label class="col-sm-2 control-label">欢乐返订单号：</label>
							  <div class="col-sm-2"><input type="text" class="form-control" name="orderNo" id="orderNo"></div>
                             
                              <label class="col-sm-2 control-label">日期:</label>
                           		<div class="col-sm-4">
		                            <div class="input-daterange input-group" id="datepicker">
									    <input type="text" class="input-sm form-control" name="date1" value="${date1}"/>
									    <span class="input-group-addon">~</span>
									    <input type="text" class="input-sm form-control" name="date2" value="${date2}" />
									</div>   
								</div>   
                           </div> 	
                       <div class="form-group">
                          	  <label class="col-sm-2 control-label">商户编号：</label>
							  <div class="col-sm-2"><input type="text" class="form-control" name="merchantNo" id="merchantNo"></div>
                           </div> 	
                            <div class="clearfix lastbottom"></div>
                            <div class="form-group" style="margin-bottom:0">
                                    <label class="col-sm-2 control-label aaa"></label>
                                   <!-- <div class="col-sm-12 col-sm-offset-13  "> -->
                                   	   <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                       	<button class="btn btn-success" type="submit"><span class="glyphicon gly-search"></span>查询</button>                             
                                        <sec:authorize access="hasAuthority('hlfAgentDebtDetail:export')">
                                        <button class="btn btn-danger col-sm-offset-14" type="button" onclick="exportExcel()"><span class="glyphicon gly-out"></span>导出</button>
                                        </sec:authorize>
                                        <button class="btn btn-default col-sm-offset-14" type="reset" id="reset"><span class="glyphicon gly-trash"></span>清空</button>
                                   <!-- </div> -->
                             </div>
                           
                    </form>
				</div>
			</div>
		</div>

		<div class="col-lg-12">
			<div class="ibox ">
				<div class="ibox-content">
				<sec:authorize access="hasAuthority('hlfAgentDebtDetail:collectionData')">  
						<span>总计：应扣款：<span id="shouldDebtAmount">0.00</span>元 ,&nbsp;&nbsp;实际扣款：<span  id="debtAmount">0</span>元,&nbsp;&nbsp;
							累计待扣款：<span  id="totalDebtAmount">0.00</span>元
						<br />
						<br />
				</sec:authorize>			
					<div class="jqGrid_wrapper">
						<table id="table_list_hlfAgentDebtRecord"></table>
						<div id="pager_list_hlfAgentDebtRecord"></div>
						<br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
					</div>
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
<script>
    // 去除空格啊
    $('input').blur(function() {
        replaceSpace(this);
    })
    function replaceSpace(obj) {
        obj.value = obj.value.replace(/\s/gi, '')
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
    /*表单提交时的处理*/
    function exportExcel() {
        var data = $("#hlfAgentDebtRecordForm").serialize();
        $.download('${ctx}/activityAccAdjustment/exportHlfAgentDebtDetail.do',data,'post');
    }

    $("#hlfAgentDebtRecordForm").submit(function() {
        $("#table_list_hlfAgentDebtRecord").setGridParam({
            datatype : 'json',
            page : 1
            //Replace the '1' here
        }).trigger("reloadGrid");
        collection();
        return false;
    });

    function getParams(o) {
        var data = $("#hlfAgentDebtRecordForm").serializeArray();
        $.each(data, function() {
            o[this.name] = this.value || '';
        });
        o.agentNo = $("#agentNo").select2("val");
        o.parentAgentNo = $("#parentAgentNo").select2("val");
        o.oneAgentNo = $("#oneAgentNo").select2("val");

    }

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

    $('#parentAgentNo').select2({
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
    
    $('#oneAgentNo').select2({
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

    function collection(){
        $.ajax({
            cache: false,
            type: "POST",
            url:"${ctx}/activityAccAdjustment/findHlfAgentDebtRecordListCollection.do",
            data:$('#hlfAgentDebtRecordForm').serialize(),// formid
            async: false,
            success: function(data) {
                $("#debtAmount").html(data.debtAmount);
                $("#shouldDebtAmount").html(data.shouldDebtAmount);
                $("#totalDebtAmount").html(data.totalDebtAmount);
            }
        });
    }

    $(function(){
        var lastsel;
        var data = $("#hlfAgentDebtRecordForm").serialize();
        // 初始化表格
        $("#table_list_hlfAgentDebtRecord").jqGrid({
			url : "${ctx}/activityAccAdjustment/findHlfAgentDebtRecordList.do",
			datatype : "json",
			mtype : "POST",
			height:"auto",
			autowidth: true,
			shrinkToFit: false,
			autoScroll: false,
			rowNum: 10,
			rowList: [ 10, 20 ],
			colNames : ['应扣款金额（元）','实际扣款金额（元）','累计待扣款金额（元）','扣款代理商编号','扣款代理商名称','所属上级代理商编号','所属上级代理商名称','所属一级代理商编号','所属一级代理商名称','商名编号','欢乐返订单','日期'],
			colModel : [
				{name : 'shouldDebtAmount',index : 'shouldDebtAmount',width : 150,align : "right",formatter : 'number'},
				{name : 'debtAmount',index : 'debtAmount',width : 150,align : "right",formatter : 'number'},
				{name : 'adjustAmount',index : 'adjustAmount',width : 160,align : "right",formatter : 'number'},
				{name : 'agentNo',index : 'agentNo',width : 150,align : "center"},
				{name : 'agentName',index : 'agentName',width : 200,align : "center"},
				{name : 'parentAgentNo',index : 'parentAgentNo',width : 150,align : "center"},
				{name : 'parentAgentName',index : 'parentAgentName',width : 200,align : "center"},
				{name : 'oneAgentNo',index : 'oneAgentNo',width : 150,align : "center"},
				{name : 'oneAgentName',index : 'oneAgentName',width : 200,align : "center"},
				{name : 'merchantNo',index : 'merchantNo',width : 300,align : "left"},
				{name : 'orderNo',index : 'orderNo',width : 300,align : "left"},
				{name : 'debtTime',index : 'debtTime',width : 200,align : "center",formatter : function(val) {return myFormatDate(val,"yyyy-MM-dd hh:mm:ss");}}
			],
			multiselect : false,//支持多项选择
			multiselectWidth : 80,
			multiboxonly: false,
			pager : "#pager_list_hlfAgentDebtRecord",
			viewrecords : true,
			hidegrid : false,
			reloadAfterSubmit: true,
			jsonReader : {
				root : "result",
				total : "totalPages",
				page : "pageNo",
				pageSize : "pageSize",
				records : "totalCount",
				repeatitems : false
			},
			prmNames : {
				page : "pageNo",
				rows : "pageSize"
			},
			serializeGridData : function(postData) {
				getParams(postData);
				return postData;
			},
			gridComplete : function() { //在此事件中循环为每一行添加修改和删除链接
				//hideCheckBox();
			}
		});
        collection();
        jQuery("#table_list_hlfAgentDebtRecord").jqGrid('setFrozenColumns');

        $(window).bind('resize',function() {
			var width = $('.jqGrid_wrapper').width();
			$('#table_list_hlfAgentDebtRecord').setGridWidth(width);
		});

        $("#reset").on("click", function () {
            $("#agentNo").empty().trigger("change");
            $("#parentAgentNo").empty().trigger("change");
            $("#oneAgentNo").empty().trigger("change");
        });
        $('.input-daterange').datepicker({
            format: "yyyy-mm-dd",
            language: "zh-CN",
            todayHighlight: true,
            autoclose: true,
            clearBtn: true
        });


	});

</script>
</title>
</html>  
      