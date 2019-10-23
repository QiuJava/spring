angular.module('inspinia').controller("orderDetailCtrl",function($scope, $http, $state, $stateParams) {
	 
	$scope.orderStatus = [{text:"全部",value:-1},{text:"待抢",value:1},{text:"再次待抢",value:2},{text:"待核查",value:3},{text:"待进件",value:4},{text:"无效单",value:5}];
	$scope.types = [{text:"全部",value:-1},{text:"推荐人代理商",value:1},{text:"公共池抢单",value:2},{text:"公司业务",value:3}];
	$scope.data = {"id":$stateParams.id};
	
	$http.post('push/getSupertuiOrderInfo.do', angular.toJson($scope.data)
	).success(function(info){
	    $scope.info = info;
	}).error(function(){
	}); 
	
});