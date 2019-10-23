/**
 * 交易查询
 * 页面上有个错误需要解决，否则看不到angular的效果,好像是页面不能响应angular指令
 */
angular.module('inspinia').controller('queryTradeCtrl',function($scope,$rootScope,$http,$state,$stateParams,$compile,$uibModal,$log,i18nService,SweetAlert){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	//数据源
	$scope.bools = [{text:'否',value:'0'},{text:'是',value:'1'}];
	$scope.recommendedSources = [{text:'是',value:'1'},{text:'否',value:'0'}];
	$scope.recommendedSources1 = [{text:'否',value:'0'},{text:'是',value:'1'},{text:'否',value:'2'},{text:'否',value:'3'}];
	$scope.tradeStatus = [{text:"全部",value:""}];
	
	$scope.agent=[];
	$scope.payMethods=[{text:"全部",value:""}];
	$scope.serviceTypes=[{text:"全部",value:""}];
	//20170221TGH
	$scope.settlementMethods=[{text:"全部",value:""},{text:"T0",value:"0"},{text:"T1",value:"1"}];
	$scope.info={merTeamId:"-1",orderType:"",agentNo:$rootScope.entityId,orderNo:"",transStatus:"SUCCESS",merchantNo:"",mobilephone:"",bool:'1',settlementMethod:"",settleStatus:"",
			terType:"",payMethod:"",serviceType:"",accountNo:"",smoney:"",emoney:"",activityType:"",productType:"-1",startCreateTime:"",endCreateTime:"",minTransAmount:"",maxTransAmount:"",
			sdate:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),edate:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'};
    $scope.zmoney = 0;
    $scope.znum = 0;

	//业务产品
 	$http.get('businessProductDefine/selectAllInfo.do')
 	.success(function(largeLoad) {
 		if(!largeLoad)
 			return;
 		$scope.productTypes=largeLoad;
 		$scope.productTypes.splice(0,0,{bpId:"-1",bpName:"全部"});
 	});
    $scope.activityTypes=[{text:"全部",value:""}];

    // 当前登录代理商业务范围是否有设置
	$scope.showSelect = true;
    $scope.initTeamId = "-1";
    $scope.selectMerTeams = function () {
        $http.get('teamInfo/getTeams')
            .success(function(data) {
                if(!data){
                    return;
                }
                $scope.merTeams=data;
                $scope.merTeams.splice(0,0,{value:"-1",text:"全部"});

                $http.get('userAction/getAccessTeamId').success(function (result) {
                    if(result.status){
                        $scope.info.merTeamId = result.accessTeamId;
                        $scope.initTeamId = result.accessTeamId;
                    }else {
                        $scope.showSelect = false;
					}
                });
            });
    }
    $scope.selectMerTeams();

    // {text: "POS刷卡-标准类", value: 4}	POS刷卡
    // {text: "NFC收款", value: 6}
    // {text: "POS刷卡-优惠类", value: 9}	VIP收款
    // {text: "账户提现", value: 10000}
    // {text: "关联提现服务", value: 10001}
    // {text: "支付宝扫码支付", value: 10002}
    // {text: "微信扫码支付", value: 10003}
    // {text: "快捷支付", value: 10004}
    // {text: "微信主扫", value: 10005}
    // {text: "支付宝主扫", value: 10006}
    // {text: "云闪付", value: 10007}
    // {text: "快捷支付2(0.38%+3)", value: 10008}
    // {text: "快捷支付3(0.55%)", value: 10009}
    if ($rootScope.agentType == 11) {
    	$scope.serviceTypes = [{value:"",text:"全部"},{value:"4",text:"POS刷卡"},{value:"10002",text:"支付宝扫码支付"},{value:"10003",text:"微信扫码支付"},
    	                       {value:"10004",text:"快捷支付"},{value:"10010",text:"银联二维码"}];
	}else{
		for(var i=0; i< initData.SERVICE_TYPE.length; i++){
			var value = initData.SERVICE_TYPE[i].value;
			var text = initData.SERVICE_TYPE[i].text;
			// 跟钟辉确认的需求
			// 9->POS刷卡-优惠类
			// 4-》POS刷卡-标准类
			// POS刷卡-标准类（用“POS刷卡”替代）、POS刷卡-优惠类（用“VIP收款”替代）
			if(value == '4'){
				text = "POS刷卡"
			}else if(value == '9'){
				text = "VIP收款";
			}
			if ($scope.oemType === "ZHZFPAY"){
				if (value == '6' || value == '9' || value == '10007' || value == '10008'){
					continue;
				}
			}
			$scope.serviceTypes.push({value:value,text:text});
		}
	}
    if ($rootScope.agentType == 11) {
    	$scope.activityTypes = [{value:"",text:"全部"},{value:"12",text:"注册返300"},{value:"7",text:"欢乐返"}];
    }else{
	    for(var i=0; i<$scope.activityTypess.length; i++){
			$scope.activityTypes.push({value:$scope.activityTypess[i].sys_value,text:$scope.activityTypess[i].sys_name});
	    }
    }
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

     //机具类型
	// $scope.termianlTypes=[];
	//获取硬件产品列表
	// $http.get('hardwareProduct/selectAllInfo.do')
	// .success(function(result){
	// 	if(!result)
	// 		return;
	// 	$scope.termianlTypes=result;
	// 	$scope.termianlTypes.splice(0,0,{hpId:"",typeName:"全部"});
	// })
	//代理商
	$http.post("agentInfo/selectAllInfo").success(function(msg){
  	   	for(var i=0; i<msg.length; i++){
  	   		$scope.agent.push({value:msg[i].agentNo,text:msg[i].agentName});
  	   	}
  	});
	
	//清空
	$scope.clear=function(){
		$scope.info={merTeamId:$scope.initTeamId,productType:"-1",orderType:"",agentNo:$rootScope.entityId,orderNo:"",transStatus:"SUCCESS",merchantNo:"",mobilephone:"",bool:'1',settlementMethod:"",
				terType:"",payMethod:"",serviceType:"",accountNo:"",smoney:"",emoney:"",
				sdate:moment(new Date().getTime()).format('YYYY-MM-DD')+' 00:00:00',
				edate:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
				activityType:"",startCreateTime:"",endCreateTime:"",minTransAmount:"",maxTransAmount:"", serviceType: ''};
	}
	
	$http.post("transInfoAction/getBoxInfo")
	 .success(function(msg){
		 if(msg.bols){
			//交易状态
			for(var i=0; i<msg.tranStatus.length; i++){
				if (msg.tranStatus[i].sysValue !== 'INIT'){
                    $scope.tradeStatus.push({value:msg.tranStatus[i].sysValue,text:msg.tranStatus[i].sysName});
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

	$scope.queryTrading = false;
    $scope.query=function(){
    	if($scope.queryTrading){
    		$scope.notice("正在查询数据中,请稍后...");
    		return;
		}
        if(!($scope.info.sdate && $scope.info.edate) && (!$scope.info.merchantNo && !$scope.info.mobilephone)){
            $scope.notice("查询条件交易时间不能为空，请选择要查询的交易时间！");
            return;
        }
        if ((!$scope.info.merchantNo && !$scope.info.mobilephone) &&
			(new Date($scope.info.edate).getTime() - new Date($scope.info.sdate).getTime() > 31*24*3600*1000)) {
			$scope.notice("查询范围不能超过一个月");
			return;
		}
        if((!$scope.info.merchantNo && !$scope.info.mobilephone) && $scope.info.sdate > $scope.info.edate){
            $scope.notice("起始时间不能大于结束时间");
            return;
        }
        $scope.queryTrading = true;
    	$http({
			url: "transInfoAction/getAllInfo?pageNo=" + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
			method: 'POST',
			data: $scope.info
		}).success(function (msg) {
            $scope.queryTrading = false;
            if(msg.success){
                $scope.zmoney = msg.data.numAndMoney || "0";
                $scope.gridOptions.data = msg.data.transData;
                $scope.gridOptions.totalItems = msg.count;
                $scope.znum = msg.count || "0";
            }else{
                $scope.notice(msg.message || "查询出错");
            }
        }).error(function () {
            $scope.queryTrading = false;
			$scope.notice("服务器异常,请稍后再试");
        });
    };

    $scope.columnDefs = [//表格数据
     					{ field: 'orderNo',displayName:'订单号',width:170},
     					{ field: 'settlementMethod',displayName:'结算周期',width:120,cellFilter:"settlementMethods"},
     					{ field: 'merchantName',displayName:'商户简称',width:150 },
     					{ field: 'belongUserCode',displayName:'所属盟主编号',width:180 },
     					{ field: 'merchantNo',displayName:'商户编号',width:180 },
     					{ field: 'sn',displayName:'机具编号',width:180 },
     					{ field: 'merGroup',displayName:'商户组织',width:180 },
     					{ field: 'bpName',displayName:'业务产品',width:150 },
     					{ field: 'recommendedSource',displayName:'超级推商户',width:150,cellFilter:"formatDropping:" + angular.toJson($scope.recommendedSources1) },
     		            { field: 'serviceType',displayName:'收款类型',width:150, cellFilter:"formatDropping:" + angular.toJson($scope.serviceTypes)},
     					{ field: 'transType1',displayName:'交易类型',width:150,cellFilter:"formatDropping:[{text:'消费',value:'PURCHASE'},{text:'冲正',value:'REVERSED'},"
     						+"{text:'消费撤销',value:'PURCHASE_VOID'},{text:'预授权',value:'PRE_AUTH'},{text:'预授权撤销',value:'PRE_AUTH_VOID'},"
     						+"{text:'预授权完成',value:'PRE_AUTH_COMPLETA'},{text:'预授权完成撤销',value:'PRE_AUTH_COMPLETE_VOID'},{text:'退货',value:'PURCHASE_REFUND'},"
     						+"{text:'查余额',value:'BALANCE_QUERY'},{text:'转账',value:'TRANSFER_ACCOUNTS'}]" },
     					{ field: 'orderType',displayName:'活动类型',width:150,cellFilter:"formatDropping:" + angular.toJson($scope.orderTypes)},
     					{ field: 'cardType1',displayName:'卡种',width:150,cellFilter:"formatDropping:[{text:'不限',value:'0'},{text:'贷记卡',value:'1'},{text:'借记卡',value:'2'}]"},
     					{ field: 'accountNo',displayName:'交易卡号',width:180 },
     					{ field: 'agentName',displayName:'代理商名称',width:180 },
     					{ field: 'directlyAgentNo',displayName:'直属下级代理商编号',width:180 },
     					{ field: 'directlyAgentName',displayName:'直属下级代理商名称',width:180 },
     					{ field: 'mobilephone',displayName:'商户手机号',width:180 },
     					{ field: 'transAmount',displayName:'金额（元）',cellFilter:"currency:''",width:180 },
     					{ field: 'transStatus1',displayName:'交易状态',width:150,cellFilter:"formatDropping:[{text:'成功',value:'SUCCESS'},{text:'失败',value:'FAILED'},"
     						+"{text:'初始化',value:'INIT'},{text:'已冲正',value:'REVERSED'},{text:'已撤销',value:'REVOKED'},{text:'已结算',value:'SETTLE'},"
     						+"{text:'超额',value:'OVERLIMIT'},{text:'已退款',value:'REFUND'},{text:'已完成',value:'COMPLETE'},{text:'关闭',value:'CLOSED'}]"},
     		            { field: 'settleStatus1',displayName:'结算状态',width:150,cellFilter:"formatDropping:" + angular.toJson($scope.settleStatusAll)},
     					{ field: 'transTime',displayName:'交易时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:150},
     					// { field: 'num1',displayName:'本级分润',cellFilter: "currency:''",width:100},
     					// { field: 'num2',displayName:'下级分润',cellFilter: "currency:''",width:100},
     					{ field: 'activityType',displayName:'机具活动类型',width:150 ,
     						cellFilter:"terminalFormatDropping:"+angular.toJson($scope.activityTypes)
     					},
     					// { field: 'recommendedSource',displayName:'是否为微创业',width:180 ,
     					// 	cellFilter:"formatDropping:"+'[{text:"否",value:null},{text:"是",value:1},{text:"否",value:0},{text:"否",value:""}]'
     					// },
     					// { field: 'deductionFee',displayName:'是否抵扣鼓励金',width:150,cellFilter:"formatDropping:"+'[{text:"是",value:1},{text:"否",value:0}]'},
     					{ field: 'ct',displayName:'商户创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:150},
     					{ field: 'id',displayName:'操作',pinnedRight:true,width:200,
     						cellTemplate:'<a class="lh30"  ng-click="grid.appScope.tradeDetailInfo(row.entity.orderNo)">详情</a>'
     					}
     				];
    if($rootScope.agentType != 11) {
    	$scope.columnDefs.splice(3, 1);
	}
	$scope.gridOptions={                           //配置表格
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
		useExternalPagination: true,                //分页数量
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
//	$scope.dtr= true;
//	$scope.agt_ch = function(){
//		if($scope.info.agentNo==""){
//			$scope.dtr= true;
//		}else{
//			$scope.dtr= false;
//		}
//	}
	
	//详情/解冻
	$scope.tradeDetailInfo=function(id){
		var modalScope = $scope.$new(); 	//相当于$scope
		modalScope.id=id;
		modalScope.transStatuss=$scope.transStatuss;
		modalScope.serviceTypes=$scope.serviceTypes;
		modalScope.transTypes=$scope.transTypes;
		var modalInstance = $uibModal.open({
			templateUrl : 'views/trade/tradeQueryDetail.html',  //指向上面创建的视图
			controller : 'tradeDetailModalCtr',// 初始化模态范围
			scope:modalScope,
			size:'lg'
		});
		modalScope.modalInstance=modalInstance;
		modalInstance.result.then(function(selectedItem){
		},function(){
			$log.info('取消: ' + new Date())
		})
	};
	
	//导出信息//打开导出终端模板
    $scope.exportTransing = false;
	$scope.exportInfo=function(){
		if((!$scope.info.merchantNo && !$scope.info.mobilephone) && !($scope.info.sdate && $scope.info.edate)){
            $scope.notice("查询条件交易时间不能为空，请选择要查询的交易时间！");
            return;
		}
		if((!$scope.info.merchantNo && !$scope.info.mobilephone) && $scope.info.sdate>$scope.info.edate){
			$scope.notice("起始时间不能大于结束时间");
			return;
		}
        if($scope.exportTransing){
            $scope.notice("正在导出数据,请不要频繁点击导出按钮.");
            return;
        }
        if ((!$scope.info.merchantNo && !$scope.info.mobilephone) &&
			(new Date($scope.info.edate).getTime() - new Date($scope.info.sdate).getTime() > 31*24*3600*1000)) {
			$scope.notice("导出范围不能超过一个月");
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
				//location.href="transInfoAction/exportTransInfo?info="+encodeURI(angular.toJson($scope.info));
				$scope.exportInfoClick("transInfoAction/exportTransInfo.do",{"info":angular.toJson($scope.info)});
			}
		});
	}
}).filter('settlementMethods', function () {
	return function (value) {
		switch(value) {
			case "0" :
				return "T0";
				break;
			case "1" :
				return "T1";
				break;
		}
	}
});
angular.module('inspinia').controller('tradeDetailModalCtr',function($scope,$http,$state,$stateParams,$compile,$uibModal,$log,i18nService){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.solutionModalClose=function(){
		 $scope.modalInstance.dismiss();
	 }
	 $scope.solutionModalOk=function(){
		 $scope.modalInstance.close($scope);
	 }
	 $scope.settlementStr=[{text:'已结算',value:"1"},{text:'未结算',value:"0"},{text:'结算中',value:"2"},{text:'结算失败',value:"3"}]
	 $scope.cardTypes=[];
	//查询总笔数和总金额
		$http.post("transInfoAction/getBoxInfo")
		 .success(function(msg){
			 if(msg.bols){
				for(var i=0; i<msg.cardType.length; i++){
		   	   		$scope.cardTypes.push({value:msg.cardType[i].sysValue,text:msg.cardType[i].sysName});
		   	   	}
			 }else{
				$scope.notice("查询出错");
			 }
		});
	 
	 $http.get('transInfoAction/queryInfoDetail?id='+$scope.id)
	    .success(function(largeLoad) {
	    	if(!largeLoad.bols){
	    		$scope.notice(largeLoad.msg);
	    	}else{
	    		$scope.infoDetail=largeLoad.tt;
	    		$scope.pcb=largeLoad.pcb;
	    		$scope.pcb1=largeLoad.pcb1;
	    		$scope.aa1=largeLoad.aa1;
	    		$scope.aa2=largeLoad.aa2;
	    	}
	 });
});

