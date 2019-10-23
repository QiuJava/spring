/**
 * 盟友活动返现明细
 */
angular.module('inspinia').controller('paCashBackDetailCtrl',function($scope,$rootScope,$http,$state,$stateParams,
		i18nService,$document,SweetAlert){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.entryStatusList = [{text:"未入账",value:"0"},{text:"已入账",value:"1"}];
	$scope.subjectTypes = [{text:'欢乐返-循环送',value:'008'},{text:'欢乐返',value:'009'}];
	$scope.paginationOptions = {pageNo : 1,pageSize : 10};
	$scope.baseInfo = {agentNo:''};
	$scope.resetForm = function () {
		$scope.baseInfo = {agentNo:''};
	}
	$scope.cashBackAmountTotal = 0;
    $scope.cashBackAmount0 = 0;
    $scope.cashBackAmount1 = 0;
    $scope.query = function () {
        $http({
            url: 'perAgent/selectPaCashBackDetail?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (msg) {
            if(msg.status){
            	$scope.cashBackAmountTotal = msg.cashBackAmountTotal;
                $scope.cashBackAmount0 = msg.cashBackAmount0;
                $scope.cashBackAmount1 = msg.cashBackAmount1;
                $scope.myGrid.data = msg.page.result;
                $scope.myGrid.totalItems = msg.page.totalCount;
            }else {
                $scope.notice(msg.msg);

            }
        }).error(function (msg) {
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    $scope.query();

	$scope.columnDefs = [
		{field: 'activeOrder',displayName: '激活订单号',width: 150,pinnable: false,sortable: false},
		{field: 'realName',displayName: '盟主姓名',width: 150,pinnable: false,sortable: false},
		{field: 'userCode',displayName: '盟主编号',width:150},
		{field: 'activityCode',displayName: '欢乐返类型',width:150,cellFilter:"formatDropping:" + angular.toJson($scope.subjectTypes)},
		{field: 'activityTypeNo',displayName: '欢乐返子类型',width:150},
		{field: 'transAmount',displayName: '交易金额(元)',width:150},
		{field: 'cashBackAmount',displayName: '返盟主金额(元)',width:150},
		{field: 'entryStatus',displayName: '返现入账状态',width:150, cellFilter:"formatDropping:" + angular.toJson($scope.entryStatusList)},
        {field: 'createTime',displayName: '激活日期',width: 180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'entryTime',displayName: '入账日期',width: 180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
	];
	$scope.myGrid = {
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10,20,50,100],	//切换每页记录数
		useExternalPagination: true,		  //开启拓展名
		enableHorizontalScrollbar: true,        //横向滚动条
		enableVerticalScrollbar : true,  		//纵向滚动条
		columnDefs: $scope.columnDefs,
		onRegisterApi: function(gridApi) {
			$scope.gridApi = gridApi;
			$scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
				$scope.paginationOptions.pageNo = newPage;
				$scope.paginationOptions.pageSize = pageSize;
				$scope.query();
			});
		}
	};

    //导出
	$scope.exportCashBackDetail=function(){
        if($scope.baseInfo.entryTimeStart > $scope.baseInfo.entryTimeEnd){
            $scope.notice("起始时间不能大于结束时间");
            return;
        }
        SweetAlert.swal({
                title: "确认导出？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                	location.href="perAgent/exportCashBackDetail?baseInfo="+encodeURIComponent(angular.toJson($scope.baseInfo));
                }
            });
    };
});