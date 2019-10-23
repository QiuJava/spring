/**
 * Mpos激活查询详情
 */
angular.module('inspinia').controller('mposActiveOrderDetailCtrl',function($scope, $http, $state,$stateParams,$sce){
    
	// 活动状态
    $scope.statusList = [{text:"全部",value:""},
    	{text:"未激活",value:"1"},
    	{text:"已激活",value:"2"},
    	{text:"已返补贴",value:"3"}
    ];
    
    // 补贴入账状态
    $scope.accountStatusList = [
    	{text:"待入账",value:"0"},
    	{text:"已记账",value:"1"},
    	{text:"记账失败",value:"2"}
    ];

    $scope.userTypeList = [{text:"全部",value:""},{text:"专员",value:"20"},{text:"经理",value:"30"},{text:"银行家",value:"40"}];//用户身份
	
	$scope.query = function () {
		$http({
	        url:"superBank/selectMposActiveOrderList?pageNo="+1+'&pageSize='+10,
	        data: {orderNo : $stateParams.orderNo},
	        method:'POST'
	    }).success(function(result){
	        if (result.status){
	            $scope.baseInfo = result.data.page.result[0];
	        } else {
	           $scope.notice(result.msg);
	        }
	    })
	};
	$scope.query();
});