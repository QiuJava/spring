/**
 * 我的信息-解冻明细查询 控制器
 * @author ZengJA
 * @date 2017-07-06 15:12:18
 */
angular.module('inspinia').controller('unFreezeQueryCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,i18nService){
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);

	$scope.myAccount = {balance: 0, balanceToday: 0};
	$scope.accountAllInfo = [];
	$scope.info = {sTime: moment(new Date().getTime() - 1 * 24 * 60 * 60 * 1000).format('YYYY-MM-DD') + ' 00:00:00'
        , eTime: moment(new Date().getTime() - 1 * 24 * 60 * 60 * 1000).format('YYYY-MM-DD') + ' 23:59:59'
        };
	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0
	setBeginTime=function(setTime){
		$scope.info.sTime = moment(setTime -  1 * 24 * 60 * 60 * 1000).format("YYYY-MM-DD HH:mm:ss");

	}

	setEndTime=function(setTime){
		$scope.info.eTime = moment(setTime - 1 * 24 * 60 * 60 * 1000).format("YYYY-MM-DD HH:mm:ss");

	}

	$scope.reset = function(){
		$scope.info = {sTime: moment(new Date().getTime() - 1 * 24 * 60 * 60 * 1000).format('YYYY-MM-DD') + ' 00:00:00'
			,eTime:moment(new Date().getTime() - 1 * 24 * 60 * 60 * 1000).format('YYYY-MM-DD') + ' 23:59:59'};
	}
	
	$scope.unFreezeQuery=function(){
		if(!($scope.info.sTime && $scope.info.eTime)){
			$scope.notice("解冻时间不能为空");
			return;
		}
		var data={"sTime":$scope.info.sTime,"eTime":$scope.info.eTime
            ,"pageNo":$scope.paginationOptions.pageNo
			,"pageSize":$scope.paginationOptions.pageSize};
		$http.post('myInfo/unFreezeQuery',"info="+angular.toJson(data)
			,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(result){
			if(result.bols){
                $scope.data = result.collection.list;
                $scope.accountGrid.totalItems = result.collection.total;
                console.log(result);
			}else{
				$scope.notice(result.msg);
			}
		});
	}

    $scope.unFreezeExport=function(){
		if(!($scope.info.sTime && $scope.info.eTime)){
			$scope.notice("解冻时间不能为空");
			return;
		}
        location.href="myInfo/unFreezeExport?info="+angular.toJson($scope.info);
    }
	$scope.accountGrid ={
        data:'data',
		 paginationPageSize:10,                  //分页数量
	     paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
	     useExternalPagination: true,
	     columnDefs:[                           //表格数据
			 { field: 'agentNo',displayName:'代理商编号',width:150}
			,{ field: 'agentName',displayName:'代理商名称',width:150}
			,{ field: 'amount',displayName:'解冻金额',width:150}
			,{ field: 'unfreezeTime',displayName:'解冻时间',width:150,
                 cellTemplate:"<div>{{row.entity.unfreezeTime | date:'yyyy-MM-dd'}} {{row.entity.unfreezeTime | date:'HH:mm:ss'}}</div>"
             }
			,{ field: 'terminalFreezeAmount',displayName:'解冻机具预冻结款',width:150}
			,{ field: 'otherFreezeAmount',displayName:'解冻其他预冻结款',width:150}
			,{ field: 'fenFreezeAmount',displayName:'解冻分润账户冻结款',width:180}
			,{ field: 'activityFreezeAmount',displayName:'解冻活动补贴账户冻结款',width:180}
			,{ field: 'remark',displayName:'备注',width:150}
		 ]
		 ,onRegisterApi: function(gridApi) {                //选中行配置
			 $scope.gridApi = gridApi;
			 //全选
			 $scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function (rows) {
				if(rows[0].isSelected){
				   $scope.testRow = rows[0].entity;
				   for(var i=0;i<rows.length;i++){
					   rowList[rows[i].entity.id]=rows[i].entity;
				   }
				}else{
					rowList={};
				}
			 })
			 //单选
			 $scope.gridApi.selection.on.rowSelectionChanged($scope,function (row) {
				if(row.isSelected){
				   $scope.testRow = row.entity;
				   rowList[row.entity.id]=row.entity;
				}else{
					delete rowList[row.entity.id];
				}
			 })
			 $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
					$scope.paginationOptions.pageNo = newPage;
					$scope.paginationOptions.pageSize = pageSize;
					$scope.unFreezeQuery();
					rowList={};
			 });
		}
	};
});