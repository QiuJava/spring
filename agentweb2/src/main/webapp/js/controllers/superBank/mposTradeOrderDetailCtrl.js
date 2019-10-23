/**
 * Mpos激活查询详情
 */
angular.module('inspinia').controller('mposTradeOrderDetailCtrl',function($scope, $http, $state,$stateParams,$sce){
    // 交易状态
    $scope.transStatusList = [
    	{text:"初始化",value:"0"},
    	{text:"成功",value:"1"},
    	{text:"失败",value:"2"}
    ];
    // 交易类型(1刷卡支付 2微信支付 3支付宝 4云闪付 5取现 6银联二维码 7快捷支付 8测试交易)
    $scope.transTypeList = [
		{text:"POS",value:"1"},
		{text:"支付宝",value:"2"},
		{text:"微信",value:"3"},
		{text:"快捷",value:"4"},
		{text:"银联二维码",value:"5"}
    ];
    // 是否激活交易
    $scope.isActiveTradeList = [{text:"是",value:"1"},{text:"否",value:"0"}];
    // 结算周期
    $scope.settleCycleList = [{text:"T0",value:"1"},{text:"T1",value:"2"}];
    // 收款类型
    $scope.receiveTypeList = [{text:"标准类",value:"1"},{text:"VIP类",value:"2"}];
    // 订单状态
    $scope.statusList = [
    	{text:"全部",value:""},
    	{text:"待付款",value:"1"},
    	{text:"待发货",value:"2"},
    	{text:"待收货",value:"3"},
    	{text:"已收货",value:"4"},
    	{text:"已关闭",value:"9"}];
    // 记账/入账状态
    $scope.accountStatusList = [{text:"待入账",value:"0"},{text:"已入账",value:"1"},{text:"记账失败",value:"2"}];
    // 计算分润状态
    $scope.profitStatusList = [{text:"计算失败",value:"0"},{text:"计算成功",value:"1"}];
    $scope.userTypeList = [{text:"专员",value:"20"},{text:"经理",value:"30"},{text:"银行家",value:"40"}];//用户身份
    //结算状态
    $scope.settleStatusList = [{text:"未提交",value:"0"},{text:"已提交",value:"1"},{text:"提交失败",value:"2"},{text:"超时",value:"3"},{text:"交易成功",value:"4"},{text:"交易失败",value:"5"},{text:"未知",value:"6"}];

    $scope.query = function () {
		$http({
	        url:"superBank/selectMposTradeOrderDetail?orderNo="+$stateParams.orderNo,
	    }).success(function(result){
	        if (result.status){
	            $scope.baseInfo = result.data;
	        } else {
	           $scope.notice(result.msg);
	        }
	    })
	};
	$scope.query();


});