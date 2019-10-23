/**
 * 公告详情
 */
angular.module('inspinia').controller("noticeDetailCtrl",function($scope, $http, $state, $stateParams) {
	//数据源
	$scope.sysType=[{text:"商户",value:'1'},{text:"代理商",value:'2'}];
	$scope.agentBusiness = [{text:"全部",value:'0'},{text:"指定一级代理商下（包括下级发展）的商户",value:'1'}];
	$scope.baseInfo = {agentBusiness:'0'};
	$scope.team = [{text:'全部',value:'0'},{text:'直营',value:'1'},{text:'非直营',value:'2'}];
	$scope.agentTypes = [{text:'所有代理商',value:'1'},{text:'所有一级代理商',value:'2'},
	                     {text:'所有直营代理商',value:'3'},{text:'所有直营一级代理商',value:'4'},
	                     {text:'所有非直营代理商',value:'5'},{text:'所有非直营一级代理商',value:'6'},
	                     {text:'所有下级代理商',value:'7'},{text:'直接下级代理商',value:'8'}];
	$scope.$state = $state;
	var id = $stateParams.id;
	
	$http.get('notice/selectById/'+$stateParams.id).success(function(msg){
		$scope.noticeInfo = msg.data;
	}).error(function(){
	})
	
}).filter('trustHtml', function ($sce) {
    return function (input) {
        return $sce.trustAsHtml(input);
    }
});;
