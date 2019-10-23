/**
 * 交易查询
 * 页面上有个错误需要解决，否则看不到angular的效果,好像是页面不能响应angular指令
 */
angular.module('inspinia').controller('userQueryCtrl',function($scope,$rootScope,$http,$state,$stateParams,$compile,$uibModal,$log,i18nService,SweetAlert){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.info = {};
    $scope.merCapaList = [
        {text: '用户', value: '1'},
        {text: '超级用户', value: '2'},
        {text: '会员', value: '3'},
        {text: '黄金会员', value: '4'},
        {text: '钻石会员', value: '5'}
    ];
    $scope.merAccountList = [
        {text: '否', value: '0'},
        {text: '是', value: '1'}
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
            { field: 'userName',displayName:'昵称',width:150,},
            { field: 'mobileUsername',displayName:'手机号',width:150},
            { field: 'agentNo',displayName:'直属服务商',width:150},
            { field: 'merCapa',displayName:'代理身份',width:170,cellFilter:"formatDropping:" + angular.toJson($scope.merCapaList)},
            { field: 'merAccount',displayName:'是否开户',width:100, cellFilter:"formatDropping:" + angular.toJson($scope.merAccountList)},
            {field: 'createTime',displayName: '入驻时间',width: 170, cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'action',displayName: '操作',width: 120,pinnedRight:true,cellTemplate:
                    '<a class="lh30" ui-sref="redemption.userDetails({merchantNo:row.entity.merchantNo})" target="_black">详情</a>'  +
                    '<span ng-show="row.entity.directMerchant">' +
                        '<span ng-show="row.entity.notOpenAgent">' +
                            ' | <a class="lh30" ui-sref="redemption.userAddAgent({merchantNo:row.entity.merchantNo})" target="_black">开通代理商</a>' +
                        '</span>' +
                        '<span ng-show="!row.entity.notOpenAgent">' +
                            ' | <a class="lh30" ui-sref="redemption.userUpdateAgent({merchantNo:row.entity.merchantNo})" target="_black">修改代理商</a>' +
                        '</span>' +
                    '</span>'
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
            url: 'redemMerchant/selectByUserRedemMerchant?pageNo=' + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
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
            url: 'redemMerchant/selectSum?pageNo=' + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
            data: $scope.info,
            method: 'POST'
        }).success(function (data) {
            if(data.success){
                $scope.merInfoTotal = data.data;
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
    $scope.clearMerInfoTotal=function () {
        $scope.merInfoTotal={merTotal:0,ordmemTotal:0,supermemTotal:0,merActTotal:0,
            ordparTotal:0,goldparTotal:0,diamparTotal:0};
    };
    $scope.clearMerInfoTotal();

    $scope.agentList = [];
    $http.post("agentInfo/selectAllInfo")
        .success(function(msg){
            for(var i=0; i<msg.length; i++){
                $scope.agentList.push({value:msg[i].agentNo,text:msg[i].agentNo + '-' + msg[i].agentName});
            }
        });
});

