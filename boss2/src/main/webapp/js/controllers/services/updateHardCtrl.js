/**
/**
 * 硬件产品新增
 */
angular.module('inspinia').controller('updateHardCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,$stateParams){
	
	$scope.info = {orgId:null};
	$scope.team = [{text:'全部',value:null}];
    $scope.secretTypeList = [{text:'无',value:0},{text:'双密钥',value:1}];
	$scope.commit=function(){
		$scope.submitting = true;
		$http.post("hardwareProduct/updateHard",angular.toJson($scope.info))
		.success(function(data){
			if(data.bols){
				$scope.notice(data.msg);
				$state.transitionTo('service.queryHard',null,{reload:true});
			}else{
				$scope.notice(data.msg);
				$scope.submitting = false;
			}
		});
	}
	
	$http.get('hardwareProduct/queryHardById/'+$stateParams.id).success(function(msg){
		if(msg.status){
			for(var i=0;i<msg.teamInfo.length;i++){
				$scope.team.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
			}
			$scope.info = msg.hp;
		} else {
			$scope.notice('查询失败');
		}
	});
	
})