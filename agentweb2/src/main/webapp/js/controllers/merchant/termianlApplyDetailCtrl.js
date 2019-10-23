/**
 * 机具申请详情
 */
angular.module('inspinia').controller('termianlApplyDetailCtrl',function($scope,$state,$rootScope,$filter,$log,$http,$stateParams,$compile,i18nService){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.info={};
    $scope.statuss=[{text:"全部",value:""},{text:"已处理",value:1},{text:"待直属处理",value:0},{text:"待一级处理",value:2}];
	$http.get('terminalApplyAction/selectDetail?ids='+$stateParams.id)
	.success(function(largeLoad) {
		if(largeLoad.bols){
			$scope.info=largeLoad.result;
		}else{
			$scope.notice("查询出错");
		}
	});

});