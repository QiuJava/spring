/**
 * 交易查询
 * 页面上有个错误需要解决，否则看不到angular的效果,好像是页面不能响应angular指令
 */
angular.module('inspinia').controller('raShareQueryCtrl',function($scope,$rootScope,$http,$state,$stateParams,$compile,$uibModal,$log,i18nService,SweetAlert){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.info = {
        agentNo:$scope.$root.entityId
    };
    $scope.accStatusList = [
        {text: '未记账', value: '0'},
        {text: '记账成功', value: '1'},
        {text: '记账失败', value: '2'},
        {text: '已提交记账', value: '3'}
    ];
    $scope.orderTypeList = [
        {text: '积分兑换', value: 'D'},
        {text: '商户收款', value: 'O'},
        {text: '信用卡还款', value: 'R'}
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
            { field: 'orderNo',displayName:'订单ID',width:170},
            { field: 'merchantNo',displayName:'用户ID',width:120},
            { field: 'userName',displayName:'用户名称',width:150},
            { field: 'mobile',displayName:'用户手机号',width:150},
            { field: 'orderType',displayName:'订单类型',width:100, cellFilter:"formatDropping:" + angular.toJson($scope.orderTypeList)},
            { field: 'amount',displayName:'交易金额',width:80},
            { field: 'shareAmount',displayName:'分润金额',width:150},
            { field: 'agentNo',displayName:'服务商ID',width:150},
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
            url: 'redemActiveOrderAction/listShareOrder?pageNo=' + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
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
        $scope.info = {
            agentNo:$scope.$root.entityId
        };
    };


    $scope.agentList = [];
    $http.post("agentInfo/selectAllInfo")
        .success(function(msg){
            for(var i=0; i<msg.length; i++){
                $scope.agentList.push({value:msg[i].agentNo,text:msg[i].agentNo + '-' + msg[i].agentName});
            }
        });
});

