
/**
 * 查询业务产品
 */
angular.module('inspinia', ['angularFileUpload']).controller('giftsRecordCtrl', function ($scope, $http, $state, $stateParams, i18nService, $document, SweetAlert, FileUploader) {
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.pageNo = 1;
    $scope.pageSize = 10;
    $scope.queryInfo = {
        agentNo: "",
        startTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 00:00:00'),
        endTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'

    };
    //是否校验时间
    isVerifyTime = 1;//校验：1，不校验：0
    setBeginTime=function(setTime){
        $scope.queryInfo.startTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }



    setEndTime=function(setTime){
        $scope.queryInfo.endTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }
    $scope.countUserCoupon = {
        faceValue: 0,
        balance: 0,
        usedMoney: 0,
        overdueMoney: 0
    };
    $scope.dictList = {};
    if ($scope.oemType === 'ZHZFPAY'){
        $scope.dictList['COUPON_CODE'] = [
            {text: "注册返", value: 1},
            {text: "签到返", value: 2}
            // {text: "充值返", value: 3},
            // {text: "邀请有奖", value: 4},
            // {text: "云闪付", value: 5}
        ];
    }else if ($scope.oemType === 'PERAGENT'){
        $scope.dictList['COUPON_CODE'] = initData['COUPON_CODE'];
        $scope.dictList['COUPON_CODE'].splice(3,1)
    }else if ($scope.oemType === 'YLSTCZB'){
        $scope.dictList['COUPON_CODE'] = [
            {text: "注册返", value: 1},
            // {text: "签到返", value: 2},
            // {text: "充值返", value: 3},
            // {text: "邀请有奖", value: 4},
            {text: "云闪付", value: 5}
        ];
    }else{
        $scope.dictList['COUPON_CODE'] = initData['COUPON_CODE'];
    }
    // [注意] 在增删或者调整列的时候,请注意修改 replaceSysDict("COUPON_STATUS", 9)
    $scope.columns = [
        {field: 'id', displayName: '序号', pinnable: false, width: 180, sortable: false},
        {field: 'merchantNo', displayName: '商户编号', pinnable: false, width: 180, sortable: false},
        {field: 'merchantName', displayName: '商户名称', pinnable: false, width: 180, sortable: false},
        {field: 'mobilephone', displayName: '商户手机号', pinnable: false, width: 180, sortable: false},
        {field: 'agentName', displayName: '所属代理商', pinnable: false, width: 180, sortable: false},
        {field: 'couponCode', displayName: '参与活动', pinnable: false, width: 180, sortable: false, cellFilter:  'formatDropping:'+angular.toJson($scope.dictList['COUPON_CODE'])},
        {field: 'faceValue', displayName: '赠送金额', pinnable: false, width: 180, sortable: false},
        {field: 'balance', displayName: '可用金额', pinnable: false, width: 180, sortable: false},
        {field: 'usedMoney', displayName: '已使用金额', pinnable: false, width: 180, sortable: false},
        // {field: 'overdueMoney', displayName: '过期金额', pinnable: false, width: 180, sortable: false},
        {field: 'couponStatus', displayName: '使用状态', pinnable: false, width: 180, sortable: false},
        {field: 'startTime', displayName: '赠送日期', pinnable: false, width: 180, sortable: false},
        {field: 'endTime', displayName: '失效日期', pinnable: false, width: 180, sortable: false},
        {field: 'couponNo', displayName: '券编号', pinnable: false, width: 180, sortable: false}
    ];
    replaceSysDict("COUPON_STATUS", 9);
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
                $scope.listUserCoupons();
            });
            $scope.listUserCoupons();
        }
    };



    //代理商下拉列表查询
    $scope.agent = [{text: "全部", value: ""}];
    $http.post("agentInfo/selectAllInfo")
        .success(function (msg) {
            for (var i = 0; i < msg.length; i++) {
                $scope.agent.push({value: msg[i].agentNode, text: msg[i].agentNo + " " + msg[i].agentName});
            }
        });

    /**
     * 表格替换数据字段的方法
     * @param keyName       数据字典的sysey
     * @param columnsIndex  ui-grid 列的序号
     */
    function replaceSysDict(keyName, columnsIndex) {
        $http.post("sysDict/listSysDictGroup.do?keyName="+keyName)
            .success(function (msg) {
                $scope.dictList[keyName] = [];
                if(msg['success'] && msg['data']){
                    angular.forEach(msg['data'], function (item) {
                        $scope.dictList[keyName].push({
                            text: item['sysName'],
                            value: item['sysValue']
                        })
                    });
                    var tempColumns = angular.copy($scope.columns[columnsIndex]);
                    tempColumns['cellFilter'] = 'formatDropping:'+angular.toJson($scope.dictList[keyName]);
                    $scope.columns[columnsIndex] = tempColumns;
                }
            });
    }

    //查询
    $scope.listUserCoupons = function () {
        if($scope.queryInfo.startTime && $scope.queryInfo.startTime.format){
            $scope.queryInfo.startTime = $scope.queryInfo.startTime.format("YYYY-MM-DD");
        }
        if($scope.queryInfo.endTime && $scope.queryInfo.endTime.format){
            $scope.queryInfo.endTime = $scope.queryInfo.endTime.format("YYYY-MM-DD");
        }
        if (!($scope.queryInfo.startTime && $scope.queryInfo.startTime)){
            $scope.notice("赠送日期不能为空，请重新选择");
            return;
        }
        if($scope.queryInfo.startTime && $scope.queryInfo.startTime &&
            $scope.queryInfo.startTime > $scope.queryInfo.endTime){
            $scope.notice("开始日期不能大于结束日期.");
            return;
        }
        // 获取券的统计信息
        $http({
            url: "activityDetail/countUserCoupons",
            method: 'POST',
            data: $scope.queryInfo
        }).success(function (msg) {
            if(msg.success){
                $scope.countUserCoupon = msg.data;
            }
        });
        // 获取券信息列表
        $http({
            url: 'activityDetail/listUserCoupons?pageNo=' + $scope.pageNo + "&pageSize=" + $scope.pageSize,
            method: 'POST',
            data: $scope.queryInfo
        }).success(function(data){
            $scope.activityGrid.data = data.data || [];
            $scope.activityGrid.totalItems = data.count;
        }).error(function (data) {
            $scope.notice("服务器异常.");
        });

    };
    //reset
    $scope.resetForm = function () {
        $scope.queryInfo = {
            agentNo: "",
            startTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 00:00:00',
            endTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
    };

});