
/**
 * 查询业务产品
 */
angular.module('inspinia').controller('merchantIncomeCtrl', function ($scope, $http, $state, $stateParams, i18nService) {
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.activityCodes = [{text: '云闪付', value: '5'}]
    $scope.pageNo = 1;
    $scope.pageSize = 10;
    $scope.info = {
        startCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
        endCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
    };
    isVerifyTime = 1;//校验：1，不校验：0
    setBeginTime=function(setTime){
        $scope.info.startCreateTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }

    setEndTime=function(setTime){
        $scope.info.endCreateTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }


    $scope.columns = [
        {field: 'id', displayName: '序号', pinnable: false, width: 180, sortable: false},
        {field: 'merchantNo', displayName: '商户编号', pinnable: false, width: 180, sortable: false},
        {field: 'merchantName', displayName: '商户名称', pinnable: false, width: 180, sortable: false},
        {field: 'mobilephone', displayName: '商户手机号', pinnable: false, width: 180, sortable: false},
        {field: 'transAmount', displayName: '交易金额', pinnable: false, width: 180, sortable: false},
        {field: 'transFee', displayName: '交易手续费', pinnable: false, width: 180, sortable: false},
        {field: 'discountFee', displayName: '计算优惠后手续费', pinnable: false, width: 180, sortable: false},
        {field: 'merchantProfit', displayName: '收益金额', pinnable: false, width: 180, sortable: false},
        {field: 'activityCode', displayName: '活动类型', pinnable: false, width: 180, sortable: false,cellFilter: "formatDropping:" + angular.toJson($scope.activityCodes)},
        {field: 'orderNo', displayName: '交易订单', pinnable: false, width: 180, sortable: false},
        {field: 'createTime', displayName: '时间', pinnable: false, width: 180, sortable: false, cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'}
    ];

    $scope.activityGrid = {
        paginationPageSize: 10,                  //分页数量
        paginationPageSizes: [10, 20, 50, 100],	  //切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar: true,  		//纵向滚动条
        columnDefs: $scope.columns,
        onRegisterApi: function (gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.pageNo = newPage;
                $scope.pageSize = pageSize;
                $scope.listMerchantIncome();
            });
            $scope.listMerchantIncome();
        }
    };

    //查询
    $scope.listMerchantIncome = function () {
        if (!($scope.info.startCreateTime && $scope.info.endCreateTime)) {
            $scope.notice("创建日期日期不能为空，请重新选择");
            return
        }

        // 获取券信息列表
        $http({
            url: 'activityDetail/listMerchantIncome?pageNo=' + $scope.pageNo + "&pageSize=" + $scope.pageSize,
            method: 'POST',
            data: $scope.info
        }).success(function(data){
            $scope.activityGrid.data = data.data || [];
            $scope.activityGrid.totalItems = data.count;
        }).error(function (data) {
            $scope.notice("服务器异常.");
        });
    };

    //reset
    $scope.resetForm = function () {
        $scope.info = {
            startCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
            endCreateTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
    };

});