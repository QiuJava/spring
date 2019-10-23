/**
 * 系统管理-系统菜单 控制器
 * 
 * @author xyf1
 */

angular.module('inspinia').controller('invitePrizesMerchantCtrl',function($scope, $http, $state, $stateParams, i18nService, $compile, $filter, $uibModal, $log,uiGridConstants, $httpParamSerializer, $httpParamSerializerJQLike,SweetAlert) {
	i18nService.setCurrentLang('zh-cn');

	$scope.accountStatusSelect = [{
	    text: "未入账", value: '0'
    },{
        text: "已入账", value: '1'
    },{
        text: "入账失败", value: '2'
    }];

    $scope.agentList = [];
    $scope.paginationOptions = {
        pageNo : 1,
        pageSize : 10
    };
    $scope.baseInfo = {
        agentNo: $scope.$root.entityId,
        containSub: '1',
        accountStatus: ''
    };
    var defatltCountInvitePrizesMerchant = {
        sumAmount: "0.00",
        unrecordedAmount: '0.00',
        recordedAmount: '0.00'
    };
    $scope.countInvitePrizesMerchant = defatltCountInvitePrizesMerchant;

    //代理商
    $http.post("agentInfo/selectAllInfo")
        .success(function(msg){
            //响应成功
            for(var i=0; i<msg.length; i++){
                $scope.agentList.push({value:msg[i].agentNo,text:msg[i].agentName});
            }
        });

    $scope.columnDefs = [
        {field: 'merchantName',displayName: '邀请商户名称',width: 150,pinnable: false,sortable: false},
        {field: 'merchantNo',displayName: '邀请商户编号',width: 150,pinnable: false,sortable: false},
        {field: 'agentName',displayName: '所属代理商名称',width: 150,pinnable: false,sortable: false},
        {field: 'agentNo',displayName: '所属代理商编号',width: 140,pinnable: false,sortable: false},
        {field: 'prizesAmount',displayName: '奖励金额',width: 120,pinnable: false,sortable: false},
        {field: 'orderNo',displayName: '交易订单',width: 120,pinnable: false,sortable: false},
        {field: 'createTime',displayName: '创建时间',width: 150,pinnable: false,sortable: false,
            cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'accountStatus',displayName: '入账状态',width: 120,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.accountStatusSelect)},
        {field: 'accountTime',displayName: '入账时间',width: 150,pinnable: false,sortable: false,
            cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'}

    ];
    $scope.invitePrizesMerchantGrid = {
        data : "shareData",
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        rowHeight:40,
        columnDefs: $scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.queryInvitePrizesMerchant();
            });
        }
    };
    isVerifyTime = 1;//校验：1，不校验：0
    setStartCreateTime=function(setTime){
        $scope.baseInfo.startCreateTime= moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }
    setEndCreateTime=function(setTime){
        $scope.baseInfo.endCreateTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }
    $scope.queryInvitePrizesMerchant = function () {
        $http({
            url: 'invitePrizesMerchant/listInvitePrizesMerchant?pageNo='+$scope.paginationOptions.pageNo+'&size=' + $scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (msg) {
            if (!msg.success){
                $scope.notice(msg.message);
                return;
            }
            $scope.shareData = msg.data;
            $scope.invitePrizesMerchantGrid.totalItems = msg.count;
        }).error(function (msg) {
            $scope.notice('服务器异常,请稍后再试.');
        });
        $http({
            url: 'invitePrizesMerchant/countInvitePrizesMerchant',
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (msg) {
            if (!msg.success){
                $scope.countInvitePrizesMerchant = defatltCountInvitePrizesMerchant;
                return;
            }
            $scope.countInvitePrizesMerchant =  msg.data;
        }).error(function (msg) {
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    
    $scope.resetForm = function () {
        $scope.baseInfo = {
            agentNo: $scope.$root.entityId,
            containSub: '1',
            accountStatus: ''
        };
    }
});