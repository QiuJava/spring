/**
 * 交易查询
 * 页面上有个错误需要解决，否则看不到angular的效果,好像是页面不能响应angular指令
 */
angular.module('inspinia').controller('queryTradeByMerchantCtrl',function($scope,$rootScope,$http,$state,$stateParams,$compile,$uibModal,$log,i18nService,SweetAlert){

	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	// ========================= start 配置查询条件 ==========================
    var oneDaySecond = 24 * 3600 * 1000;
    var maxDiffDay = 93;
    // 查询条件的默认值
    var defatultInfo = {
        orderType: '',
		agentNo: $scope.entityId,
		transStatus:"SUCCESS",
		merchantNo:"",
		mobilephone:"",
		bool:"1",
		settlementMethod:"",
		settleStatus:"",
        terType:"",
		payMethod:"",
		smoney:"",
		emoney:"",
		activityType:"",
		productType:"",
//		sdate:moment(new Date().getTime() - oneDaySecond * (maxDiffDay - 1)).format('YYYY-MM-DD'+' 00:00:00'),
		sdate:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
		edate:moment(new Date().getTime()).format('YYYY-MM-DD') + " 23:59:59",
		startCreateTime:"",
		endCreateTime:"",
		minTransAmount:"",
		maxTransAmount:""
        // sdate:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
		// edate:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
    };
    // 所属下级
    $scope.bools = [{text:'否',value:'0'},{text:'是',value:'1'}];
    $scope.info = angular.extend({}, defatultInfo);
    $scope.orderTypes = [];
    if ($rootScope.agentType == 11) {
        $scope.orderTypes = [{value:0,text:"普通订单"},{value:3,text:"欢乐返"},{value:4,text:"充值返"},
            {value:5,text:"云闪付"},{value:6,text:"购买鼓励金"},{value:7,text:"境外卡交易"}];
    }else{
        for (var i = 0; i < initData.ORDER_TYPE.length; i ++){
            $scope.orderTypes.push({
                text: initData.ORDER_TYPE[i].text,
                value: initData.ORDER_TYPE[i].value + ""
            })
        }
    }

    // 交易状态
    $scope.tradeStatus = [{text:"全部",value:""}];
    // 代理商名称
    $scope.agent=[];
    // 交易方式
	$scope.payMethods=[{text:"全部",value:""}];
    // 结算周期
    $scope.settlementMethods=[{text:"全部",value:""},{text:"T0",value:"0"},{text:"T1",value:"1"}];
    // 机具活动类型
    $scope.activityTypes=[{text:"全部",value:""}];
	// =========================  end 配置查询条件  ==========================

	// ======================== start 配置查询条件的业务产品 =================
    $http.get('businessProductDefine/selectAllInfo.do')
        .success(function(largeLoad) {
            if(!largeLoad)
                return;
            $scope.productTypes = largeLoad;
            $scope.productTypes.splice(0,0,{bpId:"",bpName:"全部"});
        });
	// ======================== end 配置查询条件的业务产品 =================

    if ($rootScope.agentType == 11) {
        $scope.activityTypes = [{value:"",text:"全部"},{value:"12",text:"注册返300"},{value:"7",text:"欢乐返"}];
    }else{
        for(var i=0; i<$scope.activityTypess.length; i++){
            $scope.activityTypes.push({value:$scope.activityTypess[i].sys_value,text:$scope.activityTypess[i].sys_name});
        }

    }

    // ======================== start 配置查询条件的硬件产品种类 =================
    // $scope.termianlTypes=[];
    // $http.get('hardwareProduct/selectAllInfo.do')
    //     .success(function(result){
    //         if(!result)
    //             return;
    //         $scope.termianlTypes=result;
    //         $scope.termianlTypes.splice(0,0,{hpId:"",typeName:"全部"});
    //     });
    // ======================== end   配置查询条件的硬件产品种类 =================

    // ======================== start 配置查询条件的代理商 =================
    $http.post("agentInfo/selectAllInfo")
        .success(function(msg){
            for(var i=0; i<msg.length; i++){
                $scope.agent.push({value:msg[i].agentNo,text:msg[i].agentName});
            }
        });
    // ======================== end 配置查询条件的代理商 =================

	// ======================== start 配置查询条件的交易状态,交易方式 =================
    $http.post("transInfoAction/getBoxInfo")
        .success(function(msg){
            if(msg.bols){
                //交易状态
                for(var i=0; i<msg.tranStatus.length; i++){
                    if (msg.tranStatus[i].sysValue !== 'INIT') {
                        $scope.tradeStatus.push({value: msg.tranStatus[i].sysValue, text: msg.tranStatus[i].sysName});
                    }
                }
                //交易方式
                for(var i=0; i<msg.payMethod.length; i++){
                    $scope.payMethods.push({value:msg.payMethod[i].sysValue,text:msg.payMethod[i].sysName});
                }
            }else{
                $scope.notice("查询出错");
            }
        });
    // ======================== end 配置查询条件的交易状态,交易方式 =================

    // ======================== start 配置三个按钮的事件 =================
	// 清空按钮
    $scope.clear=function(){
        $scope.info = angular.extend({}, defatultInfo);
    };
    // 查询按钮
    $scope.queryTrading = false;
    $scope.query=function(){
        if($scope.queryTrading){
            $scope.notice("正在查询数据中,请稍后...");
            return;
        }
        if(!validSearchParam()){
            return;
        }
        $scope.queryTrading = true;
        $http({
            url: 'transInfoAction/getAllInfoByMerchant' + "?pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            method: 'POST',
            data: $scope.info
        }).success(function(msg){
            if(msg.success){
                $scope.gridOptions.data = msg.data || [];
                $scope.gridOptions.totalItems = msg.count;
            }else{
                $scope.notice(msg.message);
            }
            $scope.queryTrading = false;
        }).error(function () {
            $scope.queryTrading = false;
            $scope.notice("服务器异常,请稍后再试");
        });
    };
    
    //是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0

	keyChange=function(){
		if ($scope.info.merchantNo || $scope.info.mobilephone || $scope.info.orderNo
				 || $scope.info.acqReferenceNo || $scope.info.accountNo) {
			isVerifyTime = 0;
		} else {
			isVerifyTime = 1;
		}
	}
	setBeginTime=function(setTime){
		$scope.info.sdate = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	setEndTime=function(setTime){
		$scope.info.edate = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}
	
    function validSearchParam(){
        var sdateMoment = moment($scope.info.sdate, 'YYYY-MM-DD HH:mm:ss');
        var edateMoment = moment($scope.info.edate, 'YYYY-MM-DD HH:mm:ss');
        if (!sdateMoment.isValid() || !edateMoment.isValid()){
            $scope.notice("请选择交易时间");
            return false;
        }
        var diffTime = edateMoment.diff(sdateMoment);
        if (diffTime < 0){
            $scope.notice("起始时间不能大于结束时间");
            return false;
        }else if(diffTime > oneDaySecond * maxDiffDay){
            $scope.notice("查询范围不能超过三个月");
            return false;
        }
        if($scope.info.sdate>$scope.info.edate){
            $scope.notice("起始时间不能大于结束时间");
            return false;
        }
        return true;
    }
    // 导出事件
    $scope.exportTransing = false;
    $scope.exportInfo=function(){
        if(!validSearchParam()){
            return;
        }
        if($scope.exportTransing){
            $scope.notice("正在导出数据,请不要频繁点击导出按钮.");
            return;
        }
        SweetAlert.swal({
                title: "确认导出？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    $scope.exportTransing = true;
                    setTimeout(function () {
                        $scope.exportTransing = false;
                    }, 5000);
                    location.href="transInfoAction/exportTransInfoByMerchant?"+$.param($scope.info);
                }
            });
    };
    // ======================== end   配置三个按钮的事件 =================

	// ======================== start   配置表格数据 =================
    $scope.columnDefs = [//表格数据
                    { field: 'merchantName',displayName:'商户简称',width:150 },
                    { field: 'belongUserCode',displayName:'所属盟主编号',width:180 },
                    { field: 'merchantNo',displayName:'商户编号',width:180 },
                    { field: 'agentNo',displayName:'所属代理商编号',width:180 },
                    { field: 'agentName',displayName:'所属代理商名称',width:180 },
                    { field: 'parentAgentNo',displayName:'上级代理商编号',width:180 },
                    // { field: 'parentAgentName',displayName:'上级代理商名称',width:180 },
                    { field: 'directlyAgentNo',displayName:'直属下级代理商编号',width:180 },
                    { field: 'directlyAgentName',displayName:'直属下级代理商名称',width:180 },
                    { field: 'mobilephone',displayName:'商户手机号',width:180 },
                    { field: 'transAmount',displayName:'金额（元）',cellFilter:"currency:''",width:180 },
        	        { field: 'ct',displayName:'商户创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:150}
                ];
    if($rootScope.agentType != 11) {
    	$scope.columnDefs.splice(1, 1);
	}
    $scope.gridOptions={                           	//配置表格
        paginationPageSize:10,                  	//分页数量
        paginationPageSizes: [10, 20,50,100],	  	//切换每页记录数
        useExternalPagination: true,               	//分页数量
        columnDefs:$scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
	// ======================== end   配置表格数据 =================
});
