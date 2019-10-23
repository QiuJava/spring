/**
 * 公告详情
 */
angular.module('inspinia').controller("deliverNoticeCtrl", ['$scope', '$http', '$state', '$stateParams', function($scope, $http, $state, $stateParams) {
	//数据源
	$scope.sysType=[{text:"代理商",value:'2'},{text:"商户",value:'1'}];
	$scope.agentTypes = [{text:'所有下级代理商',value:'7'},{text:'直接下级代理商',value:'8'}];
	$scope.$state = $state;
	
	var id = $stateParams.id;
	
	$http.get('notice/selectById/'+id).success(function(msg){
		$scope.noticeInfo = msg.data;
	}).error(function(){
	});
	
    $scope.deliverNotice = function(){
    	$scope.submitting = true;
    	$http.get('notice/deliverNotice/' + id).success(function(msg){
    		if(msg.status){
    			$scope.notice(msg.msg);
    			$state.transitionTo('more.sentNotices',null,{reload:true});
    		}else{
    			$scope.notice(msg.msg);
    			$scope.submitting = false;
    		}
    	}).error(function(){
    		$scope.notice("下发失败！");
    		$scope.submitting = false;
    	});
    }
}]).filter('trustHtml', function ($sce) {
    return function (input) {
        return $sce.trustAsHtml(input);
    }
});;
