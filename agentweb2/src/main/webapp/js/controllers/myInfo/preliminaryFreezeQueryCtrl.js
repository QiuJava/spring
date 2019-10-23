/**
 * 我的信息-预冻结明细查询 控制器
 * @author ZengJA
 * @date 2017-07-06 15:12:18
 */
angular.module('inspinia').controller('preliminaryFreezeQueryCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,i18nService){
	
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	
	$scope.freezeReason=[{text:"全部",value:""},{text:"机具款",value:"terminal"},{text:"其他",value:"other"}];
	
	$scope.myAccount = {balance: 0, balanceToday: 0};
	$scope.accountAllInfo = [];
	$scope.info = {freezeReason:""
        ,sTime: moment(new Date().getTime() - 1 * 24 * 60 * 60 * 1000).format('YYYY-MM-DD')+' 00:00:00'
        ,eTime: moment(new Date().getTime() - 1 * 24 * 60 * 60 * 1000).format('YYYY-MM-DD') + ' 23:59:59'
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
		$scope.info = {freezeReason:"",
			sTime: moment(new Date().getTime() - 1 * 24 * 60 * 60 * 1000).format('YYYY-MM-DD')+' 00:00:00'
			,eTime: moment(new Date().getTime() - 1 * 24 * 60 * 60 * 1000).format('YYYY-MM-DD') + ' 23:59:59'}
	}
	
	$scope.preliminaryFreezeQuery=function(){
		if(!($scope.info.sdate && scope.info.edate)){
			$scope.notice("申请预计时间不能为空");
			return;
		}
		var data={"sTime":$scope.info.sTime,"eTime":$scope.info.eTime
			,"freezeReason":$scope.info.freezeReason
            ,"pageNo":$scope.paginationOptions.pageNo
			,"pageSize":$scope.paginationOptions.pageSize};
		$http.post('myInfo/preliminaryFreezeQuery',"info="+angular.toJson(data)
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

    $scope.preliminaryFreezeExport=function(){
        if(!($scope.info.sdate && scope.info.edate)){
            $scope.notice("申请预计时间不能为空");
            return;
        }
        location.href="myInfo/preliminaryFreezeExport?info="+angular.toJson($scope.info);
    }
	$scope.accountGrid ={
		 data:'data',
		 paginationPageSize:10,                  //分页数量
	     paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
	     useExternalPagination: true,
	     columnDefs:[                           //表格数据
			{ field: 'freezeTime',displayName:'申请预冻结时间',width:150,
				cellTemplate:"<div>{{row.entity.freezeTime | date:'yyyy-MM-dd'}} {{row.entity.freezeTime | date:'HH:mm:ss'}}</div>"
			}
			,{ field: 'agentNo',displayName:'代理商编号',width:150}
			,{ field: 'agentName',displayName:'代理商名称',width:150}
			,{ field: 'terminalFreezeAmount',displayName:'机具款预冻结金额',width:150}
			,{ field: 'otherFreezeAmount',displayName:'其他冻结金额',width:150}
			,{ field: 'fenFreezeAmount',displayName:'分润账户冻结金额',width:150}
			,{ field: 'activityFreezeAmount',displayName:'活动补贴账户冻结金额',width:180}
			,{ field: 'freezeAmount',displayName:'预冻金额',width:150}
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
					$scope.preliminaryFreezeQuery();
					rowList={};
			 });
		}
	};
});