/**
 * 交易查询
 * 页面上有个错误需要解决，否则看不到angular的效果,好像是页面不能响应angular指令
 */
angular.module('inspinia').controller('raUserQueryCtrl',function($scope,$rootScope,$http,$state,$stateParams,$compile,$uibModal,$log,i18nService,SweetAlert){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.info = {};
    $scope.merAccountList = [
        {text: '否', value: '0'},
        {text: '是', value: '1'}
    ];
    $scope.merchantStatusList = [
        {text: '待进件', value: '1'},
        {text: '审核中', value: '2'},
        {text: '审核失败', value: '3'},
        {text: '审核通过', value: '4'}
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
            { field: 'merchantNo',displayName:'用户ID',width:170},
            { field: 'userName',displayName:'昵称',width:150},
            { field: 'mobileUsername',displayName:'手机号',width:150},
            { field: 'realName',displayName:'姓名',width:150},
            { field: 'createTime',displayName:'激活时间',width:150},
            { field: 'receiveMerchantNo',displayName:'收款商户ID',width:150},
            { field: 'repayMerchantNo',displayName:'还款商户ID',width:150},
            { field: 'oneAgentNo',displayName:'一级代理商ID',width:150},
            { field: 'agentNo',displayName:'直属代理商ID',width:150},
            { field: 'merchantStatus',displayName:'进件状态',width:150, cellFilter:"formatDropping:" + angular.toJson($scope.merchantStatusList)},
            { field: 'receiveCreateTime',displayName:'进件时间',width:150},
            {field: 'action',displayName: '操作',width: 120,pinnedRight:true,cellTemplate:
                    '<a class="lh30" ui-sref="redemption.raUserDetails({merchantNo:row.entity.merchantNo})" target="_black">详情</a>'
            }
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
            url: 'redemActiveMerchantAction/listMerchants?pageNo=' + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
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

    $scope.agentList = [];
    $http.post("agentInfo/selectAllInfo")
        .success(function(msg){
            for(var i=0; i<msg.length; i++){
                $scope.agentList.push({value:msg[i].agentNo,text:msg[i].agentNo + '-' + msg[i].agentName});
            }
        });
});

