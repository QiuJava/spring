/**
/**
 * 硬件产品新增
 */
angular.module('inspinia').controller('addHardCtrl',function($scope,$http,$state,$stateParams,$compile,$filter){
	$scope.info = {orgId:null,secretType:0,devicePn:$scope.devicePNTypeLists[0].value};
	$scope.team = [{text:'全部',value:null}];
	$scope.secretTypeList = [{text:'无',value:0},{text:'双密钥',value:1}];
	$scope.commit=function(){
		$scope.submitting = true;
		$http.post("hardwareProduct/addHard",angular.toJson($scope.info))
		.success(function(data){
			if(data.bols){
				$scope.notice(data.msg);
				$state.transitionTo('service.insertHard',null,{reload:true});
			}else{
				$scope.notice(data.msg);
				$scope.submitting = false;
			}
		});
	}
	
	$http.get('teamInfo/queryTeamName.do').success(function(msg){
			for(var i=0;i<msg.teamInfo.length;i++){
				$scope.team.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
			}
	});
	  
})