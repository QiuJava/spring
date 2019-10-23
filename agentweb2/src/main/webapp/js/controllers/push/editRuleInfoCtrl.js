angular.module('inspinia').controller("editRuleCtrl",function($scope, $http, $state, $stateParams) {
	
	$scope.validNode = [{text:"抢单成功",value:1},{text:"提交进件",value:2},{text:"进件审批通过",value:3},{text:"首次交易",value:4}];
	var e_time = new Date().valueOf() + (89 * 24 * 60 * 60 * 1000);
	$scope.newInfo = {"rewardValidNode":1,"feeValidNode":1,"efficientDate":new Date(),"disabledDate":e_time};
	$scope.data = {"id":$stateParams.id};
	
	$scope.ruleDetail = function() {
		$http.post('push/getSupertuiRule.do', angular.toJson($scope.data)
		).success(function(info){
	        $scope.info = info;
		}).error(function(){
		}); 
	}

	$scope.ruleDetail();
	
	$scope.editRule = function() {
		$scope.newInfo.id = $stateParams.id;
		var data = {"newInfo":$scope.newInfo};
		$http.post('push/updateSupertuiRule.do',
				angular.toJson(data)
		).success(function(msg){
			$scope.notice(msg.msg);
			if(msg.status){
				$scope.ruleDetail();
				$scope.newInfo = {"rewardValidNode":1,"feeValidNode":1};
			}
		}).error(function(){
		});
	};
	
});