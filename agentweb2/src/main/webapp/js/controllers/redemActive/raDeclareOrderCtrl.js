/**
 * 交易查询
 * 页面上有个错误需要解决，否则看不到angular的效果,好像是页面不能响应angular指令
 */
angular.module('inspinia').controller('raDeclareOrderCtrl',function($scope,$rootScope,$http,$state,$stateParams,$compile,$uibModal,$log,i18nService,SweetAlert){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.info = {};
    $scope.checkStatusList = [
        {text: '审核中', value: '0'},
        {text: '审核成功', value: '1'},
        {text: '审核失败', value: '2'}
    ];
    $scope.accStatusList = [
        {text: '未记账', value: '0'},
        {text: '记账成功', value: '1'},
        {text: '记账失败', value: '2'},
        {text: '已提交记账', value: '3'}
    ];
    $scope.receiveStatusList = [
        {text: '未收到', value: '0'},
        {text: '已收到', value: '1'}
    ];
    $scope.orgCodeList = [];
    $scope.paginationOptions = {
        pageNo: 1,
        pageSize: 10
    };
    $scope.defaultCount = {
        oemShare: '0',
        amount: '0'
    };
    $scope.count = $scope.defaultCount;
    $scope.gridOptions={                           //配置表格
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,                //分页数量
        columnDefs:[                           //表格数据
            { field: 'orderNo',displayName:'订单ID',width:170},
            { field: 'checkStatus',displayName:'核销状态',width:100, cellFilter:"formatDropping:" + angular.toJson($scope.checkStatusList)},
            { field: 'orgName',displayName:'兑换机构',width:100},
            { field: 'merchantNo',displayName:'用户ID',width:100},
            { field: 'userName',displayName:'用户名称',width:150},
            { field: 'mobile',displayName:'用户手机号',width:150},
            { field: 'amount',displayName:'兑换价格',width:120},
            { field: 'oemShare',displayName:'品牌分润',width:120},
            { field: 'createTime',displayName:'创建时间',width:170},
            { field: 'checkTime',displayName:'核销时间',width:170},
            { field: 'checkOper',displayName:'核销人',width:170},
            { field: 'receiveStatus',displayName:'收货状态',width:120, cellFilter:"formatDropping:" + angular.toJson($scope.receiveStatusList)},
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

    $http({
        url: 'redemActiveOrderAction/listOrgCode',
        method: 'POST'
    }).success(function (data) {
        if (data.success){
            for (var item in data.data){
                $scope.orgCodeList.push({text: data.data[item]['org_name'], value: data.data[item]['org_code']});
            }
        }
    });
    $scope.query = function () {

        $http({
            url: 'redemActiveOrderAction/listDeclareOrder?pageNo=' + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
            data: $scope.info,
            method: 'POST'
        }).success(function (data) {
            if(data.success){
                $scope.gridOptions.data = data.data && data.data.list;
                $scope.gridOptions.totalItems = data.count;
                $scope.count = $.extend({}, $scope.defaultCount, data.data && data.data.count);
            }else{
                $scope.notice(data.message || "查询出错");
                $scope.count = $scope.defaultCount;
            }
        }).error(function () {
            $scope.notice("服务器异常,请稍后再试");
        });
    };

    $scope.clear = function () {
        $scope.info = {};
    };
});

