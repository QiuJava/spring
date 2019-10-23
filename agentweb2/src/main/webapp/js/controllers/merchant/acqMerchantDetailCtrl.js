/**
 * 商户详情查看
 */

angular.module('inspinia').controller('acqMerchantDetailCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal){
	
	//数据源
	$scope.info={};
	$scope.fileList=[];
	$scope.auditList=[];
	$http.post('merchantInfo/acqMerInfoDetail.do?id='+$stateParams.id)
	.success(function(result) {
		if(!result.status){
			$scope.notice(result.msg);
			return;
		}
		$scope.info=result.data;
		$scope.fileList=result.data.fileList;
		$scope.auditList=result.data.auditList;
		
	});
	$scope.merchantTypes=function(data){
		if(data==1){
			return '个体收单商户';
		}
		if(data==2){
			return '企业收单商户';
		}
	};
	
	$scope.accountTypes=function(data){
		if(data==2){
			return '对公';
		}
		if(data==1){
			return '对私';
		}
	}
	
	$scope.addTime=function(time1,time2){
		return time1 + " - " +time2;
	}
	
	$scope.auditStatus=function(data){
		if (data==1) {
			return "待审核";
		}
		if (data==2) {
			return "审核通过";
		}
		if (data==3) {
			return "审核不通过";
		}
	}
	$scope.intos = [];
	 $http.post("merchantInfo/intos")
  	 .success(function(data){
  	   	for(var i=0; i<data.length; i++){
  	   		$scope.intos.push({value:data[i].sysValue,text:data[i].sysName});
  	   	}
  	});
	$scope.intoSources=function(data){
		
		for (var i = 0; i < $scope.intos.length; i++) {
			var into = $scope.intos[i];
			if (into.value==data) {
				return into.text;
			}
		} 
		
	}
	
	
	$scope.acqMerAutidList={
		data:'auditList',
		columnDefs:[
	           {field:'auditStatus',displayName:'审核状态',width:120,pinnable:false,sortable:false,
	        	   cellFilter: "formatDropping:[{text:'审核不通过',value:'3'},{text:'审核通过',value:'2'},{text:'待审核',value:'1'}]"
	           },
	           {field:'examinationOpinions',displayName:'审核意见',width:200,pinnable:false,sortable:false}, 
	           {field:'createTime',displayName:'审核日期',width:200,pinnable:false,sortable:false,
	        	   cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"
	           }
			]
		};
	
	//编辑进件资料
	$scope.updateAcqMerInfo=function(pp,id,status,content,name){
		
	}
	
});

