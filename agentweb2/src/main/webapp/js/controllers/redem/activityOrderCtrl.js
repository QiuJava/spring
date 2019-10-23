/**
 * 交易查询
 * 页面上有个错误需要解决，否则看不到angular的效果,好像是页面不能响应angular指令
 */
angular.module('inspinia').controller('activityOrderCtrl',function($scope,$rootScope,$http,$state,$stateParams,$compile,$uibModal,$log,i18nService,SweetAlert){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.info = {};
	$scope.orderStatusList = [
		{text: '初始化', value: 'INIT'},
		{text: '已提交', value: 'SUB'},
		{text: '成功', value: 'SUCCESS'},
		{text: '失败', value: 'FAILED'},
		{text: '取消', value: 'CANCEL'},
		{text: '未知', value: 'UNKNOW'}
	];
	$scope.accStatusList = [
        {text: '未记账', value: '0'},
        {text: '记账成功', value: '1'},
        {text: '记账失败', value: '2'},
        {text: '已提交记账', value: '3'}
	];
    $scope.paginationOptions = {
        pageNo: 1,
        pageSize: 10
	};
    $scope.defaultSummary = {
        oemShare: '0.00'
    };
    $scope.summary = $scope.defaultSummary;

    $scope.gridOptions={                           //配置表格
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,                //分页数量
        columnDefs:[                           //表格数据
            { field: 'orderNo',displayName:'订单ID',width:170},
            { field: 'orderStatus',displayName:'订单状态',width:100, cellFilter:"formatDropping:" + angular.toJson($scope.orderStatusList)},
            { field: 'merchantNo',displayName:'贡献人ID',width:100},
            { field: 'userName',displayName:'贡献人昵称',width:150},
            { field: 'mobile',displayName:'贡献人手机号',width:150},
            { field: 'amount',displayName:'售价',width:80},
            { field: 'provideAmout',displayName:'发放奖金',width:100},
            { field: 'createTime',displayName:'创建时间',width:170},
            { field: 'payTime',displayName:'支付时间',width:170},
            { field: 'payOrderNo',displayName:'关联支付订单',width:250},
            { field: 'oemShare',displayName:'品牌商分润',width:120},
            { field: 'accStatus',displayName:'记账状态',width:100, cellFilter:"formatDropping:" + angular.toJson($scope.accStatusList)}
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };


    $scope.query = function () {
        $http({
            url: 'redemOrder/summaryActivityOrder?pageNo=' + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
            data: $scope.info,
            method: 'POST'
        }).success(function (data) {
            if(data.success){
                $scope.summary = data.data;
            }else{
                $scope.summary = $scope.defaultSummary;
            }
        });

		$http({
			url: 'redemOrder/listActivityOrder?pageNo=' + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
			data: $scope.info,
            method: 'POST'
        }).success(function (data) {
            if(data.success){
                $scope.gridOptions.data = data.data;
                $scope.gridOptions.totalItems = data.count;
            }else{
                $scope.notice(data.message || "查询出错");
            }
        }).error(function () {
            $scope.notice("服务器异常,请稍后再试");
        });
    };

    $scope.clear = function () {
        $scope.info = {};
    };

    // $scope.exportInfo = function () {
    //     SweetAlert.swal({
    //             title: "确认导出？",
    //             showCancelButton: true,
    //             confirmButtonColor: "#DD6B55",
    //             confirmButtonText: "提交",
    //             cancelButtonText: "取消",
    //             closeOnConfirm: true,
    //             closeOnCancel: true
    //         },
    //         function (isConfirm) {
    //             if (isConfirm) {
    //                 location.href="redemOrder/exportActivityOrder?"+$.param($scope.info);
    //             }
    //         });
    // };
});