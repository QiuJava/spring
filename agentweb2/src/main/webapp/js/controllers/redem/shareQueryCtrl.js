/**
 * 交易查询
 * 页面上有个错误需要解决，否则看不到angular的效果,好像是页面不能响应angular指令
 */
angular.module('inspinia').controller('shareQueryCtrl',function($scope,$rootScope,$http,$state,$stateParams,$compile,$uibModal,$log,i18nService,SweetAlert){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.info = {};
    $scope.shareAmountTotal =0;
    $scope.accStatusList = [
        {text: '未记账', value: '0'},
        {text: '记账成功', value: '1'},
        {text: '记账失败', value: '2'},
        {text: '已提交记账', value: '3'}
    ];
    $scope.orderTypeList = [
        {text: '代理订单', value: 'A'},
        {text: '积分兑换', value: 'D'}
	];
    $scope.merCapaList = [
        {text: '用户', value: '1'},
        {text: '超级用户', value: '2'},
        {text: '会员', value: '3'},
        {text: '黄金会员', value: '4'},
        {text: '钻石会员', value: '5'}
    ];
    $scope.paginationOptions = {
        pageNo: 1,
        pageSize: 10
    };


    $scope.gridOptions={                           //配置表格
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,                //分页数量
        columnDefs:[                           //表格数据
            { field: 'id',displayName:'分润明细ID',width:170},
            { field: 'orderNo',displayName:'订单ID',width:170},
            { field: 'orderType',displayName:'订单类型',width:100, cellFilter:"formatDropping:" + angular.toJson($scope.orderTypeList)},
            { field: 'merchantNo',displayName:'贡献人ID',width:100},
            { field: 'userName',displayName:'贡献人昵称',width:150},
            { field: 'mobile',displayName:'贡献人手机号',width:150},
            { field: 'amount',displayName:'总金额',width:80},
            { field: 'profitMerchantNo',displayName:'收益人ID',width:100},
            { field: 'profitUserName',displayName:'收益人昵称',width:150},
            { field: 'profitMobile',displayName:'收益人手机号',width:150},
            { field: 'profitMerCapa',displayName:'收益人身份',width:150, cellFilter:"formatDropping:" + angular.toJson($scope.merCapaList)},
            { field: 'shareAmount',displayName:'收益人分润',width:150},
            { field: 'shareGrade',displayName:'当前分润层级',width:150},
            { field: 'accStatus',displayName:'记账状态',width:170, cellFilter:"formatDropping:" + angular.toJson($scope.accStatusList)},
            { field: 'createTime',displayName:'创建时间',width:170}
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
            url: 'redemOrder/listShareOrder?pageNo=' + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
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

        $http({
            url: 'redemOrder/listShareOrderSum?pageNo=' + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
            data: $scope.info,
            method: 'POST'
        }).success(function (data) {
            if(data.success){
                $scope.shareAmountTotal = data.data.shareAmountTotal;
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
});

