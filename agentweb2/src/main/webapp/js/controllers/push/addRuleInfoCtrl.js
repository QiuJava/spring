angular.module('inspinia').controller("addRuleCtrl",function($scope, $http, $state, $stateParams) {
	
	$scope.validNode = [{text:"抢单成功",value:1},{text:"提交进件",value:2},{text:"进件审批通过",value:3},{text:"首次交易",value:4}];
	var e_time = new Date().valueOf() + (89 * 24 * 60 * 60 * 1000);
	$scope.info = {"rewardValidNode":1,"feeValidNode":1,"efficientDate":new Date(),"disabledDate":e_time};
	
	//业务产品
	$http.get('businessProductDefine/selectAllInfo.do')
	.success(function(largeLoad) {
		if(!largeLoad)
			return
		$scope.productTypes=largeLoad;
	});
	
	$scope.addRule = function() {
		var data= {"info":$scope.info};
		$http.post('push/addSupertuiRule.do',
				angular.toJson(data)
		).success(function(msg){
			if(msg.status)
				$scope.info = {"rewardValidNode":1,"feeValidNode":1,"efficientDate":new Date(),"disabledDate":e_time};
			$scope.notice(msg.msg);
		}).error(function(){
		});
	};
	
});