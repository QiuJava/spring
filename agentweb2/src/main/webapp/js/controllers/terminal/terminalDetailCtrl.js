/**
 * 机具详情
 */
angular.module('inspinia').controller('terminalDetailCtrl',function($scope,$state,$http,$stateParams,$filter){
	
	 	var data;
	 	$http.get('terminalInfo/selectObjInfo.do?ids='+$stateParams.termId)
	    .success(function(data) {
	    	if(!data.success){
	    		$scope.notice(data.message);
	    		return;
			}
            var largeLoad = data.data;
	    	if(!largeLoad)
	    		return;
	    	data=largeLoad;
	    	if(largeLoad.openStatus==0){
	    		data.openStatusName="已入库";	
	    	}
	    	if(largeLoad.openStatus==1){
	    		data.openStatusName="已分配";	
	    	}
	    	if(largeLoad.openStatus==2){
	    		data.openStatusName="已使用";	
	    	}
	    	
	    	var temper1=largeLoad.startTime;
	    	if(temper1){
	    		var dt1=new Date();
	 			dt1.setTime(temper1);
	 			data.startTimes=$filter('date')(dt1,'yyyy-MM-dd HH:mm:ss');
	    	}
	    	
 			var temper2=largeLoad.createTime;
 			if(temper2){
 				var dt2=new Date();
	 			dt2.setTime(temper2);
	 			data.createTimes=$filter('date')(dt2,'yyyy-MM-dd HH:mm:ss');
	    	}

			var temper3=largeLoad.receiptDate;
			if(temper3){
				var dt3=new Date();
				dt3.setTime(temper3);
				data.receiptDate=$filter('date')(dt3,'yyyy-MM-dd HH:mm:ss');
			}

			var temper4=largeLoad.downDate;
			if(temper4){
				var dt4=new Date();
				dt4.setTime(temper4);
				data.downDate=$filter('date')(dt4,'yyyy-MM-dd HH:mm:ss');
			}
 			$scope.data=data;
	    });
	 	
	 	$scope.agentL = _Dlevel;
});

