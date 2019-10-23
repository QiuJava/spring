/**
 * 系统管理-系统菜单 控制器
 * 
 * @author xyf1
 */

angular.module('inspinia').controller('superPushShareQueryCtrl',function($scope, $http, $state, $stateParams, i18nService, $compile, $filter, $uibModal, $log,uiGridConstants, $httpParamSerializer, $httpParamSerializerJQLike,SweetAlert) {
	i18nService.setCurrentLang('zh-cn');

	$scope.shareStatusSelect = [{
	    text: "未入账", value: '0'
    },{
        text: "已入账", value: '1'
    },{
        text: "入账失败", value: '2'
    }];
	$scope.shareTypeSelect = [{
	    text: "一级代理商分润", value: '0'
    },{
        text: "直属代理商分润", value: '1'
    }
    // ,{
    //     text: "一级分润", value: '2'
    // },{
    //     text: "二级分润", value: '3'
    // },{
    //     text: "三级分润", value: '4'
    // }
    ];

    $scope.paginationOptions = {
        pageNo : 1,
        pageSize : 10
    };
    $scope.baseInfo = {
        agentNo: $scope.$root.entityId,
        containSub: '1',
        shareType: '',
        shareStatus: ''
    };
    var defaultCountShare = {
        transAmount: "0.00",
        shareAmount: "0.00",
        unrecordedAmount: '0.00',
        recordedAmount: '0.00',
        orderCount: 0
    };
    $scope.countShare = defaultCountShare;
    // $scope.agentList = [];
    //代理商
    // $http.post("agentInfo/selectAllInfo")
    //     .success(function(msg){
    //         //响应成功
    //         for(var i=0; i<msg.length; i++){
    //             $scope.agentList.push({value:msg[i].agentNo,text:msg[i].agentName});
    //         }
    //     });

    $scope.columnDefs = [
        {field: 'createTime',displayName: '分润创建时间',width: 150,pinnable: false,sortable: false,
            cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
        {field: 'shareAmount',displayName: '分润金额',width: 120,pinnable: false,sortable: false},
        {field: 'shareType',displayName: '分润级别',width: 120,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.shareTypeSelect)},
        {field: 'shareRate',displayName: '分润百分比',width: 120,pinnable: false,sortable: false},
        {field: 'shareName',displayName: '分润代理商名称',width: 130,pinnable: false,sortable: false},
        {field: 'shareNo',displayName: '分润代理商编号',width: 120,pinnable: false,sortable: false},
        {field: 'transAmount',displayName: '交易金额',width: 120,pinnable: false,sortable: false},
        {field: 'orderNo',displayName: '交易订单号',width: 120,pinnable: false,sortable: false},
        {field: 'merchantNo',displayName: '交易商户编号',width: 130,pinnable: false,sortable: false},
        {field: 'shareStatus',displayName: '入账状态',width: 120,pinnable: false,sortable: false,
            cellFilter:"formatDropping:"+ angular.toJson($scope.shareStatusSelect)},
        {field: 'shareTime',displayName: '入账时间',width: 150,pinnable: false,sortable: false,
            cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'}
    ];
    $scope.shareGrid = {
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
                $scope.querySuperPushShare();
            });
        }
    };

    isVerifyTime = 1;//校验：1，不校验：0
    setStartShareTime=function(setTime){
        $scope.baseInfo.startShareTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }

    setEndShareTim=function(setTime){
        $scope.baseInfo.endShareTim = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }
    setStartCreateTime=function(setTime){
        $scope.baseInfo.startCreateTime= moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }
    setEndCreateTime=function(setTime){
        $scope.baseInfo.endCreateTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }
    $scope.exportSuperPushShare = function () {
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
                    location.href="superPushAction/exportSuperPushShare?"+$.param($scope.baseInfo);
                }
            });
    };
    $scope.querySuperPushShare = function () {
        $http({
            url: 'superPushAction/listSuperPushShare?pageNo='+$scope.paginationOptions.pageNo+'&size=' + $scope.paginationOptions.pageSize,
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (msg) {
            if (!msg.success){
                $scope.notice(msg.message);
                return;
            }
            $scope.shareData = msg.data;
            $scope.shareGrid.totalItems = msg.count;
        }).error(function (msg) {
            $scope.notice('服务器异常,请稍后再试.');
        });
        $http({
            url: 'superPushAction/countSuperPushShare',
            data: $scope.baseInfo,
            method:'POST'
        }).success(function (msg) {
            if (!msg.success){
                $scope.countShare = defaultCountShare;
                return;
            }
            $scope.countShare =  msg.data;
        }).error(function (msg) {
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
    
    $scope.resetForm = function () {
        $scope.baseInfo = {
            agentNo: $scope.$root.entityId,
            containSub: '1',
            shareType: '',
            shareStatus: ''
        };
    }
});