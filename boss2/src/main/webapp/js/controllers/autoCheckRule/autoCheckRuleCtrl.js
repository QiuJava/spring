angular.module('inspinia',['uiSwitch']).controller('autoCheckRuleCtrl',function(i18nService,$scope,$http,$state,$stateParams,$compile,$filter,SweetAlert){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.baseInfo = {};
	$http.get('autoCheckRule/autoCheckRule.do?paramKey='+"autoCheckRule.paramKey").success(function(msg){
		if(msg!=null){
			$scope.baseInfo.singleMerchTimes = msg[0].paramValue;
			$scope.baseInfo.phohoCompProp = msg[1].paramValue;
			$scope.baseInfo.bankCardOcr = msg[2].paramValue;
			$scope.baseInfo.idCardOcr = msg[3].paramValue;
			$scope.baseInfo.minAge = msg[4].paramValue.substring(0,2);
			$scope.baseInfo.maxAge = msg[4].paramValue.substring(3,5);
            $scope.baseInfo.livingJskj = msg[5];
            $scope.baseInfo.livingXskj = msg[6];
            $scope.baseInfo.livingXinLian = msg[7];
            $scope.baseInfo.ocrJskj = msg[8];
            $scope.baseInfo.ocrXskj = msg[9];
            $scope.baseInfo.ocrXinLian = msg[10];
            $scope.baseInfo.ocrYlsw = msg[11];
            $scope.baseInfo.realJskj = msg[12];
            $scope.baseInfo.realXskj = msg[13];
            $scope.baseInfo.realXinLian = msg[14];
            $scope.baseInfo.realYlsw = msg[15];
			
		}
	});
	//=================================== percent
	$scope.commit = function(){
		
		$scope.submitting = true;
		var data=$scope.baseInfo;
        if(Number(data.livingJskj.percent)+Number(data.livingXskj.percent)+Number(data.livingXinLian.percent)!=100){
            $scope.notice("请控制通道路由百分比之和为100！");
            $state.transitionTo('func.autoCheckRule',null,{reload:true});
            return;
        }
        if(Number(data.ocrJskj.percent)+Number(data.ocrXskj.percent)+Number(data.ocrXinLian.percent)+Number(data.ocrYlsw.percent)!=100){
        	$scope.notice("请控制通道路由百分比之和为100！");
        	$state.transitionTo('func.autoCheckRule',null,{reload:true});
        	return;
        }
        if(Number(data.realJskj.percent)+Number(data.realXskj.percent)+Number(data.realXinLian.percent)+Number(data.realYlsw.percent)!=100){
        	$scope.notice("请控制通道路由百分比之和为100！");
        	$state.transitionTo('func.autoCheckRule',null,{reload:true});
        	return;
        }

		if($scope.baseInfo.minAge<$scope.baseInfo.maxAge){
			$http.post('autoCheckRule/updateValues.do', angular.toJson(data)).success(function(msg){
	            if(msg.status){
	            	$state.transitionTo('func.autoCheckRule',null,{reload:true});
					$scope.submitting = false;
	            }
				$scope.submitting = false;
				$scope.notice(msg.msg);
	        }).error(function(){
				$scope.submitting = false;
	        }); 
		}else{
			$scope.notice("保存失败,最小年龄不能大于最大年龄！");
			$state.transitionTo('func.autoCheckRule',null,{reload:true});
		}
	}
	
	//===================================
	
	$scope.autoCheckRuleTable = {
			data: 'autoCheckRuleData',
			paginationPageSize: 10,
			paginationPageSizes: [10, 20, 50, 100],
			useExternalPagination: true,		  	//开启拓展名
			columnDefs: [
	            {field: 'id', displayName: '序号'},
	            {field: 'ruleDis', displayName: '规则名称'},
	            {field: 'isOpen', displayName: '是否打开',cellTemplate:
	            	'<span ><switch class="switch switch-s" ng-model="row.entity.isOpen" ng-true-value="1" ng-false-value="0" ng-change="grid.appScope.open1(row)" /></span>'
//	            	+'<span ng-show="row.entity.isOpen==1">开启</span><span ng-show="row.entity.isOpen==0">关闭</span>'
	            },
	            {field: 'isPass', displayName: '是否必过',cellTemplate:
	            	'<span ><switch class="switch switch-s" ng-model="row.entity.isPass" ng-true-value="1" ng-false-value="0" ng-change="grid.appScope.open2(row)" /></span>'
//	            	+'<span ng-show="row.entity.isPass==1">开启</span><span ng-show="row.entity.isPass==0">关闭</span>'
	            }
	        ]
		};
		
		//查询
		$scope.query = function(){
			$http.post('autoCheckRule/autoCheckRuleAll.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
				$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
					.success(function(page){
						if(!page){
							return;
						}
						$scope.autoCheckRuleData = page;
						$scope.autoCheckRuleTable.totalItems = page.totalCount;
					}).error(function(){
					});
		}
		$scope.query();
		
		//=====================================
		$scope.open1=function(row){
			if(row.entity.isOpen){
				$scope.serviceText = "确定开启？";
			} else {
				$scope.serviceText = "确定关闭？";
			}
	        SweetAlert.swal({
	            title: $scope.serviceText,
//	            text: "服务状态为关闭后，不能正常交易!",
	            type: "warning",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: "提交",
	            cancelButtonText: "取消",
	            closeOnConfirm: true,
	            closeOnCancel: true },
		        function (isConfirm) {
		            if (isConfirm) {
		            	if(row.entity.isOpen==true){
		            		row.entity.isOpen=1;
		            	} else if(row.entity.isOpen==false){
		            		row.entity.isOpen=0;
		            	}
		            	var data={"isOpen":row.entity.isOpen,"id":row.entity.id};
		                $http.post("autoCheckRule/updateIsOpen.do",angular.toJson(data))
		            	.success(function(data){
		            		if(data.status){
		            			$scope.notice("操作成功！");
		            		}else{
		            			if(row.entity.isOpen==true){
		    	            		row.entity.isOpen = false;
		    	            	} else {
		    	            		row.entity.isOpen = true;
		    	            	}
		            			$scope.notice("操作失败！");
		            		}
		            	})
		            	.error(function(data){
		            		if(row.entity.isOpen==true){
	    	            		row.entity.isOpen = false;
	    	            	} else {
	    	            		row.entity.isOpen = true;
	    	            	}
		            		$scope.notice("服务器异常")
		            	});
		            } else {
		            	if(row.entity.isOpen==true){
		            		row.entity.isOpen = false;
		            	} else {
		            		row.entity.isOpen = true;
		            	}
		            }
	        });
	    	
	    };	
	    
		$scope.open2=function(row){
			if(row.entity.isPass){
				$scope.serviceText = "确定开启？";
			} else {
				$scope.serviceText = "确定关闭？";
			}
	        SweetAlert.swal({
	            title: $scope.serviceText,
	            type: "warning",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: "提交",
	            cancelButtonText: "取消",
	            closeOnConfirm: true,
	            closeOnCancel: true },
		        function (isConfirm) {
		            if (isConfirm) {
		            	if(row.entity.isPass==true){
		            		row.entity.isPass=1;
		            	} else if(row.entity.isPass==false){
		            		row.entity.isPass=0;
		            	}
		            	var data={"isPass":row.entity.isPass,"id":row.entity.id};
		                $http.post("autoCheckRule/updateIsPass.do",angular.toJson(data))
		            	.success(function(data){
		            		if(data.status){
		            			$scope.notice("操作成功！");
		            		}else{
		            			if(row.entity.isPass==true){
		    	            		row.entity.isPass = false;
		    	            	} else {
		    	            		row.entity.isPass = true;
		    	            	}
		            			$scope.notice("操作失败！");
		            		}
		            	})
		            	.error(function(data){
		            		if(row.entity.isPass==true){
	    	            		row.entity.isPass = false;
	    	            	} else {
	    	            		row.entity.isPass = true;
	    	            	}
		            		$scope.notice("服务器异常")
		            	});
		            } else {
		            	if(row.entity.isPass==true){
		            		row.entity.isPass = false;
		            	} else {
		            		row.entity.isPass = true;
		            	}
		            }
	        });
	    	
	    };	
	    	
});