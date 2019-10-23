angular.module('inspinia').controller('raRepayOrderCtrl',function($scope,$rootScope,$http,$state,$stateParams,$compile,$uibModal,$log,i18nService,SweetAlert){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.info = {
        agentNo:$scope.$root.entityId
    };
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
    $scope.repayStatusList = [
        {text: '还款成功', value: '3'},
        {text: '还款失败', value: '4'}
    ];

    $scope.paginationOptions = {
        pageNo: 1,
        pageSize: 10
    };

    $scope.defaultCount = {
        oemShare: '0',
        planAmount: '0',
        actualAmount: '0'
    };
    $scope.count = $scope.defaultCount;
    $scope.gridOptions={                           //配置表格
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,                //分页数量
        columnDefs:[                           //表格数据
            { field: 'orderNo',displayName:'订单ID',width:170},
            { field: 'orderStatus',displayName:'订单状态',width:100, cellFilter:"formatDropping:" + angular.toJson($scope.orderStatusList)},
            { field: 'repayStatus',displayName:'还款状态',width:100, cellFilter:"formatDropping:" + angular.toJson($scope.repayStatusList)},
            { field: 'merchantNo',displayName:'用户ID',width:100},
            { field: 'userName',displayName:'用户名称',width:150},
            { field: 'mobile',displayName:'用户手机号',width:150},
            { field: 'repayMerchantNo',displayName:'还款商户ID',width:150},
            { field: 'sourceOrderNo',displayName:'关联订单号',width:150},
            { field: 'planAmount',displayName:'目标还款金额',width:150},
            { field: 'actualAmount',displayName:'实际消费金额',width:150},
            { field: 'rate',displayName:'品牌发放总奖金扣率',width:180},
            { field: 'createTime',displayName:'创建时间',width:170},
            { field: 'completionTime',displayName:'订单完成时间',width:170},
            { field: 'agentNo',displayName:'所属代理商ID',width:170},
            { field: 'oemShare',displayName:'品牌分润',width:120},
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
            url: 'redemActiveOrderAction/listRepayOrder?pageNo=' + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
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

