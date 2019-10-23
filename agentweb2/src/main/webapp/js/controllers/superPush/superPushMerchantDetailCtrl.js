/**
 * 商户详情查看
 */

angular.module('inspinia').controller('superPushMerchantDetailCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal){
	
	//数据源
	$scope.merchantInfo = {};
	$scope.superPush = {};
	$scope.terminalInfoList = {};
	$scope.merchantCardInfo = {};
	$scope.oneLevelAgent = {};
	var merchanStatus = {
		"0": "商户关闭",
		"1": "正常",
		"2": "冻结",
		"-1": '资料未完善'
	};
    var merchantType = {
        "1": "个人",
        "2": "个体商户",
        "3": "企业商户",
		"-1": ""
    };
    var accountType = {
    	"1": "对公",
		"2": "对私",
		"-1": ''
	};
	$http.get('superPushAction/getSuperPushMerchantDetail?userId='+$stateParams.userId)
	.success(function(result) {
		$scope.merchantInfo = result.data && result.data.merchantInfo;
        $scope.superPush = result.data && result.data.superPush;
        $scope.terminalInfoList = result.data && result.data.terminalInfo;
        $scope.merchantCardInfo = result.data && result.data.merchantCardInfo;
        $scope.oneLevelAgent = result.data && result.data.oneLevelAgent;
        $scope.merchantInfo.status = merchanStatus[($scope.merchantInfo && $scope.merchantInfo.status) || "-1"];
        $scope.merchantInfo.merchantType = merchantType[($scope.merchantInfo && $scope.merchantInfo.merchantType) || "-1"];
        $scope.merchantCardInfo.accountType = accountType[($scope.merchantCardInfo && $scope.merchantCardInfo.accountType) || "-1"];
	});
});

