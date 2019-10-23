angular.module('inspinia').controller('happyBackNotFullDeductDetailCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,i18nService){
	
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	
	$scope.agentList=[];
	
	$scope.info = {agentNo:"",orderNo:""
        , sTime: moment(new Date().getTime() ).format('YYYY-MM-DD') + ' 00:00:00'
        , eTime: moment(new Date().getTime() ).format('YYYY-MM-DD') + ' 23:59:59'
        };
	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0
	setBeginTime=function(setTime){
		$scope.info.sTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

	}

	setEndTime=function(setTime){
		$scope.info.eTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");

	}
	$scope.reset = function(){
		$scope.info = {sTime: moment(new Date().getTime() ).format('YYYY-MM-DD') + ' 00:00:00'
            ,eTime: moment(new Date().getTime() ).format('YYYY-MM-DD') + ' 23:59:59'
			,agentNo:"",orderNo:""};
	}
	$http.post('agentInfo/chidrenAgent').success(function(result){
		if (result.status) {
			$scope.agentList = result.data;
		}
	});
	$scope.detail={};
	$scope.data=[];
	$scope.query=function(){
		if (!($scope.info.sTime && $scope.info.eTime)){
			$scope.notice("日期不能为空");
			return
		}
		var data={"sTime":$scope.info.sTime,"eTime":$scope.info.eTime,"orderNo":$scope.info.orderNo
			,"agentNo":$scope.info.agentNo
            ,"pageNo":$scope.paginationOptions.pageNo
			,"pageSize":$scope.paginationOptions.pageSize};
		$http.post('myInfo/happyBackNotFullDeductDetail',"info="+angular.toJson(data)
			,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(result){
			if(result.status){
				$scope.detail = result.data;
				$scope.data =result.data.list || [];
				$scope.happyBackNotFullDeductDetailGrid.totalItems =result.data.total;
			}else {
                $scope.detail = {};
                $scope.data = [];
                $scope.happyBackNotFullDeductDetailGrid.totalItems = 0;
			}
		});
	}
	$scope.query();
    $scope.exportList=function(){
    	if (!($scope.info.sTime && $scope.info.eTime)){
    		$scope.notice("日期不能为空");
    		return
		}
        location.href="myInfo/happyBackNotFullDeductDetailExport?info="+angular.toJson($scope.info);
        
    }
	$scope.happyBackNotFullDeductDetailGrid ={
		 data:'data',
		 paginationPageSize:10,                  //分页数量
	     paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
	     useExternalPagination: true,
	     
	     columnDefs:[                           //表格数据
			{ field: 'shouldDebtAmount',displayName:'应扣款金额(元)',width:150,cellTemplate:"<div>{{row.entity.shouldDebtAmount}}</div>"}
            ,{ field: 'debtAmount',displayName:'实际扣款金额(元)',width:150,cellTemplate:"<div>{{row.entity.debtAmount}}</div>"}
            ,{ field: 'adjustAmount',displayName:'累计待扣款金额(元)',width:180,cellTemplate:"<div>{{row.entity.adjustAmount}}</div>"}
			,{ field: 'agentNo',displayName:'扣款代理商编号',width:150}
			,{ field: 'agentName',displayName:'扣款代理商名称',width:200}
			,{ field: 'debtTime',displayName:'日期',width:150,cellTemplate:"<div>{{row.entity.debtTime | date:'yyyy-MM-dd HH:mm:ss'}}</div>"}
			,{ field: 'orderNo',displayName:'欢乐返订单号',width:250}
		 ]
		 ,onRegisterApi: function(gridApi) {                //选中行配置
			 $scope.gridApi = gridApi;
			 $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
					$scope.paginationOptions.pageNo = newPage;
					$scope.paginationOptions.pageSize = pageSize;
					$scope.query();
			 });
		}
	};
});