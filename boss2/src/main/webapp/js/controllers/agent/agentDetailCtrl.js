angular.module('inspinia').controller("agentDetailCtrl", function($scope, $http, $state, $stateParams,uiGridConstants,i18nService,$log,$uibModal,$compile) {
	var self = this;
	$scope.merRateStr='[{text:"每笔固定金额",value:"1"},{text:"每笔扣率",value:"2"},{text:"每笔扣率带保底封顶",value:"3"},{text:"每笔扣率+固定金额",value:"4"},{text:"单笔阶梯扣率",value:"5"}]';
	$scope.statusStr = '[{text:"正常",value:1},{text:"关闭",value:0}]';
	$scope.type = [ {text : '个人',value :  1 }, {text : '个体商户',value :  2 }, {text : '企业商户',value :  3} ];
	$scope.checkStatus=[{text:"待审核",value:0},{text:"审核通过",value:1},{text:'审核不通过',value:2}];
	$scope.effectiveStatus = [{text:"否",value:0},{text:"是",value:1},{text:'否',value:2}];
	$scope.agent={};
	$scope.teamType=[];
	$scope.dpData=[];
	$scope.shareData=[];
	$scope.rateData=[];
	$scope.quotaData=[];
	$scope.teamId = $stateParams.teamId;
	$http.get('teamInfo/queryTeamName.do').success(function(msg){
		for(var i=0;i<msg.teamInfo.length;i++){
			$scope.teamType.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
		}
		//2.2.3-1$scope.agent.teamId=msg.teamInfo[0].teamId;
	});
	$http.post('agentInfo/queryAgentInfoDetail',{"agentNo":$stateParams.id,"teamId":$stateParams.teamId}).success(function(msg){
		$scope.agent = msg.agentInfo;
		$scope.dpData = msg.agentProducts;
		$scope.shareData = msg.agentShare;
		$scope.rateData = msg.agentRate;
		$scope.quotaData = msg.agentQuota;
		$scope.happyBackGrid.data = msg.hbTypes;
		if(msg.hbTypes==null||msg.hbTypes.length==0){
            $("#happyBack").hide();
		}
	}).error(function(){
	});

	var rowList={};
    $scope.bpList = {
        data: 'dpData',
        columnDefs: [
             {field: 'key1',displayName: '业务产品ID',width: 150},
             {field: 'key3',displayName: '业务产品名称',width: 150},
             {field: 'key5',displayName: '类型',width: 150,cellFilter:"formatDropping:"+angular.toJson($scope.type)},
             {field: 'key2',displayName: '状态',width: 150,cellFilter:"formatDropping:"+$scope.statusStr}
        ],
		onRegisterApi : function(gridApi) {
			$scope.gridApiProduct = gridApi;
		}
    };
    $scope.frStr='[{text:"每笔固定收益金额",value:"1"},{text:"每笔收益率",value:"2"},{text:"每笔收益率带保底封顶",value:"3"},{text:"每笔收益率+每笔固定收益金额",value:"4"},'+
		'{text:"成本价+固定分润比",value:"5"},{text:"成本价+阶梯分润比",value:"6"}]';
	$scope.shareList = {
		data: 'shareData',
		columnDefs: [
		    {field: 'checkStatus',displayName:'审核状态',width:135,cellFilter:"formatDropping:"+angular.toJson($scope.checkStatus)},
		    {field: 'uncheckStatus',displayName: '未生效审核状态',width: 180},
		    {field: 'bpName',displayName: '业务产品名称',width: 150},
		    {field: 'serviceName',displayName: '服务名称',width: 150},
		    {field: 'serviceType',displayName: '服务种类',width: 150,cellTemplate:
				'<div class="lh30" ng-show="row.entity.serviceType!=10000&&row.entity.serviceType!=10001"><span ng-bind="row.entity.serviceType | serviceTypeFilter"/></div>'
				+'<div class="lh30" ng-show="row.entity.serviceType==10000||row.entity.serviceType==10001"><span ng-bind="row.entity.serviceType2 | serviceTypeFilter"/>-提现</div>'
				},
            {field: 'cardType',displayName: '银行卡种类',width: 150,cellFilter:"formatDropping:"+$scope.cardTypeStr},
            {field: 'holidaysMark',displayName: '节假日标志',width: 150,cellFilter:"formatDropping:"+$scope.holidaysStr},
            {field: 'profitType',displayName: '分润方式',width: 200,cellFilter:"formatDropping:"+$scope.frStr},
            {field: 'income',displayName: '代理商收益',width: 150},
            {field: 'cost',displayName: '代理商成本',width: 150},
            {field: 'shareProfitPercent',displayName: '代理商固定分润百分比',width: 180,cellTemplate:'<span class="checkbox" ng-show="row.entity.shareProfitPercent!=null">'+
            	'{{row.entity.shareProfitPercent}}%</span>'},
            {field: 'ladderRate',displayName: '阶梯分润比例',width: 300},
            {field: 'efficientDate',displayName: '生效日期',width: 200,cellFilter:'date:"yyyy-MM-dd"'},
            {field: 'checkStatus',displayName: '当前是否生效',width: 180,cellFilter:"formatDropping:" + angular.toJson($scope.effectiveStatus)}
		]
	};
	
	var rateRowsList={};
	$scope.rateList = {
		data: 'rateData',
		columnDefs: [
		     {field: 'isGlobal',displayName: '与公司管控费率相同',width: 180,
		    	 cellTemplate:
		    		 '<input type="checkbox" disabled="true" ng-model="row.entity.isGlobal" ng-true-value="1" ng-false-value="0"/>'
		     },
             {field: 'serviceName',displayName: '服务名称',width: 150},
             {field: 'cardType',displayName: '银行卡种类',width: 150,cellFilter:"formatDropping:"+$scope.cardTypeStr},
             {field: 'holidaysMark',displayName: '节假日标志',width: 150,cellFilter:"formatDropping:"+$scope.holidaysStr},
		     {field: 'rateType',displayName: '费率方式',width: 200,cellFilter:'formatDropping:'+$scope.merRateStr},
		     {field: 'merRate',displayName: '商户费率',width: 300}
		],
		onRegisterApi: function(gridApi) {                //选中行配置
	        $scope.rateGridApi = gridApi;
		}
	};
	
	$scope.quotaList = {
		data: 'quotaData',
		columnDefs: [
            {field: 'isGlobal',displayName: '与公司管控费率相同',width: 180,
            	 cellTemplate:
		    		 '<input type="checkbox" disabled="true" ng-model="row.entity.isGlobal" ng-true-value="1" ng-false-value="0"/>'
            },
            {field: 'serviceName',displayName: '服务名称',width: 150},
            {field: 'cardType',displayName: '银行卡种类',width: 150,cellFilter:"formatDropping:"+$scope.cardTypeStr},
            {field: 'holidaysMark',displayName: '节假日标志',width: 150,cellFilter:"formatDropping:"+$scope.holidaysStr},
            {field: 'singleDayAmount',displayName: '单日最大交易金额',width: 180,pinnable: false,sortable: false,editable:true,
            },
            {field: 'singleMinAmount',displayName: '单笔最小交易额',width: 180,pinnable: false,sortable: false,editable:true,
            },
            {field: 'singleCountAmount',displayName: '单笔最大交易额',width: 180,pinnable: false,sortable: false,editable:true,
            },
            {field: 'singleDaycardAmount',displayName: '单日单卡最大交易额',width: 200,pinnable: false,sortable: false,editable:true,
            },
            {field: 'singleDaycardCount',displayName: '单日单卡最大交易笔数',width: 250,pinnable: false,sortable: false,editable:true,
            }
		],
		onRegisterApi: function(gridApi) {                //选中行配置
	        $scope.quotaGridApi = gridApi;
    	}
	};

	$scope.happyBackGrid = {
		columnDefs: [
			{field: 'activityTypeNo',displayName: '欢乐返子类型编号'},
			{field: 'activityTypeName',displayName: '欢乐返子类型名称'},
			{field: 'functionName',displayName: '欢乐返类型'},
			{field: 'transAmount',displayName: '交易金额',cellTemplate:'<div class="lh30">{{row.entity.transAmount}}元<div/>'},
			{field: 'cashBackAmount',displayName: '返现金额',cellTemplate:'<div class="lh30">{{row.entity.cashBackAmount}}元<div/>'},
			{field: 'taxRate',displayName: '返现比例',cellTemplate:'<div class="lh30">{{row.entity.taxRate*100}}%<div/>'},
            {field: 'repeatRegisterAmount',displayName: '重复注册返现金额',cellTemplate:'<div class="lh30" ng-show="row.entity.repeatRegisterAmount!=null">{{row.entity.repeatRegisterAmount}}元<div/>'},
            {field: 'repeatRegisterRatio',displayName: '重复注册返现比例',cellTemplate:'<div class="lh30">{{row.entity.repeatRegisterRatio*100}}%<div/>'},
            {field: 'emptyAmount',displayName: '首次注册不满扣N值',cellTemplate:'<div class="lh30" ng-show="row.entity.emptyAmount!=null">{{row.entity.emptyAmount}}元<div/>'},
            {field: 'fullAmount',displayName: '首次注册满奖M值',cellTemplate:'<div class="lh30" ng-show="row.entity.fullAmount!=null">{{row.entity.fullAmount}}元<div/>'},
            {field: 'repeatEmptyAmount',displayName: '重复注册不满扣N值',cellTemplate:'<div class="lh30" ng-show="row.entity.repeatEmptyAmount!=null">{{row.entity.repeatEmptyAmount}}元<div/>'},
            {field: 'repeatFullAmount',displayName: '重复注册满奖M值',cellTemplate:'<div class="lh30" ng-show="row.entity.repeatFullAmount!=null">{{row.entity.repeatFullAmount}}元<div/>'}
		]
	};


	/**
	 * 获取敏感数据
	 */
	$scope.dataSta=true;
	$scope.getDataProcessing = function () {
		if($scope.dataSta){
			$http.post("agentInfo/getDataProcessing",{"agentNo":$stateParams.id,"teamId":$stateParams.teamId})
				.success(function(data) {
					if(data.status){
						$scope.agent.mobilephone = data.agentInfo.mobilephone;
						$scope.agent.idCardNo = data.agentInfo.idCardNo;
						$scope.dataSta=false;
					}else{
						$scope.notice(data.msg);
					}
				});
		}
	};

}).filter('compareDateFilter', function () {
	return function (value) {
		var date = new Date().getTime();
		if(date < value){
			return "否";
		} 
		if(date >= value ){
			return "是";
		}
	}
});