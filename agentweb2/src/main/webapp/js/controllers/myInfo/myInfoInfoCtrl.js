/**
 * 我的信息-我的信息 控制器
 * @author xyf1
 */

angular.module('inspinia',['uiSwitch']).controller("myInfoInfoCtrl", function($scope, $http,SweetAlert, $state, $stateParams,uiGridConstants,i18nService,$log,$uibModal,$compile) {
    i18nService.setCurrentLang('zh-cn');
    var self = this;
	$scope.merRateStr='[{text:"每笔固定金额",value:"1"},{text:"每笔扣率",value:"2"},{text:"每笔扣率带保底封顶",value:"3"},{text:"每笔扣率+固定金额",value:"4"},{text:"单笔阶梯扣率",value:"5"}]';
//	$scope.level = [{text:'1',value:1},{text:'2',value:2},{text:'3',value:3},{text:'4',value:4},{text:'5',value:5},{text:'6',value:6},{text:'7',value:7},
//	                {text:'8',value:8}];
	$scope.statusStr = '[{text:"正常",value:1},{text:"关闭",value:0}]';
	//活动类型
    if ($scope.oemType === 'ZHZFPAY'){
        $scope.subjectTypes = [{text:'全部',value:''},{text:'欢乐返',value:'009'}];
    }else{
        $scope.subjectTypes = [{text:'全部',value:''},{text:'欢乐返-循环送',value:'008'},{text:'欢乐返',value:'009'}];
	}
	$scope.agent={};
	$scope.teamType=[];
	$scope.dpData=[];
	$scope.shareData=[];
	$scope.rateData=[];
	$scope.quotaData=[];
    $scope.happyBackListData=[];
	$http.get('teamInfo/queryTeamName.do').success(function(msg){
		for(var i=0;i<msg.teamInfo.length;i++){
			$scope.teamType.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
		}
//		$scope.agent.teamId=msg.teamInfo[0].teamId;
	});
	$scope.showStatus = true;
	$http.post('myInfo/queryMyInfo',{"agentNo":$scope.entityId,"teamId":$scope.teamId}).success(function(msg){
		$scope.agent = msg.agentInfo;
		$scope.dpData = msg.agentProducts;
		$scope.shareData = msg.agentShare;
		$scope.rateData = msg.agentRate;
		$scope.quotaData = msg.agentQouta;
        $scope.happyBackListData = msg.happyBackList;
		if ($scope.happyBackListData=="" || $scope.happyBackListData==null) {
			$scope.showStatus = false;
		}
		for ( var index in $scope.happyBackListData) {
			if(!$scope.happyBackListData[index].showFullPrizeAmount && $scope.happyBackListData[index].showNotFullDeductAmount){
				$scope.columnDefs.splice(8,1);
				$scope.columnDefs.splice(9,1);
				break;
			}else if(!$scope.happyBackListData[index].showNotFullDeductAmount && $scope.happyBackListData[index].showFullPrizeAmount){
				$scope.columnDefs.splice(9,1);
				$scope.columnDefs.splice(10,1);
				break;
			}else if(!$scope.happyBackListData[index].showFullPrizeAmount && !$scope.happyBackListData[index].showNotFullDeductAmount) {
				$scope.columnDefs.splice(8,4);
				break;
			}
			
		}
	}).error(function(){
	});
	
	
	
	var rowList={};
    $scope.bpList = {
        data: 'dpData',
        columnDefs: [
             // {field: 'key1',displayName: '业务产品ID',width: 150},
             // {field: 'key3',displayName: '业务产品名称',width: 150},
             // {field: 'key2',displayName: '状态',width: 150,cellFilter:"formatDropping:"+$scope.statusStr}
            {field: 'key1',displayName: '业务产品ID',width: 150},
            {field: 'key3',displayName: '业务产品名称',width: 150},
            {field: 'key2',displayName: '状态',width: 150,cellFilter:"formatDropping:"+$scope.statusStr},
            {field: 'key6',displayName: '群组号',width: 200},
            {field: 'key8',displayName: '设置默认费率',width: 200,pinnable: false,sortable: false,editable:true,cellTemplate:
                '<div ng-show="!!row.entity.key6">' +
				'<span ng-show="row.entity.key8"><switch class="switch-s wide switch-boss switch-moren" disabled="row.entity.key8" ng-model="row.entity.key8" ng-change="" /></span>' +
				'<span ng-show="!row.entity.key8"><switch class="switch-s wide switch-boss switch-moren" disabled="row.entity.key8" ng-model="row.entity.key8" ng-change="grid.appScope.updateDefaultFlagSwitch(row)"/></span>' +
				'</div>'
			},
            {field: 'key7',displayName: '是否允许单独申请',width: 200,cellFilter:"formatDropping:"+angular.toJson($scope.bool)}
        ],
		onRegisterApi : function(gridApi) {
			$scope.gridApiProduct = gridApi;
		}
    };
    $scope.frStr='[{text:"每笔固定收益金额",value:"1"},{text:"每笔收益率",value:"2"},{text:"每笔收益率带保底封顶",value:"3"},{text:"每笔收益率+每笔固定收益金额",value:"4"},'+
		'{text:"商户费率与代理商成本差额百分比分润",value:"5"},{text:"商户费率与代理商成本差额阶梯分润",value:"6"}]';
	$scope.shareList = {
		data: 'shareData',
		columnDefs: [

            {field: 'bpName',displayName: '业务产品名称',width: 150},
            {field: 'serviceName',displayName: '服务名称',width: 200},
            {field: 'serviceType',displayName: '服务种类',width: 150,
                cellTemplate:
                '<div class="lh30" ng-show="row.entity.serviceType!=10000&&row.entity.serviceType!=10001"><span ng-bind="row.entity.serviceType | serviceTypeFilter"/></div>'
                +'<div class="lh30" ng-show="row.entity.serviceType==10000||row.entity.serviceType==10001"><span ng-bind="row.entity.serviceType2 | serviceTypeFilter"/>-提现</div>'
            },
            {field: 'cardType',displayName: '银行卡种类',width: 150,cellFilter:"formatDropping:"+$scope.cardTypeStr},
            {field: 'holidaysMark',displayName: '节假日标志',width: 150,cellFilter:"formatDropping:"+$scope.holidaysStr},
            // {field: 'profitType',displayName: '分润方式',width: 200,cellFilter:"formatDropping:"+$scope.frStr},
            // {field: 'income',displayName: '代理商收益',width: 150},
            // {field: 'cost',displayName: '代理商成本',width: 150},
            {field: 'cost',displayName: '代理商成本',width: 150,cellTemplate:
            //如果不是体现服务，也就是交易服务，后面显示%
            '<div ng-show="row.entity.serviceType!=10000&&row.entity.serviceType!=10001" style="width:98%;height:98%;"> '
            + '{{row.entity[col.field]}}'
            +'</div>'
            //如果是体现服务，后面显示单位元
            +'<div ng-show="row.entity.serviceType==10000||row.entity.serviceType==10001" style="width:98%;height:98%;"> '
            + '{{row.entity[col.field]}} <span ng-show="row.entity[col.field]">元</span>'
            +'</div>'},
            {field: 'shareProfitPercent',displayName: '代理商固定分润百分比',width: 180,cellTemplate:'<span class="lh30" ng-show="row.entity.shareProfitPercent!=null">'+
            '{{row.entity.shareProfitPercent}}%</span>'},
            // {field: 'ladderRate',displayName: '阶梯分润比例',width: 300},
            {field: 'efficientDate',displayName: '生效日期',width: 200,cellFilter:'date:"yyyy-MM-dd"'},
            {field: 'efficientDate',displayName: '当前是否生效',width: 180,cellFilter:"compareDateFilter"}

            // {field: 'serviceName',displayName: '服务名称',width: 150},
            // {field: 'cardType',displayName: '银行卡种类',width: 120,cellFilter:"formatDropping:"+$scope.cardTypeStr},
            // {field: 'holidaysMark',displayName: '节假日标志',width: 120,cellFilter:"formatDropping:"+$scope.holidaysStr},
            // {field: 'profitType',displayName: '分润方式',width: 250,cellFilter:"formatDropping:"+$scope.frStr},
            // {field: 'income',displayName: '代理商收益',width: 150},
            // {field: 'cost',displayName: '代理商成本',width: 150},
            // {field: 'shareProfitPercent',displayName: '代理商固定分润百分比',width: 200,cellTemplate:'<span ng-show="row.entity.shareProfitPercent!=null">'+
            // 	'{{row.entity.shareProfitPercent}}%</span>'},
            // {field: 'ladderRate',displayName: '阶梯分润比例',width: 400},
            // {field: 'efficientDate',displayName: '生效日期',width: 200,cellFilter:'date:"yyyy-MM-dd"'},
            // {field: 'efficientDate',displayName: '当前是否生效',width: 200,cellFilter:"compareDateFilter"}
		]
	};
	
	$scope.columnDefs = [
	     {field: 'activityTypeNo',displayName: '欢乐返子类型编号',width: 180},
         {field: 'activityTypeName',displayName: '欢乐返子类型名称',width: 150},
         {field: 'activityCode',displayName: '欢乐返类型',width: 150,cellFilter:"formatDropping:"+ angular.toJson($scope.subjectTypes)},//,cellFilter:'formatDropping:'+$scope.merRateStr
         {field: 'transAmount',displayName: '交易金额',width: 150},
	     {field: 'cashBackAmount',displayName: '返现金额',width: 150,
	            	cellTemplate:
					'<div style="width:98%;height:98%;" > '
					+'<span type="text" style="width:80%;height:98%;" class="ui-widget input" ng-readonly="false" ng-bind="row.entity[col.field]"/>'
					+' 元'
					+'</div>'},
	     {field: 'taxRate',displayName: '返现比例',width: 200,
    	            	cellTemplate:
        					'<div style="width:98%;height:98%;" > '
        					+'<span type="text" style="width:80%;height:98%;" class="ui-widget input" ng-readonly="false" ng-bind="row.entity[col.field]*100"/>'
        					+'%'
        					+'</div>'},
        {field: 'repeatRegisterAmount',displayName: '重复注册返现金额',width: 150,
            cellTemplate:
            '<div style="width:98%;height:98%;" ng-show="row.entity.repeatRegisterAmount!=null"> '
            +'<span ng-bind="row.entity[col.field]" />'
            +' 元'
            +'</div>'
        },
        {field: 'repeatRegisterRatio',displayName: '重复注册返现比例',width: 200,
            cellTemplate:
            '<div style="width:98%;height:98%;" ng-show="row.entity.repeatRegisterRatio!=null"> '
            +'<span ng-bind="row.entity[col.field]*100" />'
            +' %'
            +'</div>'
        },
        
        
        {field: 'fullPrizeAmount',displayName: '首次注册满奖金额',width: 150,
            cellTemplate:
            '<div style="width:98%;height:98%;" ng-show="row.entity.showFullPrizeAmount" > '
            +'<span ng-bind="row.entity[col.field]" />'
            +' 元'
            +'</div>'
        },
        {field: 'notFullDeductAmount',displayName: '首次注册不满扣金额',width: 150,
        	cellTemplate:
        		'<div style="width:98%;height:98%;" ng-show="row.entity.showNotFullDeductAmount" > '
        		+'<span ng-bind="row.entity[col.field]" />'
        		+' 元'
        		+'</div>'
        },
        {field: 'repeatFullPrizeAmount',displayName: '重复注册满奖金额',width: 150,
        	cellTemplate:
        		'<div style="width:98%;height:98%;" ng-show="row.entity.showFullPrizeAmount"> '
        		+'<span ng-bind="row.entity[col.field]" />'
        		+' 元'
        		+'</div>'
        },
        {field: 'repeatNotFullDeductAmount',displayName: '重复注册不满扣金额',width: 150,
        	cellTemplate:
        		'<div style="width:98%;height:98%;" ng-show="row.entity.showNotFullDeductAmount"> '
        		+'<span ng-bind="row.entity[col.field]" />'
        		+' 元'
        		+'</div>'
        }
        
        
	];
	var rateRowsList={};
	$scope.happyBackList = {
		data: 'happyBackListData',
		columnDefs: $scope.columnDefs,
		onRegisterApi: function(gridApi) {                //选中行配置
	        $scope.rateGridApi = gridApi;
		}
	};
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
            {field: 'singleMinAmount',displayName: '单笔最小交易额',width: 150,pinnable: false,sortable: false,editable:true,
            },
            {field: 'singleCountAmount',displayName: '单笔最大交易额',width: 150,pinnable: false,sortable: false,editable:true,
            },
            {field: 'singleDaycardAmount',displayName: '单日单卡最大交易额',width: 180,pinnable: false,sortable: false,editable:true,
            },
            {field: 'singleDaycardCount',displayName: '单日单卡最大交易笔数',width: 250,pinnable: false,sortable: false,editable:true,
            }
		],
		onRegisterApi: function(gridApi) {                //选中行配置
	        $scope.quotaGridApi = gridApi;
    	}
	};
	$scope.updateDefaultFlagSwitch = function (row) {
		if(!row.entity.key8){
			return;
		}
        var changeId = $scope.confirmDefaultFlagSwitch(row);
		SweetAlert.swal({
            title: "设置默认费率？",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "提交",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true
        },function (isConfirm) {
        	if (isConfirm) {
                $http({
                    url: 'myInfo/updateDefaultFlagSwitch?bpId=' + row.entity.key1,
                    method: 'POST'
                }).success(function (data) {
                    if(!data.success){
                        $scope.notice(data.message);
                        $scope.cancelDefaultFlagSwitch(row, changeId);
                    }else{
                        $scope.notice("更新默认费率成功");
					}
                }).error(function (data) {
                    $scope.notice("服务器异常,更新状态失败.");
                    $scope.cancelDefaultFlagSwitch(row, changeId);
                });
            } else {
                $scope.cancelDefaultFlagSwitch(row, changeId);
            }
        });
    } ;
	$scope.confirmDefaultFlagSwitch = function (row) {
	    var changeId = null;
        angular.forEach($scope.dpData, function (item) {
        	if(item.key1 == row.entity.key1 || row.entity.key6 != item.key6 || item.key8 == !row.entity.key8){
        		return;
			}
            item.key8 = !row.entity.key8;
            changeId = item.key1;
        });
        return changeId;
    };
    $scope.cancelDefaultFlagSwitch = function (row, changeId) {
        row.entity.key8 = !row.entity.key8;
        if (!changeId) return;
        angular.forEach($scope.dpData, function (item) {
            if (changeId == item.key1){
                item.key8 = !row.entity.key8;
            }
        });
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