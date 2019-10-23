/**
 * 我的信息-预调账明细查询 控制器
 * @author ZengJA
 * @date 2017-07-06 15:12:18
 */
angular.module('inspinia').controller('preliminaryAdjustQueryCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,i18nService){
	
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	
	$scope.adjustReason=[{text:"全部",value:""}
	,{text:"开通返现",value:"open_return"}
	,{text:"费率差异",value:"rate_variance"}
	,{text:"商户管理费",value:"merchant_management_fee"}
	,{text:"风控扣款",value:"risk_deduction"}
	,{text:"保证金扣除",value:"margin_deduction"}
	,{text:"其他",value:"other"}
	];
	
	$scope.myAccount = {balance: 0, balanceToday: 0};
	$scope.accountAllInfo = [];
	$scope.info = {adjustReason:""
        , sTime: moment(new Date().getTime()).format('YYYY-MM-DD') + ' 00:00:00'
        , eTime: moment(new Date().getTime()).format('YYYY-MM-DD') + ' 23:59:59'
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
		$scope.info = {
			sTime: moment(new Date().getTime()).format('YYYY-MM-DD') + ' 00:00:00'
			, eTime: moment(new Date().getTime()).format('YYYY-MM-DD') + ' 23:59:59'
			,adjustReason:""};
	}
	
	$scope.preliminaryAdjustQuery=function(){
		if(!($scope.info.sTime && $scope.info.eTime)){
			$scope.notice("申请预调账时间不能为空");
			return;
		}
		var data={"sTime":$scope.info.sTime,"eTime":$scope.info.eTime
			,"adjustReason":$scope.info.adjustReason
            ,"pageNo":$scope.paginationOptions.pageNo
			,"pageSize":$scope.paginationOptions.pageSize};
		$http.post('myInfo/preliminaryAdjustQuery',"info="+angular.toJson(data)
			,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(result){
			if(result.bols){
                $scope.data = result.collection.list;
                $scope.accountGrid.totalItems = result.collection.total;
                // console.log(result);
			}else{
				$scope.notice(result.msg);
			}
		});
	}

    $scope.preliminaryAdjustExport=function(){
		if(!($scope.info.sTime && $scope.info.eTime)){
			$scope.notice("申请预调账时间不能为空");
			return;
		}
        location.href="myInfo/preliminaryAdjustExport?info="+angular.toJson($scope.info);
    }
	$scope.accountGrid ={
		 data:'data',
		 paginationPageSize:10,                  //分页数量
	     paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
	     useExternalPagination: true,
	     columnDefs:[                           //表格数据
			{ field: 'agentNo',displayName:'代理商编号',width:150}
            ,{ field: 'agentName',displayName:'代理商名称',width:150}
            ,{ field: 'adjustTime',displayName:'申请预调账时间',width:150,
                 cellTemplate:"<div>{{row.entity.adjustTime | date:'yyyy-MM-dd'}} {{row.entity.adjustTime | date:'HH:mm:ss'}}</div>"
             }
            ,{ field: 'openBackAmount',displayName:'开通返现',width:150}
			,{ field: 'rateDiffAmount',displayName:'费率差异',width:150}
			,{ field: 'tuiCostAmount',displayName:'超级推成本',width:150}
			,{ field: 'riskSubAmount',displayName:'风控扣款',width:150}
			,{ field: 'merMgAmount',displayName:'商户管理费',width:150}
			,{ field: 'bailSubAmount',displayName:'保证金扣除',width:150}
			,{ field: 'otherAmount',displayName:'其他',width:150}
			,{ field: 'activityAvailableAmount',displayName:'账户可用余额调账金额',width:180}
			,{ field: 'activityFreezeAmount',displayName:'账户冻结余额调账金额',width:180}
			,{ field: 'generateAmount',displayName:'预调账金额',width:150}
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
					$scope.preliminaryAdjustQuery();
					rowList={};
			 });
		}
	};
});