angular.module('inspinia').controller("ruleDetailCtrl",function($scope, $http, $state, $stateParams) {
	
	   $scope.validNode = [{text:"抢单成功",value:1},{text:"提交进件",value:2},{text:"进件审批通过",value:3},{text:"首次交易",value:4}];
	   $scope.data = {"id":$stateParams.id};
	   
	   $http.post('push/getSupertuiRule.do', angular.toJson($scope.data)
	   ).success(function(info){
	        $scope.info = info;
	   }).error(function(){
	   }); 
	   
});