/**
 * 交易查询
 * 页面上有个错误需要解决，否则看不到angular的效果,好像是页面不能响应angular指令
 */
angular.module('inspinia').controller('zqQueryMerchantCtrl',function($scope,$filter,$rootScope,$http,$state,$stateParams,$compile,$uibModal,$log,i18nService,SweetAlert){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文

	$scope.info = {
	    syncStatus: '',
        startCreateTime: $filter('date')(new Date(),'yyyy-MM-dd') + " 00:00:00",
        endCreateTime : $filter('date')(new Date(),'yyyy-MM-dd') + " 23:59:59"
	};
    $scope.pageNo = 1;
    $scope.pageSize = 10;

    $scope.gridOptions={                           //配置表格
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,
        columnDefs:[
            { field: 'unionpayMerNo',displayName:'银联报备商户编号',width:160},
            { field: 'bpId',displayName:'商户进件编号',width:160},
            { field: 'merchantNo',displayName:'商户编号',width:160},
            { field: 'merchantName',displayName:'商户名称',width:160},
            { field: 'syncStatus',displayName:'同步状态',width:100,
                cellFilter:"formatDropping:[{text:'初始化',value:0},{text:'同步成功',value:1},{text:'同步失败',value:2}]"},

            { field: 'mobilephone',displayName:'手机号',width:160},
            { field: 'syncRemark',displayName:'失败原因',width:160},
			{ field: 'createTime',displayName:'创建时间',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'}
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.pageNo = newPage;
                $scope.pageSize = pageSize;
                $scope.queryZQMerchantInfo();
            });
            $scope.queryZQMerchantInfo();
        }
    };



	//================ function =================
    $scope.exportZQMerchantInfoing = false;
    $scope.queryZQMerchantInfoing = false;
	$scope.queryZQMerchantInfo = function () {
        if($scope.info.startCreateTime && $scope.info.startCreateTime.format){
            $scope.info.startCreateTime = $scope.info.startCreateTime.format("YYYY-MM-DD HH:mm:ss");
        }
        if($scope.info.endCreateTime && $scope.info.endCreateTime.format){
            $scope.info.endCreateTime = $scope.info.endCreateTime.format("YYYY-MM-DD HH:mm:ss");
        }
        if($scope.info.startCreateTime > $scope.info.endCreateTime){
            $scope.notice("起始时间不能大于结束时间");
            return;
        }
        if($scope.queryZQMerchantInfoing){
            $scope.notice("正在查询数据,请不要频繁点击查询按钮.");
            return;
        }
        $scope.queryZQMerchantInfoing = true;
		$http({
			url: 'zqMerchantAction/queryZQMerchantInfo?pageNo=' + $scope.pageNo + "&pageSize=" + $scope.pageSize,
			method: 'POST',
			data: $scope.info
        }).success(function (data) {
            $scope.gridOptions.data = data.result;
            $scope.gridOptions.totalItems = data.totalCount;
            $scope.queryZQMerchantInfoing = false;
        }).error(function (data) {
            $scope.notice("服务器异常.");
            $scope.queryZQMerchantInfoing = false;
        })
    };

	$scope.clear = function () {
        $scope.info = {syncStatus: ''};
    };

	$scope.exportZQMerchantInfo = function () {
        if($scope.info.startCreateTime && $scope.info.startCreateTime.format){
            $scope.info.startCreateTime = $scope.info.startCreateTime.format("YYYY-MM-DD HH:mm:ss");
        }
        if($scope.info.endCreateTime && $scope.info.endCreateTime.format){
            $scope.info.endCreateTime = $scope.info.endCreateTime.format("YYYY-MM-DD HH:mm:ss");
        }
        if($scope.info.startCreateTime &&  $scope.info.endCreateTime &&
            $scope.info.startCreateTime > $scope.info.endCreateTime){
            $scope.notice("起始时间不能大于结束时间");
            return;
        }
        if($scope.exportZQMerchantInfoing){
            $scope.notice("正在导出数据,请不要频繁点击导出按钮.");
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
                $scope.exportZQMerchantInfoing = true;
                setTimeout(function () {
                    $scope.exportZQMerchantInfoing = false;
                }, 10000);
                location.href="zqMerchantAction/exportZQMerchantInfo?"+$.param($scope.info);
            }
        });
    }
});
