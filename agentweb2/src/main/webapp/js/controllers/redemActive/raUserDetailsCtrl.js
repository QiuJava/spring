/**
 * 交易查询
 * 页面上有个错误需要解决，否则看不到angular的效果,好像是页面不能响应angular指令
 */
angular.module('inspinia').controller('raUserDetailsCtrl', function ($scope, $rootScope, $http, $state, $stateParams, $compile, $uibModal, $log, i18nService, SweetAlert) {
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    var merchantNo = $state.params.merchantNo;
    $scope.merchantStatusList = {"1": "待进件", "2": "审核中", "3": "审核失败", "4": "审核通过"};

    $http({
        url: 'redemActiveMerchantAction/queryMerchantDetails?merchantNo=' + $state.params.merchantNo,
        method:'POST'
    }).success(function (data) {
        if (data.success){
            $scope.merchant = data.data;
            $scope.merchant.merchant_status = $scope.merchant && $scope.merchantStatusList[$scope.merchant.merchant_status];
        }else{
            $scope.notice(data.message);
        }
    });


    $scope.paginationOptions = {
        pageNo: 1,
        pageSize: 10
    };
    $scope.serviceList = [
        {text: '冲正', value: 'chargeback'},
        {text: '激活分润', value: 'actshare'},
        {text: '报单分润', value: 'delcshare'},
        {text: '提现', value: 'cash'},
        {text: '报单', value: 'delcsale'}
    ];
    $scope.gridOptions={                           //配置表格
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,                //分页数量
        columnDefs:[                           //表格数据
            { field: 'create_time',displayName:'记账时间',width:170},
            { field: 'service',displayName:'操作',width:170, cellFilter:"formatDropping:" + angular.toJson($scope.serviceList)},
            { field: 'amount',displayName:'金额',width:100},
            { field: 'amount_behind',displayName:'可用金额',width:100}
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
    $scope.info = {
        startTime: '',
        endTime: '',
        service: ''
    };
    $scope.query = function () {
        $http({
            url: 'redemActiveMerchantAction/listBalanceHis',
            data: {
                pageNo: $scope.paginationOptions.pageNo,
                pageSize: $scope.paginationOptions.pageSize,
                startTime: $scope.info.startTime,
                endTime: $scope.info.endTime,
                service: $scope.info.service,
                merchantNo: $state.params.merchantNo
            },
            method: 'POST'
        }).success(function (data) {
            if(data.success){
                $scope.gridOptions.data = data.data;
                $scope.gridOptions.totalItems = data.count;
                if (data.count == 0){
                    $scope.notice("查询不到数据");
                }
            }else{
                $scope.notice(data.message || "查询出错");
            }
        }).error(function () {
            $scope.notice("服务器异常,请稍后再试");
        });
    };

    $scope.clear = function () {
        $scope.info = {
            startTime: '',
            endTime: '',
            service: ''
        };
    };
});

